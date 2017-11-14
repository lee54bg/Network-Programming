package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class Database {
	private ArrayList<Person> people;
	private Connection connection;
	
	public Database() {
		people = new ArrayList<Person>();
	}
	
	public void addPers(Person person) {
		// create a sql date object so we can use it in our INSERT statement
		Calendar calendar = Calendar.getInstance();
		Date startDate = new Date(calendar.getTime().getTime());

		//the mysql insert statement
	    String query = "INSERT INTO clients (FirstName, LastName, Email,"		// 3 fields
	    		+ " SocialSecurity, PhoneNumber, PIN, Address, City, State,"	// 6
	    		+ " UserName, Password, SvgAct, SvgActBal, ChkAct, ChkActBal)"	// 6
	    		+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement preparedStmt;
		
		try {
			preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString	(1, person.getFirstName() );
			preparedStmt.setString	(2, person.getLastName() );
			preparedStmt.setString	(3, person.getEmail() );
			preparedStmt.setInt		(4, person.getSocSec() );
			preparedStmt.setString	(5, person.getPhoneNum() );
			preparedStmt.setInt		(6, person.getPinNum());
			preparedStmt.setString	(7, person.getAddress());
			preparedStmt.setString	(8, person.getCity());
			preparedStmt.setString	(9, person.getState());
			preparedStmt.setString	(10, person.getUserName());
			preparedStmt.setString	(11, person.getPassWd());
			preparedStmt.setInt		(12, person.getSvgAct());
			preparedStmt.setInt		(13, 0);
			preparedStmt.setInt		(14, person.getChkAct());
			preparedStmt.setInt		(15, 0);
			
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Person> getDB() {
		return people;
	}
	
	public boolean searchForUser(String testname) {
		ResultSet rs = null;
		boolean result = false;
		
		String query = "SELECT * FROM clients WHERE UserName = ?";
		
		PreparedStatement statement;
		
		//Connect
		try {			
			statement = connection.prepareStatement(query);

		    statement.setString(1, testname);

		    rs = statement.executeQuery();
		    
		    if(rs.next()) {
				// username exists
				result = true;
			} else {
				// otherwise username does not exist
				result =  false;
			}
    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void connect() {
		if (connection != null)
			return;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost:3306/atb_bank";
			String username = "testadmin";
			String pwd = "root";
			
			connection = DriverManager.getConnection(url, username, pwd);
			 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	// Disconnect the SQL table
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Can't close connection");
			}
		}
	}
}