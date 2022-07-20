package System;

import java.util.ArrayList;

import Messaging.Event;
import Messaging.EventManager;
import Messaging.FlightChangedEvent;

public class FlightProfile {

	private static int ID_COUNTER = 1;
	
	ArrayList<Waypoint> _waypoints;
	Location currentLoc;
	EventManager evtMgr;
	boolean isActive;
	int id;
	int _startTime;
	
	public FlightProfile(int startTime) {
		_waypoints = new ArrayList<Waypoint>();
		evtMgr = new EventManager();
		id = ID_COUNTER;
		++ID_COUNTER;
		isActive = false;
		_startTime = startTime;

	}
	
	public void addWaypoint(Waypoint point) {
		_waypoints.add(point);
	}
	
	public void update(int time) {
		if(time < _startTime) {
			isActive = false;
			return;
		}
		isActive = true;
 		boolean found = false;
		for(Waypoint waypoint:_waypoints) {
			if(time <= waypoint.getTimeOverTarget()) {
				//waypoints will be in cronological order and therefore if a waypoint is less than time
				//then it is the current waypoint
				currentLoc = waypoint.updateLocation(time);
				dataChanged();
				found = true;
				break;
			}
		}
		if(!found) {
			isActive = false;
		}
	}
	
	public boolean isActive() {
		return isActive;
	}
	
   public void addListener (Event lst) {
    	evtMgr.registerCallBack(lst);
    }
   
   public void dataChanged () {
		FlightChangedEvent event = new FlightChangedEvent( currentLoc, id );
	    evtMgr.notify(Event.FLIGHT_CHANGED, (Object) event);
   }
}
