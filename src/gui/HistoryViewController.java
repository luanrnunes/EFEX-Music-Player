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
import dao.Songdata;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	}


}
	



