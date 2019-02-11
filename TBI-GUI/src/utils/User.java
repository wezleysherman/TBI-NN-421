package utils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import java.io.File;
import java.io.Serializable;

//Class for saving patient information
@SuppressWarnings("serial")
public class User implements Serializable {

	private static final String basePath = buildDefaultPath();
	private String firstName;
	private String lastName;
	private String file;
	//private Theme theme;
	private Date dateCreated;
	private Date lastAccess;
	private String uid;
	
	//constructor for user w/o a name
	public User() {
		this("", "", new Date());
	}
	
	//constructor for user w/ a name
	public User(String f, String l, Date d) {
		firstName = f;
		lastName = l;
		dateCreated = d;
		lastAccess = d;
		//TODO theme = default;
		this.file = new File(basePath, uid).getAbsolutePath();
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

	public Date getLastAccessDate() {
		return lastAccess;
	}
	
	public Date getCreatedDate() {
		return dateCreated;
	}

	//TODO
	/*public Theme getTheme() {
		return theme;
	}*/
	
	//TODO
	/*public Theme setTheme(int index) {
		theme = themes(index);
	}*/
	
	//TODO
	/*public Theme resetTheme() {
		theme = default;
	}*/

	private static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "patients");
		return f.getAbsolutePath();
	}
}
