package ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import utils.Patient;
import utils.PatientManagement;
import utils.Scan;

public class PatientInfoEntryScene {
	static boolean analyzeFailed = false;
	
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		GridPane pointerGrid = new GridPane();
		TextField patFNameField = new TextField();
		TextField patLNameField = new TextField();
		TextField fileField = new TextField();
		TextField notesField = new TextField();
		FileChooser fileChooser = new FileChooser();
		DatePicker datePicker = new DatePicker();
		Button finishBtn = new Button("Finish");
		
		//Text fields set up and design
		patFNameField.setMaxSize(200, 10);
		patFNameField.setPromptText("Patient First Name");
		
		patLNameField.setMaxSize(200, 10);
		patLNameField.setPromptText("Patient Last Name");
		
		fileField.setMaxSize(200, 10);
		fileField.setPromptText("Select File");
		
		notesField.setMaxSize(200, 10);
		notesField.setPromptText("Notes");
		
		datePicker.setPromptText("Select Date of Scan");
		
		//Add required indicator tags
		StackPane fNameStackPane = makeRequiredSVG();
		GridPane.setConstraints(fNameStackPane, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER);
		pointerGrid.getChildren().add(fNameStackPane);
		
		StackPane lNameStackPane = makeRequiredSVG();
		GridPane.setConstraints(lNameStackPane, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		pointerGrid.getChildren().add(lNameStackPane);
		
		StackPane fileStackPane = makeRequiredSVG();
		GridPane.setConstraints(fileStackPane, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		pointerGrid.getChildren().add(fileStackPane);
		
		StackPane dateStackPane = makeRequiredSVG();
		GridPane.setConstraints(dateStackPane, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		pointerGrid.getChildren().add(dateStackPane);
		
		fNameStackPane.setVisible(false);
		lNameStackPane.setVisible(false);
		fileStackPane.setVisible(false);
		dateStackPane.setVisible(false);
		
		//Construct Grid
		contentGrid.setVgap(15);
		pointerGrid.setVgap(15);
		
		GridPane.setConstraints(patFNameField, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(patLNameField, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(fileField, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(datePicker, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesField, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(finishBtn, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(pointerGrid, 2, 2, 2, 4, HPos.LEFT, VPos.CENTER);
		contentGrid.getChildren().addAll(patFNameField, patLNameField, fileField, datePicker, notesField, finishBtn, pointerGrid);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100.0/11);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100.0/3);
		ColumnConstraints columnCon2 = new ColumnConstraints();
		contentGrid.getColumnConstraints().addAll(columnCon, columnCon2, columnCon2);
		
		rowCon = new RowConstraints();
		rowCon.setPercentHeight(100.0);
		pointerGrid.getRowConstraints().addAll(rowCon, rowCon, rowCon, rowCon);
		columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		pointerGrid.getColumnConstraints().add(columnCon);
				
		Scan newScan = new Scan();
		
		//File Chooser Setup
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("NIFTI", "*.nii", "*.nifti", "*.txt")); //TODO remove .txt
		fileField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(arg2) {
					File file = fileChooser.showOpenDialog(manager.getStage());
		            if (file != null) {
		            	fileField.setText(file.getName());
		            	newScan.setScan(file);
		            }
				}
	            datePicker.requestFocus();
			}
		});
		
		//Date Picker Setup
		datePicker.setMaxSize(198, 10);
		datePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newScan.setDateOfScan(java.sql.Date.valueOf(datePicker.getValue()));;
			}
		});
		
		//Finish Button Setup
		finishBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		finishBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				boolean complete = true;
				if(patFNameField.getText().equals("")) {
					complete = false;
					fNameStackPane.setVisible(true);
				} else {
					fNameStackPane.setVisible(false);
				}	
				if(patLNameField.getText().equals("")) {
					complete = false;
					lNameStackPane.setVisible(true);
				} else {
					lNameStackPane.setVisible(false);
				}
				
				//File needs date or date needs file
				if (newScan.getDateOfScan() != null && newScan.getScan() == null) {
					complete = false;
					fileStackPane.setVisible(true);
				} else {
					fileStackPane.setVisible(false);
				}
				
				if (newScan.getDateOfScan() == null && newScan.getScan() != null) {
					complete = false;
					dateStackPane.setVisible(true);
				} else {
					dateStackPane.setVisible(false);
				}
				
				//Switch to proper scene
				if(complete) {
					Date dateCreated = java.sql.Date.valueOf(LocalDate.now()); //get current date
					Patient patient = new Patient(patFNameField.getText(), patLNameField.getText(), dateCreated, notesField.getText());
					if (newScan.getDateOfScan() != null && newScan.getScan() != null) {
						patient.addRawScan(newScan);
					}
					try {
						PatientManagement.exportPatient(patient);
					} catch (IOException e) {
						manager.makeError("Creating new patient failed. There is an issue with the file structure of the database. Check utils.PatientManagement exportPatient().", e);
					}
					manager.getSceneStack().push(manager.getSceneID());
					manager.paintScene("PreviousPatient");
				}
			}
			
		});
		finishBtn.setTooltip(new Tooltip("Create a new patient in the system."));
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.getStyleClass().add("content-pane");
		layout.setCenter(mainGrid);
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
	
	//Construct SVG image for pointing to empty elements
	public static StackPane makeRequiredSVG() {
		StackPane stackPane = new StackPane();
		SVGPath svg = new SVGPath();
		svg.setStroke(new Color(.949019607, .30980392157, .227450980392, 1));
		svg.setFill(new Color(.949019607, .30980392157, .227450980392, 1));
		svg.setContent("M200 5h-150v20h150l7-10z");
		svg.setRotate(180);
		svg.setStyle("-fx-background-color: #f24f3a");
		Label dialogLabel = new Label("This Field is Required");
		stackPane.getChildren().addAll(svg, dialogLabel);
		return stackPane;
	}
}