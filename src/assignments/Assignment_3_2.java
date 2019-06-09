package assignments;

import encryption.Paillier;
import encryption.PaillierPublic;
import utility.Field;

import java.math.BigInteger;

public class Assignment_3_2 {

    public static void main(String[] args){
        Paillier p = new Paillier();
        PaillierPublic pp = p.getPublicPaillier();

        Field zn = new Field(p.getN());

        BigInteger b1 = zn.randomValue();
        BigInteger b2 = zn.randomValue();
        BigInteger b1b2 = zn.multiply(b1, b2);

        BigInteger eb1 = p.encrypt(b1);
        BigInteger eb2 = p.encrypt(b2);

        System.out.println("b1*b2\t\t\t\t" + zn.multiply(b1, b2));
        System.out.println("D(E(b1)*E(b2))\t\t" + p.decrypt(Paillier.secure_multiplication(eb1, eb2, pp)));
        System.out.println("D(E(b1*b2))\t\t\t" + p.decrypt(p.encrypt(b1b2)));

        for(int i = 0; i < 50; i++){
            b1 = zn.randomValue();
            b2 = zn.randomValue();
            b1b2 = zn.multiply(b1,b2);

            eb1 = p.encrypt(b1);
            eb2 = p.encrypt(b2);

            if(b1b2.compareTo(p.decrypt(p.encrypt(b1b2))) != 0){
                System.out.println("b1*b2 != D(E(b1*b2)) " + i);
            }
            if(b1b2.compareTo(p.decrypt(Paillier.secure_multiplication(eb1, eb2, pp))) != 0){
                System.out.println("b1*b2 != D(E(b1) * E(b2)) " + i);
            }
        }

        System.out.println("Done testing 50 random number combinations");
    }
}
