package view;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Person;

public class LoginForm {

	private JFrame frmLogin;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnLogin;
	private JButton btnCancel;
	private JButton btnSignUp;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblWelcomeToBank;
	
	private String userName;
	private String passWord;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginForm() {
		/*
		 * Starting from here, just ignore the GUI code.  You're just
		 * initializing the values here at this point
		 */
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 384, 283);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.setVisible(true);
		frmLogin.getContentPane().setLayout(null);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(12, 173, 97, 25);
		frmLogin.getContentPane().add(btnLogin);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(121, 173, 97, 25);
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
		
		lblWelcomeToBank = new JLabel("Welcome to ABT Bank");
		lblWelcomeToBank.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblWelcomeToBank.setBounds(101, 16, 180, 43);
		frmLogin.getContentPane().add(lblWelcomeToBank);
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(230, 173, 97, 25);
		frmLogin.getContentPane().add(btnSignUp);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SignUp signup = new SignUp();
				frmLogin.dispose();
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			Socket client = null;
			
			// Your streams
			DataOutputStream	out;
			DataInputStream		in;
			
			boolean confirmed;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userName = getUserName();
				passWord = getPassWd();
				
				try {
					// Your sending the username and password to the server for verification
					client	= new Socket(InetAddress.getByName("localhost"), 3000);
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
						
						JOptionPane.showMessageDialog(frmLogin, "Welcome back!");
						
						ClientApp clientApp = new ClientApp(client);
						
						/*
						 * This just means that I'm going to delete the current
						 * frame I'm in.  For transition purposes
						 */
						frmLogin.dispose();
					} else {
						JOptionPane.showMessageDialog(frmLogin, "Invalid credentials.  Please try again");
						client.close();
					}	
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});	// End of btnLogin
		
		
	} // End of initialize method
	
	public String getUserName() {
		return textField.getText();
	}
	
	public String getPassWd() {
		return textField_1.getText();
	}
} // End of LoginForm Class
