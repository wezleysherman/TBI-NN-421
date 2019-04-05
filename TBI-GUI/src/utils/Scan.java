package utils;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.InputMismatchException;

public class Scan implements Serializable, Comparable {

	private Date dateOfScan;
	private File rawScan;
	private File procScan;
	private String notes;
	private String label;
	private float labelProb;

	public Scan() {
		this.setDateOfScan(null);
		this.setRawScan(null);
		this.notes = "";
	}
	
	public Scan(Date dOS, File inScan) {
		this.setDateOfScan(dOS);
		this.setRawScan(inScan);
	}
	
	public Scan(Date dOS, File inScan, String inNotes) {
		this.setDateOfScan(dOS);
		this.setRawScan(inScan);
		this.setNotes(inNotes);
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		if(notes != null) {
			notes = notes + "\nLabel:" + label;
		} else {
			notes = "Label:" + label;
		}
	}

	public float getLabelProb() {
		return labelProb;
	}

	public void setLabelProb(float labelProb) {
		this.labelProb = labelProb;
		notes = notes + "\nProbability:" + labelProb;
	}

	public Date getDateOfScan() {
		return dateOfScan;
	}

	public void setDateOfScan(Date dateOfScan) {
		this.dateOfScan = dateOfScan;
	}

	public File getRawScan() {
		return rawScan;
	}

	public void setRawScan(File scan) {
		this.rawScan = scan;
	}
	
	public File getProcScan() {
		return procScan;
	}

	public void setProcScan(File scan) {
		this.procScan = scan;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public int compareTo(Object other) {
		if(other.getClass() != Scan.class) {
			throw new InputMismatchException("You tried to compare a scan with a not-scan.");
		}
		return ((Scan)other).getDateOfScan().compareTo(this.dateOfScan);
	}
	
}
