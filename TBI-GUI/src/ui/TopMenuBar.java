package ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class TopMenuBar {
	
	public static MenuBar newMenuBar(StateManager manager) {
		
		MenuBar menuBar = new MenuBar();
		
		//Preferences
		Menu preferences = new Menu("Preferences");
		MenuItem themes = new MenuItem("Themes");
		preferences.getItems().addAll(themes);
		
		//Shortcuts
		Menu shortcuts = new Menu("Shortcuts");
		MenuItem newPat = new MenuItem("New Patient");
		MenuItem patList = new MenuItem("Patient List");
		shortcuts.getItems().addAll(newPat, patList);
		
		menuBar.getMenus().addAll(preferences, shortcuts);
		
		return menuBar;
	}
}