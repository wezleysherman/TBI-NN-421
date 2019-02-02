package ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * This page will allow for the user to view likely trauma areas within the selected CT scan image
 * @author Canyon Schubert
 */
public class LikelyTraumaAreasScene {
	//TODO: File input from the viewScan page
	public static Scene initializeScene(StateManager manager/*, File in*/) {
		
		// declaration of UI elements
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		
		// constructing grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		// grid restraints
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		// adding side menu and content to the main scene
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// content grid gets added to the displayed grid
		mainGrid.getChildren().add(contentGrid);
		
		// displayed grid is set onto the layout for the scene
		layout.setCenter(mainGrid);
		
		// scene is created using the layout
		Scene likelyTraumaAreasScene = new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
		
		// return the newly created scene
		return likelyTraumaAreasScene;
	}	
}