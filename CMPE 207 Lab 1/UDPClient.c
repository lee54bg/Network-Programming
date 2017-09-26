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
	
	// Create a socket for connection and use this variable to hold the
	// status of the network_socket
	int network_socket;

	// Declare the parameters for the server socket the user is trying to connect to
	struct sockaddr_in server_address;
	
	// Initialize the socket with the following parameters
	/*
	AF_INET = IPv4
	SOCK_DGRAM = UDP
	0 = No flags set	
	*/
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);
	

	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(13);
	server_address.sin_addr.s_addr = INADDR_ANY;

	int cnctn_status = connect(network_socket, (struct sockaddr *) &server_address, sizeof(server_address));
	
	if(cnctn_status == -1)
		printf("The connection is invalid.");
	
	char daytimeResponse[512];
	recv(network_socket, daytimeResponse, sizeof(daytimeResponse), 0);
	printf("The daytime response from the server is: %s\n", daytimeResponse);	

	// Close the socket connected to the Daytime Server
	close(network_socket);

	return 0;
}
