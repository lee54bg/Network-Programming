package model;

import java.io.Serializable;

/*
 * Serializable allows us to send objects over java using ObjectOutputStream
 * and ObjectInputStream
 */
public class Person implements Serializable {
	private int id;
	private int age;
	private int pinNum;
	private int chkAct;
	private int svgAct;
	private int socSec;
	private double chkBalance;
	private double svgsBalance;
	private int numOfTries;
	private int deposit, withdraw;
	
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNum;
	private String userName;
	private String passWd;	
	
	/*
	 * Getters and Setters.  Ignore this since this is just a Data Structure that
	 * will hold the information of a person
	 */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPinNum() {
		return pinNum;
	}

	public void setPinNum(int pinNum) {
		this.pinNum = pinNum;
	}

	public int getChkAct() {
		return chkAct;
	}

	public void setChkAct(int chkAct) {
		this.chkAct = chkAct;
	}

	public int getSvgAct() {
		return svgAct;
	}

	public void setSvgAct(int svgAct) {
		this.svgAct = svgAct;
	}

	public int getSocSec() {
		return socSec;
	}

	public void setSocSec(int socSec) {
		this.socSec = socSec;
	}

	public int getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(int totalBalance) {
		this.totalBalance = totalBalance;
	}

	public int getNumOfTries() {
		return numOfTries;
	}

	public void setNumOfTries(int numOfTries) {
		this.numOfTries = numOfTries;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public int getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(int withdraw) {
		this.withdraw = withdraw;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWd() {
		return passWd;
	}

	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public boolean isActDisabled() {
		return actDisabled;
	}

	public void setActDisabled(boolean actDisabled) {
		this.actDisabled = actDisabled;
	}

	private String occupation;
	private AgeCategory ageCategory;
	
	private boolean gender;
	private boolean actDisabled;
	
	public Person(String userName, String passWd) {
		this.userName	= userName;
		this.passWd		= passWd;
	}
}
