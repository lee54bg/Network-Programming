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

int main() {
	
	// variables
	int server_socket;
	struct sockaddr_in server_address;
	int client_socket;
	char server_message[1024] = "You connected to the server!\n";
	char buffer[1024];
	int rval;
	int val = 0;
	int n;
	
	// create server socket
	server_socket = socket(AF_INET, SOCK_STREAM, 0);
	// check if socket failed to create
	if(server_socket < 0) {
		printf("Failed to create a socket");
		exit(1);
	}
	
	// define server address
	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(9002);
	server_address.sin_addr.s_addr = INADDR_ANY;
	
	// bind socket to specific IP and port
	if(bind(server_socket, (struct sockaddr *) &server_address, sizeof(server_address))) {
		printf("bind failed");
		exit(1);
	}
	
	// listening
	// max 5 connections
	listen(server_socket,5);
	
	// accept connections
	do {
		// initialize a socket we can send data to
		client_socket = accept(server_socket, (struct sockaddr *) 0, 0);
		if(client_socket == -1) {
			printf("accept failed");
		}
		else {
			memset(buffer, 0, sizeof(buffer));
			printf(".............\n");
			rval = recv(client_socket, buffer, sizeof(buffer), 0);
			if(rval < 0) {
				printf("reading stream message error");
			}
			else if (rval == 0){
				printf("Ending connection \n");
			}
			else {	
				printf("Received buffer size: %d \n", rval);
			}
			// read the data
			printf("Received integer: %s \n", buffer);
			// convert to int
			val = atoi(buffer);
			// decrement by 1
			val = val - 1;
			// convert to char array
			//itoa(val, buffer, 10);
			n = sprintf(buffer, "%d", val);
			printf("Sending: %s \n", buffer);
			// send to client
			send(client_socket, buffer, sizeof(buffer), 0);
			close(client_socket);
		}
	} while(1);
	
	// close the socket
	close(server_socket);
	
	return 0;
}
