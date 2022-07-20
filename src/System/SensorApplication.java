package System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Interface.LoadStuff;
import Interface.MainWindow;

public class SensorApplication {
	public static void main(String[] args) {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(Configurations.configFile));
			br.close();
		} catch(IOException e) {
			System.out.println("Could not open file " + Configurations.configFile);
			System.exit(1);
		}
		
		WorldBounds bounds = LoadStuff.getBounds("res\\radars\\radars.txt");
		if(bounds == null) {
			System.out.println("Did not find required bounds for world.");
			System.out.println("Put Required bounds in file: res\\radars\\radars.txt");
			System.exit(1);
		}
		//setup all the configurations before anything else
	    Configurations._upperLeft = new Location("Upper Left", bounds.getUpperLeftBound().getLatitude(), 
	    													bounds.getUpperLeftBound().getLongitude());
	    
	    Configurations._lowerRight = new Location("Lower Right", bounds.getLowerRightBound().getLatitude(), 
				bounds.getLowerRightBound().getLongitude());
	    
		// TODO Auto-generated method stub
		try {
			
			@SuppressWarnings("unused")
			MainWindow app = new MainWindow();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
