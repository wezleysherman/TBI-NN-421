package ui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class PatientInfoScene {
	public static Scene initializeScene(StateManager manager, Patient patient) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		
		/*DEBUG
		System.out.println(patient.getFirstName());
		*/
		
		//Construct Grid		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//GridPane.setConstraints(patientTable, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().addAll();
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newPatientInfoBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setCenter(mainGrid);
		
		Scene scene = new Scene(layout, manager.stage.getWidth(), manager.stage.getHeight());
		
		return scene;
	}
}
