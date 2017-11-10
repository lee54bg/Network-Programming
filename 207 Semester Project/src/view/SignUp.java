package view;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class SignUp extends JFrame {
	private FormPanel formPanel;
	
	public SignUp() {
		super("Hello World");
		setSize(400, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
		setVisible(true);
		
		formPanel = new FormPanel();
		getContentPane().add(formPanel, BorderLayout.CENTER);
	}
}
