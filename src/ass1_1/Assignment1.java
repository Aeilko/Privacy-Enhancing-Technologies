package ass1_1;

import mixnet.Connector;

import java.net.ConnectException;

public class Assignment1 {

	public static void main(String[] args){
		// Public keys for the mixnet encryption
		String[] keys = new String[3];
		keys[0] = "./data/public-key-mix-1.pem";
		keys[1] = "./data/public-key-mix-2.pem";
		keys[2] = "./data/public-key-mix-3.pem";

		// Connection settings
		String host = "pets.ewi.utwente.nl";
		int port = 53025;

		// Sent a message over the network
		String msg = "Group 34";
		byte[] m = msg.getBytes();
		try {
			Connector c = new Connector(host, port, keys);
			c.sent(m);
		}
		catch(ConnectException e){
			System.err.println("Can't connect to the host (restart the mixnet on the server)");
		}
		catch(Exception e){ e.printStackTrace(); }
	}
}
