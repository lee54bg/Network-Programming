/*
 * Author: Brandon Lee Gaerlan, Timothy Fong, Asvin Peiris
 * Class: CMPE 207 Network Programming and Applications
 * Program: Create a UDP program that gets an output from the daytime service using UDP datagrams
 * Due Date: 10-03-2017
 * */

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

int main(int argc, char const **argv) {

	// Create socket
	int network_socket	= 0,
	// Monitor status of the socket that's being created
	socketStatus		= 0;

	//The buffer that will be used for storing the Daytime server response
	char daytimeResponse[BUFSIZE];
	//The string used to send a datagram to the Daytime server
	char emptyString[] = "Yoo";
	
	//The local and remote address of the server respectively
	struct sockaddr_in hostaddr, remaddr;
	//The information of the daytime server that will be passed here
	struct hostent *server;
	//The type of service that will be used.  In this case UDP
	struct servent *daytime;

	//Size of the host address
	socklen_t locaddrlen = sizeof(hostaddr);
	//Size of the remote address (Daytime Server)
	socklen_t remaddrlen = sizeof(remaddr);	
	
	//Check to make sure that the socket has been created
	if ( (network_socket = socket(AF_INET, SOCK_DGRAM, 0)) < 0)
		printf("Socket doesn't work");
	
	//Get the details of the Daytime server provided on the argument of the client
	server = gethostbyname(argv[1]);
	//Get the type of service using daytime and udp
	daytime = getservbyname("daytime", "udp");
	
	//Set the IP address of the struct to IPv4
	remaddr.sin_family = AF_INET;
	//Set the port number of the struct to that declared in the daytime service (UDP Port 13)
	remaddr.sin_port = ntohs(37);
	//Copy the address of the host entity (daytime server) to the struct (remaddr)
	bcopy((char *) server->h_addr, (char *) &remaddr.sin_addr.s_addr, server->h_length);

	//Check to see if the datagram has been sent
	if( (socketStatus = sendto(network_socket, emptyString, sizeof(emptyString), 0, (struct sockaddr *) &remaddr, remaddrlen)) < 0) {
		printf("Send didn't work");
	}
	
	//Ensure that the Daytime response buffer has been zeroed out
	memset((char *) &daytimeResponse, 0, sizeof(daytimeResponse));

	//Check to see if the client has successfully received a response back from the server
	if( (socketStatus = recvfrom(network_socket, daytimeResponse, sizeof(daytimeResponse), 0, (struct sockaddr *) &remaddr, &remaddrlen)) < 0) {
		printf("recvfrom didn't work");
	}
	
	//Print the response of the daytime server and close the socket afterwards
	printf("Results: %s\n", daytimeResponse);	
	close(network_socket);

	return 0;
}
