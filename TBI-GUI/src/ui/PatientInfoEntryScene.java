package ui;

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
import javafx.stage.Stage;

public class PatientInfoEntryScene {
	
	public static Scene initializeScene(Stage stage) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		TextField patNameField = new TextField();
		TextField fileField = new TextField();
		TextField notesField = new TextField();
		TextField dateField = new TextField();
		Button analyze = new Button();
		
		//Text fields set up and design
		patNameField.setMaxSize(200, 10);
		patNameField.setPromptText("Patient Name");
		
		fileField.setMaxSize(200, 10);
		fileField.setPromptText("Select File");
		
		dateField.setMaxSize(200, 10);
		dateField.setPromptText("Date of Scan");
		
		notesField.setMaxSize(200, 10);
		notesField.setPromptText("Notes");
		
		//Analyze button Setup/Styling
		analyze.setText("Analyze");
		
		analyze.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				BorderPane root = new BorderPane();
				Scene scene = LandingScene.initializeScene(stage);
				stage.setScene(scene);
			}
			
		});
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		GridPane.setConstraints(patNameField, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(fileField, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(dateField, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesField, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(analyze, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		
		contentGrid.getChildren().addAll(patNameField, fileField, dateField, notesField, analyze);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newPatientInfoBar(stage);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setCenter(mainGrid);
		
		Scene landingScene = new Scene(layout, 960, 540);
		
		return landingScene;
	}
}