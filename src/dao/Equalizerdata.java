package dao;

public class Equalizerdata {
	
	public double volumeBoostDt;
	
	public Boolean bassBoostDt;
	
	public String equalizerPreset;

	public Equalizerdata(double volBoostValue, Boolean bassBoostSelected, String equalizerPreset) {
		super();
		this.volumeBoostDt = volBoostValue;
		this.bassBoostDt = bassBoostSelected;
		this.equalizerPreset = equalizerPreset;
	}

	public double getVolumeBoostDt() {
		return volumeBoostDt;
	}

	public void setVolumeBoostDt(double volumeBoostDt) {
		this.volumeBoostDt = volumeBoostDt;
	}

	public Boolean getBassBoostDt() {
		return bassBoostDt;
	}

	public void setBassBoostDt(Boolean bassBoostDt) {
		this.bassBoostDt = bassBoostDt;
	}

	public String getEqualizerPreset() {
		return equalizerPreset;
	}

	public void setEqualizerPreset(String equalizerPreset) {
		this.equalizerPreset = equalizerPreset;
	}
	
	

}
