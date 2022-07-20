package Interface;

import javax.swing.JCheckBox;

import Sensors.Radar;

public class RadarCheckBox extends JCheckBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Radar _radar;
	
	public RadarCheckBox(String title, Radar radar) {
		super(title);
		_radar = radar;
	}

	public Radar get_radar() {
		return _radar;
	}

	public void set_radar(Radar _radar) {
		this._radar = _radar;
	}

	
}
