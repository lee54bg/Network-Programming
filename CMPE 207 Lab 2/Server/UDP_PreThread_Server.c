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

void *client_handler(void *client_socket);

int main(int argc, char **argv) {
	int lstn_scket		= 0,	// Create our listening socket outside for all threads to use
	bytes_rcvd		= 0,	// Bytes read for each individual read
	thread_count		= 0,	// Count threads
	total_bytes_rcvd	= 0;	// Total number of bytes received from the client
	
	unsigned short server_port;

	// Creating local and remote endpoints
	struct sockaddr_in localaddr;

	// Creating local and remote endpoints
	struct hostent *get_server_info;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	
	// Zero out the local address
	memset( &localaddr, 0, sizeof(localaddr) );

	// Create the socket with the following paremeters
	lstn_scket = socket(AF_INET, SOCK_DGRAM, 0);
	
	// Check to ensure that the socket connects
	if ( lstn_scket < 0) {
		perror("Unable to create socket.  Terminating...");
		exit(EXIT_FAILURE);
	}

	localaddr.sin_family = AF_INET;

	switch(argc) {
		case 3:
			server_port	= strtol(argv[1], NULL, 10);	// Port number to be used for connecting to the server	
			thread_count	= atoi(argv[2]);		// number of threads pre-created

			localaddr.sin_addr.s_addr	= INADDR_ANY;
			localaddr.sin_port		= htons(server_port);
			break;	
		default:
			puts("Invalid arguments.  Terminating server...");
			exit(EXIT_FAILURE);
	}
	
	// Ensure that the local endpoint has been binding to our created socket
	if ( bind(lstn_scket, (struct sockaddr *) &localaddr, localaddrlen ) == -1) {
		perror("Error in binding");
		exit(1);
	}

	int count;
	
	pthread_t client_thread[thread_count];	// Create thread

	for(count = 0; count < thread_count; ++count) {
		if( (pthread_create(&client_thread[count], NULL, client_handler, (void*) &lstn_scket)) < 0 ) {
			perror("Error in thread creation: ");
		}
	}

	for(count = 0; count < thread_count; ++count) {
		pthread_join(client_thread[count], NULL);
	}
	
	close(lstn_scket);

	return 0;
}

// Start of client_handler
void *client_handler(void *listen_socket) {
	while(1) {
		int lstn_scket	= *(int*) listen_socket,	// Listening Socket
		filedes		= 0,				// Creating our file Descriptor
		bytes_snt	= 0,				// Bytes sent for each individual send
		bytes_rcvd	= 0,				// Bytes read for each individual read
		bytes_rd	= 0,				// Bytes read from file for each individual read
		total_bytes_snt	= 0,				// Total number of bytes sent to the client
		total_bytes_rcvd	= 0,			// Total number of bytes received from the client
		total_bytes_rd		= 0;			// Total number of bytes read from the file		

		struct sockaddr_in remaddr;
		struct hostent *clientinfo;
		socklen_t remaddrlen = sizeof(remaddr);
	
		char buffer[BUFFERSIZE];
		char file_path[FILENAMESIZE];

		printf("Thread %ld waiting for clients...\n", pthread_self());

		// Zero out the buffer for the file_path
		memset( &remaddr, 0, sizeof(remaddr) );

		recvfrom(lstn_scket, file_path, FILENAMESIZE, 0, (struct sockaddr*) &remaddr, &remaddrlen);
		clientinfo = gethostbyaddr((char*) &remaddr.sin_addr.s_addr, sizeof(remaddr.sin_addr.s_addr), AF_INET);

		filedes = open(file_path, O_RDONLY);

		/* See if the file is openable.  If not then go back to recvfrom.
		 * If it is, then proceed with normal operations
		 */
		if (filedes == -1) {
			perror("Error with opening file: ");
		} else {
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
				bytes_snt = sendto(lstn_scket, buffer, bytes_rd, 0, (struct sockaddr*) &remaddr, remaddrlen);
		
				total_bytes_snt += bytes_snt;
		
				if ( bytes_snt < 0) {
					perror("ERROR sending to socket ");
					exit(1);
				}

				memset(buffer, 0, BUFFERSIZE);
			}

			printf("The total number of bytes read from file: %d\n", total_bytes_rd);
			printf("The total number of bytes sent: %d\n", total_bytes_snt);
			printf("Thread %ld finished sending...\n\n", pthread_self());

			// Close your file descriptors
			close(filedes);
		}
	} // End of while loop
} // End of client_handler
