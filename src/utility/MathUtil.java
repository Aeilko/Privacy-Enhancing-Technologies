package utility;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static double[] polynomialRegression(Map<Integer, Double> datapoints, int order) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[] y = new double[datapoints.size()];
        double[][] x = new double[datapoints.size()][order];
        int i = 0;
        for (int k : datapoints.keySet()) {
            y[i] = datapoints.get(k);

            for(int j = 0; j < order; j++){
                x[i][j] = Math.pow(k, (j+1));
            }

            i++;
        }
        regression.newSampleData(y, x);
        return regression.estimateRegressionParameters();
    }
}
