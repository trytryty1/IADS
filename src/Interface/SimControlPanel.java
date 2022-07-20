package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Messaging.Event;
import System.Managers.SimulatorManager;

public class SimControlPanel extends JPanel implements Event {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JButton showButton = new JButton("Show Sim View");
	JButton startSimulation = new JButton("Start Sim");
	JButton restartSimulation = new JButton("Restart Sim");
	
	JLabel timeLabel = new JLabel();
	
	//JButton pauseSimulation = new JButton("Pause Sim");
	
	SimulatorPanel _simPanel;
	
	//need a reference to this to add things to the desktop pane
	MainWindow _parent;
	SimulatorManager _simManager;
	
	public SimControlPanel(MainWindow parent, SimulatorManager simManager) {
	
		_simManager = simManager;
		_parent = parent;
		
	    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(showButton);
		this.add(startSimulation);
		this.add(restartSimulation);
		this.add(timeLabel);
		restartSimulation.setVisible(false);
		
		showButton.addActionListener(new ActionListener() {
			
	        public void actionPerformed(ActionEvent e) {
	        	parent.showSimViewer();
	        }
	      });
		
		restartSimulation.addActionListener(new ActionListener() {
			
	        public void actionPerformed(ActionEvent e) {
	        	if(_simManager != null) {
	        		_simManager.stopSimulation();
	        		_simManager.startSimulation();
	        		startSimulation.setText("Pause");
	        	}
	        }
	      });
	
		startSimulation.addActionListener(new ActionListener() {
			
	        public void actionPerformed(ActionEvent e) {
	        	if(!_simManager.isRunning()) {
	        		_simManager.startSimulation();
	        		startSimulation.setText("Pause");
	        		restartSimulation.setVisible(true);

	        	} else if (_simManager.isPaused()) {
	        		_simManager.unpauseSimulation();
	        		startSimulation.setText("Pause");
	        	} else {
	        		_simManager.pauseSimulation();
	        		startSimulation.setText("Unpause");
	        	}
	        }
	      });
		

		
	}

	@Override
	public void eventCallback(int eventInfo, Object data) {

		if (eventInfo == Event.SIM_TICK) {
			if(data instanceof Integer) {
				Integer time = (Integer)data;
				timeLabel.setText(time.toString());
			}
		}
	}
}
