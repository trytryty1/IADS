package Interface;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import Messaging.DataChangeEvent;
import Messaging.Event;
import Sensors.Plot;
import Sensors.Radar;
import System.Location;
import System.Managers.PlotManager;

public class SimulatorPanel extends JPanel implements Event{
	private static final long serialVersionUID = 1L;
	Location _upperLeft;
	Location _lowerRight;
	
	float _width, _height;
	float _range;
	ArrayList<Radar> _radars;
	PlotManager _plotMgr;
	
	public SimulatorPanel(Location upperLeft, Location lowerRight, ArrayList<Radar> radars)  {
		_upperLeft = upperLeft;
		_lowerRight = lowerRight;
		double distance = upperLeft.distanceTo(_lowerRight);
		double brng = upperLeft.getBearingBetweenLoc(_lowerRight);
		//System.out.println("angle between corners is: " + Math.toDegrees(brng));
		
		//System.out.println("total playing field distance is: " + distance);
		_width = Math.abs((float) ((float) distance * Math.cos(brng)));
		//System.out.println("width of field is: " + _width);

		_height = Math.abs((float) ((float) distance * Math.sin(brng)));
		//System.out.println("height of field is:  " + _height);

		_range = _width < _height ? _width : _height;
		_radars = radars;
		
		_plotMgr = new PlotManager();
		//_plotMgr.addListener(this);
	}
	
	  public void paint(Graphics g) {
		    super.paint(g);
		    Graphics2D g2 = (Graphics2D) g;
		    g2.setStroke(new BasicStroke(3));

		    int dim = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
		    float scale = (float)dim / _range;
		    
		    g.drawImage(Images.MAP_IMAGE,0, 0, getScaledValue(_range,scale), getScaledValue(_range, scale), null);
		    for(Radar radar: _radars) {
		    	if(!radar.isActive()) {
		    		continue;
		    	}
		    	int radius = getScaledValue((float) radar.getRange(), scale);
		    	
		    	Point coord = getCoordsFromLocationInWorld(radar.getLoc(), new Dimension(dim, dim));
		    	g.drawOval( coord.x - radius, coord.y - radius, radius * 2, radius * 2); 
		    	g.drawString(radar.getName(), coord.x , coord.y);
		    }
		    
		    for(Plot _plot: _plotMgr.getPlots()) {

		    	int radius = getScaledValue((float) 4, scale) > 4 ? getScaledValue((float) 4, scale) : 4;
		    	Point coord = getCoordsFromLocationInWorld(_plot.getLoc(), new Dimension(dim, dim));
		    	
		    	g.fillOval( coord.x - radius, coord.y - radius, radius * 2, radius * 2); 
		    	g.drawImage(Images.AIR_PLANE_IMAGE, coord.x - radius, coord.y - radius, radius * 2, radius * 2, null);
		    	
		    }

	  }

	  public int getScaledValue(float value, float scale) {
		  return (int) ( value * scale);
	  }

	@Override
	public void eventCallback(int eventInfo, Object data) {
		
		if(eventInfo == Event.PLOT_DATA_CHANGED) {
			DataChangeEvent event = (DataChangeEvent) data;
			_plotMgr.addPlot(event.getNewPlot());
			this.repaint();
		}	
	}
	
	public Point getCoordsFromLocationInWorld(Location loc, Dimension dim) {
		double a = loc.getLongitude();
		double b = Math.tan(loc.getLatitudeAsRadians());
		
		double min_a = _upperLeft.getLongitude();
		double min_b = Math.tan(_lowerRight.getLatitudeAsRadians());
		
		double max_a = _lowerRight.getLongitude();
		double max_b = Math.tan(_upperLeft.getLatitudeAsRadians());
		double scaleX = dim.width / (max_a - min_a);
		double offsetX = -dim.width * min_a / (max_a - min_a);
		
		double scaleY = -dim.height / (max_b - min_b);
		double offsetY = dim.height * max_b / (max_b - min_b);
		
		return new Point((int)(scaleX * a + offsetX), (int)(scaleY * b + offsetY));
		
		
	}
	
}
