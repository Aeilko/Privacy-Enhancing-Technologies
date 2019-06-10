package encryption;

import utility.Field;

import java.math.BigInteger;

/**
 * The Paillier cryptosystem which only exposes the methods which should be publicly available
 * This class is not actually secure to distribute, since it does contain the original Paillier system,
 * however, the methods only perform actions the public is supposed to perform.
 */
public class PaillierPublic {

    // Original Paillier cryptosystem
    private Paillier p;

    /**
     * Generate new instance based on the given Paillier cryptosystem
     * @param p The full Paillier cryptosystem
     */
    public PaillierPublic(Paillier p){
        this.p = p;
    }

    /**
     * Encrypts a value using this instances public values
     * @param plaintext The to be encrypted plaintext
     * @return The encrypted plaintext
     */
    public BigInteger encrypt(BigInteger plaintext){
        return this.p.encrypt(plaintext);
    }

    /**
     * This method decrypts the given values, multiplies them and returns the encrypted result/
     * @param Ea The ecncrypted value A
     * @param Eb The encrypted value of B
     * @return The encryptied value of the product of decrypted Ea and Eb. E(D(Ea) * D(Eb))
     */
    public BigInteger decrypt_multiply_encrypt(BigInteger Ea, BigInteger Eb){
        //System.out.println("message");
        BigInteger a = p.decrypt(Ea);
        BigInteger b = p.decrypt(Eb);

        BigInteger ab = this.getZn().multiply(a, b);

        //System.out.println("message");
        return p.encrypt(ab);
    }

    /**
     * Returns the N value of this Paillier cryptosystem
     */
    public BigInteger getN(){
        return this.p.getN();
    }

    /**
     * Returns the Zn* field of this Paillier cryptosystem
     */
    public Field getZn(){
        return new Field(p.getN());
    }

    /**
     * Returns the Zn^2* field of this Paillier cryptosystem
     */
    public Field getZn2(){
        return new Field(p.getN().pow(2));
    }
}
