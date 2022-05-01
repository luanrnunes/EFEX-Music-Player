package gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;


import application.Main;
import dao.EfexDAO;
import dao.Songdata;
import gui.util.Alerts;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class MainViewController  implements Initializable  {

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
	private Button videoButton;
	
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

	private Boolean videoRunning = true;
	
	private Boolean isMuted = false;
	
	private String fullPath;
	
	private String videoPath;
	
	private URI fullPathUri;

	private MediaPlayer mediaPlayer;

	private Media media;
	
	private String selectedAudio;
	
	private File selectedFile; 
	
	private String repeatset;
	
	private String dbCurrent;
	
	private String dbEnd;
	
	private Integer repeatSetInt;
	
	private Timer timer;
	
	private Timer timer2;
	
	private TimerTask task;
	
	private TimerTask task2;
	
	private String repeatSong;
	
	private URI repeatSongUri;
	
	private  double playTime;
	
	private double current;
	
	private double videoCurrent;
	
	private double end;
	
	private boolean isRunning;
	
	private boolean isFullScreen;
	
    
   private MediaView mediaView; 
	
	

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	
	LocalDateTime now = LocalDateTime.now();  

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
	public void verifyCheckbox(ActionEvent event) {
        if(repeat.isSelected()){
        	timesRepeat.setDisable(false);
            setTimesRepeat();
            repeatState = true;
        }
        else {
        	timesRepeat.setDisable(true);
        	repeatState = false;
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
		public void onMenuHistory() {
	        Parent root;
	        try {
	            root = FXMLLoader.load(getClass().getResource("/gui/HistoryView.fxml"));
	            Stage stage = new Stage();
	            stage.setTitle("Database - History");
	            stage.setScene(new Scene(root, 500, 500));
	            stage.show();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		
	@FXML
	public void onMenuItemConfig() {
		loadView("/gui/Item.fxml", (ConfigViewController controller) -> {
			System.out.println("ok");
		});
	}

	@FXML
	public void onbtPlayButtonAction() {
		
		
		if (mediaPlayer != null) {
			actionStatus.setTextFill(Color.web("#000dff"));
			actionStatus.setText("Now playing: " + selectedFile.getName());
			mediaPlayer.play();
			startTimer();
			startTimer2();
			

			
		} else {
			
				if(fullPath == null) {
					actionStatus.setTextFill(Color.web("#FF0000"));
					actionStatus.setText("No song selected!");
				}
				else {

				media = new Media(new File(fullPath).toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.play();
				mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
				actionStatus.setTextFill(Color.web("#000dff"));
				selectedAudio = fullPath;
				
				actionStatus.setText("Now playing: " + selectedFile.getName());
				
				startTimer();
				startTimer2();
				onSpeedSetter(null);
				
				}
				dbCurrent = Double.toString(current);
				dbEnd = Double.toString(end);
				try {
					Songdata dt = new Songdata(selectedFile.getName(), dbEnd.toString(),fullPath, dtf.format(now));
					EfexDAO efexDao = new EfexDAO(dao.DB.getConnection());
					efexDao.insert(dt);
					System.out.println("Inserted! New ID = " + dt.getName());
					
					}catch(RuntimeException e) {
						e.printStackTrace();	
					}
		}
			
			
				/*if (repeatset != null || repeatset != "" ) {
					setTimesRepeat();
				}*/
			
	}
		
	
		public Integer setTimesRepeat () {
		try {
			repeatset = timesRepeat.getText();
			repeatSetInt = Integer.parseInt(repeatset);
			return repeatSetInt;
		}catch(RuntimeException e) {
			repeatSetInt = null;
			return repeatSetInt;
		}
		}

	@FXML
	public void onbtStopButtonAction() {
		
		stopTimer();
		stopTimer2();
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
		repeatSongUri = fullPathUri;
		System.out.println("The song for repeat is: "+ repeatSong);
		try {
			fullPath = selectedFile.getCanonicalPath();
			fullPathUri = selectedFile.toURI();
			System.out.println("The actual song is: "+fullPath);
			media = new Media(new File(fullPath).toURI().toString());

		} catch (IOException e) {

			actionStatus.setText("Failed getting file path: " + e);
		}

		if (selectedFile != null) {
			actionStatus.setText("Now playing: " + selectedFile.getName());
			try {
				mediaPlayer.stop();
				mediaPlayer.dispose();
				media = new Media(new File(fullPath).toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.play();
				mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
				actionStatus.setTextFill(Color.web("#000dff"));
				selectedAudio = fullPath;
				
				actionStatus.setText("Now playing: " + selectedFile.getName());

				startTimer();
				startTimer2();
				onSpeedSetter(null);
				
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
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
				stopTimer2();
			}
				
			
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now Playing: " + songs.get(songCounter).getName());
			
			mediaPlayer.play();
			
			startTimer();
			startTimer2();
			
		}
		else {
			
			songCounter = songs.size() - 1;
			
			mediaPlayer.stop();
			
			if(isRunning) {
				
				stopTimer();
				stopTimer2();
			}
			
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now playing: " + songs.get(songCounter).getName());
			mediaPlayer.play();
			
			startTimer();
			startTimer2();
			
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
				stopTimer2();
			}
			
			media = new Media(songs.get(songCounter).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			
			actionStatus.setText("Now Playing: " + songs.get(songCounter).getName());
			
			mediaPlayer.play();
			
			startTimer();
			startTimer2();
			
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
			
			startTimer();
			startTimer2();
		}
		
	}
	
	@FXML
	public void onBtVideo () {
		
		
		FileChooser fileChooser = new FileChooser();
		selectedFile = fileChooser.showOpenDialog(null);
		try {
			videoPath = selectedFile.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("The video selected is: "+ videoPath);
		
		Stage stage = new Stage();
		
        StackPane root = new StackPane();
        
        media = new Media( new File(videoPath).toURI().toString());

        mediaPlayer = new MediaPlayer( new Media(new File(videoPath).toURI().toString()));
        
        mediaView = new MediaView(mediaPlayer);

        root.getChildren().add(mediaView);
        
        dbCurrent = Double.toString(current);
		dbEnd = Double.toString(end);
        
        
        Scene scene = new Scene(root, 1280, 720);
        
        stage.initModality(Modality.APPLICATION_MODAL);
        
        ContextMenu contextMenu = new ContextMenu();
        //Creating the menu Items for the context menu
        MenuItem item1 = new MenuItem("Copy");
        MenuItem item2 = new MenuItem("remove");
        contextMenu.getItems().addAll(item1, item2);
        contextMenu.styleProperty().setValue("-fx-background-color:  #F7CCAC");
        
        
        
        // Create a label that will report the selection.
        Label response;
        response = new Label("Video Menu");

        // Create the context menu items
        MenuItem ff10 = new MenuItem("Fast Forward + 10s");
        MenuItem rw10 = new MenuItem("Rewind - 10s");
        MenuItem ff30 = new MenuItem("Fast Forward + 30s");
        MenuItem rw30 = new MenuItem("Rewind - 30s");
        MenuItem a0 = new MenuItem("Normal Speed");
        MenuItem a15x = new MenuItem("Accelarate 1.5x");
        MenuItem a20x = new MenuItem("Accelarate 2.0x");;
        MenuItem a05x = new MenuItem("Slow Down 0.5x");
        MenuItem a02x = new MenuItem("Slow Down 0.25x");
        MenuItem reset = new MenuItem("Reset");
        MenuItem muteVideo = new MenuItem("Mute");

        // Create a context (i.e., popup) menu that shows edit options.
        final ContextMenu editMenu = new ContextMenu(ff10, rw10,ff30,rw30,a0,a15x,a20x,a05x,a02x ,reset, muteVideo);
        editMenu.styleProperty().setValue("-fx-background-color:  #F7CCAC");

     // Add the context menu to the entire scene graph.
        root.setOnContextMenuRequested(
                    new EventHandler<ContextMenuEvent>() {
          public void handle(ContextMenuEvent ae) {
            // Popup menu at the location of the right click.
            editMenu.show(root, ae.getScreenX(), ae.getScreenY());
            
          }
        });
        

        stage.setScene(scene);
        stage.setTitle("EFEX Video Player - "+videoPath);
        stage.show();

       mediaPlayer.play();
	      
	       playTime = current;
	       startTimer2();

		
		try {
			Songdata dt = new Songdata(selectedFile.getName(), dbEnd.toString(),videoPath, dtf.format(now));
			EfexDAO efexDao = new EfexDAO(dao.DB.getConnection());
			efexDao.insert(dt);
			System.out.println("Inserted! New ID = " + dt.getName());
			
			}catch(RuntimeException e) {
				e.printStackTrace();	
			}
       
      videoRunning = true;
      
      isFullScreen = false;
       
       stage.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent mouseEvent) {
    	    	
    	    	editMenu.hide();
    	    	
    	    	if(videoRunning == true) {
    	        mediaPlayer.pause();
    	        videoRunning = false;	        
    	    }
    	    else {
    	    	mediaPlayer.play();
    	    	videoRunning = true;
    	    }}
    	});
       
       scene.setOnKeyPressed(f -> {
    	    if (f.getCode() == KeyCode.F && isFullScreen == false) {
    	    	((Stage)mediaView.getScene().getWindow()).setFullScreen(true);
    	    	 isFullScreen = true;
    	    }
    	    else if (f.getCode() == KeyCode.F && isFullScreen == true) {
    	    	((Stage)mediaView.getScene().getWindow()).setFullScreen(false);
   	    	 isFullScreen = false;
   	    }
    	           
    	       /*try {
    		Thread.sleep(2 * 1000);
    	} catch (InterruptedException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}*/
    	       

        		
    	    
    	});
       
       scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
    	    @Override
    	    public void handle(MouseEvent mouseEvent) {
    	        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
    	            if(mouseEvent.getClickCount() == 2 && isFullScreen == false){
    	            	((Stage)mediaView.getScene().getWindow()).setFullScreen(true);
    	            	isFullScreen = true;
    	            }
    	            else if(mouseEvent.getClickCount() == 2 && isFullScreen == true){
    	            	((Stage)mediaView.getScene().getWindow()).setFullScreen(false);
    	            	isFullScreen = false;
    	            }
    	        }
    	    }
       });
       
       ff10.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.seek(Duration.seconds(playTime+10));
        	   System.out.println("Playtime: " +playTime);
        	   mediaPlayer.play();
             
           }
           
       });
       
       rw10.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.seek(Duration.seconds(playTime-10));
        	   System.out.println("Playtime: " +playTime);
        	   mediaPlayer.play();
             
           }
           
       });
       
       ff30.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.seek(Duration.seconds(playTime+30));
        	   System.out.println("Playtime: " +playTime);
        	   mediaPlayer.play();
             
           }
           
       });
       
       rw30.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.seek(Duration.seconds(playTime-30));
        	   System.out.println("Playtime: " +playTime);
        	   mediaPlayer.play();
             
           }
           
       });
       
       muteVideo.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   if(isMuted == false) {
              mediaPlayer.setVolume(0);
              isMuted = true;
        	   } else {
        		   isMuted = false;
        		   mediaPlayer.setVolume(100);
        	   }
           }      
           
       });
       
       a0.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.setRate(1);
           }   
           
       });
       
       a15x.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.setRate(1.5);
           }   
           
       });
       
       a20x.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.setRate(2);
           }   
           
       });
       
       a02x.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.setRate(0.2);
           }   
           
       });
       
       a05x.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
        	   mediaPlayer.setRate(0.5);
           }   
           
       });
       
       reset.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
              mediaPlayer.seek(Duration.seconds(0));
              mediaPlayer.play();
             
           }
           
           
           
       });
          
       
	};
	
	

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
				songProgressBar.setProgress(current/end);
				if(current == end) {
					
					System.out.println("Entrou na condicao de fim de musica");
					stopTimer();
					if (repeatState == true && setTimesRepeat() != null) {
						if (repeatSong == null) {
							for (int i=0; i<=setTimesRepeat(); i++)
							mediaPlayer.getClass().getResource(fullPathUri.toString());
							 MediaPlayer a =new MediaPlayer(new Media(fullPathUri.toString()));
							 a.setOnEndOfMedia(new Runnable() {
							       public void run() {
							         a.seek(Duration.ZERO);
							       }
							   });
							  a.play();
						
					} 
						else {
						if (repeatSong == null) {
							for (int i=0; i<=setTimesRepeat(); i++)
							mediaPlayer.getClass().getResource(repeatSongUri.toString());
							 MediaPlayer a =new MediaPlayer(new Media(repeatSong.toString()));
							 a.setOnEndOfMedia(new Runnable() {
							       public void run() {
							         a.seek(Duration.ZERO);
							       }
							   });
							  a.play();
						try {
							mediaPlayer.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}
						
				
				}
					
				}
				
			}
		};
		

		timer.scheduleAtFixedRate(task, 0, 1000);
		
	
	}
	
	@FXML
	public void startTimer2() {
		timer2 = new Timer();
		task2 = new TimerTask() {

			@Override
			public void run() {
				current = mediaPlayer.getCurrentTime().toSeconds();
				end = media.getDuration().toSeconds();
				mediaTime.setText(current+" / " + end);
				playTime = current;
				System.out.println("task2: "+current+" / " + end);
				
			}
			
		};
		timer2.scheduleAtFixedRate(task2, 0, 1000);
	}
	
	@FXML
	public void stopTimer() {
		
		timer.cancel();

	}
	
	@FXML
	public void stopTimer2() {

		timer2.cancel();

	}

}
