import javax.swing.SwingUtilities;

public class MainApp {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame();
			}
		});		
	}
}
