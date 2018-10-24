package app;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class LandingScene {
		
	public Scene initializeScene(Scene scene) {
		BorderPane layout = new BorderPane();
		GridPane grid = new GridPane();
		Label orLabel = new Label("or");
		Button newPatBtn = new Button();
		ComboBox<String> prevDrp = new ComboBox<>();
		ArrayList<String> items = new ArrayList<>();
		
		//Button Setup/Styling
		newPatBtn.setText("Start New Patient");
		
		//Label Styling
		
		
		//Drop Down Setup/Styling
		items.add("Select Previous Patient");
		items.add("This is how to add patients");
		items.add("Frick");		
		ObservableList<String> dropList = FXCollections.observableArrayList(items);

		prevDrp.setItems(dropList);
		
		//Construct Grid
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(15);
		grid.setHgap(10);
		
		GridPane.setConstraints(newPatBtn, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(orLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(prevDrp, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		
		grid.getChildren().addAll(newPatBtn, orLabel, prevDrp);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		grid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		grid.getColumnConstraints().add(columnCon);
		
		//Add Grid and layout to scene
		layout.setCenter(grid);
		prevDrp.getSelectionModel().select(0);
		
		Scene landingScene = new Scene(layout, 960, 540);
		
		return landingScene;
	}
}