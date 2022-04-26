package org.ucb.c5.sequtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the reverse complement of a DNA sequence
 *
 * @author J. Christopher Anderson
 */
public class RevComp {

    public void initiate() {

    }

    /**
     * Calculates the reverse complement of a DNA sequence preserving the case
     * of each character
     *
     * @param dna the DNA sequence that should be reverse complemented
     * @return the reverse complement of dna
     */
    public String run(String dna) {
        StringBuilder sb = new StringBuilder();
        for (int i = dna.length() - 1; i >= 0; i--) {
            char achar = dna.charAt(i);
            char base = compbase(achar);
            sb.append(base);
        }

        return sb.toString();
    }

    private char compbase(char abase) {

        char compbase = 'z';
        switch (abase) {
            case 'G':
                compbase = 'C';
                break;
            case 'C':
                compbase = 'G';
                break;
            case 'A':
                compbase = 'T';
                break;
            case 'T':
                compbase = 'A';
                break;
            case 'a':
                compbase = 't';
                break;
            case 't':
                compbase = 'a';
                break;
            case 'c':
                compbase = 'g';
                break;
            case 'g':
                compbase = 'c';
                break;
            case 'B':
                compbase = 'V';
                break;
            case 'D':
                compbase = 'H';
                break;
            case 'H':
                compbase = 'D';
                break;
            case 'K':
                compbase = 'M';
                break;
            case 'N':
                compbase = 'N';
                break;
            case 'R':
                compbase = 'Y';
                break;
            case 'S':
                compbase = 'S';
                break;
            case 'M':
                compbase = 'K';
                break;
            case 'V':
                compbase = 'B';
                break;
            case 'W':
                compbase = 'W';
                break;
            case 'Y':
                compbase = 'R';
                break;
            case 'b':
                compbase = 'v';
                break;
            case 'd':
                compbase = 'h';
                break;
            case 'h':
                compbase = 'd';
                break;
            case 'k':
                compbase = 'm';
                break;
            case 'n':
                compbase = 'n';
                break;
            case 'r':
                compbase = 'y';
                break;
            case 's':
                compbase = 's';
                break;
            case 'v':
                compbase = 'b';
                break;
            case 'w':
                compbase = 'w';
                break;
            case 'y':
                compbase = 'r';
                break;
            case 'm':
                compbase = 'k';
                break;
            default:
                throw new IllegalArgumentException("No complementary base for " + abase);
        }
        return compbase;
    }

    public static void main(String[] args) {
        RevComp revcomp = new RevComp();
        double startTime = System.currentTimeMillis();
        revcomp.initiate();
        //double middleTime = System.currentTimeMillis();
        //String rc = revcomp.run("ATGCATCCCAAATAANRW");
        for (int i = 0; i < 1000; i++) {
            String rc = revcomp.run("ATGCATCCCAAATAANRWATCCCAAATAANRWATCCCAAATAANRWATCCCAAATAANRWCATCCCAAATAANRWATCCCAAATAANRWATCCCAAATAANRWATCCCAAATAANRW");
        }
        double stopTime = System.currentTimeMillis();

        System.out.println(startTime);
        System.out.println(stopTime);
        System.out.println(stopTime - startTime);

    }
}
