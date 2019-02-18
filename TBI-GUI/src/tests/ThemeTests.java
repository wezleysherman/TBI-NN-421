package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.ThemeManagement;

public class ThemeTests {
	public String defaultPath = ThemeManagement.getDefaultPath();
	public Hashtable<String, String> themes = ThemeManagement.getThemeList();

	@Test
	public void testBasics() {
		//folder path
		File f = new File(System.getProperty("user.dir"), "src");
		f = new File(f.getAbsolutePath(), "resources");
		f = new File(f.getAbsolutePath(), "themes");
		assertEquals(f.getAbsolutePath(), defaultPath);
		
		//folder exists & has permissions
		assertTrue(f.exists());
		assertTrue(f.canRead());
		assertTrue(f.canWrite());
	}
	
	public void testListUtils() throws Exception {
		assertTrue(themes.get("_current") != null);
		ThemeManagement.addTheme("Dark Theme", "darkTheme.css");
		ThemeManagement.addTheme("Green Theme", "greenTheme.css");
		ThemeManagement.setCurrentTheme("Green Theme");
		assertEquals(ThemeManagement.getCurrentThemeName(), "Green Theme");
		assertEquals(ThemeManagement.getThemeList().contains("Green Theme"), true);
		
		File f = new File(defaultPath, "greenTheme.css");
		assertEquals(ThemeManagement.getCurrentThemePath(), f.getAbsolutePath());
		
		ThemeManagement.deleteTheme(defaultPath, "greenTheme.css");
		assertEquals(ThemeManagement.getThemeList().contains("Green Theme"), false);
		assertEquals(ThemeManagement.getCurrentThemeName(), "Dark Theme");
	
	}

}
