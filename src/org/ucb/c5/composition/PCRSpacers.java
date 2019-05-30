package org.ucb.c5.composition;

import org.ucb.c5.sequtils.RevComp;

/**
 * For encoding spacer elements and convenience methods
 * for accessing them.
 * 
 * @author J. Christopher Anderson
 */
 class PCRSpacers {
    private final String[] spacers;
    private final String[] revcomps;
    
    PCRSpacers() {
        //Oligos curated from QPCR data
        spacers = new String[11];
        spacers[0] = "GGCATGGACTGTGGTCATGAG"; //GAPDH
        spacers[1] = "CTGTTAGGCACGTAAATATAGACC"; //MCUR1
        spacers[2] = "GGGTACCCACGCGAATCAC"; //HMBS
        spacers[3] = "GGGGCTGTACTGCTTAACCAG"; //Hprt1
        spacers[4] = "CAGGCCCGTAGTGCTTCAG"; //PPIB
        spacers[5] = "CTGGTACACAGGGTGATGG"; //CMPK2
        spacers[6] = "GAAGAGTTAATGAGCTTCACG"; //LMNTD1
        spacers[7] = "CGGGTTTATGCCAATAATGAC"; //TDG
        spacers[8] = "GGTGATGAAGCTACACAATTCC"; //CCL14
        spacers[9] = "CACAGAGTGGGTTAAAGCG"; //IL2RG
        spacers[10] = "GAGACTGACGCGCCCCAGCC"; //MBH
        
        //Construct the reverse complement of each oligo
        RevComp rc = new RevComp();
        rc.initiate();
        
        revcomps = new String[spacers.length];
        for(int i=0; i<spacers.length; i++) {
            revcomps[i] = rc.run(spacers[i]);
        }
    }
    
    String getPromoter5Prime() {
        return spacers[0];
    }
    
    String getPromoter3Prime() {
        return spacers[1];
    }
    
    String getPromoterReverse() {
        return revcomps[1];
    }
    
    String getTerminator3Prime() {
        return spacers[spacers.length - 1];
    }
    
    String getGene5Prime(int orfIndex) {
        return spacers[orfIndex + 1];
    }
    
    String getGene3Prime(int orfIndex) {
        return spacers[orfIndex + 2];
    }
    
    String getGeneReverse(int orfIndex) {
        return revcomps[orfIndex + 2];
    }
    
    int size() {
        return spacers.length;
    }

    public static void main(String[] args) {
        PCRSpacers spacer = new PCRSpacers();
        System.out.println("Promoter forward: " + spacer.getPromoter5Prime());
        System.out.println("Promoter reverse: " + spacer.getPromoterReverse());
    }

}
