package utility;

import java.math.BigInteger;
import java.util.Random;

public class Field {

    // The value of the field
    private BigInteger n;

    /**
     * Creates a new field of Zn*
     * @param n The value of n
     */
    public Field(int n){
        this(BigInteger.valueOf(n));
    }

    /**
     * Creates a new field of Zn*
     * @param n The value of n
     */
    public Field(BigInteger n){
        this.n = n;
    }

    /**
     * Multiplies the given numbers in this field
     * @param a The first number
     * @param b The second number
     * @return The product of the two numbers under modulo n
     */
    public BigInteger multiply(BigInteger a, BigInteger b){
        return a.multiply(b).mod(this.n);
    }

    /**
     * Performs the power operation a^b within this field
     * @param a The ground number of the power operation
     * @param b The exponent of the power operation
     * @return a^b in this field
     */
    public BigInteger pow(BigInteger a, BigInteger b){
        return a.modPow(b, this.n);
    }

    /**
     * Calculates the modular inverse of the given number
     * @param a The number of which the inverse will be calculated
     * @return The inverse of a
     */
    public BigInteger inverse(BigInteger a){
        return a.modInverse(this.n);
    }

    /**
     * Returns a random value within this field
     */
    public BigInteger randomValue(){
        Random r = new Random();
        BigInteger x = this.n;
        while(x.compareTo(this.n) != -1){
            x = new BigInteger(this.n.bitLength(), r);
        }
        return x;
    }
}
