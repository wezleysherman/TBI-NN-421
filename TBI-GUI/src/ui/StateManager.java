package ui;

import java.util.Stack;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// TODO: find better way to implement state manager
/**
 * StateManager for the UI, controls painting scenes to the screen and setting the stage
 * @author Canyon Schubert
 *
 */
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
		stage.setMinWidth(960);
		stage.setMinHeight(540);
		
		this.paintScene(sceneID);
		
		stage.show();
	}
	
	/**
	 * Determines which scene to paint to the stage
	 * @param newSceneID :is currently a distinct string indicating which scene will be displayed, it is different than sceneID for stack pushing purposes
	 */
	@SuppressWarnings("static-access")
	public void paintScene(String newSceneID) {			
		sceneID = newSceneID;
	//TODO: Add key and scene when more scenes are added (keys are the names of corresponding buttons)	
		if (sceneID.equals("landing"))
			scene = new LandingScene().initializeScene(this);
		else if (sceneID.equals("newPat"))
			scene = new PatientInfoEntryScene().initializeScene(this);
		else if (sceneID.equals("viewScan"))
			scene = new ScanVisualizerScene().initializeScene(this);
		else if (sceneID.equals("likelyTrauma"))
			scene = new LikelyTraumaAreasScene().initializeScene(this);
		else if (sceneID.equals("viewCNN"))
			scene = new CNNVisualizationScene().initializeScene(this);
		else if (sceneID.equals("prevPat"))
			scene = new PreviousPatientScene().initializeScene(this);
		else if (sceneID.equals("algoVis"))
			scene = new AlgorithmVisualizerScene().initializeScene(this);
		
		stage.setScene(scene);
		
		/* DEBUG CONSOLE OUTPUTS
		System.out.println(sceneID);
		System.out.println(sceneStack.size());
		System.out.println(sceneStack.toString());
		*/
	}
	
	/**
	 * Determines which scene to paint to the stage
	 * @param newSceneID :is currently a distinct string indicating which scene will be displayed, it is different than sceneID for stack pushing purposes
	 */
	@SuppressWarnings("static-access")
	public void paintScene(String newSceneID, Patient patient) {			
		sceneID = newSceneID;
		if (sceneID.equals("patInfo"))
			scene = new PatientInfoScene().initializeScene(this, patient);
		
		stage.setScene(scene);
	}
	
}