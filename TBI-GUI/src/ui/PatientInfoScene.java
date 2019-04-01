package ui;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utils.Holder;
import utils.Patient;
import utils.PatientManagement;
import utils.Scan;

/**
 * This page displays all the info associated with a chosen patient
 * @author Ty Chase
 */
public class PatientInfoScene {

	private static Patient patient = new Patient();

	public static Scene initializeScene(StateManager manager) {
		//Load Patient info from the database
		try {
			patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
		} catch (IOException e) {
			manager.makeError("Cannot load a patient. You might be using an outdated version of the database. \n"
					+ "Try deleting the resources/patients folder. WARNING, this will delete all saved patient data in the system. \n"
					+ "Check utils.PatientManagement importPatient().", e);
			manager.setPatient(null);
			manager.getSceneStack().pop();
			manager.paintScene("PreviousPatient");
		}

		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;

		//Create elements
		Label firstNameLabel = new Label("First Name");
		Label lastNameLabel = new Label("Last Name");
		Label pictureLabel = new Label("Picture");
		Label notesLabel = new Label("Notes");
		Label scansLabel = new Label("Scans");
		firstNameLabel.setStyle("-fx-font-weight: bold");
		lastNameLabel.setStyle("-fx-font-weight: bold");
		pictureLabel.setStyle("-fx-font-weight: bold");
		notesLabel.setStyle("-fx-font-weight: bold");
		scansLabel.setStyle("-fx-font-weight: bold");

		//Construct content grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setHgap(10);
		contentGrid.setVgap(10);
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(10);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(30);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(30);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(30);
		contentGrid.getColumnConstraints().addAll(column0, column1, column2, column3);

		//Add elements to content grid
		GridPane.setConstraints(firstNameLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(lastNameLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(pictureLabel, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scansLabel, 0, 5, 1, 1, HPos.LEFT, VPos.CENTER);
		contentGrid.getChildren().addAll(
				firstNameLabel, lastNameLabel, pictureLabel, notesLabel, scansLabel
				);

		//Check if edit was pressed
		if (!manager.getStateBool()) {
			//Create elements
			Label firstName = new Label(patient.getFirstName());
			Label lastName = new Label(patient.getLastName());

			ImageView picture = new ImageView();
			if(patient.getPicture() != null) {
				Image pictureImage = new Image(patient.getPicture().toURI().toString());
				double height = pictureImage.getHeight();
				double width = pictureImage.getWidth();
				width = width * 100 / height;
				height = 100;
				picture.setFitHeight(height);
				picture.setFitWidth(width);
				picture.setImage(pictureImage);			
			}

			TextArea notes = new TextArea(patient.getNotes());
			notes.setPrefHeight(300);
			notes.setEditable(false);

			//Set up scan table
			ObservableList<Scan> scanList = FXCollections.observableArrayList();
			for(Scan scan : patient.getScans()) {
				scanList.add(scan);
			}

			TableView<Scan> scanTable = new TableView<Scan>();
			scanTable.setEditable(false);

			TableColumn dateCol = new TableColumn("Date");
			dateCol.prefWidthProperty().bind(scanTable.widthProperty().multiply(.19));
			dateCol.setCellValueFactory(new PropertyValueFactory<Scan, Date>("dateOfScan"));

			TableColumn fileCol = new TableColumn("File");
			fileCol.prefWidthProperty().bind(scanTable.widthProperty().multiply(.39));
			fileCol.setCellValueFactory(new PropertyValueFactory<Scan, File>("scan"));

			TableColumn notesCol = new TableColumn("Notes");
			notesCol.prefWidthProperty().bind(scanTable.widthProperty().multiply(.39));
			notesCol.setCellValueFactory(new PropertyValueFactory<Scan, String>("notes"));

			scanTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			scanTable.getColumns().addAll(dateCol, fileCol, notesCol);

			scanTable.setItems(scanList);

			scanTable.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2) {
					if (scanTable.getSelectionModel().getSelectedItem() != null) {
						manager.setScan(scanTable.getSelectionModel().getSelectedItem());
						manager.getSceneStack().push(manager.getSceneID());
						manager.paintScene("ScanVisualizer");
					}
				}
			});

			//Analyze button
			Button analyzeBtn = new Button("Analyze");
			analyzeBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			analyzeBtn.setTooltip(new Tooltip("Analyze this scan."));
			analyzeBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					if (scanTable.getSelectionModel().getSelectedItem() != null) {
						manager.setScan(scanTable.getSelectionModel().getSelectedItem());
						manager.getSceneStack().push(manager.getSceneID());
						manager.paintScene("ScanVisualizer");
					} else {
						manager.makeDialog("No scan was selected!");
					}
				}
			});

			//Add elements to content grid
			GridPane.setConstraints(firstName, 1, 1, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(lastName, 1, 2, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(picture, 1, 3, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(notes, 1, 4, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(scanTable, 1, 5, 3, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(analyzeBtn, 1, 6, 3, 1, HPos.CENTER, VPos.CENTER);
			contentGrid.getChildren().addAll(
					firstName, lastName, picture, notes, scanTable, analyzeBtn
					);
		}

		//Edit was pressed
		else {
			//Create elements
			scansLabel.setVisible(false);
			TextField firstField = new TextField(patient.getFirstName());
			TextField lastField = new TextField(patient.getLastName());
			TextField pictureField = new TextField("Select Picture");
			TextArea notesArea = new TextArea(patient.getNotes());
			notesArea.setPrefHeight(300);
			Button saveBtn = new Button("Save");
			Button cancelBtn = new Button("Cancel");

			Holder holder = new Holder();
			LinkedList<File> newFiles = new LinkedList<File>();

			saveBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent ev) {	            	
					try {
						//Date needs a Scan
						if (holder.getDate() != null && newFiles.size() == 0) {
							manager.makeDialog("Please select a file for the new scan.");
						}
						else {
							patient.setFirstName(firstField.getText());
							patient.setLastName(lastField.getText());
							patient.setNotes(notesArea.getText());
							if (holder.getFile() != null) {
								patient.setPicture(holder.getFile());
							}
							patient.savePatient();
							manager.setStateBool(false);
							manager.paintScene("PatientInfo");
						}
					} catch (Exception e) {
						manager.makeError("Edit operation failed. Voiding changes. There is an issue with the file structure of the database. \n"
								+ "Check utils.PatientManagement exportPatient().", e);
					}
				}
			});

			cancelBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					manager.setStateBool(false);
					manager.paintScene("PatientInfo");
				}
			});

			FileChooser pictureChooser = new FileChooser();
			pictureChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("IMAGE", "*.png", "*.jpg"));
			pictureField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
					if(arg2) {
						File picture = pictureChooser.showOpenDialog(manager.getStage());
						if (picture != null) {
							pictureField.setText(picture.getName());
							holder.setFile(picture);
						}
					}
					cancelBtn.requestFocus();
				}
			});

			//Add elements to content grid
			GridPane.setConstraints(firstField, 1, 1, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(lastField, 1, 2, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(pictureField, 1, 3, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(notesArea, 1, 4, 3, 1, HPos.LEFT, VPos.CENTER);
			GridPane.setConstraints(saveBtn, 1, 5, 3, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(cancelBtn, 1, 6, 3, 1, HPos.CENTER, VPos.CENTER);
			contentGrid.getChildren().addAll(
					firstField, lastField, pictureField, notesArea, saveBtn, cancelBtn
					);
		}

		//Merge content grid with left nav
		contentGrid.getStyleClass().add("content-pane");
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		layout.setTop(TopMenuBar.newMenuBar(manager));

		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
}
