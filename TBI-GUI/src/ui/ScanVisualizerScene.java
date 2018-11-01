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
import javafx.stage.Stage;

/**
 * This page will allow for the user to view a CT scan image
 * @author Ty Chase
 * REFERENCES: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
 */
public class ScanVisualizerScene {
	
	public static Scene initializeScene(Stage stage, StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		FileChooser fileChooser = new FileChooser();
		Button viewBtn = new Button();
		Button likelyTraumaBtn = new Button();
		
		//Analyze button Setup/Styling
		viewBtn.setText("Select File");
		likelyTraumaBtn.setText("View Likely Trauma Areas");
		
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DICOM", "*.dicom"),
                new FileChooser.ExtensionFilter("NIFTI", "*.nifti")
            );
		
		viewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    //TODO
                }
            }
        });
		
		likelyTraumaBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				manager.sceneStack.push(manager.sceneID);
				manager.paintScene("likelyTrauma");
			}
		});
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		// Button Positions on UI
		GridPane.setConstraints(viewBtn, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(likelyTraumaBtn, 0, 20, 1, 1, HPos.CENTER, VPos.CENTER);
		
		// add buttons to UI
		contentGrid.getChildren().addAll(viewBtn, likelyTraumaBtn);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100/11);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newPatientInfoBar(stage, manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setCenter(mainGrid);
		
		Scene landingScene = new Scene(layout, 960, 540);
		
		return landingScene;
	}
}