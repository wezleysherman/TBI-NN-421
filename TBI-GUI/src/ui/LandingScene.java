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
	
	public static boolean debug = true; //Manually change this value
	private final static String BACKGROUND_COLOR = "-fx-background-color: #455357";
	private final static String BUTTON_DEFAULT = " -fx-background-color: #cfd8dc;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	private final static String BUTTON_ENTERED = " -fx-background-color: #b1babe;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	private final static String BUTTON_PRESSED = " -fx-background-color: #c5ced2;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";

			
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane grid = new GridPane();
		Label orLabel = new Label("or");
		Button newPatBtn = new Button();
		Button prevPatient = new Button();
		Button algoVisBtn = new Button();
		Button viewScanBtn = new Button();
		Button viewCNNBtn = new Button();
		
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
		
		algoVisBtn.setText("Algorithm Visualizer");
		algoVisBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("algoVis");
			}
		});
		
		
		viewScanBtn.setText("View Scan <DEBUG>");
		viewScanBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("viewScan");
			}
		});
		
		viewCNNBtn.setText("View CNN Vis <DEBUG>");
		viewCNNBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("viewCNN");
			}
		});
		
		//Previous Patient Button Setup
		prevPatient.setText("Find Previous Patient");
		prevPatient.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("prevPat");
				
			}
			
		});
		
		//Construct Grid
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(15);
		grid.setHgap(10);
		
		GridPane.setConstraints(newPatBtn, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(orLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(prevPatient, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(algoVisBtn, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		grid.getChildren().addAll(newPatBtn, orLabel, prevPatient, algoVisBtn);
		if (debug) {
			GridPane.setConstraints(viewScanBtn, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(viewCNNBtn, 0,9, 1, 1, HPos.CENTER, VPos.CENTER);
			grid.getChildren().addAll(viewScanBtn, viewCNNBtn);
		}
			
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
