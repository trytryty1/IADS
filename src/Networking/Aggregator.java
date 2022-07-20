package Networking;

import java.util.ArrayList;

import Messaging.DataChangeEvent;
import Messaging.Event;
import Messaging.EventManager;
import Messaging.NetworkReceiveEvent;
import Sensors.Plot;

public class Aggregator implements Event {

	DatagramClient client;
	ArrayList<DatagramServer> servers = new ArrayList<DatagramServer>();
	EventManager evtMgr = new EventManager();
	
	boolean shouldDisplay;
	
	public Aggregator() {
		client = new DatagramClient();
		shouldDisplay = false;
	}
	
	public void addOrigin(DatagramServer server) {
		servers.add(server);
		server.addListener(this);
	}
	
	public void startListening() {
		for(DatagramServer server:servers) {
			if(!server.isAlive()) {
				System.out.println("Aggregator listening on port " + server.getPort());
				server.start();
			}
		}
	}
	
	public void addDest(String ip, int port) {
		client.addDest(ip, port);
	}
	
   	public void addListener (Event lst) {
   		evtMgr.registerCallBack(lst);
   	}
  
	@Override
	public void eventCallback(int eventInfo, Object data) {
		if(eventInfo == Event.NETWORK_DATA_RECEIVED) {
			NetworkReceiveEvent event = (NetworkReceiveEvent) data;
			
			//just forward the data on
			client.send(event.getData());
			
			//this is for display purposes only
			if(shouldDisplay) {
				Plot newPlot = Plot.deserialize(event.getData());
				DataChangeEvent sendEvent = new DataChangeEvent(newPlot);
				evtMgr.notify(Event.PLOT_DATA_CHANGED, sendEvent);
			}
		}
	}

	public boolean isShouldDisplay() {
		return shouldDisplay;
	}

	public void setShouldDisplay(boolean shouldDisplay) {
		this.shouldDisplay = shouldDisplay;
	}
}
