package assignments;

import encryption.Paillier;
import encryption.PaillierPublic;
import utility.Field;

import java.math.BigInteger;

public class Assignment_3_1 {

    public static void main(String[] args){
        // Displayed testing of Paillier values
        Paillier p = new Paillier();
        PaillierPublic pp = p.getPublicPaillier();

        Field zn = new Field(p.getN());

        BigInteger b1 = zn.randomValue();
        BigInteger b2 = zn.randomValue();
        BigInteger c = zn.randomValue();

        BigInteger Eb1 = p.encrypt(b1);
        BigInteger Eb2 = p.encrypt(b2);
        BigInteger Eb1pb2 = p.encrypt(b1.add(b2));

        BigInteger Eb1c = p.encrypt(b1.multiply(c));

        BigInteger Eb1mb2 = p.encrypt(b1.subtract(b2));

        System.out.println("b1\t\t\t\t\t" + b1.toString());
        System.out.println("b2\t\t\t\t\t" + b2.toString());
        System.out.println("c\t\t\t\t\t" + c.toString());

        System.out.println("D(E(b1))\t\t\t" + p.decrypt(Eb1).toString());
        System.out.println("D(E(b2))\t\t\t" + p.decrypt(Eb2).toString());

        System.out.println("D(E(b1)+E(b2))\t\t" + p.decrypt(Paillier.secure_addition(Eb1, Eb2, pp)));
        System.out.println("D(E(b1+b2))\t\t\t" + p.decrypt(Eb1pb2));

        System.out.println("D(E(b1)*c)\t\t\t" + p.decrypt(Paillier.secure_scalar_multiplication(Eb1, c, pp)));
        System.out.println("D(E(b1*c))\t\t\t" + p.decrypt(Eb1c));

        System.out.println("D(E(b1)-E(b2))\t\t" + p.decrypt(Paillier.secure_subtraction(Eb1, Eb2, pp)));
        System.out.println("D(E(b1-b2))\t\t\t" + p.decrypt(Eb1mb2));


        // Blind testing of Paillier system with random values
        for(int i = 0; i < 50; i++){
            b1 = zn.randomValue();
            b2 = zn.randomValue();
            c = zn.randomValue();

            Eb1 = p.encrypt(b1);
            Eb2 = p.encrypt(b2);

            Eb1pb2 = p.encrypt(b1.add(b2));

            Eb1c = p.encrypt(b1.multiply(c));

            Eb1mb2 = p.encrypt(b1.subtract(b2));

            if(p.decrypt(Eb1).compareTo(b1) != 0){
                System.out.println("D(E(b1)) != b1 " + i);
            }
            if(p.decrypt(Eb2).compareTo(b2) != 0){
                System.out.println("D(E(b2)) != b2" + i);
            }
            if(p.decrypt(Eb1pb2).compareTo(p.decrypt(Paillier.secure_addition(Eb1, Eb2, pp))) != 0){
                System.out.println("D(E(b1+b2)) != D(E(b1)+E(b2))" + i);
            }
            if(p.decrypt(Eb1c).compareTo(p.decrypt(Paillier.secure_scalar_multiplication(Eb1, c, pp))) != 0){
                System.out.println("D(E(b1*c)) != D(E(b1)*c)" + i);
            }
            if(p.decrypt(Eb1mb2).compareTo(p.decrypt(Paillier.secure_subtraction(Eb1, Eb2, pp))) != 0){
                System.out.println("D(E(b1-b2)) != D(E(b1)-E(b2))" + i);
            }
        }

        System.out.println("Done testing 50 random number combinations");
    }
}
