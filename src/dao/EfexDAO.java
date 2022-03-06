package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class EfexDAO {
	private Connection conn;
	
	public EfexDAO(Connection conn) {
		this.conn = conn;
	}

	public void insert(Songdata obj) {
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
	
	private Songdata instantiateSongdata(ResultSet rs) throws SQLException {
		
		Songdata obj = new Songdata();
		obj.setName(rs.getString("name"));
		obj.setDuration(rs.getString("duration"));
		obj.setPath(rs.getString("path"));
		return obj;
	}
	
	
}
