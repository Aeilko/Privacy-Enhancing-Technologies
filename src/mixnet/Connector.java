package mixnet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

/**
 * Actually connects to the mixnet to sent messages
 */
public class Connector {

	// The mix network used for the encryption
	private MixNetwork mn;

	// Connection to the mixnet server
	private Socket s;


	public Connector(String host, int port, String[] keyLocations) throws InvalidKeySpecException, InvalidKeyException, IOException {
		// The MixNetwork for encryption of messages
		mn = new MixNetwork(keyLocations);

		// The connection to the mixnet
		this.s = new Socket(host, port);
	}

	public void sent(byte[] msg) throws BadPaddingException, IllegalBlockSizeException, IOException {
		//System.out.println("Sending message to mixnet");
		OutputStream os = s.getOutputStream();
		os.write(mn.encrypt(msg));

		// Get response
		byte [] resp = s.getInputStream().readNBytes(1);
		if(resp[0] == 6) {
			//System.out.println("Message is received");
		}
		else
			System.out.println("Error, response: '" + resp[0] + "'");
	}
}
