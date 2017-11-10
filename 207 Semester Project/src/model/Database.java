package model;

import java.util.ArrayList;

public class Database {
	private ArrayList<Person> people;
	
	public Database() {
		people = new ArrayList<Person>();
	}
	
	public void addPers(Person person) {
		people.add(person);
	}
	
	public ArrayList<Person> getDB() {
		return people;
	}
	
	
}
