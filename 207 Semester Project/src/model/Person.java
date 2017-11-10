package model;

import java.io.Serializable;

public class Person implements Serializable {
	private int id;
	private int age;
	private int pinNum;
	private int chkAct;
	private int svgAct;
	private int socSec;
	private int totalBalance;
	private int numOfTries;
	private int deposit, withdraw;
	
	private String firstName;
	private String email;
	private String phoneNum;
	private String lastName;
	private String occupation;
	private AgeCategory ageCategory;
	
	private boolean gender;
	private boolean actDisabled;
	
}
