package com.thomasdh.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Thomas on 11-4-14.
 */
public class Main {

    public static void main(String[] args) {

        while (true) {
            Scanner input = new Scanner(System.in);

            System.out.println("Welcome to the RSA toolkit by Thomas den Hollander");
            System.out.println("(1) to generate new RSA keys");
            System.out.println("(2) to encrypt/ decrypt something");
            System.out.println("(0) to quit");

            String inputString = input.nextLine();

            if (inputString.equals("1")) {
                generateRSAKeys();
            } else if (inputString.equals("0")) {
                System.exit(0);
            } else if (inputString.equals("2")) {
                System.out.println("Please specify the input:");
                BigInteger number = BigInteger.valueOf(Long.parseLong(input.nextLine()));
                System.out.println("Please specify the exponent:");
                BigInteger exponent = BigInteger.valueOf(Long.parseLong(input.nextLine()));
                System.out.println("Please specify the modulus:");
                BigInteger modulus = BigInteger.valueOf(Long.parseLong(input.nextLine()));
                System.out.println("The answer is " + number.modPow(exponent, modulus).toString());
            }
        }
    }

    static void generateRSAKeys() {
        BigInteger p, q, m, e, z, d;

        Scanner input = new Scanner(System.in);

        System.out.println("Please enter the first prime number:");
        p = BigInteger.valueOf(Long.parseLong(input.nextLine()));

        System.out.println("Please enter the second prime number:");
        q = BigInteger.valueOf(Long.parseLong(input.nextLine()));

        z = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        m = p.multiply(q);
        System.out.println("The modulus is " + m);

        System.out.println("Please enter a guess for the public exponent: (The program wil calculate the closest solution)");
        BigInteger guess = BigInteger.valueOf(Long.parseLong(input.nextLine()));

        e = getPublicExponent(guess, z);

        System.out.println("The public exponent is " + e);


        d = calculatePrivateExponent(z, e);

        System.out.println("The private exponent is " + d);

        System.out.println();
        System.out.println("Encrypting: x ^ " + e + " mod " + m);
        System.out.println("Decrypting: x ^ " + d + " mod " + m);
        System.out.println("------------------------------------");
        System.out.println();
    }

    static BigInteger calculatePrivateExponent(BigInteger z, BigInteger e) {
        ArrayList<BigInteger[]> table1 = new ArrayList<BigInteger[]>();

        table1.add(new BigInteger[4]);
        table1.get(0)[0] = z;
        table1.get(0)[1] = e;

        while (table1.get(table1.size() - 1)[1].compareTo(BigInteger.ZERO) != 0) {
            int count = table1.size() - 1;
            table1.get(count)[2] = table1.get(count)[0].divide(table1.get(count)[1]);
            table1.get(count)[3] = table1.get(count)[0].mod(table1.get(count)[1]);

            table1.add(new BigInteger[4]);
            table1.get(count + 1)[0] = table1.get(count)[1];
            table1.get(count + 1)[1] = table1.get(count)[3];
        }

        ArrayList<BigInteger[]> table2 = new ArrayList<BigInteger[]>();
        table2.add(new BigInteger[]{z, BigInteger.ONE, BigInteger.ZERO});
        table2.add(new BigInteger[]{e, BigInteger.ZERO, BigInteger.ONE});

        int index = 0;
        while (table2.get(table2.size() - 1)[0].compareTo(BigInteger.ONE) != 0) {
            int tableLocation = index + 2;
            table2.add(new BigInteger[3]);
            table2.get(tableLocation)[0] = table1.get(tableLocation)[0];
            table2.get(tableLocation)[1] = table2.get(tableLocation - 2)[1].subtract(table2.get(tableLocation - 1)[1].multiply(table1.get(index)[2]));
            table2.get(tableLocation)[2] = table2.get(tableLocation - 2)[2].subtract(table2.get(tableLocation - 1)[2].multiply(table1.get(index)[2]));
            index++;
        }
        BigInteger answer = table2.get(table2.size() - 1)[2].mod(z);
        if (answer.signum() == 1)
            answer.add(z);
        return answer;
    }

    static BigInteger getPublicExponent(BigInteger guess, BigInteger z) {
        BigInteger e = z;
        BigInteger count = BigInteger.ZERO;
        do {
            BigInteger currentNum = waving(guess, count);
            if (currentNum.compareTo(BigInteger.ONE) > 0 && currentNum.compareTo(z) < 0) {
                e = waving(guess, count);
            }
            count.add(BigInteger.ONE);
        } while (gcd(z, e).compareTo(BigInteger.ONE) != 0);
        return e;
    }

    static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.compareTo(BigInteger.ZERO) == 0) {
            return a;
        }
        return gcd(b, a.mod(b));
    }

    //returns the n'th number from a number 'number', including lower and higher numbers
    static BigInteger waving(BigInteger number, BigInteger n) {
        if (n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
            return number.add(n.divide(BigInteger.valueOf(2)));
        } else {
            return number.subtract(n.divide(BigInteger.valueOf(2)));
        }
    }
}
