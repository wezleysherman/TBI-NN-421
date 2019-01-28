package utils;

import java.util.Collections;
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
	private String notes;
	private LinkedList<Scan> rawScans;
	private LinkedList<Scan> procScans;
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
		/* Handles adding a new scan to the patient's linked list.
		 *
		 *	Input:
		 * 		- scan: A scan object containing the patient's scan image
		 */
		this.rawScans.add(scan);
		Collections.sort(rawScans);
	}

	public Scan getRawScan(int index) {
		/* Handles getting a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- index: index of scan we want to return
		 */
		return this.rawScans.get(index);
	}
	
	public Scan delRawScan(int index) {
		/* Handles removing a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- index: index of scan we want to return
		 */
		return this.rawScans.remove(index);
	}
	
	public void addProcScan(Scan scan) {
		/* Handles adding a new scan to the patient's linked list.
		 *
		 *	Input:
		 * 		- scan: A scan object containing the patient's analyzed scan image
		 */
		this.procScans.add(scan);
		Collections.sort(procScans);
	}

	public Scan getProcScan(int index) {
		/* Handles getting a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- index: index of scan we want to return
		 */
		return this.procScans.get(index);
	}
	
	public Scan delProcScan(int index) {
		/* Handles removing a scan of a specific index from the linked list
		 *
		 *	Input:
		 * 		- index: index of scan we want to return
		 */
		return this.procScans.remove(index);
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
