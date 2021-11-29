#pragma once


void get_msg(const char *need, char *msg);

void prompt_server_list(struct sockaddr_in **servers, int nbserv, char *buffer_send);

void prompt_party_list(struct clientTCP *cltTCP, char **lparty, int *nbparty, char *buffer_recv, char *buffer_send, int bufsize);

void launch_game(struct clientTCP *cltTCP, char *buffer_send, char *buffer_recv, int bufsize);