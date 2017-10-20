default: clean compile

compile:
	gcc TCP_Server.c -o TCP_Server
	gcc TCP_Fork_Server.c -o TCP_Fork_Server
	gcc TCP_PreFork_Server.c -o TCP_PreFork_Server
	gcc TCP_Thread_Server.c -pthread -o TCP_Thread_Server
	gcc TCP_PreThread_Server.c -pthread -o TCP_PreThread_Server
	gcc UDP_Server.c -o UDP_Server
	gcc UDP_Fork_Server.c -o UDP_Fork_Server
	gcc UDP_PreFork_Server.c -o UDP_PreFork_Server
	gcc UDP_Thread_Server.c -pthread -o UDP_Thread_Server
	gcc UDP_PreThread_Server.c -pthread -o UDP_PreThread_Server
clean: 
	rm -f TCP_Server TCP_Fork_Server TCP_PreFork_Server TCP_Thread_Server TCP_PreThread_Server UDP_Server UDP_Fork_Server UDP_Thread_Server UDP_PreThread_Server UDP_PreFork_Server
