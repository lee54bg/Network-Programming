package view;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class FormPanel extends JPanel {
	
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
			"Account Type"
	};
	
	private JLabel[]		lbls;
	private JTextField[]	txtFlds;
	
	private JTextField nameField;
	private JTextField occupationField;
	private JButton okBtn;
	
	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 350;
		setPreferredSize(dim);
		
		lbls	= new JLabel[labels.length];
		txtFlds = new JTextField[labels.length];
		
		nameField		= new JTextField(10);
		occupationField = new JTextField(10);
		
		okBtn = new JButton("OK");
		
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
		
		/*for(int i = 0; i < labels.length; i++)
			txtFlds[i] = new JTextField(10);*/
		
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
	}
}
