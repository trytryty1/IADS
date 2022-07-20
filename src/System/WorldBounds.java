package System;

public class WorldBounds {

	Location _upperLeftBound;
	Location _lowerRightBound;

	public WorldBounds(Location upperLeftBound, Location lowerRightBound) {
		this.setUpperLeftBound(upperLeftBound);
		this.setLowerRightBound(lowerRightBound);
	}
	
	public Location getUpperLeftBound() {
		return _upperLeftBound;
	}

	public void setUpperLeftBound(Location _upperLeftBound) {
		this._upperLeftBound = _upperLeftBound;
	}

	public Location getLowerRightBound() {
		return _lowerRightBound;
	}

	public void setLowerRightBound(Location _lowerRightBound) {
		this._lowerRightBound = _lowerRightBound;
	}
}
