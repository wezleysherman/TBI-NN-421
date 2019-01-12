package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import utils.PatientManagement;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class VerticalSideMenu {
		
	public static GridPane newSideBar(StateManager manager) {
		GridPane mainGrid = new GridPane();
		GridPane contentGrid = new GridPane();
		Label appLabel = new Label("TBI Application");
		Pane colorPane = new Pane();
		
		// Home Button Dialog SetUp
		Dialog<ButtonType> homeWarning = new Dialog<ButtonType>();
		homeWarning.setTitle("Warning!");
		homeWarning.setHeaderText("Returning to the home page will cause you to lose all unsaved information!");
		homeWarning.setResizable(false);
		GridPane hwPane = new GridPane();
		ColumnConstraints hwCol1 = new ColumnConstraints();
		hwCol1.setPercentWidth(50);
		ColumnConstraints hwCol2 = new ColumnConstraints();
		hwCol2.setPercentWidth(50);
		hwPane.getColumnConstraints().addAll(hwCol1, hwCol2);
		Label hwLabel = new Label("Continue?");
		hwPane.setAlignment(Pos.CENTER);
		Button hwY = new Button("Yes");
		Button hwN = new Button("No");
		hwY.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent arg0) {
				homeWarning.setResult(ButtonType.YES);
				homeWarning.close();
			}
		});
		hwN.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent arg0) {
				homeWarning.setResult(ButtonType.NO);
				homeWarning.close();
			}
		});
		hwY.setPrefWidth(75);
		hwN.setPrefWidth(75);
		hwPane.setVgap(5);
		hwPane.setHgap(5);
		hwN.setDefaultButton(true);
		hwPane.getChildren().addAll(hwLabel, hwY, hwN);
		GridPane.setConstraints(hwLabel, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(hwY, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
		GridPane.setConstraints(hwN, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		
		homeWarning.getDialogPane().setContent(hwPane);
		// TODO: Find a way to remove empty button bar (empty padding at the button)
		
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
		Style.styleLabel(appLabel);
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
		Style.stylePane(colorPane);
		GridPane.setConstraints(colorPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(contentGrid, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		mainGrid.getChildren().addAll(colorPane, contentGrid);
		
		//backBtn------------------------------------------------------------------------------------------------------------------------------------
		Button backBtn = new Button("Back");
		Style.styleButton(backBtn);
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
		Style.styleButton(homeBtn);
		homeBtn.setTooltip(new Tooltip("Return to the home page (You will lose any unsaved information from this run of the program)."));
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeWarning.showAndWait();
				if (homeWarning.getResult().getButtonData().equals(ButtonData.YES)) {
					manager.getSceneStack().clear();
					manager.paintScene("Landing");
				}
			}
		});
		GridPane.setConstraints(homeBtn,  1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(homeBtn);
		
		//algoVisBtn---------------------------------------------------------------------------------------------------------------------------------
		Button algoVisBtn = new Button ("Algorithm Visualizer");
		Style.styleButton(algoVisBtn);
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
			Label sceneLabel = new Label("Algorithm Visualizer");
			Style.styleLabel(sceneLabel);
			GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
			Button recentBtn = new Button("Last 100 Scans");
			Style.styleButton(recentBtn);
			recentBtn.setTooltip(new Tooltip("View the accuracy of the algorithm in its last 100 uses."));
			GridPane.setConstraints(recentBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
			
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
			
			contentGrid.getChildren().addAll(sceneLabel, recentBtn);
		}
		else if (manager.getSceneID().equals("PatientInfo")) {
			Label sceneLabel = new Label("Patient Info");
			Style.styleLabel(sceneLabel);
			GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
			Button editBtn = new Button("Edit Patient");
			Style.styleButton(editBtn);
			editBtn.setTooltip(new Tooltip("Edit this patient's data."));
			GridPane.setConstraints(editBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
			
			editBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					manager.setStateBool(true);
					manager.paintScene("PatientInfo");
				}
			});
			
			contentGrid.getChildren().addAll(sceneLabel, editBtn);
		}
		else if (manager.getSceneID().equals("ScanVisualizer")) {
			ColumnConstraints column2 = new ColumnConstraints();
			column2.setPercentWidth(50);
			ColumnConstraints column3 = new ColumnConstraints();
			column3.setPercentWidth(50);
			contentGrid.getColumnConstraints().addAll(column2, column3);
			
			GridPane.setConstraints(appLabel, 0, 0, 4, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(backBtn, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(homeBtn,  2, 1, 2, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(algoVisBtn, 0, 2, 4, 1, HPos.CENTER, VPos.CENTER);
			
			//TODO Make it take the patient entry. This page should be able to be gotten to without having a patient
			Label patientLabel = new Label("PLACEHOLDER1");
			Style.styleLabel(patientLabel);
			GridPane.setConstraints(patientLabel, 0, 6, 4, 1, HPos.CENTER, VPos.CENTER);
			Label dateLabel = new Label("PlaceHolder");//manager.getPatient().getDate().toString());
			Style.styleLabel(dateLabel);
			GridPane.setConstraints(dateLabel, 0, 7, 4, 1, HPos.CENTER, VPos.CENTER);
			Label recentLabel = new Label("Other Recent Scans:");
			Style.styleLabel(recentLabel);
			GridPane.setConstraints(recentLabel, 0, 9, 4, 1, HPos.CENTER, VPos.CENTER);
			
			contentGrid.getChildren().addAll(patientLabel, dateLabel, recentLabel);
			
			try {
				Patient patient = PatientManagement.importPatient(PatientManagement.getDefaultPath(), manager.getPatient().getUid());
				for (int i = 0; i < patient.getNumScans(); ++i) {
					Label newLbl = new Label("");
					if (i == 0) {
						newLbl.setText("Latest:");
					}
					else if (i == patient.getNumScans()-1) {
						newLbl.setText("Oldest:");
					}
					Style.styleLabel(newLbl);
					GridPane.setConstraints(newLbl, 0, i + 10, 1, 1, HPos.RIGHT, VPos.CENTER);
					Button newBtn = new Button(patient.getScans().get(i).getDateOfScan().toString());
					Style.styleButton(newBtn);
					GridPane.setConstraints(newBtn, 1, i + 10, 3, 1, HPos.CENTER, VPos.CENTER);
					newBtn.setTooltip(new Tooltip("View this scan."));
					
					newBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							// TODO: Implement this?
						}
					});
					
					contentGrid.getChildren().addAll(newLbl, newBtn);
				}
			} catch (IOException e) {
				manager.makeDialog("No PatientEntry object set in manager \n" + e.getStackTrace());
			}

			Button uploadBtn = new Button("Upload New Scan");
			Style.styleButton(uploadBtn);
			//TODO needs to take into account the number of scans when placing things in the proper row...for some reason...this will change
			GridPane.setConstraints(uploadBtn, 1, 11 + 0, 3, 1, HPos.CENTER, VPos.CENTER);
			// TODO: Implement this?
			uploadBtn.setTooltip(new Tooltip("Upload a new scan for this patient."));
			
			uploadBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.getExtensionFilters().addAll(
			                new FileChooser.ExtensionFilter("DICOM", "*.dicom"),
			                new FileChooser.ExtensionFilter("NIFTI", "*.nifti")
			            );
					File file = fileChooser.showOpenDialog(manager.getStage());
	                if (file != null) {
	                    //TODO
	                }
				}
			});
			
			Label notesLabel = new Label("PLACEHOLDER2"); //TODO "Doctors Notes: \n" + manager.getPatient().getNotes());
			Style.styleLabel(notesLabel);
			notesLabel.setWrapText(true);
			//TODO fix how things are laid out for this entire sidebar
			GridPane.setConstraints(notesLabel, 0, 14 + 0, 4, 1, HPos.CENTER, VPos.CENTER);
			
			contentGrid.getChildren().addAll(uploadBtn, notesLabel);
		}
		return mainGrid;
	}
	
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
		Style.styleScrollPane(scrollPane);
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
		Style.styleTextArea(docNotesField);
		docNotesField.setText("This is where the doctors notes would be entered into the sidebar.");
		
		//Set style labels
		Style.styleLabel(dateLabel);
		Style.styleLabel(screenNameLabel);
		
		//Add elements to sideBar
		GridPane.setConstraints(dateLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(screenNameLabel, 0, 4, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(scrollPane, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(docNotesField, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
		grid.getChildren().addAll(dateLabel, screenNameLabel, scrollPane, docNotesField);
	}
}
