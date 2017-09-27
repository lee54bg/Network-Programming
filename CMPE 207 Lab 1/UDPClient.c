// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <string.h>
#include <unistd.h>
#include <netdb.h>
#define PORT 8080
#define BUFSIZE 1024

int main(int argc, char const *argv[]) {
	printf("Starting UDP client\n");

	// Create a socket for connection and use this variable to hold the
	// status of the network_socket
	int network_socket;
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);	
	struct hostent *server;

	//Will be used for keeping track of the file lines later
	int count, length;
	
	char hostname[] = "time-a.nist.gov";
	
	// Test to make sure socket is working properly
	printf("Set the network socket\n");

	// Declare the parameters for the server socket the user is trying to connect to
	struct sockaddr_in server_address;
	int addrlen = sizeof(server_address);
	
	server = gethostbyname(hostname);
	
	server_address.sin_family = AF_INET;		// IPv4 Address
	bcopy((char *) server->h_addr,
		(char *) &server_address.sin_addr.s_addr, server->h_length);
	//memcpy(&server_address.sin_addr.s_addr, server->h_addr, server->h_length);
		
	//server_address.sin_port = htons(13);		// Get the port 
	//server_address.sin_addr.s_addr = "134.63.4.101";	// Use any IP Address
	
	// Bind is only for servers
	//bind(network_socket, (struct sockaddr *) &server_address, sizeof(server_address));
	
	printf("Successfully binded the ip address\n");

	char daytimeResponse[BUFSIZE];
	char temp[] = "Hello";
	int test;
	
	test = sendto(network_socket, temp, sizeof(temp), 0, (struct sockaddr *) &server_address, addrlen);
	
	if( test < 0 )
		printf("Send doesn't work");

	test = recvfrom(network_socket, daytimeResponse, sizeof(daytimeResponse), 0, (struct sockaddr *) &server_address, &addrlen);
	
	if( test < 0)
		printf("Receive doesn't work");
	
	
	printf("About to print my dgram received\n");
	printf("Message is: %s\n", daytimeResponse);
	
		
	return 0;
}
