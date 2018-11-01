package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

/**
 * The user interface starter
 * @author Ty Chase
 * REFERENCES:
 * https://docs.oracle.com/javase/8/javafx/get-started-tutorial/hello_world.htm
 */
public class App extends Application {

	@Override
	public void start(Stage stage) {
		
		StateManager manager = new StateManager(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}