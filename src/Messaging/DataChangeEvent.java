package Messaging;

import Sensors.Plot;

public class DataChangeEvent extends Object{

	Plot newPlot;
	
	public DataChangeEvent( Plot plot) {
		newPlot = plot;
	}

	public Plot getNewPlot() {
		return newPlot;
	}

	public void setNewPlot(Plot newPlot) {
		this.newPlot = newPlot;
	}

}
