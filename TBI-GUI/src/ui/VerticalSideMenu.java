package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import java.util.EmptyStackException;

public class VerticalSideMenu {
	
	private final static String VERTICAL_MENU_COLOR = "-fx-background-color: #455357";
	
	public static GridPane newSideBar(StateManager manager) {
		GridPane mainGrid = new GridPane();
		GridPane contentGrid = new GridPane();
		Button backBtn = new Button("BACK");
		Button homeBtn = new Button("HOME");
		Button algoVisBtn = new Button ("Algorithm Visualizer");
		Pane colorPane = new Pane();
		
		// Home Button Dialog SetUp TODO: Find a way to center the yes/no buttons
		Dialog<ButtonType> homeWarning = new Dialog<ButtonType>();
		homeWarning.setTitle("Warning!");
		homeWarning.setHeaderText("Returning to the home page will cause you to lose all unsaved information!");
		homeWarning.setResizable(false);
		GridPane hwPane = new GridPane();
		Label hwLabel = new Label("Continue?");
		hwPane.add(hwLabel, 1, 0);
		hwPane.setAlignment(Pos.CENTER);
		ButtonType hwConfirm = new ButtonType("Yes", ButtonData.YES);
		ButtonType hwDecline = new ButtonType("No", ButtonData.NO);
		homeWarning.getDialogPane().setContent(hwPane);
		homeWarning.getDialogPane().getButtonTypes().addAll(hwConfirm, hwDecline);	
		
		//Button Handling/Tooltips
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					manager.paintScene(manager.sceneStack.pop());
				}
				catch (EmptyStackException ex) {
					manager.paintScene("landing");
					/* if this message prints, the implementation of a previous page is probably missing a push operation.
					 * this makes the code safe for master and general testing, but makes problems less obvious during development (watch for console messages)
					 */
					System.out.println(ex + " Something wrong with stack implementation, returning to landing page.");
				}
			}
		});
		String backBtnTT = "Return to the previous page (You will lose any information you input on this page).";
		backBtn.setTooltip(new Tooltip(backBtnTT));
		
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeWarning.showAndWait();
				if (homeWarning.getResult().getButtonData().equals(ButtonData.YES)) {
					manager.sceneStack.clear();
					manager.paintScene("landing");
				}
			}
		});
		String homeBtnTT = "Return to the home page (You will lose any unsaved information from this run of the program).";
		homeBtn.setTooltip(new Tooltip(homeBtnTT));
		
		algoVisBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!manager.sceneID.equals("algoVis")) {
					manager.sceneStack.push(manager.sceneID);
					manager.paintScene("algoVis");
				}
			}
		});
		
		//Construct content grid
		contentGrid.setHgap(5);
		contentGrid.setVgap(5);
		contentGrid.setPadding(new Insets(5, 5, 5, 5));
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(50);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(50);
		contentGrid.getColumnConstraints().addAll(column0, column1);
		
		//Add elements to content grid
		backBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		homeBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		algoVisBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(backBtn, 0, 0, 1, 1, HPos.LEFT, VPos.TOP);
		GridPane.setConstraints(homeBtn,  1, 0, 1, 1, HPos.LEFT, VPos.TOP);
		GridPane.setConstraints(algoVisBtn, 0, 1, 2, 1, HPos.LEFT, VPos.TOP);
		contentGrid.getChildren().addAll(backBtn, homeBtn, algoVisBtn);
		
		//Construct main grid
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		mainGrid.getRowConstraints().add(rowCon);
		
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100/5);
		mainGrid.getColumnConstraints().add(0, columnCon);
		
		ColumnConstraints columnCon2 = new ColumnConstraints();
		columnCon2.setPercentWidth(400/5);
		mainGrid.getColumnConstraints().add(1, columnCon2);
		
		//Merge content grid with main grid
		colorPane.setStyle(VERTICAL_MENU_COLOR);
		GridPane.setConstraints(colorPane, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(contentGrid, 0, 0, 1, 1, HPos.CENTER, VPos.TOP);
		mainGrid.getChildren().addAll(colorPane, contentGrid);
		
		return mainGrid;
	}
}
