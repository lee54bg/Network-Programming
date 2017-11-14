package view;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Database;
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
	Database db;
	
	private JFrame frmPIN;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnPINChange;
	private JButton btnCancel;
	private JLabel lblCurrentPIN;
	private JLabel lblNewPIN;
	
	// 1 = savings, 2 = checking
	int accountType = 0;
	
	// Declare variables
	int newPINValue = 0;
	int currentPINValue = 0;
	BigDecimal withdrawValue;
	BigDecimal depositValue;

	// Connection to DB 
	Connection conn = null;
	PreparedStatement statement = null;	
	
	public ClientApp(Socket client, String userName, int accountType) {
		
		this.client = client;
		this.userName = userName;
		this.accountType = accountType;
		
		if (accountType == 1) {
			frame = new JFrame("Savings Account");
		}
		else if (accountType == 2) {
			frame = new JFrame("Checking Account");
		} 
		
		initialize();
		connectDB();
	}

	// Initialize the GUI components
	private void initialize() {
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(460, 414);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.setBounds(76, 132, 126, 25);
		frame.getContentPane().add(btnWithdraw);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setBounds(216, 132, 126, 25);
		frame.getContentPane().add(btnDeposit);
		
		JButton btnBalance = new JButton("Check Balance");
		btnBalance.setBounds(76, 189, 126, 25);
		frame.getContentPane().add(btnBalance);
		
		JButton btnChangePIN = new JButton("Change PIN");
		btnChangePIN.setBounds(216, 189, 126, 25);
		frame.getContentPane().add(btnChangePIN);
		
		JLabel lblWelcomeBackFellow = new JLabel("What would you like to do today?");
		lblWelcomeBackFellow.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblWelcomeBackFellow.setBounds(76, 50, 336, 25);
		frame.getContentPane().add(lblWelcomeBackFellow);
		
		JButton btnExit = new JButton("Accounts");
		btnExit.setBounds(147, 276, 126, 25);
		frame.getContentPane().add(btnExit);
		
		btnWithdraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String withdraw = JOptionPane.showInputDialog(frame, "Your current balance is $ "
					+ checkBalance() + "\nHow much would you like to withdraw?", "Withdraw", JOptionPane.INFORMATION_MESSAGE);
				
				//Added to not crash on cancel
				if (withdraw != null) {
					if (withdraw.matches("^\\d+\\.\\d{2}$")) {
						withdrawValue = new BigDecimal(withdraw);
						BigDecimal x = withdraw();
						JOptionPane.showMessageDialog(frame, "Your new balance is $ " + x, "Balance", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(frame, "Please enter a value with 2 decimals!", "Error!", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		btnDeposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String deposit = JOptionPane.showInputDialog(frame, "Your current balance is $ "
					+ checkBalance() + "\nHow much would you like to deposit?", "Deposit!", JOptionPane.INFORMATION_MESSAGE);
				
				//Added to not crash on cancel
				if (deposit != null) {

					if (deposit.matches("^\\d+\\.\\d{2}$")) {
						depositValue = new BigDecimal(deposit);
						BigDecimal x = deposit();
						JOptionPane.showMessageDialog(frame, "Your new balance is $ " + x, "Balance", JOptionPane.INFORMATION_MESSAGE);

					}
					else {
						JOptionPane.showMessageDialog(frame, "Please enter a value with 2 decimals!", "Error!", JOptionPane.INFORMATION_MESSAGE);

					}
				}				
			}
		});
		
		btnBalance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				BigDecimal balance;				
				balance = checkBalance();
				JOptionPane.showMessageDialog(frame, "Your current balance is $ " + balance, "Check Balance", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		
		btnChangePIN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
								
				frmPIN = new JFrame();
				frmPIN.setTitle("Change PIN");
				frmPIN.setBounds(100, 100, 384, 283);
				frmPIN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmPIN.setVisible(true);
				frmPIN.getContentPane().setLayout(null);
				
				lblCurrentPIN = new JLabel("Current PIN:");
				lblCurrentPIN.setBounds(48, 72, 85, 16);
				frmPIN.getContentPane().add(lblCurrentPIN);
				
				lblNewPIN = new JLabel("New PIN:");
				lblNewPIN.setBounds(48, 124, 85, 16);
				frmPIN.getContentPane().add(lblNewPIN);
				
				textField = new JTextField();
				textField.setBounds(128, 69, 153, 22);
				frmPIN.getContentPane().add(textField);
				textField.setColumns(10);
				
				textField_1 = new JTextField();
				textField_1.setBounds(128, 121, 153, 22);
				frmPIN.getContentPane().add(textField_1);
				textField_1.setColumns(10);
				
				btnPINChange = new JButton("Change PIN");
				btnPINChange.setBounds(26, 174, 97, 25);
				frmPIN.getContentPane().add(btnPINChange);
				
				btnCancel = new JButton("Cancel");
				btnCancel.setBounds(135, 174, 97, 25);
				frmPIN.getContentPane().add(btnCancel);
				
							
				
				// PINChange button
				btnPINChange.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						String currentPIN = textField.getText();
						String newPIN = textField_1.getText();
						
						if (currentPIN.isEmpty() || newPIN.isEmpty()) {
							JOptionPane.showMessageDialog(frmPIN, "No value for current PIN or new PIN was entered."
									+ "\nPlease try again!", "Error!", JOptionPane.INFORMATION_MESSAGE);
						}						
						else if (currentPIN.matches("^\\d{4}$") && newPIN.matches("^\\d{4}$")) {
							currentPINValue = Integer.parseInt(textField.getText());
							newPINValue = Integer.parseInt(textField_1.getText());
				
							if (checkPIN() == 1){
								
								changePIN();
								JOptionPane.showMessageDialog(frame, "Your PIN has been successfully changed!", "PIN Changed", JOptionPane.INFORMATION_MESSAGE);
								frmPIN.dispose();	
							}
							else {
								JOptionPane.showMessageDialog(frame, "Your current PIN is incorrect. Please try again!", "Error!", JOptionPane.INFORMATION_MESSAGE);

							}
						}
						else {
							JOptionPane.showMessageDialog(frame, "Please enter PINs with 4 digits only!", "Error!", JOptionPane.INFORMATION_MESSAGE);

						}
					}
				});
				
				
				
				// Cancel button closes the current frame
				btnCancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						frmPIN.dispose();
					}
				});
				


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

				AcctView acctview = new AcctView(client, userName);
				frame.dispose();
			}
			
		});
	}
	
	//Create functions for the buttons
	
	//Check PIN
	private int checkPIN() {
		
		String username;
		int pin;
		
		username = userName;
		pin	= currentPINValue;

		String query = "SELECT * FROM clients WHERE PIN = ? AND UserName = BINARY ?";
		try {
			statement = conn.prepareStatement(query);
			statement.setInt(1, pin);
			statement.setString(2, username);

			ResultSet rs = statement.executeQuery();
			
			if(rs.next()) {
				return 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//Change PIN
	private void changePIN() {
		
		String username;
		int pin;
		
		username = userName;
		pin	= newPINValue;

		String query = "UPDATE clients SET PIN = ? WHERE UserName = BINARY ?";
		try {
			statement = conn.prepareStatement(query);
			statement.setInt(1, pin);
			statement.setString(2, username);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Check Balance
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
		
		if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
			JOptionPane.showMessageDialog(frame, "Sorry, you can not withdraw this amount as your balance will become negative. "
					+ "Please enter a correct amount!", "Error!", JOptionPane.INFORMATION_MESSAGE);
			
		return checkBalance();

		}
		
//		else if (newBalance.compareTo(BigDecimal.ZERO) >= 0)
		else {
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
				e.printStackTrace();
			}
		}
		
		return newBalance;
	}
	
	public void connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost:3306/atb_bank";
			String dbusername = "testadmin";
			String pwd = "root";
			
			conn = DriverManager.getConnection(url, dbusername, pwd);		    
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // End of connectDB() method
}
