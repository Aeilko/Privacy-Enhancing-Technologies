package assignments;

import encryption.Paillier;
import encryption.PaillierPublic;
import utility.MathUtil;
import utility.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Assignment_3_3 {

    public static void ass3_a(){
        System.out.println("Assignment 3a");

        // List of decimal coefficients
        List<BigDecimal> coefs = new ArrayList<>();
        coefs.add(BigDecimal.valueOf(-13485, 4));
        coefs.add(BigDecimal.valueOf(18456, 4));
        coefs.add(BigDecimal.valueOf(-46476, 5));
        coefs.add(BigDecimal.valueOf(79054, 6));
        coefs.add(BigDecimal.valueOf(-81238, 7));
        coefs.add(BigDecimal.valueOf(45553, 8));
        coefs.add(BigDecimal.valueOf(-10679, 9));

        // Perform scaling to Integer values
        Pair<List<BigInteger>, Integer> pair = MathUtil.scaleDecimals(coefs);
        List<BigInteger> coefsInt = pair.v1;
        int scale = pair.v2;

        // Set up the crypto system
        Paillier p = new Paillier();
        PaillierPublic pp = p.getPublicPaillier();

        // Table header
        System.out.println("x\t\tlog2(x)\t\tf(x)\t\terror");
        // Calculate logarithm for x = [2, 10], both normally and in secure_logarithm_approximation
        for(int i = 2; i <= 10; i++){
            // Calculate the normal log(i) in base 2
            BigDecimal normal_logi = BigDecimal.valueOf(Math.log(i)/Math.log(2));
            normal_logi = normal_logi.setScale(4, RoundingMode.HALF_UP);

            // Encrypt the value of i
            BigInteger Ei = p.encrypt(BigInteger.valueOf(i));
            // Calculate the encrypted approximation of log(i) using [i]. This value is scaled
            BigInteger ElogiScaled = Paillier.secure_log_approximation(Ei, coefsInt, pp);
            // Decrypt
            BigInteger logiScaled = p.decrypt(ElogiScaled);
            // The number is scaled (to create integers from the floating coefficients), remove the scaling and round to 4 digits
            BigDecimal logi = new BigDecimal(logiScaled, scale).setScale(4, RoundingMode.HALF_UP);
            // Calculate the error
            BigDecimal error = logi.subtract(normal_logi).abs();

            // Display result row
            System.out.print(i + "\t\t");
            System.out.print(normal_logi + "\t\t");
            System.out.print(logi + "\t\t");
            System.out.println(error);
        }
    }

    public static void main(String[] args){
        Assignment_3_3.ass3_a();
    }
}
