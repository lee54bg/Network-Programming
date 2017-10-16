#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>
#define BUFSIZE 1024
#define FILENAMESIZE 256

int main(int argc, char **argv) {
	int network_socket	= 0,	// Create socket
		clientSocket	= 0,	// Creating our client Socket
		socketStatus	= 0,	// Creating our socket status
		filedes		= 0,	// Creating our file Descriptor
		pid		= 0;	// Used for identifying our process id (both child and parent)
		bytesSnt	= 0,	// Bytes sent for each individual send
		bytesRcvd	= 0,	// Bytes read for each individual read
		totalBytesSnt	= 0,	// Total number of bytes sent to the server
		totalBytesRcvd	= 0;	// Total number of bytes received from the server
	
	unsigned short server_port = 12345;

	char buffer[BUFSIZ];
	char *ip_address;
	char file_path[FILENAMESIZE];
	char *filename;

	struct sockaddr_in localaddr, remaddr;			// Creating local and remote endpoints

	memset( (char *) &localaddr, 0, sizeof(localaddr) );	// Zero out the local address

	switch(argc) {
		case 2:
			server_port	= strtol(argv[1], NULL, 10);	// Port number to be used for connecting to the server	
			break;	
		case 3:
			ip_address	= argv[1];			// IP Address in ASCII form
			server_port	= strtol(argv[2], NULL, 10);	// Port number to be used for connecting to the server
			break;
		default:
			puts("Not enough arguments.  Terminating server...");
	}

	ssize_t read_return;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	// Create the socket with the following paremeters
	network_socket = socket(AF_INET, SOCK_STREAM, 0);
	
	// Check to ensure that the socket connects
	if ( network_socket < 0) {
		perror("Socket cannot connect");
		exit(1);
	}
	
	// Set the parameters of the local address
	localaddr.sin_family		= AF_INET;
	localaddr.sin_addr.s_addr	= INADDR_ANY;
	localaddr.sin_port		= htons(server_port);

	// Ensure that the local endpoint has been binding to our created socket
	if ( bind(network_socket, (struct sockaddr *) &localaddr, localaddrlen ) == -1) {
		perror("Error in binding");
		exit(1);
	}

	if ( listen(network_socket, 5) < 0 )
		perror("Error on listen");

	while(1) {
		puts("Waiting for clients...");
		clientSocket = accept(network_socket, (struct sockaddr *) &remaddr, &remaddrlen);
		
		bzero(file_path, sizeof(file_path));	// Zero out the buffer for the file_path
		bytesRcvd = read(clientSocket, (char *) file_path, sizeof(file_path) );	// Read the file name request from the client

		puts(file_path);	// For debugging purposes

		filedes = open(file_path, O_RDONLY);

		if (filedes == -1) {
			perror("open");
			continue;
		} else
			pid = fork();

		if(pid == 0) {
			close(network_socket);

			read_return = read(filedes, buffer, BUFSIZ);
		
			totalBytesSnt += read_return;

			// Break out of loop if there's no more data to be read
			if (read_return == 0)
			    break;

			// Exit if there's an error in reading
			if (read_return == -1) {
			    perror("read");
			    exit(EXIT_FAILURE);
			}

			// Send content over to the remote endpoint
			socketStatus = write(clientSocket, buffer, read_return);
	
			if (socketStatus < 0) {
				perror("ERROR reading from socket");
				exit(1);
			}

			// Close the listening socket
			close(clientSocket);
			exit(0);
		} else if(pid == -1) {
			perror("Error in forking");
		} else {
			puts("Error in processing the file");
			continue;		
		}
			
	}
		
	close(network_socket);

	return 0;
}
