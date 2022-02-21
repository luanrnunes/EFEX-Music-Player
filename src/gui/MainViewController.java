package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.util.Duration;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemFile;

	@FXML
	private MenuItem menuItemConfig;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private Button playButton, stopButton, playListButton, pauseButton, nextButton, resetButton;
	
	@FXML
	private Button prevButton;

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
	private ProgressBar songProgressBar;
	
	private File directory;
	
	private File [] files;
	
	private ArrayList<File> songs;
	
	private int songCounter;
	
	private int [] speeds = {25, 50, 75, 100, 125, 150, 175, 200};
	
	private Boolean repeatState = false;

	private String fullPath;

	private MediaPlayer mediaPlayer;

	private Media media;
	
	private String selectedAudio;
	
	private File selectedFile; 
	
	private String repeatset;
	
	private String dbCurrent;
	
	private String dbEnd;
	
	private Integer repeatSetInt;
	
	private Timer timer;
	
	private TimerTask task;
	
	private String repeatSong;
	
	private double current;
	
	private double end;
	
	private boolean isRunning;

	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		songs = new ArrayList<File>();
		directory = new File("music");
		files = directory.listFiles(); /*Metodo captura todos os arquivos no folder*/
		
		if(files != null) {
			
			for(File file : files) {
				songs.add(file);
			}
			
		}
		
		/*media = new Media(songs.get(songCounter).toURI().toString());
		mediaPlayer = new MediaPlayer(media);*/
		
		for(int i = 0; i < speeds.length; i++) {
			speedSelector.getItems().add(Integer.toString(speeds[i])+'%');
		}
		
		speedSelector.setOnAction(this::onSpeedSetter);
		
		
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
				
			}
			
		});
		
		songProgressBar.setStyle("-fx-accent: #54BAB9;");
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
	

	@FXML
	public void onMenuEqualizerAction() {

	        Parent root;
	        try {
	            root = FXMLLoader.load(getClass().getResource("/gui/Equalizer.fxml"));
	            Stage stage = new Stage();
	            stage.setTitle("EFEX Equalizer");
	            stage.setScene(new Scene(root, 600, 400));
	            stage.show();
	            EqualizerViewController eq = new EqualizerViewController();
	            eq.initialize(null, null);
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
		};
	

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
				
				if(repeat.isSelected()) {
					
					System.out.println("repeat is selected with value of "+setTimesRepeat());
					timesRepeat.setEditable(true);
					for (int i=0; i == setTimesRepeat(); i++) {
						media = new Media(new File(repeatSong).toURI().toString());
						mediaPlayer.play();
						mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
						actionStatus.setTextFill(Color.web("#000dff"));
					}	
					
				}
				else {
					timesRepeat.setEditable(false);
				}
				media = new Media(new File(fullPath).toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.play();
				mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
				actionStatus.setTextFill(Color.web("#000dff"));
				selectedAudio = fullPath;

				startTimer();
				onSpeedSetter(null);
				
				dbCurrent = Double.toString(current);
				dbEnd = Double.toString(end);
				mediaTime.setText(dbCurrent+" / " + dbEnd);
			} else {
			if (mediaPlayer != null) {
					actionStatus.setTextFill(Color.web("#000dff"));
					mediaPlayer.play();
					dbCurrent = Double.toString(current);
					dbEnd = Double.toString(end);
					mediaTime.setText(dbCurrent+" / " + dbEnd);
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
		
		public Integer setTimesRepeat () {
		
			repeatset = timesRepeat.getText();
			repeatSetInt = Integer.parseInt(repeatset);
			return repeatSetInt;
		}
	

	@FXML
	public void onbtStopButtonAction() {
		
		stopTimer();
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
		repeatSong = fullPath;
		System.out.println("The song for repeat is: "+ repeatSong);
		try {
			fullPath = selectedFile.getCanonicalPath();
			System.out.println("The actual song is: "+fullPath);

		} catch (IOException e) {

			actionStatus.setText("Failed getting file path: " + e);
		}

		if (selectedFile != null) {
			actionStatus.setText("Now playing: " + selectedFile.getName());
		} else {
			actionStatus.setTextFill(Color.web("#FF0000"));
			actionStatus.setText("File selection cancelled.");
		}

		}
			
	
	@FXML
	public void onMenuItemAboutAction() {
		/*
		loadView("/gui/AboutView.fxml", x -> {

		});*/
		
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/gui/AboutView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
		
	
	@FXML
	public void verifyCheckbox () {
		
			repeatState = true;
	}
	
	@FXML
	public void onBtReset() {
		
		songProgressBar.setProgress(0);
		mediaPlayer.seek(Duration.seconds(0));
	}
	
	@FXML
	public void onBtNext() {
		
		if(songCounter < songs.size() - 1)
		{
			songCounter ++;
			
			mediaPlayer.stop();
			
			if(isRunning) {
				stopTimer();
			}
				
			
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now Playing: " + songs.get(songCounter).getName());
			
			mediaPlayer.play();
		}
		else {
			
			songCounter = songs.size() - 1;
			
			mediaPlayer.stop();
			
			if(isRunning) {
				
				stopTimer();
			}
			
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now playing: " + songs.get(songCounter).getName());
			mediaPlayer.play();
		}
		
	}
	
	@FXML
	public void onBtPrevious() {
		if(songCounter > 0)
		{
			songCounter --;
			
			mediaPlayer.stop();
			
			if(isRunning) {
				
				stopTimer();
			}
			
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now Playing: " + songs.get(songCounter).getName());
			
			mediaPlayer.play();
		}
		else {
			
			songCounter = songs.size() - 1;
			
			mediaPlayer.stop();
			
			if(isRunning) {
				
				stopTimer();
			}
		
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now Playing: " + songs.get(songCounter).getName());
			mediaPlayer.play();
		}
		
	}
	
	/*Listeners sao ActionEvent event*/
	@FXML
	public void onSpeedSetter(ActionEvent event) {
		/*getValue().substring(0, speedSelector.getValue().length() -1)) esta linha ignora o simbolo de porcento ao passar no evento*/
		
		if(speedSelector.getValue() == null)
			mediaPlayer.setRate(1);
		
	else  {
		mediaPlayer.setRate(Integer.parseInt(speedSelector.getValue().substring(0, speedSelector.getValue().length() -1)) * 0.01);
		}
	}
	
	@FXML
	public void startTimer() {
		
		 timer = new Timer();
		
		task = new TimerTask() {
			
			public void run () {
				
				isRunning = true;
				current = mediaPlayer.getCurrentTime().toSeconds();
				end = media.getDuration().toSeconds();
				System.out.println(current/end);
				songProgressBar.setProgress(current/end);
				
				if(current/end == 1) {
					stopTimer();
				}
				
			}
		};
		
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	@FXML
	public void stopTimer() {
		timer = new Timer();
		timer.cancel();
	}

}
