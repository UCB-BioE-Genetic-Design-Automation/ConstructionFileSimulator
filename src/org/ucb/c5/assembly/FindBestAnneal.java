package org.ucb.c5.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author J. Christopher Anderson
 */
public class FindBestAnneal {

    public void initiate() throws Exception {
    }

    /**
     *
     * @param dnaRegion The region in which to find a ~20bp anneal
     * @param float5 If false the startIndex is 0, otherwise variable
     * @param float3 If false the endIndex is the end of dnaRegion, otherwise
     * variable
     * @return a 2-item array for the start and end indices of the anneal on dnaRegion
     */
    public int[] run(String dnaRegion, boolean float5, boolean float3) throws Exception {
        if (!float5 && !float3) {
            throw new IllegalArgumentException("One or the other end of the annealing region must be allowed to float");
        }

//        The overall G/C content of your annealing region should be between 50 and 65%
//        The overall base composition of the sequences should be balanced (no missing bases, no excesses of one particular base)
//        The length of your sequence is around 18 and 25 bp
//        The sequence should appear random. There shouldn't be long stretches of a single base, or large regions of G/C rich sequence and all A/T in other regions
//        There should be little secondary structure

        int[] indices = new int[2];
        int startIndex = 0;
        int endIndex = dnaRegion.length();

        indices[0] = startIndex;
        indices[1] = endIndex;
        return indices;
    }

    public static void main(String[] args) throws Exception {
        FindBestAnneal fba = new FindBestAnneal();
        fba.initiate();
        String seq = "ATGAGTATTCAACATTTCCGTGTCGCCCTTATTCCCTTTT";
        
        int[] annealTT = fba.run(seq, true, true);
        System.out.println("TT: " + seq.substring(annealTT[0], annealTT[1]));
        
        int[] annealTF = fba.run(seq, true, false);
        System.out.println("TF: " + seq.substring(annealTF[0], annealTF[1]));
        
        int[] annealFT = fba.run(seq, false, true);
        System.out.println("FT: " + seq.substring(annealFT[0], annealFT[1]));
        
        try {
            int[] annealFF = fba.run(seq, false, false);
        } catch(Exception err) {
            System.out.println("Correctly erroring on false, false");
        }
    }
}
