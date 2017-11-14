package view;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import model.Database;
import model.Person;


public class FormPanel extends JPanel {
	private Database db;
	
	private String labels[] = {
			"First Name",
			"Last Name",
			"Date of Birth",
			"Social Security",
			"Email Address",
			"Phone Number",
			"PIN",
			"Address",
			"City",
			"State",
			"Account Type",
			"Username",
			"Password"
	};
	
	private String btns[] = {"Submit", "Cancel"};
	
	private JFrame frames;
	
	private JLabel[]		lbls;
	private JTextField[]	txtFlds;
	private JButton[]		okBtn;
	
	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 350;
		setPreferredSize(dim);
		
		db = new Database();
		
		lbls	= new JLabel[labels.length];
		txtFlds = new JTextField[labels.length];
		okBtn	= new JButton[btns.length];
		
		Border innerBorder = BorderFactory.createTitledBorder("Sign Up");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.insets = new Insets(20, 5, 5, 20);
		
		for(int i = 0; i < labels.length; ++i) {
			lbls[i] = new JLabel(labels[i]);
			gc.gridx = 0;
			gc.gridy = i;
			gc.fill = GridBagConstraints.NONE;
			gc.anchor = GridBagConstraints.LINE_END;
			add(lbls[i], gc);
		}
		
		for(int i = 0; i < labels.length; i++) {
			if( (i == 2) || (i == 10) )
				continue;
			else {
				txtFlds[i] = new JTextField(15);
				gc.gridx = 1;
				gc.gridy = i;
				gc.anchor = GridBagConstraints.LINE_START;
				add(txtFlds[i], gc);
			}
		} // End of for loop
		
		for(int i = 0; i < btns.length; i++) {
			okBtn[i] = new JButton(btns[i]);
			gc.gridx = i;
			gc.gridy = 13;
			if( i == 0 )
				gc.anchor = GridBagConstraints.LINE_END;
			else
				gc.anchor = GridBagConstraints.LINE_START;
			add(okBtn[i], gc);
		}
		
		// Submit Button
		okBtn[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				int i;
//				
//				for(i = 0; i < labels.length; i++) {
//					if( (i == 2) || (i == 10) )
//						continue;
//					else
//						if(txtFlds[i].getText().equals("")) {
//							JOptionPane.showMessageDialog(frames, "Please fill out the form");
//							break;
//						}
//					
//					if(i == 9)
//						break;
//				} // End of for loop
				validateSignUp();
				
//				if( !isANum(txtFlds[3].getText()) )
//					JOptionPane.showMessageDialog(frames, "Please enter a valid SSN");
//				else if( !isANum(txtFlds[6].getText()) )
//					JOptionPane.showMessageDialog(frames, "Please enter a valid PIN");
//				else if(db.searchForUser(txtFlds[11].getText()))
//					JOptionPane.showMessageDialog(frames, "Username already exists.\n"
//													+ "Please try another Username.");
//				else {
//					Person p = new Person(getUserName(), getPassWd());
//					p.setFirstName(getFirstName());
//					p.setLastName(getLastName());
//					p.setSocSec(getSSN());
//					p.setEmail(getEmail());
//					p.setPhoneNum(getPhoneNum());
//					p.setPinNum(getPIN());
//					p.setAddress(getAddress());
//					p.setCity(getCity());
//					p.setState(getState());
//					p.setChkAct(genAcctNum());
//					p.setSvgAct(genAcctNum());
//					
//					db.connect();
//					db.addPers(p);
//					db.disconnect();
//				}
			}
		});
		
		// Cancel
		okBtn[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientLoginView window = new ClientLoginView();
				frames.dispose();
			}
		}); // End of cancel button		
		
	} // End of Constructor
	
	/*
	 * Valiidate Sign Up once fields
	 * have been fully filled
	 */
	public void validateSignUp() {
		// Number of valid fields need to be match
		int numOfVldFlds = 0;
		
		// First Name
		if( !txtFlds[0].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid First Name");
		// Last Name
		if( !txtFlds[1].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid Last Name");
		// SSN
		if( txtFlds[3].getText().matches("^\\d{9}$")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid 9 digit SSN");
		// Email
		if( txtFlds[4].getText().contains("@"))
			numOfVldFlds++;
		else
			JOptionPane.showMessageDialog(frames, "Please enter a valid email");
		// Phone Number
		if( !txtFlds[5].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid Phone Number");
		// PIN number
		if( txtFlds[6].getText().matches("^\\d{4}$"))
			numOfVldFlds++;
		else
			JOptionPane.showMessageDialog(frames, "Please enter a valid 4 digit PIN");
		// Address
		if( !txtFlds[7].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid address");
		// City
		if( !txtFlds[8].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid city");
		// State
		if( !txtFlds[9].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid state");
		// Username
		if( !txtFlds[11].getText().equals("")) {
			numOfVldFlds++;
		} else
			JOptionPane.showMessageDialog(frames, "Please enter a valid username");
		// Password
		if( txtFlds[12].getText().length() >= 8 )
			numOfVldFlds++;
		else
			JOptionPane.showMessageDialog(frames, "Please enter a password that\nhas "
					+ "minimum of 8 characters");
		
		// final check to create new account
		if( numOfVldFlds == 11) {
			Person p = new Person(getUserName(), getPassWd());
			p.setFirstName(getFirstName());
			p.setLastName(getLastName());
			p.setSocSec(getSSN());
			p.setEmail(getEmail());
			p.setPhoneNum(getPhoneNum());
			p.setPinNum(getPIN());
			p.setAddress(getAddress());
			p.setCity(getCity());
			p.setState(getState());
			p.setChkAct(genAcctNum());
			p.setSvgAct(genAcctNum());
			
			db.connect();
			if(db.searchForUser(txtFlds[11].getText())) {
				JOptionPane.showMessageDialog(frames, "Username already exists.\n"
												+ "Please try another Username.");
			}
			else {
				db.addPers(p);
				db.disconnect();
				JOptionPane.showMessageDialog(frames, "You have created an account successfully!\n"
				+ "Your Checking account No. is " + p.getChkAct()
				 + "\nYour Savings account No. is " + p.getSvgAct() );

				ClientLoginView window = new ClientLoginView();
				frames.dispose();
				
			}
		}

		
	}
	
	/*
	 * Methods to acquire proper data from the
	 * Textfields
	 */
	public String getFirstName() {
		return txtFlds[0].getText();
	}
	
	public String getLastName() {
		return txtFlds[1].getText();
	}
	
	public int getSSN() {
		return Integer.parseInt(txtFlds[3].getText());
	}
	
	public String getEmail() {
		return txtFlds[4].getText();
	}
	
	public String getPhoneNum() {
		return txtFlds[5].getText();
	}
	
	public int getPIN() {
		return Integer.parseInt(txtFlds[6].getText());
	}
	
	public String getAddress() {
		return txtFlds[7].getText();
	}
	
	public String getCity() {
		return txtFlds[8].getText();
	}
	
	public String getState() {
		return txtFlds[9].getText();
	}
	
	public String getUserName() {
		return txtFlds[11].getText();
	}
	
	public String getPassWd() {
		return txtFlds[12].getText();
	}
	
	/*
	 * Check to see if this is a string
	 * Returns True: if this is a number
	 * False: if this isn't
	 */
	public boolean isANum(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}
	
	/*
	 * Generate Account Numbers for both Checking and Savings
	 */
	public int genAcctNum() {
		Random rand = new Random();
		int accountNum,
			length = 0;
		
		do {
			accountNum = 100000000 + rand.nextInt(900000000);
			
		} while( (length = String.valueOf(accountNum).length()) != 9);
		
		return accountNum;
	}
	
	/*
	 * Getter and Setter for JFrame
	 */
	public JFrame getFrames() {
		return frames;
	}

	public void setFrames(JFrame frames) {
		this.frames = frames;
	}
}