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
#define BUFSIZE 1024

int main(int argc, char **argv) {
	int network_socket	= 0,	// Create socket
		clientSocket	= 0,	// Creating our client Socket
		socketStatus	= 0,	// Creating our socket status
		filedes		= 0;	// Creating our file Descriptor
	
	unsigned short server_port = 12345;

	char *file_path = "Programming.txt";	// Name of our file just for testing purposes
	char buffer[BUFSIZ];
	
	// Sets file name if there's an argument
	if( argc > 1 ) {
		file_path = argv[1];
		if (argc > 2) {
			server_port = strtol(argv[2], NULL, 10);
		}
	}

	ssize_t read_return;

	// Creating local and remote endpoints
	struct sockaddr_in localaddr, remaddr;

	// Getting the length of both local and remote sockets
	socklen_t localaddrlen	= sizeof(localaddr);
	socklen_t remaddrlen	= sizeof(remaddr);
	
	puts("Before open");
	
	filedes = open(file_path, O_RDONLY);

	if (filedes == -1) {
	        perror("open");
		exit(EXIT_FAILURE);
	}
	
	printf("open works");

	// Create the socket with the following paremeters
	network_socket = socket(AF_INET, SOCK_STREAM, 0);
	
	// Check to ensure that the socket connects
	if ( network_socket < 0) {
		perror("Socket cannot connect");
		exit(1);
	}
	
	// Zero out the local address
	memset( (char *) &localaddr, 0, sizeof(localaddr) );

	// Set the parameters of the local address
	localaddr.sin_family = AF_INET;
	localaddr.sin_addr.s_addr = INADDR_ANY;
	localaddr.sin_port = htons(server_port);

	// Ensure that the local endpoint has been binding to our created socket
	if ( bind(network_socket, (struct sockaddr *) &localaddr, localaddrlen ) == -1)
		perror("Error in binding");

	if ( listen(network_socket, 5) < 0 )
		perror("Error on listen");
	
	clientSocket = accept(network_socket, (struct sockaddr *) &remaddr, &remaddrlen);

	while(1) {
		read_return = read(filedes, buffer, BUFSIZ);
		
		// Break out of loop if there's no more data to be read
		if (read_return == 0)
		    break;

		// Exit if there's an error in reading
		if (read_return == -1) {
		    perror("read");
		    exit(EXIT_FAILURE);
		}

		// Send content over to the remote endpoint
		socketStatus = write(clientSocket, buffer, read_return);
	
		if (socketStatus < 0) {
			perror("ERROR reading from socket");
		}

	}
		
	// Close the listening socket
	close(network_socket);

	return 0;
}
