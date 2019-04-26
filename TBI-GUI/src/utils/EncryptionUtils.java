package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public class EncryptionUtils {

	protected static Key exportInfo(Patient info, Key key) throws IOException{
		File f = new File(info.getFile());
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
			SealedObject sobj = new SealedObject(info, cipher);

			// create cipher stream
			CipherOutputStream cos = new CipherOutputStream(bout, cipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);

			// write object
			oos.writeObject(sobj);
			oos.close();
			
			return key;
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
	
	protected static Info importInfo(Key key, String path, String uid) throws IOException{
		
		// setup output file
		File f = new File(path, uid);
		f = new File(f.getAbsolutePath(), "data.enc");
		FileInputStream fin = new FileInputStream(f.getAbsolutePath());
		
		BufferedInputStream bin = new BufferedInputStream(fin);
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
			return (Info)sobj.getObject(cipher);
		} catch (ClassNotFoundException e) {
			fin.close();
			throw new IOException("Could not parse write as patient. Read failed.");
		} catch (NoSuchPaddingException e) {
			fin.close();
			throw new IOException("Invalid padding on algorithm. Read failed.");
		} catch (StreamCorruptedException e) {
			fin.close();
			throw new IOException("Invalid key. Read failed.");
		} catch (InvalidKeyException e) {
			fin.close();
			e.printStackTrace();
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
		} catch (InvalidClassException e) {
			fin.close();
			throw new IOException("You are attempting to access a previous version of the Patient class. Read failed.");
		} 
	}
	
}
