package System.Managers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Messaging.Event;
import Messaging.EventManager;
import Messaging.FlightChangedEvent;
import Networking.DatagramClient;
import Sensors.Plot;
import System.FlightProfile;


public class SimulatorManager implements Event {
	Timer timer;
	ArrayList<FlightProfile> profiles;
	int counter;
	DatagramClient _sender;
	boolean isPaused;
	boolean isRunning;
    EventManager mgr = new EventManager();

	public SimulatorManager(DatagramClient sender) {
		counter = 0;
		_sender = sender;
		profiles = new ArrayList<FlightProfile>();
		isRunning = false;
	}
	
	public void startSimulation() {
		//if this is the first time then setup the decay counter
		counter = 0;
		isRunning = true;
		if (timer == null) {
		    timer = new Timer();
		    final TimerTask timerTask = new TimerTask() {
		        @Override
		        public void run() {
		        	if(!isPaused) {
		        		++counter;
		        		mgr.notify(Event.SIM_TICK, new Integer(counter));
		        		for(FlightProfile profile: profiles) {
		        			profile.update(counter);
		        		}
		        	}
		        }
		    };
		    timer.schedule(timerTask, 1000, 1000);  //decay the plots every second
		}		
	}
	
	public void pauseSimulation() {
		isPaused = true;
	}
	
	public void unpauseSimulation() {
		isPaused = false;
	}
	
	public void addProfile(FlightProfile profile) {
		profiles.add(profile);
		profile.addListener(this);
	}

   public void addListener (Event lst) {
        mgr.registerCallBack(lst);
    }
	
	@Override
	public void eventCallback(int eventInfo, Object data) {
		if(eventInfo == Event.FLIGHT_CHANGED) {
			FlightChangedEvent event = (FlightChangedEvent)data;
			Plot newPlot = new Plot(event.getLoc());
			newPlot.setMyId(event.getId());
			_sender.send(newPlot.serialize());
		}	
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void stopSimulation() {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
		
	}
}
