package main;

import java.awt.EventQueue;

import view.LoginForm;

public class MainProgram {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
