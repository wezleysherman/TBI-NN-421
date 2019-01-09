package ui;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

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
import utils.PatientEntry;
import utils.PatientManagement;

public class PreviousPatientScene {
	
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		BorderPane innerLayout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		TableView patientTable = new TableView();
		Button retrieveBtn = new Button();
		
		//Fill the table with information from the database
		Hashtable <String, PatientEntry> patients = PatientManagement.getPatientList();
		Set<String> keySet = patients.keySet();
		ObservableList<Patient> patientList = FXCollections.observableArrayList();
        for(String key: keySet){
        	patientList.add(new Patient("Johnnnnnnnnnnnnn", "Doe", new Date(), "notes"));
        	PatientEntry entry = patients.get(key);
        	patientList.add(new Patient(entry.name, entry.name, new Date(), "notesss"));
        }
		
		//Retrieve button Setup/Styling
		retrieveBtn.setText("Retrieve");
		Style.styleButton(retrieveBtn);
		retrieveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (patientTable.getSelectionModel().getSelectedItem() != null) {
					manager.setPatient((Patient)patientTable.getSelectionModel().getSelectedItem());
					manager.getSceneStack().push(manager.getSceneID());
					manager.paintScene("PatientInfo");
				} else {
					manager.makeDialog("Error", "No patient was selected!");
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