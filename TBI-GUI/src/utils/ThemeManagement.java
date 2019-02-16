package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

//for documentation, see TBI-GUI\GUI_DOCS\themeManagementDoc.html
public class ThemeManagement {

	private static String defaultPath = buildDefaultPath();
	//current is stored as <"_current", FormattedName>
	private static Hashtable <String, String> themeList; //non-current stored as <FormattedName, filepath>
	private static final String defaultTheme = "Dark Theme";

	public static String buildDefaultPath() {
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "themes");
		return f.getAbsolutePath();
	}

	public static String getDefaultPath() {
		return defaultPath;
	}
	
	public static void setDefaultPath(String dp) {
		defaultPath = dp;
	}
	
	public static String setCurrentTheme(String formattedName) {
		themeList.put("_current", formattedName);
		return themeList.get(formattedName);
	}
	
	public static String getCurrentThemeName() throws IOException {
		if(themeList == null) {
			themeList = importThemeList();
		}
		return themeList.get("_current");
	}
	
	public static String getCurrentThemePath() throws IOException {
		if(themeList == null) {
			themeList = importThemeList();
		}
		return themeList.get(themeList.get("_current"));
	}
	
	public static void deleteTheme(String path, String filename) throws IOException{
		if(filename.equals(themeList.get(themeList.get("_current")))){
			themeList.put("_current", defaultTheme);
		}
		File top = new File(path, filename);
		File data = new File(top.getAbsolutePath(), "data.enc");
		data.delete();
		top.delete();
		
		remTheme(filename);
	}

	public static void addTheme(String formattedName, String filename) throws IOException{
		if(themeList == null) {
			themeList = importThemeList();
		}
		themeList.put(formattedName, filename);
		exportThemeList();
	}
	
	public static void remTheme(String filename) throws IOException{
		if(themeList == null) {
			themeList = importThemeList();
		}
		
		themeList.remove(filename);
		exportThemeList();
	}

	public static void exportThemeList() throws IOException{
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "record.enc");

		// create file stream
		f.createNewFile();
		FileOutputStream fout = new FileOutputStream(f.getAbsoluteFile());

		ObjectOutputStream oos = new ObjectOutputStream(fout);

		// write object
		oos.writeObject(themeList);
		oos.close();
	}

	public static Hashtable <String, String> importThemeList() throws IOException{
		File f = new File(defaultPath);
		f.mkdirs();
		f = new File(f.getAbsolutePath(), "record.enc");

		if(!f.exists()) {
			// create file stream
			f.createNewFile();
			themeList = new Hashtable <String, String>();
			themeList.put("_current", defaultTheme);
			exportThemeList();
			return themeList;
		} else {
			FileInputStream fin = new FileInputStream(f.getAbsolutePath());
			try {
				ObjectInputStream oin = new ObjectInputStream(fin);
				// read and unseal object
				themeList = (Hashtable)oin.readObject();
				oin.close();
				// return theme
				return themeList;
			} catch (ClassNotFoundException e) {
				fin.close();
				throw new IOException("Could not parse write as theme. Read failed.");
			}
		}
	}

	public static Hashtable <String, String> getThemeList(){
		if(themeList == null) {
			try {
				themeList = importThemeList();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return themeList;
	}

}
