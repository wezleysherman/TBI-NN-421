package ui;

import java.io.File;
import java.time.LocalDate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;

public class PatientInfoEntryScene {
	final static String BACKGROUND_COLOR = "-fx-background-color: #cfd8dc";
	
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		TextField patNameField = new TextField();
		TextField fileField = new TextField();
		TextField notesField = new TextField();
		FileChooser fileChooser = new FileChooser();
		DatePicker datePicker = new DatePicker();
		Button analyze = new Button();
		
		//Text fields set up and design
		patNameField.setMaxSize(200, 10);
		patNameField.setPromptText("Patient Name");
		
		fileField.setMaxSize(200, 10);
		fileField.setPromptText("Select File");
		
		notesField.setMaxSize(200, 10);
		notesField.setPromptText("Notes");
		
		datePicker.setPromptText("Select Date of Scan");
		
		//Analyze button Setup/Styling
		//TODO: Button function (right now just sends back to landing)
		analyze.setText("Analyze");
		
		analyze.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				// TODO: change key from landing to new page key when page is created
				manager.paintScene("landing");
			}
			
		});
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		GridPane.setConstraints(patNameField, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(fileField, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(datePicker, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesField, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(analyze, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		
		contentGrid.getChildren().addAll(patNameField, fileField, datePicker, notesField, analyze);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//File Chooser Setup
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DICOM", "*.dicom"),
                new FileChooser.ExtensionFilter("NIFTI", "*.nifti")
            );
		
		//Use File Chooser on file select
		fileField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(arg2) {
					File file = fileChooser.showOpenDialog(manager.stage);
		            if (file != null) {
		                //TODO
		            }
				}
	            datePicker.requestFocus();
			}
		});
		
		//Set up date picker
		datePicker.setMaxSize(198, 10);
		datePicker.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				//TODO: use date somewhere and remove suppression
				@SuppressWarnings("unused")
				LocalDate date = datePicker.getValue();
			}
		});
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newPatientInfoBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setStyle(BACKGROUND_COLOR);
		layout.setCenter(mainGrid);
		
		double x = manager.stage.getWidth();
		double y = manager.stage.getHeight();
		Scene scene = new Scene(layout, x, y);
		
		return scene;
	}
}