package main;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

public class SysAdminApp {

	private JFrame frmSystemAdmin;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_5;
	private JTable table;

	private String colNms[] = {"First", "Last", "Role"};
	
	private Object data[][] = {
			{"Bob", "Garcia", "Teller",},
			{"Anne", "Buenaflores", "Client",},
			{"Joe", "Hisaishi", "Teller",}
	};
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SysAdminApp window = new SysAdminApp();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SysAdminApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSystemAdmin = new JFrame();
		frmSystemAdmin.setTitle("System Admin");
		frmSystemAdmin.setBounds(100, 100, 400, 470);
		frmSystemAdmin.setVisible(true);
		frmSystemAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		table = new JTable(data, colNms);
		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		
		JScrollPane tblScrl = new JScrollPane(table);
		tblScrl.setBounds(0, 190, 375, 170);
		
		JPanel viewPanel = new JPanel();
		viewPanel.setToolTipText("Advanced Search");
		JPanel crtePanel = new JPanel();
		JPanel delPanel = new JPanel();
		
		tabbedPane.add("View", viewPanel);
		viewPanel.setLayout(null);
		tabbedPane.add("Create", crtePanel);
		GridBagLayout gbl_crtePanel = new GridBagLayout();
		gbl_crtePanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_crtePanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_crtePanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_crtePanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		crtePanel.setLayout(gbl_crtePanel);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 1;
		crtePanel.add(comboBox, gbc_comboBox);
		
		JLabel lblFirstName = new JLabel("First Name:");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.EAST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 1;
		gbc_lblFirstName.gridy = 2;
		crtePanel.add(lblFirstName, gbc_lblFirstName);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 2;
		gbc_textField_3.gridy = 2;
		crtePanel.add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name:");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 1;
		gbc_lblLastName.gridy = 3;
		crtePanel.add(lblLastName, gbc_lblLastName);
		
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 2;
		gbc_textField_4.gridy = 3;
		crtePanel.add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 1;
		gbc_lblUsername.gridy = 4;
		crtePanel.add(lblUsername, gbc_lblUsername);
		
		textField_6 = new JTextField();
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 0);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 2;
		gbc_textField_6.gridy = 4;
		crtePanel.add(textField_6, gbc_textField_6);
		textField_6.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email:");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 1;
		gbc_lblEmail.gridy = 5;
		crtePanel.add(lblEmail, gbc_lblEmail);
		
		textField_7 = new JTextField();
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 0);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 2;
		gbc_textField_7.gridy = 5;
		crtePanel.add(textField_7, gbc_textField_7);
		textField_7.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 6;
		crtePanel.add(lblPassword, gbc_lblPassword);
		
		textField_8 = new JTextField();
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 0);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 2;
		gbc_textField_8.gridy = 6;
		crtePanel.add(textField_8, gbc_textField_8);
		textField_8.setColumns(10);
		
		JButton btnNewButton = new JButton("Create");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 11;
		crtePanel.add(btnNewButton, gbc_btnNewButton);
		tabbedPane.add("Delete", delPanel);
		
		JMenuBar menuBar = new JMenuBar();
		frmSystemAdmin.setJMenuBar(menuBar);
		
		JMenuItem mntmFile = new JMenuItem("Preferences");
		menuBar.add(mntmFile);
		
		viewPanel.add(tblScrl);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(40, 43, 56, 16);
		viewPanel.add(lblName);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(62, 103, 56, 16);
		viewPanel.add(lblId);
		
		JLabel lblOr = new JLabel("or");
		lblOr.setBounds(117, 74, 56, 16);
		viewPanel.add(lblOr);
		
		textField_9 = new JTextField();
		textField_9.setBounds(93, 40, 116, 22);
		viewPanel.add(textField_9);
		textField_9.setColumns(10);
		
		textField_10 = new JTextField();
		textField_10.setBounds(93, 100, 116, 22);
		viewPanel.add(textField_10);
		textField_10.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Search");
		btnNewButton_1.setBounds(50, 138, 97, 25);
		viewPanel.add(btnNewButton_1);
		
		JLabel lblSearchBy = new JLabel("Search by");
		lblSearchBy.setBounds(93, 11, 116, 16);
		viewPanel.add(lblSearchBy);
		
		JButton btnNewButton_2 = new JButton("Advanced Search");
		btnNewButton_2.setToolTipText("Advanced Search");
		btnNewButton_2.setBounds(165, 138, 97, 25);
		viewPanel.add(btnNewButton_2);
		
		frmSystemAdmin.getContentPane().add(tabbedPane);
	}
}
