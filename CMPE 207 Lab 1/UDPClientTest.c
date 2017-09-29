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
	localaddrlen	= 0,
	remaddrlen	= 0,	// Used to take the size of the remote address
	socketStatus	= 0;

	char daytimeResponse[BUFSIZE];


	struct sockaddr_in hostaddr, remaddr;

	socklen_t locaddrlen = sizeof(hostaddr);
	socklen_t remaddrlen = sizeof(remaddr);
	
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);
	
	if (network_socket < 0)
		printf("Socket doesn't work");
		
	return 0;
}
