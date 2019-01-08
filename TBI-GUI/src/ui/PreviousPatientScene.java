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

public class PreviousPatientScene {
	
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		BorderPane innerLayout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		TableView<Patient> patientTable = new TableView<Patient>();
		
		/*// TODO test patients ---------------------------------------------------------------------------------
		LinkedList<Scan> johnScans = new LinkedList<Scan>();
		johnScans.push(new Scan(new Date(), new Image("resources/TestImage1.jpg")));
		ObservableList<Patient> patientList = FXCollections.observableArrayList(
				new Patient("John", "Doe", new Date(), "notes", johnScans),
				new Patient("Jane", "Doe", new Date(), "More notes than last time"));
		// ----------------------------------------------------------------------------------------------------*/
		ObservableList<Patient> patientList = FXCollections.observableArrayList();
		
		//Retrieve button Setup/Styling
		Button retrieveBtn = new Button("Retrieve");
		Style.styleButton(retrieveBtn);
		retrieveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (patientTable.getSelectionModel().getSelectedItem() != null) {
					manager.setPatient(patientTable.getSelectionModel().getSelectedItem());
					manager.getSceneStack().push(manager.getSceneID());
					manager.paintScene("PatientInfo");
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
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
}
