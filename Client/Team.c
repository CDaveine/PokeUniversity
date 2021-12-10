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

int nbpoke;
bool isExit;

void *thInput(void *nothing){
    char buf[SIZE], move[4];
    int fd, select, size;
    
    if((fd = open("OUT_Team.fifo", O_WRONLY)) == -1){
        perror("can't opent Team.fifo please launch the client before");
        exit(1);
    }
    
    while (!isExit)
    {
        if(!nbpoke)
        {
            continue;
        }

        do{
            fgets(buf, SIZE, stdin);
            sscanf(buf, "%d %s\n", &select, move);
        }while((select < 0 || select >= nbpoke) || (strncmp(move, "up", 2) && strncmp(move, "down", 4) && strncmp(move, "free", 4)));
        
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

typedef struct
{
    char variety[25];
    char type[25];
    int lvl, currxp, xpnextlvl;
    int pv, maxpv;
    int att, def;
    char attack1[25], attack1type[25];
    char attack2[25], attack2type[25];
}t_poketudiant;

char * get_pokemoji(t_poketudiant poketudiant)
{
    if(!strncmp(poketudiant.variety, "Parlfor", 7))
    {
        return "😃";
    }
    else if(!strncmp(poketudiant.variety, "Ismar", 5))
    {
        return "😄";
    }
    else if(!strncmp(poketudiant.variety, "Rigolamor", 9))
    {
        return "😂";
    }
    else if(!strncmp(poketudiant.variety, "Proscratino", 11))
    {
        return "🥱";
    }
    else if(!strncmp(poketudiant.variety, "Couchtar", 8))
    {
        return "🥳";
    }
    else if(!strncmp(poketudiant.variety, "Nuidebou", 8))
    {
        return "😈";
    }
    else if(!strncmp(poketudiant.variety, "Alabourre", 9))
    {
        return "😱";
    }
    else if(!strncmp(poketudiant.variety, "Buchafon", 8))
    {
        return "👨‍💻";
    }
    else if(!strncmp(poketudiant.variety, "Belmention", 10))
    {
        return "🧑‍⚖️";
    }
    else if(!strncmp(poketudiant.variety, "Promomajor", 10))
    {
        return "👨‍🎓";
    }
    else if(!strncmp(poketudiant.variety, "Enseignant-dresseur", 19))
    {
        return "👨‍🏫";
    }
    else
    {
        return " ";
    }
}

void print_poketudiant(t_poketudiant poketudiant)
{
    printf(" │ %s Variety: %s Type: %s\n", get_pokemoji(poketudiant), poketudiant.variety, poketudiant.type);
    printf(" │ ❤️  %d/%d\n", poketudiant.pv, poketudiant.maxpv);
    printf(" │ ⚔️  %d 🛡️  %d\n", poketudiant.att, poketudiant.def);
    printf(" │ lvl: %d exp: %d/%d\n", poketudiant.lvl, poketudiant.currxp, poketudiant.xpnextlvl);
    printf(" │ Attack 1: %s type: %s\n", poketudiant.attack1, poketudiant.attack1type);
    printf(" │ Attack 2: %s type: %s\n", poketudiant.attack2, poketudiant.attack2type);
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
        sscanf(temp, "team contains %d", &nbpoke);

        for (int i = 0; i < nbpoke; i++)
        {
            temp = strtok(NULL, "\n");
            sscanf(temp,"%s %s %d %d %d %d %d %d %d %s %s %s %s\n", 
                poketudiant.variety, poketudiant.type, &poketudiant.lvl, &poketudiant.currxp, &poketudiant.xpnextlvl, 
                &poketudiant.pv, &poketudiant.maxpv, &poketudiant.att, &poketudiant.def, poketudiant.attack1, poketudiant.attack1type, 
                poketudiant.attack2, poketudiant.attack2type);

            printf("\nPokétudiant n°%d:\n", i);
            print_poketudiant(poketudiant);

        }

        printf("\n%s Select pokétudiant + ", color_text(BLACK, LIGHT_GRAY, "[0...]"));
        printf("%s Move selected pokétudiant up ", color_text(BLACK, LIGHT_GRAY, "[up]"));
        printf("%s Move selected pokétudiant down ", color_text(BLACK, LIGHT_GRAY, "[down]"));
        printf("%s Free pokétudiant\nExample: %s\n", color_text(BLACK, LIGHT_GRAY, "[free]"), color_text(BLACK, LIGHT_GRAY, "[0 down]"));
    }

    close(fd);

    return 0;
}
