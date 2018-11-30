package ui;

import java.util.Stack;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * StateManager for the UI, controls painting scenes to the screen and setting the stage
 * @author Canyon Schubert & Ty Chase
 */
public class StateManager {
	private StackPane root = new StackPane();
	private Scene scene = new Scene(root, 960, 540);
	private Stage stage;
	private Stack<String> sceneStack;
	private String sceneID = "Landing";
	private Patient patient = null;
	private boolean stateBool = false;
	
	public Stage getStage() {
		return stage;
	}
	public Stack<String> getSceneStack() {
		return sceneStack;
	}
	public void setSceneID(String sceneID) {
		this.sceneID = sceneID;
	}
	public String getSceneID() {
		return sceneID;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setStateBool(boolean stateBool) {
		this.stateBool = stateBool;
	}
	public boolean getStateBool() {
		return stateBool;
	}
	
	// constructor (mostly just the app start)
	public StateManager(Stage inStage) {
		sceneStack = new Stack<String>();
		stage = inStage;
		stage.setTitle("TBI");
		stage.setWidth(960);
		stage.setHeight(540);
		stage.setMinWidth(960);
		stage.setMinHeight(540);
		
		paintScene(sceneID);
		
		stage.show();
	}
	
	/**
	 * Determines which scene to paint to the stage
	 * @param newSceneID :is currently a distinct string indicating which scene will be displayed, it is different than sceneID for stack pushing purposes
	 */
	@SuppressWarnings("static-access")
	public void paintScene(String newSceneID) {			
		this.sceneID = newSceneID; // current scene being displayed(added onto sceneStack when changed)
		
		if (sceneID.equals("Landing")) {
			this.patient = null;
			scene = new LandingScene().initializeScene(this);
		}
		else if (sceneID.equals("PatientInfoEntry")) {
			this.patient = null;
			scene = new PatientInfoEntryScene().initializeScene(this);
		}
		else if (sceneID.equals("LikelyTraumaAreas"))
			scene = new LikelyTraumaAreasScene().initializeScene(this);
		else if (sceneID.equals("CNNVisualizer"))
			scene = new CNNVisualizationScene().initializeScene(this);
		else if (sceneID.equals("PreviousPatient"))
			scene = new PreviousPatientScene().initializeScene(this);
		else if (sceneID.equals("AlgorithmVisualizer"))
			scene = new AlgorithmVisualizerScene().initializeScene(this);
		else if (sceneID.equals("ScanVisualizer"))
			scene = new ScanVisualizerScene().initializeScene(this);
		else if (sceneID.equals("PatientInfo")) {
			scene = new PatientInfoScene().initializeScene(this);
		}
		
		stage.setScene(scene);
		
		debugStack();
	}
	/* DEBUG CONSOLE OUTPUTS*/
	public void debugStack() {
		System.out.println(sceneID);
		System.out.println(sceneStack.size());
		System.out.println(sceneStack.toString());
		try {
			System.out.println(patient.getFirstName() + " " + patient.getLastName());
		}
		catch (Exception e) {
			System.out.println(patient);
		}
	}
}