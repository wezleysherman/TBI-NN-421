package ui;

import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// TODO: find better way to implement state manager
public class StateManager {
	Stack<String> sceneStack;
	Stage stage;
	StackPane root = new StackPane();
	Scene scene = new Scene(root, 960, 540);
	String sceneID = "landing";
	
	public StateManager(Stage inStage) {
		sceneStack = new Stack<String>();
		stage = inStage;
		stage.setTitle("TBI");
		stage.setWidth(960);
		stage.setHeight(540);
		
		this.paintScene(sceneID);
		
		stage.show();
	}
	
	public void paintScene(String newSceneID) {
			
		sceneID = newSceneID;
	//TODO: Add key and scene when more scenes are added (keys are the names of corresponding buttons)	
		if (sceneID.equals("landing"))
			scene = new LandingScene().initializeScene(stage, this);
		else if (sceneID.equals("newPat"))
			scene = new PatientInfoEntryScene().initializeScene(stage, this);
		else if (sceneID.equals("viewScan"))
			scene = new ScanVisualizerScene().initializeScene(stage, this);
		else if (sceneID.equals("likelyTrauma"))
			scene = new LikelyTraumaAreasScene().initializeScene(stage, this);
		
		stage.setScene(scene);
		
		/* DEBUG CONSOLE OUTPUTS
		System.out.println(sceneID);
		System.out.println(sceneStack.size());
		System.out.println(sceneStack.toString());
		*/
	}
}