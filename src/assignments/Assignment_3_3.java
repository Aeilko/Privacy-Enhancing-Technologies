package assignments;

import encryption.Paillier;
import encryption.PaillierPublic;

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
        coefs.add(BigDecimal.valueOf(-1.3485));
        coefs.add(BigDecimal.valueOf(1.8456));
        coefs.add(BigDecimal.valueOf(-4.6476));
        coefs.add(BigDecimal.valueOf(7.9054));
        coefs.add(BigDecimal.valueOf(-8.1238));
        coefs.add(BigDecimal.valueOf(4.5553));
        coefs.add(BigDecimal.valueOf(-1.0679));

        // Add the scaling of the log function and find the largest value
        int maxScale = -1;
        for(int i = 0; i < coefs.size(); i++){
            // Apply the log approximation scaling
            coefs.set(i, coefs.get(i).scaleByPowerOfTen(i == 0 ? 0 : (i-1)*-1));

            // Find the largest scale
            int s = coefs.get(i).scale();
            if(s > maxScale)
                maxScale = s;
        }

        // Multiply everything by the max scale to generate integers which we can use in Paillier
        List<BigInteger> coefInt = new ArrayList<>();
        for(BigDecimal c: coefs){
            coefInt.add(c.scaleByPowerOfTen(maxScale).toBigInteger());
        }

        // Set up the crypto system
        Paillier p = new Paillier();
        PaillierPublic pp = p.getPublicPaillier();

        // Calculate logarithm for x = [2, 10], both normally and in secure_logarithm_approximation
        System.out.println("x\t\tlog2(i)\t\tf(x)\t\terror");
        for(int i = 2; i <= 10; i++){
            // Calculate the normal log(i) in base 2
            BigDecimal normal_logi = BigDecimal.valueOf(Math.log(i)/Math.log(2));
            normal_logi = normal_logi.setScale(4, RoundingMode.HALF_UP);

            // Encrypt the value of i
            BigInteger Ei = p.encrypt(BigInteger.valueOf(i));
            // Calculate the encrypted approximation of log(i) using [i]. This value is scaled
            BigInteger ElogiScaled = Paillier.secure_log_approximation(Ei, coefInt, pp);
            // Decrypt
            BigInteger logiScaled = p.decrypt(ElogiScaled);
            // The number is scaled (to create integers from the floating coefficients), remove the scaling and round to 4 digits
            BigDecimal logi = new BigDecimal(logiScaled, maxScale).setScale(4, RoundingMode.HALF_UP);
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
