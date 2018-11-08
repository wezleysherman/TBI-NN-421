package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import ui.Patient;

public class PatientManagement{
	
	private static Key key;
	
	public static boolean exportPatient(Patient patient) throws IOException {
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bytes);
		
		File f = new File(patient.getFile());
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "data.enc");

		System.out.println(f.getAbsolutePath());
		f.createNewFile();
		FileOutputStream out = new FileOutputStream(patient.getFile() + "/data.enc");
		
		CipherOutputStream cipherOut;
		try {
			key = KeyGenerator.getInstance("AES").generateKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherOut = new CipherOutputStream(out, cipher);
		} catch (NoSuchAlgorithmException e) {
			out.close();
			return false;
		} catch (NoSuchPaddingException e) {
			out.close();
			return false;
		} catch (InvalidKeyException e) {
			out.close();
			return false;
		}
		
		oos.writeObject(patient);
		byte [] bar = bytes.toByteArray();
		cipherOut.write(bar);
		out.flush();
        out.close();
		return true;
	}
	
	public static Patient importPatient() {
		
		return null;
	}
	
}