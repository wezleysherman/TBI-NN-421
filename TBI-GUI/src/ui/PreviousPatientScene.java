package ui;

import utils.PatientEntry;
import utils.PatientManagement;
import java.util.Hashtable;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
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
		TableView<PatientEntry> patientTable = new TableView<PatientEntry>();
		
		//Fill the table with information from the database
		Hashtable <String, PatientEntry> patients = PatientManagement.getPatientList();
		Set<String> keySet = patients.keySet();
		ObservableList<PatientEntry> patientList = FXCollections.observableArrayList();
        for(String key: keySet){
        	PatientEntry entry = patients.get(key);
        	patientList.add(entry);
        }
		
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
				} else {
					manager.makeDialog("No patient was selected!");
				}
			}
		});
		retrieveBtn.setTooltip(new Tooltip("Retrieve patient info and scans for selected patient."));
		retrieveBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		//Setup table
		patientTable.setEditable(false);
		
		TableColumn nameCol = new TableColumn("Name");
		nameCol.prefWidthProperty().bind(patientTable.widthProperty().multiply(.5));
		nameCol.setCellValueFactory(new PropertyValueFactory<PatientEntry, String>("name"));
		
		TableColumn uidCol = new TableColumn("UID");
		uidCol.prefWidthProperty().bind(patientTable.widthProperty().multiply(.5));
		uidCol.setCellValueFactory(new PropertyValueFactory<PatientEntry, String>("uid"));
		
		patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		patientTable.getColumns().addAll(nameCol, uidCol);
		
		patientTable.setItems(patientList);
		patientTable.getStyleClass().add("tableview");
		
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