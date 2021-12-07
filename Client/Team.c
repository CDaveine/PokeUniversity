#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>
#include <string.h>

#define SIZE  500

int main(int argc, char const *argv[])
{
    int fd, nbpoke;
    char buffer[SIZE], *temp;

    if((fd = open("Team.fifo", O_RDONLY)) == -1){
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
    }

    close(fd);

    return 0;
}
