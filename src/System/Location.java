package System;

public class Location {
	private String name;
    private double longitude;
    private double latitude;   
   
    // create and initialize a point with given name and
    // (latitude, longitude) specified in degrees
    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    // return distance between this location and that location
    // measured in statute miles
    public double distanceTo(Location that) {
    	double earthRadius = 3959d;
    		double longTheta = getLongitudeAsRadians() - that.getLongitudeAsRadians();
    		double latTheta = getLatitudeAsRadians() - that.getLatitudeAsRadians();
    		
    		double a = Math.sin(latTheta / 2) * Math.sin(latTheta / 2) + Math.cos(getLatitudeAsRadians()) * Math.cos(that.getLatitudeAsRadians()) *
    					Math.sin(longTheta / 2) * Math.sin(longTheta / 2);
    		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    		return earthRadius * c;
    		

    }

    //bearing is in radians
    //distance is in miles
    public Location getNewLocationFromBearingDistance(double bearing, double distance) {
    	double earthRadius = 3959d; //Mean Radius of the earth in miles

    	double brngRad = bearing;
        double latRad = Math.toRadians(latitude);
        double lonRad = Math.toRadians(longitude);
        //int earthRadius = 3959;
        double distFrac = distance / earthRadius;

        double latitudeResult = Math.asin(Math.sin(latRad) * Math.cos(distFrac) + Math.cos(latRad) * Math.sin(distFrac) * Math.cos(brngRad));
        double a = Math.atan2(Math.sin(brngRad) * Math.sin(distFrac) * Math.cos(latRad), Math.cos(distFrac) - Math.sin(latRad) * Math.sin(latitudeResult));
        double longitudeResult = (lonRad + a + 3 * Math.PI) % (2 * Math.PI) - Math.PI;
      		
    	return new Location("",Math.toDegrees(latitudeResult),Math.toDegrees(longitudeResult));

    }
    
    // return string representation of this point
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }

    public String serialize() {
    	return "" + latitude + "," + longitude;
    }
    public static Location deserialize(String data) {
    	String[] parts = data.split(",");
    	double lat = Double.parseDouble(parts[0].trim());
    	double lon = Double.parseDouble(parts[1].trim());
    	
    	return new Location("", lat, lon);
    }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitudeAsRadians() {
		return Math.toRadians(longitude);
	}
	
	public double getLatitudeAsRadians() {
		return Math.toRadians(latitude);
	}
	

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void addLongitude(double delta) {
		longitude += delta;
	}
	
	public void addLatitude(double delta) {
		latitude += delta;
	}
	public double getBearingBetweenLoc(Location loc) {
		double y = Math.sin( loc.getLongitudeAsRadians() - getLongitudeAsRadians()) * Math.cos(loc.getLatitudeAsRadians());
		double x = Math.cos(getLatitudeAsRadians())*Math.sin(loc.getLatitudeAsRadians()) -
		        Math.sin(getLatitudeAsRadians())*Math.cos(loc.getLatitudeAsRadians())*Math.cos(loc.getLongitudeAsRadians() - getLongitudeAsRadians());
		double degrees = Math.toDegrees(Math.atan2(y, x)) ;
		degrees += 180;
		degrees = (degrees + 360) % 360;
		return Math.toRadians(degrees);
	}
}
