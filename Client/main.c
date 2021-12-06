#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "clientbrc.h"
#include "clientTCP.h"
#include "game.h"
#include "color.h"

#define MAX 20
#define SIZE 500
#define BROADCAST_PORT 9000
#define TCP_PORT 9001


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
                    lparty = prompt_party_list(cltTCP, &nbparty, buffer_recv, buffer_send, SIZE);
                    iparty = atoi(buffer_send);

                    if(!strncmp(buffer_send, "create", 6))
                    {
                        // create party
                        system("clear");
                        get_msg("Enter a party name: ", buffer_send);
                        temp = (char *) malloc((13+strlen(buffer_send)) * sizeof(char));
                        strcpy(temp, "create game ");
                        strcat(temp, buffer_send);
                        temp[strlen(buffer_recv)] = '\0';

                        cltTCP->client_send_tcp(cltTCP, temp);
                        cltTCP->client_receive_tcp(cltTCP, buffer_recv, SIZE);

                        if(!strncmp(buffer_recv, "game created", 12)){
                            launch_game(cltTCP, buffer_send, buffer_recv, SIZE);
                        }
                        else{
                            for (int i = 0; i < nbparty; i++)
                            {
                                free(lparty[i]);
                            }
                            
                            free(lparty);
                            continue; // return to party list
                        }

                    }else if(iparty >= 0 &&  iparty < nbparty)
                    {
                        // join party
                        strcpy(buffer_send, "join game ");
                        strncat(buffer_send, lparty[iparty], strlen(lparty[iparty]));

                        cltTCP->client_send_tcp(cltTCP, buffer_send);
                        cltTCP->client_receive_tcp(cltTCP, buffer_recv, SIZE);

                        if(!strncmp(buffer_recv, "game joined", 11)){
                            launch_game(cltTCP, buffer_send, buffer_recv, SIZE);
                        }
                        else{
                            for (int i = 0; i < nbparty; i++)
                            {
                                free(lparty[i]);
                            }
                            
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
