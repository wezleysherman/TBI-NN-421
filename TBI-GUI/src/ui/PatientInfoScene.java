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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 * This page displays all the info associated with a chosen patient
 * @author Ty Chase
 */
public class PatientInfoScene {
	
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
	
	public static Scene initializeScene(StateManager manager, Patient patient, boolean edit) {
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
		
		
		if (!edit) {
			//Create elements
			Label firstName = new Label(patient.getFirstName());
			Label lastName = new Label(patient.getLastName());
			Label notes = new Label(patient.getNotes());
			
			ImageView displayImg = new ImageView();
			if (patient.getNumScans() > 0) {
				displayImg.setImage(patient.getScans().get(0).getScan());
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
			TextField firstField = new TextField(patient.getFirstName());
			TextField lastField = new TextField(patient.getLastName());
			TextArea notesArea = new TextArea(patient.getNotes());
			Button fileBtn = new Button("Add Scan");
			Button saveBtn = new Button("Save");
			Button cancelBtn = new Button("Cancel");
			styleButton(fileBtn);
			styleButton(saveBtn);
			styleButton(cancelBtn);
			fileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			cancelBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			
			saveBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	            	patient.setFirstName(firstField.getText());
	            	patient.setLastName(lastField.getText());
	            	patient.setNotes(notesArea.getText());
	            	
	            	manager.paintScene("patInfo", patient);
	            }
	        });
			
			cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(final ActionEvent e) {
	            	manager.paintScene("patInfo", false);
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
