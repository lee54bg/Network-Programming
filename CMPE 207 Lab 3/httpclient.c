/*
 * Author: Brandon Lee Gaerlan, Timothy Fong, Asvin Peiris
 * Class: CMPE 207 Network Programming and Applications
 * Program: Create a TCP Client and UDP Client along with multiple types of TCP and UDP Servers to
 * simulate file transfer.  Lab specifications available in the sheet the professor provided us.
 * Due Date: 10-19-2017
 * */

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
#define BUFFERSIZE 576
#define FILENAMESIZE 256

int create_socket();

int main(int argc, char **argv) {
	int server_socket	= 0,	// Create our server socket file descriptor
	file_desc		= 0,	// Create File Descriptor
	socket_status		= 0,	// Monitor status of the socket that's being created for the initial connect
	bytes_rcvd		= 0,	// Used to hold the number of bytes read in a single read call
	total_bytes_rcvd	= 0;	// Total number of bytes received from the server
	
	char buffer[BUFFERSIZE];	// Buffer to be used for reading and writing data
	char file_path[] = "http_file";	// The file name to be sent to the server
	char *file_name;
	char *httpreq;

	// Port number to be used for connection
	int server_port;

	// Initialize the remote address struct
	struct sockaddr_in remaddr;

	// Website information that will be passed through here
	struct hostent *host_site;
	
	// Create socket
	server_socket = create_socket();

	// Clearing out the buffers of the remote address and our buffer
	memset(&remaddr, 0, sizeof(remaddr));
	memset(&buffer, 0, sizeof(buffer));

	// Set the address family to be IPv4
	remaddr.sin_family = AF_INET;
	
	switch(argc) {
		/*
		case 3:
			// IP Address in ASCII form
			host_site = gethostbyname(argv[1]);
	
			// Port number to be used for connecting to the server
			server_port = atoi(argv[2]);

			// Port Number of our server
			remaddr.sin_port = htons(server_port);
			bcopy((char *) host_site->h_addr, (char *) &remaddr.sin_addr.s_addr, host_site->h_length);

			httpreq = malloc(strlen("GET /\r\n") + 1);
			httpreq = "GET /\r\n";
			puts(httpreq);
			break;
		*/
		case 4:
			host_site	= gethostbyname(argv[1]);	// IP Address in ASCII form
			server_port	= atoi(argv[2]);		// Port number to be used for connecting to the server
			file_name	= (char*) malloc(strlen(argv[3]));
					
			remaddr.sin_port = htons(server_port);		// Port Number of our server
			bcopy((char *) host_site->h_addr, (char *) &remaddr.sin_addr.s_addr, host_site->h_length);

			httpreq = malloc(strlen("GET ") + strlen(argv[3]) + strlen(" HTTP/1.1\r\n") + strlen("Host: ") + strlen(argv[1]) + strlen("\r\n\n") + 1);
			sprintf(httpreq, "GET %s HTTP/1.1\r\nHost: %s\r\n\r\n", argv[3], argv[1]);
			puts(httpreq);
			break;
		default:
			printf("Insufficient and/or invalid arguments.  Terminating program...\n");
			exit(EXIT_FAILURE);
	}
	
	// Connect to the server
	socket_status = connect(server_socket, (struct sockaddr *) &remaddr, sizeof(remaddr));
	
	// More error checking for the socket descriptor
	if(socket_status == -1) {
		perror("Unable to establish connection");
            	exit(EXIT_FAILURE);
	}
	
	// Open the file and create the file if it doesn't exist
	file_desc = open(file_path,
		O_WRONLY | O_CREAT | O_TRUNC,
		S_IRUSR | S_IWUSR);
	
	// if theres an error, then terminate the program, otherwise continue with normal read operation
	if (file_desc == -1) {
		free(file_name);
		free(httpreq);
		perror("Error in creating file");
            	exit(EXIT_FAILURE);
	} else {
		// Send file name to the server to be read and check for errors
		if ( ( send(server_socket, httpreq, strlen(httpreq), 0) ) == -1) {
			perror("Failed to write to file");
			exit(EXIT_FAILURE);
		}

		while(1) {
			bytes_rcvd = recv(server_socket, buffer, BUFFERSIZE, 0);
					
			if (bytes_rcvd == -1) {
				break;
			}

			total_bytes_rcvd += bytes_rcvd;

			if (write(file_desc, buffer, bytes_rcvd) < 0) {
				perror("Failed to write to file");
				exit(EXIT_FAILURE);
			}
		
			memset(buffer, 0, BUFFERSIZE);
		}
		printf("Total bytes received: %d\n", total_bytes_rcvd);

		free(file_name);
		free(httpreq);

		// Close your file descriptors
		close(file_desc);
		close(server_socket);
	}

	return 0;
}

int create_socket() {
	int server_socket = 0;

	/* Set the timeout for the socket
	 * so that the socket is not stuck blocking on read
	 */
	struct timeval timeout;
	timeout.tv_sec = 3;
	timeout.tv_usec = 0;

	// Create our socket and return a socket descriptor and assign to server_socket
	server_socket = socket(AF_INET, SOCK_STREAM, 0);

	// Set timeout for our socket
	setsockopt(server_socket, SOL_SOCKET, SO_RCVTIMEO, (char *) &timeout, sizeof(timeout));
	
	// Error checking for socket creation
	if (server_socket == -1) {
		perror("Error in socket creation\n");
		exit(EXIT_FAILURE);
	}	

	return server_socket;
}
