package utils;

import java.io.Serializable;
import java.security.Key;

@SuppressWarnings({ "serial", "rawtypes" })
public class UserEntry extends InfoEntry implements Comparable, Serializable {
	
	public UserEntry(String n, String u, Key k) {
		super(n, u, k);
	}

	public int compareTo(Object o) {
		return super.compareTo(o);
	}
	
}
