#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netinet/tcp.h>


int main(int argc, char **argv) {
	int newSocket, socketStatus;
	char bufferLine[1024];
	struct sockaddr_in servaddr;

	newSocket = socket(AF_INET, SOCK_STREAM, 0);

	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(13);

	if (inet_pton(AF_INET, argv[1], &servaddr.sin_addr) <= 0)
		//err_quit("pton didn't work");

	if( connect(newSocket, (struct sockaddr *) &servaddr, sizeof(servaddr) ) < 0)
		printf("print");//err_quit("inet_pton error");

	while ( socketStatus = read(newSocket, bufferLine, sizeof(bufferLine)) > 0) {
		bufferLine[socketStatus] = 0;
		if ( fputs(bufferLine, stdout) == EOF)
			printf("fputs error");
	}
	if (newSocket < 0)
		printf("Socket not working");//err_sys("read error")
		
	
	exit(0);
}
