package org.ucb.c5.utils;

/**
 * Utility methods for common molecular biology operations
 *
 * @author J. Christopher Anderson
 */
public class SequenceUtils {

    public static String complement(String rbsrc) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rbsrc.length(); i++) {
            char achar = rbsrc.charAt(i);
            if (achar == 'A') {
                sb.append("T");
            } else if (achar == 'T') {
                sb.append("A");
            } else if (achar == 'C') {
                sb.append("G");
            } else if (achar == 'G') {
                sb.append("C");
            } else if (achar == 'a') {
                sb.append("t");
            } else if (achar == 't') {
                sb.append("a");
            } else if (achar == 'c') {
                sb.append("g");
            } else if (achar == 'g') {
                sb.append("c");
            }
        }
        return sb.toString();
    }

    public static double calcGC(String inseq) {
        String seq = inseq.toUpperCase();
        //Tally up the A's and G's
        int cs = 0;
        int gs = 0;

        for (int i = 0; i < seq.length(); i++) {
            char achar = seq.charAt(i);
            if (achar == 'C') {
                cs++;
            } else if (achar == 'G') {
                gs++;
            }
        }
        
        double length = 1.0*seq.length();
        double out = (gs + cs)/length;
        return out;
    }

}
