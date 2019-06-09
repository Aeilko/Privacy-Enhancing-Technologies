package encryption;

import utility.Field;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the Paillier cryptosystem.
 * Also provides some of the arithmetic Paillier can perform in the ecrypted space.
 * @author Aeilko Bos
 */
public class Paillier {

    // Private keys
    private BigInteger lambda;
    private BigInteger mu;

    // Public keys
    private BigInteger n;
    private BigInteger g;

    // Other useful variables
    private Field zn;
    private Field zn2;

    /**
     * Creates a new instance of the Paillier cryptosystem, using random generated prime numbers of 1024 bits.
     */
    public Paillier(){
        // Create 2 random, non-equal primes
        BigInteger p = BigInteger.ZERO;
        BigInteger q = BigInteger.ZERO;
        while(p.compareTo(q) == 0) {
            p = BigInteger.probablePrime(1024, new Random());
            q = BigInteger.probablePrime(1024, new Random());
        }
        this.setup(p, q);
    }

    /**
     * Create a new instance Paillier cryptosystem, using the providede prime numbers.
     * The given arguments should be prime, this method does not perform any additional checks on the security of the prime numbers
     * @param p The first prime number
     * @param q The second prime number, should not equal the first prime number
     */
    public Paillier(BigInteger p, BigInteger q){
        this.setup(p, q);
    }

    /**
     * Create a new instance Paillier cryptosystem, using the providede prime numbers.
     * The given arguments should be prime, this method does not perform any additional checks on the security of the prime numbers
     * @param p The first prime number
     * @param q The second prime number, should not equal the first prime number
     */
    public Paillier(int p, int q){
        this.setup(BigInteger.valueOf(p), BigInteger.valueOf(q));
    }

    /**
     * Performs the calculations required to get the private and public keys.
     * @param p The first prime number used
     * @param q The second prime number used
     */
    private void setup(BigInteger p, BigInteger q){
        // p-1 and q-1
        BigInteger p1 = p.subtract(BigInteger.ONE);
        BigInteger q1 = q.subtract(BigInteger.ONE);

        // Lambda
        BigInteger lambda = p1.multiply(q1);

        // n and n^2
        BigInteger n = p.multiply(q);
        BigInteger n2 = n.pow(2);

        // generator g, using the shortcut g = n+1
        BigInteger g = n.add(BigInteger.ONE);

        // Save public key
        this.n = n;
        this.g = g;

        // Calculate mu(μ)
        Field zn = new Field(n);
        Field zn2 = new Field(n2);

        BigInteger mu = zn.inverse(lambda);

        // Save the private keys
        this.lambda = lambda;
        this.mu = mu;

        // Save some other helpful variables
        this.zn = zn;
        this.zn2 = zn2;
    }

    /**
     * Encrypts the message of using the public keys of this instance
     * @param plaintext The to be encrypted texts
     * @return The ciphertext corresponding to the provided plaintext
     */
    public BigInteger encrypt(BigInteger plaintext){
        BigInteger r = this.zn.randomValue();
        // (g^p * r^n) mod n^2
        BigInteger c = this.zn2.multiply(this.zn2.pow(this.g, plaintext), this.zn2.pow(r, this.n));
        return c;
    }

    /**
     * Decrypts the given value using the private keys of this instance
     * @param ciphertext The to be decrypted ciphertext
     * @return The plaintext corresponding to the provided ciphertext
     */
    public BigInteger decrypt(BigInteger ciphertext){
        // (L( (c^λ) mod n^2 ) * μ ) mod n
        BigInteger x = this.zn2.pow(ciphertext, this.lambda);
        return this.zn.multiply(this.L(x), this.mu);
    }


    // Static methods, these methods only use the public key information of Paillier cryptosystem
    /**
     * Performs addition in the cipherspace
     * @param cipher1 The first ciphertext
     * @param cipher2 The second ciphertext
     * @param p The public information of the Paillier cryptosystem
     * @return The addition of the given ciphertexts in the cipherspace
     */
    public static BigInteger secure_addition(BigInteger cipher1, BigInteger cipher2, PaillierPublic p){
        return p.getZn2().multiply(cipher1, cipher2);
    }

    /**
     * Perform scaling of the given ciphertext by the given value in the cipherspace
     * @param cipher The cipher which will be scaled
     * @param c The scaler
     * @param p The public information of the Paillier cryptosystem
     * @return The scaled value of the given cipthertext in the cipherspace
     */
    public static BigInteger secure_scalar_multiplication(BigInteger cipher, BigInteger c, PaillierPublic p){
        return p.getZn2().pow(cipher, c);
    }

    /**
     * Subtracts the second ciphertext from the first ciphertext in the cipherspace
     * @param cipher1 The base ciphertext
     * @param cipher2 The to be subtracted ciphertext
     * @param p The public information of the Paillier cryptosystem
     * @return The encrypted subtraction of the first value by the second value
     */
    public static BigInteger secure_subtraction(BigInteger cipher1, BigInteger cipher2, PaillierPublic p){
        BigInteger nCipher2 = p.getZn2().pow(cipher2, p.getN().subtract(BigInteger.ONE));
        return p.getZn2().multiply(cipher1, nCipher2);
    }

    /**
     * Multiplies two ciphertexts in the cipherspace
     * @param x The first ciphertext
     * @param y The second ciphertext
     * @param client The public information of the Paillier cryptosystem
     * @return The encrypted product of the original values of the given ciphertexts
     */
    public static BigInteger secure_multiplication(BigInteger x, BigInteger y, PaillierPublic client){
        Field zn = client.getZn();
        Field zn2 = client.getZn2();

        BigInteger rx = zn.randomValue();
        BigInteger ry = zn.randomValue();

        BigInteger Exp = zn2.multiply(x, client.encrypt(rx.negate()));
        BigInteger Eyp = zn2.multiply(y, client.encrypt(ry.negate()));

        // [x'*y']
        BigInteger Exyp = client.decrypt_multiply_encrypt(Exp, Eyp);

        // [x]^ry
        BigInteger xry = Paillier.secure_scalar_multiplication(x, ry, client);
        // [y]^rx
        BigInteger yrx = Paillier.secure_scalar_multiplication(y, rx, client);
        // [-rx * ry]
        BigInteger mrxry = client.encrypt(client.getZn().multiply(rx.negate(), ry));

        // [x'*y']*[x]^ry
        BigInteger xpypxry = client.getZn2().multiply(Exyp, xry);
        // [y]^rx * [-rx * ry]
        BigInteger yrxrxry = client.getZn2().multiply(yrx, mrxry);
        // [x'*y']*[x]^ry * [y]^rx * [-rx * ry] = [x*y]
        BigInteger xy = client.getZn2().multiply(xpypxry, yrxrxry);
        return xy;
    }

    /**
     * Combines an encrypted value and a plaintext exponent and returns encrypted value of the decrypted value to the power of the exponent ([x], e => [x^e])
     * @param x An encrypted value in the Paillier cryptosystem
     * @param exponent The plaintext value which is used as exponent
     * @param p The public values of the Paillier cryptosystem
     * @return [x], e => [x^e]
     */
    public static BigInteger secure_pow(BigInteger x, int exponent, PaillierPublic p){
        // x^0 = 1
        BigInteger result = (exponent == 0 ? p.encrypt(BigInteger.ONE) : x);
        for(int i = 0; i < (exponent-1); i++){
            result = Paillier.secure_multiplication(result, x, p);
        }
        return result;
    }

    /**
     * Performs the logarithmic approximation in base 2 on the encrypted value x, using the provided coefficients
     * @param x An encrypted value in the Paillier cryptosystem
     * @param coef A list of coefficients in integer form
     * @param p The public values of the Paillier cryptosystem
     * @return The encrypted result of the logarithmic approximation in base 2
     */
    public static BigInteger secure_log_approximation(BigInteger x, List<BigInteger> coef, PaillierPublic p){
        // The first coefficient is not multiplied with x, however it does have to be encrypted to perform secure addition
        BigInteger result = p.encrypt(coef.get(0));
        for(int i = 1; i < coef.size(); i++){
            // [x], i => [x^i]
            BigInteger tmp = Paillier.secure_pow(x, i, p);
            // c, [x^i] => [c*x^i]
            tmp = Paillier.secure_scalar_multiplication(tmp, coef.get(i), p);
            // [c*x^0 + c*^i + ... + c*x^(i-1)], [c*x^i] => [c*x^0 + c*^i + ... + c*x^i]
            result = Paillier.secure_addition(result, tmp, p);
        }

        return result;
    }


    // Public keys
    /**
     * Returns the generator of this Paillier cryptosystem
     */
    public BigInteger getG() {
        return this.g;
    }

    /**
     * Return the n values of this Paillier cryptosystem
     */
    public BigInteger getN(){
        return this.n;
    }

    /**
     * Returns the public accessible methods of this Paillier cryptosystem
     */
    public PaillierPublic getPublicPaillier(){
        return new PaillierPublic(this);
    }


    // Private methods
    /**
     * Calculates (x-1)/n
     */
    private BigInteger L(BigInteger x){
        return x.subtract(BigInteger.ONE).divide(this.n);
    }
}
