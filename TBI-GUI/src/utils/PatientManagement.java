package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

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

		System.out.println(f.getAbsolutePath());
		f.createNewFile();
		FileOutputStream out = new FileOutputStream(patient.getFile() + "/data.enc");
		
		CipherOutputStream cout;
		try {
			byte[] iv = new byte[128/8];
			new SecureRandom().nextBytes(iv);
			ivs = new IvParameterSpec(iv);
			key = KeyGenerator.getInstance("AES").generateKey();
			System.out.println(key);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, ivs);
			SealedObject sobj = new SealedObject(patient, cipher);
			cout = new CipherOutputStream(out, cipher);
			ObjectOutputStream oos = new ObjectOutputStream(cout);
			oos.writeObject(sobj);
		} catch (NoSuchAlgorithmException e) {
			out.close();
			return false;
		} catch (NoSuchPaddingException e) {
			out.close();
			return false;
		} catch (InvalidKeyException e) {
			out.close();
			return false;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
	public static Object importPatient(String path, String uid) throws Exception {
		String fullPath = path + uid + "\\data.enc";
		System.out.println(fullPath);
		System.out.println(key);
		FileInputStream in = new FileInputStream(fullPath);
		ObjectInputStream oin;
		CipherInputStream cin;
		
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, ivs);
			cin = new CipherInputStream(in, cipher);
			oin = new ObjectInputStream(cin);
			SealedObject sobj = (SealedObject)oin.readObject();
			return sobj.getObject(cipher);
		}catch(ClassNotFoundException e) {
			throw e;
		}catch(NoSuchPaddingException e) {
			throw e;
		}catch(InvalidKeyException e){
			throw e;
		}catch(NoSuchAlgorithmException e){
			throw e;
		}catch(Exception e){ 
			throw e;
		}
	}
	
}