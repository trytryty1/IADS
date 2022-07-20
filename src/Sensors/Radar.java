package Sensors;


import java.awt.Graphics;
import java.util.ArrayList;

import Messaging.Event;
import Messaging.EventManager;
import Networking.DatagramClient;
import System.Location;

public class Radar {
	
	Location loc;
	float range;
	ArrayList<Plot> trackedTargets;
    String name;
    EventManager mgr = new EventManager();

    DatagramClient client;
    
    boolean isActive;
    
	public Radar(String name, double latitude, double longitude, boolean isActive) {
		init( new Location(name, latitude, longitude), name, isActive);
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isActive() {
		return this.isActive;
	}
	
	public Radar(Location newLoc, boolean isActive) {
		init(new Location(newLoc.getName(), newLoc.getLatitude(), newLoc.getLongitude()), "", isActive);
	}
	
	public void init(Location loc, String name, boolean isActive) {
		this.loc = loc;
		setRange(100);
		this.name = name;
		trackedTargets = new ArrayList<Plot>();
		this.isActive = isActive;
	}
	
	
	private boolean isTargetVisible(Location tgt) {
		if(this.loc.distanceTo(tgt) < range) {
			return true;
		}
		return false;
	}
	
    public void addListener (Event lst) {
        mgr.registerCallBack(lst);
    }

    public void dataChanged () {
    	mgr.notify(Event.PLOT_DATA_CHANGED, null);
    }
    
	public void checkAvailableTargetInRange(Plot newPlot) {
		boolean found = false;
		
	//	System.out.println("Checking target " + newPlot.toString());
		for(Plot plot: trackedTargets) {
			if(plot.getMyId() == newPlot.getMyId()) {
				found = true;
				if(this.isTargetVisible(newPlot.getLoc())) {
					//System.out.println("Target is visisble and already being tracked " + plot.serialize());
					plot.refresh();
					plot.setLoc(newPlot.loc);
					sendPlot(newPlot);
					break;
				} else {
					System.out.println("Target is not visible anymore");
					trackedTargets.remove(plot);
					break;
				}
			} 
		}
		
		if(!found) {
			//System.out.println("Target is not already being tracked");

			if(this.isTargetVisible(newPlot.getLoc())) {
				System.out.println("Target is visisble and added to tracked queue");
				trackedTargets.add(newPlot);
				sendPlot(newPlot);
				found = true;
			}
		}
		
		if(found) {
			dataChanged();
		}
	}

	public void sendPlot(Plot plot) {
		if(isActive) {
			client.send(plot.serialize());
		}
	}
	
	public void decayPlots() {
		for(int i = trackedTargets.size() - 1; i >= 0; --i) {
			Plot plot = trackedTargets.get(i);
			plot.decay();
			if(plot.isStale()) {
				trackedTargets.remove(plot);
			}
		}
	}
	
	public void draw(Graphics g, float scale) {
		
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public ArrayList<Plot> getTrackedTargets() {
		return trackedTargets;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DatagramClient getClient() {
		return client;
	}

	public void setClient(DatagramClient client) {
		this.client = client;
	}
	
}
