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
	totalBytesRcvd		= 0;	// Total number of bytes received from the server
		
	char buffer[BUFFERSIZE];	// Buffer to be used for reading and writing data
	char *file_path;	// The file name to be sent to the server
	char *ip_address;	// The name of the ip address to be used to connect to the server
	ssize_t read_return;	// Size of the buffer to be used for reading and writing to the file

	unsigned short server_port = 12345;	// Port number to be used for connection
	struct hostent *server_info;		// Used to hold server information
	struct sockaddr_in remaddr;	// Initialize client and server address data structure
	struct timeval timeout;			// Set the timeout for the socket so that the socket is not stuck on read
	timeout.tv_sec = 2;
	timeout.tv_usec = 0;

	socklen_t remaddrlen = sizeof(remaddr);		// Length of our remaddrlen
	
	network_socket = socket(AF_INET, SOCK_DGRAM, 0);
	setsockopt(network_socket, SOL_SOCKET, SO_RCVTIMEO, (char *) &timeout, sizeof(timeout));

	if ( network_socket < 0)
		printf("Socket doesn't work");

	memset((char *) &remaddr, 0, sizeof(remaddr));
	memset((char *) &buffer, 0, sizeof(buffer));
	remaddr.sin_family	= AF_INET;
	
	switch(argc) {
		case 2:
			file_path = argv[1];
			remaddr.sin_port	= htons(3002);
			remaddr.sin_addr.s_addr	= INADDR_ANY;
			break;	
		case 3:
			server_port		= strtol(argv[1], NULL, 10);	// Port number to be used for connecting to the server
			file_path = argv[2];
			
			remaddr.sin_port	= htons(server_port);		// Port Number of our server
			remaddr.sin_addr.s_addr	= INADDR_ANY;			// Any IP address to be used locally
			break;	
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
	
	char *tempbuff;
	while(1) {
		bytesRcvd = recvfrom(network_socket, tempbuff, sizeof(tempbuff), 0, (struct sockaddr*) &remaddr, &remaddrlen);
		
		puts(buffer);

		if(bytesRcvd <= 0)
			break;

		totalBytesRcvd += bytesRcvd;

		if (bytesRcvd < 0) {
                	perror("read");
	                exit(EXIT_FAILURE);
		}

		bytesSnt = write(filedes, buffer, BUFFERSIZE);
				
		totalBytesSnt += bytesSnt;
				
		if ( bytesSnt < 0) {
			perror("ERROR sending to socket ");
			exit(1);
		}
		
		memset(buffer, '\0', BUFFERSIZE);
	}

	printf("The total number of bytes received: %d\n", totalBytesRcvd);

	memset(buffer, 0, BUFFERSIZE);
	sprintf(buffer, "%d", totalBytesRcvd);
	write(network_socket, buffer, BUFFERSIZE);	// Sending the amount of bytes received to the server

	close(filedes);
	close(network_socket);

	return 0;
}
