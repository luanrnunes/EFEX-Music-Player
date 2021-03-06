package gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import dao.DB;
import dao.EfexDAO;
import dao.Songdata;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryViewController implements Initializable {

	@FXML
	private TableView<Songdata> table;
	
	@FXML
	private TableColumn<Songdata, String> col_song;
	
	@FXML
	private TableColumn<Songdata, String> col_duration;
	
	@FXML
	private TableColumn<Songdata, String> col_path;
	
	@FXML
	private TableColumn<Songdata, Time> col_timestamp;
	
	@FXML
	private Button remove;
	
	private Integer selectedIndex = -1;
	
	private Integer item;
	
	ObservableList<Songdata> oblist = FXCollections.observableArrayList();
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	
	LocalDateTime now = LocalDateTime.now(); 
	
	public HistoryViewController() {
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		Connection con = DB.getConnection();
		
		try {
			ResultSet rs = con.createStatement().executeQuery("select * from song_logs");
			while (rs.next()) {
				oblist.add(new Songdata(rs.getString("song"), rs.getString("duration"), rs.getString("path"), dtf.format(now)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		col_song.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
		col_path.setCellValueFactory(new PropertyValueFactory<>("path"));
		col_timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		
		table.setItems(oblist);
		
		/*Utilizar caso eu precise capturar cliques na tableview*/
		table.setOnMouseClicked (event -> {
				Songdata selectedItem = table.getSelectionModel().getSelectedItem();
				selectedIndex = table.getSelectionModel().getSelectedIndex();
				item = selectedItem.getId(selectedIndex-1);
								
	});



}

	@FXML
	public void removeBtn() {
		Connection conn = DB.getConnection();
		EfexDAO efexdao = new EfexDAO(conn);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Flush Data");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to flush the history data?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
		efexdao.remove();
        Alert alertConfirm = new Alert(AlertType.INFORMATION);
        alertConfirm.setTitle("Data wiped out");
        alertConfirm.setHeaderText(null);
        alertConfirm.setContentText("All the history data has been flushed succesfully!");
        alertConfirm.showAndWait();
        
       /* table.getScene().getWindow().setWidth(table.getScene().getWidth() + 0.001); /*Refresh scene*/
        
        MainViewController mvc = new MainViewController();
        table.getScene().getWindow().hide();
        mvc.onMenuHistory();

        }
	};
	
}
	



