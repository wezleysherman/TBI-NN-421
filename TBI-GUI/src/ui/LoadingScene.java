package ui;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingScene {

	//TODO: File input from the viewScan page
	public static BorderPane createLoadingScene(StateManager manager/*, File in*/) {

		// declaration of UI elements
		BorderPane layout = new BorderPane();
		StackPane content = new StackPane();
		layout.setStyle("-fx-background-color: rgba(255, 255, 255, .5);");
		content.setAlignment(Pos.CENTER);
		
		//Create path and elements to move on the path
		Circle circlePath = new Circle(80);
		Circle circle = new Circle(10);
		circle.setFill(Color.rgb(69, 83, 87));
		Circle circle2 = new Circle(10);
		circle2.setFill(Color.RED);
		Circle circle3 = new Circle(10);
		circle3.setFill(Color.rgb(69, 83, 87));
		Circle circle4 = new Circle(10);
		circle4.setFill(Color.rgb(69, 83, 87));

		//set how the elements will move on the path and how long it takes
		PathTransition pt1 = new PathTransition();
		pt1.setDuration(Duration.millis(2000));
		pt1.setPath(circlePath);
		pt1.setNode(circle);
		pt1.setCycleCount(Timeline.INDEFINITE);
		
		PathTransition pt2 = new PathTransition();
		pt2.setDuration(Duration.millis(2000));
		pt2.setPath(circlePath);
		pt2.setNode(circle2);
		pt2.setCycleCount(Timeline.INDEFINITE);

		PathTransition pt3 = new PathTransition();
		pt3.setDuration(Duration.millis(2000));
		pt3.setPath(circlePath);
		pt3.setNode(circle3);
		pt3.setCycleCount(Timeline.INDEFINITE);
		
		PathTransition pt4 = new PathTransition();
		pt4.setDuration(Duration.millis(2000));
		pt4.setPath(circlePath);
		pt4.setNode(circle4);
		pt4.setCycleCount(Timeline.INDEFINITE);
		
		//set when elements make the moves on the path
		Timeline loadingAnimation1 = new Timeline(
				new KeyFrame(Duration.seconds(0), event -> pt1.play()),
				new KeyFrame(Duration.seconds(2), event -> pt1.pause()),
				new KeyFrame(Duration.seconds(3), event -> pt1.pause())
		);
		Timeline loadingAnimation2 = new Timeline(
				new KeyFrame(Duration.seconds(0), event -> pt2.pause()),
				new KeyFrame(Duration.seconds(.25), event -> pt2.play()),
				new KeyFrame(Duration.seconds(2.25), event -> pt2.pause()),
				new KeyFrame(Duration.seconds(3), event -> pt2.play())
		);
		Timeline loadingAnimation3 = new Timeline(
				new KeyFrame(Duration.seconds(0), event -> pt3.pause()),
				new KeyFrame(Duration.seconds(.5), event -> pt3.play()),
				new KeyFrame(Duration.seconds(2.5), event -> pt3.pause()),
				new KeyFrame(Duration.seconds(3), event -> pt3.play())
		);
		Timeline loadingAnimation4 = new Timeline(
				new KeyFrame(Duration.seconds(0), event -> pt4.pause()),
				new KeyFrame(Duration.seconds(.75), event -> pt4.play()),
				new KeyFrame(Duration.seconds(2.75), event -> pt4.pause()),
				new KeyFrame(Duration.seconds(3), event -> pt4.play())
		);
				
		loadingAnimation1.setCycleCount(Timeline.INDEFINITE);
		loadingAnimation2.setCycleCount(Timeline.INDEFINITE);
		loadingAnimation3.setCycleCount(Timeline.INDEFINITE);
		loadingAnimation4.setCycleCount(Timeline.INDEFINITE);
		
		//Play the animation
		loadingAnimation1.play();
		loadingAnimation2.play();
		loadingAnimation3.play();
		loadingAnimation4.play();
		
		//Add some information on the loading screen
		Circle backgroundCircle = new Circle(60);
		backgroundCircle.setFill(Color.rgb(69, 83, 87));
		Label opening = new Label("Opening Viewer");
		opening.getStyleClass().add("label-white");

		StackPane.setAlignment(circle, Pos.CENTER);
		StackPane.setAlignment(circle2, Pos.CENTER);
		StackPane.setAlignment(circle3, Pos.CENTER);
		StackPane.setAlignment(circle4, Pos.CENTER);
		StackPane.setAlignment(opening, Pos.CENTER);
		
		content.getChildren().addAll(circle, circle2, circle3, circle4, backgroundCircle, opening);
		layout.setCenter(content);
	
		return layout;
	}
}
