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
#include <pthread.h>
#define BUFFERSIZE 1024
#define FILENAMESIZE 256

void *client_handler(void *client_socket);

int main(int argc, char **argv) {
	int lstnScket	= 0,	// Create our listening socket
	clientSocket	= 0,	// Creating our client Socket
	filedes		= 0,	// Creating our file Descriptor
	pid		= 0;	// Used for identifying our process id (both child and parent)
	
	unsigned short server_port;

	char file_path[FILENAMESIZE];

	struct sockaddr_in localaddr, remaddr;			// Creating local and remote endpoints

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	memset( &localaddr, 0, localaddrlen );	// Zero out the local address
	memset( &remaddr, 0, remaddrlen );	// Zero out the remote address

	switch(argc) {
		case 2:
			server_port	= strtol(argv[1], NULL, 10);	// Port number to be used for connecting to the server	
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

		pthread_t client_thread;	// Create thread

		if( (pthread_create(&client_thread, NULL, client_handler, (void*) &clientSocket)) < 0 ) {
			perror("Error in thread creation: ");
		}
	}
		
	close(lstnScket);

	return 0;
}

void *client_handler(void *client_socket) {
	int clientSocket = *(int*) client_socket,	// Creating our client Socket
	filedes		= 0,	// Creating our file Descriptor
	bytes_snt	= 0,	// Bytes sent for each individual send
	bytes_rcvd	= 0,	// Bytes read for each individual read
	bytes_rd	= 0,	// Bytes read for each individual read
	total_bytes_snt		= 0,	// Total number of bytes sent to the client
	total_bytes_rcvd	= 0,	// Total number of bytes received from the client
	total_bytes_rd		= 0;	// Total number of bytes read from the file

	char buffer[BUFFERSIZE];
	char file_path[FILENAMESIZE];

	// Zero out the buffer for the file_path
	memset(file_path, 0, sizeof(file_path));
	// Read the file name request from the client
	bytes_rcvd = read(clientSocket, &file_path, sizeof(file_path) );
	
	filedes = open(file_path, O_RDONLY);

	if (filedes == -1) {
		perror("Error with opening file: ");
		exit(EXIT_FAILURE);
	}

	// Run until the process of reading from the file and sending to the client is finished					
	while(1) {
		printf("Thread ID %ld initiated\n", pthread_self());

		bytes_rd = read(filedes, buffer, BUFFERSIZE);

		total_bytes_rd += bytes_rd;

		// Break out of loop if there's no more data to be read
		if (bytes_rd == 0)
		    break;

		// Exit if there's an error in reading
		if (bytes_rd == -1) {
		    perror("Error in reading the file: ");
		    exit(EXIT_FAILURE);
		}

		// Send content over to the remote endpoint
		bytes_snt = write(clientSocket, buffer, bytes_rd);
		
		total_bytes_snt += bytes_snt;
		
		if ( bytes_snt < 0) {
			perror("ERROR sending to socket ");
			exit(1);
		}

		memset(buffer, 0, BUFFERSIZE);
	}

	printf("The total number of bytes read from file: %d\n", total_bytes_rd);
	printf("The total number of bytes sent: %d\n", total_bytes_snt);
	
	memset(buffer, 0, BUFFERSIZE);
	read(clientSocket, buffer, BUFFERSIZE);
	
	bytes_rcvd = atoi(buffer);

	if (total_bytes_rd == total_bytes_snt) {
		puts("File has been successfully sent");

		if (bytes_rcvd == total_bytes_snt)
			puts("Client has successfully received file");
		else
			puts("Error occured during file transmission");
	} else
		puts("Error occured during file transmission");

	// Close the listening socket
	close(filedes);
	close(clientSocket);

	pthread_exit(NULL);
}
