#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>

#include "color.h"
#include "poketudiant.h"

#define SIZE  500

int nbpoke;
bool isExit;
bool isFight;

void *thInput(void *nothing){
    char buf[SIZE], move[4];
    int fd, select, size;
    
    if((fd = open("OUT_Team.fifo", O_WRONLY)) == -1){
        perror("can't opent Team.fifo please launch the client before");
        exit(1);
    }
    
    while (!isExit && !isFight)
    {
        if(!nbpoke)
        {
            continue;
        }

        do{
            fgets(buf, SIZE, stdin);
            sscanf(buf, "%d %s\n", &select, move);
        }while((select < 0 || select >= nbpoke) || (strncmp(move, "up", 2) && strncmp(move, "down", 4) && strncmp(move, "free", 4)));
        
        if (isFight)
        {
            continue;
        }
        
        if(!strncmp(move, "up", 2) || !strncmp(move, "down", 4))
        {
            size = sprintf(buf, "poketudiant %d move %s\n", select, move);
        }
        else
        {
            size = sprintf(buf, "poketudiant %d %s\n", select, move);
        }

        buf[size] = '\0';
        write(fd, buf, size+1);
    }
    
    close(fd);
    return NULL;
}


int main(int argc, char const *argv[])
{
    int fd;
    char buffer[SIZE], *temp;
    pthread_t threadInput;
    bool isRunning;
    t_poketudiant poketudiant;

    if((fd = open("IN_Team.fifo", O_RDONLY)) == -1){
        perror("can't opent Team.fifo please launch the client before");
        exit(1);
    }
    
    isExit = false;
    isRunning = false;
    isFight = false;

    pthread_create(&threadInput, NULL, thInput, NULL);
    
    printf("Team\n");
    while(read(fd, buffer, SIZE))
    {
        if(isExit = !strncmp(buffer, "exit", 4)){
            break;
        }
        

        system("clear");
        printf("Team\n");
        temp = strtok(buffer, "\n");

        if(!isFight){
            isFight = !strncmp(temp, "fight", 5);
            if(isFight)
            {
                continue;
            }
        }
        else
        {
            isFight = strncmp(temp, "endfight", 8);
            if(!isFight)
            {
               pthread_create(&threadInput, NULL, thInput, NULL);
               continue;
            }
        }
        
        sscanf(temp, "team contains %d", &nbpoke);
        for (int i = 0; i < nbpoke; i++)
        {
            temp = strtok(NULL, "\n");
            poketudiant = to_poketudiant(temp);

            printf("\nPokétudiant n°%d:\n", i);
            print_poketudiant(poketudiant);

        }

        if (!isFight)
        {
            printf("\n%s Select pokétudiant + ", color_text(BLACK, LIGHT_GRAY, "[0...]"));
            printf("%s Move selected pokétudiant up ", color_text(BLACK, LIGHT_GRAY, "[up]"));
            printf("%s Move selected pokétudiant down ", color_text(BLACK, LIGHT_GRAY, "[down]"));
            printf("%s Free pokétudiant\nExample: %s\n", color_text(BLACK, LIGHT_GRAY, "[free]"), color_text(BLACK, LIGHT_GRAY, "[0 down]"));
        }
    }

    close(fd);

    return 0;
}
