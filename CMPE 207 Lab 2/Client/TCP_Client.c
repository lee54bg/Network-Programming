// Server side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <unistd.h>
#include <netdb.h>
#include <string.h>
#include <fcntl.h>
#define BUFSIZE 1024

int main(int argc, char **argv) {
	int network_socket	= 0,	// Create socket
	filedes			= 0,
	socketStatus		= 0;	// Monitor status of the socket that's being created
	
	char buffer[BUFSIZE];
	char *file_path = "output.tmp";
	ssize_t read_return;
	struct sockaddr_in hostaddr, remaddr;
	
	unsigned short server_port = 12345;

	if( argc > 1) {
		file_path = argv[1];

		if (argc > 2) {
			server_port = strtol(argv[2], NULL, 10);
		}
	}

	socklen_t locaddrlen = sizeof(hostaddr);	// Length of our locaddrlen
	socklen_t remaddrlen = sizeof(remaddr);		// Length of our remaddrlen
	
	network_socket = socket(AF_INET, SOCK_STREAM, 0);

	if ( network_socket < 0)
		printf("Socket doesn't work");

	memset((char *) &remaddr, 0, sizeof(remaddr));
	remaddr.sin_family = AF_INET;
	remaddr.sin_port = htons(server_port);
	remaddr.sin_addr.s_addr = INADDR_ANY;
	
	socketStatus = connect(network_socket, (struct sockaddr *) &remaddr, remaddrlen);
	
	memset((char *) &buffer, 0, sizeof(buffer));
	
	while(1) {
		filedes = open(file_path,
                	O_WRONLY | O_CREAT | O_TRUNC,
	                S_IRUSR | S_IWUSR);

		if (filedes == -1) {
			perror("open");
	            	exit(EXIT_FAILURE);
		}

	        do {
			read_return = read(network_socket, buffer, BUFSIZE);

			if (read_return == -1) {
	                	perror("read");
		                exit(EXIT_FAILURE);
			}

        		if (write(filedes, buffer, read_return) == -1) {
				perror("write");
				exit(EXIT_FAILURE);
			}
		} while (read_return > 0);
		
		printf("Buffer content: %s\n", buffer);

		close(filedes);
		close(network_socket);
		break;
	}
	close(filedes);
	close(network_socket);

	return 0;
}
