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
	addrlen = 0,		// Used for the address length
	length = 0,
	test = 0,
	raddrlen = 0;		
	
	char daytimeResponse[BUFSIZE];
	char temp[] = "Hello";
	char hostname[] = "time.nist.gov";

	struct hostent *server;
	struct sockaddr_in hostaddr, server_address;
	
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);	
	
	if (network_socket < 0)
		printf("Socket doesn't work");

	server = gethostbyname(hostname);	//Set the hostname as the hostent
	
	/*
	hostaddr.sin_family = AF_INET;		// IPv4 Address
	hostaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	hostaddr.sin_port = htons(13);		// Get the port 	
	*/

	server_address.sin_family = AF_INET;		// IPv4 Address
	bcopy((char *) server->h_addr, (char *) &server_address.sin_addr.s_addr, server->h_length);
	server_address.sin_port = htons(13);		// Get the port 	
	
	addrlen = sizeof(hostaddr);
	raddrlen = sizeof(server_address);
	
	test = sendto(network_socket, temp, sizeof(temp), 0, (struct sockaddr *) &hostaddr, addrlen);
	
	printf(" %d bytes sent", test);

	if( test < 0 )
		printf("Send doesn't work");

	test = recvfrom(network_socket, daytimeResponse, sizeof(daytimeResponse), 0, (struct sockaddr *) &server_address, &raddrlen);
	
	if( test < 0)
		printf("Receive doesn't work");

	printf("Message is: %s\n", daytimeResponse);
		
	return 0;
}
