package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import utils.PatientEntry;
import utils.Scan;

/**
 * StateManager for the UI, controls painting scenes to the screen and setting the stage
 * REFERENCES:
 * http://java-buddy.blogspot.com/2014/02/update-javafx-ui-in-scheduled-task-of.html
 * https://stackoverflow.com/questions/12153622/how-to-close-a-javafx-application-on-window-close
 */
public class StateManager {
	private final boolean DEBUG = false; //Manually change this value 
	private StackPane root = new StackPane();
	private Scene scene = new Scene(root, 960, 540);
	private Stage stage;
	private Stack<String> sceneStack;
	private String sceneID = "Landing";
	private PatientEntry patient = null;
	private Scan scan = null;
	private boolean stateBool = false;
	
	private final File defaultTheme = new File ("./src/resources/themes/Dark.css");
	private String themeFile = "file:///" + defaultTheme.getAbsolutePath().replace("\\", "/");
	
	public Stage getStage() {
		return stage;
	}
	public Stack<String> getSceneStack() {
		return sceneStack;
	}
	public Scene getScene() {
		return scene;
	}
	public void setSceneID(String sceneID) {
		this.sceneID = sceneID;
	}
	public String getSceneID() {
		return sceneID;
	}
	public void setPatient(PatientEntry patient) {
		this.patient = patient;
	}
	public PatientEntry getPatient() {
		return patient;
	}
	public void setScan(Scan scan) {
		this.scan = scan;
	}
	public Scan getScan() {
		return scan;
	}
	public void setStateBool(boolean stateBool) {
		this.stateBool = stateBool;
	}
	public boolean getStateBool() {
		return stateBool;
	}
	public void setThemeFile(String fileName) {
		this.themeFile = fileName;
		paintScene(sceneID);
	}
	public String getThemeFile() {
		return themeFile;
	}
	
	// constructor (mostly just the app start)
	public StateManager(Stage inStage) {
		sceneStack = new Stack<String>();
		stage = inStage;
		Image icon = new Image("resources/icon.png");
		stage.getIcons().add(icon);
		stage.setTitle("Traumatic Brain Injury Locator");
		stage.setWidth(960);
		stage.setHeight(540);
		stage.setMinWidth(960);
		stage.setMinHeight(540);
		
		paintScene(sceneID);
		
		stage.show();
	}
	
	/**
	 * Determines which scene to paint to the stage
	 * @param newSceneID: a distinct string indicating which scene will be displayed
	 */
	@SuppressWarnings("static-access")
	public void paintScene(String newSceneID) {			
		this.sceneID = newSceneID;
		if (sceneID.equals("Landing")) {
			this.patient = null;
			scene = new LandingScene().initializeScene(this);
		}
		else if (sceneID.equals("PatientInfoEntry")) {
			this.patient = null;
			scene = new PatientInfoEntryScene().initializeScene(this);
		}
		else if (sceneID.equals("PreviousPatient")) {
			scene = new PreviousPatientScene().initializeScene(this);
		}
		else if (sceneID.equals("ScanVisualizer")) {
			scene = new ScanVisualizerScene().initializeScene(this);
		}
		else if (sceneID.equals("PatientInfo")) {
			scene = new PatientInfoScene().initializeScene(this);
		}
		else if (sceneID.equals("ThemeCreation")) {
			scene = new ThemeCreationScene().initializeScene(this);
		}
		
		stage.setScene(scene);
		stage.getScene().getStylesheets().add(themeFile);
		
		//Kill timers on close of app
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		if (DEBUG) {
			debugStack();
		}
	}
	
	/**
	 * Raises a dialog box with a message for the user
	 * @param dialogTitle: title of the dialog box
	 * @param message: a message to the user
	 */
	public void makeDialog(String message) {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setResizable(false);
		
		Label messLabel = new Label(message);
		messLabel.setMaxSize(300, 500);
		messLabel.setWrapText(true);
		messLabel.autosize();
		messLabel.getStyleClass().add("label-white");
		Button close = new Button("Okay");
		close.setOnAction(e -> dialogStage.close());
		close.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		VBox dialogLayout = new VBox(5);
		GridPane buttonGrid = new GridPane();
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100/3);
		buttonGrid.getColumnConstraints().addAll(columnCon, columnCon, columnCon);
		GridPane.setConstraints(close, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		buttonGrid.getChildren().addAll(close);
		dialogLayout.getChildren().addAll(messLabel, buttonGrid);
		dialogLayout.setAlignment(Pos.CENTER);
		dialogLayout.setPadding(new Insets(40, 40, 40, 40));
		dialogLayout.setSpacing(15);
		dialogLayout.getStyleClass().add("vbox-dialog-box");
		
		Scene dialogScene = new Scene(dialogLayout);
		dialogStage.sizeToScene();
		dialogStage.setScene(dialogScene);
		dialogStage.getScene().getStylesheets().add(themeFile);
		dialogStage.showAndWait();
	}
	
	/**
	 * Raises a dialog box with a question for the user
	 * @param question: a question to the user
	 */
	public boolean makeQuestion(String question) {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setResizable(false);
		
		Label messLabel = new Label(question);
		messLabel.setMaxSize(300, 500);
		messLabel.setWrapText(true);
		messLabel.getStyleClass().add("label-white");
		messLabel.autosize();
		Button yesBtn = new Button("Yes");
		Button noBtn = new Button("No");
		
		class ValueHolder {boolean value;}
		ValueHolder vh = new ValueHolder();
		
		yesBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vh.value = true;
				dialogStage.close();	
			}
		});
		noBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vh.value = false;
				dialogStage.close();	
			}
		});
		
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
		dialogStage.getScene().getStylesheets().add(themeFile);
		dialogStage.showAndWait();
		
		return vh.value;
	}
	
	/**
	 * Raises a dialog box with a message for the user and shows stacktrace
	 * @param dialogTitle: title of the dialog box
	 * @param message: a message to the user
	 * @param ex: exception
	 */
	public void makeError(String message, Exception ex) {
		logError(ex);
		String stack = "";
		for(int i = 0; i < ex.getStackTrace().length; ++i) {
			stack += ex.getStackTrace()[i] + "\n";
		}		
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setResizable(false);
		dialogStage.setWidth(700);
		dialogStage.setHeight(200);
		VBox dialogLayout = new VBox(5);
		
		TitledPane titledPane = new TitledPane();
		titledPane.setText("More Information");
		titledPane.setExpanded(false);
		titledPane.expandedProperty().addListener((observable, oldVal, newVal) -> {
			if(newVal) {
				resizeSmooth(200, 500, dialogStage);
			} else {
				resizeSmooth(500, 200, dialogStage);
			}
		});
		VBox vbox = new VBox();
		TextArea textArea = new TextArea();
		textArea.getStyleClass().add("text-area-dialog");
		textArea.setText(message + "\n\n" + stack);
		textArea.setPrefSize(260, 300);
		textArea.setEditable(false);
		vbox.getChildren().add(textArea);
		titledPane.setContent(vbox);
		
		Label messLabel = new Label(message);
		messLabel.getStyleClass().add("label-white");
		messLabel.setMaxSize(300, 500);
		messLabel.setWrapText(true);
		messLabel.autosize();
		messLabel.setAlignment(Pos.CENTER);
		Button close = new Button("Okay");
		close.setOnAction(e -> dialogStage.close());
		
		GridPane buttonGrid = new GridPane();
		ColumnConstraints columnCon = new ColumnConstraints();
		columnCon.setPercentWidth(100/3);
		buttonGrid.getColumnConstraints().addAll(columnCon, columnCon, columnCon);
		GridPane.setConstraints(close, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		buttonGrid.getChildren().addAll(close);
		dialogLayout.getChildren().addAll(messLabel, titledPane, buttonGrid);
		dialogLayout.setAlignment(Pos.CENTER);
		dialogLayout.setPadding(new Insets(40, 40, 40, 40));
		dialogLayout.setSpacing(15);
		dialogLayout.getStyleClass().add("vbox-dialog-box");
		
		Scene dialogScene = new Scene(dialogLayout);
		dialogStage.setScene(dialogScene);
		dialogStage.getScene().getStylesheets().add(themeFile);
		dialogStage.showAndWait();
	}

	/**
	 * Write the error and time it occurred to a file
	 * @param ex: exception
	 */
	public void logError(Exception exception) {
		PrintWriter writer;		
		try {
			writer = new PrintWriter(new FileOutputStream(new File("./errorlog.txt"), true));
			writer.append("----------------------------------------------------------------------------------------------------\n\n");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
			writer.append("Error occurred at " + dtf.format(LocalDateTime.now()) + "\n\n");
			for(int i = 0; i < exception.getStackTrace().length; ++i) {
				writer.append(exception.getStackTrace()[i] + "\n");
			}
			writer.append("\n");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Resizes a window smoothly height only
	 * @param start: starting height
	 * @param finish: ending height
	 * @param stage: stage that will be resized
	 */
	public void resizeSmooth(double start, double finish, Stage stage) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			double curSize = start;
			
			@Override
			public void run() {
				if(curSize == finish) {
					this.cancel();
				}
				if(start < finish) {
					curSize += 10;
					stage.setHeight(curSize);
				} else {
					curSize -= 10;
					stage.setHeight(curSize);
				}
			}
			
		}, 0, 15);
	}
	
	public static void textMaxLength(final TextField tf, final int maxLength) {
		tf.textProperty().addListener((observable, oldVal, newVal) -> {
			if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
		});
	}
	public static void textMaxLength(final TextArea tf, final int maxLength) {
		tf.textProperty().addListener((observable, oldVal, newVal) -> {
			if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
		});
	}
	
	/* DEBUG CONSOLE OUTPUTS*/
	public void debugStack() {
		System.out.println(sceneID);
		System.out.println(sceneStack.size());
		System.out.println(sceneStack.toString());
		System.out.println(patient.getName() + " " + patient.getUid());
	}
}