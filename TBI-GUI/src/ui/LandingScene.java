package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

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
		
		//Button Setup/Styling
		newPatBtn.setText("Start New Patient");
		newPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("newPat");
			}
		});
		newPatBtn.setStyle(BUTTON_DEFAULT);
		newPatBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				newPatBtn.setStyle(BUTTON_ENTERED);
			}
		});
		newPatBtn.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				newPatBtn.setStyle(BUTTON_DEFAULT);
			}
		});
		newPatBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				newPatBtn.setStyle(BUTTON_PRESSED);
			}
		});
		
		prevPatBtn.setText("Find Previous Patient");
		prevPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("prevPat");
			}
		});
		prevPatBtn.setStyle(BUTTON_DEFAULT);
		prevPatBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevPatBtn.setStyle(BUTTON_ENTERED);
			}
		});
		prevPatBtn.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevPatBtn.setStyle(BUTTON_DEFAULT);
			}
		});
		prevPatBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevPatBtn.setStyle(BUTTON_PRESSED);
			}
		});
		
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
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("viewScan");
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
}
