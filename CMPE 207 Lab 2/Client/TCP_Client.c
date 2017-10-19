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
#define BUFFERSIZE 1024
#define FILENAMESIZE 256

int main(int argc, char **argv) {
	int server_socket	= 0,	// Create our server socket file descriptor
	filedes			= 0,	// Create File Descriptor
	socket_status		= 0,	// Monitor status of the socket that's being created for the initial connect
	bytes_rcvd		= 0,	// Used to hold the number of bytes read in a single read call
	total_bytes_rcvd	= 0;	// Total number of bytes received from the server
	
	
	char buffer[BUFFERSIZE];	// Buffer to be used for reading and writing data
	char *file_path;		// The file name to be sent to the server
	char *ip_address;		// The name of the ip address to be used to connect to the server
	ssize_t read_return;		// Size of the buffer to be used for reading and writing to the file

	unsigned short server_port = 12345;	// Port number to be used for connection
	struct sockaddr_in hostaddr, remaddr;	// Initialize client and server address data structure
	struct timeval timeout;			// Set the timeout for the socket so that the socket is not stuck on read
	timeout.tv_sec = 2;
	timeout.tv_usec = 0;

	socklen_t remaddrlen = sizeof(remaddr);		// Length of our remote address
	
	server_socket = socket(AF_INET, SOCK_STREAM, 0);
	setsockopt(server_socket, SOL_SOCKET, SO_RCVTIMEO, (char *) &timeout, sizeof(timeout));

	if ( server_socket < 0)
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

	socket_status = connect(server_socket, (struct sockaddr *) &remaddr, remaddrlen);

	if(socket_status < 0) {
		perror("Unable to establish connection");
            	exit(EXIT_FAILURE);
	}
	
	// Send file name to the server to be read and check for errors
	if ( ( write(server_socket, file_path, sizeof(file_path)) ) == -1) {
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
		bytes_rcvd = read(server_socket, buffer, BUFFERSIZE);
		
		if(bytes_rcvd <= 0)
			break;

		total_bytes_rcvd += bytes_rcvd;

		if (bytes_rcvd == -1) {
                	perror("read");
	                exit(EXIT_FAILURE);
		}

		if (write(filedes, buffer, bytes_rcvd) == -1) {
			perror("Failed to write to file");
			exit(EXIT_FAILURE);
		}
		
		memset(buffer, 0, BUFFERSIZE);
	}

	printf("The total number of bytes received: %d\n", total_bytes_rcvd);

	memset(buffer, 0, BUFFERSIZE);
	sprintf(buffer, "%d", total_bytes_rcvd);
	write(server_socket, buffer, BUFFERSIZE);	// Sending the amount of bytes received to the server

	close(filedes);
	close(server_socket);

	return 0;
}
