package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

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
	    String query = "INSERT INTO clients (FirstName, LastName, Email,"
	    		+ " SocialSecurity, PhoneNumber, PIN, Address, City, State)"
	    		+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
			
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Person> getDB() {
		return people;
	}
	
	public void searchForUser() {
		
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
	
	/*public void load() throws SQLException {
		people.clear();
	}*/
	
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
