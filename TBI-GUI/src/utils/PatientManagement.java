package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;

//for documentation, see TBI-GUI\GUI_DOCS\patientManagementDoc.html
public class PatientManagement {

	private static String defaultPath = buildDefaultPath();
	private static Hashtable <String, PatientEntry> patientList;

	public static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "patients");
		return f.getAbsolutePath();
	}

	public static String getDefaultPath() {
		return defaultPath;
	}
	
	public static void setDefaultPath(String dp) {
		defaultPath = dp;
	}

	public static boolean exportPatient(Patient patient) throws IOException {
		if(patientList == null) {
			importPatientList();
		}
		Key key = null;
		if(patientList.contains(patient.getUID())) {
			key = patientList.get(patient.getUID()).key;
		}
		key = EncryptionUtils.exportInfo(patient, key);
		addPatient(new PatientEntry(patient.getFirstName() + " " + patient.getLastName(), patient.getUID(), key));
		return true;
	}

	public static Patient importPatient(String path, String uid) throws IOException {
		try {
			Key key = null;
			if(patientList.containsKey(uid)){
				key = patientList.get(uid).key;
			} else {
				return null;
			}
			return (Patient)EncryptionUtils.importInfo(key, path, uid);
		} catch (FileNotFoundException e) {
			throw new IOException("The UID you are trying to access does not exist. Read failed.");
		}
	}
	
	public static void deletePatient(String path, String uid) throws IOException{
		File top = new File(path, uid);
		File data = new File(top.getAbsolutePath(), "data.enc");
		data.delete();
		top.delete();
		
		remPatient(uid);
	}

	public static void addPatient(PatientEntry p) throws IOException{
		if(patientList == null) {
			patientList = importPatientList();
		}
		patientList.put(p.uid, p);
		exportPatientList();
	}
	
	public static void remPatient(String uid) throws IOException{
		if(patientList == null) {
			patientList = importPatientList();
		}
		
		patientList.remove(uid);
		exportPatientList();
	}

	public static void exportPatientList() throws IOException{
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "record.enc");

		// create file stream
		f.createNewFile();
		FileOutputStream fout = new FileOutputStream(f.getAbsoluteFile());

		ObjectOutputStream oos = new ObjectOutputStream(fout);

		// write object
		oos.writeObject(patientList);
		oos.close();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable <String, PatientEntry> importPatientList() throws IOException{
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "record.enc");

		if(!f.exists()) {
			// create file stream
			f.createNewFile();
			patientList = new Hashtable <String, PatientEntry>();
			exportPatientList();
			return patientList;
		} else {
			FileInputStream fin = new FileInputStream(f.getAbsolutePath());
			try {
				ObjectInputStream oin = new ObjectInputStream(fin);
				// read and unseal object
				patientList = (Hashtable)oin.readObject();
				oin.close();
				// return patient
				return patientList;
			} catch (ClassNotFoundException e) {
				fin.close();
				throw new IOException("Could not parse write as patient. Read failed.");
			}
		}
	}

	public static Hashtable <String, PatientEntry> getPatientList(){
		if(patientList == null) {
			try {
				patientList = importPatientList();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return patientList;
	}
	
	@SuppressWarnings("unchecked")
	public static LinkedList <PatientEntry> dateSortPatients(){
		if(patientList == null) {
			try {
				patientList = importPatientList();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LinkedList<PatientEntry> l = new LinkedList<PatientEntry>(patientList.values());
		Collections.sort(l);
		
		return l;
	}

}
