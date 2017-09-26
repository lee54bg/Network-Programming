// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <string.h>
#include <unistd.h>
//#include <unp.h>
#define PORT 8080

int main(int argc, char const *argv[]) {
	
	// Create a socket for connection and use this variable to hold the
	// status of the network_socket
	int network_socket;
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);	

	//Will be used for keeping track of the file lines later
	int count;
	
	// Declare the parameters for the server socket the user is trying to connect to
	struct sockaddr_in server_address;
	server_address.sin_family = AF_INET;		// IPv4 Address
	server_address.sin_port = htons(13);		// Get the port 
	server_address.sin_addr.s_addr = INADDR_ANY;	// Use any IP Address
	
	char daytimeResponse[512];
	int cc = recvfrom(network_socket, daytimeResponse, sizeof(daytimeResponse), 0);
	
	printf("The daytime response from the server is: %s\n", daytimeResponse);	
	while (cc > 0) {
		daytimeResponse[512 + 1] = '\0';
		fputs(daytimeResponse, stdout);
	}
/*
	while( (count = read(network_socket, daytimeResponse, sizeof(daytimeResponse))) ) {
		daytimeResponse[count] = 0;
		if( fputs(daytimeResponse, stdout) == EOF)
			err_sys("Can't read from file");
	}
*/
	
	// Close the socket connected to the Daytime Server
	close(network_socket);
	
	return 0;
}
