package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Person;

public class ClientLoginView {
	private JFrame frmLogin;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnLogin;
	private JButton btnCancel;
	private JButton btnSignUp;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblWelcomeToBank;
	private JMenuBar menu;
	private JMenuItem preferences;
	
	private String userName;
	private String passWord;
	
	private String ipAddress;
	private int portNum;
	
	private Socket client = null;
	private DataOutputStream out;
	private DataInputStream in;
	
	// Different parameters
	public ClientLoginView(String ipAddress, int portNum) {
		this.ipAddress = ipAddress;
		this.portNum = portNum;
		
		createComponents();
		addListener();
	}
	
	/*
	 * Default Constructor
	 */
	public ClientLoginView() {
		this.ipAddress = "127.0.0.1";
		this.portNum = 3000;
		
		createComponents();
		addListener();
	}
	
	
	/*
	 * Initializing the components in the JFrame
	 */
	private void createComponents() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 384, 283);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.setVisible(true);
		frmLogin.getContentPane().setLayout(null);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(26, 174, 97, 25);
		frmLogin.getContentPane().add(btnLogin);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(135, 174, 97, 25);
		frmLogin.getContentPane().add(btnCancel);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(48, 72, 85, 16);
		frmLogin.getContentPane().add(lblUsername);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(48, 124, 85, 16);
		frmLogin.getContentPane().add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(128, 69, 153, 22);
		frmLogin.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(128, 121, 153, 22);
		frmLogin.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		lblWelcomeToBank = new JLabel("Welcome to ABT Bank!");
		lblWelcomeToBank.setFont(new Font("SimSun", Font.PLAIN, 21));
		lblWelcomeToBank.setBounds(48, 13, 226, 43);
		frmLogin.getContentPane().add(lblWelcomeToBank);
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(244, 174, 97, 25);
		frmLogin.getContentPane().add(btnSignUp);
		
		menu = new JMenuBar();
		frmLogin.setJMenuBar(menu);
		
		preferences = new JMenuItem("Preferences");
		menu.add(preferences);
	} // End of createComponents()

	/**
	 * Initialize the contents of the frame.
	 */
	private void addListener() {
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SignUp signup = new SignUp();
				frmLogin.dispose();
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			boolean confirmed;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userName = getUserName();
				passWord = getPassWd();
				
				//Check if login fields are empty instead of always trying to connect to DB
				if (userName.isEmpty() || passWord.isEmpty()) {
					JOptionPane.showMessageDialog(frmLogin, "No username or password was entered."
							+ " Please try again!", "Error!", JOptionPane.INFORMATION_MESSAGE);
				}
				
				else {
					try {
						// Your sending the username and password to the server for verification
						client	= new Socket(ipAddress, portNum);
						out		= new DataOutputStream(client.getOutputStream());
						out.writeUTF(userName);
						out.writeUTF(passWord);
						out.flush();
										
						// Your receiving that verification back
						in	= new DataInputStream(client.getInputStream());
											
						confirmed = in.readBoolean();
						
						if(confirmed == true) {
							out.close();
							in.close();
							
							JOptionPane.showMessageDialog(frmLogin, "Welcome back " + userName + "!", "Welcome to ATB Bank!", JOptionPane.INFORMATION_MESSAGE);
							
							AcctView acctview = new AcctView(client, userName);
							
							// Close the current frame
							frmLogin.dispose();
						} else {
							JOptionPane.showMessageDialog(frmLogin, "Invalid credentials.  Please try again", "Error!", JOptionPane.INFORMATION_MESSAGE);
							client.close();
						}	
					} catch (ConnectException con) {
						con.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} 
				}
				

			}
		});	// End of btnLogin
		
		// Cancel button closes the current frame
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frmLogin.dispose();
			}
		});
		
		// Menu item preferences
		preferences.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				PreferencesView preference = new PreferencesView();
				frmLogin.dispose();
			}
			// Empty events
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
	} // End of initialize method
	
	public String getUserName() {
		return textField.getText();
	}
	
	public String getPassWd() {
		return textField_1.getText();
	}
} // End of LoginForm Class
