package encryption;

import utility.Field;

import java.math.BigInteger;
import java.util.Random;

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

    public Paillier(BigInteger p, BigInteger q){
        this.setup(p, q);
    }

    public Paillier(int p, int q){
        this.setup(BigInteger.valueOf(p), BigInteger.valueOf(q));
    }

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

    public BigInteger encrypt(BigInteger plaintext){
        BigInteger r = this.zn.randomValue();
        // (g^p * r^n) mod n^2
        BigInteger c = this.zn2.multiply(this.zn2.pow(this.g, plaintext), this.zn2.pow(r, this.n));
        return c;
    }

    public BigInteger decrypt(BigInteger ciphertext){
        // (L( (c^λ) mod n^2 ) * μ ) mod n
        BigInteger x = this.zn2.pow(ciphertext, this.lambda);
        return this.zn.multiply(this.L(x), this.mu);
    }

    public BigInteger secure_addition(BigInteger cipher1, BigInteger cipher2){
        return this.zn2.multiply(cipher1, cipher2);
    }

    public BigInteger secure_scalar_multiplication(BigInteger cipher, BigInteger c){
        return this.zn2.pow(cipher, c);
    }

    public BigInteger secure_subtraction(BigInteger cipher1, BigInteger cipher2){
        BigInteger nCipher2 = this.zn2.pow(cipher2, this.n.subtract(BigInteger.ONE));
        return this.zn2.multiply(cipher1, nCipher2);
    }


    // Public keys
    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getN(){
        return this.n;
    }


    // Private methods
    private BigInteger L(BigInteger x){
        return x.subtract(BigInteger.ONE).divide(this.n);
    }
}
