package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * This page will allow for the user to view a CT scan image
 * @author Ty Chase
 * REFERENCES: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
 */
public class ScanVisualizerScene {
		
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		Button likelyTraumaBtn = new Button();
		Button algoVisBtn = new Button();
		Button viewCNNBtn = new Button();
		
		//Create panes to the grids so the elements can fully fill the grid
		BorderPane cnnBPane = new BorderPane();
		Pane cnnPane = new Pane();
		Pane likelyTraumaPane = new Pane();
		BorderPane likelyTraumaBPane = new BorderPane();
		BorderPane algoVisBPane = new BorderPane();
		
		//Temp Chart for proof of concept with dummy data
		LineChart<Number, Number> chart1 = null;
		int[] fileNum = new int[]{100, 200, 300, 400, 500, 600, 700, 800};
		double[] percentages = new double[]{10.5, 15.6, 20.5, 35.6, 48.9, 68.3, 80.1, 92.3};
		NumberAxis xAxis1 = new NumberAxis();
		xAxis1.setLabel("Files analyzed");
		NumberAxis yAxis1 = new NumberAxis();
		yAxis1.setLabel("Percent Accuracy");
		chart1 = new LineChart<Number, Number>(xAxis1, yAxis1);
		chart1.setTitle("Percent Accuracy Increase with More Files Analyzed");
		XYChart.Series series1 = new XYChart.Series();
        series1.setName("Specific Data Points");
		for (int i = 0; i < fileNum.length; ++i) {
			series1.getData().add(new XYChart.Data<Integer, Double>(fileNum[i],percentages[i]));
		}
		chart1.getData().add(series1);
		chart1.setMaxWidth(Double.MAX_VALUE);
		
		//###ADD ELEMENTS TO THEIR GRID LOCATION###
		//Algorithm Cell setup
		algoVisBPane.prefWidthProperty().bind(contentGrid.widthProperty());
		algoVisBtn.setMaxWidth(Double.MAX_VALUE);
		algoVisBPane.setCenter(chart1);
		algoVisBPane.setBottom(algoVisBtn);
		
		//Get dummy image to fill into the grid
		Image filterImage = new Image("resources/TestImage1.jpg");
		ImageView displayCNNImage = new ImageView();
		displayCNNImage.setImage(filterImage);
		ImageView displayLTAImage = new ImageView();
		displayLTAImage.setImage(filterImage);
		
		//Likely Trauma Area cell setup
		displayLTAImage.fitWidthProperty().bind(likelyTraumaPane.widthProperty());
		displayLTAImage.fitHeightProperty().bind(likelyTraumaPane.heightProperty());
		
		likelyTraumaBPane.prefWidthProperty().bind(contentGrid.widthProperty());
		likelyTraumaBtn.setMaxWidth(Double.MAX_VALUE);
		likelyTraumaBPane.setCenter(likelyTraumaPane);
		likelyTraumaBPane.setBottom(likelyTraumaBtn);
		likelyTraumaPane.getChildren().add(displayLTAImage);
		
		//CNN cell setup
		displayCNNImage.fitWidthProperty().bind(cnnPane.widthProperty());
		displayCNNImage.fitHeightProperty().bind(cnnPane.heightProperty());
		
		cnnBPane.prefWidthProperty().bind(contentGrid.widthProperty());
		viewCNNBtn.setMaxWidth(Double.MAX_VALUE);
		cnnBPane.setCenter(cnnPane);
		cnnBPane.setBottom(viewCNNBtn);
		cnnPane.getChildren().add(displayCNNImage);
		
		//###STYLE AND ADD FUNCTION TO BUTTONS
		//Setup buttons on the scene
		viewCNNBtn.setText("CNN Visualizer");
		likelyTraumaBtn.setText("Trauma Area Visualizer");
		algoVisBtn.setText("Algorithm Visualizer");
		
		//Style CNN Button
		viewCNNBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("CNNVisualizer");
			}
		});
		String viewCNNTT = "View the Convolutional Neural Network Visualizer.";
		viewCNNBtn.setTooltip(new Tooltip(viewCNNTT));
		
		//Style LTA button
		likelyTraumaBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("LikelyTraumaAreas");
			}
		});
		String likelyTraumaTT = "View the Likely Trauma Areas Visualizer.";
		likelyTraumaBtn.setTooltip(new Tooltip(likelyTraumaTT));
		
		//Style Algorithm Visualizer button
		algoVisBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				manager.getSceneStack().push(manager.getSceneID());
				manager.paintScene("AlgorithmVisualizer");
			}
		});
		String algoVisTT = "View the Algorithm Visualizer.";
		algoVisBtn.setTooltip(new Tooltip(algoVisTT));
		
		//###LAYOUT CONTENT GRID AND ADD ELEMENTS
		//Construct Content Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		//Size the Columns
		ColumnConstraints contentColumn = new ColumnConstraints(200,200,Double.MAX_VALUE);
		contentColumn.setHgrow(Priority.ALWAYS);
		contentColumn.setPercentWidth(50);
		contentGrid.getColumnConstraints().addAll(contentColumn, contentColumn);
		
		RowConstraints contentRow = new RowConstraints(200, 200, Double.MAX_VALUE);
		contentRow.setPercentHeight(50);
		contentGrid.getRowConstraints().addAll(contentRow, contentRow);
				
		// Position Elements within the UI
		GridPane.setConstraints(likelyTraumaBPane, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		GridPane.setConstraints(algoVisBPane, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		GridPane.setConstraints(cnnBPane, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1);
		
		contentGrid.getChildren().addAll(likelyTraumaBPane, algoVisBPane, cnnBPane);
		contentGrid.getStyleClass().add("content-pane");
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newSideBar(manager);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		layout.setTop(TopMenuBar.newMenuBar(manager));
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
}