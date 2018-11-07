package ui;

import java.util.Date;
import java.util.UUID;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

//Class for saving patient information
public class Patient implements Serializable{

	private String firstName;
	private String lastName;
	private String file;
	private Date date;
	private String notes;
	private UUID uid;
	
	//constructor for a new patient (needs UID generated)
	public Patient(String fName, String lName, String fileN, Date pDate, String pNotes) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setFile(fileN);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.uid = UUID.nameUUIDFromBytes((fName + " " + lName).getBytes());
	}
	
	//constructor for a previous patient (already has UID)
	public Patient(String fName, String lName, String fileN, Date pDate, String pNotes, String uid) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setFile(fileN);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.uid = UUID.fromString(uid);
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
	
	public String getUID() {
		return uid.toString();
	}
	private void writeObject(java.io.ObjectOutputStream out) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
		Key key = KeyGenerator.getInstance("AES").generateKey();
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		
		
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
		
	}
		 
	private void readObjectNoData() throws ObjectStreamException{
		
	}
}
