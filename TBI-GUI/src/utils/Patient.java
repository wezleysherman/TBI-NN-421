package utils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import java.io.File;
import java.io.Serializable;

//Class for saving patient information
@SuppressWarnings("serial")
public class Patient extends Info implements Serializable {

	private String basePath = PatientManagement.buildDefaultPath();
	private Date dateCreated;
	private String notes;
	private LinkedList<Scan> scans;
	private File picture;
	
	//constructor for blank patient
	public Patient() {
		this("", "", new Date(), "");
	}

	// constructor for fresh patient with no scans
	public Patient(String fName, String lName, Date pDate, String pNotes) {
		this(fName, lName, pDate, pNotes, wrapScan(pDate, null));
	}

	// constructor for fresh patient with 1 scan
	public Patient(String fName, String lName, Date pDate, String pNotes, File pScan) {
		this(fName, lName, pDate, pNotes, wrapScan(pDate, pScan));
	}

	// constructor for patient w/ established uid with 1 scan
	public Patient(String fName, String lName, Date pDate, String pNotes, File pScan, String uid) {
		this(fName, lName, pDate, pNotes, wrapScan(pDate, pScan), uid);

	}

	// constructor for fresh patient with multiple scans
	public Patient(String fName, String lName, Date pDate, String pNotes, LinkedList<Scan> pScans) {
		this(fName, lName, pDate, pNotes, pScans, UUID.nameUUIDFromBytes((fName + " " + lName).getBytes()).toString());

	}

	// constructor for patient w/ established uid and multiple scans (main
	// constructor)
	public Patient(String fName, String lName, Date pDate, String pNotes, LinkedList<Scan> pScans, String uid) {
		super(fName, lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.setScans(pScans);
		this.uid = uid;
		this.file = new File(basePath, uid).getAbsolutePath();
	}

	public static LinkedList<Scan> wrapScan(Date pDate, File pScan) {
		if (pScan == null) {
			return new LinkedList<Scan>();
		}

		Scan newScan = new Scan(pDate, pScan);
		LinkedList<Scan> pScans = new LinkedList<Scan>();
		pScans.push(newScan);
		return pScans;
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

	public LinkedList<Scan> getScans() {
		return scans;
	}

	public void setScans(LinkedList<Scan> scans) {
		this.scans = scans;
	}

	public Integer getNumScans() {
		return this.scans.size();
	}

	public Date getLastScanDate() {
		Scan last = this.scans.peek();
		return last.getDateOfScan();
	}

	public File getPicture() {
		return picture;
	}
	
	public void setPicture(File file) {
		this.picture = file;
	}
	
	public void addScan(Scan scan) {
		/*
		 * Handles adding a new scan to the patient's linked list.
		 *
		 * Input: - scan: A scan object containing the patient's scan image
		 */
		scans.add(scan);
		analyzeScan(scan); //TODO add file location of processed scan to scan object
		Collections.sort(scans);
	}

	public Scan getScan(int index) {
		/*
		 * Handles getting a scan of a specific index from the linked list
		 *
		 * Input: - index: index of scan we want to return
		 */
		return this.scans.get(index);
	}

	public Scan delScan(int index) {
		/*
		 * Handles removing a scan of a specific index from the linked list
		 *
		 * Input: - index: index of scan we want to return
		 */
		return this.scans.remove(index);
	}

	public void savePatient() throws Exception {
		PatientManagement.exportPatient(this);
	}
	
	public Scan analyzeScan(Scan s) {
		String file = s.getRawScan().getAbsolutePath();
		file = file.substring(file.length()-3, file.length());
		System.out.println(file);
		List l = new LinkedList(); l.add("jpg"); l.add("png"); l.add("gif");
		if(!l.contains(file)) {
			s.setLabel("Attempted to analyze but could not due to filetype.");
		}else {
			s = NNUtils.get_label(s);
			System.out.println(String.format("BEST MATCH: %s (%.2f%% likely)", s.getLabel(),
					s.getLabelProb()));
		}
		return s;
	}

}
