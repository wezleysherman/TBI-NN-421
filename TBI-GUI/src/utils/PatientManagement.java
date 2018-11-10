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
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import ui.Patient;

public class PatientManagement{
	
	private static Key key;
	
	public static boolean exportPatient(Patient patient) throws IOException {
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bout);
		
		File f = new File(patient.getFile());
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "data.enc");

		System.out.println(f.getAbsolutePath());
		f.createNewFile();
		FileOutputStream out = new FileOutputStream(patient.getFile() + "/data.enc");
		
		CipherOutputStream cout;
		try {
			key = KeyGenerator.getInstance("AES").generateKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cout = new CipherOutputStream(out, cipher);
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
		byte [] bar = bout.toByteArray();
		cout.write(bar);
		out.flush();
        out.close();
		return true;
	}
	
	public static Object importPatient(String path, String uid) throws IOException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException {
		String fullPath = path + uid + "\\data.enc";
		System.out.println(fullPath);
		File file = new File(fullPath);
		FileInputStream in = new FileInputStream(fullPath);
		ObjectInputStream oin;
		CipherInputStream cin;
		ByteArrayInputStream bin;
		Object ret = null;
		
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			cin = new CipherInputStream(in, cipher);
			byte[] bar = new byte[(int)file.length()];
			int read = cin.read(bar);
			while(read >= 0) {
				System.out.println(read);
				System.out.println((int)file.length()-read);
				read += cin.read(bar, read, (int)file.length()-read);
				System.out.println(read);
			}
			bin = new ByteArrayInputStream(bar);
			oin = new ObjectInputStream(bin);
			ret = oin.readObject();
			return ret;
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