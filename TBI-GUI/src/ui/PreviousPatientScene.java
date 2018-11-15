package ui;

import java.util.Date;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

// TODO: find a way to reduce the amount of warnings thrown by this page
public class PreviousPatientScene {
	
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
	
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		BorderPane innerLayout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		TableView patientTable = new TableView();
		Button retrieveBtn = new Button();
		
		// test patients --------------------------------------------------------------------------------------
		LinkedList<Scan> johnScans = new LinkedList<Scan>();
		johnScans.push(new Scan(new Date(), new Image("resources/TestImage1.jpg")));
		ObservableList<Patient> patientList = FXCollections.observableArrayList(
				new Patient("John", "Doe", new Date(), "notes", johnScans),
				new Patient("Jane", "Doe", new Date(), "More notes than last time"));
		// ----------------------------------------------------------------------------------------------------
		
		//Retrieve button Setup/Styling
		retrieveBtn.setText("Retrieve");
		styleButton(retrieveBtn);
		retrieveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (patientTable.getSelectionModel().getSelectedItem() != null) {
					manager.sceneStack.push(manager.sceneID);
					manager.paintScene("patInfo", (Patient)patientTable.getSelectionModel().getSelectedItem());
				}
			}
		});
		retrieveBtn.setTooltip(new Tooltip("Retrieve patient info and scans for selected patient."));
		retrieveBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		//Setup table
		patientTable.setEditable(false);
		
		TableColumn firstNameCol = new TableColumn("First Name");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstName"));
		
		TableColumn lastNameCol = new TableColumn("Last Name");
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastName"));
		
		// TODO: figure out how to do number of scans or last scan date or something here
		TableColumn fileCol = new TableColumn("# of Scans");
		fileCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("numScans"));
		
		TableColumn dateCol = new TableColumn("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("date"));
		
		TableColumn notesCol = new TableColumn("Notes");
		notesCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("notes"));
		
		patientTable.getColumns().addAll(firstNameCol, lastNameCol, fileCol, dateCol, notesCol);
				
		patientTable.setItems(patientList);
		
		//Construct Grid		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		GridPane.setConstraints(patientTable, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().addAll(patientTable);
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(innerLayout, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(innerLayout);
		
		innerLayout.setCenter(contentGrid);
		innerLayout.setBottom(retrieveBtn);
		layout.setCenter(mainGrid);
		
		double x = manager.stage.getWidth();
		double y = manager.stage.getHeight();
		Scene scene = new Scene(layout, x, y);
		
		return scene;
	}
}
