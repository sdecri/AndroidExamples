/**
 * myFile.java
 */
package com.example.androidexamples.storage_and_theme;

/**
 * @author Simone
 * 17/feb/2013
 */
public class MyCheckedFile {

	//ATTRIBUTES
	private boolean checked;
	private String name;
	
	//CONSTRUCTORS
	public MyCheckedFile(String name,boolean checked) {
		super();
		this.checked = checked;
		this.name = name;
	}
	
	public MyCheckedFile(String name) {
		this(name,false);
	}

	//METHODS
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
