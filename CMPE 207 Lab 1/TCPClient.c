// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <unistd.h>
#include <netdb.h>
#include <string.h>
#define BUFSIZE 1024

int main(int argc, char const *argv[]) {

	int network_socket	= 0,	// Create socket
	socketStatus		= 0;	// Monitor status of the socket that's being created

	char daytimeResponse[BUFSIZE];
	char hostname[] = "time-a.nist.gov";
	
	struct sockaddr_in hostaddr, remaddr;
	struct hostent *server;

	socklen_t locaddrlen = sizeof(hostaddr);
	socklen_t remaddrlen = sizeof(remaddr);
	
	if ( (network_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0)
		printf("Socket doesn't work");
	
	server = gethostbyname(hostname);	
	
	remaddr.sin_family = AF_INET;
	remaddr.sin_port = htons(13);
	
	printf("Sin_familiy: %d\n", remaddr.sin_family);
	printf("Port number: %d\n", remaddr.sin_port);

	bcopy((char *) server->h_addr, (char *) &remaddr.sin_addr.s_addr, server->h_length);

	printf("Address: %d\n", &remaddr.sin_addr.s_addr);

	if( (socketStatus = connect(network_socket, (struct sockaddr *) &remaddr, remaddrlen)) < 0) {
		printf("Send didn't work");
	}
	
	printf("Sendto works\n");
	memset((char *) &daytimeResponse, 0, sizeof(daytimeResponse));
	
	

	if( (socketStatus = recv(network_socket, daytimeResponse, sizeof(daytimeResponse), 0)) < 0) {
		printf("recvfrom didn't work");
	}

	printf("Results: %s\n", daytimeResponse);	

	close(network_socket);

	return 0;
}
