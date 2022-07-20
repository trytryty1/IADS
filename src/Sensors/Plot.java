package Sensors;

import System.Location;

public class Plot {
	static int idCounter;
	
	int myId;
	Location loc;
	int staleCounter;
	
	
	public Plot(Location loc) {
		this.loc = loc;
		myId = idCounter;
		idCounter++;
		staleCounter = 10;
	}
	
	public boolean isStale() {
		return staleCounter <= 0;
	}
	
	public void refresh() {
		staleCounter = 10;
	}
	
	public void decay() {
		if(staleCounter > 0) {
			--staleCounter;
		}
	}

	public String serialize() {
		return "" + myId + ":" + loc.serialize();
	}
	
	public static Plot deserialize(String data) {
		
		String[] parts = data.split(":");
		int id = Integer.parseInt(parts[0]);
		Location loc = Location.deserialize(parts[1]);
		
		Plot plot = new Plot(loc);
		plot.setMyId(id);
		return plot;
		
	}
	
	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
}
