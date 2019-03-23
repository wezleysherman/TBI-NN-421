package utils;

import java.io.File;
import java.util.Date;

public class Holder {
	
	private File file;
	private Date date;
	
	public Holder() {
		file = null;
		date = null;
	}
	
	public void setFile(File in) {
		this.file = in;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setDate(Date in) {
		this.date = in;
	}
	
	public Date getDate() {
		return date;
	}
}