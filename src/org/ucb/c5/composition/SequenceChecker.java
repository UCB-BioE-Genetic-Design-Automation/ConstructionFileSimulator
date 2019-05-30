package org.ucb.c5.composition;

import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.sequtils.RevComp;

/**
 * Checks a sequence for forbidden sequences
 * 
 * @author J. Christopher Anderson
 */
public class SequenceChecker {
    
    private RevComp revcomp;
    private List<String> forbidden;
    
    public void initiate() {
        revcomp = new RevComp();
        revcomp.initiate();
     
        //Populate forbidden sequences
        forbidden = new ArrayList<>();
        forbidden.add("AAAAAAAA"); //poly(A)
        forbidden.add("TTTTTTTT"); //poly(T)
        forbidden.add("CCCCCCCC"); //poly(C)
        forbidden.add("GGGGGGGG"); //poly(G)
        forbidden.add("ATATATAT"); //poly(AT)
        forbidden.add("CAATTG");   //MfeI
        forbidden.add("GAATTC");   //EcoRI
        forbidden.add("GGATCC");   //BamHI
        forbidden.add("AGATCT");   //BglII
        forbidden.add("ACTAGT");   //SpeI
        forbidden.add("TCTAGA");   //XbaI
        forbidden.add("GGTCTC");   //BsaI
        forbidden.add("CGTCTC");   //BsmBI
        forbidden.add("CACCTGC");  //AarI
        forbidden.add("CTGCAG");   //PstI
        forbidden.add("CTCGAG");   //XhoI
        forbidden.add("GCGGCCGC"); //NotI
        forbidden.add("AAGCTT");   //HindIII
    }
    
    /**
     * Checks a DNA sequence for forbidden Strings
     * 
     * @param dnaseq
     * @return true if passes; false if contains a forbidden sequence
     */
    public boolean run(String dnaseq) {
        String rc = revcomp.run(dnaseq);
        String combined = dnaseq + "x" + rc;
        combined = combined.toUpperCase();
        
        for(String site : forbidden) {
            if(combined.contains(site)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        SequenceChecker checker = new SequenceChecker();
        checker.initiate();
        boolean result = checker.run("GGGGGGGGG");  //returns false due to poly(G)
        System.out.println(result);
    }
}
