package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemFile;

	@FXML
	private MenuItem menuItemConfig;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private Button playButton;

	@FXML
	private Button stopButton;

	@FXML
	private Button playListButton;

	@FXML
	private Button pauseButton;

	@FXML
	private Stage stage;

	@FXML
	private Label actionStatus;

	@FXML
	private Label mediaTime;

	private String fullPath;

	private MediaPlayer mediaPlayer;

	private Media hit;
	
	private String selectedAudio;

	private ChangeListener<Duration> TimeListener;

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
				selectedAudio = fullPath;
				hit = new Media(new File(selectedAudio).toURI().toString());
				mediaPlayer = new MediaPlayer(hit);
				mediaPlayer.play();
				mediaTime.setText(mediaPlayer.getCurrentTime()+"\n"+mediaPlayer.getCycleDuration());
			} else {
				if (mediaPlayer != null) {
					selectedAudio = fullPath;
					hit = new Media(new File(selectedAudio).toURI().toString());
					mediaPlayer.stop();
					mediaPlayer.play();
					mediaTime.setText(mediaPlayer.getCurrentTime()+"\n"+mediaPlayer.getCycleDuration());
				}
			}
		}
		 catch (Exception e) {
			actionStatus.setText("No song selected!");
			System.out.println(e.getMessage());
		}
		

	}

	@FXML
	public void onbtStopButtonAction() {
		selectedAudio = fullPath;
		hit = new Media(new File(selectedAudio).toURI().toString());
		mediaPlayer.stop();
		mediaTime.setText(mediaPlayer.getCurrentTime()+"\n"+mediaPlayer.getCycleDuration());
		
	}

	@FXML
	public void pauseButtonAction() {
		String selectedAudio = fullPath;
		hit = new Media(new File(selectedAudio).toURI().toString());
		mediaPlayer.pause();
		mediaTime.setText(mediaPlayer.getCurrentTime()+"\n"+mediaPlayer.getCycleDuration());
	}

	@FXML
	public void onbtPlaylistAction() {

		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		try {
			fullPath = selectedFile.getCanonicalPath();
			System.out.println(fullPath);

		} catch (IOException e) {

			actionStatus.setText("Failed getting file path: " + e);
		}

		if (selectedFile != null) {

			actionStatus.setText("Now playing: " + selectedFile.getName());
		} else {
			actionStatus.setText("File selection cancelled.");
		}
		
		
	}
	
			
	
	@FXML
	public void onMenuItemAboutAction() {

		loadView("/gui/AboutView.fxml", x -> {

		});
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
