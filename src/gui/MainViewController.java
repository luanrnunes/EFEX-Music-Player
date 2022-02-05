package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
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
	private Button playButton;
	
	@FXML
	private Button stopButton;
	
	@FXML
	private Button playListButton;
	
	@FXML
	private Stage stage;
	
	@FXML
	private Label actionStatus;

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
		loadView("/gui/About.fxml", x -> {
			System.out.println("ok");
		});
	}
	
	@FXML
	public void onbtStopButtonAction() {
		
			System.out.println("ok");
		
	}
	
	@FXML
	public void onbtPlaylistAction() {
		
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		 
		if (selectedFile != null) {
		 
		    actionStatus.setText("File selected: " + selectedFile.getName());
		}
		else {
		    actionStatus.setText("File selection cancelled.");
		}
	}
		
	
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
			System.out.println("ok");
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
