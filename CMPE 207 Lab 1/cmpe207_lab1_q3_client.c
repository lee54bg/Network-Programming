/*
 * Author: Brandon Lee Gaerlan, Timothy Fong, Asvin Peiris
 * Class: CMPE 207 Network Programming and Applications
 * Program: Create a TCP Client that takes two time servers in and
 * finds the difference of the times between them
 * Due Date: 10-03-2017
 * */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <time.h>

int main(int argc, char *argv[]) {
	// variables
	// sockets
	int network_socket1;
	int network_socket2;
	// specify an address for the socket
	struct sockaddr_in server_address1;
	struct sockaddr_in server_address2;
	// pointer to host information entry
	struct hostent *host1;
	struct hostent *host2;
	int connection_status1;
	int connection_status2;
	// receive data from servers
	unsigned long server1;
	unsigned long server2;
	// structs to convert Unix TimeStamp into day:month:date:year
	time_t t1, t2;
	struct tm ts1;
	struct tm ts2;
	char buff[1024];
	// difference between servers
	unsigned long difference;
	
	// reset address
	// in case leftover data from another process
	memset(&network_socket1, 0, sizeof(network_socket1));
	memset(&network_socket2, 0, sizeof(network_socket2));
	
	// Create TCP sockets
	// Note change SOCK_STREAM to SOCK_DGRAM for UDP
	network_socket1 = socket(AF_INET, SOCK_STREAM, 0);
	if (network_socket1 < 0) {
		printf("socket failed");
		exit(1);
	}
	network_socket2 = socket(AF_INET, SOCK_STREAM, 0);
	if (network_socket2 < 0) {
		printf("socket failed");
		exit(1);
	}
	
	// init server_address1
	memset(&server_address1, 0, sizeof(server_address1));
	// init server_address2
	memset(&server_address2, 0, sizeof(server_address2));
	
	// address family: AF_INET
	server_address1.sin_family = AF_INET;
	server_address2.sin_family = AF_INET;

	// Map host1 and host2 name to IP address
	host1 = gethostbyname(argv[1]);
	if(host1 == NULL){
		printf("Error gethostbyname\n");
	}
	host2 = gethostbyname(argv[2]);
	if(host2 == NULL){
		printf("Error gethostbyname\n");
	}
	
	// h_addr = h_addr_list[0]
	printf("server1: %s\n", argv[1]);
	printf("server2: %s\n", argv[2]);
	// printf("HOST: %x\n", host1->h_addr);
	if (host1 == 0) {
		printf("gethostname failed");
		exit(1);
	}
	if (host2 == 0) {
		printf("gethostname failed");
		exit(1);
	}
	
	// fill in socket address struct
	memcpy(&server_address1.sin_addr, host1->h_addr, host1->h_length);
	memcpy(&server_address2.sin_addr, host2->h_addr, host2->h_length);

	// Map service name to port number
	// port in network byte order
	// port we want to connect to; we choose 37 for TIME
	server_address1.sin_port = htons(37);
	server_address2.sin_port = htons(37);
	
	// connection_status = 0 means no problem
	// connection_status = -1 mean error
	connection_status1 = connect(network_socket1, (struct sockaddr *) &server_address1, sizeof(server_address1));
	// check for error with connection_status
	if(connection_status1 < 0) {
		printf("Error with connection \n");
		exit(1);
	}
	connection_status2 = connect(network_socket2, (struct sockaddr *) &server_address2, sizeof(server_address2));
	// check for error with connection_status
	if(connection_status2 < 0) {
		printf("Error with connection \n");
		exit(1);
	}
	
	// receive response from server1
	recv(network_socket1, &server1, sizeof(server1), 0);
	server1 = htonl(server1);
	server1 = server1 -  2208988800;
	t1 = server1;
	ts1 = *localtime(&t1);
	strftime(buff, sizeof(buff), "%a %Y-%m-%d %H:%M:%S %Z", &ts1);
	// print out server1 response
	printf("The server1 responded with: %s \n", buff);
	// receive response from server2
	recv(network_socket2, &server2, sizeof(server2), 0);
	server2 = htonl(server2);
	server2 = server2 - 2208988800;
	t2 = server2;
	ts2 = *localtime(&t2);
	strftime(buff, sizeof(buff), "%a %Y-%m-%d %H:%M:%S %Z", &ts2);
	// print out server2 response
	printf("The server2 responded with: %s \n", buff);
	
	// print difference in time
	difference = abs(server1 - server2);
	printf("Difference between the servers: %lu seconds \n", difference);
	
	// close sockets
	close(network_socket1);
	close(network_socket2);
	
	return 0;
}
