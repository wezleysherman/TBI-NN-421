package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class VerticalSideMenu {
	
	private final static String VERTICAL_MENU_COLOR = "-fx-background-color: #455357";
	
	public static GridPane newPatientInfoBar(Stage stage, StateManager manager) {
		GridPane mainGrid = new GridPane();
		GridPane contentGrid = new GridPane();
		Button backBtn = new Button();
		Label pageName = new Label();
		Pane colorPane = new Pane();
		
		//Button Setup
		backBtn.setText("BACK");
		backBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				manager.paintScene(manager.sceneStack.pop());
			}
			
		});
		
		//Color Cells of Vertical Side Menu and combine content with main grid layout
		colorPane.setStyle(VERTICAL_MENU_COLOR);
		
		GridPane.setConstraints(colorPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(contentGrid, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		
		mainGrid.getChildren().addAll(colorPane, contentGrid);
		
		
		//Label Setup
		pageName.setText("New Patient Entry");
		
		GridPane.setConstraints(backBtn, 0, 0, 1, 1, HPos.LEFT, VPos.TOP);
		GridPane.setConstraints(pageName, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);

		contentGrid.getChildren().addAll(backBtn, pageName);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		mainGrid.getRowConstraints().add(rowCon);
		
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100/5);
		mainGrid.getColumnConstraints().add(0, columnCon);
		
		ColumnConstraints columnCon2 = new ColumnConstraints();
		columnCon2.setPercentWidth(400/5);
		mainGrid.getColumnConstraints().add(1, columnCon2);
		
		return mainGrid;
	}
}
