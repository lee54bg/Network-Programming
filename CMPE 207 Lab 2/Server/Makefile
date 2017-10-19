default: clean compile

compile:
	gcc TCP_Server.c -o TCP_Server
	gcc TCP_Fork_Server.c -o TCP_Fork_Server
	gcc TCP_PreFork_Server.c -o TCP_PreFork_Server
	gcc TCP_Thread_Server.c -pthread -o TCP_Thread_Server
	gcc TCP_PreThread_Server.c -pthread -o TCP_PreThread_Server

clean: 
	rm -f TCP_Server TCP_Fork_Server TCP_PreFork_Server TCP_Thread_Server TCP_PreThread_Server
