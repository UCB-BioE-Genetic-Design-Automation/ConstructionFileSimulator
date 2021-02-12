package org.ucb.c5.sequtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the reverse complement of a DNA sequence
 *
 * @author J. Christopher Anderson
 */
public class RevComp {
    Map<Character, String> baseToRC;

    public void initiate() {
        baseToRC = new HashMap<>();
        
        baseToRC.put('A', "T");
        baseToRC.put('T', "A");
        baseToRC.put('C', "G");
        baseToRC.put('G', "C");
        baseToRC.put('a', "t");
        baseToRC.put('t', "a");
        baseToRC.put('c', "g");
        baseToRC.put('g', "c");
        baseToRC.put('B', "V");
        baseToRC.put('D', "H");
        baseToRC.put('H', "D");
        baseToRC.put('K', "M");
        baseToRC.put('N', "N");
        baseToRC.put('R', "Y");
        baseToRC.put('S', "S");
        baseToRC.put('M', "K");
        baseToRC.put('V', "B");
        baseToRC.put('W', "W");
        baseToRC.put('Y', "R");
        baseToRC.put('b', "v");
        baseToRC.put('d', "h");
        baseToRC.put('h', "d");
        baseToRC.put('k', "m");
        baseToRC.put('n', "n");
        baseToRC.put('r', "y");
        baseToRC.put('s', "s");
        baseToRC.put('v', "b");
        baseToRC.put('w', "w");
        baseToRC.put('y', "r");
        baseToRC.put('m', "k");
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
            String base = baseToRC.get(achar);
            if(base==null) {
                throw new IllegalArgumentException("Invalid base " + achar + " in " + dna);
            }
            sb.append(base);
        }

        return sb.toString();
    }
    
    public static void main(String[] args) {
        RevComp revcomp = new RevComp();
        revcomp.initiate();
        
        String rc = revcomp.run("ATGCATCCCAAATAANRW");
        System.out.println(rc);
    }
}
