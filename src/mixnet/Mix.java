package mixnet;

import encryption.AES;
import encryption.RSA;
import utility.ArrayUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

/**
 * A node in the mix network
 * This is only the encryption for the node in a mix network, not an actualy mix network node
 */
public class Mix {

	// Helper for the RSA encoding
	private RSA RSAencoder;

	// Helper for the AES encoding (This should actually not be here since we should use a random one for every message)
	private AES AESEncoder;


	public Mix(String publicKeyLocation) throws IOException, InvalidKeySpecException, InvalidKeyException {
		System.out.println("Creating mix from key: " + publicKeyLocation);

		this.RSAencoder = new RSA(publicKeyLocation);
		// This can be random for every message, but it's easier to use the same one multiple times
		this.AESEncoder = new AES();
	}

	/**
	 * Encrypts the data for this mix network node
	 * @param plain The original data
	 * @return The original data in the mix network node encrypted form
	 */
	public byte[] encrypt(byte[] plain) throws BadPaddingException, IllegalBlockSizeException {
		byte[] iv = AESEncoder.getIV();
		byte[] pKey = AESEncoder.getPrivateKey();

		byte[] keyPart = this.RSAencoder.encrypt(ArrayUtil.append(iv, pKey));

		byte[] msgPart = this.AESEncoder.encrypt(plain);

		return ArrayUtil.append(keyPart, msgPart);
	}
}
