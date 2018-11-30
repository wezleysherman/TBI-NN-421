package ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The user interface starter
 * @author Ty Chase
 * REFERENCES:
 * https://docs.oracle.com/javase/8/javafx/get-started-tutorial/hello_world.htm
 */
public class App extends Application {
	
	@Override
	public void start(Stage stage) {
		@SuppressWarnings("unused") //TODO: look into the implications of fixing this warning (low severity)
		StateManager manager = new StateManager(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}