package ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import utils.PatientEntry;

/**
 * This page is used to create new themes
 * @author Ty Chase
 * REFERENCES: 
 * https://docs.oracle.com/javafx/2/ui_controls/color-picker.htm
 * https://farrukhobaid.wordpress.com/2013/01/16/dark-scrollbar-skin-for-javafx/
 */
public class ThemeCreationScene {

	public static Scene initializeScene(StateManager manager) {
	
		HashMap<String, String> colors = new HashMap<String, String>();
		
		BorderPane layout = new BorderPane();
		GridPane wholeGrid = new GridPane();
		GridPane mainGrid = new GridPane();
		GridPane previewGrid = new GridPane();
		
		
		RowConstraints wholeRow = new RowConstraints();
		wholeRow.setPercentHeight(100);
		wholeGrid.getRowConstraints().add(wholeRow);
		ColumnConstraints wholeColumn = new ColumnConstraints();
		wholeColumn.setPercentWidth(100);
		wholeGrid.getColumnConstraints().add(wholeColumn);
		
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
		
		//Construct scroll bar
		ScrollPane scrollPane = new ScrollPane(mainGrid);
		scrollPane.getStyleClass().add("scroll-pane");
		wholeGrid.prefWidthProperty().bind(scrollPane.widthProperty());
		
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
		
		//Preview Button
		Button previewButton = new Button("Button");
		previewButton.setMaxWidth(Double.MAX_VALUE);
		previewButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (colors.get("buttonHover") != null) {
					previewButton.setStyle("-fx-background-color: " + colors.get("buttonHover") + ";");
				}
			}
		});
		previewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (colors.get("buttonPress") != null) {
					previewButton.setStyle("-fx-background-color: " + colors.get("buttonPress") + ";");
				}
			}
		});
		previewButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (colors.get("button") != null) {
					previewButton.setStyle("-fx-background-color: " + colors.get("button") + ";");
				}
			}
		});
		
		//Preview Menu
		MenuBar previewMenuBar = new MenuBar();
		Menu previewMenu = new Menu("Menu");		
		MenuItem previewMenuItem = new MenuItem("MenuItem");
		previewMenu.getItems().add(previewMenuItem);
		previewMenuBar.getMenus().add(previewMenu);
		
		//Preview Table
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
		labelPicker.setMaxWidth(Double.MAX_VALUE);
		labelPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("label", toHex(labelPicker.getValue()));
				previewLabel.setStyle("-fx-text-fill: " + colors.get("label") + ";");
			}
		});
		GridPane.setConstraints(labelLabel, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(labelPicker, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(labelLabel, labelPicker);
		
		Label contentPaneLabel = new Label("Content Pane");
		ColorPicker contentPanePicker = new ColorPicker();
		contentPanePicker.setMaxWidth(Double.MAX_VALUE);
		contentPanePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("contentPane", toHex(contentPanePicker.getValue()));
				previewGrid.setStyle("-fx-background-color: " + colors.get("contentPane") + ";");
			}
		});
		GridPane.setConstraints(contentPaneLabel, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(contentPanePicker, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(contentPaneLabel, contentPanePicker);
		
		Label sidePaneLabel = new Label("Side Pane*/Scrollbar*");
		ColorPicker sidePanePicker = new ColorPicker();
		sidePanePicker.setMaxWidth(Double.MAX_VALUE);
		sidePanePicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("sidePane", toHex(sidePanePicker.getValue()));
			}
		});
		GridPane.setConstraints(sidePaneLabel, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(sidePanePicker, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(sidePaneLabel, sidePanePicker);
		
		Label dialogBoxLabel = new Label("Dialog Box*");
		ColorPicker dialogBoxPicker = new ColorPicker();
		dialogBoxPicker.setMaxWidth(Double.MAX_VALUE);
		dialogBoxPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("dialogBox", toHex(dialogBoxPicker.getValue()));
			}
		});
		GridPane.setConstraints(dialogBoxLabel, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(dialogBoxPicker, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(dialogBoxLabel, dialogBoxPicker);
		
		Label buttonLabel = new Label("Button");
		ColorPicker buttonPicker = new ColorPicker();
		buttonPicker.setMaxWidth(Double.MAX_VALUE);
		buttonPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("button", toHex(buttonPicker.getValue()));
				previewButton.setStyle("-fx-background-color: " + colors.get("button") + ";");	
			}
		});
		GridPane.setConstraints(buttonLabel, 0, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(buttonPicker, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(buttonLabel, buttonPicker);
		
		Label buttonHoverLabel = new Label("Button: Hover");
		ColorPicker buttonHoverPicker = new ColorPicker();
		buttonHoverPicker.setMaxWidth(Double.MAX_VALUE);
		buttonHoverPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("buttonHover", toHex(buttonHoverPicker.getValue()));
			}
		});
		GridPane.setConstraints(buttonHoverLabel, 0, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(buttonHoverPicker, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(buttonHoverLabel, buttonHoverPicker);
		
		Label buttonPressLabel = new Label("Button: Press");
		ColorPicker buttonPressPicker = new ColorPicker();
		buttonPressPicker.setMaxWidth(Double.MAX_VALUE);
		buttonPressPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("buttonPress", toHex(buttonPressPicker.getValue()));
			}
		});
		GridPane.setConstraints(buttonPressLabel, 0, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(buttonPressPicker, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(buttonPressLabel, buttonPressPicker);
		
		Label menuTextLabel = new Label("Menu Text*");
		ColorPicker menuTextPicker = new ColorPicker();
		menuTextPicker.setMaxWidth(Double.MAX_VALUE);
		menuTextPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("menuText", toHex(menuTextPicker.getValue()));
				//TODO
			}
		});
		GridPane.setConstraints(menuTextLabel, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuTextPicker, 1, 8, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuTextLabel, menuTextPicker);
		
		Label menuBarLabel = new Label("Menubar");
		ColorPicker menuBarPicker = new ColorPicker();
		menuBarPicker.setMaxWidth(Double.MAX_VALUE);
		menuBarPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("menuBar", toHex(menuBarPicker.getValue()));
				previewMenuBar.setStyle("-fx-background-color: " + colors.get("menuBar") + ";");
			}
		});
		GridPane.setConstraints(menuBarLabel, 0, 9, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuBarPicker, 1, 9, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuBarLabel, menuBarPicker);
		
		Label menuLabel = new Label("Menu");
		ColorPicker menuPicker = new ColorPicker();
		menuPicker.setMaxWidth(Double.MAX_VALUE);
		menuPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("menu", toHex(menuPicker.getValue()));
				previewMenu.setStyle("-fx-background-color: " + colors.get("menu") + ";");
			}
		});
		GridPane.setConstraints(menuLabel, 0, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuPicker, 1, 10, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuLabel, menuPicker);
		
		Label menuItemLabel = new Label("MenuItem");
		ColorPicker menuItemPicker = new ColorPicker();
		menuItemPicker.setMaxWidth(Double.MAX_VALUE);
		menuItemPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("menuItem", toHex(menuItemPicker.getValue()));
				previewMenuItem.setStyle("-fx-background-color: " + colors.get("menuItem") + ";");
			}
		});
		GridPane.setConstraints(menuItemLabel, 0, 11, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(menuItemPicker, 1, 11, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(menuItemLabel, menuItemPicker);
	
		Label tableTextLabel = new Label("Table Text*");
		ColorPicker tableTextPicker = new ColorPicker();
		tableTextPicker.setMaxWidth(Double.MAX_VALUE);
		tableTextPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("tableText", toHex(tableTextPicker.getValue()));
				//TODO
			}
		});
		GridPane.setConstraints(tableTextLabel, 0, 12, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableTextPicker, 1, 12, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableTextLabel, tableTextPicker);
		
		Label tableHeaderLabel = new Label("Table Header*");
		ColorPicker tableHeaderPicker = new ColorPicker();
		tableHeaderPicker.setMaxWidth(Double.MAX_VALUE);
		tableHeaderPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("tableHeader", toHex(tableHeaderPicker.getValue()));
				//TODO
			}
		});
		GridPane.setConstraints(tableHeaderLabel, 0, 13, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableHeaderPicker, 1, 13, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableHeaderLabel, tableHeaderPicker);
		
		Label tableRowEvenLabel = new Label("Table Row Even");
		ColorPicker tableRowEvenPicker = new ColorPicker();
		tableRowEvenPicker.setMaxWidth(Double.MAX_VALUE);
		tableRowEvenPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("tableRowEven", toHex(tableRowEvenPicker.getValue()));
				String style = "-fx-control-inner-background: " + colors.get("tableRowEven") + ";";
				if (colors.get("tableRowOdd") != null) {
					style += "-fx-control-inner-background-alt: " + colors.get("tableRowOdd") + ";";
				}
				if (colors.get("tableRowSelected") != null) {
					style += "-fx-selection-bar: " + colors.get("tableRowSelected") + ";";
				}
				previewTable.setStyle(style);
			}
		});
		GridPane.setConstraints(tableRowEvenLabel, 0, 14, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowEvenPicker, 1, 14, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowEvenLabel, tableRowEvenPicker);
		
		Label tableRowOddLabel = new Label("Table Row Odd");
		ColorPicker tableRowOddPicker = new ColorPicker();
		tableRowOddPicker.setMaxWidth(Double.MAX_VALUE);
		tableRowOddPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("tableRowOdd", toHex(tableRowOddPicker.getValue()));
				String style = "-fx-control-inner-background-alt: " + colors.get("tableRowOdd") + ";";
				if (colors.get("tableRowEven") != null) {
					style += "-fx-control-inner-background: " + colors.get("tableRowEven") + ";";
				}
				if (colors.get("tableRowSelected") != null) {
					style += "-fx-selection-bar: " + colors.get("tableRowSelected") + ";";
				}
				previewTable.setStyle(style);
			}
		});
		GridPane.setConstraints(tableRowOddLabel, 0, 15, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowOddPicker, 1, 15, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowOddLabel, tableRowOddPicker);
		
		Label tableRowOddSelectedLabel = new Label("Table Row: Selected");
		ColorPicker tableRowSelectedPicker = new ColorPicker();
		tableRowSelectedPicker.setMaxWidth(Double.MAX_VALUE);
		tableRowSelectedPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				colors.put("tableRowSelected", toHex(tableRowSelectedPicker.getValue()));
				String style = "-fx-selection-bar: " + colors.get("tableRowSelected") + ";";
				if (colors.get("tableRowEven") != null) {
					style += "-fx-control-inner-background: " + colors.get("tableRowEven") + ";";
				}
				if (colors.get("tableRowOdd") != null) {
					style += "-fx-control-inner-background-alt: " + colors.get("tableRowOdd") + ";";
				}
				previewTable.setStyle(style);
			}
		});
		GridPane.setConstraints(tableRowOddSelectedLabel, 0, 16, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(tableRowSelectedPicker, 1, 16, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().addAll(tableRowOddSelectedLabel, tableRowSelectedPicker);
		
		Label disclaimer = new Label("*Not shown in preview");
		GridPane.setConstraints(disclaimer, 0, 17, 2, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(disclaimer);
		
		//Save---------------------------------------------------------------------------------------------------------------------------------------
		TextField nameField = new TextField();
		nameField.setPromptText("Name your theme");
		GridPane.setConstraints(nameField, 0, 18, 1, 1, HPos.CENTER, VPos.CENTER);
		
		Button saveBtn = new Button("Save");
		saveBtn.setMaxWidth(Double.MAX_VALUE);
		saveBtn.setTooltip(new Tooltip("Save this theme so it can be loaded later."));
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				if (colors.get("label") == null) 
					colors.put("label", toHex(labelPicker.getValue()));
				if (colors.get("contentPane") == null) 
					colors.put("contentPane", toHex(contentPanePicker.getValue()));
				if (colors.get("sidePane") == null) 
					colors.put("sidePane", toHex(sidePanePicker.getValue()));
				if (colors.get("dialogBox") == null) 
					colors.put("dialogBox", toHex(dialogBoxPicker.getValue()));
				if (colors.get("button") == null) 
					colors.put("button", toHex(buttonPicker.getValue()));
				if (colors.get("buttonHover") == null) 
					colors.put("buttonHover", toHex(buttonHoverPicker.getValue()));
				if (colors.get("buttonPress") == null) 
					colors.put("buttonPress", toHex(buttonPressPicker.getValue()));
				if (colors.get("menuText") == null) 
					colors.put("menuText", toHex(menuTextPicker.getValue()));
				if (colors.get("menuBar") == null) 
					colors.put("menuBar", toHex(menuBarPicker.getValue()));
				if (colors.get("menu") == null) 
					colors.put("menu", toHex(menuPicker.getValue()));
				if (colors.get("menuItem") == null) 
					colors.put("menuItem", toHex(menuItemPicker.getValue()));
				if (colors.get("tableText") == null) 
					colors.put("tableText", toHex(tableTextPicker.getValue()));
				if (colors.get("tableHeader") == null) 
					colors.put("tableHeader", toHex(tableHeaderPicker.getValue()));
				if (colors.get("tableRowEven") == null) 
					colors.put("tableRowEven", toHex(tableRowEvenPicker.getValue()));
				if (colors.get("tableRowOdd") == null) 
					colors.put("tableRowOdd", toHex(tableRowOddPicker.getValue()));
				if (colors.get("tableRowSelected") == null) 
					colors.put("tableRowSelected", toHex(tableRowSelectedPicker.getValue()));
				
				if (nameField.getText().isEmpty()) {
					manager.makeDialog("Please give the theme a name.");
				}
				else {
					writeCss(manager, nameField.getText(), colors);
				}
			}
		});
		GridPane.setConstraints(saveBtn, 1, 18, 1, 1, HPos.CENTER, VPos.CENTER);
		
		mainGrid.getChildren().addAll(nameField, saveBtn);
		
		//Add Grid and layout to scene---------------------------------------------------------------------------------------------------------------
		mainGrid.getStyleClass().add("content-pane");
		
		GridPane.setConstraints(previewGrid, 2, 1, 1, 18, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(previewGrid);
		wholeGrid.getChildren().addAll(mainGrid, scrollPane);
		layout.setCenter(wholeGrid);
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
	
	private static void writeCss(StateManager manager, String name, HashMap<String, String> colors) {
		PrintWriter writer;		
		try {
			File file = new File("./src/resources/themes/" + name + ".css");
			
			if (file.exists()) {
				manager.makeDialog("A theme with this name already exists. Choose a different name.");
			} 
			
			else if (name.contains(" ")) {
				manager.makeDialog("Names cannot contain spcaes.");
			}
			
			else {
				writer = new PrintWriter(new FileOutputStream(file));
				writer.append(".label-white { -fx-text-fill: " + colors.get("label") + "; } \n\n");
				writer.append(".content-pane { -fx-background-color: " + colors.get("contentPane") + "; } \n\n");
				writer.append(".side-pane { -fx-background-color: " + colors.get("sidePane") + "; } \n\n");
				writer.append(".vbox-dialog-box { -fx-background-color: " + colors.get("dialogBox") + "; } \n\n");
				writer.append(".button, .color-picker { -fx-background-color: " + colors.get("button") + "; "
						+ "-fx-background-radius: 5; "
						+ "-fx-background-insets: 0,1,2; "
						+ "-fx-text-fill: " + colors.get("label") + "; } \n\n");
				writer.append(".button:hover, .color-picker:hover { -fx-background-color: " + colors.get("buttonHover") + "; "
						+ "-fx-background-radius: 5; "
						+ "-fx-background-insets: 0,1,2; "
						+ "-fx-text-fill: " + colors.get("label") + "; } \n\n");
				writer.append(".button:pressed, .color-picker:pressed { -fx-background-color: " + colors.get("buttonPress") + "; "
						+ "-fx-background-radius: 5; "
						+ "-fx-background-insets: 0,1,2; "
						+ "-fx-text-fill: " + colors.get("label") + "; } \n\n");
				writer.append(".menu-bar { -fx-background-color: " + colors.get("menuBar") + "; "
						+ "-fx-text-fill: " + colors.get("menuText") + "; } \n\n");
				writer.append(".menu .label { -fx-text-fill: " + colors.get("menuText") + "; } \n\n");
				writer.append(".menu:hover { -fx-background-color: " + colors.get("menu") + "; } \n\n");
				writer.append(".menu:showing { -fx-background-color: " + colors.get("menu") + "; } \n\n");
				writer.append(".menu-item .label { -fx-text-fill: " + colors.get("menuText") + "; } \n\n");
				writer.append(".menu-item:focused { -fx-background-color: " + colors.get("menuItem") + "; } \n\n");
				writer.append(".scroll-pane-side { -fx-border-color: " + colors.get("sidePane") + "; "
						+ "-fx-background-color: " + colors.get("sidePane") + "; } \n\n");
				writer.append(".scroll-pane-side .viewport { -fx-border-color: " + colors.get("sidePane") + "; "
						+ "-fx-background-color: " + colors.get("sidePane") + "; } \n\n");
				writer.append(".tranparent-pane { -fx-background-color: transparent; } \n\n");
				writer.append(".text-area-sidebar { -fx-text-fill: " + colors.get("label") + "; "
						+ "-fx-background-color: transparent, transparent, white, transparent; } \n\n");
				writer.append(".text-area-sidebar .content { -fx-background-color: " + colors.get("sidePane") + "; "
						+ "-fx-background-color: transparent, transparent, white, " + colors.get("sidePane") + "; } \n\n");
				writer.append(".table-view { -fx-background-color: transparent; "
						+ "-fx-control-inner-background: " + colors.get("tableRowEven") + "; "
						+ "-fx-control-inner-background-alt: " + colors.get("tableRowOdd") + "; "
						+ "-fx-selection-bar: transparent; "
						+ "-fx-background-insets: 0, 0 0 1 0; "
						+ "-fx-padding: 0.0em; } \n\n");
				writer.append(".table-view .table-cell { -fx-text-fill: " + colors.get("tableText") + "; } \n\n");
				writer.append(".table-row-cell { -fx-background-insets: 0, 0 0 1 0; "
						+ "-fx-padding: 0.0em; } \n\n");
				writer.append(".table-view:focused { -fx-background-color: transparent; } \n\n");
				writer.append(".table-view .column-header-background { -fx-background-color: " + colors.get("tableHeader") + "; } \n\n");
				writer.append(".table-view .column-header-background .label { -fx-background-color: transparent; "
						+ "-fx-text-fill: " + colors.get("tableText") + "; } \n\n");
				writer.append(".table-view .column-header { -fx-background-color: transparent; } \n\n");
				writer.append(".text-area-dialog { -fx-text-fill: " + colors.get("label") + "; } \n\n");
				writer.append(".titled-pane { -fx-text-fill: " + colors.get("label") + "; } \n\n");
				writer.append(".titled-pane .title { -fx-background-color: " + colors.get("contentPane") + "; } \n\n");
				writer.append(".titled-pane .content { -fx-background-color: " + colors.get("sidePane") + "; } \n\n");
				writer.append(".scroll-pane .corner { -fx-background-color: transparent; } \n\n");
				writer.append(".scroll-bar { -fx-background-color: transparent; } \n\n");
				writer.append(".scroll-bar:vertical .thumb, .scroll-bar:horizontal .thumb "
						+ "{ -fx-background-color: " + colors.get("sidePane") + "; } \n\n");
				writer.append(".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button, "
						+ ".scroll-bar:horizontal .increment-button, .scroll-bar:horizontal .decrement-button "
						+ "{ -fx-background-color: transparent; } \n\n");
				writer.append(".scroll-bar:horizontal .increment-arrow, .scroll-bar:vertical .increment-arrow, "
						+ ".scroll-bar:horizontal .decrement-arrow, .scroll-bar:vertical .decrement-arrow "
						+ "{ -fx-background-color: " + colors.get("sidePane") + "; } \n\n");
				
				writer.close();
				
				manager.makeDialog("Theme saved. Select from the preferences menu to use.");
				manager.getSceneStack().clear();
				manager.paintScene("Landing");
			}
			
		} catch (Exception e) {
			manager.makeError("Error saving theme. Your name uses invalid characters.", e);
		}
	}
}