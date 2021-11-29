#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>

#define SIZE  500

int main(int argc, char const *argv[])
{
    int fd;
    char buffer[SIZE], temp[50];

    if((fd = open("Team.fifo", O_RDONLY)) == -1){
        perror("can't opent Team.fifo please launch the client before");
        exit(1);
    }
    
    while(read(fd, buffer, SIZE))
    {
        printf("%s", buffer);
    }

    return 0;
}
