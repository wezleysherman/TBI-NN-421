package ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class CNNVisualizationScene {

	public static Scene initializeScene(StateManager manager) {

		// declaration of UI elements
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		Image filterImage = new Image("resources/TestImage1.jpg");
		ImageView displayImage = new ImageView();
		displayImage.setImage(filterImage);
		
		// constructing grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		// grid restraints
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//Add image to the screen
		GridPane.setConstraints(displayImage, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		contentGrid.getChildren().add(displayImage);
		
		// adding side menu and content to the main scene
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// content grid gets added to the displayed grid
		mainGrid.getChildren().add(contentGrid);
		contentGrid.getStyleClass().add("content-pane");
		
		// displayed grid is set onto the layout for the scene
		layout.setCenter(mainGrid);
		
		// scene is created using the layout
		Scene likelyTraumaAreasScene = new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
		
		// return the newly created scene
		return likelyTraumaAreasScene;
	}
}