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

    /**
     * Recovers the secret used in this collection of Fiat-Shamir protocol runs.
     *
     * @param N
     *            The modulus
     * @param X
     *            The public component
     * @param runs
     *            Ten runs of the protocol.
     * @return
     */
    public static BigInteger recoverSecret(BigInteger N, BigInteger X,
                                            ProtocolRun[] runs) {
        BigInteger secret = BigInteger.ZERO;
        //We loop through the array and try to find same R:s
        for (int i = 0; i < runs.length-1; i++){
            for (int j = i+1; j < runs.length; j++){
            //R must be same, meanwhile c must be different in order to be able to find the secret
             if ((runs[i].R == runs[j].R) && (runs[i].c != runs[j].c)){
                 BigInteger s1 = runs[j].s;
                 BigInteger s2 = runs[i].s;
                 //secret = s1 / s2, where s1 = r mod n * x mod n, s2 = r * x^0 mod n = r mod n
                 //switch of s1 and s2 depending on value of c.
                 if (runs[i].c == 0){
                     secret = s1.divide(s2);
                 }else{
                     secret = s2.divide(s1);
                 }
             }
            }
        }
        return secret;
    }
}