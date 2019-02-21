package ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * This page will allow for the user to view likely trauma areas within the selected CT scan image
 */
public class LikelyTraumaAreasScene {
	static ServerSocket servSock = null;
	static Socket clientSock;
	static PrintWriter toViewer;
	static GridPane contentGrid;

	//TODO: File input from the viewScan page
	public static Scene initializeScene(StateManager manager/*, File in*/) {

		// declaration of UI elements
		BorderPane layout = new BorderPane();
		contentGrid = new GridPane();
		GridPane mainGrid;
		Label connectionLabel = new Label("Opening Viewer...");

		// constructing grid
		contentGrid.setPadding(new Insets(10, 10, 10, 10));
		contentGrid.setVgap(15);
		contentGrid.setHgap(10);

		// grid restraints
		RowConstraints rowCon = new RowConstraints();
		rowCon.setPercentHeight(100);
		contentGrid.getRowConstraints().add(rowCon);
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100);
		contentGrid.getColumnConstraints().add(columnCon);

		// adding side menu and content to the main scene
		mainGrid = VerticalSideMenu.newSideBar(manager);
		GridPane.setConstraints(contentGrid, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setConstraints(connectionLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);

		// content grid gets added to the displayed grid
		mainGrid.getChildren().add(contentGrid);
		contentGrid.getChildren().add(connectionLabel);

		// displayed grid is set onto the layout for the scene
		layout.setCenter(mainGrid);
		layout.setTop(TopMenuBar.newMenuBar(manager));

		// scene is created using the layout
		Scene likelyTraumaAreasScene = new Scene(layout, manager.getStage().getWidth(), manager.getStage().getHeight());
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
							connectionLabel.setText("Opened!");
						}
					});  
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
					try {
						server.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Closing connection socket");
				}    
				return null;
			}};
			new Thread(launch).start();
			return likelyTraumaAreasScene;
	}
}