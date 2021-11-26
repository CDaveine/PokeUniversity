#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>

#include "client.h"

#define MAX 20
#define SIZE 500

static void get_msg(char* msg){
    printf("message: \n");
    fgets(msg, SIZE, stdin);
}

int main(int argc, char const *argv[])
{
    time_t timer, endtimer;
    Client clt = client_create_broadcast(9000);
    struct sockaddr_in *server, **servers = (struct sockaddr_in**) malloc(sizeof(struct sockaddr_in*));
    int nbserv = 0;

    

    char buffer_send[SIZE];
    char buffer_recv[SIZE];
    
    clt->client_send_udp(clt, "looking for poketudiant servers");

    timer = time(NULL);
    endtimer = timer + 3;

    while (timer < endtimer)
    {
        server = receive_server(clt, buffer_recv, SIZE);
        if(!strncmp(buffer_recv, "i’m a poketudiant server", 24)){
            printf("receive\n");
            if(nbserv != 0){
                servers = (struct sockaddr_in**) realloc(servers, (nbserv+1) * sizeof(struct sockaddr_in*));
            }
            servers[nbserv] = server;
            nbserv++;
        }
        else
        {
            free(server);
        }

        timer = time(NULL);
    }

    for (int i = 0; i < nbserv; i++)
    {
        printf("%s\n", inet_ntoa(servers[i]->sin_addr));
    }
    
    

    /*for(;;){
        get_msg(buffer_send);

        if(!strncmp(buffer_send, "exit", 4)){
            break;
        }

        clt->client_send_udp(clt, buffer_send);
        
        clt->client_receive_udp(clt, buffer_recv, SIZE);

        printf("%s\n", buffer_recv);
    }*/

    client_close_and_free(clt);

    return 0;
}
