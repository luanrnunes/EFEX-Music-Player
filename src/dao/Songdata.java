package dao;

import java.sql.Time;

public class Songdata {
	
	private Integer id;

	private String name;
	private String duration;
	private String path;
	private String timestamp;
	
	public Songdata() {
		
	}
	
	public Songdata(Integer id) {
		this.id = id;
	}

	public Songdata(String name, String duration, String path, String timestamp) {
		super();
		this.name = name;
		this.duration = duration;
		this.path = path;
		this.timestamp = timestamp;
	}
	
	public Integer getId(Integer selectedindex) {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
			
	
}
