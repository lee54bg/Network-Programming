import javax.swing.SwingUtilities;

public class TellerApp {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TellerFrame();
			} // End of run method
		}); // End of SwingUtilities
	}
}
