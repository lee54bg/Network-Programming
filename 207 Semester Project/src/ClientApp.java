import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientApp {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApp window = new ClientApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(460, 414);
		frame.getContentPane().setLayout(null);
		
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
	}
	
	
}
