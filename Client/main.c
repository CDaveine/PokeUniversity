#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "clientbrc.h"
#include "color.h"

#define MAX 20
#define SIZE 500

static void get_msg(const char *need, char *msg){
    printf("%s \n", need);
    fgets(msg, SIZE, stdin);
}

int main(int argc, char const *argv[])
{
    Clientbrc clt = client_create_broadcast(9000);
    struct sockaddr_in **servers;
    int nbserv;
    char buffer_send[SIZE];
    char buffer_recv[SIZE];
    
    clt->client_send_broadcast(clt, "looking for poketudiant servers");
    servers = clt->client_receive_servers(clt, "i'm a poketudiant server", &nbserv);

    if(nbserv != 0){
        for (int i = 0; i < nbserv; i++)
        {
            printf("Serveur %d %s\n", i, inet_ntoa(servers[i]->sin_addr));
        }

        printf("%s ", color_text(BLACK, LIGHT_GRAY, "exit"));
        printf("%s ", color_text(BLACK, LIGHT_GRAY, "refresh"));
        get_msg(color_text(BLACK, LIGHT_GRAY, "choose server (0...)"), buffer_send);


        for (int i = 0; i < nbserv; i++)
        {
            free(servers[i]);
        }
        free(servers);
    }
    
    clientbrc_close_and_free(clt);

    return 0;
}
