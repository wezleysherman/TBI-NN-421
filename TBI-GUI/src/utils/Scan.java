package utils;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.InputMismatchException;

public class Scan implements Serializable, Comparable{

	private Date dateOfScan;
	private File scan;
	private String notes;
	private String label;
	private float labelProb;

	public Scan() {
		this.setDateOfScan(null);
		this.setScan(null);
	}
	
	public Scan(Date dOS, File inScan) {
		this.setDateOfScan(dOS);
		this.setScan(inScan);
	}
	
	public Scan(Date dOS, File inScan, String inNotes) {
		this.setDateOfScan(dOS);
		this.setScan(inScan);
		this.setNotes(inNotes);
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		notes = notes + "\nLabel:" + label;
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

	public File getScan() {
		return scan;
	}

	public void setScan(File scan) {
		this.scan = scan;
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
