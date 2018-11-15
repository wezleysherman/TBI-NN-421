package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ui.Patient;

public class PatientManagement{
	
	private static Key key;
	private static LinkedList patientList = refreshPatientList();
	private static final String defaultPath = System.getProperty("user.dir") + "\\src\\resources\\patients\\";
	
	
	public static boolean exportPatient(Patient patient) throws IOException {
		File f = new File(patient.getFile());
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "data.enc");
		
		//create file stream
		f.createNewFile();
		FileOutputStream fout = new FileOutputStream(f.getAbsoluteFile());
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		try {
			//setup cipher
			if(key == null) {
				key = KeyGenerator.getInstance("AES").generateKey();
			}
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			//create sealed object
			SealedObject sobj = new SealedObject(patient, cipher);
			
			//create cipher stream
	        CipherOutputStream cos = new CipherOutputStream(bout, cipher);
	        ObjectOutputStream oos = new ObjectOutputStream(cos);
	        
	        //write object
			oos.writeObject(sobj);
			oos.close();
			
			addPatient(new PatientEntry(patient.getFirstName() + " " + patient.getLastName(),
					   patient.getUID(), key));
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
		//setup output file
		String fullPath = path + uid + "\\data.enc";
		FileInputStream fin = new FileInputStream(fullPath);
		BufferedInputStream bin = new BufferedInputStream(fin);
		
		try {
			//setup cipher
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			//setup decryption
			CipherInputStream cin = new CipherInputStream(bin, cipher);
			ObjectInputStream oin = new ObjectInputStream(cin);
			
			//read and unseal object
			SealedObject sobj = (SealedObject)oin.readObject();
			oin.close();
			
			//return patient
			return (Patient)sobj.getObject(cipher);
		}catch(ClassNotFoundException e) {
			fin.close();
			throw new IOException("Could not parse write as patient. Read failed.");
		}catch(NoSuchPaddingException e) {
			fin.close();
			throw new IOException("Invalid padding on algorithm. Read failed.");
		}catch(InvalidKeyException e){
			fin.close();
			throw new IOException("Invalid key. Read failed.");
		}catch(NoSuchAlgorithmException e){
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
	
	public static void addPatient(PatientEntry p) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
		Key fkey = null;
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "records.enc");
		boolean flag = f.exists();
		System.out.println(f.getAbsolutePath());
		
		FileOutputStream fout = new FileOutputStream(f.getAbsolutePath(), true);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		
		if(!flag) {
			System.out.println("yes");
			//create file stream
			f.createNewFile();
			try {
				fkey = KeyGenerator.getInstance("AES").generateKey();
				System.out.println(fkey.getEncoded());
				byte [] temp = Base64.getEncoder().encode(key.getEncoded());
				bout.write(temp, 0, 24);
				bout.flush();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			FileInputStream fin = new FileInputStream(f.getAbsolutePath());
			BufferedInputStream bin = new BufferedInputStream(fin); 
			byte [] keyBytes = new byte[24];
			bin.read(keyBytes, 0, 24);
			keyBytes = Base64.getDecoder().decode(keyBytes);
		    fkey = new SecretKeySpec(keyBytes, "AES");
			System.out.println(fkey.getEncoded());
		}
		
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, fkey);
		
		//create sealed object
		SealedObject sobj = new SealedObject(p, cipher);
		CipherOutputStream cos = new CipherOutputStream(bout, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        
        //write object
		oos.writeObject(sobj);
		oos.close();
		
		System.out.println("Saving " + p.name + " as " + p.uid + " with " + p.key.getEncoded()); 
	}
	
	public static LinkedList refreshPatientList() {
		
		return null;
	}
	
	public static LinkedList getPatientList() {
		return null;
	}
	
}