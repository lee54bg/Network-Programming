// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <unistd.h>
#include <netdb.h>
#define BUFSIZE 1024

int main(int argc, char const *argv[]) {

	int network_socket = 0,	// Create socket
	socketStatus	= 0;	// Monitor status of the socket that's being created

	char daytimeResponse[] = "Hi";

	struct sockaddr_in hostaddr, remaddr;

	socklen_t locaddrlen = sizeof(hostaddr);
	socklen_t remaddrlen = sizeof(remaddr);
	
	if ( (network_socket = socket(AF_INET, SOCK_DGRAM, 0)) < 0)
		printf("Socket doesn't work");
	
	remaddr.sin_family = AF_INET;
	remaddr.sin_port = htons(3002);
	remaddr.sin_addr.s_addr = htonl(INADDR_ANY);

	if( (socketStatus = sendto(network_socket, daytimeResponse, sizeof(daytimeResponse), 0, (struct sockaddr *) &remaddr, remaddrlen)) < 0) {
		printf("Send didn't work");
	}

	return 0;
}
