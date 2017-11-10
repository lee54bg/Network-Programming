package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
	private ArrayList<Person> people;
	private Connection connection;
	
	public Database() {
		people = new ArrayList<Person>();
	}
	
	public void addPers(Person person) {
		people.add(person);
	}
	
	public ArrayList<Person> getDB() {
		return people;
	}
	
	public void searchForUser() {
		
	}
	
	public void connect() throws Exception {

		if (connection != null)
			return;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3306/atb_bank";
		String username = "testadmin";
		String pwd = "root";
		
		connection = DriverManager.getConnection(url, username, pwd);
	}
	
	public void load() throws SQLException {
		people.clear();
		
		/*String 
		
		Statement selectStatement = connection.createStatement();
		selectStatement.executeQuery("");*/
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
