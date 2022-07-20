package System.Managers;

import Messaging.DataChangeEvent;
import Messaging.Event;
import Messaging.EventManager;
import Messaging.NetworkReceiveEvent;
import Networking.DatagramServer;
import Sensors.Plot;

public class SimulatorInputManager implements Event{

	DatagramServer server;
	//ArrayList<Plot> plots = new ArrayList<Plot>();
	EventManager evtMgr = new EventManager();
	boolean shouldDisplay;
	
	public SimulatorInputManager(DatagramServer server) {
		this.server = server;
		this.server.addListener(this);
	}

	public void startListener() {
		if(!server.isAlive()) {
			this.server.start();
		}
	}
	
	@Override
	public void eventCallback(int eventInfo, Object data) {
		if(eventInfo == Event.NETWORK_DATA_RECEIVED) {
			NetworkReceiveEvent event = (NetworkReceiveEvent) data;
			Plot newPlot = Plot.deserialize(event.getData());

			DataChangeEvent sendEvent = new DataChangeEvent(newPlot);
			evtMgr.notify(Event.PLOT_DATA_CHANGED, sendEvent);
		}
		
	}
	
	   public void addListener (Event lst) {
	    	evtMgr.registerCallBack(lst);
	    }

	public boolean isShouldDisplay() {
		return shouldDisplay;
	}

	public void setShouldDisplay(boolean shouldDisplay) {
		this.shouldDisplay = shouldDisplay;
	}
	   
	
}
