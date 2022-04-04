package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class EfexDAO {
	public Connection conn;
	
	public EfexDAO () {
		
	}
	
	public EfexDAO(Connection conn) {
		this.conn = conn;
	}

	public void insert(Songdata obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO song_logs "
					+"(song, duration, path, timestamp) "
				    +"Values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getDuration());
			st.setString(3, obj.getPath());
			st.setString(4, obj.getTimestamp());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				System.out.println(rs);
			}
				
				else {
					throw new RuntimeException("Unexpected error! No rows affected");
				}
				
				
			} catch(SQLException e){
				e.printStackTrace();
			}}
		
		public void get(Songdata obj) {
			PreparedStatement st = null;
			
			try {
				st = conn.prepareStatement(
						"INSERT INTO song_logs "
						+"(song, duration, path) "
					    +"Values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				st.setString(1, obj.getName());
				st.setString(2, obj.getDuration());
				st.setString(3, obj.getPath());

				
				int rowsAffected = st.executeUpdate();
				
				if (rowsAffected > 0) {
					ResultSet rs = st.getGeneratedKeys();
					System.out.println(rs);
				}
					
					else {
						throw new RuntimeException("Unexpected error! No rows affected");
					}
					
					
				}
			
		catch(SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	public void remove() {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE from song_logs where 1=1",Statement.RETURN_GENERATED_KEYS);


			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				System.out.println(rs);
			}
				
				
			} catch(SQLException e){
				e.printStackTrace();
			}
	
		finally {
		DB.closeStatement(st);
	}
	}
		
	public void insertEqualizerData(Equalizerdata eqObj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO equalizer_data "
					+"(volBoostData, bassBoostData, presetsData) "
				    +"Values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, eqObj.getVolumeBoostDt());
			st.setBoolean(2, eqObj.getBassBoostDt());
			st.setString(3, eqObj.getEqualizerPreset());

			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				System.out.println(rs);
			}
				
				else {
					throw new RuntimeException("Unexpected error! No rows affected");
				}
				
				
			}
		
	catch(SQLException e) {
		throw new RuntimeException(e.getMessage());
	}
	finally {
		DB.closeStatement(st);
	}
	
	
}
	
	public void deleteEqualizerData() {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"delete from equalizer_data"
					, Statement.RETURN_GENERATED_KEYS);
		
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				System.out.println(rs);
			}
				
				else {
					throw new RuntimeException("Unexpected error! No rows affected");
				}
				
				
			}
		
	catch(SQLException e) {
		throw new RuntimeException(e.getMessage());
	}
	finally {
		DB.closeStatement(st);
	}
	
}
	
	public Integer getEqualizerData_BassBoost() {
		PreparedStatement st = null;
	
	try {
		st = conn.prepareStatement(
				"select bassBoostData from equalizer_data"
				, Statement.RETURN_GENERATED_KEYS);
	
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
			Integer bassBoostValue = rs.getInt(1);
			System.out.println("BassBoost= "+bassBoostValue);
			return bassBoostValue;
			}
			
			
		}	catch(SQLException e) {
			throw new RuntimeException(e.getMessage());
			
		}	finally {
			DB.closeStatement(st);
		}
	return 0;	
}
	
	public String getEqualizerData_Preset() {
		PreparedStatement st = null;
		
	try {
		st = conn.prepareStatement(
				"select presetsData from equalizer_data"
				, Statement.RETURN_GENERATED_KEYS);
	
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
			String presetValue = rs.getString(1);
			System.out.println("BassBoost= "+presetValue);
			return presetValue;
			}
			
			
		}	catch(SQLException e) {
			throw new RuntimeException(e.getMessage());
			
		}	finally {
			DB.closeStatement(st);
		}
	return null;
	}
	
	public Double getEqualizerData_volBoostData() {
		PreparedStatement st = null;
		
	try {
		st = conn.prepareStatement(
				"select volBoostData from equalizer_data"
				, Statement.RETURN_GENERATED_KEYS);
	
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
			Double volumeBoostValue = rs.getDouble(1);
			System.out.println("BassBoost= "+volumeBoostValue);
			return volumeBoostValue;
			}
			
			
		}	catch(SQLException e) {
			throw new RuntimeException(e.getMessage());
			
		}	finally {
			DB.closeStatement(st);
		}
	return 0.0;
	}
	
}
