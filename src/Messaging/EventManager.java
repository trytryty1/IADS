package Messaging;

import java.util.ArrayList;

public class EventManager {
	ArrayList<Event> callbacks = new ArrayList<Event>();
	
	public void registerCallBack(Event e) {
		callbacks.add(e);
	}
	
	public void notify(int eventInfo, Object data) {
		for(Event event: callbacks) {
			event.eventCallback(eventInfo, data);
		}
	}
	
	public void removeCallBack(Event e) {
		callbacks.remove(e);
	}
}
