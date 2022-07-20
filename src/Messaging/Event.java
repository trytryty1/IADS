package Messaging;

public interface Event
{
	
	public static final int PLOT_DATA_CHANGED = 1;
	public static final int FLIGHT_CHANGED = 2;
	public static final int NETWORK_DATA_RECEIVED = 3;
	public static final int SIM_TICK = 10;
	
    // This is just a regular method so it can return something or
    // take arguments if you like.
    public void eventCallback (int EventInfo, Object data);
}

