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
import java.io.IOException;
import java.net.Socket;

public class ClientApp {
	JFrame frame;
	Socket client;
	Person person;

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
	public ClientApp(Socket client, Person person) {
		frame = new JFrame("Client");
		this.client = client;
		this.person = person;
		
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
		
		JButton btnNewButton = new JButton("Change PIN");
		btnNewButton.setBounds(216, 189, 126, 25);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblWelcomeBackFellow = new JLabel("Welcome back fellow customer");
		lblWelcomeBackFellow.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblWelcomeBackFellow.setBounds(76, 50, 336, 16);
		frame.getContentPane().add(lblWelcomeBackFellow);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(147, 276, 97, 25);
		frame.getContentPane().add(btnExit);
		
		btnWithdraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String withdraw = JOptionPane.showInputDialog(frame, "Withdraw how much?");
				double withdrawValue = Double.parseDouble(withdraw);
			}
		});
		
		btnDeposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String deposit = JOptionPane.showInputDialog(frame, "Deposit how much?");
				double depositValue = Double.parseDouble(deposit);
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				LoginForm window = new LoginForm();
				
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	
}
