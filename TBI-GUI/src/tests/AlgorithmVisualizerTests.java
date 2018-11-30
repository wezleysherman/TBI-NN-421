package tests;

import ui.App;
import ui.AlgorithmVisualizerScene;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import ui.StateManager;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/**
 * Tests for the Algorithm Visualizer page
 * @author Ty Chase
 * REFERENCES: https://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
 */
public class AlgorithmVisualizerTests {
	
	@Test
	public void test() throws InterruptedException { //Run the App
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Stage stage = new Stage();
						new App().start(stage);
						AlgorithmVisualizerScene.initializeScene(new StateManager(stage));
					}
				});
			}
		});
		thread.start();
		Thread.sleep(3000); //let run for 3 seconds
		Thread.currentThread().interrupt(); //kill thread
		
		/*
		//TODO change to the actual values from the algorithm when we have that
		for (int i = 0; i < AlgorithmVisualizerScene.fileNum.length; ++i)
			assertEquals(AlgorithmVisualizerScene.chart1.getData().get(0).getData().get(i).getXValue(), AlgorithmVisualizerScene.fileNum[i]);
		for (int i = 0; i < AlgorithmVisualizerScene.percentages.length; ++i)
			assertEquals(AlgorithmVisualizerScene.chart1.getData().get(0).getData().get(i).getYValue(), AlgorithmVisualizerScene.percentages[i]);
		assert(AlgorithmVisualizerScene.chart1.getTitle().equals("Percent Accuracy Increase with More Files Analyzed"));
		assert(AlgorithmVisualizerScene.chart1.getData().get(0).getName().equals("Specific Data Points"));
		*/
	}
}