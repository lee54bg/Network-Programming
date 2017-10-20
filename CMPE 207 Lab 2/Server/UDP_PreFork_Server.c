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
	int lstn_scket	= 0,	// Create our listening socket
	clientSocket	= 0,	// Creating our client Socket
		numOfChild	= 0,	// Used for creating number of children
	socketStatus	= 0,	// Creating our socket status
	filedes		= 0,	// Creating our file Descriptor
	pid		= 0,	// Used for identifying our process id (both child and parent)
	bytesSnt	= 0,	// Bytes sent for each individual send
	bytesRcvd	= 0,	// Bytes read for each individual read
	bytes_rd		= 0,	// Bytes read for each individual read
	total_bytes_snt		= 0,	// Total number of bytes sent to the client
	total_bytes_rcvd	= 0,	// Total number of bytes received from the client
	total_bytes_rd		= 0;	// Total number of bytes read from the file
	
	unsigned short server_port;
	
	char file_path[FILENAMESIZE];


	char buffer[BUFFERSIZE];
//	char file_path[FILENAMESIZE];

	// Creating local and remote endpoints
	struct sockaddr_in localaddr, remaddr;
	struct hostent *clientinfo;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	// Zero out the local address
	memset( &localaddr, 0, sizeof(localaddr) );

	switch(argc) {
		case 2:
			// Port number to be used for connecting to the server	
			server_port = strtol(argv[1], NULL, 10);
			break;	
		case 3:
			server_port	= strtol(argv[1], NULL, 10);	// Port number to be used for connecting to the server
			numOfChild	= atoi(argv[2]);
			break;
		default:
			puts("Not enough arguments.  Terminating server...");
			exit(EXIT_FAILURE);
			break;
	}

	// Create the socket with the following paremeters
	lstn_scket = socket(AF_INET, SOCK_DGRAM, 0);
	
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
	if ( bind(lstn_scket, (struct sockaddr *) &localaddr, localaddrlen ) < 0) {
		perror("Error in binding");
		exit(1);
	}
	
	// Count number of children
	int cntChld;
		
	for(cntChld = 0; cntChld < numOfChild; ++cntChld) {
		pid = fork();
		
		if(pid == 0)
			break;	
	}
	

	if(pid == 0) {
		while(1) {
			

		
		// Debugging purposes
			printf("Child process #%d waiting for clients\n", getpid());
		
		// Zero out the buffer for the file_path
		memset(file_path, 0, sizeof(file_path));

		bytesRcvd	= recvfrom(lstn_scket, file_path, FILENAMESIZE, 0, (struct sockaddr*) &remaddr, &remaddrlen);
		clientinfo	= gethostbyaddr((char*) &remaddr.sin_addr.s_addr, sizeof(remaddr.sin_addr.s_addr), AF_INET);
		
			printf("file_path variable has: %s \n", file_path);
			printf("file_path length: %lu \n", strlen(file_path));

//			if(strlen(file_path) == 0){
//			continue;	
//			}
			
			
//			puts(file_path);
			if(bytesRcvd == -1) {
				printf("Child process #%d threw error\n", getpid());
				perror("Error on reading filename ");
				exit(EXIT_FAILURE);
			}

	//		getchar();
		filedes = open(file_path, O_RDONLY);

		if (filedes == -1) {
			perror("Error with opening file: ");
			continue;
//						getchar();

		}

			// Run until the process of reading from the file and sending to the client is finished					
			while(1) {
				bytes_rd = read(filedes, buffer, BUFFERSIZE);
				
				total_bytes_rd += bytes_rd;						

				// Break out of loop if there's no more data to be read
				if (bytes_rd == 0)
				    break;

				// Exit if there's an error in reading
				if (bytes_rd < 0) {
				    perror("Error in reading the file: ");
				    exit(EXIT_FAILURE);
				}

				// Send content over to the remote endpoint
				bytesSnt = sendto(lstn_scket, buffer, bytes_rd, 0, (struct sockaddr*) &remaddr, remaddrlen);
			
				total_bytes_snt += bytesSnt;
			
				if ( bytesSnt < 0) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, '\0', BUFFERSIZE);					
			}

			printf("The total number of bytes read from file: %d\n", total_bytes_rd);
			printf("The total number of bytes sent: %d\n", total_bytes_snt);
		
		}
	}
	

	else if(pid > 0) {
		wait(NULL);
	} if(pid == -1) {
		perror("Error in child processes");
	}

	// Close the listening socket
	close(lstn_scket);

	return 0;
}

