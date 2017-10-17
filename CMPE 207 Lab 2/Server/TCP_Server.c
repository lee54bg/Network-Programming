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
#define BUFFERSIZE 1024
#define FILENAMESIZE 256

int main(int argc, char **argv) {
	int lstnScket	= 0,	// Create our listening socket
	clientSocket	= 0,	// Creating our client Socket
	socketStatus	= 0,	// Creating our socket status
	filedes		= 0,	// Creating our file Descriptor
	pid		= 0,	// Used for identifying our process id (both child and parent)
	bytesSnt	= 0,	// Bytes sent for each individual send
	bytesRcvd	= 0,	// Bytes read for each individual read
	totalBytesSnt	= 0,	// Total number of bytes sent to the client
	totalBytesRd	= 0;	// Total number of bytes read from the file
	
	unsigned short server_port = 12345;

	char buffer[BUFFERSIZE];
	char *ip_address;
	char file_path[FILENAMESIZE];
	char *filename;

	struct sockaddr_in localaddr, remaddr;			// Creating local and remote endpoints

	ssize_t read_return;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
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
			exit(EXIT_FAILURE);
			break;
	}

	// Create the socket with the following paremeters
	lstnScket = socket(AF_INET, SOCK_STREAM, 0);
	
	// Check to ensure that the socket connects
	if ( lstnScket < 0) {
		perror("Unable to create socket.  Terminating...");
		exit(EXIT_FAILURE);
	}
	
	// Set the parameters of the local address
	localaddr.sin_family		= AF_INET;
	localaddr.sin_addr.s_addr	= INADDR_ANY;
	localaddr.sin_port		= htons(server_port);

	// Ensure that the local endpoint has been binding to our created socket
	if ( bind(lstnScket, (struct sockaddr *) &localaddr, localaddrlen ) == -1) {
		perror("Error in binding");
		exit(1);
	}

	if ( listen(lstnScket, 5) < 0 )
		perror("Error on listen");

	while(1) {
		puts("Waiting for clients...");
		clientSocket = accept(lstnScket, (struct sockaddr *) &remaddr, &remaddrlen);
				
		memset(file_path, 0, sizeof(file_path));	// Zero out the buffer for the file_path
		bytesRcvd = read(clientSocket, (char *) file_path, sizeof(file_path) );	// Read the file name request from the client
		
		totalBytesSnt	= 0;	// Set total sent to 0
		totalBytesRd	= 0;	// Set total read to 0
		

		filedes = open(file_path, O_RDONLY);	// File Descriptor for the open file

		if (filedes == -1) {
			perror("Error with opening file: ");
		} else {
			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
				read_return = read(filedes, buffer, BUFFERSIZE);
	
				totalBytesRd += read_return;	// Set total amount of bytes read

				// Break out of loop if there's no more data to be read
				if (read_return == 0)
				    break;

				// Exit if there's an error in reading
				if (read_return == -1) {
				    perror("Error in reading the file: ");
				    exit(EXIT_FAILURE);
				}

				// Send content over to the remote endpoint
				bytesSnt = write(clientSocket, buffer, read_return);
			
				totalBytesSnt += bytesSnt;	// Set total amount of bytes that have been sent
			
				if ( bytesSnt < 0) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, 0, BUFFERSIZE);
			}

			printf("The total number of bytes read from file: %d\n", totalBytesRd);
			printf("The total number of bytes sent to client: %d\n", totalBytesSnt);
		
			memset(buffer, 0, BUFFERSIZE);
			read(clientSocket, buffer, BUFFERSIZE);
		
			bytesRcvd = atoi(buffer);
			printf("From client: %d\n", bytesRcvd);

			// Do a integrity check and ensure that the client has successfully received the file
			if (totalBytesRd == totalBytesSnt) {
				puts("File has been successfully sent");

				if (bytesRcvd == totalBytesSnt)
					puts("Client has successfully received file");
				else
					puts("Error occured during file transmission");
			} else
				puts("Error occured during file transmission");

			// Close the listening socket
			close(clientSocket);
		}
	}
		
	close(lstnScket);	// Close listening socket though this is just for safety measures

	return 0;
}
