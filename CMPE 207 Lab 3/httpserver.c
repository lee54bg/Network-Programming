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
#define BUFFERSIZE 576

int create_socket();

int main(int argc, char **argv) {
	int lstn_scket	= 0,	// Create our listening socket
	client_socket	= 0,	// Creating our client Socket
	filedes		= 0,	// Creating our file Descriptor
	pid		= 0,	// Used for identifying our process id (both child and parent)
	bytes_snt	= 0,	// Bytes sent for each individual send
	bytes_rcvd	= 0,	// Bytes read for each individual read
	bytes_rd	= 0,	// Bytes read for each individual read
	total_bytes_sent	= 0,	// Total number of bytes sent to the client
	total_bytes_rd		= 0;	// Total number of bytes read from the file

	// Our port number for our listening socket
	int server_port;
	
	// Buffer used for sending and receiving data
	char buffer[BUFFERSIZE];
	
	// Creating our local and remote endpoints data structures
	struct sockaddr_in localaddr, remaddr;

	socklen_t localaddrlen	= sizeof(localaddr);	// Length of our local address
	socklen_t remaddrlen	= sizeof(remaddr);	// Length of our remote address
	
	memset( &localaddr, 0, localaddrlen );	// Zero out the local address
	memset( &remaddr, 0, remaddrlen );		// Zero out the remote address

	switch(argc) {
		case 2:
			// Port number to be used for connecting to the server
			server_port = atoi(argv[1]);
			break;	
		default:
			puts("Not enough arguments.  Terminating server...");
			exit(EXIT_FAILURE);
	}

	// Create the socket with the following paremeters
	lstn_scket = create_socket();
	
	// Set the parameters of the local address
	localaddr.sin_family		= AF_INET;
	localaddr.sin_addr.s_addr	= INADDR_ANY;
	localaddr.sin_port		= htons(server_port);

	// Ensure that the local endpoint has been binding to our created socket
	if ( bind(lstn_scket, (struct sockaddr *) &localaddr, localaddrlen ) < 0) {
		perror("Error in binding");
		exit(EXIT_FAILURE);
	}

	// Start accepting requests on the listening socket with a queue length of 5
	if ( listen(lstn_scket, 5) == -1 ) {
		perror("Error on listen");
		exit(EXIT_FAILURE);
	}

	while(1) {
		puts("Waiting for clients...");
		client_socket = accept(lstn_scket, (struct sockaddr *) &remaddr, &remaddrlen);
				
		// Zero out the buffer for the file_path
		memset(file_path, 0, sizeof(file_path));

		// Read the file name request from the client
		bytes_rcvd = read(client_socket, (char *) file_path, sizeof(file_path) );
		
		total_bytes_sent	= 0;	// Set total sent to 0
		total_bytes_rd		= 0;	// Set total read to 0
		
		// File Descriptor for the open file
		filedes = open(file_path, O_RDONLY);

		if (filedes == -1) {
			perror("Error with opening file: ");
		} else {
			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
				bytes_rd = read(filedes, buffer, BUFFERSIZE);
	
				// Set total amount of bytes read
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
				bytes_snt = write(client_socket, buffer, bytes_rd);
			
				// Set total amount of bytes that have been sent
				total_bytes_sent += bytes_snt;
			
				if ( bytes_snt < 0) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, 0, BUFFERSIZE);
			}

			
			close(client_socket);
		}
	}
	
	// Close listening socket though this is just for safety measures	
	close(lstn_scket);

	return 0;
}

int create_socket() {
	int lstn_scket = 0;

	// Create the socket with the following paremeters
	lstn_scket = socket(AF_INET, SOCK_STREAM, 0);
	
	// Check to ensure that the socket connects
	if ( lstn_scket < 0) {
		perror("Unable to create socket.  Terminating...");
		exit(EXIT_FAILURE);
	}
	
	return lstn_scket;
}
