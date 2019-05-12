package encryption;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Handles AES encryption according to the settings for PET assignment 1
 */
public class AES {

	// Keys
	private SecretKey privateKey;

	// IV
	private IvParameterSpec iv;

	// Ciphers
	private Cipher encryptCipher;
	private Cipher decryptCipher;

	public AES(){
		Security.addProvider(new BouncyCastleProvider());

		try {
			// Generate private key
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			this.privateKey = kgen.generateKey();

			// Generate IV
			SecureRandom randomSecureRandom = new SecureRandom();
			byte[] rand = new byte[16];
			randomSecureRandom.nextBytes(rand);
			this.iv = new IvParameterSpec(rand);

			// Create cipher
			this.encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			this.decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			this.encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey, this.iv);
			this.decryptCipher.init(Cipher.DECRYPT_MODE, this.privateKey, this.iv);
		}
		catch(NoSuchAlgorithmException e){ /* Unreachable */ }
		catch(NoSuchProviderException e){ /* Unreachable */ }
		catch(NoSuchPaddingException e){ /* Unreachable */ }
		catch(InvalidAlgorithmParameterException e){ /* Unreachable */ }
		catch(InvalidKeyException e){ /* Unreachable */ }
	}

	/**
	 * Encrypts the given bytes
	 * @param plain The to be encrypted data
	 * @return The data in encrypted form
	 */
	public byte[] encrypt(byte[] plain) throws BadPaddingException, IllegalBlockSizeException {
		return this.encryptCipher.doFinal(plain);
	}

	/**
	 * Decrypts the given bytes
	 * @param cipher The to be decrypted data
	 * @return The data in plain form
	 */
	public byte[] decrypt(byte[] cipher) throws BadPaddingException, IllegalBlockSizeException {
		return this.decryptCipher.doFinal(cipher);
	}


	// Getters for the private values (of course they are public)
	public byte[] getIV(){
		return this.iv.getIV();
	}

	public byte[] getPrivateKey(){
		return this.privateKey.getEncoded();
	}
}
