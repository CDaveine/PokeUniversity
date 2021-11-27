#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "clientbrc.h"
#include "clientTCP.h"
#include "color.h"

#define MAX 20
#define SIZE 500
#define BROADCAST_PORT 9000
#define TCP_PORT 9001

static void get_msg(const char *need, char *msg){
    printf("%s \n", need);
    fgets(msg, SIZE, stdin);
}

static void prompt_server_list(struct sockaddr_in **servers, int nbserv, char *buffer_send){
    int iserv;
    do{
        system("clear");
        for (int i = 0; i < nbserv; i++)
        {
            printf("Serveurd %d: %s\n", i, inet_ntoa(servers[i]->sin_addr));
        }

        // choose the server
        printf("%s Exit the game ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
        printf("%s Refresh server list ", color_text(BLACK, LIGHT_GRAY, "[refresh]"));
        get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[0...]"), " Choose server"), buffer_send);

        iserv = atoi(buffer_send);
    }while (strncmp(buffer_send, "exit", 4) && strncmp(buffer_send, "refresh", 7) && (iserv < 0 || iserv >= nbserv));
}

static void prompt_party_list(struct clientTCP *cltTCP, char **lparty, int *nbparty, char *buffer_recv, char *buffer_send, int bufsize){
    int iparty;

    do{
        cltTCP->client_send_tcp(cltTCP, "require game list");
        cltTCP->client_receive_tcp(cltTCP, buffer_recv, bufsize);

        *nbparty = atoi(buffer_recv);
        lparty = (char **) malloc(*nbparty * sizeof(char *));

        system("clear");
        for (int i = 0; i < *nbparty; i++)
        {
            cltTCP->client_receive_tcp(cltTCP, buffer_recv, bufsize);

            strtok(buffer_recv, " ");
            lparty[i] = strtok(buffer_recv, " ");
        }

        printf("%s Return to servers list ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
        printf("%s Refresh the party list ", color_text(BLACK, LIGHT_GRAY, "[refresh]")); // it could be everything because I've make the choice to don't stock the nbplayer
        if(*nbparty != 0){
            printf("%s Create new party ", color_text(BLACK, LIGHT_GRAY, "[create]"));
            get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[0...]"), " Choose a party"), buffer_send);
        }
        else{
            get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[create]"), " Create new party"), buffer_send);
        }

        iparty = atoi(buffer_send);
    }while (strncmp(buffer_send, "exit", 4) && strncmp(buffer_send, "create", 4) && (iparty < 0 || iparty >= *nbparty));
}

static void launch_game(struct clientTCP *cltTCP, char *buffer_send, char *buffer_recv, int bufsize){
    int nbrow, nbcol, nbrecv = 0;
    char *temp = (char *) malloc(25 * sizeof(char));

    int n = cltTCP->client_receive_tcp(cltTCP, buffer_recv, bufsize);    
    strcpy(temp, strtok(buffer_recv, "\n"));
    printf("n %d\n", n);
    
    if(!strncmp(temp, "map", 3)){
        temp = strtok(temp, " "); // pass map

        temp = strtok(NULL, " ");
        nbrow = atoi(temp);

        temp = strtok(NULL, " ");
        nbcol = atoi(temp);

        temp = strtok(buffer_recv, "\n"); // pass the first line
        do{
            while ((temp = strtok(NULL, "\n")) != NULL)
            {
                printf("%s\n", temp);
                nbrecv++;
            }
            cltTCP->client_receive_tcp(cltTCP, buffer_recv, bufsize);
        }while(nbrecv != nbrow);
        
        printf("%s Return to party list ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
        printf("%s Go up ", color_text(BLACK, LIGHT_GRAY, "[z"));
        printf("%s Go down ", color_text(BLACK, LIGHT_GRAY, "[s]"));
        printf("%s Go left ", color_text(BLACK, LIGHT_GRAY, "[q]"));
        get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[d]"), " Go right"), buffer_send);
    }

    switch (buffer_send[0])
    {
    case 'z':
        cltTCP->client_send_tcp(cltTCP, "map move up");
        break;

    case 's':
        cltTCP->client_send_tcp(cltTCP, "map move down");
        break;

    case 'q':
        cltTCP->client_send_tcp(cltTCP, "map move left");
        break;
    case 'd':
        cltTCP->client_send_tcp(cltTCP, "map move right");
        break;

    default:
        break;
    }
    
    free(temp);
}

int main(int argc, char const *argv[])
{
    Clientbrc clt = client_create_broadcast(BROADCAST_PORT);
    ClientTCP cltTCP;
    struct sockaddr_in **servers, *server;
    int nbserv, nbparty, iserv, iparty;
    char buffer_send[SIZE];
    char buffer_recv[SIZE];
    char **lparty, *temp;

    for (;;)
    {
        clt->client_send_broadcast(clt, "looking for poketudiant servers");
        servers = clt->client_receive_servers(clt, "i'm a poketudiant server", buffer_recv, SIZE, &nbserv);

        if(nbserv != 0){ 
            // prompt servers
            prompt_server_list(servers, nbserv, buffer_send);
            iserv = atoi(buffer_send);
            if (!strncmp(buffer_send, "exit", 4))
            {
                break;
            }
            else if(iserv < nbserv)
            {
                server = servers[iserv];
                cltTCP = client_create_tcp(inet_ntoa(servers[iserv]->sin_addr), TCP_PORT);

                if(connect(cltTCP->sock, (struct sockaddr *) &cltTCP->addr,cltTCP->len)==-1){
                    perror("connection failed");
                    exit(1);
                }
                
                // show party list
                do{
                    prompt_party_list(cltTCP, lparty, &nbparty, buffer_recv, buffer_send, SIZE);
                    iparty = atoi(buffer_recv);

                    if(!strncmp(buffer_send, "create", 6))
                    {
                        // create party
                        system("clear");
                        get_msg("Enter a party name: ", buffer_send);
                        temp = (char *) malloc((13+strlen(buffer_send)) * sizeof(char));
                        strcpy(temp, "create game ");
                        strcat(temp, buffer_send);
                        cltTCP->client_send_tcp(cltTCP, temp);
                        cltTCP->client_receive_tcp(cltTCP, buffer_recv, SIZE);

                        if(!strncmp(buffer_recv, "game created", 12)){
                            launch_game(cltTCP, buffer_send, buffer_recv, SIZE);
                        }
                        else{
                            free(lparty);
                            continue; // return to party list
                        }

                    }else if(iparty >= 0 &&  iparty < nbparty)
                    {
                        // join party
                        strcpy(buffer_send, "join game ");
                        strcat(buffer_send, lparty[iparty]);

                        cltTCP->client_send_tcp(cltTCP, buffer_send);
                        cltTCP->client_receive_tcp(cltTCP, buffer_recv, SIZE);

                        if(!strncmp(buffer_recv, "game joined", 11)){
                            // launch game
                        }
                        else{
                            free(lparty);
                            continue; // return to party list
                        }
                    }
                }while (strncmp(buffer_send, "exit", 4));

                continue; // return to server list
                
            }
        }
        else{
            do{
                system("clear");
                printf("No server found\n");
                printf("%s ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
                get_msg(color_text(BLACK, LIGHT_GRAY, "[refresh]"), buffer_send);
            }while (strncmp(buffer_send, "exit", 4) && strncmp(buffer_send, "refresh", 7));

            if (!strncmp(buffer_send, "exit", 4))
            {
                break;
            }
        }
    }
    
    for (int i = 0; i < nbserv; i++)
    {
        free(servers[i]);
    }
    free(servers);

    clientbrc_close_and_free(clt);

    return 0;
}
