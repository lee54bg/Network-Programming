// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <netdb.h>
#include <string.h>
#define BUFSIZE 1024

int main(int argc, char const *argv[]) {

	int network_socket = 0,	// Create socket
	localaddrlen = 0,
	//remaddrlen = 0,		// Used for the address length
	socketStatus = 0;

	
	char daytimeResponse[BUFSIZE];

	struct sockaddr_in localaddr, remaddr;
	
	//localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen = sizeof(remaddr);
	
	if ( (network_socket = socket(AF_INET, SOCK_DGRAM, 0)) < 0)
		printf("Socket doesn't work\n");
	
	memset( (char *) &localaddr, 0, sizeof(localaddr) );
	localaddr.sin_family = AF_INET;		// IPv4 Address
	localaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	localaddr.sin_port = htons(3000);		// Get the port 	
	
	if ( bind(network_socket, (struct sockaddr *) &localaddr, sizeof(localaddr) ) < 0) {
		printf("Error in binding");
		return 0;
	}
	
	while(1) {
		socketStatus = recvfrom(network_socket, daytimeResponse, sizeof(daytimeResponse), 0, (struct sockaddr *) &remaddr, &remaddrlen);
	
		if( socketStatus < 0)
			printf("Receive doesn't work");
	
		printf("Message is: %s\n", daytimeResponse);
	}	
}
