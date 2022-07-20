package System.Managers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Messaging.DataChangeEvent;
import Messaging.Event;
import Sensors.Plot;
import Sensors.Radar;

public class RadarManager implements Event{
	 ArrayList<Radar> radars = new ArrayList<Radar>();
	 Timer timer = null;
	 
	 static RadarManager radarMgr = null;
	 
	 public static RadarManager getInstance() {
		 if(radarMgr == null) {
			 radarMgr = new RadarManager();
		 }
		 return radarMgr;
	 }
	 
	 
	public void addRadar(Radar newRadar) {
		
		radars.add(newRadar);

		//if this is the first time then setup the decay counter
		if (timer == null) {
		    timer = new Timer();
		    final TimerTask timerTask = new TimerTask() {
		        @Override
		        public void run() {
		        	for(Radar radar: radars) {
						radar.decayPlots();
					}	  
		        }
		    };

		    
		    timer.schedule(timerTask, 1000, 1000);  //decay the plots every second
		}
		
		
	}
	
	public  ArrayList<Radar> getRadars() {
		return radars;
	}
	
	public  void updatePlots(Plot newPlot) {
		for(Radar radar: radars) {
			radar.checkAvailableTargetInRange(newPlot);
		}
	}


	@Override
	public void eventCallback(int eventInfo, Object data) {
		if(eventInfo == Event.PLOT_DATA_CHANGED) {
			//System.out.println("plot updated");
			DataChangeEvent event = (DataChangeEvent) data;
			updatePlots(event.getNewPlot());
			//System.out.println(event.getNewPlot().serialize());
		}
		
	}


	public void setSimulatorInputManager(SimulatorInputManager simulatorInputManager) {
		
		simulatorInputManager.addListener(this);
		
	}
}
