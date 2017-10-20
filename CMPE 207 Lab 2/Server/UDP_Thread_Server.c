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
#define BUFFERSIZE 512
#define FILENAMESIZE 256

typedef struct Request {
	socklen_t fromlen;
	struct sockaddr_in from;
	char file_path[FILENAMESIZE];
} Request;

void *client_handler(void *client_info);

int lstnScket	= 0;	// Create our listening socket outside for all threads to use

int main(int argc, char **argv) {
	int bytesRcvd		= 0,	// Bytes read for each individual read
	totalBytesRcvd		= 0;	// Total number of bytes received from the client
	
	unsigned short server_port = 12345;

	char *ip_address;

	struct sockaddr_in localaddr;			// Creating local and remote endpoints

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	
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
	lstnScket = socket(AF_INET, SOCK_DGRAM, 0);
	
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

	while(1) {
		puts("Waiting for clients...");
		// create struct to hold client info and client request
		struct Request *request = (Request*) malloc(sizeof(Request));
		memset(request->file_path, 0, sizeof(request->file_path));	// Zero out the buffer for the file_path
		request->fromlen = sizeof(struct sockaddr_in);

		// store client info and client request into struct
		bytesRcvd	= recvfrom(lstnScket, request->file_path, FILENAMESIZE, 0, (struct sockaddr*) &request->from, &request->fromlen);	// Read the file name request from the client
		// check bytes received
		if(bytesRcvd < 0) {
			perror("recvfrom");
		}

		puts(request->file_path);	// Debugging purposes

		pthread_t client_thread;	// Create thread

		if( (pthread_create(&client_thread, NULL, client_handler, (void*) request)) < 0 ) {
			perror("Error in thread creation: ");
		}
	}
		
	close(lstnScket);

	return 0;
}

void *client_handler(void *client_info) {
	printf("Thread %ld Initialized\n", pthread_self());

	// casting client_info back to struct
	Request *request = (Request*) client_info;

	int filedes	= 0,	// Creating our file Descriptor
	bytesSnt	= 0,	// Bytes sent for each individual send
	bytesRcvd	= 0,	// Bytes read for each individual read
	totalBytesSnt	= 0,	// Total number of bytes sent to the client
	totalBytesRcvd	= 0,	// Total number of bytes received from the client
	totalBytesRd	= 0;	// Total number of bytes read from the file

	char buffer[BUFFERSIZE];

	ssize_t read_return;
	
	filedes = open(request->file_path, O_RDONLY);

	if (filedes == -1) {
		perror("Error with opening file: ");
		exit(EXIT_FAILURE);
	}

	// Run until the process of reading from the file and sending to the client is finished					
	while(1) {
		read_return = read(filedes, buffer, BUFFERSIZE);

		totalBytesRd += read_return;

		// Break out of loop if there's no more data to be read
		if (read_return == 0)
		    break;

		// Exit if there's an error in reading
		if (read_return == -1) {
		    perror("Error in reading the file: ");
		    exit(EXIT_FAILURE);
		}

		// Send content over to the remote endpoint
		bytesSnt = sendto(lstnScket, buffer, read_return, 0, (struct sockaddr*) &request->from, request->fromlen);
		
		totalBytesSnt += bytesSnt;
		
		if ( bytesSnt < 0) {
			perror("ERROR sending to socket ");
			exit(1);
		}

		bzero(buffer, BUFFERSIZE);
	}

	printf("Thread %ld finished sending...\n", pthread_self());
	printf("The total number of bytes read from file: %d\n", totalBytesRd);
	printf("The total number of bytes sent: %d\n", totalBytesSnt);
	
	free(request);

	// Close the thread
	pthread_exit(NULL);
}
