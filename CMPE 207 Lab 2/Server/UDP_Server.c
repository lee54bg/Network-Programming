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
#define BUFFERSIZE 512
#define FILENAMESIZE 256

int main(int argc, char **argv) {
	int lstnScket	= 0,	// Create our listening socket
	clientSocket	= 0,	// Creating our client Socket
	socketStatus	= 0,	// Creating our socket status
	filedes		= 0,	// Creating our file Descriptor
	pid		= 0,	// Used for identifying our process id (both child and parent)
	bytesSnt	= 0,	// Bytes sent for each individual send
	bytesRcvd	= 0,	// Bytes read for each individual read
	bytesRd		= 0,	// Bytes read for each individual read
	totalBytesSnt	= 0,	// Total number of bytes sent to the client
	totalBytesRcvd	= 0,	// Total number of bytes received from the client
	totalBytesRd	= 0;	// Total number of bytes read from the file
	
	unsigned short server_port;

	char buffer[BUFFERSIZE];
	char *ip_address;
	char file_path[FILENAMESIZE];
	char *filename;

	struct sockaddr_in localaddr, remaddr;			// Creating local and remote endpoints
	struct hostent *clientinfo;
	ssize_t read_return;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	memset( (char *) &localaddr, 0, sizeof(localaddr) );	// Zero out the local address

	switch(argc) {
		case 2:
			server_port = strtol(argv[1], NULL, 10);	// Port number to be used for connecting to the server	
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
	if ( bind(lstnScket, (struct sockaddr *) &localaddr, localaddrlen ) < 0) {
		perror("Error in binding");
		exit(1);
	}

	while(1) {
		puts("Waiting for clients...");
				
		memset(file_path, 0, sizeof(file_path));	// Zero out the buffer for the file_path

		bytesRcvd	= recvfrom(lstnScket, file_path, FILENAMESIZE, 0, (struct sockaddr*) &remaddr, &remaddrlen);
		clientinfo	= gethostbyaddr((char*) &remaddr.sin_addr.s_addr, sizeof(remaddr.sin_addr.s_addr), AF_INET);

		puts(file_path);	// Debugging purposes

		filedes = open(file_path, O_RDONLY);

		if (filedes == -1) {
			perror("Error with opening file: ");
			continue;
		} else {
			pid = fork();

			if(pid == 0) {
				printf("Child process %d\n", getpid());
				
				//totalBytesRd	= 0;
				//totalBytesSnt	= 0;

				// Run until the process of reading from the file and sending to the client is finished					
				while(1) {
					read_return = read(filedes, buffer, BUFFERSIZE);
					
					// puts(buffer);

					totalBytesRd += read_return;						

					// Break out of loop if there's no more data to be read
					if (read_return <= 0)
					    break;

					// Exit if there's an error in reading
					if (read_return < 0) {
					    perror("Error in reading the file: ");
					    exit(EXIT_FAILURE);
					}

					// Send content over to the remote endpoint
					bytesSnt = sendto(lstnScket, buffer, read_return, 0, (struct sockaddr*) &remaddr, remaddrlen);
				
					totalBytesSnt += bytesSnt;
				
					if ( bytesSnt < 0) {
						perror("ERROR sending to socket ");
						exit(1);
					}

					memset(buffer, '\0', BUFFERSIZE);					
				}

				printf("The total number of bytes read from file: %d\n", totalBytesRd);
				printf("The total number of bytes sent: %d\n", totalBytesSnt);
			
				memset(buffer, 0, BUFFERSIZE);
				recvfrom(lstnScket, buffer, BUFFERSIZE, 0, (struct sockaddr*) &remaddr, &remaddrlen);
				clientinfo = gethostbyaddr((char*) &remaddr.sin_addr.s_addr, sizeof(remaddr.sin_addr.s_addr), AF_INET);

				bytesRcvd = atoi(buffer);
				printf("From client: %d\n", bytesRcvd);

				if (totalBytesRd == totalBytesSnt) {
					puts("File has been successfully sent");

					if (bytesRcvd == totalBytesSnt)
						puts("Client has successfully received file");
					else
						puts("Error occured during file transmission");
				} else
					puts("Error occured during file transmission");

				// Close the listening socket
				close(lstnScket);
				exit(0);
			} else if(pid == -1) {
				perror("Error in creating child processes");
			} else {
				continue;
			}
		}	
	}
		
	close(lstnScket);

	return 0;
}
