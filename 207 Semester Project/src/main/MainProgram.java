package main;

import java.awt.EventQueue;

import view.LoginFormView;

public class MainProgram {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFormView window = new LoginFormView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} // End of main program	
}
