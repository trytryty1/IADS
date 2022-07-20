package Interface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Sensors.Radar;
import System.Managers.RadarManager;

public class RadarControlPanel extends JPanel implements ItemListener {


	private static final long serialVersionUID = 1L;
	//need a reference to this to add things to the desktop pane
	MainWindow _parent;
	ArrayList<RadarCheckBox> radarCheckBoxes = new ArrayList<RadarCheckBox>();
	
	public RadarControlPanel(MainWindow parent) {
		
		_parent = parent;

		this.add(getRadarSelectPanel(RadarManager.getInstance().getRadars()));
		
		JButton showAll = new JButton("Show All");
		JButton hideAll = new JButton ("Hide All");
		this.add(showAll);
		this.add(hideAll);
		
		showAll.addActionListener(new ActionListener() {
			
	        public void actionPerformed(ActionEvent e) {
	        	for(RadarCheckBox checkBox: radarCheckBoxes) {
	        		if(!checkBox.isSelected()) {
	        			checkBox.setSelected(true);
	        		}
	        	}
	        }
	      });
		
		hideAll.addActionListener(new ActionListener() {
			
	        public void actionPerformed(ActionEvent e) {
	        	for(RadarCheckBox checkBox: radarCheckBoxes) {
	        		if(checkBox.isSelected()) {
	        			checkBox.setSelected(false);
	        		}
	        	}
	        }
	      });
	}
	
	public JScrollPane getRadarSelectPanel(ArrayList<Radar> radars) {
    
	    JPanel checkBoxPanel = new JPanel();
	    checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

	    checkBoxPanel.setBorder(BorderFactory.createTitledBorder("Toggle Radars"));
	    checkBoxPanel.setToolTipText("Turn Radars on/off");
	    checkBoxPanel.setSize(new Dimension(this.getWidth(), (int)(this.getHeight() * 0.4)));
	    
	    for(Radar radar: radars) {
			RadarCheckBox radarCheckBox = new RadarCheckBox(radar.getName(), radar);
			radarCheckBoxes.add(radarCheckBox);
			radarCheckBox.addItemListener(this);
			checkBoxPanel.add(radarCheckBox);
			radarCheckBox.setSelected(radar.isActive());
	    }
	    
	    JScrollPane vertical = new JScrollPane(checkBoxPanel);
	    vertical.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    vertical.setVisible(true);
	    this.add(vertical);
	    
	    return vertical;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if(source instanceof RadarCheckBox) {
			RadarCheckBox checkbox = (RadarCheckBox) source;
			if(checkbox.isSelected()) {
				_parent.addRadarPanel(checkbox.get_radar());
				checkbox.get_radar().setIsActive(true);
			} else {
				_parent.removeRadarPanel(checkbox.get_radar().getName());
				checkbox.get_radar().setIsActive(false);

			}
		}
		_parent.repaint();
	}

}
