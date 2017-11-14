package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import main.SysAdminApp;
import model.Database;
import model.SysLoginModel;
import view.ClientApp;
import view.ClientLoginView;
import view.PreferencesView;
import view.SysAdmnLgnVw;
import view.AcctView;

public class SysAdminController {
	private SysAdmnLgnVw sysAdLogIn;
	private SysLoginModel loginModel;
	private SysAdminApp sysAdminApp;
	
	public SysAdminController(SysAdmnLgnVw sysAdLogIn, SysLoginModel loginModel) {
		this.sysAdLogIn = sysAdLogIn;
		this.loginModel = loginModel;
		
		this.sysAdLogIn.addBtnLoginListener( new SysLogIn() );
		this.sysAdLogIn.addCancelListener( new Cancel() );
		this.sysAdLogIn.addPrefListener( new Preferences() );
	}
	
	public class SysLogIn implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( loginModel.verifyUser( sysAdLogIn.getUserName(), sysAdLogIn.getPassWd() ) )
				sysAdminApp = new SysAdminApp();
			else
				JOptionPane.showMessageDialog(sysAdLogIn, "Please try again");
		}
	}
	
	public class Cancel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sysAdLogIn.dispose();
		}
	}
	
	public class Preferences implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			PreferencesView preferencesView = new PreferencesView();
			sysAdLogIn.dispose();
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
