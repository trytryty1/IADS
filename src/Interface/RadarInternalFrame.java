package Interface;

import javax.swing.JInternalFrame;

public class RadarInternalFrame extends JInternalFrame {
	

	private static final long serialVersionUID = 1L;
	
	RadarPanel _panel;
	RadarInternalFrame (String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, RadarPanel panel) {
		super(title, resizable, closable, maximizable, iconifiable);
		_panel = panel;
	}
	
	public RadarPanel getRadarPanel() {
		return _panel;
	}
}
