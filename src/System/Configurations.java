package System;

import Sensors.Plot;

public class Configurations {

	public static Location _upperLeft;
	public static Location _lowerRight;

	public static String configFile = "res\\radars\\configuration.txt";
	public static boolean isInWorld(Plot newPlot) {
		double lon = newPlot.getLoc().getLongitude();
		double lat = newPlot.getLoc().getLatitude();
		
		if(lat <= _upperLeft.getLatitude() && lat >= _lowerRight.getLatitude() && 
				lon >= _upperLeft.getLongitude() && lon <= _lowerRight.getLongitude()) {
			return true;
		} 
		
		return false;
	}

	
}
