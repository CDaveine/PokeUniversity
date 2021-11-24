#include <stdio.h>
#include <string.h>

#include "client.h"

#define MAX 20
#define SIZE 500

static void get_msg(char* msg){
    printf("message: \n");
    fgets(msg, SIZE, stdin);
}

int main(int argc, char const *argv[])
{
    Client clt = client_create_udp("127.0.0.1", 9001);

    char buffer_send[SIZE];
    char buffer_recv[SIZE];

    for(;;){
        get_msg(buffer_send);

        if(!strncmp(buffer_send, "exit", 4)){
            break;
        }

        clt->client_send_udp(clt, buffer_send);
        clt->client_receive_udp(clt, buffer_recv, SIZE);

        printf("%s\n", buffer_recv);
    }

    client_close_and_free(clt);

    return 0;
}
