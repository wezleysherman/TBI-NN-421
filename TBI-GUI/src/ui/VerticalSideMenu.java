package ui;

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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import utils.Patient;
import utils.PatientManagement;
import utils.Scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class VerticalSideMenu {
		
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
				catch (EmptyStackException ex) {
					manager.paintScene("Landing");
					System.out.println(ex + " Something wrong with stack implementation, returning to landing page.");
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
		if (manager.getSceneID().equals("PatientInfoEntry")) {
			//TODO
		}
		else if (manager.getSceneID().equals("LikelyTraumaAreas")) {
			makeLTA(contentGrid);
		}
		else if (manager.getSceneID().equals("CNNVisualizer")) {
			makeCNN(contentGrid);
		}
		else if (manager.getSceneID().equals("PreviousPatient")) {
			//TODO
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
	private static void makeCNN(GridPane grid) {
		//Construct main grid
		RowConstraints rowCon = new RowConstraints();
		ColumnConstraints columnCon = new ColumnConstraints();

		//Construct Grid for sideBar
		grid.getRowConstraints().addAll(rowCon, rowCon, rowCon);
		grid.getColumnConstraints().addAll(columnCon);
		
		RowConstraints rowCon2 = new RowConstraints();
		rowCon2.setPercentHeight(40);
		grid.getRowConstraints().add(rowCon2);
		
		RowConstraints rowCon3 = new RowConstraints();
		rowCon3.setPercentHeight(30);
		grid.getRowConstraints().add(rowCon3);
		
		//Create Elements
		ColumnConstraints columnConScroll = new ColumnConstraints();
		columnConScroll.setPercentWidth(100);
	}
	
	//Add LTA Elements to the Main Grid
	private static void makeLTA(GridPane grid) {
		//Construct main grid
		RowConstraints rowCon = new RowConstraints();
		ColumnConstraints columnCon = new ColumnConstraints();

		//Construct Grid for sideBar
		grid.getRowConstraints().addAll(rowCon, rowCon, rowCon, rowCon, rowCon);
		grid.getColumnConstraints().addAll(columnCon);
		
		RowConstraints rowCon2 = new RowConstraints();
		rowCon2.setPercentHeight(40);
		grid.getRowConstraints().add(rowCon2);
		
		RowConstraints rowCon3 = new RowConstraints();
		rowCon3.setPercentHeight(30);
		grid.getRowConstraints().add(rowCon3);
		
		//Create Elements
		ColumnConstraints columnConScroll = new ColumnConstraints();
		columnConScroll.setPercentWidth(100);
		
		Label dateLabel = new Label("CT Scan 10/15/1994");
		Label screenNameLabel = new Label("Likely Trauma Areas");
		TextArea docNotesField = new TextArea();
		GridPane scrollGrid = new GridPane();
		ScrollPane scrollPane = new ScrollPane(scrollGrid);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.getStyleClass().add("");
		ColumnConstraints scrollGridCols = new ColumnConstraints();
		scrollGridCols.setPercentWidth(100);
		scrollGrid.getColumnConstraints().add(scrollGridCols);
		scrollGrid.prefWidthProperty().bind(scrollPane.widthProperty());
		
		//Dummy Data for testing purposes
		ArrayList<String> regionList = new ArrayList<String>();
		for(int i = 0; i < 10; i++) {
			regionList.add("Region " + i);
		}
		
		for(int j = 0; j < regionList.size(); j++) {
			//TODO change make button set file being viewed and repaint the scene
			//Place Holder -> Can be changed to panes for better look
			Button layerButton = new Button();
			layerButton.setMaxWidth(Double.MAX_VALUE);
			layerButton.setText((String) regionList.get(j));
			GridPane.setMargin(layerButton, new Insets(0, 20, 0, 0));
			GridPane.setConstraints(layerButton, 0, j, 2, 1, HPos.LEFT, VPos.CENTER);
			scrollGrid.getChildren().add(layerButton);
		}
		
		//Set up text area
		docNotesField.getStyleClass().add("text-area-sidebar");
		docNotesField.setText("This is where the doctors notes would be entered into the sidebar.");
		
		//Set style labels
		dateLabel.getStyleClass().add("label-white");
		screenNameLabel.getStyleClass().add("label-white");
		
		//Add elements to sideBar
		GridPane.setConstraints(dateLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(screenNameLabel, 0, 4, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(scrollPane, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(docNotesField, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
		grid.getChildren().addAll(dateLabel, screenNameLabel, scrollPane, docNotesField);
	}
	
	//Add AV Elements to the Main Grid
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
	
	//Add PI Elements to the Main Grid
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
						//TODO in error task
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
						manager.makeError("Unable to delete scans. An issue with scans in the database occured.", e);
					}
				}
			}
		});
		
		grid.getChildren().addAll(sceneLabel, editBtn, delPatBtn, delScansBtn);
	}
	
	//Add SV Elements to the Main Grid
	private static void makeSV(GridPane grid, StateManager manager) { //TODO everything is sized wrong on this sidebar
		try {
			Patient patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
			
			Label sceneLabel = new Label("Scan Vizualizer");
			sceneLabel.getStyleClass().add("label-white");
			GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
			
			Label patientLabel = new Label("Patient: " + patient.getFirstName() + " " + patient.getLastName());
			patientLabel.getStyleClass().add("label-white");
			GridPane.setConstraints(patientLabel, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
			
			Label dateLabel = new Label("Scan Date: " + manager.getScan().getDateOfScan().toString());
			dateLabel.getStyleClass().add("label-white");
			GridPane.setConstraints(dateLabel, 0, 7, 2, 1, HPos.CENTER, VPos.CENTER);
			
			Label notesLabel = new Label("Scan Notes: " + manager.getScan().getNotes()); //TODO make notes editable
			notesLabel.getStyleClass().add("label-white");
			notesLabel.setWrapText(true);
			GridPane.setConstraints(notesLabel, 0, 8, 2, 1, HPos.CENTER, VPos.CENTER);
			
			Button delScanBtn = new Button("Delete Scan");
			delScanBtn.setMaxWidth(Double.MAX_VALUE);
			delScanBtn.setTooltip(new Tooltip("Delete this scan."));
			GridPane.setConstraints(delScanBtn, 0, 9, 2, 1, HPos.CENTER, VPos.CENTER);
			delScanBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					boolean yes = manager.makeQuestion("This will remove all of the data associated with this scan, are you sure you want to continue?");
					if (yes) {
						for(Scan scan : patient.getRawScans()) {
				        	if (scan.equals(manager.getScan())) {
				        		patient.getRawScans().remove(scan);
				        		break;
				        	}
				        }
						try {
							patient.savePatient();
							manager.setScan(null);
							manager.getSceneStack().pop();
							manager.paintScene("PatientInfo");
						} catch (Exception e) {
							// TODO in error task
						}
					}
				}
			});
			
			Label otherLabel = new Label("Other Scans:");
			otherLabel.getStyleClass().add("label-white");
			GridPane.setConstraints(otherLabel, 0, 10, 2, 1, HPos.CENTER, VPos.CENTER);
			
			Label newestLabel = new Label("Newest");
			newestLabel.getStyleClass().add("label-white");
			GridPane.setConstraints(newestLabel, 0, 11, 2, 1, HPos.CENTER, VPos.CENTER);
		
			for (int i = 0; i < patient.getNumRawScans(); ++i) {
				Button scanBtn = new Button(patient.getRawScans().get(i).getDateOfScan().toString());
				scanBtn.setMaxWidth(Double.MAX_VALUE);
				GridPane.setConstraints(scanBtn, 0, 12 + i, 2, 1, HPos.CENTER, VPos.CENTER);
				scanBtn.setTooltip(new Tooltip("View this scan."));
				
				scanBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO: Implement this
					}
				});
				
				grid.getChildren().add(scanBtn);
			}
			
			Label oldestLabel = new Label("Oldest");
			oldestLabel.getStyleClass().add("label-white");
			GridPane.setConstraints(oldestLabel, 0, 12 + patient.getNumRawScans(), 2, 1, HPos.CENTER, VPos.CENTER);
			
			grid.getChildren().addAll(sceneLabel, delScanBtn, patientLabel, dateLabel, otherLabel, notesLabel, newestLabel, oldestLabel);
		} catch (Exception e) {
			//TODO in error task
		}
	}
}