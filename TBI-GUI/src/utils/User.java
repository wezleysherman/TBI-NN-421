package utils;

import java.util.Date;
import java.io.File;
import java.io.Serializable;

//Class for saving patient information
@SuppressWarnings("serial")
public class User extends Info implements Serializable {

	private static final String basePath = UserManagement.buildDefaultPath();
	//private Theme theme;
	private Date dateCreated;
	private Date lastAccess;
	
	//constructor for user w/o a name
	public User() {
		this("", "", new Date());
	}
	
	//constructor for user w/ a name
	public User(String f, String l, Date d) {
		super(f, l);
		dateCreated = d;
		lastAccess = d;
		//TODO theme = default;
		super.file = new File(basePath, uid).getAbsolutePath();
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
}
