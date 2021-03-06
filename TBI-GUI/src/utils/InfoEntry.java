package utils;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;

@SuppressWarnings({ "serial", "rawtypes" })
public abstract class InfoEntry implements Serializable, Comparable {
	
	public String name;
	public String uid;
	public Key key;
	public Date date;
	
	public InfoEntry(String n, String u, Key k) {
		name = n;
		uid = u;
		key = k;
		date = new Date();
	}

	public final String getName() {
		return name;
	}

	public final String getUid() {
		return uid;
	}
	
	public final Date getLastDate() {
		return date;
	}

	public int compareTo(Object o) {
		InfoEntry other = (InfoEntry)o;
		int comp = date.compareTo(other.date);
		if(comp == 0) {
			comp = name.compareTo(other.name);
			if(comp == 0) {
				comp = uid.compareTo(other.uid);
			}
		}
		
		return comp;
	}

}
