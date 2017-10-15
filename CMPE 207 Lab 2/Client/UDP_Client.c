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
	char emptyString[] = "Yoo";
	char hostname[] = "time-b.nist.gov";
	
	struct sockaddr_in hostaddr, remaddr;
	struct hostent *server;
	struct servent *daytime;

	socklen_t locaddrlen = sizeof(hostaddr);
	socklen_t remaddrlen = sizeof(remaddr);
	
	if ( (network_socket = socket(AF_INET, SOCK_DGRAM, 0)) < 0)
		printf("Socket doesn't work");
	
	server = gethostbyname(hostname);	
	daytime = getservbyname("daytime", "udp");	

	remaddr.sin_family = AF_INET;
	remaddr.sin_port = ntohs(37);
	bcopy((char *) server->h_addr, (char *) &remaddr.sin_addr.s_addr, server->h_length);

	if( (socketStatus = sendto(network_socket, emptyString, sizeof(emptyString), 0, (struct sockaddr *) &remaddr, remaddrlen)) < 0) {
		printf("Send didn't work");
	}
	
	printf("Port number is: %d\n", daytime->s_port);
	printf("Port number is: %d\n", ntohs(daytime->s_port));
	memset((char *) &daytimeResponse, 0, sizeof(daytimeResponse));
	
	if( (socketStatus = recvfrom(network_socket, daytimeResponse, sizeof(daytimeResponse), 0, (struct sockaddr *) &remaddr, &remaddrlen)) < 0) {
		printf("recvfrom didn't work");
	}

	printf("Results: %s\n", daytimeResponse);	
	close(network_socket);

	return 0;
}
