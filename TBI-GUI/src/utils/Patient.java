package utils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

//Class for saving patient information
@SuppressWarnings("serial")
public class Patient extends Info implements Serializable {

	private String basePath = PatientManagement.buildDefaultPath();
	private Date dateCreated;
	private String notes;
	private LinkedList<Scan> rawScans;
	private LinkedList<Scan> procScans;

	// constructor for blank patient
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
		this.setRawScans(pScans);
		this.procScans = new LinkedList<Scan>();
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

	public LinkedList<Scan> getRawScans() {
		return rawScans;
	}

	public void setRawScans(LinkedList<Scan> scans) {
		this.rawScans = scans;
	}

	public LinkedList<Scan> getProcScans() {
		return procScans;
	}

	public void setProcScans(LinkedList<Scan> scans) {
		this.procScans = scans;
	}

	public Integer getNumRawScans() {
		return this.rawScans.size();
	}

	public Integer getNumProcScans() {
		return this.procScans.size();
	}

	public Date getLastRawScanDate() {
		Scan last = this.rawScans.peek();
		return last.getDateOfScan();
	}

	public void addRawScan(Scan scan) {
		/*
		 * Handles adding a new scan to the patient's linked list.
		 *
		 * Input: - scan: A scan object containing the patient's scan image
		 */
		scan = analyzeScan(scan);
		this.rawScans.add(scan);
		Collections.sort(rawScans);
	}

	public Scan getRawScan(int index) {
		/*
		 * Handles getting a scan of a specific index from the linked list
		 *
		 * Input: - index: index of scan we want to return
		 */
		return this.rawScans.get(index);
	}

	public Scan delRawScan(int index) {
		/*
		 * Handles removing a scan of a specific index from the linked list
		 *
		 * Input: - index: index of scan we want to return
		 */
		return this.rawScans.remove(index);
	}

	public void addProcScan(Scan scan) {
		/*
		 * Handles adding a new scan to the patient's linked list.
		 *
		 * Input: - scan: A scan object containing the patient's analyzed scan image
		 */
		this.procScans.add(scan);
		Collections.sort(procScans);
	}

	public Scan getProcScan(int index) {
		/*
		 * Handles getting a scan of a specific index from the linked list
		 *
		 * Input: - index: index of scan we want to return
		 */
		return this.procScans.get(index);
	}

	public Scan delProcScan(int index) {
		/*
		 * Handles removing a scan of a specific index from the linked list
		 *
		 * Input: - index: index of scan we want to return
		 */
		return this.procScans.remove(index);
	}

	public void savePatient() throws Exception {
		PatientManagement.exportPatient(this);
	}
	
	public Scan analyzeScan(Scan s) {
		String file = s.getScan().getAbsolutePath();
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
