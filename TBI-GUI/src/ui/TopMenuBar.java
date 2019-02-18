package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * REFERENCE: http://tutorials.jenkov.com/javafx/menubar.html
 * @author Ty Chase
 */
public class TopMenuBar {
	
	public static MenuBar newMenuBar(StateManager manager) {
		
		MenuBar menuBar = new MenuBar();
		
		//Shortcuts
		Menu shortcuts = new Menu("Shortcuts");
		
		MenuItem home = new MenuItem("Home");
		home.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (manager.makeQuestion("Returning to the home page will cause you to lose all unsaved information. Are you sure?")) {
					manager.getSceneStack().clear();
					manager.paintScene("Landing");
				}
			}
		});
		
		MenuItem newPat = new MenuItem("New Patient");
		newPat.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("PatientInfoEntry");
			}
		});
		
		MenuItem patList = new MenuItem("Patient List");
		patList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("PreviousPatient");
			}
		});
		
		shortcuts.getItems().addAll(home, newPat, patList);
		
		//Preferences
		Menu preferences = new Menu("Preferences");
		Menu themes = new Menu("Themes");
		
		MenuItem darkTheme = new MenuItem("Dark");
		darkTheme.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.setThemeFile("../resources/themes/darkTheme.css");
			}
		});
		
		themes.getItems().addAll(darkTheme); //Add themes here
		
		preferences.getItems().addAll(themes);
		
		menuBar.getMenus().addAll(shortcuts, preferences);
		
		return menuBar;
	}
}