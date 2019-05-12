package mixnet;

import utility.ArrayUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

/**
 * A network of multiple Mix nodes
 * This only handles the encryption of messages for a mix network, it's not an actual mix network
 */
public class MixNetwork {

	private Mix[] mix;
	private int mixes;

	public MixNetwork(String[] keyLocations) throws InvalidKeySpecException, InvalidKeyException, IOException {
		this.mix = new Mix[keyLocations.length];
		this.mixes = this.mix.length;

		System.out.println("Setting up MixNetwork of " + this.mixes + " nodes");

		for(int i = 0; i < keyLocations.length; i++){
			this.mix[i] = new Mix(keyLocations[i]);
		}
		System.out.println("MixNetwork is created");
	}

	/**
	 * Encrypts the given message so it can be sent over the Mix Network
	 * @param plain The to be encrypted data
	 * @return The encrypted version of the data which can be sent over the mix network
	 */
	public byte[] encrypt(byte[] plain) throws BadPaddingException, IllegalBlockSizeException {
		byte[] result = plain;

		// Loop in reverse order over the mix nodes since the first node is the first one to decrypt
		for(int i = this.mixes-1; i >= 0; i--){
			result = this.mix[i].encrypt(result);
		}

		// Prefix the encrypted text with the length
		BigInteger len = BigInteger.valueOf(result.length);
		byte[] l = len.toByteArray();
		if(l.length < 4){
			byte[] lTMP = l;
			l = new byte[4];
			for(int i = 0; i < lTMP.length; i++){
				l[(4-lTMP.length)+i] = lTMP[i];
			}
		}

		return ArrayUtil.append(l, result);
	}
}
