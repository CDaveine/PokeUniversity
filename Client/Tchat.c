#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>
#include <string.h>

#define SIZE 500

int main(int argc, char const *argv[])
{
    char buffer[SIZE];
    int fd;

    if((fd = open("IN_Tchat.fifo", O_RDONLY)) == -1){
        perror("can't opent Tchat.fifo please launch the client before");
        exit(1);
    }

    printf("Tchat\n");
    while(read(fd, buffer, SIZE))
    {
        if(!strncmp(buffer, "exit", 4)){
            break;
        }
        system("clear");
        printf("Tchat\n%s", buffer);
    }

    close(fd);

    return 0;
}
