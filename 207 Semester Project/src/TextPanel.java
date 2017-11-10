import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextPanel extends JPanel {
	
	private JTextArea txtArea;
	private String txtToSet;
	
	public TextPanel() {
		txtArea = new JTextArea();
		txtArea.setEditable(false);
		
		setLayout(new BorderLayout());
		
		add(new JScrollPane(txtArea), BorderLayout.CENTER);
	}
	
	public JTextArea getTxtArea() {
		return txtArea;
	}
	
	public void appendText(String text) {
		txtArea.append(text);
	}
	
	public void clearTxt() {
		txtArea.setText("");		
	}
	
	public void getTxt() {
		txtArea.getText();
	}
	
	public void setTxt(String txtToSet) {
		txtArea.setText(txtToSet);
	}
	
	public void addToTxt(final String label) {
		txtArea.setText(txtArea.getText() + label);
	}
}