package edu.aims.mitchell.ian.fortunecookie;

import java.util.Random;

/**
 * Created by Ian on 1/19/2015.
 */
public class Lotto {

    //private static String[] ns = new String[6];

    public static String[] random() {
        int[] n = new int[6];
        int tmp;
        Random rn = new Random();
        boolean match;
        String[] ns = new String[6];

        for (int i = 0; i < 6; i++) {
            do {
                match = false;
                n[i] = rn.nextInt(49) + 1;

                for (int j = i - 1; j >= 0; j--) {
                    if (n[i] == n[j]) {
                        match = true;
                    }
                }
            }while (match);
        }

        for (int i = 0; i < 6 ; i++){
            for (int j = i + 1; j < 6; j++){
                if (n[i] > n[j]){
                    tmp = n[i];
                    n[i] = n[j];
                    n[j] = tmp;
                }
            }
        }

        for (int i = 0; i < 6; i++) {

            ns[i] = String.valueOf(n[i]);
        }
        return ns;
    }
}