#pragma once

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>


struct client 
{
    int sock;
    struct sockaddr_in addr;
    socklen_t len;

    ssize_t (*client_receive_udp)(struct client *this, char *buf, size_t size);
    void (*client_send_udp)(struct client *this, char *msg);
};

typedef struct client *Client;

Client client_create_udp(char *addr, int port);

Client client_create_broadcast(int port);

struct sockaddr_in * receive_server(struct client *this, char *buf, size_t size);

void client_close_and_free(struct client *this);
