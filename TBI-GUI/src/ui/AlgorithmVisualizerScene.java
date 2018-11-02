package ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * This page will show graphs of the correctness of the algorithm
 * @author Ty Chase
 * REFERENCES: https://docs.oracle.com/javafx/2/charts/line-chart.htm#CIHGBCFI
 */
public class AlgorithmVisualizerScene {
	
	public static LineChart<Number, Number> chart1 = null;
	//Dummy Data //TODO import data from the algorithm and update test class with same values
	public static int[] fileNum = new int[]{100, 200, 300, 400, 500, 600, 700, 800};
	public static double[] percentages = new double[]{10.5, 15.6, 20.5, 35.6, 48.9, 68.3, 80.1, 92.3};

	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		
		//Chart 1 Creation
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
				
		//Construct Grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		
		GridPane.setConstraints(chart1, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		contentGrid.getChildren().addAll(chart1);
		
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newPatientInfoBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		mainGrid.getChildren().add(contentGrid);
		
		layout.setCenter(mainGrid);
		
		Scene scene = new Scene(layout, manager.stage.getWidth(), manager.stage.getHeight());
		
		return scene;
	}
}