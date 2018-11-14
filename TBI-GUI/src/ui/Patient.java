package ui;

import java.util.Date;
import java.util.LinkedList;
import javafx.scene.image.Image;

//Class for saving patient information
public class Patient {

	private String firstName;
	private String lastName;
	private Date date; // date of latest scan?
	private String notes;
	//TODO: clean up linkedList implementation (size tracker, adding/removing scans, etc)
	private LinkedList<Scan> scans = new LinkedList<Scan>();
	private Integer numScans;
	
	// constructor for fresh patient with no scans or notes
	public Patient(String fName, String lName, Date pDate) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.numScans = scans.size();
	}
	
	// constructor for fresh patient with no scans
	public Patient(String fName, String lName, Date pDate, String pNotes) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.numScans = scans.size();
	}
	
	// constructor for patient with only one scan being entered
	public Patient(String fName, String lName, Date pDate, String pNotes, Image pScan) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		Scan newScan = new Scan(pDate, pScan);
		this.scans.push(newScan);
		this.numScans = scans.size();
	}
	
	// constructor for patient with multiple scans being entered
	public Patient(String fName, String lName, Date pDate, String pNotes, LinkedList<Scan> pScans) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.setScans(pScans);
		this.numScans = scans.size();
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
	
	public LinkedList<Scan> getScans() {
		this.numScans = scans.size();
		return scans;
	}
	
	public void setScans(LinkedList<Scan> scans) {
		this.numScans = scans.size();
		this.scans = scans;
	}
	
	public Integer getNumScans() {
		return numScans;
	}
}
