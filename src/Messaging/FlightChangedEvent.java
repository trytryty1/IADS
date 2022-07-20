package Messaging;

import System.Location;

public class FlightChangedEvent {
	private Location loc;
	private int id;
	
	public FlightChangedEvent(Location _loc, int _id) {
		loc = _loc;
		id = _id;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
