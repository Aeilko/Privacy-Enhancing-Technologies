package utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

    public static Pair<List<BigInteger>, Integer> scaleDecimals(List<BigDecimal> in){
        // Find the largest scale
        int maxScale = -1;
        for(int i = 0; i < in.size(); i++){
            int s = in.get(i).scale();
            if(s > maxScale)
                maxScale = s;
        }

        // Multiply everything by the max scale to generate an integer list
        List<BigInteger> result = new ArrayList<>();
        for(BigDecimal c: in){
            result.add(c.scaleByPowerOfTen(maxScale).toBigInteger());
        }

        // Return scaled list and scale used
        return new Pair<>(result, maxScale);
    }
}
