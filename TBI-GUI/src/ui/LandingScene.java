package ui;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class LandingScene {
	
	public static boolean debug = true; //Manually change this value
		
	public static Scene initializeScene(Stage stage) {
		BorderPane layout = new BorderPane();
		GridPane grid = new GridPane();
		Label orLabel = new Label("or");
		Button newPatBtn = new Button();
		ComboBox<String> prevDrp = new ComboBox<>();
		ArrayList<String> items = new ArrayList<>();
		Button viewScanBtn = new Button();
		Button viewCNNBtn = new Button();
		
		//Button Setup/Styling
		newPatBtn.setText("Start New Patient");
		newPatBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				BorderPane root = new BorderPane();
				Scene scene = PatientInfoEntryScene.initializeScene(stage);
				stage.setScene(scene);
			}
		});
		
		viewScanBtn.setText("View Scan <DEBUG>");
		viewScanBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				BorderPane root = new BorderPane();
				Scene scene = ScanVisualizerScene.initializeScene(stage);
				stage.setScene(scene);
			}
		});
		
		viewCNNBtn.setText("View CNN Vis <DEBUG>");
		viewCNNBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				BorderPane root = new BorderPane();
				Scene scene = CNNVisualizationScene.initializeScene(stage);
				stage.setScene(scene);
			}
		});
		
		//Drop Down Setup/Styling
		items.add("Select Previous Patient");
		items.add("This is how to add patients");
		items.add("Fun with code");		
		ObservableList<String> dropList = FXCollections.observableArrayList(items);

		prevDrp.setItems(dropList);
		prevDrp.getSelectionModel().select(0);
		
		//Construct Grid
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(15);
		grid.setHgap(10);
		
		GridPane.setConstraints(newPatBtn, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(orLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(prevDrp, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		grid.getChildren().addAll(newPatBtn, orLabel, prevDrp);
		if (debug) {
			GridPane.setConstraints(viewScanBtn, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
			GridPane.setConstraints(viewCNNBtn, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
			grid.getChildren().addAll(viewScanBtn, viewCNNBtn);
		}
			
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		grid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		grid.getColumnConstraints().add(columnCon);
		
		//Add Grid and layout to scene
		layout.setCenter(grid);
		
		double x = stage.getWidth();
		double y = stage.getHeight();
		Scene scene = new Scene(layout, x, y);
		
		return scene;
	}
}