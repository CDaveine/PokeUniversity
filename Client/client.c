#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#include "client .h"
#include "error.h"

ssize_t client_receive_udp(struct client *this, char *buf, size_t size){
    if(!buf){
        return 0;
    }

    return recvfrom(this->sock, buf, size, 0, NULL, NULL);
}

void client_send_udp(struct client *this, char *msg){
    if(sendto(this->sock, msg, strlen(msg), 0, (struct sockaddr*) &this->addr, sizeof(struct sockaddr_in)) == ERR){
        syserror(SEND_ERROR);
    }
}

Client client_create_udp( char *addr, int port){
    int sock;
    Client res = (struct client *) malloc(sizeof(struct client));

    sock = socket( AF_INET, SOCK_DGRAM, 0);
    if(sock == ERR){
        syserror(SOCKET_ERROR);
    }

    res->sock = sock;
    res->addr.sin_family = AF_INET;
    res->addr.sin_port = htons((uint16_t) port);

    if (!inet_aton(addr, &res->addr.sin_addr) == ERR)
    {
        client_close_and_free(res);
        syserror(SOCKET_ERROR);
    }

    res->client_receive_udp = &client_receive_udp;
    res->client_send_udp = &client_send_udp;

    return res;
}

void client_close_and_free(struct client *this){
    if(close(this->sock) == ERR){
        syserror(SOCKET_CLOSE_ERROR);
    }

    free(this);
}