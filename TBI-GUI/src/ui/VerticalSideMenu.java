package ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import utils.Holder;
import utils.Patient;
import utils.PatientManagement;
import utils.Scan;

public class VerticalSideMenu {
		
	private static Patient patient = new Patient();
	
	public static GridPane newSideBar(StateManager manager) {
		GridPane mainGrid = new GridPane();
		GridPane contentGrid = new GridPane();
		Label appLabel = new Label("TBI Application");
		appLabel.getStyleClass().add("label-white");
		Pane colorPane = new Pane();
		
		//Construct content grid
		contentGrid.setHgap(5);
		contentGrid.setVgap(5);
		contentGrid.setPadding(new Insets(5, 5, 5, 5));
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(50);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(50);
		contentGrid.getColumnConstraints().addAll(column0, column1);
		
		//Add elements to content grid
		GridPane.setConstraints(appLabel, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(appLabel);
		
		//Construct main grid
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		mainGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100/5);
		mainGrid.getColumnConstraints().add(0, columnCon);
		ColumnConstraints columnCon2 = new ColumnConstraints();
		columnCon2.setPercentWidth(400/5);
		mainGrid.getColumnConstraints().add(1, columnCon2);
		
		//Merge content grid with main grid
		colorPane.getStyleClass().add("side-pane");
		GridPane.setConstraints(colorPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(contentGrid, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		mainGrid.getChildren().addAll(colorPane, contentGrid);
		
		//backBtn------------------------------------------------------------------------------------------------------------------------------------
		Button backBtn = new Button("Back");
		backBtn.setMaxWidth(Double.MAX_VALUE);
		backBtn.setTooltip(new Tooltip("Return to the previous page (You will lose any information you input on this page)."));
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					manager.paintScene(manager.getSceneStack().pop());
				}
				catch (EmptyStackException e) {
					manager.paintScene("Landing");
					manager.makeError("Back operation failed. sceneStack in ui.StateManager is empty and cannot be popped.", e);
				}
			}
		});
		GridPane.setConstraints(backBtn, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(backBtn);
		
		//homeBtn------------------------------------------------------------------------------------------------------------------------------------
		Button homeBtn = new Button("Home");
		homeBtn.setMaxWidth(Double.MAX_VALUE);
		homeBtn.setTooltip(new Tooltip("Return to the home page (You will lose any unsaved information from this run of the program)."));
		
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				boolean yes = manager.makeQuestion("Returning to the home page will cause you to lose all unsaved information. Are you sure?");
				if (yes) {
					manager.getSceneStack().clear();
					manager.paintScene("Landing");
				}
			}
		});
		GridPane.setConstraints(homeBtn,  1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(homeBtn);
		
		//Side bar for different pages---------------------------------------------------------------------------------------------------------------
		if (manager.getSceneID().equals("PatientInfo")) {
			makePI(contentGrid, manager);
		}
		else if (manager.getSceneID().equals("ScanVisualizer")) {
			makeSV(contentGrid, manager);
		}
		return mainGrid;
	}
	
	//Side bar constructions-------------------------------------------------------------------------------------------------------------------------
	
	//Add Patient Information Elements to the Main Grid
	private static void makePI(GridPane grid, StateManager manager) {
		Label sceneLabel = new Label("Patient Info");
		sceneLabel.getStyleClass().add("label-white");
		Button editBtn = new Button("Edit Patient");
		editBtn.setTooltip(new Tooltip("Edit this patient's data."));
		Button delPatBtn = new Button("Delete Patient");
		delPatBtn.setMaxWidth(Double.MAX_VALUE);
		delPatBtn.setTooltip(new Tooltip("Delete this patient."));
		Button delScansBtn = new Button("Delete All Scans");
		delScansBtn.setMaxWidth(Double.MAX_VALUE);
		delScansBtn.setTooltip(new Tooltip("Delete this patient's scans."));
		
		editBtn.setMaxWidth(Double.MAX_VALUE);
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.setStateBool(true);
				manager.paintScene("PatientInfo");
			}
		});
		
		delPatBtn.setMaxWidth(Double.MAX_VALUE);
		delPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				boolean yes = manager.makeQuestion("This will remove all of the data associated with this patient, are you sure you want to continue?");
				if (yes) {
					try {
						PatientManagement.deletePatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
						manager.setPatient(null);
						manager.getSceneStack().pop();
						manager.paintScene("PreviousPatient");
					} catch (IOException e) {
						manager.makeError("Deleting patient failed. There is an issue with the file structure of the database. \n"
								+ "Check utils.PatientManagement deletePatient().", e);
					}
				}
			}
		});
		
		delScansBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				boolean yes = manager.makeQuestion("This will remove all of the scans associated with this patient, are you sure you want to continue?");
				if (yes) {
					try {
						Patient patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
						patient.getScans().clear();
						patient.savePatient();
						manager.paintScene("PatientInfo");
					} catch (Exception e) {
						manager.makeError("Delete scans failed. There is an issue with the file structure of the database. \n"
								+ "Check utils.PatientManagement exportPatient().", e);
					}
				}
			}
		});
		
		//Add Scan to the current patient
		TextField fileField = new TextField();
		StateManager.textMaxLength(fileField, 50);
		fileField.setEditable(false);
		LinkedList<File> newFiles = new LinkedList<File>();
		Holder holder = new Holder();
		DatePicker datePicker = new DatePicker();
		Button addScanBtn = new Button("Add Scan");
		Button saveScanButton = new Button("Save and Analyze Scans");
		Button cancelAddScan = new Button("Cancel");
		
		addScanBtn.setMaxWidth(Double.MAX_VALUE);
		saveScanButton.setMaxWidth(Double.MAX_VALUE);
		cancelAddScan.setMaxWidth(Double.MAX_VALUE);
		datePicker.setMaxWidth(Double.MAX_VALUE);
		
		saveScanButton.setVisible(false);
		cancelAddScan.setVisible(false);
		datePicker.setVisible(false);
		fileField.setVisible(false);
		
		datePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				holder.setDate(java.sql.Date.valueOf(datePicker.getValue()));
			}
		});
		
		cancelAddScan.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				fileField.setText("");
				newFiles.clear();
				saveScanButton.setVisible(false);
				cancelAddScan.setVisible(false);
				datePicker.setVisible(false);
				fileField.setVisible(false);
			}
			
		});
		
		addScanBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SCAN", "*.nii", "*.nifti", "*.png"));

				List<File> files = fileChooser.showOpenMultipleDialog(manager.getStage());
				if (files != null && files.size() > 0) {
					saveScanButton.setVisible(true);
					cancelAddScan.setVisible(true);
					datePicker.setVisible(true);
					fileField.setVisible(true);
					if (files.size() == 1) {
						fileField.setText(files.get(0).getName());
					}
					else {
						fileField.setText(files.size() + " files");
					}
					for (int i = 0; i < files.size(); ++i) {
						newFiles.add(files.get(i));
					}
				}					
			}

		});
		
		saveScanButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Patient patient = null;
				try {
					patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (newFiles.size() > 0) {
					if (holder.getDate() == null) {
						manager.makeDialog("No date was selected for the scan(s). Today's date will be used.");
						holder.setDate(java.sql.Date.valueOf(LocalDate.now()));
					}
					for (int i = 0; i < newFiles.size(); ++i) {
						patient.addScan(new Scan(holder.getDate(), newFiles.get(i)));
						try {
							PatientManagement.exportPatient(patient);
						} catch (IOException e) {
							manager.makeError("Creating new patient failed. There is an issue with the file structure of the database. \n"
									+ "Check utils.PatientManagement exportPatient().", e);
						}
					}
				}
				manager.paintScene("PatientInfo");
			}
		});
		
		GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(editBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(delPatBtn, 0, 7, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(delScansBtn, 0, 8, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(addScanBtn, 0, 9, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(fileField, 0, 10, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(datePicker, 0, 11, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(saveScanButton, 0, 12, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(cancelAddScan, 0, 13, 2, 1, HPos.CENTER, VPos.CENTER);
		
		grid.getChildren().addAll(sceneLabel, editBtn, delPatBtn, delScansBtn, fileField, datePicker, addScanBtn, saveScanButton, cancelAddScan);
	}
	
	//Add Scan Visualizer Elements to the Main Grid
	private static void makeSV(GridPane grid, StateManager manager) {
		try {
			patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
		} catch (IOException e) {
			manager.makeError("Cannot load a patient. You might be using an outdated version of the database. \n"
	        		+ "Try deleting the resources/patients folder. WARNING, this will delete all saved patient data in the system. \n"
	        		+ "Check utils.PatientManagement importPatient().", e);
		}
		
		Label sceneLabel = new Label("Scan Vizualizer");
		sceneLabel.getStyleClass().add("label-white");
		
		Label patientLabel = new Label("Patient: " + patient.getFirstName() + " " + patient.getLastName());
		patientLabel.getStyleClass().add("label-white");
		
		Label dateLabel = new Label("Scan Date: " + manager.getScan().getDateOfScan().toString());
		dateLabel.getStyleClass().add("label-white");
		
		Label scansLabel = new Label("Scans: ");
		scansLabel.getStyleClass().add("label-white");
		
		Label notesLabel = new Label("Scan Notes: ");
		notesLabel.getStyleClass().add("label-white");
		
		Button delScanBtn = new Button("Delete Current Scan");
		delScanBtn.setMaxWidth(Double.MAX_VALUE);
		delScanBtn.setTooltip(new Tooltip("Delete this scan."));
		delScanBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (manager.makeQuestion("This will remove all of the data associated with this scan, are you sure you want to continue?")) {
					for(Scan scan : patient.getScans()) {
						if (scan.getRawScan().equals(manager.getScan().getRawScan())) {
			        		patient.getScans().remove(scan);
			        	}			        	
			        }
					try {
						patient.savePatient();
						manager.setScan(null);
						manager.getSceneStack().pop();
						manager.paintScene("PatientInfo");
					} catch (Exception e) {
						manager.makeError("Delete scan failed. There is an issue with the file structure of the database. \n"
								+ "Check utils.PatientManagement exportPatient().", e);
					}
				}
			}
		});
		
		GridPane scrollGrid = new GridPane();
		ScrollPane scrollPane = new ScrollPane(scrollGrid);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.getStyleClass().add("scroll-pane-side");
		scrollGrid.getStyleClass().add("side-pane");
		ColumnConstraints scrollGridCols = new ColumnConstraints();
		scrollGridCols.setPercentWidth(100);
		scrollGrid.getColumnConstraints().add(scrollGridCols);
		scrollGrid.prefWidthProperty().bind(scrollPane.widthProperty());
		
		for (int i = 0; i < patient.getNumScans(); ++i) {
			Button scanBtn = new Button(patient.getScans().get(i).getRawScan().getName() + " Scan " + (i + 1));
			scanBtn.setMaxWidth(Double.MAX_VALUE);
			scanBtn.setTooltip(new Tooltip("View this scan."));
			scanBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					int index = Character.getNumericValue(scanBtn.getText().charAt(scanBtn.getText().length() - 1));
					manager.setScan(patient.getScans().get(index - 1));
					manager.paintScene("ScanVisualizer");
					//TODO See about only redrawing elements that need to be updated.
				}
			});
			
			GridPane.setMargin(scanBtn, new Insets(0, 10, 10, 0));
			GridPane.setConstraints(scanBtn, 0, i, 2, 1, HPos.CENTER, VPos.CENTER);
			scrollGrid.getChildren().add(scanBtn);	
		}
		
		RowConstraints rowCon = new RowConstraints();
		RowConstraints scrollCon = new RowConstraints();
		scrollCon.setPercentHeight(25);
		grid.getRowConstraints().addAll(rowCon, rowCon, rowCon, rowCon, rowCon, rowCon, rowCon, scrollCon, rowCon, scrollCon, rowCon);
		
		//Set up text area
		Button cancelEditNotesBtn = new Button("Cancel Changes");
		Button editNotesBtn = new Button("Save Changes");

		TextArea docNotesField = new TextArea();
		StateManager.textMaxLength(docNotesField, 256);

		docNotesField.setText(manager.getScan().getNotes());
		docNotesField.setWrapText(true);
		docNotesField.getStyleClass().add("text-area-sidebar");
		docNotesField.textProperty().addListener((observable, oldVal, newVal) -> {
			cancelEditNotesBtn.setVisible(true);
			editNotesBtn.setVisible(true);
		});
		
		cancelEditNotesBtn.setMaxWidth(Double.MAX_VALUE);
		cancelEditNotesBtn.setTooltip(new Tooltip("Cancel changes to the notes."));
		cancelEditNotesBtn.setVisible(false);
		cancelEditNotesBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				docNotesField.setText(manager.getScan().getNotes());
				editNotesBtn.setVisible(false);
				cancelEditNotesBtn.setVisible(false);
			}
			
		});
		editNotesBtn.setMaxWidth(Double.MAX_VALUE);
		editNotesBtn.setTooltip(new Tooltip("Edit this scan's notes."));
		editNotesBtn.setVisible(false);
		editNotesBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					Scan scan= manager.getScan();
					scan.setNotes(docNotesField.getText());

					for(Scan patScan : patient.getScans()) {
						if (patScan.getRawScan().equals(scan.getRawScan())) {
							patScan.setDateOfScan(scan.getDateOfScan());
							patScan.setNotes(scan.getNotes());
						}			        	
					}

					patient.savePatient();
					manager.setStateBool(false);
					manager.paintScene("ScanVisualizer");
					editNotesBtn.setVisible(false);
					cancelEditNotesBtn.setVisible(false);
				} catch (Exception e) {
					manager.makeError("Edit operation failed. Voiding changes. There is an issue with the file structure of the database. \n"
							+ "Check utils.PatientManagement exportPatient().", e);
				}
			}
		});
		
		GridPane.setConstraints(sceneLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(patientLabel, 0, 4, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(dateLabel, 0, 5, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scansLabel, 0, 6, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scrollPane, 0, 7, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 8, 2, 1, HPos.LEFT, VPos.CENTER);		
		GridPane.setConstraints(docNotesField, 0, 9, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(editNotesBtn, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(cancelEditNotesBtn, 1, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(delScanBtn, 0, 11, 2, 1, HPos.CENTER, VPos.CENTER);
		
		grid.getChildren().addAll(sceneLabel, delScanBtn, patientLabel, dateLabel, scrollPane, docNotesField, scansLabel, notesLabel, editNotesBtn, cancelEditNotesBtn);
	}
}