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
	int lstn_scket	= 0,	// Create our listening socket descriptor
	client_socket	= 0,	// Create our client socket descriptor
	filedes		= 0,	// Creating our file descriptor
	pid		= 0,	// Used for identifying our process id (both child and parent)
	bytes_snt	= 0,	// Bytes sent for each individual send
	bytes_rcvd	= 0,	// Bytes received from client
	bytes_rd	= 0,	// Bytes read from file for each individual read
	total_bytes_snt	= 0,	// Total number of bytes sent to the client
	total_bytes_rd	= 0;	// Total number of bytes read from the file
	
	unsigned short server_port;

	char buffer[BUFFERSIZE];
	char file_path[FILENAMESIZE];

	// Creating local and remote endpoints
	struct sockaddr_in localaddr, remaddr;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	// Zero out the local and remote address
	memset( &localaddr, 0, sizeof(localaddr) );	// Zero out the local address
	memset( &remaddr, 0, sizeof(remaddr) );		// Zero out the remote address

	switch(argc) {
		case 2:
			// Port number to be used for connecting to the server	
			server_port = strtol(argv[1], NULL, 10);
			break;	
		default:
			puts("Not enough arguments.  Terminating server...");
			exit(EXIT_FAILURE);
			break;
	}

	// Create the socket with the following paremeters
	lstn_scket = socket(AF_INET, SOCK_STREAM, 0);
	
	// Check to ensure that the socket connects
	if ( lstn_scket < 0) {
		perror("Unable to create socket.  Terminating...");
		exit(EXIT_FAILURE);
	}
	
	// Set the parameters of the local address
	localaddr.sin_family		= AF_INET;
	localaddr.sin_addr.s_addr	= INADDR_ANY;
	localaddr.sin_port		= htons(server_port);

	// Ensure that the local endpoint has been binding to our created socket
	if ( bind(lstn_scket, (struct sockaddr *) &localaddr, localaddrlen ) == -1) {
		perror("Error in binding");
		exit(1);
	}

	if ( listen(lstn_scket, 5) < 0 )
		perror("Error on listen");

	while(1) {
		puts("Waiting for clients...");		
		client_socket = accept(lstn_scket, (struct sockaddr *) &remaddr, &remaddrlen);
		
		// Zero out the buffer for the file_path
		memset(file_path, 0, sizeof(file_path));

		// Read the file name request from the client
		bytes_rcvd = read(client_socket, (char *) file_path, sizeof(file_path) );
		
		filedes = open(file_path, O_RDONLY);

		if (filedes == -1) {
			perror("Error with opening file: ");
			continue;
		} else
			pid = fork();

		if(pid == 0) {
			printf("Child process %d initiated\n", getpid());
			close(lstn_scket);

			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
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
				bytes_snt = write(client_socket, buffer, bytes_rd);
				
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
			read(client_socket, buffer, BUFFERSIZE);
			
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
			close(client_socket);
			exit(0);
		} else if(pid == -1) {
			perror("Error in child processes");
		} else if(pid > 0) {	// Goes back to accept
			close(client_socket);
			continue;		
		}
			
	}
		
	close(lstn_scket);

	return 0;
}
