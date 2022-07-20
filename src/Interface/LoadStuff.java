package Interface;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Networking.Aggregator;
import Networking.DatagramClient;
import Networking.DatagramServer;
import Sensors.Radar;
import System.FlightProfile;
import System.Location;
import System.Waypoint;
import System.WorldBounds;
import System.Managers.SimulatorInputManager;
import System.Managers.SimulatorManager;


public class LoadStuff {
	
	public static WorldBounds getBounds(String filename) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));

	        String line;
			line = br.readLine();
			
	        while (line != null) {
	        	if(line.startsWith("[WORLDCOORDS")) {
	        		String[] parts = line.split(":");
	        		if(parts.length > 1) {
	        			String[] coords = parts[1].split(";");
	        			if(coords.length == 2) {
	        				return new WorldBounds(Location.deserialize(coords[0]),
	        									   Location.deserialize(coords[1]));
	        			}
	        		}
	        	
	        	}
	        line = br.readLine();
	      
	        } 
	        br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not open file " + filename);
		}
		
		System.out.println("There was an error getting world bounds");
		System.out.println("Use \"[WORLDCOORDS:lat1,lon1;lat2,lon2\"");

		return null;
	}
	
	public static ArrayList<Aggregator> loadAggregators(String filename) {
		BufferedReader br;
		ArrayList<Aggregator> aggregators = new ArrayList<Aggregator>();
		
		try {
			br = new BufferedReader(new FileReader(filename));

	        String line = br.readLine();
	        while (line != null) {
	        	if(line.startsWith("[AGGREGATOR")) {
	        		Aggregator aggregator = new Aggregator();
	        				
	        		String[] parts = line.split(":");
	        		//listening ports are the second element
	        		String[] portStrings = parts[1].split(",");
	        		for(String portStr:portStrings) {
	        			int port = Integer.parseInt(portStr);
	        			DatagramServer server = new DatagramServer(port);
	        			aggregator.addOrigin(server);
	        		}
	        		//the third element is the destinations splits with a ';'
	        		String[] destStrings = parts[2].split(";");
	        		for(String destStr:destStrings) {
	        			String[] elements = destStr.split(",");
	        			String ip = elements[0].trim();
	        			String portStr = elements[1].trim();
	        			aggregator.addDest(ip,  Integer.parseInt(portStr));
	        		}
	        		if(parts.length <= 4 && parts[3].trim().toUpperCase().equals("DISPLAY_ON")) {
	        			aggregator.setShouldDisplay(true);
	        		}
	        		aggregators.add(aggregator);
	        	}
	        	line = br.readLine();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		if(aggregators.size() > 0) {
			return aggregators;
		} else {
			return null;
		}
	}
	public static SimulatorInputManager loadSimInputManager(String filename) {

		BufferedReader br;
		SimulatorInputManager manager = null;
		
		try {
			br = new BufferedReader(new FileReader(filename));

	        String line = br.readLine();
	        while (line != null) {
	        	if(line.startsWith("[SIMINPUT")) {
	        		String[] parts = line.split(":");
	        		int port = Integer.parseInt(parts[1].trim());
	        		DatagramServer server = new DatagramServer(port);
	        		manager = new SimulatorInputManager(server);
	        		if(parts[2].trim().toUpperCase().equals("DISPLAY_ON") ) {
	        			manager.setShouldDisplay(true);
	        		} else {
	        			manager.setShouldDisplay(false);
	        		}
	        	}
	        	line = br.readLine();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		
		}
	
		return manager;
		
	}
	
	public static SimulatorManager loadSimulator(String filename) {
		BufferedReader br;
		SimulatorManager simulatorManager = null;
		
		try {
			br = new BufferedReader(new FileReader(filename));

	        String line = br.readLine();
	        while (line != null) {
	        	if(line.startsWith("[SIMULATOR")) {
	        		String[] parts = line.split(":");
	        		//parts[1] - IP addresses to send to
	        		DatagramClient client = new DatagramClient();
	        		
	        		for(int i = 1; i < parts.length; ++i) {
	        			String dest = parts[i];
	        			String[] destParts = dest.split(",");
	        			client.addDest(destParts[0].trim(), Integer.parseInt(destParts[1].trim()));
	        		}
	        		
	        		simulatorManager = new SimulatorManager(client);
	        		line = br.readLine();
	        		while(line != null) {
	        			//read the flight paths
	        			if(line.startsWith("[FLIGHTPATH")) {
	        				String[] waypoints = line.split(":");
	        				//second element is startime
	        					        				
	        				FlightProfile flight = null;
	        				
	        				Location start = null;
	        				Location end = null;
	        				int tot;
	        				int starttime;
	        				int waypointStartTime = 0;
	        				
	    	        		for(int i = 1; i < waypoints.length; ++i) {
	    	        			
	    	        			String[] elements = waypoints[i].split(",");
	    	        			//first two elements are Lat,Long
	    	        			Location temp = new Location("", 
    	        						Double.parseDouble(elements[0]),
    	        						Double.parseDouble(elements[1]));
	    	        			
	    	        			//third element is time over target
	    	        			tot = (int)Double.parseDouble(elements[2]);
	    	        			
    	        				if(i == 1) {
    	    	        			start = temp;
    	        					starttime = tot;
    	        					waypointStartTime = starttime;
    	        					flight = new FlightProfile(starttime);
    	        					continue;
    	        				} else  {
    	        					end = temp;
    	        				}
		    	        			
    	        				Waypoint waypoint = new Waypoint(end,start,tot,waypointStartTime);
    	        				waypointStartTime = tot;
		    	        		flight.addWaypoint(waypoint);
		    	 	        	start = end;
	    	        			
	    	        		}
	    	        		simulatorManager.addProfile(flight);
	        			}
	        			line = br.readLine();
	        		}
	        	}
	        	line = br.readLine();
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Errors reading flight profile");
		}
		
		return simulatorManager;
		
	}
	
	public static ArrayList<Radar> loadProducers(String filename) {
		ArrayList<Radar> radars = new ArrayList<Radar>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));

	        String line = br.readLine();
	        while (line != null) {
	        	if(line.startsWith("[PRODUCER")) {
	        		String[] parts = line.split(":");
	        		if(parts[1].trim().equals("RADAR")) {
	        			String name = parts[2];
	        			String s_loc = parts[3];
	        			Location loc = Location.deserialize(s_loc);
	        			DatagramClient client = new DatagramClient();
	        			client.addDest(parts[5], Integer.parseInt(parts[6]));
	        			float range = Float.parseFloat(parts[4]);
	        			
	        			Radar radar = new Radar(loc, true);
	        			radar.setName(name);
	        			radar.setRange(range);
	        			radar.setClient(client);
	        			radars.add(radar);
	        		} else {
	        			System.out.println("Something wrong with line: " + line);
	        		}
	        	}
	        	line = br.readLine();
	        }
	        br.close();
	        return radars;
	    } catch (FileNotFoundException e) {
			System.out.println("Could not find the file " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read from the file " + filename);
			e.printStackTrace();
		} finally {
	        
	    }
		return null;
	}
	
}
