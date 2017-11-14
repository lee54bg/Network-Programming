package view;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class PreferencesView extends JFrame {
	private JFrame frame;
	private JButton btnOk;
	private JButton btnCancel;
	
	private JTextField txtPortNumber;
	private JTextField txtIpAddress;
	
	private String ipAddress;
	private int portNum;
	
	private LoginFormView login;
	
	public PreferencesView() {
		frame = this;
		setTitle("Login");
		setSize(320, 220);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBounds(40, 36, 78, 16);
		getContentPane().add(lblIpAddress);
		
		btnOk = new JButton("Ok");
		btnOk.setBounds(40, 120, 97, 25);
		getContentPane().add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(147, 120, 97, 25);
		getContentPane().add(btnCancel);
		
		txtPortNumber = new JTextField();
		txtPortNumber.setBounds(130, 68, 116, 22);
		getContentPane().add(txtPortNumber);
		txtPortNumber.setColumns(10);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setBounds(130, 33, 116, 22);
		getContentPane().add(txtIpAddress);
		txtIpAddress.setColumns(10);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setBounds(40, 71, 78, 16);
		getContentPane().add(lblPortNumber);
		setVisible(true);
		
		addListeners();
	}
	
	private void addListeners() {
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login = new LoginFormView();
				dispose();
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ipAddress	= getIpAddress();
					portNum		= getPortNum();
					
					login = new LoginFormView(ipAddress, portNum);
					dispose();
				} catch(NumberFormatException num) {
					JOptionPane.showMessageDialog(frame, "Wrong fo");
				}				
			}
		});
	}
	
	// Checks and validates IP address
	private static boolean checkIP(String ipAddr) {
		String[] num = ipAddr.split("\\.");

		if (num.length != 4)
			return false;

		for (String str : num) {
			int i = Integer.parseInt(str);
			if ((i < 0) || (i > 255))
				return false;
		}

		return true;
	} // End of checkIP method
	
	// Gets the IP Address
	private String getIpAddress() {
		return txtIpAddress.getText();
	}
	
	// Gets the Port Number
	private int getPortNum() {
		return Integer.parseInt(txtPortNumber.getText());
	}	
}
