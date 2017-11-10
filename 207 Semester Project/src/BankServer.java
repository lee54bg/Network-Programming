/*
 * Author: Brandon Lee Gaerlan
 * Program: Create a program that connects a SlaveBot to a MasterBot
 * The master must have the following specified commands as noted in the Programming Project requirements
 * Due Date: 03-06-2017
 * */


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
			BankServer master = new BankServer(3000);
			master.start();
			
			while(true) {
				
			} // End of while loop
			
			
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

	
	
	// Multithreaded server to accept clients
	public void run() {
		// Gets a reference to the ServerSocket initialized in the main object
		ServerSocket serverSocket = getSvrScket();
		
		while(true) {
			try {
				clients = serverSocket.accept();
				clntSckts.add(clients);
				dates.add(LocalDate.now());
				
				login(clients);
				// testing purposes
				System.out.println("A client has connected");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	} // End of run method

	private void login(Socket client) {
		DataInputStream in;
		String username;
		String password;
		
		try {
			in = new DataInputStream(client.getInputStream());
			username = in.readUTF();
			password = in.readUTF();
			System.out.println(username + " " + password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
} // End of BankServer class