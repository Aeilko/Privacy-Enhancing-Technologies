package encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * Handles RSA encryption according to the settings for PET assignment 1
 */
public class RSA {
	private Cipher cipher;

	public RSA(String filename) throws IOException, InvalidKeySpecException, InvalidKeyException {
		Security.addProvider(new BouncyCastleProvider());

		try {
			// Read public key
			Reader reader = new FileReader(filename);
			PemObject pemOb = new PemReader(reader).readPemObject();
			PublicKey publicKey = KeyFactory.getInstance("RSA", "BC").generatePublic(new X509EncodedKeySpec(pemOb.getContent()));

			// Save the cipher instance
			this.cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		}
		catch(NoSuchAlgorithmException e){ /* Unreachable */}
		catch(NoSuchPaddingException e){/* Unreachable */ }
		catch(NoSuchProviderException e){ /* Unreachable */ }
	}

	/**
	 * Encrypts the given data
	 * @param plain The to be encrypted data
	 * @return The encrypted data
	 */
	public byte[] encrypt(byte[] plain) throws BadPaddingException, IllegalBlockSizeException {
		return this.cipher.doFinal(plain);
	}
}
