package assignments;

import mixnet.Connector;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Assignment_1_2 {

	public static void main(String[] args){
		// Public keys for the mixnet encryption
		String[] keys = new String[3];
		keys[0] = "./data/public-key-mix-1.pem";
		keys[1] = "./data/public-key-mix-2.pem";
		keys[2] = "./data/public-key-mix-3.pem";

		// Connection settings
		String host = "pets.ewi.utwente.nl";
		int port = 50606;
		String exitFile = "https://pets.ewi.utwente.nl/log/34-7z+bziJtTGx2FOzw3LMR+WQfrUFM3W4FCoVamA8R9QE=/exit.csv";


		// Sent multiple messages over the network
		String msg;
		byte[] m;
		try {
			Connector c = new Connector(host, port, keys);

			// Flush the mixnet so there are nog messages left.
			ArrayList<String> msgSent = new ArrayList<>();
			for(int i = 1; i <= 2048; i++){
				msg = "flush " + i;
				m = msg.getBytes();
				msgSent.add(msg);
				c.sent(m);

				URL u = new URL(exitFile);
				InputStream is = u.openStream();
				Scanner in = new Scanner(is);
				while(in.hasNextLine()){
					String line = in.nextLine();
					String[] vals = line.split(",");
					if(vals.length == 3) {
						if(msgSent.contains(vals[2])){
							msgSent.remove(vals[2]);
						}
					}
				}

				// If msgSent is empty there are no more messages left in the mixnet, so it is flushed
				if(msgSent.size() == 0)
					break;
			}

			System.out.println("Mixnet is flushed");

			// System is flushed, now we can sent X messages into the mixnet. If the exit file contains exactly X messages we know all t values divide X.
			int x = 70;
			System.out.println("Sending " + x +  " messages into the mixnet");
			msgSent = new ArrayList<>();
			for(int i = 1; i <= x; i++){
				msg = "count " + i;
				m = msg.getBytes();
				msgSent.add(msg);
				c.sent(m);
			}

			URL u = new URL(exitFile);
			InputStream is = u.openStream();
			Scanner in = new Scanner(is);
			while(in.hasNextLine()){
				String line = in.nextLine();
				String[] vals = line.split(",");
				if(vals.length == 3) {
					if(msgSent.contains(vals[2])){
						msgSent.remove(vals[2]);
					}
				}
			}

			System.out.println("Length of msgSent: " + msgSent.size());

		}
		catch(ConnectException e){
			System.err.println("Can't connect to the host (restart the mixnet on the server)");
		}
		catch(Exception e){ e.printStackTrace(); }
	}
}
