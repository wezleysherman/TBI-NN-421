package ui;

import java.util.Date;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class PreviousPatientScene {
	public static Scene initializeScene(Stage stage) {
		BorderPane layout = new BorderPane();
		BorderPane innerLayout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		TableView patientTable = new TableView();
		Button retrieve = new Button();
		ObservableList<Patient> patientList = FXCollections.observableArrayList(
				new Patient("John", "Doe", "FilePath", new Date(), "notes"),
				new Patient("Jane", "Doe", "AnotherFilePath", new Date(), "More notes than last time"));
		
		//Retrieve button Setup/Styling
		retrieve.setText("Retrieve");
		
		retrieve.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Scene scene = LandingScene.initializeScene(stage);
				stage.setScene(scene);
			}
			
		});
		
		//Setup table
		patientTable.setEditable(false);
		
		TableColumn firstNameCol = new TableColumn("First Name");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstName"));
		
		TableColumn lastNameCol = new TableColumn("Last Name");
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("lastName"));
		
		TableColumn fileCol = new TableColumn("File");
		fileCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("file"));
		
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
		mainGrid = VerticalSideMenu.newPatientInfoBar(stage);
		GridPane.setConstraints(innerLayout, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		
		mainGrid.getChildren().add(innerLayout);
		
		innerLayout.setCenter(contentGrid);
		innerLayout.setBottom(retrieve);
		layout.setCenter(mainGrid);
		
		double x = stage.getWidth();
		double y = stage.getHeight();
		Scene scene = new Scene(layout, x, y);
		
		return scene;
	}
}