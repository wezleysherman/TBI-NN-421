package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.EmptyStackException;

public class VerticalSideMenu {
	
	private final static String VERTICAL_MENU_COLOR = "-fx-background-color: #455357";
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
	
	private static void styleLabel(Label label) {
		label.setStyle("-fx-text-fill: #f1fafe; -fx-font-size:14px;");
	}
	
	public static GridPane newSideBar(StateManager manager) {
		GridPane mainGrid = new GridPane();
		GridPane contentGrid = new GridPane();
		Label appLabel = new Label("TBI Application");
		Pane colorPane = new Pane();
		
		// Home Button Dialog SetUp TODO: Find a way to center the yes/no buttons
		Dialog<ButtonType> homeWarning = new Dialog<ButtonType>();
		homeWarning.setTitle("Warning!");
		homeWarning.setHeaderText("Returning to the home page will cause you to lose all unsaved information!");
		homeWarning.setResizable(false);
		GridPane hwPane = new GridPane();
		Label hwLabel = new Label("Continue?");
		hwPane.add(hwLabel, 1, 0);
		hwPane.setAlignment(Pos.CENTER);
		ButtonType hwConfirm = new ButtonType("Yes", ButtonData.YES);
		ButtonType hwDecline = new ButtonType("No", ButtonData.NO);
		homeWarning.getDialogPane().setContent(hwPane);
		homeWarning.getDialogPane().getButtonTypes().addAll(hwConfirm, hwDecline);	
		
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
		styleLabel(appLabel);
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
		colorPane.setStyle(VERTICAL_MENU_COLOR);
		GridPane.setConstraints(colorPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(contentGrid, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		mainGrid.getChildren().addAll(colorPane, contentGrid);
		
		//backBtn------------------------------------------------------------------------------------------------------------------------------------
		Button backBtn = new Button("Back");
		backBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		styleButton(backBtn);
		backBtn.setTooltip(new Tooltip("Return to the previous page (You will lose any information you input on this page)."));
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					if (manager.sceneID.equals("algoVis") && manager.sceneStack.peek().equals("algoVis")) {
						manager.sceneStack.pop();
						manager.paintScene(manager.sceneStack.pop());
					}
					else {
						manager.paintScene(manager.sceneStack.pop());
					}
				}
				catch (EmptyStackException ex) {
					manager.paintScene("landing");
					System.out.println(ex + " Something wrong with stack implementation, returning to landing page.");
				}
			}
		});
		GridPane.setConstraints(backBtn, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(backBtn);
		
		//homeBtn------------------------------------------------------------------------------------------------------------------------------------
		Button homeBtn = new Button("Home");
		homeBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		styleButton(homeBtn);
		homeBtn.setTooltip(new Tooltip("Return to the home page (You will lose any unsaved information from this run of the program)."));
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeWarning.showAndWait();
				if (homeWarning.getResult().getButtonData().equals(ButtonData.YES)) {
					manager.sceneStack.clear();
					manager.paintScene("landing");
				}
			}
		});
		GridPane.setConstraints(homeBtn,  1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(homeBtn);
		
		//algoVisBtn---------------------------------------------------------------------------------------------------------------------------------
		Button algoVisBtn = new Button ("Algorithm Visualizer");
		algoVisBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		styleButton(algoVisBtn);
		algoVisBtn.setTooltip(new Tooltip("View the accuracy of the algorithm as a whole."));
		algoVisBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!manager.sceneID.equals("algoVis")) {
					manager.sceneStack.push(manager.sceneID);
				}
				manager.paintScene("algoVis");
			}
		});
		GridPane.setConstraints(algoVisBtn, 0, 2, 2, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(algoVisBtn);
		
		//Side bar for different pages---------------------------------------------------------------------------------------------------------------
		if (manager.sceneID.equals("newPat")) {
			//TODO
		}
		else if (manager.sceneID.equals("viewScan")) {
			//TODO
		}
		else if (manager.sceneID.equals("likelyTrauma")) {
			//TODO
		}
		else if (manager.sceneID.equals("viewCNN")) {
			//TODO
		}
		else if (manager.sceneID.equals("prevPat")) {
			//TODO
		}
		else if (manager.sceneID.equals("algoVis")) {
			Label sceneLabel = new Label("Algorithm Visualizer");
			styleLabel(sceneLabel);
			GridPane.setConstraints(sceneLabel, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
			Button recentBtn = new Button("Last 100 Scans");
			styleButton(recentBtn);
			recentBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			recentBtn.setTooltip(new Tooltip("View the accuracy of the algorithm in its last 100 uses."));
			GridPane.setConstraints(recentBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
			contentGrid.getChildren().addAll(sceneLabel, recentBtn);
			
			recentBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					manager.sceneStack.push("algoVis");
					manager.paintScene("algoVis", false);
				}
			});
		}
		else if (manager.sceneID.equals("patInfo")) {
			//TODO
		}
		return mainGrid;
	}
	
	public static GridPane newSideBar(StateManager manager, Patient patient) {
		GridPane mainGrid = new GridPane();
		GridPane contentGrid = new GridPane();
		Label appLabel = new Label("TBI Application");
		Pane colorPane = new Pane();
	
		// Home Button Dialog SetUp TODO: Find a way to center the yes/no buttons
		Dialog<ButtonType> homeWarning = new Dialog<ButtonType>();
		homeWarning.setTitle("Warning!");
		homeWarning.setHeaderText("Returning to the home page will cause you to lose all unsaved information!");
		homeWarning.setResizable(false);
		GridPane hwPane = new GridPane();
		Label hwLabel = new Label("Continue?");
		hwPane.add(hwLabel, 1, 0);
		hwPane.setAlignment(Pos.CENTER);
		ButtonType hwConfirm = new ButtonType("Yes", ButtonData.YES);
		ButtonType hwDecline = new ButtonType("No", ButtonData.NO);
		homeWarning.getDialogPane().setContent(hwPane);
		homeWarning.getDialogPane().getButtonTypes().addAll(hwConfirm, hwDecline);	
		
		//Construct content grid
		contentGrid.setHgap(5);
		contentGrid.setVgap(5);
		contentGrid.setPadding(new Insets(5, 5, 5, 5));
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(50);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(50);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(50);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(50);
		contentGrid.getColumnConstraints().addAll(column0, column1, column2, column3);
		
		//Add elements to content grid
		styleLabel(appLabel);
		GridPane.setConstraints(appLabel, 0, 0, 4, 1, HPos.CENTER, VPos.CENTER);
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
		colorPane.setStyle(VERTICAL_MENU_COLOR);
		GridPane.setConstraints(colorPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(contentGrid, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		mainGrid.getChildren().addAll(colorPane, contentGrid);
		
		//backBtn------------------------------------------------------------------------------------------------------------------------------------
		Button backBtn = new Button("Back");
		backBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		styleButton(backBtn);
		backBtn.setTooltip(new Tooltip("Return to the previous page (You will lose any information you input on this page)."));
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					if (manager.sceneID.equals("algoVis") && manager.sceneStack.peek().equals("algoVis")) {
						manager.sceneStack.pop();
						manager.paintScene(manager.sceneStack.pop());
					}
					else {
						manager.paintScene(manager.sceneStack.pop());
					}
				}
				catch (EmptyStackException ex) {
					manager.paintScene("landing");
					System.out.println(ex + " Something wrong with stack implementation, returning to landing page.");
				}
			}
		});
		GridPane.setConstraints(backBtn, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(backBtn);
		
		//homeBtn------------------------------------------------------------------------------------------------------------------------------------
		Button homeBtn = new Button("Home");
		homeBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		styleButton(homeBtn);
		homeBtn.setTooltip(new Tooltip("Return to the home page (You will lose any unsaved information from this run of the program)."));
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeWarning.showAndWait();
				if (homeWarning.getResult().getButtonData().equals(ButtonData.YES)) {
					manager.sceneStack.clear();
					manager.paintScene("landing");
				}
			}
		});
		GridPane.setConstraints(homeBtn,  2, 1, 2, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().add(homeBtn);
		
		// TODO: Change around formatting based on group concensus
		if (manager.sceneID.equals("viewScan")) {
			Label patientLabel = new Label(patient.getFirstName() + " " + patient.getLastName());
			styleLabel(patientLabel);
			GridPane.setConstraints(patientLabel, 0, 5, 4, 1, HPos.CENTER, VPos.CENTER);
			Label dateLabel = new Label(patient.getDate().toString());
			styleLabel(dateLabel);
			GridPane.setConstraints(dateLabel, 0, 6, 4, 1, HPos.CENTER, VPos.CENTER);
			Label recentLabel = new Label("Other Recent Scans:");
			styleLabel(recentLabel);
			GridPane.setConstraints(recentLabel, 0, 8, 4, 1, HPos.CENTER, VPos.CENTER);
			
			contentGrid.getChildren().addAll(patientLabel, dateLabel, recentLabel);
			
			for (int i = 0; i < patient.getNumScans(); ++i) {
				Label newLbl = new Label("");
				if (i == 0) {
					newLbl.setText("Latest:");
				}
				else if (i == patient.getNumScans()-1) {
					newLbl.setText("Oldest:");
				}
				styleLabel(newLbl);
				GridPane.setConstraints(newLbl, 0, i + 9, 1, 1, HPos.RIGHT, VPos.CENTER);
				Button newBtn = new Button(patient.getScans().get(i).getDateOfScan().toString());
				styleButton(newBtn);
				GridPane.setConstraints(newBtn, 1, i + 9, 3, 1, HPos.CENTER, VPos.CENTER);
				newBtn.setTooltip(new Tooltip("View this scan."));
				
				// TODO: Implement this?
				newBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						
					}
				});
				
				contentGrid.getChildren().addAll(newLbl, newBtn);
			}
			
			Button uploadBtn = new Button("Upload New Scan");
			styleButton(uploadBtn);
			GridPane.setConstraints(uploadBtn, 1, 10 + patient.getNumScans(), 3, 1, HPos.CENTER, VPos.CENTER);
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
					File file = fileChooser.showOpenDialog(manager.stage);
	                if (file != null) {
	                    //TODO
	                }
				}
			});
			
			Label notesLabel = new Label("Doctors Notes: \n" + patient.getNotes());
			styleLabel(notesLabel);
			notesLabel.setWrapText(true);
			GridPane.setConstraints(notesLabel, 0, 13 + patient.getNumScans(), 4, 1, HPos.CENTER, VPos.CENTER);
			
			contentGrid.getChildren().addAll(uploadBtn, notesLabel);
		}
		
		return mainGrid;
	}
}