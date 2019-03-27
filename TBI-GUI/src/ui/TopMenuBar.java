package ui;

import java.io.File;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * REFERENCES: 
 * http://tutorials.jenkov.com/javafx/menubar.html
 * https://www.baeldung.com/java-file-extension
 * https://blog.idrsolutions.com/2014/04/use-external-css-files-javafx/
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
		
		//File directory = new File(TopMenuBar.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "resources/themes");
		File directory = new File("./src/resources/themes");
		for (File file : directory.listFiles()) {
			String fileName = file.getName();
			if (!fileName.equals("greenTheme.css")) { //factor out test theme
				if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("css")) {
					String themeName = fileName.substring(0, fileName.lastIndexOf("."));
					MenuItem theme = new MenuItem(themeName);
					
					theme.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							if (manager.getThemeFile().equals("file:///" + file.getAbsolutePath().replace("\\", "/"))) {
								manager.makeDialog("This theme is already loaded.");
							}
							else {
								manager.setThemeFile("file:///" + file.getAbsolutePath().replace("\\", "/"));
							}
						}
					});
					themes.getItems().add(theme);
				}
			}
		}
		
		MenuItem themeCreate = new MenuItem("Theme Creator");
		themeCreate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("ThemeCreation");
			}
		});
		
		preferences.getItems().addAll(themes, themeCreate);
		
		menuBar.getMenus().addAll(shortcuts, preferences);
		
		return menuBar;
	}
}