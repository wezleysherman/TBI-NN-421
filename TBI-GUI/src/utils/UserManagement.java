package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

//for documentation, see TBI-GUI\GUI_DOCS\ userManagementDoc.html
public class UserManagement {

	private static String defaultPath = buildDefaultPath();
	private static Hashtable <String, UserEntry> userList;

	public static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "users");
		return f.getAbsolutePath();
	}

	public static String getDefaultPath() {
		return defaultPath;
	}
	
	public static void setDefaultPath(String dp) {
		defaultPath = dp;
	}

	public static boolean exportUser(User user) throws IOException {
		if(userList == null) {
			importUserList();
		}

		Key key = null;
		if(userList.contains(user.getUID())) {
			key = userList.get(user.getUID()).key;
		}

		File f = new File(user.getFile());
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
			SealedObject sobj = new SealedObject(user, cipher);

			// create cipher stream
			CipherOutputStream cos = new CipherOutputStream(bout, cipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);

			// write object
			oos.writeObject(sobj);
			oos.close();
			
			addUser(new UserEntry(user.getFirstName() + " " + user.getLastName(), user.getUID(), key));
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

	public static User importUser(String path, String uid) throws IOException {
		try {
			// setup output file
			File f = new File(path, uid);
			f = new File(f.getAbsolutePath(), "data.enc");
			FileInputStream fin = new FileInputStream(f.getAbsolutePath());
			
			BufferedInputStream bin = new BufferedInputStream(fin);
	
			Key key = null;
			if(userList.containsKey(uid)){
				key = userList.get(uid).key;
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
	
				// return user
				return (User) sobj.getObject(cipher);
			} catch (ClassNotFoundException e) {
				fin.close();
				throw new IOException("Could not parse write as user. Read failed.");
			} catch (NoSuchPaddingException e) {
				fin.close();
				throw new IOException("Invalid padding on algorithm. Read failed.");
			} catch (StreamCorruptedException e) {
				fin.close();
				throw new IOException("Invalid key. Read failed.");
			} catch (InvalidKeyException e) {
				fin.close();
				throw new IOException("Invalid key. Read failed.");
			} catch (NoSuchAlgorithmException e) {
				fin.close();
				throw new IOException("Invalid algorithm. Read failed.");
			} catch (IllegalBlockSizeException e) {
				fin.close();
				throw new IOException("Could not unseal sealed user. Read failed");
			} catch (BadPaddingException e) {
				fin.close();
				throw new IOException("Invalid padding on algorithm. Read failed");
			} catch (InvalidClassException e) {
				fin.close();
				throw new IOException("You are attempting to access a previous version of the User class. Read failed.");
			} 
		} catch (FileNotFoundException e) {
			throw new IOException("The UID you are trying to access does not exist. Read failed.");
		}
	}
	
	public static void deleteUser(String path, String uid) throws IOException{
		File top = new File(path, uid);
		File data = new File(top.getAbsolutePath(), "data.enc");
		data.delete();
		top.delete();
		
		remUser(uid);
	}

	public static void addUser(UserEntry p) throws IOException{
		if(userList == null) {
			userList = importUserList();
		}
		userList.put(p.uid, p);
		exportUserList();
	}
	
	public static void remUser(String uid) throws IOException{
		if(userList == null) {
			userList = importUserList();
		}
		
		userList.remove(uid);
		exportUserList();
	}

	public static void exportUserList() throws IOException{
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "record.enc");

		// create file stream
		f.createNewFile();
		FileOutputStream fout = new FileOutputStream(f.getAbsoluteFile());

		ObjectOutputStream oos = new ObjectOutputStream(fout);

		// write object
		oos.writeObject(userList);
		oos.close();
	}

	public static Hashtable <String, UserEntry> importUserList() throws IOException{
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "record.enc");

		if(!f.exists()) {
			// create file stream
			f.createNewFile();
			userList = new Hashtable <String, UserEntry>();
			exportUserList();
			return userList;
		} else {
			FileInputStream fin = new FileInputStream(f.getAbsolutePath());
			try {
				ObjectInputStream oin = new ObjectInputStream(fin);
				// read and unseal object
				userList = (Hashtable)oin.readObject();
				oin.close();
				// return user
				return userList;
			} catch (ClassNotFoundException e) {
				fin.close();
				throw new IOException("Could not parse write as user. Read failed.");
			}
		}
	}

	public static Hashtable <String, UserEntry> getUserList(){
		if(userList == null) {
			try {
				userList = importUserList();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userList;
	}
	
	public static LinkedList <UserEntry> dateSortUsers(){
		if(userList == null) {
			try {
				userList = importUserList();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LinkedList<UserEntry> l = new LinkedList<UserEntry>(userList.values());
		Collections.sort(l);
		
		return l;
	}

}
