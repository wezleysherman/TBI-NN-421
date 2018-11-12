package ui;

import java.util.Date;
import javafx.scene.image.Image;

public class Scan {

	private Date dateOfScan;
	private Image scan;
	private String notes;
	
	public Scan(Date dOS, Image inScan) {
		this.setDateOfScan(dOS);
		this.setScan(inScan);
	}
	
	public Scan(Date dOS, Image inScan, String inNotes) {
		this.setDateOfScan(dOS);
		this.setScan(inScan);
		this.setNotes(inNotes);
	}

	public Date getDateOfScan() {
		return dateOfScan;
	}

	public void setDateOfScan(Date dateOfScan) {
		this.dateOfScan = dateOfScan;
	}

	public Image getScan() {
		return scan;
	}

	public void setScan(Image scan) {
		this.scan = scan;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
