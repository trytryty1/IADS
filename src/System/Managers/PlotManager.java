package System.Managers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Messaging.DataChangeEvent;
import Messaging.Event;
import Messaging.EventManager;
import Sensors.Plot;
import System.Configurations;

public class PlotManager implements Event {

	ArrayList<Plot> plots = new ArrayList<Plot>();
	EventManager evtMgr = new EventManager();
	
	public PlotManager() {
		Timer timer = null;
		//if this is the first time then setup the decay counter
		if (timer == null) {
		    timer = new Timer();
		    final TimerTask timerTask = new TimerTask() {
		        @Override
		        public void run() {
		        	for(Plot plot:plots) {
		        		plot.decay();
		        	}
		        }
		    };
		    timer.schedule(timerTask, 1000, 1000);  //decay the plots every second
		}
	}
	
    public void addListener (Event lst) {
    	evtMgr.registerCallBack(lst);
    }
    
	public void addPlot(Plot plot) {
		
		boolean found = false;
		
		for(Plot _plot: plots) {
			if(plot.getMyId() == _plot.getMyId()) {
				found = true;
				if(Configurations.isInWorld(plot)) {
					plot.refresh();
					_plot.setLoc(plot.getLoc());
					break;
				} else {
					plots.remove(plot);
					break;
				}
			} 
		}
		
		if(!found) {
			if(Configurations.isInWorld(plot)) {
					plots.add(plot);
			}
		}
		
	}
	
//	  public void dataChanged (Plot newPlot) {
//		  DataChangeEvent event = new DataChangeEvent( newPlot);
//		  
//	    evtMgr.notify(Event.PLOT_DATA_CHANGED, (Object) event);
//	  }

	@Override
	public void eventCallback(int eventInfo, Object data) {
		if(eventInfo == Event.PLOT_DATA_CHANGED) {
			DataChangeEvent event = (DataChangeEvent) data;
			addPlot(event.getNewPlot());
			evtMgr.notify(Event.PLOT_DATA_CHANGED, data);
		}
	}

	public ArrayList<Plot> getPlots() {
		return plots;
	}
}
