import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

public class ClientMenu {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMenu window = new ClientMenu();
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
	public ClientMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 646, 542);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(63, 165, 97, 25);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblWelcomeToAtb = new JLabel("Welcome to ATB Bank");
		lblWelcomeToAtb.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblWelcomeToAtb.setBounds(184, 85, 199, 16);
		frame.getContentPane().add(lblWelcomeToAtb);
	}
}
