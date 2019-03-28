package ui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * This page will allow for the user to view a CT scan image
 * @author Ty Chase
 * REFERENCES: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
 */
public class ScanVisualizerScene {
	
	@SuppressWarnings({ "static-access" })
	public static Scene initializeScene(StateManager manager) {
		BorderPane layout = new BorderPane();
		GridPane contentGrid = new GridPane();
		GridPane mainGrid;

		Button likelyTraumaBtn = new Button();
		Button algoVisBtn = new Button();
		Button viewCNNBtn = new Button();

		//Loading screen handlers
		Button yesBtn = new Button("Yes");
		Button noBtn = new Button("No");
		Stage dialogStage = new Stage();

		//Create panes to the grids so the elements can fully fill the grid
		BorderPane cnnBPane = new BorderPane();
		Pane cnnPane = new Pane();
		BorderPane accuracyPane = new BorderPane();
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

		//Pie Chart to show accuracy
		StackPane accuracyStack = createArc(35);
		Label informationLabel = new Label("The results of this analysis are " + manager.getScan().getLabelProb() + "% likely.");
		accuracyPane.setCenter(accuracyStack);
		accuracyPane.setAlignment(informationLabel, Pos.CENTER);
		accuracyPane.setBottom(informationLabel);

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
				//Add loading screen to frame
				BorderPane loadingPane = LoadingScene.createLoadingScene(manager);
				GridPane.setConstraints(loadingPane, 0, 0, 2, 2);
				contentGrid.getChildren().add(loadingPane);
				String path = StateManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				if(path.contains("main")) {
					path = path.substring(1, path.length() - 1).replace("/", "\\") + "\\..\\..\\..\\..\\src\\python\\NiftiViewer.py " + manager.getScan().getScan().getAbsolutePath();
				} else {
					path = path.substring(1, path.length() - 1).replace("/", "\\") + "\\..\\src\\python\\NiftiViewer.py " + manager.getScan().getScan().getAbsolutePath();
				}

				try {
					Process p = Runtime.getRuntime().exec("python -i " + path);
					Timer timer = new Timer();

					TimerTask timerTask = new TimerTask() {

						@Override
						public void run() {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										dialogStage.initModality(Modality.APPLICATION_MODAL);
										dialogStage.initStyle(StageStyle.UNDECORATED);
										dialogStage.setResizable(false);
									} catch(IllegalStateException e) {

									}

									Label messLabel = new Label("It is taking longer than expected to open the viewer. You may not have the proper python modules installed. Check the niftiviewer documentation for more information on what you need to run it.\n\n"
											+ "Cancel opening the viewer?");
									messLabel.setMaxSize(300, 500);
									messLabel.setWrapText(true);
									messLabel.getStyleClass().add("label-white");
									messLabel.autosize();

									VBox dialogLayout = new VBox(5);
									GridPane buttonGrid = new GridPane();
									ColumnConstraints columnCon = new ColumnConstraints();
									columnCon.setPercentWidth(100/5);
									buttonGrid.getColumnConstraints().addAll(columnCon, columnCon, columnCon, columnCon, columnCon);
									GridPane.setConstraints(yesBtn, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
									GridPane.setConstraints(noBtn, 3, 0, 1, 1, HPos.CENTER, VPos.CENTER);
									buttonGrid.getChildren().addAll(yesBtn, noBtn);
									dialogLayout.getChildren().addAll(messLabel, buttonGrid);
									dialogLayout.setAlignment(Pos.CENTER);
									dialogLayout.setPadding(new Insets(40, 40, 40, 40));
									dialogLayout.setSpacing(15);
									dialogLayout.getStyleClass().add("vbox-dialog-box");

									Scene dialogScene = new Scene(dialogLayout);
									dialogStage.sizeToScene();
									dialogStage.setScene(dialogScene);
									dialogStage.getScene().getStylesheets().add(manager.getThemeFile());
									try {
										dialogStage.showAndWait();
									} catch(IllegalStateException e) {

									}
								}
							});  
						}

					};
					timer.schedule(timerTask, 10 * 1000, 10 * 1000);
					yesBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							contentGrid.getChildren().remove(loadingPane);
							p.destroy();
							timer.cancel();
							timer.purge();
							dialogStage.close();	
						}
					});
					noBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							dialogStage.close();
						}
					});
					//Remove Loading screen once the python file is opened.
					Task launch = new Task() {
						@Override
						protected Object call() throws Exception {
							ServerSocket server = null;
							Socket client = null;
							try {
								server = new ServerSocket(8080);
								client = server.accept();
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										timer.cancel();
										timer.purge();
										contentGrid.getChildren().remove(loadingPane);
									}
								});  
								server.close();
								client.close();
							} catch (IOException e) {
								e.printStackTrace();
								try {
									server.close();
									client.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								System.out.println("Closing connection socket");
							}    
							return null;
						}};
						new Thread(launch).start();
				} catch (IOException ex) {
					contentGrid.getChildren().remove(loadingPane);
					manager.makeDialog("Python isn't installed! Make sure to go through the niftiviewer documentation to make sure you have everything needed.");
				}

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
		GridPane.setConstraints(accuracyPane, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1);

		contentGrid.getChildren().addAll(likelyTraumaBPane, algoVisBPane, cnnBPane, accuracyPane);
		contentGrid.getStyleClass().add("content-pane");

		//Merge Vertical Side Menu and Content
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		mainGrid.getChildren().add(contentGrid);
		layout.setCenter(mainGrid);
		layout.setTop(TopMenuBar.newMenuBar(manager));

		//Return constructed scene
		return new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
	}
	
	//Create Arc to display as percentage with animation
	public static StackPane createArc(double percent) {
		Label percentLabel = new Label(percent + "%");
		percentLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
		//Arc for placing in the center
		Arc arc2 = new Arc();
		arc2.setStartAngle(90);
		arc2.setRadiusX(50);
		arc2.setRadiusY(50);
		arc2.setLength(350);
		arc2.setType(ArcType.ROUND);
		arc2.setStyle("-fx-fill: #cfd8dc");
		Arc arc = new Arc();
		arc.setStartAngle(90);
		arc.setRadiusX(50);
		arc.setRadiusY(50);
		arc.setLength(0);
		arc.setType(ArcType.ROUND);
		arc.setStyle("-fx-fill: #455357");
		StackPane stackPane = new StackPane();
		Group arcGroup = new Group(arc2, arc);
		stackPane.getChildren().addAll(arcGroup, percentLabel);
				
		double endAngle = percent / 100 * 360;
		//Animation
		KeyValue kvStart = new KeyValue(arc.startAngleProperty(), 90);
		KeyValue kvPause = new KeyValue(arc.lengthProperty(), 1);
		KeyValue kvEnd = new KeyValue(arc.lengthProperty(), endAngle);
		
		KeyFrame fillArc = new KeyFrame(Duration.seconds(1.5), kvStart, kvEnd);
		KeyFrame pauseFrame = new KeyFrame(Duration.seconds(.5), kvStart, kvPause);
		Timeline timeline = new Timeline(pauseFrame, fillArc);
		timeline.setCycleCount(1);

		timeline.play();
		return stackPane;	
	}
}