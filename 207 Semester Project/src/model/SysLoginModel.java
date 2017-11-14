package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import view.AcctView;

public class SysLoginModel {
	private boolean verified;
	
	private String ipAddress = "127.0.0.1";
	private int portNum = 3000;
	
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	
	public boolean verifyUser(String userName, String passWord) {
		
		try {
			// Your sending the username and password to the server for verification
			client	= new Socket(ipAddress, portNum);
			out		= new DataOutputStream(client.getOutputStream());
			out.writeUTF(userName);
			out.writeUTF(passWord);
			out.flush();
							
			// Your receiving that verification back
			in	= new DataInputStream(client.getInputStream());
								
			verified = in.readBoolean();
			
			if(verified == true) {
				out.close();
				in.close();
				return true;
			} else {
				client.close();
				return false;
			}	
		} catch (ConnectException con) {
			con.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return false; 
	} // End of initialize method	
} // End of LoginForm Class
