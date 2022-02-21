package gui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;

public class EqualizerViewController implements Initializable {
	
	@FXML
	private Slider volumeBoostSlider;
	
	@FXML
	private RadioButton bassBoostSelect;
	
	@FXML
	private ComboBox <String> comboPresets = new ComboBox<String>();
	
	@FXML
	private String [] presets = {"Normal","Pop","Rock","Classical","Jazz"};
	

	public void initialize(URL url, ResourceBundle rb) {
		
	
		
		System.out.println(Arrays.toString(presets));
		for(int i = 0; i < presets.length; i++) {
			
			comboPresets.getItems().add(presets[i].toString());
			System.out.println("Adicionado preset "+presets[i].toString());
			
		}
		
		System.out.println(comboPresets.getValue());
		System.out.println(presets[1].toString());
		comboPresets.setOnAction(this::onPresetSelect);
	}
	
	
	@FXML
	public void onPresetSelect(ActionEvent event) {
		if (comboPresets.getValue() != null) {
			System.out.println("Entrou no action");
		}

	}


}
