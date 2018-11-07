package ui;

import java.awt.BorderLayout;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PatientInfoEntryScene {
	final static String BACKGROUND_COLOR = "-fx-background-color: #cfd8dc";
	static boolean dateSelected = false;
	
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
		Button analyze = new Button();
		
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
		
		//Analyze button Setup/Styling
		//TODO: Button function (right now just sends back to landing)
		analyze.setText("Analyze");
		
		analyze.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				pointerGrid.getChildren().clear();
				boolean complete = true;
				
				StackPane fNameStackPane = makeRequiredSVG();
				GridPane.setConstraints(fNameStackPane, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(0, 0, 0, 0));
				pointerGrid.getChildren().add(fNameStackPane);
				if(patFNameField.getText().equals("")) {
					complete = false;
				} else {
					fNameStackPane.setVisible(false);
				}
				
				StackPane lNameStackPane = makeRequiredSVG();
				GridPane.setConstraints(lNameStackPane, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(1, 0, 4, 0));
				pointerGrid.getChildren().add(lNameStackPane);
				if(patLNameField.getText().equals("")) {
					complete = false;
				} else {
					lNameStackPane.setVisible(false);
				}
				
				//TODO implement file selection fully and add this back in
				StackPane fileStackPane = makeRequiredSVG();
				GridPane.setConstraints(fileStackPane, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(0, 0, 0, 0));
				pointerGrid.getChildren().add(fileStackPane);
				/*if(fileField.getText().equals("")) {
					complete = false;
				*///} else {
					fileStackPane.setVisible(false);
				//}
				
				StackPane dateStackPane = makeRequiredSVG();
				GridPane.setConstraints(dateStackPane, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(1, 0, 0, 0));
				pointerGrid.getChildren().add(dateStackPane);
				if(!dateSelected) {
					complete = false;
				} else {
					dateStackPane.setVisible(false);
				}
				
				if(complete) {
					manager.sceneStack.push(manager.sceneID);
					// TODO: change key from landing to new page key when page is created
					manager.paintScene("viewScan");
				}
			}
			
		});
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
				
		pointerGrid.setVgap(15);
		
		GridPane.setConstraints(patFNameField, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(patLNameField, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(fileField, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(datePicker, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesField, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(analyze, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(pointerGrid, 2, 2, 1, 4, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().addAll(patFNameField, patLNameField, fileField, datePicker, notesField, analyze, pointerGrid);
		contentGrid.setMaxHeight(10);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100.0/11);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100.0/3);
		contentGrid.getColumnConstraints().add(columnCon);
		
		rowCon = new RowConstraints();
		rowCon.setPercentHeight(100.0/4);
		pointerGrid.getRowConstraints().add(rowCon);
		columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		pointerGrid.getColumnConstraints().add(columnCon);
				
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
				dateSelected = true;
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
	
	public static StackPane makeRequiredSVG() {
		StackPane stackPane = new StackPane();
		SVGPath svg = new SVGPath();
		svg.setStroke(new Color(.949019607, .30980392157, .227450980392, 1));
		svg.setFill(new Color(.949019607, .30980392157, .227450980392, 1));
		svg.setContent("M200 5h-200v20h200l7-10z");
		svg.setRotate(180);
		svg.setStyle("-fx-background-color: #f24f3a");
		Label dialogLabel = new Label("This Field is Required");
		stackPane.getChildren().addAll(svg, dialogLabel);
		return stackPane;
	}
}