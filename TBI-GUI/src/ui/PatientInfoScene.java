package ui;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * This page displays all the info associated with a chosen patient
 * @author Ty Chase
 */
public class PatientInfoScene {
	
	public static Scene initializeScene(StateManager manager) {
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
		column1.setPercentWidth(75);
		contentGrid.getColumnConstraints().addAll(column0, column1);
		
		//Add elements to content grid
		GridPane.setConstraints(firstNameLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(lastNameLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scansLabel, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		contentGrid.getChildren().addAll(
				firstNameLabel, lastNameLabel, notesLabel, scansLabel
			);
		
		
		/*if (!manager.getStateBool()) {
			//Create elements
			Label firstName = new Label(manager.getPatient().getFirstName());
			Label lastName = new Label(manager.getPatient().getLastName());
			Label notes = new Label(manager.getPatient().getNotes());
			
			ImageView displayImg = new ImageView();
			if (manager.getPatient().getNumScans() > 0) {
				displayImg.setImage(manager.getPatient().getScans().get(0).getScan());
			}
			displayImg.fitWidthProperty().bind(contentGrid.widthProperty().divide(3));
			displayImg.fitHeightProperty().bind(contentGrid.heightProperty().divide(3));
			
			//Add elements to content grid
			GridPane.setConstraints(firstName, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(lastName, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(notes, 1, 3, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(displayImg, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);
			contentGrid.getChildren().addAll(
					firstName, lastName, notes, displayImg
				);
		}
		else {
			//Create elements
			TextField firstField = new TextField(manager.getPatient().getFirstName());
			TextField lastField = new TextField(manager.getPatient().getLastName());
			TextArea notesArea = new TextArea(manager.getPatient().getNotes());
			Button fileBtn = new Button("Add Scan");
			Button saveBtn = new Button("Save");
			Button cancelBtn = new Button("Cancel");
			Style.styleButton(fileBtn);
			Style.styleButton(saveBtn);
			Style.styleButton(cancelBtn);
			fileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			cancelBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			
			saveBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	            	manager.getPatient().setFirstName(firstField.getText());
	            	manager.getPatient().setLastName(lastField.getText());
	            	manager.getPatient().setNotes(notesArea.getText());
	            	
	            	manager.setPatient(manager.getPatient());
	            	manager.setStateBool(false);
	            	manager.paintScene("PatientInfo");
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
	                new FileChooser.ExtensionFilter("DICOM", "*.dicom"),
	                new FileChooser.ExtensionFilter("NIFTI", "*.nifti")
	            );
			fileBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	                File file = fileChooser.showOpenDialog(manager.getStage());
	                if (file != null) {
	                    //TODO patient.addSacn
	                }
	            }
	        });
			
			//Add elements to content grid
			GridPane.setConstraints(firstField, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(lastField, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(notesArea, 1, 3, 1, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(fileBtn, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(saveBtn, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(cancelBtn, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
			contentGrid.getChildren().addAll(
					firstField, lastField, notesArea, fileBtn, saveBtn, cancelBtn
				);
		}*/

		//Merge content grid with left nav
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
}
