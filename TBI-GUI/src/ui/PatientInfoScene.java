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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * This page displays all the info associated with a chosen patient
 * @author Ty Chase
 */
public class PatientInfoScene {
	public static Scene initializeScene(StateManager manager, Patient patient) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		
		//Create elements
		Label firstNameLabel = new Label("First Name");
		Label lastNameLabel = new Label("Last Name");
		Label notesLabel = new Label("Notes");
		Label imagesLabel = new Label("Images");
		Label firstName = new Label(patient.getFirstName());
		Label lastName = new Label(patient.getLastName());
		Label notes = new Label(patient.getNotes());
		
		ImageView displayImg = new ImageView();
		displayImg.setImage(patient.getScans().get(0).getScan());
		
		//TODO: Decide if filepicker is good here instead of just displayign images?
		//Button filesBtn = new Button("Select File");
		
		//Button specifications
		/*
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DICOM", "*.dicom"),
                new FileChooser.ExtensionFilter("NIFTI", "*.nifti")
            );
		filesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(manager.stage);
                if (file != null) {
                    //TODO
                }
            }
        });
        */
		
		//Style elements
		firstNameLabel.setStyle("-fx-font-weight: bold");
		lastNameLabel.setStyle("-fx-font-weight: bold");
		notesLabel.setStyle("-fx-font-weight: bold");
		imagesLabel.setStyle("-fx-font-weight: bold");
		
		//Construct content grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(25);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(75);
		contentGrid.getColumnConstraints().addAll(column0, column1);
		
		//TODO: display all scans on file?
		displayImg.fitWidthProperty().bind(contentGrid.widthProperty().divide(3));
		displayImg.fitHeightProperty().bind(contentGrid.heightProperty().divide(3));
		
		//Add elements to content grid
		GridPane.setConstraints(firstNameLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(lastNameLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(imagesLabel, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(firstName, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(lastName, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(notes, 1, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(displayImg, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		contentGrid.getChildren().addAll(
				firstNameLabel, lastNameLabel, notesLabel, imagesLabel, firstName, lastName, notes, displayImg
			);
		
		//Merge content grid with left nav
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		
		//Return constructed scene
		return new Scene(layout, manager.stage.getWidth(), manager.stage.getHeight());
	}
}
