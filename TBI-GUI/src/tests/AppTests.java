package tests;

import ui.App;

import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/**
 * Tests for the main App
 * @author Ty Chase
 * REFERENCES: https://stackoverflow.com/questions/18429422/basic-junit-test-for-javafx-8
 */
public class AppTests {
	@Test
	public void test() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new App().start(new Stage());
					}
				});
			}
		});
		thread.start();
		Thread.currentThread().interrupt();
	}
}