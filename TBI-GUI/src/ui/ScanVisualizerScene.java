package ui;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;

/**
 * This page will allow for the user to view a CT scan image
 * @author Ty Chase
 * REFERENCES: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
 */
public class ScanVisualizerScene {
	
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane sideGrid = new GridPane();
		GridPane mainGrid;
		FileChooser fileChooser = new FileChooser();
		Button fileChoiceBtn = new Button();
		Button likelyTraumaBtn = new Button();
		Button algoVisBtn = new Button();
		Button viewCNNBtn = new Button();
		
		//Analyze button Setup/Styling
		fileChoiceBtn.setText("Select File");
		viewCNNBtn.setText("CNN Visualizer");
		likelyTraumaBtn.setText("Trauma Area Visualizer");
		algoVisBtn.setText("Algorithm Visualizer");
		
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DICOM", "*.dicom"),
                new FileChooser.ExtensionFilter("NIFTI", "*.nifti")
            );
		
		fileChoiceBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(manager.stage);
                if (file != null) {
                    //TODO
                }
            }
        });
		
		viewCNNBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("viewCNN");
			}
		});
		
		likelyTraumaBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("likelyTrauma");
			}
		});
		
		algoVisBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("algoVis");
			}
		});
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1);
		
		sideGrid = VerticalSideMenu.newSideBar(manager);
		
		
		
		
		
		// Button Positions on UI
		GridPane.setConstraints(fileChoiceBtn, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(likelyTraumaBtn, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(algoVisBtn, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(viewCNNBtn, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// add buttons to UI
		contentGrid.getChildren().addAll(fileChoiceBtn, viewCNNBtn, likelyTraumaBtn, algoVisBtn);
		
		//Merge Vertical Side Menu and Content
		mainGrid = sideGrid;
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setCenter(mainGrid);
		
		Scene scene = new Scene(layout, manager.stage.getWidth(), manager.stage.getHeight());
		
		return scene;
	}
}