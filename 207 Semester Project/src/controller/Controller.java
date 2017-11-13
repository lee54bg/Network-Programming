package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Database;
import view.ClientApp;
import view.LoginForm;
import view.SysAdminApp;
import view.AcctView;

public class Controller {
	Database db;
	
	// These are our views
	LoginForm	login;
	AcctView	acctView;
	ClientApp	client;
	SysAdminApp	sysAdmin;
	
	public Controller(LoginForm login) {
		this.login = login;
	}
	
	public Controller(ClientApp	client) {
		this.client = client;
	}
	
	public Controller(SysAdminApp sysAdmin) {
		this.sysAdmin = sysAdmin;
	}
	
	public Controller(AcctView	acctView) {
		this.acctView = acctView;
	}
	
	class CalculateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}
}
