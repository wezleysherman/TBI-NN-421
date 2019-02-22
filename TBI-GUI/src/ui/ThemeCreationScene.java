package ui;

import java.util.EmptyStackException;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import utils.PatientEntry;

/**
 * This page is used to create new themes
 * @author Ty Chase
 * REFERENCES: https://docs.oracle.com/javafx/2/ui_controls/color-picker.htm
 */
public class ThemeCreationScene {

	public static Scene initializeScene(StateManager manager) {
		
		BorderPane layout = new BorderPane();
		GridPane mainGrid = new GridPane();
		GridPane previewGrid = new GridPane();
		
		//Construct main grid
		mainGrid.setPadding(new Insets(10, 10, 10, 10));
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(15);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(15);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(70);
		mainGrid.getColumnConstraints().addAll(column0, column1, column2);
		
		/*
		ScrollPane scrollPane = new ScrollPane(mainGrid);
		scrollPane.getStyleClass().add("scroll-pane");
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		mainGrid.prefWidthProperty().bind(scrollPane.widthProperty());
		GridPane.setConstraints(scrollPane, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(scrollPane);
		*/
		
		//Construct preview grid
		previewGrid.setPadding(new Insets(10, 10, 10, 10));
		previewGrid.setHgap(10);
		previewGrid.setVgap(10);
		ColumnConstraints pColumn0 = new ColumnConstraints();
		pColumn0.setPercentWidth(100);
		previewGrid.getColumnConstraints().addAll(pColumn0);
		
		//Preview------------------------------------------------------------------------------------------------------------------------------------
		
		Label previewTitle = new Label("Preview");
		previewTitle.setStyle("-fx-font-weight: bold;");
		GridPane.setConstraints(previewTitle, 2, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(previewTitle);
		
		Label previewLabel = new Label("Label");
		Button previewButton = new Button("Button");
		previewButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		MenuBar previewMenuBar = new MenuBar();
		Menu previewMenu = new Menu("Menu");
		MenuItem previewMenuItem = new MenuItem("MenuItem");
		previewMenu.getItems().add(previewMenuItem);
		previewMenuBar.getMenus().add(previewMenu);
		
		ObservableList<PatientEntry> previewList = FXCollections.observableArrayList();
		for (int i = 0; i < 5; ++i) {
			previewList.add(new PatientEntry("text", null, null));
		}
		TableView<PatientEntry> previewTable = new TableView<PatientEntry>();
		TableColumn previewColumn = new TableColumn("Column");
		previewColumn.setCellValueFactory(new PropertyValueFactory<PatientEntry, String>("name"));
		previewTable.setEditable(false);
		previewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		previewTable.getColumns().add(previewColumn);
		previewTable.setItems(previewList);
		
		GridPane.setConstraints(previewMenuBar, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(previewLabel, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(previewButton, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(previewTable, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		previewGrid.getChildren().addAll(previewLabel, previewButton, previewTable, previewMenuBar);
		
		//Back---------------------------------------------------------------------------------------------------------------------------------------
		Button backBtn = new Button("Back");
		backBtn.setMaxWidth(Double.MAX_VALUE);
		backBtn.setTooltip(new Tooltip("Return to the previous page (You will lose any information you input on this page)."));
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					manager.paintScene(manager.getSceneStack().pop());
				}
				catch (EmptyStackException e) {
					manager.paintScene("Landing");
					manager.makeError("Back operation failed. sceneStack in ui.StateManager is empty and cannot be popped.", e);
				}
			}
		});
		GridPane.setConstraints(backBtn, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(backBtn);
		
		//Color Pickers------------------------------------------------------------------------------------------------------------------------------
		
		Label labelLabel = new Label("Label");
		ColorPicker labelPicker = new ColorPicker();
		labelPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				previewLabel.setStyle("-fx-text-fill: " + toHex(labelPicker.getValue()) + ";");
			}
		});
		GridPane.setConstraints(labelLabel, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(labelPicker, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(labelLabel, labelPicker);
		
		Label contentPaneLabel = new Label("Content Pane");
		ColorPicker contentPanePicker = new ColorPicker();
		contentPanePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				previewGrid.setStyle("-fx-background-color: " + toHex(contentPanePicker.getValue()) + ";");
			}
		});
		GridPane.setConstraints(contentPaneLabel, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(contentPanePicker, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(contentPaneLabel, contentPanePicker);
		
		Label sidePaneLabel = new Label("Side Pane");
		ColorPicker sidePanePicker = new ColorPicker();
		GridPane.setConstraints(sidePaneLabel, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(sidePanePicker, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(sidePaneLabel, sidePanePicker);
		
		Label buttonLabel = new Label("Button");
		ColorPicker buttonPicker = new ColorPicker();
		buttonPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				previewButton.setStyle("-fx-background-color: " + toHex(buttonPicker.getValue()) + ";");
			}
		});
		GridPane.setConstraints(buttonLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(buttonPicker, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(buttonLabel, buttonPicker);
		
		Label buttonHoverLabel = new Label("Button: Hover");
		ColorPicker buttonHoverPicker = new ColorPicker();
		GridPane.setConstraints(buttonHoverLabel, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(buttonHoverPicker, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(buttonHoverLabel, buttonHoverPicker);
		
		Label buttonPressLabel = new Label("Button: Press");
		ColorPicker buttonPressPicker = new ColorPicker();
		GridPane.setConstraints(buttonPressLabel, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(buttonPressPicker, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(buttonPressLabel, buttonPressPicker);
		
		Label menuBarLabel = new Label("Top Menubar");
		ColorPicker menuBarPicker = new ColorPicker();
		menuBarPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				previewMenuBar.setStyle("-fx-background-color: " + toHex(menuBarPicker.getValue()) + ";");
			}
		});
		GridPane.setConstraints(menuBarLabel, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuBarPicker, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuBarLabel, menuBarPicker);
		
		Label menuLabelLabel = new Label("Menu Label");
		ColorPicker menuLabelPicker = new ColorPicker();
		GridPane.setConstraints(menuLabelLabel, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuLabelPicker, 1, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuLabelLabel, menuLabelPicker);
		
		Label menuHoverLabel = new Label("Menu: Hover");
		ColorPicker menuHoverPicker = new ColorPicker();
		GridPane.setConstraints(menuHoverLabel, 0, 9, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuHoverPicker, 1, 9, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuHoverLabel, menuHoverPicker);
		
		Label menuShowingLabel = new Label("Menu: Showing");
		ColorPicker menuShowingPicker = new ColorPicker();
		GridPane.setConstraints(menuShowingLabel, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuShowingPicker, 1, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuShowingLabel, menuShowingPicker);
		
		Label menuItemLabelLabel = new Label("Menu Item Label");
		ColorPicker menuItemLabelPicker = new ColorPicker();
		GridPane.setConstraints(menuItemLabelLabel, 0, 11, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuItemLabelPicker, 1, 11, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuItemLabelLabel, menuItemLabelPicker);
		
		Label menuItemFocusedLabel = new Label("Menu Item: Focused");
		ColorPicker menuItemFocusedPicker = new ColorPicker();
		GridPane.setConstraints(menuItemFocusedLabel, 0, 12, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuItemFocusedPicker, 1, 12, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuItemFocusedLabel, menuItemFocusedPicker);
		
		Label dialogBoxLabel = new Label("Dialog Box");
		ColorPicker dialogBoxPicker = new ColorPicker();
		GridPane.setConstraints(dialogBoxLabel, 0, 13, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(dialogBoxPicker, 1, 13, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(dialogBoxLabel, dialogBoxPicker);
		
		Label tableTextLabel = new Label("Table Text");
		ColorPicker tableTextPicker = new ColorPicker();
		GridPane.setConstraints(tableTextLabel, 0, 14, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableTextPicker, 1, 14, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableTextLabel, tableTextPicker);
		
		Label tableRowEvenLabel = new Label("Table Row Even");
		ColorPicker tableRowEvenPicker = new ColorPicker();
		GridPane.setConstraints(tableRowEvenLabel, 0, 15, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowEvenPicker, 1, 15, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowEvenLabel, tableRowEvenPicker);
		
		Label tableRowEvenSelectedLabel = new Label("Table Row Even: Selected");
		ColorPicker tableRowEvenSelectedPicker = new ColorPicker();
		GridPane.setConstraints(tableRowEvenSelectedLabel, 0, 16, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowEvenSelectedPicker, 1, 16, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowEvenSelectedLabel, tableRowEvenSelectedPicker);
		
		Label tableRowOddLabel = new Label("Table Row Odd");
		ColorPicker tableRowOddPicker = new ColorPicker();
		GridPane.setConstraints(tableRowOddLabel, 0, 17, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowOddPicker, 1, 17, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowOddLabel, tableRowOddPicker);
		
		Label tableRowOddSelectedLabel = new Label("Table Row Odd: Selected");
		ColorPicker tableRowOddSelectedPicker = new ColorPicker();
		GridPane.setConstraints(tableRowOddSelectedLabel, 0, 18, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowOddSelectedPicker, 1, 18, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowOddSelectedLabel, tableRowOddSelectedPicker);
		
		//Save---------------------------------------------------------------------------------------------------------------------------------------
		TextField nameField = new TextField();
		nameField.setPromptText("Name your theme");
		GridPane.setConstraints(nameField, 0, 19, 1, 1, HPos.CENTER, VPos.CENTER);
		
		Button saveBtn = new Button("Save");
		saveBtn.setMaxWidth(Double.MAX_VALUE);
		saveBtn.setTooltip(new Tooltip("Save this theme so it can be loaded later."));
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//TODO
			}
		});
		GridPane.setConstraints(saveBtn, 1, 19, 1, 1, HPos.CENTER, VPos.CENTER);
		
		mainGrid.getChildren().addAll(nameField, saveBtn);
		
		//Add Grid and layout to scene---------------------------------------------------------------------------------------------------------------
		//layout.getStyleClass().add("side-pane");
		GridPane.setConstraints(previewGrid, 2, 1, 1, mainGrid.getRowCount() - 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(previewGrid);
		layout.setCenter(mainGrid);
		layout.setTop(TopMenuBar.newMenuBar(manager));
		
		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
	
	/** 
	 * REFERNECES:
	 * http://www.javacreed.com/how-to-get-the-hex-value-from-color/
	 * https://stackoverflow.com/questions/39078651/how-to-convert-javafx-scene-paint-color-to-java-awt-color
	 */
	private static String toHex(Color color) {
		java.awt.Color awt = new java.awt.Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue());
		String hex = Integer.toHexString(awt.getRGB() & 0xffffff);
		if (hex.length() < 6) {
			hex = "000000".substring(0, 6 - hex.length()) + hex;
		}
		return "#" + hex;
	}
}