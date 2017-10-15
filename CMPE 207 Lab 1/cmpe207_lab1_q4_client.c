/*
 * Author: Brandon Lee Gaerlan, Timothy Fong, Asvin Peiris
 * Class: CMPE 207 Network Programming and Applications
 * Program: Create a TCP Client Server program in which the client sends a number to the
 * Server, the Server then decrements that number by 1 and sends it back to the client.
 * The client will then display that number on the console
 * Due Date: 10-03-2017
 * */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
	
	// variables
	int network_socket;
	// specify an address for the socket
	struct sockaddr_in server_address;
	// pointer to host information entry
	struct hostent *phe;
	int connection_status;
	// send integer to server
	char message[1024];
	// receive data from server
	char server_response[1024];
	
	
	// reset address
	// in case leftover data from another process
	memset(&network_socket, 0, sizeof(network_socket));
	
	// Create TCP socket
	// Note change SOCK_STREAM to SOCK_DGRAM for UDP
	network_socket = socket(AF_INET, SOCK_STREAM, 0);
	if (network_socket < 0) {
		printf("socket failed");
		exit(1);
	}
	
	// init server_address
	memset(&server_address, 0, sizeof(server_address));
	// address family: AF_INET
	server_address.sin_family = AF_INET;
	
	// Map host name to IP address
	phe = gethostbyname(argv[1]);
	if(phe == NULL){
		printf("Error gethostbyname\n");
	}
	// h_addr = h_addr_list[0]
	printf("argv[1]: %s\n", argv[1]);
	// printf("HOST: %x\n", phe->h_addr);
	if (phe == 0) {
		printf("gethostname failed");
		exit(1);
	}
	memcpy(&server_address.sin_addr, phe->h_addr, phe->h_length);
	
	// Map service name to port number
	// port in network byte order
	// port we want to connect to; we choose 9002
	server_address.sin_port = htons(9002);
	
	// connection_status = 0 means no problem
	// connection_status = -1 mean error
	connection_status = connect(network_socket, (struct sockaddr *) &server_address, sizeof(server_address));
	// check for error with connection_status
	if(connection_status < 0) {
		printf("Error with connection \n");
		exit(1);
	}
	
	// read a integer from user
	printf("input an integer: ");
	scanf("%s", message);
	if(send(network_socket, message, sizeof(message), 0) < 0) {
		printf("send failed");
		exit(1);
	}
	
	// receive response from server
	recv(network_socket, &server_response, sizeof(server_response), 0);
	
	// print out server response
	printf("The server responded with: %s \n", server_response);

	// finally close the socket
	close(network_socket);
	
	return 0;
}
