package ui;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class CNNVisualizationScene {

	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		Image filterImage = new Image("resources/TestImage1.jpg");
		ImageView displayImage = new ImageView();
		displayImage.setImage(filterImage);
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		GridPane.setConstraints(displayImage, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		contentGrid.getChildren().addAll(displayImage);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newSideBar(manager);

		//Fill SideBar Elements
		TextArea docNotesField = new TextArea();
		GridPane scrollGrid = new GridPane();
		ScrollPane scrollPane = new ScrollPane(scrollGrid);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		Style.styleScrollPane(scrollPane);
		ColumnConstraints scrollGridCols = new ColumnConstraints();
		scrollGridCols.setPercentWidth(100);
		scrollGrid.getColumnConstraints().add(scrollGridCols);
		scrollGrid.prefWidthProperty().bind(scrollPane.widthProperty());
		
		//Dummy Data for testing purposes
		ArrayList<String> layerList = new ArrayList<String>();
		for(int i = 0; i < 10; i++) {
			layerList.add("Layer " + i);
		}
		
		for(int j = 0; j < layerList.size(); j++) {
			//Place Holder -> Can be changed to panes for better look
			Button layerButton = new Button();
			layerButton.setMaxWidth(Double.MAX_VALUE);
			layerButton.setText((String) layerList.get(j));
			GridPane.setMargin(layerButton, new Insets(0, 20, 0, 0));
			GridPane.setConstraints(layerButton, 0, j, 2, 1, HPos.LEFT, VPos.CENTER);
			scrollGrid.getChildren().add(layerButton);
		}
		
		//Set up text area
		Style.styleTextArea(docNotesField);
		docNotesField.setText("This is where the doctors notes would be entered into the sidebar.");
		
		//Add elements to sideBar
		GridPane.setConstraints(scrollPane, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(docNotesField, 0, 4, 2, 1, HPos.CENTER, VPos.CENTER);
		GridPane sideBarGrid = (GridPane) mainGrid.getChildren().get(1);
		sideBarGrid.getChildren().addAll(scrollPane, docNotesField);
		
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		//Add contentGrid to main grid
		mainGrid.getChildren().add(contentGrid);
		
		Style.styleBorderPane(layout);
		layout.setCenter(mainGrid);
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
}