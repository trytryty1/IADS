package Networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DatagramClient
{

	ArrayList<HostData> senders = new ArrayList<HostData>();
	
	private class HostData {
		public InetAddress host;
		public int port;
	}
	
   public DatagramClient() {

   }
   
   public void addDest(String _ip, int _port) {
	   
	   HostData data = new HostData();
	   
       // Convert the arguments first, to ensure that they are valid
       try {
			data.host = InetAddress.getByName( _ip ) ;
			 data.port  =  _port;
			 senders.add(data);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
      
   }
   public void send(String sendString) {
	   
	   DatagramSocket socket = null ;
       // Construct the datagram packet
       byte [] data = sendString.getBytes();
       
       try {
    	   socket = new DatagramSocket() ;
       } catch (SocketException e1) {
    	   // TODO Auto-generated catch block
    	   e1.printStackTrace();
       }
       
	   // Construct the socket
       try {
    	   for(HostData sender: senders) {
		       DatagramPacket packet = new DatagramPacket( data, data.length, sender.host, sender.port ) ;
		       //System.out.println("Sending to " + sender.host + " on port " + sender.port);
		       socket.send( packet ) ;
    	   }

        } catch( Exception e ) {
           System.out.println( e ) ;
        } finally {
           if( socket != null ) {
              socket.close();
           }
        }
   }
}
