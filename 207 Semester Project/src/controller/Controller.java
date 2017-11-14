package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.SysAdminApp;
import model.Database;
import model.SysLoginModel;
import view.ClientApp;
import view.LoginFormView;
import view.SysAdmnLgnVw;
import view.AcctView;

public class Controller {
	private SysAdmnLgnVw login;
	private SysLoginModel loginModel;
	
	public Controller(SysAdmnLgnVw login, SysLoginModel loginModel) {
		this.login = login;
		this.loginModel = loginModel;
	}
	
	public class SignUp implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	public class LogIn implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}
}
