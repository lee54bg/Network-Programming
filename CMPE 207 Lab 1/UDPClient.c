// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <string.h>
#include <unistd.h>
#define PORT 8080

int main(int argc, char const *argv[]) {
	
	/* Declare variables for sockets, ip address, etc. */
	// Create a socket for connection
	int network_socket;
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);

	struct sockaddr_in server_address;
	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(13);
	server_address.sin_addr.s_addr = INADDR_ANY;

	int cnctn_status = connect(network_socket, (struct sockaddr *) &server_address, sizeof(server_address));
	
	if(cnctn_status == -1)
		printf("The connection is invalid.");

	return 0;
}
