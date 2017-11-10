import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class MainFrame extends JFrame {
	
	/*
	 * The default constructor initializing the frame
	 */
	public MainFrame() {
		super();
		
		setLayout(new BorderLayout());
		
		setSize(550, 550);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}	
}
