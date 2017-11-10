/*
 * Author: Brandon Lee Gaerlan
 * Program: Create a program that connects a SlaveBot to a MasterBot
 * The master must have the following specified commands as noted in the Programming Project requirements
 * Due Date: 03-06-2017
 * */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BankServer extends Thread {
	
	// Initialize a single ServerSocket
	private ServerSocket serverSocket;
	private Socket clients;
	
	// Slave sockets and their associated dates
	private List<Socket> clntSckts = new ArrayList<Socket>();
	private List<String> ipAdds = new ArrayList<>();
	private List<LocalDate> dates = new ArrayList<LocalDate>();
	
	//Constructor
	public BankServer(int hostPortNum) throws IOException {
		serverSocket = new ServerSocket(hostPortNum);		
	}
	
	/*
	 * Main Program
	 * */
	public static void main(String args[]) {
		
		try {
			int portNum;
			String cmdPrmpt = null;
			Scanner in = new Scanner(System.in);
			String[] cmdArgs;
			
			if(args.length != 2) {
				
				System.out.println(">Terminating.  Not enough arguments");
				System.exit(0);
				
			} else {
				
				portNum = Integer.parseInt(args[1]);
				
				System.out.println("Initiating Server");
				
				BankServer master = new BankServer(portNum);
				master.start();
				
				// Execute arguments in the CLI
				while(true) {
					
				} // End of while loop
				
				
			}
			
			in.close();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
		System.exit(0);
	} // End of Main
	
	// Getters and Setters
	public ServerSocket getSvrScket() {
		return serverSocket;
	}
	
	public List<Socket> getclntSckts() {
		return clntSckts;
	}

	public List<LocalDate> getDates() {
		return dates;
	}	

	/*
	 * Connect, Disconnect, and List methods
	 *
	 */
	
	public void connect(String cmdPrmpt) {
		try {
			String[] cmdArgs = cmdPrmpt.split(" ");
			
			ArrayList<Socket> sockets = (ArrayList<Socket>) getclntSckts();
			PrintWriter output;
			
			if(cmdArgs[1].equals("all")) {
				for(Socket socket : sockets) {
					output	= new PrintWriter(socket.getOutputStream(), true);
					output.println(cmdPrmpt);
					break;
				}
			} else if(checkIP(cmdArgs[1])) {
				String toCompare = "/" + cmdArgs[1];
				
				for(Socket socket : sockets) {
					if(socket.getInetAddress().toString().equals(toCompare)) {
						output	= new PrintWriter(socket.getOutputStream(), true);
						output.println(cmdPrmpt);
						break;
					}
				}
			} else {
				for(Socket socket : sockets) {
					if(socket.getInetAddress().getHostName().toString().equals(cmdArgs[1])) {
						output	= new PrintWriter(socket.getOutputStream(), true);
						output.println(cmdPrmpt);
						break;
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void list() {
		//Initialize iterator
		Iterator<LocalDate> dateIterator = dates.iterator();
		
		//Iterate through each slave and display the following information
		for(Socket slave : clntSckts)
			//Print out the list of slaves along with their IP address and their socket information
			System.out.println(
				slave.getInetAddress().getHostName() + "\t"
				+ slave.getRemoteSocketAddress() + "\t"
				+ slave.getPort() + "\t"
				+ dateIterator.next()
			);
	} // End of list method
	
	// Checks and validates IP address
	private static boolean checkIP(String ipAddr) {

		String[] num = ipAddr.split("\\.");

		if (num.length != 4)
			return false;

		for (String str : num) {
			int i = Integer.parseInt(str);
			if ((i < 0) || (i > 255))
				return false;
		}

		return true;
	} // End of checkIP method

	// Multithreaded server to accept clients (SlaveBots)
	public void run() {
		// Gets a reference to the ServerSocket initialized in the main object (MasterBot)
		ServerSocket serverSocket = getSvrScket();
		
		while(true) {
			try {
				clients = serverSocket.accept();
				clntSckts.add(clients);
				dates.add(LocalDate.now());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	} // End of run method
	
} // End of MasterBot class