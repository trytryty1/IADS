package System;

public class Waypoint {
	
	public static final int TICK_MEASURED_IN_MILLIS = 500;
	
	Location _dest, _origin;
	int _timeOverTarget;
	Location _currentLoc;
	int _startTime;
	int _currentTime;
	
	public Waypoint(Location dest, Location origin, int timeOverTarget, int startTime ) {
		_dest = dest;
		_origin = origin;
		_currentLoc = _origin;
		_timeOverTarget = timeOverTarget;
		_startTime = startTime;
	}
	
	public void startWaypoint(int startTime) {
		_startTime = startTime;
		_currentTime = _startTime;
		
	}
	
	
	public Location updateLocation(int time) {
		_currentTime = time;
		
		//if over then send the dest waypoint as the location
		if(_currentTime > _timeOverTarget) {
			return _dest;
		}
		
		int timeDelta = _currentTime - (_startTime);
		System.out.println("Waypoint tot: " + _timeOverTarget + " currentime: " + _currentTime + "time delta: " + timeDelta);
		float percent = (float)timeDelta / ((float)_timeOverTarget - (float)_startTime);
	
		double currentLat = _origin.getLatitude() + (_dest.getLatitude() - _origin.getLatitude()) * percent;
		double currentLong = _origin.getLongitude() + (_dest.getLongitude() - _origin.getLongitude()) * percent;
		
		return new Location("",currentLat, currentLong);
	}
	
	public boolean isComplete() {
		return _currentTime >= _timeOverTarget;
	}

	public Location getDest() {
		return _dest;
	}

	public void setDest(Location _dest) {
		this._dest = _dest;
	}

	public Location getOrigin() {
		return _origin;
	}

	public void setOrigin(Location _origin) {
		this._origin = _origin;
	}

	public int getTimeOverTarget() {
		return _timeOverTarget;
	}

	public void setTimeOverTarget(int _timeOverTarget) {
		this._timeOverTarget = _timeOverTarget;
	}

	public int getCurrentTime() {
		return _currentTime;
	}

	public void setCurrentTime(int _currentTime) {
		this._currentTime = _currentTime;
	}
}
