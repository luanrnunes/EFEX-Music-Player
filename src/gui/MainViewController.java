package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemFile;

	@FXML
	private MenuItem menuItemConfig;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private Button playButton, stopButton, playListButton, pauseButton, previousButton, nextButton;
	

	@FXML
	private Stage stage;

	@FXML
	private Label actionStatus;

	@FXML
	private Label mediaTime;
	
	@FXML
	private TextField timesRepeat;
	
	@FXML
	private CheckBox repeat;
	
	@FXML
	private ComboBox<String> speedSelector;
	
	@FXML
	private Slider volumeSlider;
	
	@FXML 
	private ProgressBar songProgress;
	
	private File directory;
	
	private File [] files;
	
	private ArrayList<File> songs;
	
	private int songCounter;
	
	private int [] speeds = {25, 50, 75, 100, 125, 150, 175, 200};
	
	private Boolean repeatState = false;

	private String fullPath;

	private MediaPlayer mediaPlayer;

	private Media hit;
	
	private String selectedAudio;
	
	private File selectedFile; 
	
	private String repeatset;
	
	private Integer repeatSetInt;
	
	private Timer timer;
	
	private boolean isRunning;

	@FXML
	public void onMenuItemFileAction() {
		loadView("/gui/ItemView.fxml", (FileViewController controller) -> {

			System.out.println("ok");
		});
	}

	@FXML
	public void onMenuItemConfig() {
		loadView("/gui/Item.fxml", (ConfigViewController controller) -> {
			System.out.println("ok");
		});
	}

	@FXML
	public void onbtPlayButtonAction() {
		try {
		

			if (mediaPlayer == null) {
				actionStatus.setTextFill(Color.web("#000dff"));
				selectedAudio = fullPath;
				hit = new Media(new File(selectedAudio).toURI().toString());
				mediaPlayer = new MediaPlayer(hit);
				mediaPlayer.play();
				/*Duration durMilli = mediaPlayer.getCurrentTime();
				String durMilliStr = durMilli.toString();
				Double durDouble = Double.parseDouble(durMilliStr);
				Double converMilli = (durDouble/1000) / 60;*/
				mediaTime.setText(mediaPlayer.getCurrentTime()+" / "+mediaPlayer.getCycleDuration());
			} else {
				if (mediaPlayer != null) {
					actionStatus.setTextFill(Color.web("#000dff"));
					mediaPlayer.play();
					mediaTime.setText(mediaPlayer.getCurrentTime()+" / "+mediaPlayer.getCycleDuration());
				}
				if (repeatset != null || repeatset != "" ) {
					setTimesRepeat();
				}
			}
		}
		 catch (Exception e) {
			 if (mediaPlayer != null) {
			actionStatus.setText("Now playing: " + selectedFile.getName());
			System.out.println(e.getMessage());
			 }
			 else {
				 actionStatus.setTextFill(Color.web("#FF0000"));
				 actionStatus.setText("No song selected!");
			 }
		}
	}
		
		public void setTimesRepeat () {
		
			repeatset = timesRepeat.getText();
			repeatSetInt = Integer.parseInt(repeatset);
		
			
		if (repeatState == true) {
			for (int i=1; i <= repeatSetInt; i++);
			mediaPlayer.play();
			mediaPlayer.stop();
			System.out.println("Entrou na repeatState");
		}
		
		}
	

	@FXML
	public void onbtStopButtonAction() {

		mediaPlayer.stop();
		mediaTime.setText((mediaPlayer.getCurrentTime()+" / "+mediaPlayer.getCycleDuration()));
		actionStatus.setTextFill(Color.web("#FF0000"));
		actionStatus.setText("Now stopped: " + selectedFile.getName());
		
	}

	@FXML
	public void pauseButtonAction() {
	
		mediaPlayer.pause();
		mediaTime.setText(mediaPlayer.getCurrentTime()+" / "+mediaPlayer.getCycleDuration());
		actionStatus.setTextFill(Color.web("#FFA500"));
		actionStatus.setText("Now paused: " + selectedFile.getName());
	}

	@FXML
	public void onbtPlaylistAction() {

		FileChooser fileChooser = new FileChooser();
		selectedFile = fileChooser.showOpenDialog(null);
		try {
			fullPath = selectedFile.getCanonicalPath();
			System.out.println(fullPath);

		} catch (IOException e) {

			actionStatus.setText("Failed getting file path: " + e);
		}

		if (selectedFile != null && mediaPlayer ==null) {
			actionStatus.setText("Now playing: " + selectedFile.getName());
		} else {
			actionStatus.setTextFill(Color.web("#FF0000"));
			actionStatus.setText("File selection cancelled.");
		}
		
		
	}
	
			
	
	@FXML
	public void onMenuItemAboutAction() {

		loadView("/gui/AboutView.fxml", x -> {

		});
	}
	
	@FXML
	public void verifyCheckbox () {
		
			repeatState = true;
	}
	
	@FXML
	public void onBtReset() {
		
	}
	
	@FXML
	public void onBtNext() {
		
	}
	
	@FXML
	public void onBtPrevious() {
		
	}
	
	@FXML
	public void onSliderSpeed(ActionEvent event) {
		
	}
	
	@FXML
	public void startTimer() {
		
	}
	
	@FXML
	public void stopTimer() {
		
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();

			/* Casting para o VBox da tela principal. Ver FXML... */
			/* Permite que mantenha a janela aberta junto as janelas filhas */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			/* Executa a acao dos parametros que foram passados para inicializacao */
			T controller = loader.getController();
			initializingAction.accept(controller);
			
			
			

		} catch (IOException e) {

			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}

	}

}
