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
#include <wait.h>
#define BUFFERSIZE 1024
#define FILENAMESIZE 256

// Usage: ./TCP_PreFork_Server port_num num_of_children

int main(int argc, char **argv) {
	int lstn_scket	= 0,	// Create our listening socket
	clientSocket	= 0,	// Creating our client Socket
	num_of_child	= 0,	// Used for creating number of children
	filedes		= 0,	// Creating our file Descriptor
	pid		= 0,	// Used for identifying our process id (both child and parent)
	bytes_snt	= 0,	// Bytes sent for each individual send
	bytes_rcvd	= 0,	// Bytes read for each individual read
	bytes_rd	= 0,	// Bytes read for each individual read
	total_bytes_snt	= 0,	// Total number of bytes sent to the client
	total_bytes_rd	= 0;	// Total number of bytes read from the file
	
	unsigned short server_port = 12345;	// Port number to be used to bind to local endpoint

	char buffer[BUFFERSIZE];	// Buffer used for sending and receiving data
	char file_path[FILENAMESIZE];	// This will be our file name

	// Creating local and remote endpoints
	struct sockaddr_in localaddr, remaddr;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	// Zero out the local and remote address
	memset( &localaddr, 0, sizeof(localaddr) );
	memset( &remaddr, 0, sizeof(remaddr) );

	switch(argc) {
		case 3:
			// Port number to be used for connecting to the server	
			server_port	= strtol(argv[1], NULL, 10);
			num_of_child	= atoi(argv[2]);
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

	if ( listen(lstn_scket, 10) < 0 )
		perror("Error on listen: ");

	// Count number of children
	int cnt_chld;
		
	for(cnt_chld = 0; cnt_chld < num_of_child; ++cnt_chld) {
		pid = fork();
		
		if(pid == 0)
			break;	
	}

	if(pid == 0) {
		// Start Server process
		while(1) {
			// Zero out the buffer for the file_path
			memset(file_path, 0, sizeof(file_path));

			total_bytes_rd	= 0;
			total_bytes_snt	= 0;

			// Debugging purposes
			printf("Child process #%d waiting for clients\n", getpid());

			clientSocket = accept(lstn_scket, (struct sockaddr *) &remaddr, &remaddrlen);

			// Read the file name request from the client
			bytes_rcvd = read(clientSocket, (char *) file_path, sizeof(file_path) );
		
			filedes = open(file_path, O_RDONLY);

			if (filedes == -1) {
				perror("Error with opening file: ");
				continue;
			}		

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

				if (bytes_rcvd == total_bytes_snt) {
					puts("Client has successfully received file");
					printf("Child process #%d finished\n", getpid());
				}

				else
					puts("Error occured during file transmission");
			} else
				puts("Error occured during file transmission");

			close(clientSocket);
		} // End of while loop
	} else if(pid > 0) {
		wait(NULL);
	} if(pid == -1) {
		perror("Error in child processes");
	}
	
	// Close Listening socket
	close(lstn_scket);

	return 0;
}
