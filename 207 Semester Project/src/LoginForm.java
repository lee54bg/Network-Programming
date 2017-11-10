import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoginForm {

	private JFrame frmLogin;
	private JTextField textField;
	private JTextField textField_1;
	private String userName;
	private String passWd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.frmLogin.setVisible(true);
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 384, 283);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(75, 173, 97, 25);
		frmLogin.getContentPane().add(btnLogin);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(184, 173, 97, 25);
		frmLogin.getContentPane().add(btnCancel);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(48, 72, 85, 16);
		frmLogin.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
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
		
		JLabel lblWelcomeToBank = new JLabel("Welcome to ABT Bank");
		lblWelcomeToBank.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblWelcomeToBank.setBounds(101, 16, 180, 43);
		frmLogin.getContentPane().add(lblWelcomeToBank);
		
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String password = textField_1.getText();
				
				if(username.equals("") && password.equals("")) {
					Socket client = new Socket(InetAddress.getByAddress(addr), 3000);
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid credentials.  Please try again");
				}
			}
		});
	}
	
	
}
