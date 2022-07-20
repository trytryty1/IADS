package Interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import Messaging.Event;
import Sensors.Plot;
import Sensors.Radar;
import System.Location;

public class RadarPanel extends JPanel implements Event{
	
	private static final long serialVersionUID = 1L;
	
	//public static int WINDOW_SIZE_IN_MILES = 400;
	
	private int[] ringDistance  = { 100,200,300,400,500 };
	
	Location _upperLeft, _lowerRight;
	
	Radar sensor;
	
	public RadarPanel(Radar sensor) {
		super();
		this.sensor = sensor;
		sensor.addListener(this);
		
		System.out.println("Creating a radar at " + sensor.getName());
		//bearings are all like a compass with north = 0 and west = 270 and south = 180 and east = 90
		Location left = sensor.getLoc().getNewLocationFromBearingDistance(Math.toRadians(270), sensor.getRange());
//		System.out.println(sensor.getName() + " " + left.toString());
		
		Location right = sensor.getLoc().getNewLocationFromBearingDistance(Math.toRadians(90), sensor.getRange());
//		System.out.println(sensor.getName() + " " + right.toString());

		Location up = sensor.getLoc().getNewLocationFromBearingDistance(Math.toRadians(0), sensor.getRange());
//		System.out.println(sensor.getName() + " " + up.toString());

		Location down = sensor.getLoc().getNewLocationFromBearingDistance(Math.toRadians(180), sensor.getRange());
//		System.out.println(sensor.getName() + " " + down.toString());
		
		_upperLeft = new Location("",up.getLatitude(), left.getLongitude());
		_lowerRight = new Location("", down.getLatitude(), right.getLongitude());

	}
	
	public Radar getRadar() {
		return sensor;
	}
	
	  public void paint(Graphics g) {
		    super.paint(g);
		    
		    //multiply by two to get diameter
		    float range = sensor.getRange() * 2;
		    	   
		    //get the smallest axis
		    int dim = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
		    float scale = (float)dim / range;
		    float centerx = range / 2f;
		    float centery = range / 2f;
		    
		    for(int ring: ringDistance) {
		    	if(ring > sensor.getRange()) {
		    		ring = (int) sensor.getRange();
		    	}
		    			    	
		    	Location locOnGreatCircle = sensor.getLoc().getNewLocationFromBearingDistance(0d, ring);
		    	ring = (int) locOnGreatCircle.distanceTo(sensor.getLoc());
		        
		    	g.drawOval(getScaledValue(centerx, scale) - getScaledValue(ring, scale), 
		    			getScaledValue(centery, scale) - getScaledValue(ring, scale), 
		    			getScaledValue(ring, scale) * 2 , getScaledValue(ring, scale) * 2);
		    	
		    	
		    	if(ring > sensor.getRange()) {
		    		break;
		    	}

		    }

	    	//draw the gridlines
	    	g.drawLine(getScaledValue(centerx,  scale), 0, getScaledValue(centerx, scale), getScaledValue(range, scale));
	    	g.drawLine(0, getScaledValue(centery, scale), getScaledValue(range, scale), getScaledValue(centery, scale));

	    	//draw all the plots
	    	ArrayList<Plot> plots = sensor.getTrackedTargets();
	    	int plotRadius = getScaledValue(5, scale) < 5 ? 5 : getScaledValue(5,scale);
	    	if(plots != null) {
	    		for(int i = plots.size() - 1; i >= 0; --i ) {
	    			Plot plot = plots.get(i);
	    			Point coord = this.getCoordsFromLocationInWorld(plot.getLoc(), new Dimension(dim, dim));
	    			g.setColor(Color.black);
			    	g.fillOval(coord.x - plotRadius / 2, 
			    			coord.y - plotRadius / 2, 
			    			plotRadius, plotRadius);
	    		}
	    	}
	  }

	  public int getScaledValue(float value, float scale) {
		  return (int) ( value * scale);
	  }

	  
	@Override
	public void eventCallback(int eventInfo, Object data) {
		
		if(eventInfo == Event.PLOT_DATA_CHANGED) {
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


