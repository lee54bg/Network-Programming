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

public class SysAdmnLgnVw extends JFrame {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SysAdmnLgnVw sysAdmnLgnVw = new SysAdmnLgnVw();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} // End of main program
	
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
	public SysAdmnLgnVw(String ipAddress, int portNum) {
		this.ipAddress = ipAddress;
		this.portNum = portNum;
		
		createComponents();
		addListener();
	}
	
	/*
	 * Default Constructor
	 */
	public SysAdmnLgnVw() {
		this.ipAddress = "127.0.0.1";
		this.portNum = 3000;
		
		createComponents();
		addListener();
	}
	
	
	/*
	 * Initializing the components in the JFrame
	 */
	private void createComponents() {
		setTitle("Login");
		setBounds(100, 100, 384, 283);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(26, 174, 97, 25);
		add(btnLogin);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(135, 174, 97, 25);
		add(btnCancel);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(48, 72, 85, 16);
		add(lblUsername);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(48, 124, 85, 16);
		add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(128, 69, 153, 22);
		add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(128, 121, 153, 22);
		add(textField_1);
		textField_1.setColumns(10);
		
		lblWelcomeToBank = new JLabel("Welcome to ABT Bank");
		lblWelcomeToBank.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblWelcomeToBank.setBounds(48, 13, 226, 43);
		add(lblWelcomeToBank);
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(244, 174, 97, 25);
		add(btnSignUp);
		
		menu = new JMenuBar();
		setJMenuBar(menu);
		
		preferences = new JMenuItem("Preferences");
		menu.add(preferences);
	} // End of createComponents()

	/**
	 * Initialize the contents of the frame.
	 */
	private void addListener() {
//		btnSignUp.addActionListener();
//		btnLogin.addActionListener();
//		btnCancel.addActionListener();

		// Menu item preferences
		preferences.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				PreferencesView preference = new PreferencesView();
				dispose();
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
