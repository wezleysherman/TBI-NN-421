package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class LandingScene {
	
	public static boolean debug = false; //Manually change this value
	private final static String BACKGROUND_COLOR = "-fx-background-color: #455357";
	private final static String BUTTON_DEFAULT = " -fx-background-color: #f1fafe;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	private final static String BUTTON_ENTERED = " -fx-background-color: #c1cace;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	private final static String BUTTON_PRESSED = " -fx-background-color: #919a9e;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	// old colors: cfd8dc, b1babe, c5ced2
			
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane grid = new GridPane();
		Label orLabel = new Label("or");
		Button newPatBtn = new Button();
		Button prevPatBtn = new Button();
		
		//Button Setup/Styling/Tooltips
		newPatBtn.setText("Start New Patient");
		newPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("newPat");
			}
		});
		String newPatTT = "Input information and a scan for a new patient.";
		newPatBtn.setTooltip(new Tooltip(newPatTT)); 
		
		styleButton(newPatBtn);	
		
		prevPatBtn.setText("Find Previous Patient");
		prevPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("prevPat");
			}
		});
		String prevPatTT = "View/edit information and scans of patients already in the system.";
		prevPatBtn.setTooltip(new Tooltip(prevPatTT));
		
		styleButton(prevPatBtn);
		
		//Construct Grid
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(15);
		grid.setHgap(10);
		
		GridPane.setConstraints(newPatBtn, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(orLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(prevPatBtn, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);

		grid.getChildren().addAll(newPatBtn, orLabel, prevPatBtn);
		
		// DEBUG BUTTONS
		
		Button viewScanBtn = new Button("View Scan <DEBUG>");
		viewScanBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				LinkedList<Scan> scans = new LinkedList<Scan>();
				Image img = new Image("/resources/TestImage1.jpg");
				Image img2 = new Image("/resources/TestImage1.jpg");
				Image img3 = new Image("/resources/TestImage1.jpg");
				Image img4 = new Image("/resources/TestImage1.jpg");
				scans.push(new Scan(new Date(115, 3, 12), img));
				scans.push(new Scan(new Date(116, 4, 2), img2));
				scans.push(new Scan(new Date(117, 8, 11), img3));
				scans.push(new Scan(new Date(), img4));
				Patient patient = new Patient("John", "Doe", new Date(), 
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
						+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure "
						+ "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non "
						+ "proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
						);
				patient.setScans(scans);
				
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("viewScan", patient);
			}
		});
		GridPane.setConstraints(viewScanBtn, 0,0,1,1);
		
		Button likelyTraumaBtn = new Button("Trauma Areas Visualizer <DEBUG>");
		likelyTraumaBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("likelyTrauma");
			}
		});
		GridPane.setConstraints(likelyTraumaBtn, 0, 1, 1, 1);
		
		// add button to whichever page you are working on and turn debug on (remember to turn it off before merging)
		if (debug) grid.getChildren().addAll(viewScanBtn/*, likelyTraumaBtn*/);
			
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		grid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		grid.getColumnConstraints().add(columnCon);
		
		//Add Grid and layout to scene
		layout.setStyle(BACKGROUND_COLOR);
		layout.setCenter(grid);
		
		double x = manager.stage.getWidth();
		double y = manager.stage.getHeight();
		Scene scene = new Scene(layout, x, y);
		
		return scene;
	}
	
	//Styles Buttons to make layout and style of page
	private static void styleButton(Button button) {
		button.setStyle(BUTTON_DEFAULT);
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				button.setStyle(BUTTON_ENTERED);
			}
		});
		button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				button.setStyle(BUTTON_DEFAULT);
			}
		});
		button.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				button.setStyle(BUTTON_PRESSED);
			}
		});
	}
}
