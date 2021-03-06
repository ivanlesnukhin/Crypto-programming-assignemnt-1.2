package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class FiatShamir {

    public static class ProtocolRun {
        public final BigInteger R;
        public final int c;
        public final BigInteger s;

        public ProtocolRun(BigInteger R, int c, BigInteger s) {
            this.R = R;
            this.c = c;
            this.s = s;
        }
    }

    public static String decodeMessage(BigInteger m) {
        return new String(m.toByteArray());
    }

    public static void main(String[] args) {
        String filename = "input.txt";
        BigInteger N = BigInteger.ZERO;
        BigInteger X = BigInteger.ZERO;
        ProtocolRun[] runs = new ProtocolRun[10];
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            N = new BigInteger(br.readLine().split("=")[1]);
            X = new BigInteger(br.readLine().split("=")[1]);
            for (int i = 0; i < 10; i++) {
                String line = br.readLine();
                String[] elem = line.split(",");
                runs[i] = new ProtocolRun(
                        new BigInteger(elem[0].split("=")[1]),
                        Integer.parseInt(elem[1].split("=")[1]),
                        new BigInteger(elem[2].split("=")[1]));
            }
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        BigInteger m = recoverSecret(N, X, runs);
        System.out.println("Recovered message: " + m);
        System.out.println("Decoded text: " + decodeMessage(m));
    }

    /**
     * Recovers the secret used in this collection of Fiat-Shamir protocol runs.
     *
     * @param N    The modulus
     * @param X    The public component
     * @param runs Ten runs of the protocol.
     * @return
     */
    public static BigInteger recoverSecret(BigInteger N, BigInteger X, ProtocolRun[] runs) {
        BigInteger secret = BigInteger.ZERO;
        BigInteger s1 = BigInteger.ZERO;
        BigInteger s2 = BigInteger.ZERO;

        //We loop through the array and try to find same R:s
        for (int i = 0; i < runs.length - 1; i++) {
            for (int j = i + 1; j < runs.length; j++) {
                //R must be same, meanwhile c must be different in order to be able to find the secret
                if ((runs[i].R == runs[j].R) && (runs[i].c != runs[j].c)) {
                    //secret = s1 / s2, where s1 = r mod n * x mod n, s2 = r * x^0 mod n = r mod n
                    //switch of s1 and s2 depending on value of c.
                    if (runs[i].c == 0) {
                        s1 = runs[j].s;
                        s2 = runs[i].s;
                    } else {

                        s1 = runs[i].s;
                        s2 = runs[j].s;
                    }
                    secret = s1.multiply(s2.modInverse(N));
                    secret = secret.mod(N);


                }
            }

        }
        System.out.println("runs[3].R: " + runs[3].R);
        System.out.println("runs[8].R: " + runs[8].R);
        return secret;
    }
}