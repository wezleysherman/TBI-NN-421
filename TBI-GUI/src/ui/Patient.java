package ui;

import java.util.Date;

//Class for saving patient information
public class Patient {

	private String firstName;
	private String lastName;
	private String file;
	private Date date;
	private String notes;
	
	public Patient(String fName, String lName, String fileN, Date pDate, String pNotes) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setFile(fileN);
		this.setDate(pDate);
		this.setNotes(pNotes);
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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
