import java.awt.BorderLayout;

public class ClientFrame extends MainFrame {
	
	private TextPanel textPanel;
	private Toolbar toolbar;
	private FormPanel formPanel;
	private String mainFrameType;
		
	public ClientFrame() {
		super();
		toolbar = new Toolbar();
		textPanel = new TextPanel();
		formPanel = new FormPanel();
		
		toolbar.setStringListener(new StringListener() {
			public void textEmitted(String text) {
				textPanel.appendText(text);
			}
		});
		
		formPanel.setFormListener(new FormListener() {
			public void formEventOccurred(FormEvent e) {
				String name = e.getName();
				String occupation = e.getOccupation();
				
				textPanel.appendText(name + ": " + occupation + "\n");
			}
		});
		
		//add(formPanel, BorderLayout.WEST);
		//add(toolbar, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
		add(new KeyPad(textPanel), BorderLayout.SOUTH);
	} // End of ClientFrame
	
	public void mainMenu() {
		
	}
}
