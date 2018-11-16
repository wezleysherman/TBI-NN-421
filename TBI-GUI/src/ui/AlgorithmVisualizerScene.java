package ui;

import java.util.LinkedList;

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
	
	public static class Point {
		int num;
		double percent;
		public Point(int num, double percent) {
			this.num = num;
			this.percent = percent;
		}
	}
	
	public static LinkedList<Point> importData() {
		//TODO import data from the algorithm and update test class with same values
		LinkedList<Point> points = new LinkedList<Point>();
		points.add(new Point(0, 0));
		points.add(new Point(100, 10.5));
		points.add(new Point(200, 15.6));
		points.add(new Point(300, 20.5));
		points.add(new Point(400, 35.6));
		points.add(new Point(500, 48.9));
		points.add(new Point(600, 68.3));
		points.add(new Point(700, 80.1));
		points.add(new Point(750, 83.6));
		points.add(new Point(800, 92.3));
		return points;
	}
	
	public static LineChart<Number, Number> getChart(boolean displayFull) {
		LinkedList<Point> displayPoints = importData();
		int numFiles = displayPoints.getLast().num;
		
		NumberAxis xAxis = new NumberAxis("Files analyzed", 0, numFiles, 100);
		NumberAxis yAxis = new NumberAxis("Percent Accuracy", 0, 100, 10);
		LineChart<Number, Number> resChart = new LineChart<Number, Number>(xAxis, yAxis);
		XYChart.Series series = new XYChart.Series();
		series.setName("Specific Data Points");
        
		if (displayFull) {
        	resChart.setTitle("Percent Accuracy Increase with More Files Analyzed");
			for (int i = 0; i < displayPoints.size(); ++i) {
				series.getData().add(new XYChart.Data<Integer, Double>(displayPoints.get(i).num, displayPoints.get(i).percent));
			}
		}
		else {
        	resChart.setTitle("Last 100 Scans");
        	for (int i = 0; i < displayPoints.size(); ++i) {
        		if (displayPoints.get(i).num > numFiles - 100) {
        			series.getData().add(new XYChart.Data<Integer, Double>(displayPoints.get(i).num, displayPoints.get(i).percent));
        		}
        	}
        }
        
		resChart.getData().add(series);
		
		return resChart;
	}
	
	public static Scene initializeScene(StateManager manager, boolean displayFull) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;
		LineChart<Number, Number> chart = getChart(displayFull);
				
		//Construct content grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);
		
		//Add elements to content grid
		GridPane.setConstraints(chart, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		contentGrid.getChildren().addAll(chart);
		
		//Merge content grid with left nav
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		
		//Return constructed scene
		return new Scene(layout, manager.stage.getWidth(), manager.stage.getHeight());
	}
}