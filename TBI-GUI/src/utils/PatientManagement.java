package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

//for documentation, see TBI-GUI\GUI_DOCS\patientManagementDoc.html
public class PatientManagement {

	private static final String defaultPath = buildDefaultPath();
	private static Hashtable <String, PatientEntry> patientList;

	private static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "patients");
		return f.getAbsolutePath();
	}

	public static String getDefaultPath() {
		return defaultPath;
	}

	public static boolean exportPatient(Patient patient) throws IOException {
		if(patientList == null) {
			importPatientList();
		}

		Key key = null;
		if(patientList.contains(patient.getUID())) {
			key = patientList.get(patient.getUID()).key;
		}

		File f = new File(patient.getFile());
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "data.enc");

		// create file stream
		f.createNewFile();
		FileOutputStream fout = new FileOutputStream(f.getAbsoluteFile());
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		try {
			// setup cipher
			if (key == null) {
				key = KeyGenerator.getInstance("AES").generateKey();
			}
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// create sealed object
			SealedObject sobj = new SealedObject(patient, cipher);

			// create cipher stream
			CipherOutputStream cos = new CipherOutputStream(bout, cipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);

			// write object
			oos.writeObject(sobj);
			oos.close();

			addPatient(new PatientEntry(patient.getFirstName() + " " + patient.getLastName(), patient.getUID(), key));
			return true;
		} catch (NoSuchAlgorithmException e) {
			fout.close();
			throw new IOException("Invalid encryption algorithm. Write failed.");
		} catch (NoSuchPaddingException e) {
			fout.close();
			throw new IOException("Invalid padding. Write failed.");
		} catch (InvalidKeyException e) {
			fout.close();
			throw new IOException("Invalid key used. Write failed.");
		} catch (IllegalBlockSizeException e) {
			fout.close();
			throw new IOException("Object seal failed. Write failed.");
		}
	}

	public static Patient importPatient(String path, String uid) throws IOException {
		// setup output file
		File f = new File(path, uid);
		f = new File(f.getAbsolutePath(), "data.enc");
		FileInputStream fin = new FileInputStream(f.getAbsolutePath());
		BufferedInputStream bin = new BufferedInputStream(fin);

		Key key = null;
		if(patientList.containsKey(uid)){
			key = patientList.get(uid).key;
		} else {
			bin.close();
			return null;
		}

		try {
			// setup cipher
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);

			// setup decryption
			CipherInputStream cin = new CipherInputStream(bin, cipher);
			ObjectInputStream oin = new ObjectInputStream(cin);

			// read and unseal object
			SealedObject sobj = (SealedObject) oin.readObject();
			oin.close();

			// return patient
			return (Patient) sobj.getObject(cipher);
		} catch (ClassNotFoundException e) {
			fin.close();
			throw new IOException("Could not parse write as patient. Read failed.");
		} catch (NoSuchPaddingException e) {
			fin.close();
			throw new IOException("Invalid padding on algorithm. Read failed.");
		} catch (InvalidKeyException e) {
			fin.close();
			throw new IOException("Invalid key. Read failed.");
		} catch (NoSuchAlgorithmException e) {
			fin.close();
			throw new IOException("Invalid algorithm. Read failed.");
		} catch (IllegalBlockSizeException e) {
			fin.close();
			throw new IOException("Could not unseal sealed patient. Read failed");
		} catch (BadPaddingException e) {
			fin.close();
			throw new IOException("Invalid padding on algorithm. Read failed");
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

}
