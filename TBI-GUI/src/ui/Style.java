package ui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * This is where most of the styling for the application is controlled
 * @author Ty Chase
 */
public class Style {
	
	//Colors---------------------------------------------------------------------------------------
	
	private final static String BUTTON_DEFAULT = " -fx-background-color: #f1fafe;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	private final static String BUTTON_ENTERED = " -fx-background-color: #c1cace;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	private final static String BUTTON_PRESSED = " -fx-background-color: #919a9e;" + 
			"    -fx-background-radius: 5;" + 
			"    -fx-background-insets: 0,1,2;" + 
			"    -fx-text-fill: black;";
	
	private final static String LABEL_STYLE = "-fx-text-fill: #f1fafe;" +
			"	-fx-font-size: 14px;";
	
	private final static String VERTICAL_MENU_COLOR = "-fx-background-color: #455357";
	private final static String SIDE_TEXT_AREA_COLOR = "-fx-control-inner-background: #455357";
	private final static String BACKGROUND_COLOR = "-fx-background-color: #cfd8dc";
	private final static String LANDING_BACKGROUND_COLOR = "-fx-background-color: #455357";
	private final static String SCROLLPANE_BORDER = "-fx-background-color:transparent;";
	
	//Methods--------------------------------------------------------------------------------------
	
	public static void styleButton(Button button) {
		button.setStyle(BUTTON_DEFAULT);
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				button.setStyle(BUTTON_ENTERED);
			}
		});
		button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				button.setStyle(BUTTON_DEFAULT);
			}
		});
		button.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				button.setStyle(BUTTON_PRESSED);
			}
		});
		
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}
	
	public static void stylePane(Pane pane) {
		pane.setStyle(VERTICAL_MENU_COLOR);
	}
	
	public static void styleScrollPane(ScrollPane scrollPane) {
		scrollPane.setStyle(SCROLLPANE_BORDER);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	}
	
	public static void styleTextArea(TextArea textArea) {
		textArea.setStyle(SIDE_TEXT_AREA_COLOR);
		textArea.setWrapText(true);
	}
	
	public static void styleBorderPane(BorderPane borderPane) {
		borderPane.setStyle(BACKGROUND_COLOR);
	}
	
	public static void styleLandingBorderPane(BorderPane borderPane) {
		borderPane.setStyle(LANDING_BACKGROUND_COLOR);
	}
	
	public static void styleLabel(Label label) {
		label.setStyle(LABEL_STYLE);
	}
}