package utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import java.io.File;
import java.io.Serializable;

//Class for saving patient information
@SuppressWarnings("serial")
public class Patient implements Serializable {

	private static final String basePath = buildDefaultPath();
	private String firstName;
	private String lastName;
	private String file;
	private Date dateCreated;
	private Date lastScanDate;
	private String notes;
	//TODO: clean up linkedList implementation (size tracker, adding/removing scans, etc)
	private LinkedList<Scan> rawScans;
	private LinkedList<Scan> procScans;
	private Integer numRawScans;
	private Integer numProcScans;
	private UUID uid;
	
	//constructor for blank patient
	public Patient() {
		this("", "", new Date(), "");
	}
	
	//constructor for fresh patient with no scans
	public Patient(String fName, String lName, Date pDate, String pNotes) {
		this(fName, lName, pDate, pNotes, wrapScan(pDate, null));
	}
	
	//constructor for fresh patient with 1 scan
	public Patient(String fName, String lName, Date pDate, String pNotes, File pScan) {
		this(fName, lName, pDate, pNotes, wrapScan(pDate, pScan));
	}

	// constructor for fresh patient with multiple scans (main constructor)
	public Patient(String fName, String lName, Date pDate, String pNotes, LinkedList<Scan> pScans) {
		this.setFirstName(fName);
		this.setLastName(lName);
		this.setDate(pDate);
		this.setNotes(pNotes);
		this.setRawScans(pScans);
		this.procScans = new LinkedList<Scan>();
		this.setLastScanDate(pDate);
		this.numRawScans = rawScans.size();
		this.uid = UUID.nameUUIDFromBytes((fName + " " + lName).getBytes());
		this.file = new File(basePath, uid.toString()).getAbsolutePath();
	}
	
	public static LinkedList<Scan> wrapScan(Date pDate, File pScan){
		if(pScan == null) {
			return new LinkedList<Scan>();
		}
		
		Scan newScan = new Scan(pDate, pScan);
		LinkedList<Scan> pScans = new LinkedList<Scan>();
		pScans.push(newScan);
		return pScans;
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

	public String getFile() {
		return file;
	}

	public String getUID() {
		return uid.toString();
	}

	public LinkedList<Scan> getRawScans() {
		this.numRawScans = rawScans.size();
		return rawScans;
	}

	public void setRawScans(LinkedList<Scan> scans) {
		this.numRawScans = scans.size();
		this.rawScans = scans;
	}
	
	public LinkedList<Scan> getProcScans() {
		this.numProcScans = procScans.size();
		return procScans;
	}

	public void setProcScans(LinkedList<Scan> scans) {
		this.numProcScans = scans.size();
		this.procScans = scans;
	}

	public Integer getNumRawScans() {
		return this.numRawScans;
	}
	
	public Integer getNumProcScans() {
		return this.numProcScans;
	}

	public void setLastScanDate(Date date) {
		this.lastScanDate = date;
	}

	public Date getLastScanDate() {
		return this.lastScanDate;
	}

	public void addRawScan(Scan scan) {
		/* Handles adding a new scan to the patient's linked list.
		 *
		 *	Input:
		 * 		- scan: A dicom scan object conainting the patient's scan image
		 */
		this.numRawScans ++;
		this.rawScans.add(scan);
		Date scanDate = scan.getDateOfScan();
		this.setLastScanDate(scanDate);
	}

	public Scan getRawScan(int idx) {
		/* Handles getting a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- idx: index of scan we want to return
		 */
		Scan returnScan = this.rawScans.get(idx);
		return returnScan;
	}
	
	public void addProcScan(Scan scan) {
		/* Handles adding a new scan to the patient's linked list.
		 *
		 *	Input:
		 * 		- scan: A dicom scan object conainting the patient's scan image
		 */
		this.numProcScans ++;
		this.procScans.add(scan);
		Date scanDate = scan.getDateOfScan();
	}

	public Scan getProcScan(int idx) {
		/* Handles getting a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- idx: index of scan we want to return
		 */
		Scan returnScan = this.procScans.get(idx);
		return returnScan;
	}

	public void savePatient() throws Exception {
		PatientManagement.exportPatient(this);
	}

	private static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "patients");
		return f.getAbsolutePath();
	}
}
