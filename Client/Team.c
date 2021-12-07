#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>

#include "color.h"

#define SIZE  500

bool isRunning;

void *thInput(void *nbpoke){
    char buf[SIZE], move[4];
    int fd, select, n, size;

    if((fd = open("OUT_Team.fifo", O_WRONLY)) == -1){
        perror("can't opent Team.fifo please launch the client before");
        exit(1);
    }

    n = *((int *) nbpoke); 
    do{
        fgets(buf, SIZE, stdin);
        sscanf(buf, "%d %s\n", &select, move);
    }while((select < 0 || select >= n) || (strncmp(move, "up", 2) && strncmp(move, "down", 4) && strncmp(move, "free", 4)));
    size = sprintf(buf, "poketudiant %d %s\n", select, move);
    buf[size] = '\0';
    write(fd, buf, size+1);

    close(fd);
    isRunning = false;
    return NULL;
}

int main(int argc, char const *argv[])
{
    int fd, nbpoke;
    char buffer[SIZE], *temp;
    pthread_t threadInput;

    if((fd = open("IN_Team.fifo", O_RDONLY)) == -1){
        perror("can't opent Team.fifo please launch the client before");
        exit(1);
    }


    
    printf("Team\n");
    while(read(fd, buffer, SIZE))
    {
        if(!strncmp(buffer, "exit", 4)){
            break;
        }
        system("clear");
        printf("Team\n");
        if(!strncmp(buffer, "exit", 4)){
            break;
        }
        sscanf(buffer, "team contains %d\n", &nbpoke);

        read(fd, buffer, SIZE);
        for (int i = 0; i < nbpoke; i++)
        {
            if (i==0)
            {
                temp = strtok(buffer, "\n");
            }
            else
            {
                temp = strtok(NULL, "\n");
            }
            printf("%d %s\n", i, temp);
        }
        printf("%s Select pokétudiant +", color_text(BLACK, LIGHT_GRAY, "[0...]"));
        printf("%s Move selected pokétudiant up ", color_text(BLACK, LIGHT_GRAY, "[up]"));
        printf("%s Move selected pokétudiant down ", color_text(BLACK, LIGHT_GRAY, "[down]"));
        printf("%s Free pokétudiant\n Example: [0 up]\n", color_text(BLACK, LIGHT_GRAY, "[free]"));
        
        if(!isRunning)
        {
            isRunning = true;
            pthread_create(&threadInput, NULL, thInput, (void *) &nbpoke);
        }
        
    }

    close(fd);

    return 0;
}
