package utils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;

//Class for saving patient information
@SuppressWarnings("serial")
public class Patient extends Info implements Serializable {

	private String basePath = PatientManagement.buildDefaultPath();
	private Date dateCreated;
	private String notes;
	private LinkedList<Scan> scans;
	private File picture;

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

	@SuppressWarnings("unchecked")
	public void addScan(Scan scan) {
		/*
		 * Handles adding a new scan to the patient's linked list.
		 *
		 * Input: - scan: A scan object containing the patient's scan image
		 */
		/*try {
			File add = new File(basePath, uid);
			add = new File(add.getAbsolutePath(), scan.getRawScan().getName());
			Files.copy(scan.getRawScan().toPath(), add.toPath());
			scan.setRawScan(add);
		}catch(Exception e) {
			scan.setNotes("Could not move scan; it is located at its original filepath");
		}*/

		analyzeScan(scan);
		scans.add(scan);
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

	public void analyzeScan(Scan s) {
		
		/*
		 * This code can be used in the future if the Java Tensorflow library ever gets
		 * more usable. You'll just need to work with the NNUtils class. String file =
		 * s.getRawScan().getAbsolutePath(); file = file.substring(file.length()-3,
		 * file.length()); System.out.println(file); List l = new LinkedList();
		 * l.add("jpg"); l.add("png"); l.add("gif"); if(!l.contains(file)) {
		 * s.setLabel("Attempted to analyze but could not due to filetype."); }else { s
		 * = NNUtils.get_label(s);
		 * System.out.println(String.format("BEST MATCH: %s (%.2f%% likely)",
		 * s.getLabel(), s.getLabelProb())); } return s;
		 */

		// for now, we're using a quick python script :) all credit to https://www.baeldung.com/run-shell-command-in-java with tweaks

		class PyRunner implements Runnable {
			private InputStream inputStream;
			private Consumer<String> consumer;
			Scan s;
			File p;
			Patient pat;

			public PyRunner(InputStream inputStream, Consumer<String> consumer, Scan s, File p, Patient pat) {
				this.inputStream = inputStream;
				this.consumer = consumer;
				this.s = s;
				this.p = p;
				this.pat = pat;
			}

			@Override
			public void run() {
				try {
					new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
					s.setProcScan(p);
					// this is where we would set the label if we had a label to set
					//s.setLabelProb(100);
					s.setNotes("Analyzed!");
					pat.savePatient();
				} catch(Exception e) {
					System.out.println("Could not save patient");
				}
			}

		}

		try {
			s.setNotes("Analyzing...");
			String os = System.getProperty("os.name");
			ProcessBuilder builder = new ProcessBuilder();
			String cmd = "";
			cmd = "python runAnalysis.py " + s.getRawScan().getAbsolutePath() + " ";
			File proc = new File(s.getRawScan().getParent(), s.getRawScan().getName().replace(".", "_proc."));
			cmd += proc.getAbsolutePath();
			if (os.startsWith("Windows")) {
				builder.command("cmd.exe", "/c", cmd);
			} else {
				builder.command("sh", "-c", cmd);
			}
			File f = new File(System.getProperty("user.dir"), "src");
			f = new File(f.getAbsolutePath(), "python");
			f = new File(f.getAbsolutePath(), "nn");
			builder.directory(f);
			Process process = builder.start();
			PyRunner runner = new PyRunner(process.getInputStream(), System.out::println, s, proc, this);
			Executors.newSingleThreadExecutor().submit(runner);
			/*int exitCode = process.waitFor();
			assert exitCode == 0;*/
		} catch (Exception e) {
			s.setNotes("Image could not be analyzed");
		}
	}

}
