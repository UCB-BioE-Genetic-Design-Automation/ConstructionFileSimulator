package org.ucb.c5.sequtils;

import org.ucb.c5.sequtils.RevComp;

/**
 * Calculates local secondary structure present in a dna sequence
 * 
 * Spacing between arms of the hairpin can be anywhere from 4 to 9, and are
 * considered to be of equal weight
 * 
 * @author J. Christopher Anderson
 */
public class HairpinCounter {
    
    private RevComp revcomp;
    
    public void initiate() throws Exception {
        revcomp = new RevComp();
        revcomp.initiate();
    }
    
    /**
     * Calculates a score for folding for all combined
     * hairpins in a sequence.  Crudely related to deltaG.
     * 
     * This isn't biophysically quite correct, but a decent approximation
     * 
     * @param seq  The input sequence (any length, any number of hairpins)
     * @return  a deltaG of stabilization of haipins
     * @throws Exception 
     */
    public double run(String seq) throws Exception {
        seq = seq.toUpperCase();
        char[] rcSeq = revcomp.run(seq).toCharArray();
        char[] charSeq = seq.toCharArray();
        int len = seq.length();
        double out = 0.0;
        
        //For each spacer length between 4 and 9
        for(int spaces = 4; spaces <= 9; spaces++) {
            
            //scan through the sequence and count hbonds of each potential hairpin
            for(int i=0; i<len-spaces-12; i++) {
                int hbonds = countHbonds(charSeq, i, i+spaces+12, rcSeq, len);
                out+= Math.pow(2, hbonds) - 1;
            }
        }
        
        return out;
    }
    
    /**
     * Counts the number of hbonds participating in hairpins
     * 
     * @param seq
     * @param startInc
     * @param endExcl
     * @param revC
     * @param len
     * @return 
     */
    private int countHbonds(char[] seq, int startInc, int endExcl, char[] revC, int len) {
        int suffixStart = endExcl - 6;
        
        //See how many out of the six being examined match
        int matchlength = 0;
        for(int distFromEnd=0; distFromEnd<6; distFromEnd++) {
            char revChar = revC[len - 1 - startInc - 5 + distFromEnd];
            char forChar = seq[suffixStart + distFromEnd];
            if (forChar == revChar) {
                matchlength = distFromEnd;
            } else {
                break;
            }
        }

        //If there aren't 3 bp matching in hairpin, score as zero
        if(matchlength < 2) {
            return 0;
        }

        //If 3 or more matches, score based on GC/AT content
        int hbonds = 0;
        for(int i=0; i<matchlength; i++) {
            // compute metrics on revComplement(prefix).charAt(i); get that char using indexing as above
            char achar = revC[len - 1 - startInc - 5 + i];
            if(achar == 'C' || achar == 'G') {
                hbonds+=3;
            }
            if(achar == 'A' || achar == 'T') {
                hbonds+=2;
            }
        }

        return hbonds;
    }
    
    public static void main(String[] args) throws Exception {
        HairpinCounter hc = new HairpinCounter();
        hc.initiate();
        
        //A simple hairpin (TODO:  This is giving the wrong answer)
        double d = hc.run("CCCAAAAAAAGGGAAAAAAAAAAAAAAAAA");
        System.out.println("Hairpin error score: " + d);
        
        //No hairpin
         d = hc.run("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println("No hairpin score: " + d);
        
        //A simple hairpin
        d = hc.run("AAACCCAAAAAAAGGGAAAAAAAAAAAAAA");
        System.out.println("CCC..GGG score: " + d);
        
        //A simple hairpin
        d = hc.run("AAACCCCAAAAAAGGGGAAAAAAAAAAAAA");
        System.out.println("CCCC..GGGG score: " + d);
        
        //A simple hairpin
        d = hc.run("AAACGCGAAAAAACGCGAAAAAAAAAAAAA");
        System.out.println("CGCG..CGCG score: " + d);
        
        //A simple hairpin
        d = hc.run("AAACACGAAAAAACGTGAAAAAAAAAAAAA");
        System.out.println("CACG..CGTG score: " + d);
        
        //A simple hairpin
        d = hc.run("AAACCCCCAAAAAGGGGGAAAAAAAAAAAA");
        System.out.println("Hairpin4 score: " + d);
        
        //Random sequence1
        d = hc.run("AAGTCGATTCCTTCGATGGTTATAAATCAA");
        System.out.println("rand1 score: " + d);
        
        //Random sequence2
        d = hc.run("GCTGACAAGGGTTTGACATCGAGAAACAAT");
        System.out.println("rand2 score: " + d);
              
        //Random sequence with lots of A's added
        d = hc.run("GCTGACAAGGGTTTGACATCGAGAAACAATAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println("rand3 score: " + d);
        
        //Long random sequence3
        d = hc.run("AGAAGACGCAAGTCGATTCCTTCGATGGTTATAAATCAACTACTGAATTGTGCGATCCCTCCACCTCAGCTAAGGTAGCGCTGACAAGGGTTTGACATCGAGAAACAATTACCAATATTTAGTTTTTTAGCCTTGCGACAGACCTCCTACTTAGATTGCCACGCATTGAGCTAGCGAGTCAGCGATAAGCATGACGCGCTTTCAAGCGTCGCGAGTATGTGAACCAAGGCTCCGGACAGGACTATATACTTGGGTTTGATCTCGCCCCGACAACTGCAAACCTCAACATTTATAGATTATAAGGTTAGCCGAAATTGCACGTGGTGGCGCCCGCCGACTGCTCCCCGAGTGTGGCTCTTTGATCTGACAACGCGCGACCTCCATCGCGGCCGATTGTTTCTGCGGACCATGTCGTCCTCATAGTTTGGGCATGTTTCCGTTGTAGGAGTGAAGCCACTTAGCTTTGCGCCGTAGTCCCAATGAAAAACCTATGGACTTTGTTTTGGGTAGCATCAGGAATCTGAACCCTGTGAATGTGGGGGTCGCGCGCATAGACCTTTATCTCCGGTTCAAGTTAGGCATGAGGCTGCATGCTACGTTGTCACACCTACACTGCTCGAAGTAAATATGGGAAGCGCGCGGCCTGGCCCGAGGCGTTCCGCGCCGCCACGTGTTCGTTAACTGTTGATTGGTGGCACATAAGCAATACCGTAGTCCCTCAAATTCAGCTCTGTTATCTCGAGCGTTATGTGTCAAATGGCGTAGAACGGGATTGACTGTTTGACACTAGCTGGTGTTCGGTTCGGTAACGGAGAATCTGTGGGGCTATGTCACTAATACTTTCGAAACGCCCCGTACCGATGCTGAACAAGTCGATGCAGGCTCCCGTCTTTGAATAGGGGTAAACATACAAGTCGATAGAAGATGGGTAGGGGCCTCCAATTCATCCAACACTCTACGCCTTCTCCAAGAGCTAGTAGGGCACCCTGCAGTTGGAAAGGGAACTATTTCGTAGGGCGAGCCCATACCGTCTCTCTTGCGGAAGACTTAACACGATAGG");
        System.out.println("rand4 score: " + d);

        //A tRNA
        d = hc.run("GGAGAGATGCCGGAGCGGCTGAACGGACCGGTCTCTAAAACCGGAGTAGGGGCAACTCTACCGGGGGTTCAAATCCCCCTCTCTCCGCCA");
        System.out.println("tRNA score: " + d);
    }
}
