package gui;

import java.net.URL;
import java.sql.Connection;
import java.util.Arrays;
import java.util.ResourceBundle;

import dao.DB;
import dao.EfexDAO;
import dao.Equalizerdata;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;

public class EqualizerViewController implements Initializable {
	
	@FXML
	private Slider volumeBoostSlider;
	
	@FXML
	private RadioButton bassBoostSelect;
	
	@FXML
	private Button saveSettings;
	
	@FXML
	private ComboBox <String> comboPresets = new ComboBox<String>();
	
	@FXML
	private String [] presets = {"Normal","Pop","Rock","Classical","Jazz"};
	
	double volBoostValue;
	
	String comboPresetValue;
	
	Boolean bassBoostSelected;
	

	public void initialize(URL url, ResourceBundle rb) {
		
		
		
		System.out.println(Arrays.toString(presets));
		for(int i = 0; i < presets.length; i++) {
			
			comboPresets.getItems().add(presets[i].toString());
			System.out.println("Adicionado preset "+presets[i].toString());
			
		}
		
		System.out.println(comboPresets.getValue());
		System.out.println(presets[1].toString());
		comboPresets.setOnAction(this::onPresetSelect);
		
		volumeBoostSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				System.out.println(volumeBoostSlider.getValue());
				volBoostValue = volumeBoostSlider.getValue();
				
			}
			
		});
		
    	Connection conn = DB.getConnection();
    	EfexDAO efexdao = new EfexDAO(conn);
    	int bb = efexdao.getEqualizerData_BassBoost();
    	String pData = efexdao.getEqualizerData_Preset();
    	
    	System.out.println("Preset data from DB is: "+pData);
    	
    	if (bb==1) {
    		bassBoostSelect.setSelected(true);;
    	} else {
    		bassBoostSelect.setSelected(false);
    	}
    	System.out.println("Database BB "+bb);
		
	}
	
	
	@FXML
	public void onPresetSelect(ActionEvent event) {
		if (comboPresets.getValue() != null) {
			System.out.println(comboPresets.getValue());
			comboPresetValue = comboPresets.getValue();
		}	

	}
	
	@FXML
	public void onBassBoostSelect(ActionEvent event) {
        if(bassBoostSelect.isSelected()){
            System.out.println("Bass Boost selecionado!");
            bassBoostSelected = true;
        }
        else {
        	System.out.println("Bass Boost desligado!");
        	bassBoostSelected = false;
        }
	}
	
	
	@FXML
	public void onBtSaveSettings() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Commit data?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure that you want to keep these changes to the equalizer?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
        	Connection conn = DB.getConnection();
        	Equalizerdata EqData = new Equalizerdata(volBoostValue, bassBoostSelected, comboPresetValue);
        	EfexDAO efexdao = new EfexDAO(conn);
        	efexdao.deleteEqualizerData();
        	efexdao.insertEqualizerData(EqData);
	}
        else {
        	Alert alertCancel = new Alert(AlertType.INFORMATION);
        	alertCancel.setTitle("Action canceled");
        	alertCancel.setHeaderText(null);
        	alertCancel.setContentText("Action canceled, no changes were made");
        	alertCancel.showAndWait();
        }


}
	
}
