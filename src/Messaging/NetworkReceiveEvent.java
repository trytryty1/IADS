package Messaging;

public class NetworkReceiveEvent {
	String data;
	
	public NetworkReceiveEvent(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
