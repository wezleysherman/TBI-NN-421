package utils;

import java.io.Serializable;
import java.security.Key;

public class PatientEntry implements Serializable{
	public String name;
	public String uid;
	public Key key;
	
	public PatientEntry(String n, String u, Key k) {
		name = n;
		uid = u;
		key = k;
	}

	public final String getName() {
		return name;
	}

	public final String getUid() {
		return uid;
	}
}
