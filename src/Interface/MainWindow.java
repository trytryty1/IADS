package Interface;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import Networking.Aggregator;
import Sensors.Radar;
import System.Configurations;
import System.Location;
import System.Managers.PlotManager;
import System.Managers.RadarManager;
import System.Managers.SimulatorInputManager;
import System.Managers.SimulatorManager;

public class MainWindow extends JFrame {
	

	private static final long serialVersionUID = 1L;
	JDesktopPane desktop;
    PlotManager plotMgr;
    SimulatorInputManager simulatorInputManager;
    ArrayList<RadarInternalFrame> radarFrames = new ArrayList<RadarInternalFrame>();
    
	public MainWindow() throws InterruptedException {
		super();
		Images.loadImages();
		plotMgr = new PlotManager();

	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    desktop = new JDesktopPane();
	    add(desktop, BorderLayout.CENTER);
	
	    ArrayList<Radar> radars = LoadStuff.loadProducers(Configurations.configFile);

	    ArrayList<Aggregator> aggregators = LoadStuff.loadAggregators(Configurations.configFile);

	    simulatorInputManager = LoadStuff.loadSimInputManager(Configurations.configFile);
	    
	    if(simulatorInputManager != null) {
	    	simulatorInputManager.startListener();
		    RadarManager.getInstance().setSimulatorInputManager(simulatorInputManager);
	    } else {
	    	System.out.println("Warning there is not sim input linked in.  This program will not get any simulator input");
	    }
	    
	    
	    SimulatorManager simulatorManager = LoadStuff.loadSimulator(Configurations.configFile);
	    
	    if(simulatorManager != null) {
	    	addSimControl(simulatorManager);
	    }
	    
	    if(simulatorInputManager != null ) {
	    	simulatorInputManager.addListener(RadarManager.getInstance());
	    }
	    
	    if(aggregators != null ) {
	    	for(Aggregator agg: aggregators) {
	    		//start all the listening sockets
	    		agg.startListening();
	    		if(agg.isShouldDisplay()) {
	    			AggregatorPanel aggPanel = addAggregatorPanel( );
	    			agg.addListener(aggPanel);
	    		}
	    	}
	    }
	    
		for(Radar radar: radars) {
		    RadarManager.getInstance().addRadar(radar);
		    //all radars will be added through the controlPanel
		    //it will cause them to be shown if they are set to be active
			//addRadarPanel(radar);
		}

		addControlPanel();
		
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(500, 300);
	    setVisible(true);

	}
	
	public void addControlPanel() {
	    JInternalFrame internalFrame = new JInternalFrame("Control Panel", true, false, false, true);
	    desktop.add(internalFrame);

	    internalFrame.setBounds(25, 25, 200, 200);
	    
		    
	    RadarControlPanel ctrlPanel = new RadarControlPanel(this);
	    internalFrame.add(ctrlPanel);
	    internalFrame.setVisible(true);		
	    
	}
	
	public void addSimControl(SimulatorManager simManager) {
	    JInternalFrame internalFrame = new JInternalFrame("Sim Control Panel", false, false, false, true);
	    desktop.add(internalFrame);

	    internalFrame.setBounds(25, 25, 200, 200);
	    
		    
	    SimControlPanel ctrlPanel = new SimControlPanel(this, simManager);
	    simManager.addListener(ctrlPanel);
	    internalFrame.add(ctrlPanel);
	    internalFrame.setVisible(true);		
	}
	
	public AggregatorPanel addAggregatorPanel( ) {
		
		ArrayList<Radar> radars = RadarManager.getInstance().getRadars();
		
		Location loc1 = Configurations._upperLeft;
		Location loc2 = Configurations._lowerRight;
		
		AggregatorPanel aggPanel = new AggregatorPanel(loc1, loc2, radars);

		for(Radar radar: radars) {
			radar.addListener(aggPanel);
		}
		
	    JInternalFrame internalFrame = new JInternalFrame("Aggregate View", true, true, true, true);
	    desktop.add(internalFrame);

	    internalFrame.setBounds(25, 25, 200, 100);
	    
	    internalFrame.add(aggPanel);
	    internalFrame.setVisible(true);
	    return aggPanel;
	}
	
	public JInternalFrame addRadarPanel(Radar rdr) {
		
	    RadarPanel radarPanel = new RadarPanel(rdr);

	    
	    RadarInternalFrame internalFrame = new RadarInternalFrame(rdr.getName(), true, false, true, true, radarPanel);
	    desktop.add(internalFrame);

	    internalFrame.setBounds(25, 25, 200, 100);
	    
	    internalFrame.add(radarPanel);
	    internalFrame.setVisible(true);

	    radarFrames.add(internalFrame);
	    
	    return internalFrame;
	}
	
	public void removeRadarPanel(String radarTitle) {
	
		for(RadarInternalFrame radarFrame: radarFrames) {
			if(radarFrame.getRadarPanel().getRadar().getName().equals(radarTitle)) {
				radarFrame.dispose();
				radarFrames.remove(radarFrame);
				break;
			}
		}
	}
	
	public void showSimViewer()  {
		
		Location loc1 = Configurations._upperLeft;
		Location loc2 = Configurations._lowerRight;
		
		ArrayList<Radar> radars = RadarManager.getInstance().getRadars();

		SimulatorPanel simPanel = new SimulatorPanel(loc1, loc2, radars);

		
		if(simulatorInputManager != null) {
			simulatorInputManager.addListener(simPanel);
		} else {
			System.out.println("No simulation input.");
			return;
		}
		
		
	    JInternalFrame internalFrame = new JInternalFrame("Sim Input View", true, true, true, true);
	    desktop.add(internalFrame);

	    internalFrame.setBounds(25, 25, 200, 100);
	    
	    internalFrame.add(simPanel);
	    internalFrame.setVisible(true);
	    return;	
	}
		
}
