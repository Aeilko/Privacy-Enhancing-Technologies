package utility;

import java.math.BigInteger;

public class MathUtil {

    public static int gcd(int a, int b){
        if (b==0)
            return a;
        return gcd(b,a % b);
    }

    public static int lcm(int a, int b){
        int g = gcd(a, b);
        return (a*b)/g;
    }

    public static BigInteger lcm(BigInteger a, BigInteger b){
        BigInteger g = a.gcd(b);
        return a.multiply(b).divide(g);
    }
}
