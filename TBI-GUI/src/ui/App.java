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
		
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 960, 540);
		Scene landingScene = new LandingScene().initializeScene(stage);
		
		stage.setTitle("TBI");
	    stage.setScene(landingScene);
	    stage.setWidth(960);
	    stage.setHeight(540);

	    stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}