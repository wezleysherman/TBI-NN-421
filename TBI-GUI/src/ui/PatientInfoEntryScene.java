package ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;

public class PatientInfoEntryScene {
	final static String BACKGROUND_COLOR = "-fx-background-color: #cfd8dc";
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
		
		//Analyze button Setup/Styling/Tooltips
		analyze.setText("Analyze");
		
		styleButton(analyze);
		
		analyze.setOnAction(new EventHandler<ActionEvent>() {

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
				if(datePicker.getValue() == null) {
					complete = false;
					dateStackPane.setVisible(true);
				} else {
					dateStackPane.setVisible(false);
				}
				
				//Switch to proper scene
				if(complete) {
					// TODO: this sets the time to midnight, find a way to get correct date/time
					Instant instant = Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()));
					Date date = Date.from(instant);
					Patient patient = new Patient(patFNameField.getText(), patLNameField.getText(), date, notesField.getText());
					manager.getSceneStack().push(manager.getSceneID());
					manager.paintScene("viewScan", patient);
				}
			}
			
		});
		String analyzeTT = "Save patient information and analyze the selected scan (displays %chance of abuse and links to more information).";
		analyze.setTooltip(new Tooltip(analyzeTT));
				
		//Construct Grid
		contentGrid.setVgap(15);
		pointerGrid.setVgap(15);
		
		GridPane.setConstraints(patFNameField, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(patLNameField, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(fileField, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(datePicker, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesField, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(analyze, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(pointerGrid, 2, 2, 2, 4, HPos.LEFT, VPos.CENTER);
		contentGrid.getChildren().addAll(patFNameField, patLNameField, fileField, datePicker, notesField, analyze, pointerGrid);
		
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
				
		//File Chooser Setup
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("DICOM", "*.dcm"),
				new FileChooser.ExtensionFilter("NIFTI", "*.nii"),
                new FileChooser.ExtensionFilter("DICOM Full", "*.dicom"),
                new FileChooser.ExtensionFilter("NIFTI Full", "*.nifti")
            );
		
		//Use File Chooser on file select
		fileField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(arg2) {
					File file = fileChooser.showOpenDialog(manager.getStage());
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
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setStyle(BACKGROUND_COLOR);
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
	
	//Styles Buttons to make layout and style of page
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
}