package ui;

import java.util.Date;
import java.util.LinkedList;
import javafx.scene.image.Image;
import java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

//Class for saving patient information
public class Patient implements Serializable{

	private static final String basePath = buildDefaultPath();
	private String firstName;
	private String lastName;
	private String file;
	private Date dateCreated;
	private Date lastScanDate;
	private String notes;
	//TODO: clean up linkedList implementation (size tracker, adding/removing scans, etc)
	private LinkedList<Scan> scans = new LinkedList<Scan>();
	private Integer numScans;
	private UUID uid;

	// constructor for fresh patient with no scans (needs UID generated)
	public Patient(String fName, String lName, Date pDate, String pNotes) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.uid = UUID.nameUUIDFromBytes((fName + " " + lName).getBytes());
		this.file = new File(basePath, uid.toString()).getAbsolutePath();
		//System.out.println(file);
		this.setLastScanDate(pDate);
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
		this.setLastScanDate(pDate);
		this.numScans = scans.size();
		this.uid = UUID.nameUUIDFromBytes((fName + " " + lName).getBytes());
		this.file = basePath + uid;
	}

	// constructor for patient with multiple scans being entered
	public Patient(String fName, String lName, Date pDate, String pNotes, LinkedList<Scan> pScans) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.setScans(pScans);
		this.setLastScanDate(pDate);
		this.numScans = scans.size();
		this.uid = UUID.nameUUIDFromBytes((fName + " " + lName).getBytes());
		this.file = basePath + uid;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDate() {
		return this.dateCreated;
	}

	public void setDate(Date date) {
		this.dateCreated = date;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getFile(){
		return file;
	}

	public String getUID() {
		return uid.toString();
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
		return this.numScans;
	}

	public void setLastScanDate(Date date) {
		this.lastScanDate = date;
	}

	public Date getLastScanDate() {
		return this.lastScanDate;
	}

	public void addScan(Scan scan) {
		/* Handles adding a new scan to the patient's linked list.
		 *
		 *	Input:
		 * 		- scan: A dicom scan object conainting the patient's scan image
		 */
		this.numScans ++;
		this.scans.add(scan);
		Date scanDate = scan.getDateOfScan();
		this.setLastScanDate(scanDate);
	}

	public Scan getScan(int idx) {
		/* Handles getting a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- idx: index of scan we want to return
		 */
		Scan returnScan = this.scans.get(idx);
		return returnScan;
	}

	public void savePatient() {
		// To-Do: Implement once branch is merged containing data encrcyption
	}

	private static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "patients");
		return f.getAbsolutePath();
	}
}
