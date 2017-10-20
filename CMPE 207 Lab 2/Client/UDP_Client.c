// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <fcntl.h>
#define BUFFERSIZE 512
#define FILENAMESIZE 256

int main(int argc, char **argv) {
	int network_socket	= 0,	// Create socket
	filedes			= 0,	// Create File Descriptor
	socketStatus		= 0,	// Monitor status of the socket that's being created
	bytesSnt		= 0,
	bytesRcvd		= 0,
	totalBytesSnt		= 0,	// Total number of bytes sent to the server
	total_bytes_rcvd	= 0;	// Total number of bytes received from the server
		
	char buffer[BUFFERSIZE];	// Buffer to be used for reading and writing data
	char *file_path;	// The file name to be sent to the server
	char *ip_address;	// The name of the ip address to be used to connect to the server
	ssize_t read_return;	// Size of the buffer to be used for reading and writing to the file

	unsigned short server_port = 12345;	// Port number to be used for connection
	struct hostent *server_info;		// Used to hold server information
	struct sockaddr_in remaddr;		// Initialize client and server address data structure
	struct timeval timeout;			// Set the timeout for the socket so that the socket is not stuck on read
	timeout.tv_sec = 2;
	timeout.tv_usec = 0;

	// Length of our remaddrlen
	socklen_t remaddrlen = sizeof(remaddr);
	
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);
	setsockopt(network_socket, SOL_SOCKET, SO_RCVTIMEO, (char *) &timeout, sizeof(timeout));

	if ( network_socket < 0)
		printf("Socket doesn't work");

	memset(&remaddr, 0, sizeof(remaddr));
	memset(buffer, 0, sizeof(buffer));
	remaddr.sin_family	= AF_INET;
	
	switch(argc) {	
		case 4:
			ip_address		= argv[1];			// IP Address in ASCII form
			server_port		= strtol(argv[2], NULL, 10);	// Port number to be used for connecting to the server
			file_path		= argv[3];			// File name

			remaddr.sin_port	= htons(server_port);		// Port Number of our server
			remaddr.sin_addr.s_addr	= inet_addr(ip_address);	// IP Address of our server
			break;	
	}

	// Send file name to the server to be read and check for errors
	if ( ( sendto(network_socket, file_path, FILENAMESIZE, 0, (struct sockaddr*) &remaddr, remaddrlen) ) < 0) {
		perror("Failed to write to file");
		exit(EXIT_FAILURE);
	}

	filedes = open(file_path,
		O_WRONLY | O_CREAT | O_TRUNC,
		S_IRUSR | S_IWUSR);
	
	if (filedes == -1) {
		perror("Error in creating file");
            	exit(EXIT_FAILURE);
	}
	

	while(1) {
		// Read the contents from the network socket
		bytesRcvd = recvfrom(network_socket, buffer, BUFFERSIZE, 0, (struct sockaddr*) &remaddr, &remaddrlen);
		
		// Break from the loop if there are no more data to be read
		if(bytesRcvd <= 0)
			break;

		// Sum up the total number of bytes read
		total_bytes_rcvd += bytesRcvd;

		// Exit if there's an error reading the socket
		if (bytesRcvd < 0) {
                	perror("read");
	                exit(EXIT_FAILURE);
		}

		bytesSnt = write(filedes, buffer, bytesRcvd);
				
		totalBytesSnt += bytesSnt;
				
		if ( bytesSnt < 0) {
			perror("ERROR sending to socket ");
			exit(1);
		}
		
		// Clear the buffer with null characters
		memset(buffer, '\0', BUFFERSIZE);
	}

	printf("The total number of bytes received: %d\n", total_bytes_rcvd);

	close(filedes);
	close(network_socket);

	return 0;
}
