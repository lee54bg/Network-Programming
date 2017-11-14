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

import controller.SysAdminController;
import model.Person;
import model.SysLoginModel;

public class SysAdmnLgnVw extends JFrame {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SysAdmnLgnVw sysAdmnLgnVw = new SysAdmnLgnVw();
					SysLoginModel sysLoginModel = new SysLoginModel();
					SysAdminController sysAdminController = new SysAdminController(sysAdmnLgnVw, sysLoginModel);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} // End of main program
	
	private JTextField textField;
	private JTextField txtPassWd;
	
	private JButton btnLogin;
	private JButton btnCancel;
	
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblWelcomeToBank;
	
	private JMenuBar menu;
	private JMenuItem preferences;
	
	private String ipAddress;
	private int portNum;
	
	// Constructors with parameters from Preferences
	public SysAdmnLgnVw() {
			createComponents();
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
		
		txtPassWd = new JTextField();
		txtPassWd.setBounds(128, 121, 153, 22);
		add(txtPassWd);
		txtPassWd.setColumns(10);
		
		lblWelcomeToBank = new JLabel("Welcome to ABT Bank");
		lblWelcomeToBank.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblWelcomeToBank.setBounds(48, 13, 226, 43);
		add(lblWelcomeToBank);
		
		menu = new JMenuBar();
		setJMenuBar(menu);
		
		preferences = new JMenuItem("Preferences");
		menu.add(preferences);
	} // End of createComponents()

	/**
	 * Adds the listeners
	 */
	public void addBtnLoginListener(ActionListener listener) {
		btnLogin.addActionListener(listener);
	}
	
	public void addCancelListener(ActionListener listener) {
		btnCancel.addActionListener(listener);
	}
	
	public void addPrefListener(MouseListener listener) {
		preferences.addMouseListener(listener);
	}
	
	/*
	 * Getters and Setters
	 */
	
	public String getUserName() {
		return textField.getText();
	}
	
	public String getPassWd() {
		return txtPassWd.getText();
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

} // End of LoginForm Class
