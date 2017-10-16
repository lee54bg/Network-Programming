// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <string.h>
#include <fcntl.h>
#define BUFFERSIZE 1024
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
	struct sockaddr_in hostaddr, remaddr;	// Initialize client and server address data structure
	struct timeval timeout;			// Set the timeout for the socket so that the socket is not stuck on read
	timeout.tv_sec = 2;
	timeout.tv_usec = 0;

	socklen_t locaddrlen = sizeof(hostaddr);	// Length of our locaddrlen
	socklen_t remaddrlen = sizeof(remaddr);		// Length of our remaddrlen
	
	network_socket = socket(AF_INET, SOCK_STREAM, 0);
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
			
			remaddr.sin_port	= htons(server_port);
			remaddr.sin_addr.s_addr	= INADDR_ANY;
			break;	
		case 4:
			ip_address		= argv[1];			// IP Address in ASCII form
			server_port		= strtol(argv[2], NULL, 10);	// Port number to be used for connecting to the server
			file_path		= argv[3];			// File name

			remaddr.sin_port	= htons(server_port);		// Port Number
			remaddr.sin_addr.s_addr	= inet_addr(ip_address);	// IP Address
			break;	
	}

	socketStatus = connect(network_socket, (struct sockaddr *) &remaddr, remaddrlen);

	if(socketStatus < 0) {
		perror("Unable to establish connection");
            	exit(EXIT_FAILURE);
	}
	
	// Send file name to the server to be read and check for errors
	if ( ( write(network_socket, file_path, sizeof(file_path)) ) == -1) {
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
		read_return = read(network_socket, buffer, BUFFERSIZE);
		
		if(read_return <= 0)
			break;

		totalBytesRcvd += read_return;

		if (read_return == -1) {
                	perror("read");
	                exit(EXIT_FAILURE);
		}

		if (write(filedes, buffer, read_return) == -1) {
			perror("Failed to write to file");
			exit(EXIT_FAILURE);
		}
		
		bzero(buffer, BUFFERSIZE);
	}

	printf("The total number of bytes received: %d\n", totalBytesRcvd);

	close(filedes);
	close(network_socket);

	return 0;
}
