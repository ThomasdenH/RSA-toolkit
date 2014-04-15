package com.thomasdh.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static final int ENCRYPTION_TYPE_ASCII = 100;
    static final int maxASCIIValue = 128;

    static final int ENCRYPTION_TYPE_BASIC_ALPHABET = 101;
    static final char[] basicAlphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    static final int ENCRYPTION_TYPE_EXTENDED_ALPHABET = 102;
    static final char[] extendedAlphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y','Z', ',', ' ', '.', '\"','\"', ':', ';', '(', ')'};

    public static void main(String[] args) {

        while (true) {
            Scanner input = new Scanner(System.in);

            System.out.println("Welcome to the RSA toolkit by Thomas den Hollander");
            System.out.println("(1) to generate new RSA keys");
            System.out.println("(2) to encrypt/ decrypt something");
            System.out.println("(3) to convert a number to a string and vice versa");
            System.out.println("(0) to quit");

            String inputString = input.nextLine();

            if (inputString.equals("1")) {
                generateRSAKeys();
            } else if (inputString.equals("0")) {
                System.exit(0);
            } else if (inputString.equals("2")) {
                System.out.println("Please specify the number:");
                BigInteger number = new BigInteger(input.nextLine());
                System.out.println("Please specify the exponent:");
                BigInteger exponent = new BigInteger(input.nextLine());
                System.out.println("Please specify the modulus:");
                BigInteger modulus = new BigInteger(input.nextLine());
                if (number.compareTo(modulus) > 0) {
                    System.out.println("This number is bigger than the modulus allows");
                } else {
                    BigInteger answer = number.modPow(exponent, modulus);
                    System.out.println("The answer is " + answer.toString());
                }
            } else if (inputString.equals("3")) {
                System.out.println("(1) to convert a number to a string");
                System.out.println("(2) to convert a string to a number");

                boolean numberToString = input.nextLine().equals("1");
                int translateType = 100;

                System.out.println();
                System.out.println("(1) to use ASCII conversiion");
                System.out.println("(2) to use a simple alphabet (a-z)");
                System.out.println("(3) to use an extended alphabet (a-z, A-Z, .'\":;()");
                System.out.println("(4) to use fake words");

                int inputInt = Integer.parseInt(input.nextLine());
                if (inputInt == 1) {
                    translateType = ENCRYPTION_TYPE_ASCII;
                } else if (inputInt == 2) {
                    translateType = ENCRYPTION_TYPE_BASIC_ALPHABET;
                } else if (inputInt == 3){
                    translateType = ENCRYPTION_TYPE_EXTENDED_ALPHABET;
                }

                if (numberToString) {
                    System.out.println(convertBigIntegerToString(new BigInteger(input.nextLine()), translateType));
                } else {
                    System.out.println(convertStringToBigInteger(input.nextLine(), translateType));
                }
            }
        }
    }

    static BigInteger convertStringToBigInteger(String string, int type) {
        char[] chars = string.toCharArray();
        BigInteger totalNumber = BigInteger.ZERO;

        BigInteger maxValue = null;
        if (type == ENCRYPTION_TYPE_ASCII)
            maxValue = BigInteger.valueOf(maxASCIIValue);
        if (type == ENCRYPTION_TYPE_BASIC_ALPHABET)
            maxValue = BigInteger.valueOf(basicAlphabet.length);
        if (type == ENCRYPTION_TYPE_EXTENDED_ALPHABET)
            maxValue = BigInteger.valueOf(extendedAlphabet.length);

        for (int x = 0; x < chars.length; x++) {

            totalNumber = totalNumber.add(
                    BigInteger.valueOf(
                            convertCharToIntValue(chars[x], type)
                    ).multiply(maxValue
                            .pow(x)
                    ));
        }
        return totalNumber;
    }

    static int convertCharToIntValue(char c, int type) {
        if (type == ENCRYPTION_TYPE_ASCII) {
            if ((int) c > maxASCIIValue) {
                System.out.println("This character is not contained in the first " + maxASCIIValue + " characters");
            }
            return (int) c;
        }
        if (type == ENCRYPTION_TYPE_BASIC_ALPHABET) {
            for (int x = 0; x < basicAlphabet.length; x++) {
                if (basicAlphabet[x] == c) {
                    return x;
                }
            }
        }
        if (type == ENCRYPTION_TYPE_EXTENDED_ALPHABET) {
            for (int x = 0; x < extendedAlphabet.length; x++) {
                if (extendedAlphabet[x] == c) {
                    return x;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    static char convertIntToCharValue(int c, int type) {
        if (type == ENCRYPTION_TYPE_ASCII) {
            if (c > maxASCIIValue) {
                System.out.println("This character is not contained in the first " + maxASCIIValue + " characters");
            }
            return (char) c;
        }
        if (type == ENCRYPTION_TYPE_BASIC_ALPHABET) {
            return basicAlphabet[c];
        }
        if (type == ENCRYPTION_TYPE_EXTENDED_ALPHABET) {
            return extendedAlphabet[c];
        }
        throw new IllegalArgumentException();
    }

    static String convertBigIntegerToString(BigInteger integer, int type) {
        int maxValue = 0;
        if (type == ENCRYPTION_TYPE_ASCII)
            maxValue = maxASCIIValue;
        if (type == ENCRYPTION_TYPE_BASIC_ALPHABET)
            maxValue = basicAlphabet.length;
        if (type == ENCRYPTION_TYPE_EXTENDED_ALPHABET)
            maxValue = extendedAlphabet.length;

        int count = (int) Math.ceil(integer.bitLength() * Math.log(2) / Math.log(maxValue));

        String s = "";
        for (int x = 0; x < count; x++) {
            s += convertIntToCharValue(integer.mod(
                    BigInteger.valueOf(maxValue).pow(x + 1)
            ).divide(
                    BigInteger.valueOf(maxValue).pow(x)
            ).intValue(), type);
        }
        return s;
    }

    static void generateRSAKeys() {
        BigInteger p, q, m, e, z, d;

        Scanner input = new Scanner(System.in);

        System.out.println("Please enter the first prime number:");
        p = new BigInteger(input.nextLine());

        System.out.println("Please enter the second prime number:");
        q = new BigInteger(input.nextLine());

        z = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        m = p.multiply(q);
        System.out.println("The modulus is " + m + ". This is also the max value.");

        System.out.println("Please enter a guess for the public exponent: (The program wil calculate the closest solution)");
        BigInteger guess = new BigInteger(input.nextLine());

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
            answer = answer.add(z);
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
            count = count.add(BigInteger.ONE);
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
