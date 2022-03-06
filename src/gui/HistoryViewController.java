package gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.EfexDAO;
import dao.Songdata;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class HistoryViewController extends EfexDAO {

	@FXML
	private TableView<Songdata> tableview;
	
	public HistoryViewController(Connection conn) {
		super(conn);
	}
	

	//Then in your method where you process the data you can add columns dynamically....
	        /**********************************
	         * TABLE COLUMN ADDED DYNAMICALLY *
	         **********************************/
	
		@FXML
		public void onMenuHistoryAction() {
			/*
			loadView("/gui/AboutView.fxml", x -> {

			});*/
			
	        Parent root;
	        try {
	            root = FXMLLoader.load(getClass().getResource("/gui/HistoryView.fxml"));
	            Stage stage = new Stage();
	            stage.setTitle("History");
	            stage.setScene(new Scene(root, 400, 400));
	            stage.show();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        
			
			try {
			PreparedStatement st = conn.prepareStatement("SELECT name, duration, path \r\n"
					+ "FROM song_logs \r\n");
			
			ResultSet rs = st.executeQuery();
			
	        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
	            //We are using non property style for making dynamic table
	            final int j = i;                
	            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
	            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
	                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
	                    return new SimpleStringProperty(param.getValue().get(j).toString());                        
	                }                    
	            });

	            tableview.getColumns().addAll(col); 
	            System.out.println("Column ["+i+"] ");
	        }
			
			
			}catch (SQLException e){
				e.printStackTrace();
			}
			
		
	
}
}

