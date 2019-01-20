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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class LandingScene {
			
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane grid = new GridPane();
		Label orLabel = new Label("or");
		Button newPatBtn = new Button();
		Button prevPatBtn = new Button();
		
		Style.styleLabel(orLabel);
		
		//Button Setup/Styling/Tooltips
		newPatBtn.setText("Start New Patient");
		newPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("PatientInfoEntry");
			}
		});
		newPatBtn.setTooltip(new Tooltip("Input information and a scan for a new patient.")); 
		
		Style.styleButton(newPatBtn);	
		
		prevPatBtn.setText("Find Previous Patient");
		prevPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("PreviousPatient");
			}
		});
		prevPatBtn.setTooltip(new Tooltip("View/edit information and scans of patients already in the system."));
		
		Style.styleButton(prevPatBtn);
		
		//Construct Grid		
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(15);
		grid.setHgap(10);
		
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(30);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(40);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(30);
		grid.getColumnConstraints().addAll(column0, column1, column2);
		
		GridPane.setConstraints(newPatBtn, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(orLabel, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(prevPatBtn, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);

		grid.getChildren().addAll(newPatBtn, orLabel, prevPatBtn);
				
		//Add Grid and layout to scene
		Style.styleLandingBorderPane(layout);
		layout.setCenter(grid);
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
	
}