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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;



import model.Person;

public class BankServer extends Thread {
	// Initialize a new DB
	Database db;
	
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
		db = new Database();
	}
	
	/*
	 * Main Program
	 * */
	public static void main(String args[]) {
		try {
			BankServer master = new BankServer(3000);
			master.start();
			
			while(true) {
			}			
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
	
	// Multithreaded server to accept clients
	public void run() {
		
		ServerSocket serverSocket = getSvrScket();
		
		// Runs and executes the methods
		while(true) {
			try {
				clients = serverSocket.accept();
				clntSckts.add(clients);
				dates.add(LocalDate.now());
				
				login(clients);

				// Debugging purposes
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
		
		// Connection to DB 
		Connection conn = null;
		PreparedStatement statement = null;
		
		try {
			in			= new DataInputStream(client.getInputStream());
			username	= in.readUTF();
			password	= in.readUTF();
					    
			// Connect
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost:3306/atb_bank";
			String dbusername = "testadmin";
			String dbpwd = "root";
			
			conn = DriverManager.getConnection(url, dbusername, dbpwd);
			
			// Calls the query
			statement = conn.prepareStatement("SELECT * FROM clients WHERE UserName = BINARY ? AND Password = BINARY ?");

		    statement.setString(1, username);
		    statement.setString(2, password);

		    ResultSet rs	= statement.executeQuery();
		    out				= new DataOutputStream(client.getOutputStream());
		    
		    if(rs.next()) {
		    	out.writeBoolean(true);
		    } else 
		    	out.writeBoolean(false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

		         if(statement != null)
		            statement.close();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			try {

		         if(conn != null)
		            conn.close();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	} // End of login(Socket client) method
	
} // End of BankServer class