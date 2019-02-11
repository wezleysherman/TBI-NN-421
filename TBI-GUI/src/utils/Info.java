package utils;
import java.util.Date;
import java.io.Serializable;

//Class for saving patient information
@SuppressWarnings("serial")
public class Info implements Serializable {

	protected String firstName;
	protected String lastName;
	protected String file;
	protected String uid;
	
	
	public Info(String f, String l) {
		firstName = f;
		lastName = l;
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

	public String getFile() {
		return file;
	}

	public String getUID() {
		return uid;
	}
}
