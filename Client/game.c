#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <wait.h>

#include "clientTCP.h"
#include "color.h"

void get_msg(const char *need, char *msg){
    printf("%s \n", need);
    fgets(msg, 500, stdin);
}

void prompt_server_list(struct sockaddr_in **servers, int nbserv, char *buffer_send){
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

void prompt_party_list(struct clientTCP *cltTCP, char **lparty, int *nbparty, char *buffer_recv, char *buffer_send, int bufsize){
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

static void launch_team(){
    system("gnome-terminal -e ./team");
    perror("failed launch team interface");
    exit(1);
}

static void launch_tchat(){
    system("gnome-terminal -e ./team");
    perror("failed launch tchat interface");
    exit(1);
}

static void print_map(struct clientTCP *cltTCP, char *buffer_recv, char *buffer_send, char *temp){
    int nbrow, nbcol;
    char *map;

    temp = strtok(buffer_recv, " "); // pass map

    temp = strtok(NULL, " ");
    nbrow = atoi(temp);

    temp = strtok(NULL, " ");
    nbcol = atoi(temp);

    map = (char *) malloc(((nbrow+1) * nbcol) * sizeof(char));
    cltTCP->client_receive_tcp(cltTCP, map, (nbrow+1) * nbcol);

    system("clear");
    printf("%s", map);
    
    printf("%s Return to party list ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
    printf("%s Go up ", color_text(BLACK, LIGHT_GRAY, "[z]"));
    printf("%s Go down ", color_text(BLACK, LIGHT_GRAY, "[s]"));
    printf("%s Go left ", color_text(BLACK, LIGHT_GRAY, "[q]"));
    get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[d]"), " Go right"), buffer_send);
    
    if (strncmp(buffer_recv, "exit", 4))
    {
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
    }

    free(map);
}

static void print_team(char *buffer_recv, char *temp, int fifoTeam){
    int nbetu;

    temp = strtok(buffer_recv, " "); // pass team
    temp = strtok(NULL, " "); // pass contains
    temp = strtok(NULL, " "); // get N
    nbetu = atoi(temp);

    write(fifoTeam, buffer_recv, strlen(buffer_recv));
}

void launch_game(struct clientTCP *cltTCP, char *buffer_send, char *buffer_recv, int bufsize){
    int ibuff, fifoTeam, fifoTchat;
    char *temp;
    pid_t pidTeam, pidTchat;

    unlink("Team.fifo");
    unlink("Tchat.fifo");

    if(mkfifo("Team.fifo", 0777) == -1){
        perror("error create Team.fifo");
        exit(1);
    }
    
    if(mkfifo("Tchat.fifo", 0777) == -1){
        perror("error create Tchat.fifo");
        exit(1);
    }

    fifoTeam = open("Team.fifo", O_WRONLY);

    fifoTchat = open("Tchat.fifo", O_WRONLY);

    if(!(pidTeam = fork())){
        launch_team();
    }

    if(!(pidTchat = fork())){
        launch_tchat();
    }

    do
    {
        ibuff = 0;
        do
        {
            read(cltTCP->sock, &buffer_recv[ibuff], sizeof(char));
            ibuff++;
        }while (buffer_recv[ibuff]!='\n');
        buffer_recv[ibuff+1]='\0';

        printf("%s", buffer_recv);
        temp = (char *) malloc((ibuff+1)*sizeof(char));

        if(!strncmp(buffer_recv, "map", 3)){
            print_map(cltTCP, buffer_recv, buffer_send, temp);
        }
        else if (!strncmp(buffer_recv, "team contains", 13))
        {
            print_team(buffer_recv, temp, fifoTeam);
        }
        free(temp);
        
    } while (strncmp(buffer_recv, "exit", 4));

    close(fifoTeam);
    close(fifoTchat);
}