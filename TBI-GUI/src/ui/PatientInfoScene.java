package ui;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utils.Patient;
import utils.PatientManagement;
import utils.Scan;

/**
 * This page displays all the info associated with a chosen patient
 * @author Ty Chase
 */
public class PatientInfoScene {
	
	public static Scene initializeScene(StateManager manager) throws IOException {
		//Load Patient info from the database
		Patient patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
		
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		
		//Create elements
		Label firstNameLabel = new Label("First Name");
		Label lastNameLabel = new Label("Last Name");
		Label notesLabel = new Label("Notes");
		Label scansLabel = new Label("Scans");
		firstNameLabel.setStyle("-fx-font-weight: bold");
		lastNameLabel.setStyle("-fx-font-weight: bold");
		notesLabel.setStyle("-fx-font-weight: bold");
		scansLabel.setStyle("-fx-font-weight: bold");
		
		//Construct content grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(25);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(25);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(25);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(25);
		contentGrid.getColumnConstraints().addAll(column0, column1, column2, column3);
		
		//Add elements to content grid
		GridPane.setConstraints(firstNameLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(lastNameLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scansLabel, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		contentGrid.getChildren().addAll(
				firstNameLabel, lastNameLabel, notesLabel, scansLabel
			);
		
		//Check if edit was pressed
		if (!manager.getStateBool()) {
			//Create elements
			Label firstName = new Label(patient.getFirstName());
			Label lastName = new Label(patient.getLastName());
			Label notes = new Label(patient.getNotes());
			
			GridPane scrollGrid = new GridPane();
			ScrollPane scrollPane = new ScrollPane(scrollGrid);
			Style.styleScrollPane(scrollPane);
			ColumnConstraints scrollGridCols = new ColumnConstraints();
			scrollGridCols.setPercentWidth(100);
			scrollGrid.getColumnConstraints().add(scrollGridCols);
			scrollGrid.prefWidthProperty().bind(scrollPane.widthProperty());
			
			for (int i = 0; i < patient.getNumScans(); ++i) {
				Button scanBtn = new Button("Scan " + (i+1));
				Style.styleButton(scanBtn);
				scanBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						//i is out of scope, so get scan num from button text
						int scanNum = Character.getNumericValue(scanBtn.getText().charAt(scanBtn.getText().length()-1))-1;
						manager.setScan(patient.getScans().get(scanNum));
						manager.getSceneStack().push(manager.getSceneID());
						manager.paintScene("ScanVisualizer");
					}
				});
				
				GridPane.setConstraints(scanBtn, 0, i, 2, 1, HPos.CENTER, VPos.CENTER);
				scrollGrid.getChildren().add(scanBtn);
			}
			
			//Add elements to content grid
			GridPane.setConstraints(firstName, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(lastName, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(notes, 1, 3, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(scrollPane, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);
			contentGrid.getChildren().addAll(
					firstName, lastName, notes, scrollPane
				);
		}
		else {
			//Create elements
			TextField firstField = new TextField(patient.getFirstName());
			TextField lastField = new TextField(patient.getLastName());
			TextArea notesArea = new TextArea(patient.getNotes());
			Label scanLabel = new Label("Add a New Scan:");
			Button fileBtn = new Button("Select File");
			DatePicker datePicker = new DatePicker();
			datePicker.setPromptText("Date of Scan");
			Button saveBtn = new Button("Save");
			Button cancelBtn = new Button("Cancel");
			Style.styleButton(fileBtn);
			Style.styleButton(saveBtn);
			Style.styleButton(cancelBtn);
			datePicker.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			Scan newScan = new Scan();
			
			saveBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {	            	
	            	try {
	            		if (newScan.getDateOfScan() == null && newScan.getScan() != null) {
    	            		manager.makeDialog("Please select a date for the new scan.");
    	            	}
    	            	else if (newScan.getDateOfScan() != null && newScan.getScan() == null){
    	            		manager.makeDialog("Please select a file for the new scan.");
    	            	}
    	            	else {
    	            		patient.setFirstName(firstField.getText());
        	            	patient.setLastName(lastField.getText());
        	            	patient.setNotes(notesArea.getText());
    	            		if (newScan.getDateOfScan() != null && newScan.getScan() != null) {
    	            			patient.addScan(newScan);
    	            		}
    	            		patient.savePatient();
    	            		manager.setStateBool(false);
        	            	manager.paintScene("PatientInfo");
    	            	}
	            	} catch (Exception ex) {
	            		manager.makeDialog("Edit operation failed. Voiding changes.");
	            	}
	            }
	        });
			
			cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	            	manager.setStateBool(false);
	            	manager.paintScene("PatientInfo");
	            }
	        });
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("NIFTI", "*.nii"),
	                new FileChooser.ExtensionFilter("NIFTI Full", "*.nifti")
	            );
			fileBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	                File file = fileChooser.showOpenDialog(manager.getStage());
	                if (file != null) {
	                	newScan.setScan(file);
	                }
	            }
	        });
			
			datePicker.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					newScan.setDateOfScan(java.sql.Date.valueOf(datePicker.getValue()));;
				}
			});
			
			//Add elements to content grid
			GridPane.setConstraints(firstField, 1, 1, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(lastField, 1, 2, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(notesArea, 1, 3, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(scanLabel, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(fileBtn, 2, 4, 1, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(datePicker, 3, 4, 1, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(saveBtn, 1, 5, 3, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(cancelBtn, 1, 6, 3, 1, HPos.CENTER, VPos.CENTER);
			contentGrid.getChildren().addAll(
					firstField, lastField, notesArea, scanLabel, fileBtn, datePicker, saveBtn, cancelBtn
				);
		}

		//Merge content grid with left nav
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
}
