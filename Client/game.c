#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <pthread.h>
#include <stdbool.h>
#include <sys/select.h>

#include "clientTCP.h"
#include "color.h"

// private global var for threads
bool isExit;
bool isRunning;

void get_msg(const char *need, char *msg){
    printf("%s \n", need);
    fgets(msg, 500, stdin);
}

void prompt_server_list(struct sockaddr_in **servers, int nbserv, char *buffer_send){
    int iserv =-1;
    do{
        system("clear");
        for (int i = 0; i < nbserv; i++)
        {
            printf("Server %d: %s\n", i, inet_ntoa(servers[i]->sin_addr));
        }

        // choose the server
        printf("%s Exit the game ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
        printf("%s Refresh server list ", color_text(BLACK, LIGHT_GRAY, "[refresh]"));
        get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[0...]"), " Choose server"), buffer_send);
        iserv = atoi(buffer_send);
        if(!iserv && buffer_send[0] != '0')
        {
            iserv = -1;
        }
    }while(strncmp(buffer_send, "exit", 4) && strncmp(buffer_send, "refresh", 7) && (iserv < 0 || iserv >= nbserv));
}

char ** prompt_party_list(struct clientTCP *cltTCP, int *nbparty, char *buffer_recv, char *buffer_send, int bufsize){
    int iparty, ibuff, nbplayer, size;
    char *temp, pname[25], **lparty;

    lparty = NULL;
    do{
        system("clear");
        cltTCP->client_send_tcp(cltTCP, "require game list\n");
        
        ibuff = 0;
        do
        {
            read(cltTCP->sock, &buffer_recv[ibuff], sizeof(char));
            ibuff++;
        }while (buffer_recv[ibuff-1]!='\n');
        buffer_recv[ibuff] = '\0';

        sscanf(buffer_recv, "number of games %d\n", nbparty);

        if(lparty!=NULL){
            for (int i = 0; i < *nbparty; i++)
            {
                free(lparty[i]);
            }
            
            free(lparty);
        }
        lparty = (char **) malloc(*nbparty * sizeof(char *));

        if(*nbparty != 0){
            size = cltTCP->client_receive_tcp(cltTCP, buffer_recv, bufsize);
            buffer_recv[size]='\0';

            if(*nbparty != 1){
                temp = strtok(buffer_recv, "\n");
                for (int i = 0; i < *nbparty; i++)
                {
                    temp = strtok(NULL, "\n");
                    printf("party %d: %s\n", i, temp);
                    sscanf(temp, "%d %s", &nbplayer, pname);
                    lparty[i] = (char *) malloc((strlen(pname)+1)*sizeof(char));
                    strcpy(lparty[i], pname);
                    lparty[i][strlen(pname)] = '\n';
                }
            }
            else
            {
                printf("party 0: %s", buffer_recv);
                sscanf(buffer_recv, "%d %s", &nbplayer, pname);
                lparty[0] = (char *) malloc((strlen(pname)+1)*sizeof(char));
                strcpy(lparty[0], pname);
                lparty[0][strlen(pname)] = '\n';
            }

            printf("%s Return to servers list ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
            printf("%s Refresh the party list ", color_text(BLACK, LIGHT_GRAY, "[refresh]")); // it could be everything because I've make the choice to don't stock the nbplayer
            printf("%s Create new party ", color_text(BLACK, LIGHT_GRAY, "[create]"));
            get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[0...]"), " Choose a party"), buffer_send);
            iparty = atoi(buffer_send);

            if(!iparty && buffer_send[0] != '0')
            {
                iparty = -1;
            }
        }
        else{
            printf("no parties found\n");
            printf("%s Return to servers list ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
            printf("%s Refresh the party list ", color_text(BLACK, LIGHT_GRAY, "[refresh]"));
            get_msg(strcat(color_text(BLACK, LIGHT_GRAY, "[create]"), " Create new party"), buffer_send);
            iparty = -1;
        }
    }while (strncmp(buffer_send, "exit", 4) && strncmp(buffer_send, "create", 4) && (iparty < 0 || iparty >= *nbparty));

    return lparty;
}

void show_player(char **map, int nbrow, int nbcol){
    bool isfound;
    int x, y, xMax, xMin, yMax, yMin;
    for (int i = 0; i < nbrow; i++)
    {
        for (int j = 0; j < nbcol; j++)
        {
            if(isfound = (map[i][j] == '0')){
                x=j;
                y=i;
                break;
            }
        }
        if(isfound){
            break;
        }
    }

    if(nbcol < 10){
        xMax = nbcol;
        xMin = 0;
    }
    else if(x < 10)
    {
        xMax = 10;
        xMin = 0;
    }
    else if(x > nbcol-10)
    {
        xMax = nbcol;
        xMin = nbcol-10;
    }
    else
    {
        xMax = x+5;
        xMin = x-5;
    }
    
    
    if(nbrow < 10){
        yMax = nbrow;
        yMin = 0;
    }
    else if(y < 10)
    {
        yMax = 10;
        yMin = 0;
    }
    else if(y > nbrow-10)
    {
        yMax = nbrow;
        yMin = nbrow-10;
    }
    else
    {
        yMax = y+5;
        yMin = y-5;
    }

    for (int i = yMin; i < yMax; i++)
    {
        for (int j = xMin; j < xMax; j++)
        {
            if(j > strlen(map[i])){
                printf("%s", color_text(GREEN, LIGHT_GREEN, "🌿"));
            }
            else
            {
                switch (map[i][j])
                {
                case ' ':
                    printf("%s", color_text(GREEN, LIGHT_GREEN, "🌿"));
                    break;
                
                case '*':
                    printf("%s", color_text(GREEN, LIGHT_GREEN, "🥦"));
                    break;
                
                case '0':
                    printf("%s", color_text(GREEN, GREEN, "🧒"));
                    break;
                
                case '+':
                    printf("%s", color_text(GREEN, GREEN, "🏥"));
                    break;
                
                default:
                    printf("%s", color_text(GREEN, WHITE, "👦"));
                    break;
                }
            }
        }
        printf("\n");
    }
}

void print_map(struct clientTCP *cltTCP, char *buffer_recv, char *buffer_send){
    int nbrow, nbcol;
    char *temp, **map;

    sscanf(buffer_recv, "map %d %d\n", &nbrow, &nbcol);
    
    temp = (char *) malloc((nbrow * (nbcol+1)) * sizeof(char));
    cltTCP->client_receive_tcp(cltTCP, temp, (nbrow+1) * nbcol);

    map = (char **) malloc(nbrow * sizeof(char*));
    for (int i = 0; i < nbrow; i++)
    {
        map[i] = (char *) malloc(nbcol * sizeof(char));
        if (!i)
        {
            map[i] = strtok(temp, "\n");
        }
        else
        {
            map[i] = strtok(NULL, "\n");
        }
    }
    system("clear");
    show_player(map, nbrow, nbcol);
    
    printf("%s Return to server list ", color_text(BLACK, LIGHT_GRAY, "[exit]"));
    printf("%s Go up ", color_text(BLACK, LIGHT_GRAY, "[z]"));
    printf("%s Go down ", color_text(BLACK, LIGHT_GRAY, "[s]"));
    printf("%s Go left ", color_text(BLACK, LIGHT_GRAY, "[q]"));
    printf("%s Go right\n", color_text(BLACK, LIGHT_GRAY, "[d]"));

    free(temp);
    free(map);
}

void *thsend_move(void *clt){
    ClientTCP cltTCP = (struct clientTCP *)clt;
    char buffer_send[500];

    fgets(buffer_send, 500, stdin);
    isExit = !strncmp(buffer_send, "exit", 4);

    if (!isExit)
    {
        switch (buffer_send[0])
        {
        case 'z':
            cltTCP->client_send_tcp(cltTCP, "map move up\n");
            break;

        case 's':
            cltTCP->client_send_tcp(cltTCP, "map move down\n");
            break;

        case 'q':
            cltTCP->client_send_tcp(cltTCP, "map move left\n");
            break;

        case 'd':
            cltTCP->client_send_tcp(cltTCP, "map move right\n");
            break;

        default:
            thsend_move(clt);
            break;
        }
    }
    
    isRunning = false;
    return NULL;
}

void start_fight(){
    
}

void *thout_Team(void *clt){
    char buf[500];
    int fd;
    ClientTCP cltTcp;

    fd = open("OUT_Team.fifo", O_RDONLY);
    cltTcp = (struct clientTCP *) clt;
    while(read(fd, buf, 500))
    {
        if (!strncmp(buf, "exit", 4))
        {
            break;
        }
        printf("%s", buf);
        cltTcp->client_send_tcp(cltTcp, buf);
    }

    close(fd);
    return NULL;
}

void launch_game(struct clientTCP *cltTCP, char *buffer_send, char *buffer_recv, int bufsize){
    int ibuff, fifoTeam, fifoTchat;
    pid_t pidTeam, pidTchat;
    pthread_t threadMap, threadTeam;
    fd_set rfds;
    struct timeval tv;

    isRunning = false;
    isExit = false;

    unlink("IN_Team.fifo");
    unlink("OUT_Team.fifo");
    unlink("IN_Tchat.fifo");
    unlink("OUT_Tchat.fifo");

    if(mkfifo("IN_Team.fifo", 0777) == -1){
        perror("error create Team.fifo");
        exit(1);
    }

    if(mkfifo("OUT_Team.fifo", 0777) == -1){
        perror("error create Team.fifo");
        exit(1);
    }
    
    if(mkfifo("IN_Tchat.fifo", 0777) == -1){
        perror("error create Tchat.fifo");
        exit(1);
    }

    if(mkfifo("OUT_Tchat.fifo", 0777) == -1){
        perror("error create Tchat.fifo");
        exit(1);
    }

    if((pidTeam = system("gnome-terminal -- ./team"))==-1){
        perror("couldn't launch team");
        exit(1);
    }

    if((pidTchat = system("gnome-terminal -- ./tchat"))==-1){
        perror("couldn't launch tchat");
        exit(1);
    }

    fifoTeam = open("IN_Team.fifo", O_WRONLY);
    fifoTchat = open("IN_Tchat.fifo", O_WRONLY);

    pthread_create(&threadTeam, NULL, thout_Team, (void *) cltTCP);

    do
    {
        tv.tv_sec = 0;
        tv.tv_usec = 0;
        FD_ZERO(&rfds);
        FD_SET(cltTCP->sock, &rfds);

        if(select(cltTCP->sock+1, &rfds, NULL, NULL, &tv)){
            ibuff = 0;
            do
            {
                read(cltTCP->sock, &buffer_recv[ibuff], sizeof(char));
                ibuff++;
            }while (buffer_recv[ibuff-1]!='\n');
            buffer_recv[ibuff]='\0';

            if(!strncmp(buffer_recv, "map", 3)){
                print_map(cltTCP, buffer_recv, buffer_send);
                if(!isRunning){
                    isRunning = true;
                    pthread_create(&threadMap, NULL, thsend_move, (void *) cltTCP);
                }
            }
            else if(!strncmp(buffer_recv, "team contains", 13))
            {
                write(fifoTeam, buffer_recv, strlen(buffer_recv));
                cltTCP->client_receive_tcp(cltTCP, buffer_recv, bufsize);
                write(fifoTeam, buffer_recv, strlen(buffer_recv));
            }
            else if(!strncmp(buffer_recv, "message", 7))
            {
                write(fifoTchat, buffer_recv, strlen(buffer_recv));
            }
        }
    }while(!isExit);

    write(fifoTeam, "exit", 4);
    close(fifoTeam);

    write(fifoTchat, "exit", 4);
    close(fifoTchat);

    unlink("IN_Team.fifo");
    unlink("OUT_Team.fifo");
    unlink("IN_Tchat.fifo");
    unlink("OUT_Tchat.fifo");

    isExit = false;
    strcpy(buffer_send, "refresh");
}