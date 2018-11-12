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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;

import ui.Patient;

public class PatientManagement{
	
	private static Key key;
	private static IvParameterSpec ivs;
	
	public static boolean exportPatient(Patient patient) throws IOException {
		File f = new File(patient.getFile());
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "data.enc");
		
		//create file stream
		f.createNewFile();
		FileOutputStream fout = new FileOutputStream(patient.getFile() + "/data.enc");
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		try {
			//setup cipher
			byte[] iv = new byte[128/8];
			new SecureRandom().nextBytes(iv);
			ivs = new IvParameterSpec(iv);
			key = KeyGenerator.getInstance("AES").generateKey();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, ivs);
			
			//create sealed object
			SealedObject sobj = new SealedObject(patient, cipher);
			
			//create cipher stream
	        CipherOutputStream cos = new CipherOutputStream(bout, cipher);
	        ObjectOutputStream oos = new ObjectOutputStream(cos);
	        
	        //write object
			oos.writeObject(sobj);
			oos.close();
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
		} catch (InvalidAlgorithmParameterException e) {
			fout.close();
			throw new IOException("Invalid algorithm parameters. Write failed.");
		}
	}
	
	public static Patient importPatient(String path, String uid) throws IOException {
		//setup output file
		String fullPath = path + uid + "\\data.enc";
		FileInputStream fin = new FileInputStream(fullPath);
		BufferedInputStream bin = new BufferedInputStream(fin);
		
		try {
			//setup cipher
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, ivs);
			
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
		} catch (InvalidAlgorithmParameterException e) {
			fin.close();
			throw new IOException("Invalid algorithm parameter. Read failed");
		} catch (IllegalBlockSizeException e) {
			fin.close();
			throw new IOException("Could not unseal sealed patient. Read failed");
		} catch (BadPaddingException e) {
			fin.close();
			throw new IOException("Invalid padding on algorithm. Read failed");
		}
	}
	
}