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
	file_desc	= 0,	// Creating our file Descriptor
	bytes_snt	= 0,	// Bytes sent for each individual send
	bytes_rcvd	= 0,	// Bytes read for each individual read
	bytes_rd	= 0;	// Bytes read for each individual read
	
	// Our port number for our listening socket
	int server_port;
	
	// Buffer used for sending and receiving data
	char buffer[BUFFERSIZE];

	// Creating our local and remote endpoints data structures
	struct sockaddr_in localaddr, remaddr;

	// Setting the length of the remote and local address
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	// Zeroing out the local and remote address
	memset( &localaddr, 0, localaddrlen );
	memset( &remaddr, 0, remaddrlen );

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

		char *http_ok		= malloc(strlen("HTTP/1.1 200 OK\n") + 1);
		char *http_not_found	= malloc(strlen("HTTP/1.1 404 Not Found\n") + 1);
		char *get		= malloc(BUFFERSIZE);
		char *file_name		= malloc(BUFFERSIZE / 2);

		http_ok = "HTTP/1.1 200 OK\n";
		http_not_found = "HTTP/1.1 404 Not Found\n";

		// Read the request of the client and put that in the buffer
		bytes_rcvd = recv(client_socket, buffer, sizeof(buffer), 0);
	
		sscanf(buffer, "%s %s", get, file_name);

		// File Descriptor for the open file
		file_desc = open(file_name, O_RDONLY);

		if (file_desc == -1) {
			perror("Error with opening file: ");
			send(client_socket, http_not_found, strlen(http_not_found), 0);
		} else {
			send(client_socket, http_ok, strlen(http_ok), 0);

			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
				bytes_rd = read(file_desc, buffer, BUFFERSIZE);

				// Exit if there's an error in reading
				if (bytes_rd == -1)
					break;

				if (bytes_rd == 0)
					continue;
		
				// Send content over to the remote endpoint
				bytes_snt = write(client_socket, buffer, bytes_rd);
			
				if ( bytes_snt == -1) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, 0, BUFFERSIZE);
			}
		}

		free(get);
		free(file_name);
		free(http_ok);
		free(http_not_found);
		close(client_socket);
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
	if ( lstn_scket == -1) {
		perror("Unable to create socket.  Terminating...");
		exit(EXIT_FAILURE);
	}
	
	return lstn_scket;
}
