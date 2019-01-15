package ui;

import java.io.IOException;
import java.util.Stack;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.PatientEntry;
import utils.Scan;

/**
 * StateManager for the UI, controls painting scenes to the screen and setting the stage
 * @author Canyon Schubert & Ty Chase
 */
public class StateManager {
	private boolean debug = false; //Manually change this value 
	private StackPane root = new StackPane();
	private Scene scene = new Scene(root, 960, 540);
	private Stage stage;
	private Stack<String> sceneStack;
	private String sceneID = "Landing";
	private PatientEntry patient = null;
	private Scan scan = null;
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
	public void setPatient(PatientEntry patient) {
		this.patient = patient;
	}
	public PatientEntry getPatient() {
		return patient;
	}
	public void setScan(Scan scan) {
		this.scan = scan;
	}
	public Scan getScan() {
		return scan;
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
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	public void paintScene(String newSceneID) {			
		this.sceneID = newSceneID; // current scene being displayed(added onto sceneStack when changed)
		
		try {
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
			
			if (debug)
				debugStack();
		} catch (Exception e) {
			makeDialog("No PatientEntry object set in manager \n" + e.getStackTrace());
		}

		stage.setScene(scene);
		stage.getScene().getStylesheets().add(getClass().getResource("../resources/darkTheme.css").toExternalForm());
		
		debugStack();
	}
	
	/**
	 * Raises a dialog box with a message for the user
	 * @param dialogTitle: title of the dialog box
	 * @param message: a message to the user
	 */
	public void makeDialog(String message) {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setHeight(150);
		dialogStage.setWidth(300);
		dialogStage.setResizable(false);
		
		Label messLabel = new Label(message);
		Button close = new Button("Okay");
		close.setOnAction(e -> dialogStage.close());
		Style.styleButton(close);
		
		VBox dialogLayout = new VBox(5);
		GridPane buttonGrid = new GridPane();
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100/3);
		buttonGrid.getColumnConstraints().addAll(columnCon, columnCon, columnCon);
		GridPane.setConstraints(close, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		buttonGrid.getChildren().addAll(close);
		dialogLayout.getChildren().addAll(messLabel, buttonGrid);
		dialogLayout.setAlignment(Pos.CENTER);
		dialogLayout.setStyle("-fx-background-color: #cfd8dc;");
		
		Scene dialogScene = new Scene(dialogLayout);
		dialogStage.setScene(dialogScene);
		dialogStage.showAndWait();
		
	}
	
	/* DEBUG CONSOLE OUTPUTS*/
	public void debugStack() {
		System.out.println(sceneID);
		System.out.println(sceneStack.size());
		System.out.println(sceneStack.toString());
		try {
			System.out.println(patient.getName() + " " + patient.getUid());
		}
		catch (Exception e) {
			System.out.println(patient);
		}
	}
}