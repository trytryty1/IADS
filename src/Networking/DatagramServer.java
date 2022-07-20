package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Messaging.Event;
import Messaging.EventManager;
import Messaging.NetworkReceiveEvent;

public class DatagramServer extends Thread
{
   private final static int PACKETSIZE = 1000;
   EventManager evtMgr;
   int _port;
   Thread t;
   
   
   public DatagramServer(int port) {
	   evtMgr = new EventManager();
	   _port = port;
	
    }
   
   	public void addListener (Event lst) {
   		evtMgr.registerCallBack(lst);
   	}
  
   	public void dataReceived (String data) {
		NetworkReceiveEvent event = new NetworkReceiveEvent( data );
	    evtMgr.notify(Event.NETWORK_DATA_RECEIVED, (Object) event);
 	}

	@Override
	public void run() {
        // Construct the socket
        DatagramSocket socket;
		try {
			socket = new DatagramSocket( _port );
			System.out.println( "The server is ready..." ) ;
	
	
		     for( ;; )
		     {
	            // Create a packet
	            DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
	
	            // Receive a packet (blocking)
	            try {
					socket.receive( packet ) ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	            // Print the packet
	            //System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()).trim() ) ;
	
	            // Return the packet to the sender
	            //socket.send( packet ) ;
	            dataReceived(new String(packet.getData()).trim());
        	} 
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public int getPort() {
		return _port;
	}

	  
}
