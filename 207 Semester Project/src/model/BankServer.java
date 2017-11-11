package model;
/*
 * Author: Brandon Lee Gaerlan
 * Program: Create a program that connects a SlaveBot to a MasterBot
 * The master must have the following specified commands as noted in the Programming Project requirements
 * Due Date: 03-06-2017
 * */


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Person;

public class BankServer extends Thread {
	Database db = new Database();
	
	// Initialize a single ServerSocket
	private ServerSocket serverSocket;
	private Socket clients;
	
	// Connections
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
		DataOutputStream out;
		
		String username;
		String password;
		
		/*
		 * Person Initialized and hardcoded for debugging purposes
		 */
		Person tatsuya = new Person("tatsu01", "kenshin");
		tatsuya.setAge(21);
		tatsuya.setEmail("tatsuya@yahoo.com");
		tatsuya.setChkBalance(500.00);
		tatsuya.setSvgsBalance(1000.00);
		
		try {
			in			= new DataInputStream(client.getInputStream());
			username	= in.readUTF();
			password	= in.readUTF();
			
			out			= new DataOutputStream(client.getOutputStream());
			
			if(username.equals("one") && password.equals("two"))
				out.writeBoolean(true);
			else
				out.writeBoolean(false);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
} // End of BankServer class