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
	// Create our listening socket
	int lstn_scket	= 0,
	// Creating our client Socket
	client_socket	= 0,
	// Creating our file Descriptor
	file_desc	= 0,
	// Bytes sent for each individual send
	bytes_snt	= 0,
	// Bytes read for each individual read
	bytes_rd	= 0,
	// Our port number for our listening socket
	server_port	= 0;
	
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
	
	// Used to store the GET request from the client
	char *get		= (char *) malloc(BUFFERSIZE);
	// Used to store the name of the file
	char *file_name		= (char *) malloc(BUFFERSIZE / 2);
	// Used to ignore the slash that's present in the file name
	char *new_file_name	= (char *) malloc(BUFFERSIZE / 2);

	while(1) {
		puts("Waiting for clients...");
		client_socket = accept(lstn_scket, (struct sockaddr *) &remaddr, &remaddrlen);

		// HTTP headers for 200 and 404
		char http_ok[]		= "HTTP/1.1 200 OK\n";
		char http_not_found[]	= "HTTP/1.1 404 Not Found\n";
		
		// Read the request of the client and put that in the buffer
		recv(client_socket, buffer, sizeof(buffer), 0);
		
		// Parse the request and put the get request
		// and file name in their respective variables
		sscanf(buffer, "%s %s", get, file_name);
		
		// This will put the file name without the / onto new_file_name
		new_file_name = file_name + 1;

		// File Descriptor for the open file
		file_desc = open(new_file_name, O_RDONLY);

		// Handle 404 and 200 requests in the if else statement
		if (file_desc == -1) {
			printf("File name: %s sent\n", file_name);
			puts("Sending 404");
			send(client_socket, http_not_found, strlen(http_not_found), 0);
			
			file_desc = open("not_found.html", O_RDONLY);
		
			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
							
				bytes_rd = read(file_desc, buffer, BUFFERSIZE);

				// Exit if there's nothing more to read from the buffer
				if (bytes_rd == -1) {
					break;
				} else if (bytes_rd == 0)
					break;
			
				// Send content over to the remote endpoint
				bytes_snt = send(client_socket, buffer, bytes_rd, 0);
			
				if (bytes_snt == -1) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, 0, BUFFERSIZE);
			}
		} else {
			send(client_socket, http_ok, strlen(http_ok), 0);
			
			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
				bytes_rd = read(file_desc, buffer, BUFFERSIZE);

				// Exit if there's nothing more to read from the buffer
				if (bytes_rd == -1) {
					break;
				} else if (bytes_rd == 0)
					break;
			
				// Send content over to the remote endpoint
				bytes_snt = send(client_socket, buffer, bytes_rd, 0);
			
				if (bytes_snt == -1) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, 0, BUFFERSIZE);
			}
		}

		close(client_socket);
		puts("Transaction finished");
	}
	
	free(get);
	free(file_name);
	free(new_file_name);
	
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

	int enable = 1;
	// Reuse the IP address
	setsockopt(lstn_scket, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(int) < 0);
	
	return lstn_scket;
}
