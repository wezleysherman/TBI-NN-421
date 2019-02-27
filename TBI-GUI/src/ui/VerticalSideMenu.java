package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
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
		
		//algoVisBtn---------------------------------------------------------------------------------------------------------------------------------
		Button algoVisBtn = new Button ("Algorithm Visualizer");
		algoVisBtn.setMaxWidth(Double.MAX_VALUE);
		algoVisBtn.setTooltip(new Tooltip("View the accuracy of the algorithm as a whole."));
		algoVisBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!manager.getSceneID().equals("AlgorithmVisualizer")) {
					manager.getSceneStack().push(manager.getSceneID());
				}
				else if (manager.getSceneID().equals("AlgorithmVisualizer")) {
					manager.getSceneStack().pop();
				}
				manager.setStateBool(false);
				manager.paintScene("AlgorithmVisualizer");
			}
		});
		GridPane.setConstraints(algoVisBtn, 0, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(algoVisBtn);
		
		//Side bar for different pages---------------------------------------------------------------------------------------------------------------
		if (manager.getSceneID().equals("CNNVisualizer")) {
			makeCNN(contentGrid , manager);
		}
		else if (manager.getSceneID().equals("AlgorithmVisualizer")) {
			makeAV(contentGrid, manager);
		}
		else if (manager.getSceneID().equals("PatientInfo")) {
			makePI(contentGrid, manager);
		}
		else if (manager.getSceneID().equals("ScanVisualizer")) {
			makeSV(contentGrid, manager);
		}
		return mainGrid;
	}
	
	//Side bar constructions-------------------------------------------------------------------------------------------------------------------------
	
	//Add CNN Elements to the main grid
	private static void makeCNN(GridPane grid, StateManager manager) {
		try {
			patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
		} catch (IOException e) {
			manager.makeError("Cannot load a patient. You might be using an outdated version of the database. Try deleting the resources/patients folder. "
					+ "WARNING, this will delete all saved patient data in the system. Check utils.PatientManagement importPatient().", e);
		}
		
		Label sceneLabel = new Label("CNN Visualizer");
		sceneLabel.getStyleClass().add("label-white");
		
		Label patientLabel = new Label("Patient: " + patient.getFirstName() + " " + patient.getLastName());
		patientLabel.getStyleClass().add("label-white");
		
		Label dateLabel = new Label("Scan Date: " + manager.getScan().getDateOfScan().toString());
		dateLabel.getStyleClass().add("label-white");
		
		Label layersLabel = new Label("Layers: ");
		layersLabel.getStyleClass().add("label-white");
		
		Label notesLabel = new Label("Notes: ");
		notesLabel.getStyleClass().add("label-white");
		
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
		
		RowConstraints rowCon = new RowConstraints();
		RowConstraints scrollCon = new RowConstraints();
		scrollCon.setPercentHeight(25);
		grid.getRowConstraints().addAll(rowCon, rowCon, rowCon, rowCon, rowCon, rowCon, rowCon, scrollCon, rowCon, scrollCon, rowCon);
		
		//Set up text area
		TextArea docNotesField = new TextArea();
		docNotesField.setText(patient.getNotes());
		docNotesField.setWrapText(true);
		docNotesField.getStyleClass().add("text-area-sidebar");
		
		GridPane.setConstraints(sceneLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(patientLabel, 0, 4, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(dateLabel, 0, 5, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(layersLabel, 0, 6, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scrollPane, 0, 7, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 8, 2, 1, HPos.LEFT, VPos.CENTER);		
		GridPane.setConstraints(docNotesField, 0, 9, 2, 1, HPos.CENTER, VPos.CENTER);
		
		grid.getChildren().addAll(sceneLabel, patientLabel, dateLabel, scrollPane, docNotesField, layersLabel, notesLabel);

		//Dummy Data for testing purposes
		ArrayList<String> layerList = new ArrayList<String>();
		for(int i = 0; i < 10; i++) {
			layerList.add("Layer " + i);
		}
		
		for(int j = 0; j < layerList.size(); j++) {
			//Place Holder -> Can be changed to panes for better look
			Button layerButton = new Button();
			layerButton.setMaxWidth(Double.MAX_VALUE);
			layerButton.setText((String) layerList.get(j));
			GridPane.setMargin(layerButton, new Insets(0, 0, 5, 0));
			GridPane.setConstraints(layerButton, 0, j, 2, 1, HPos.LEFT, VPos.CENTER);
			scrollGrid.getChildren().add(layerButton);
		}
	}
	
	//Add Algorithm Visualizer Elements to the Main Grid
	private static void makeAV(GridPane grid, StateManager manager) {
		Label sceneLabel = new Label("Algorithm Visualizer");
		sceneLabel.getStyleClass().add("label-white");
		GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		Button recentBtn = new Button("Last 100 Scans");
		recentBtn.setTooltip(new Tooltip("View the accuracy of the algorithm in its last 100 uses."));
		GridPane.setConstraints(recentBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
		
		recentBtn.setMaxWidth(Double.MAX_VALUE);
		recentBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!manager.getSceneStack().peek().equals("algoVis")) {
					manager.getSceneStack().push("algoVis");
				}
				manager.setStateBool(true);
				manager.paintScene("AlgorithmVisualizer");
			}
		});
		
		grid.getChildren().addAll(sceneLabel, recentBtn);
	}
	
	//Add Patient Information Elements to the Main Grid
	private static void makePI(GridPane grid, StateManager manager) {
		Label sceneLabel = new Label("Patient Info");
		sceneLabel.getStyleClass().add("label-white");
		GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		Button editBtn = new Button("Edit Patient");
		editBtn.setTooltip(new Tooltip("Edit this patient's data."));
		GridPane.setConstraints(editBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
		Button delPatBtn = new Button("Delete Patient");
		delPatBtn.setMaxWidth(Double.MAX_VALUE);
		delPatBtn.setTooltip(new Tooltip("Delete this patient."));
		GridPane.setConstraints(delPatBtn, 0, 7, 2, 1, HPos.CENTER, VPos.CENTER);
		Button delScansBtn = new Button("Delete All Scans");
		delScansBtn.setMaxWidth(Double.MAX_VALUE);
		delScansBtn.setTooltip(new Tooltip("Delete this patient's scans."));
		GridPane.setConstraints(delScansBtn, 0, 8, 2, 1, HPos.CENTER, VPos.CENTER);
		
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
						patient.getRawScans().clear();
						patient.savePatient();
						manager.paintScene("PatientInfo");
					} catch (Exception e) {
						manager.makeError("Delete scans failed. There is an issue with the file structure of the database. \n"
								+ "Check utils.PatientManagement exportPatient().", e);
					}
				}
			}
		});
		
		grid.getChildren().addAll(sceneLabel, editBtn, delPatBtn, delScansBtn);
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
		
		Button editScanBtn = new Button("Edit This Scan");
		editScanBtn.setMaxWidth(Double.MAX_VALUE);
		editScanBtn.setTooltip(new Tooltip("Edit this scan's information."));
		editScanBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.setStateBool(true);
				manager.paintScene("ScanVisualizer");
			}
		});
		
		Button delScanBtn = new Button("Delete Current Scan");
		delScanBtn.setMaxWidth(Double.MAX_VALUE);
		delScanBtn.setTooltip(new Tooltip("Delete this scan."));
		delScanBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (manager.makeQuestion("This will remove all of the data associated with this scan, are you sure you want to continue?")) {
					for(Scan scan : patient.getRawScans()) {
						if (scan.getScan().equals(manager.getScan().getScan())) {
			        		patient.getRawScans().remove(scan);
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
		
		for (int i = 0; i < patient.getNumRawScans(); ++i) {
			Button scanBtn = new Button(patient.getRawScans().get(i).getDateOfScan().toString());
			scanBtn.setMaxWidth(Double.MAX_VALUE);
			scanBtn.setTooltip(new Tooltip("View this scan."));
			
			scanBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					// TODO: Implement this
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
		TextArea docNotesField = new TextArea();
		docNotesField.setText(manager.getScan().getNotes());
		docNotesField.setWrapText(true);
		docNotesField.getStyleClass().add("text-area-sidebar");
		
		GridPane.setConstraints(sceneLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(patientLabel, 0, 4, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(dateLabel, 0, 5, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scansLabel, 0, 6, 2, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(scrollPane, 0, 7, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(notesLabel, 0, 8, 2, 1, HPos.LEFT, VPos.CENTER);		
		GridPane.setConstraints(docNotesField, 0, 9, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(editScanBtn, 0, 10, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(delScanBtn, 0, 11, 2, 1, HPos.CENTER, VPos.CENTER);
		
		grid.getChildren().addAll(sceneLabel, delScanBtn, patientLabel, dateLabel, scrollPane, docNotesField, scansLabel, notesLabel, editScanBtn);
	}
}