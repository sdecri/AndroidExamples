/**
 * Contact.java
 */
package com.example.androidexamples.fragment_example.support;

import java.io.Serializable;

import android.nfc.FormatException;

/**
 * @author Simone
 * 06/gen/2013
 */
public class Contact implements Serializable{

	//CONSTANTS
	//private final static String[] names= {"Simone","Chiara","Marco","Giulia",}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ATTRIBUTES
	private String firstName;
	private String lastName;
	private long phone;
	private String email;
	
	//CONSTRUCTORS
	public Contact() {}
	
	/**
	 * 
	 * @param firstName
	 * @param secondName
	 * @param phone
	 * @param email
	 * @throws FormatException
	 */
	public Contact(String firstName, String secondName, long phone,
			String email) throws FormatException {
		super();
		if(!email.contains("@")) throw new FormatException("Bad email format");
		this.firstName = firstName;
		this.lastName = secondName;
		this.phone = phone;
		this.email = email;
	}

	
	
	//METHODS
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the phone
	 */
	public long getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(long phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 * @throws FormatException 
	 */
	public void setEmail(String email) throws FormatException {
		if(!email.contains("@")) throw new FormatException("Bad email format");
		this.email = email;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + (int) (phone ^ (phone >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !obj.getClass().equals(getClass())) return false;
		Contact c=(Contact) obj;
		
		return c.firstName.equals(email) && c.lastName.equals(lastName) &&
				c.getEmail().equals(email) && c.getPhone()==phone;
	}

	//STATIC METHODS
	/**
	 * Retunr <code>n</code> generic <code>Contatc</code>
	 * @param n
	 * @return
	 * @throws FormatException 
	 */
	public static Contact[] getRandomContact(int n) throws FormatException {
		Contact[] toReturn=new Contact[n];
		
		for(int i=0;i<n;i++) {
			toReturn[i]=new Contact();
			toReturn[i].setFirstName("FirstName"+(i+1));
			toReturn[i].setLastName("LastName"+(i+1));
			toReturn[i].setPhone(3000000000l + (long)((999999999l)*(Math.random())));
			toReturn[i].setEmail(toReturn[i].getFirstName()+"."+toReturn[i].getLastName()+"@gmail.com");
		}
		return toReturn;
	}
	
}
