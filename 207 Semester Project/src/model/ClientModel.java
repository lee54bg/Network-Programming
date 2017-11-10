package model;

public class ClientModel {
	private double checkings;
	private double savings;
	
	public void wthdrwFrmChk(double withdraw) {
		checkings -= withdraw; 
	}
	
	public void wthdrwFrmSvg(double withdraw) {
		checkings -= withdraw; 
	}
	
	public void depFrmChk(double deposit) {
		checkings += deposit; 
	}
	
	public void depFrmSvg(double deposit) {
		checkings += deposit;
	}
	
	public double getCheckings() {
		return checkings;
	}

	public double getSavings() {
		return savings;
	}
}
