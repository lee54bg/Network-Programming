package view;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Person;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;


public class ClientApp {
	JFrame frame;
	Socket client;
	Person person;
	String userName;
	
	// 1= savings, 2=checking
	int accountType = 0;
	
	
	//Declare variables
		int changePINValue = 0;
		BigDecimal withdrawValue;
		BigDecimal depositValue;
	
		//Connection to DB 
		Connection conn = null;
		PreparedStatement statement = null;	
		

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * Create the application.
	 * @param person 
	 */
	
	public ClientApp(Socket client, String userName, int x) {
		frame		= new JFrame("Client");
		this.client = client;
		this.userName = userName;
		accountType = x;
		
		initialize();
		connectDB();

	}
	
//	public ClientApp(Socket client, Person person) {
//		frame		= new JFrame("Client");
//		this.client = client;
//		this.person = person;
//		
//		initialize();
//		
//	}
//	

	public ClientApp(Socket client) {
		frame		= new JFrame("Client");
		this.client	= client;
		
		initialize();
	}

	/**
	 * Initialize the contents of the 
	 */
	private void initialize() {
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(460, 414);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.setBounds(76, 132, 97, 25);
		frame.getContentPane().add(btnWithdraw);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setBounds(216, 132, 126, 25);
		frame.getContentPane().add(btnDeposit);
		
		JButton btnBalance = new JButton("Balance");
		btnBalance.setBounds(76, 189, 97, 25);
		frame.getContentPane().add(btnBalance);
		
		JButton btnChangePIN = new JButton("Change PIN");
		btnChangePIN.setBounds(216, 189, 126, 25);
		frame.getContentPane().add(btnChangePIN);
		
		JLabel lblWelcomeBackFellow = new JLabel("Welcome back " + userName);
		
		lblWelcomeBackFellow.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblWelcomeBackFellow.setBounds(76, 50, 336, 16);
		frame.getContentPane().add(lblWelcomeBackFellow);
		
		JButton btnExit = new JButton("Accounts");
		btnExit.setBounds(147, 276, 97, 25);
		frame.getContentPane().add(btnExit);
		
		btnWithdraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String withdraw = JOptionPane.showInputDialog(frame, "Your current balance is $ "
						+ checkBalance() + "\nHow much would you like to withdraw?");
				
				//Added to not crash on cancel
				if (withdraw != null) {

				if (withdraw.matches("^\\d+\\.\\d{2}$")) {
					withdrawValue = new BigDecimal(withdraw);
					BigDecimal x = withdraw();
					JOptionPane.showMessageDialog(frame, "Your new balance is $ " + x);

				}
				else {
					JOptionPane.showMessageDialog(frame, "Please enter a value with 2 decimals!");

				}
				}
			}
		});
		
		btnDeposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String deposit = JOptionPane.showInputDialog(frame, "Your current balance is $ "
						+ checkBalance() + "\nHow much would you like to deposit?");
				
				//Added to not crash on cancel
				if (deposit != null) {

				if (deposit.matches("^\\d+\\.\\d{2}$")) {
					depositValue = new BigDecimal(deposit);
					BigDecimal x = deposit();
					JOptionPane.showMessageDialog(frame, "Your new balance is $ " + x);

				}
				else {
					JOptionPane.showMessageDialog(frame, "Please enter a value with 2 decimals!");

				}
				}				
			}
		});
		
		btnBalance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				BigDecimal balance;				
				balance = checkBalance();
				JOptionPane.showMessageDialog(frame, "Your current balance is $ " + balance);

			}
		});
		
		btnChangePIN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String changePIN = JOptionPane.showInputDialog(frame, "Please enter your new PIN?");
				
				if (changePIN != null) {
					changePINValue = Integer.parseInt(changePIN);
					changePIN();
					//clear changePIN value?
				}
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				
//				LoginForm window = new LoginForm();
				AcctView acctview = new AcctView(client, userName);
				frame.dispose();


				
				
//				try {
////					client.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
		
		});
	}
	
	//Create functions for the buttons
	
	//Change PIN
	private void changePIN() {
		
		String username;
		int pin;
		
		username = userName;
		pin	= changePINValue;

		String query = "UPDATE clients SET PIN = ? WHERE UserName = ?";
		try {
			statement = conn.prepareStatement(query);
		    statement.setInt(1, pin);
		    statement.setString(2, username);

		    statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Check Balance
	private BigDecimal checkBalance() {
		
		String username;
		BigDecimal balance = null;
		
		username = userName;

		if (accountType == 1) {
			String query = "SELECT SvgActBal FROM clients WHERE UserName=?";
			
			try {
				statement = conn.prepareStatement(query);
			    statement.setString(1, username);

			    ResultSet rs = statement.executeQuery();
			    rs.next();
			    balance = rs.getBigDecimal(1);
			    
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (accountType == 2) {
			String query = "SELECT ChkActBal FROM clients WHERE UserName=?";
			
			try {
				statement = conn.prepareStatement(query);
			    statement.setString(1, username);

			    ResultSet rs = statement.executeQuery();
			    rs.next();
			    balance = rs.getBigDecimal(1);
			    
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		 return balance;

		
	}
	
	private BigDecimal withdraw() {
		String username;
		BigDecimal withdrawvalue;
		BigDecimal currentBalance;
		BigDecimal newBalance;
		
		username = userName;
		withdrawvalue = withdrawValue;
		
		currentBalance = checkBalance();
		newBalance = currentBalance.subtract(withdrawvalue);	
		
		if (accountType == 1) {
			String query = "UPDATE clients SET SvgActBal = ? WHERE UserName = ?";
			try {
				statement = conn.prepareStatement(query);
			    statement.setBigDecimal(1, newBalance);
			    statement.setString(2, username);

			    statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if (accountType == 2) {
			String query = "UPDATE clients SET ChkActBal = ? WHERE UserName = ?";
			try {
				statement = conn.prepareStatement(query);
			    statement.setBigDecimal(1, newBalance);
			    statement.setString(2, username);

			    statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
		return newBalance;
		
	}
	
	private BigDecimal deposit() {
		String username;
		BigDecimal depositvalue;
		BigDecimal currentBalance;
		BigDecimal newBalance;
		
		username = userName;
		depositvalue = depositValue;
		
		currentBalance = checkBalance();
		
		newBalance = currentBalance.add(depositvalue);
		
		if (accountType == 1) {
			String query = "UPDATE clients SET SvgActBal = ? WHERE UserName = ?";
			try {
				statement = conn.prepareStatement(query);
			    statement.setBigDecimal(1, newBalance);
			    statement.setString(2, username);

			    statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if (accountType == 2) {
			String query = "UPDATE clients SET ChkActBal = ? WHERE UserName = ?";
			try {
				statement = conn.prepareStatement(query);
			    statement.setBigDecimal(1, newBalance);
			    statement.setString(2, username);

			    statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		
		return newBalance;
		
	}
	
	
	
	public void connectDB() {
try {
			
			//Connect
			Class.forName("com.mysql.jdbc.Driver");
						
			String url = "jdbc:mysql://localhost:3306/atb_bank";
			String dbusername = "testadmin";
			String pwd = "root";
			
			conn = DriverManager.getConnection(url, dbusername, pwd);		    
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
