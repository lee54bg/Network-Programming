import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * Allows users to create a keypad
 */
public class KeyPad extends JPanel {
	private JPanel buttonPanel;
	private JButton clearButton;
	private JTextArea display;
	private TextPanel textPanel;
		
   public KeyPad(TextPanel textPanel) {  
      setLayout(new BorderLayout());
   
      //display = new JTextArea();
      
      
      
      // Make button panel
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(4, 3));
      
      display = textPanel.getTxtArea();  
      
      // Add digit buttons
      addBtn("7");
      addBtn("8");
      addBtn("9");
      addBtn("4");
      addBtn("5");
      addBtn("6");
      addBtn("1");
      addBtn("2");
      addBtn("3");
      addBtn("0");      
      addBtn(".");
      
      // Add clear entry button
      
      clearButton = new JButton("CE");
      buttonPanel.add(clearButton);

      class ClearButtonListener implements ActionListener {  
         public void actionPerformed(ActionEvent event) {  
            display.setText("");
         }
      }
      
      clearButton.addActionListener(
    		  new ClearButtonListener());      
      
      add(buttonPanel, "Center");
   }

   /**
      Adds a button to the button panel 
      @param label the button label
   */
   private void addBtn(final String label) {  
      class BtnListener implements ActionListener {  
         public void actionPerformed(ActionEvent event) {  

            // Don't add two decimal points
            if (label.equals(".")
            		&& display.getText().indexOf(".") != -1) 
               return;

            // Append label text to button
            display.setText(display.getText() + label);
         }
      } // End of DigitButtonListener

      JButton button = new JButton(label);
      buttonPanel.add(button);
      ActionListener listener = new BtnListener();
      button.addActionListener(listener);
   }

   /** 
      Gets the value that the user entered. 
      @return the value in the text field of the keypad
   */
   public double getValue() {  
	   return Double.parseDouble(display.getText());
   }      
} // End of KeyPad
