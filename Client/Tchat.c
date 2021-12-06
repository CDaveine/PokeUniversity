#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>

#define SIZE 500

int main(int argc, char const *argv[])
{
    char buffer[SIZE];
    int fd;

    if(fd = open("Tchat.fifo", O_RDONLY)){
        perror("can't opent Tchat.fifo please launch the client before");
        exit(1);
    }

    while(read(fd, buffer, SIZE))
    {
        system("clear");
        printf("Tchat\n");
        printf("%s", buffer);
    }

    return 0;
}
