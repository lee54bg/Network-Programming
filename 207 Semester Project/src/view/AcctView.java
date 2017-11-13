package view;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
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

public class AcctView {
	private JFrame frmActType;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnChk;
	private JButton btnSav;
	private JButton btnCancel;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblWelcomeToBank;
	
	Socket client;
	private String userName;
	private String passWord;
	
	/**
	 * Create the application.
	 */
	public AcctView(Socket client, String userName) {
		frmActType = new JFrame("Client");
		this.userName = userName;
		this.client = client;	
		
		/*
		 * Starting from here, just ignore the GUI code.  You're just
		 * initializing the values here at this point
		 */
		frmActType.setTitle("Account Type");
		frmActType.setBounds(100, 100, 384, 283);
		frmActType.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmActType.setVisible(true);
		frmActType.getContentPane().setLayout(null);
		
		btnChk = new JButton("Checking");
		btnChk.setBounds(26, 174, 97, 25);
		frmActType.getContentPane().add(btnChk);
		
		btnSav = new JButton("Savings");
		btnSav.setBounds(135, 174, 97, 25);
		frmActType.getContentPane().add(btnSav);
		
//		lblUsername = new JLabel("Username:");
//		lblUsername.setBounds(48, 72, 85, 16);
//		frmActType.getContentPane().add(lblUsername);
//		
//		lblPassword = new JLabel("Password:");
//		lblPassword.setBounds(48, 124, 85, 16);
//		frmActType.getContentPane().add(lblPassword);
//		
//		textField = new JTextField();
//		textField.setBounds(128, 69, 153, 22);
//		frmActType.getContentPane().add(textField);
//		textField.setColumns(10);
//		
//		textField_1 = new JTextField();
//		textField_1.setBounds(128, 121, 153, 22);
//		frmActType.getContentPane().add(textField_1);
//		textField_1.setColumns(10);
		
		lblWelcomeToBank = new JLabel("Select an account");
		lblWelcomeToBank.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblWelcomeToBank.setBounds(48, 13, 226, 43);
		frmActType.getContentPane().add(lblWelcomeToBank);
		
		btnCancel = new JButton("Sign Out");
		btnCancel.setBounds(244, 174, 97, 25);
		frmActType.getContentPane().add(btnCancel);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		btnChk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmActType, "Checking Account!");
				
				ClientApp clientApp = new ClientApp(client, userName, 2);
				frmActType.dispose();
			}
		
		});	// End of btnChk
		
		btnSav.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmActType, "Savings Account!");
				
				ClientApp clientApp = new ClientApp(client, userName, 1);
				frmActType.dispose();
			}
		
		});	// End of btnChk
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginForm window = new LoginForm();
				frmActType.dispose();
			}
		}); // End of cancel button		
	} // End of initialize method
	
	public String getUserName() {
		return textField.getText();
	}
	
	public String getPassWd() {
		return textField_1.getText();
	}
} // End of LoginForm Class
