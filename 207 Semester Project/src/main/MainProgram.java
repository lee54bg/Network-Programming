package main;

import java.awt.EventQueue;

import view.ClientLoginView;

public class MainProgram {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLoginView window = new ClientLoginView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} // End of main program	
}
