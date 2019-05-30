package org.ucb.c5.composition;

import org.ucb.c5.composition.model.RBSOption;
import org.ucb.c5.composition.model.Transcript;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This reverse translates a protein sequence to a DNA and chooses an RBS. It
 * uses the highest CAI codon for each amino acid in the specified Host.
 *
 * @author J. Christopher Anderson
 * 
 * TODO:  REPLACE WITH YOUR VERSION
 */
public class TranscriptDesigner {

    private Map<Character, String> aminoAcidToCodon;
    private RBSChooser rbsChooser;  //Delete the 2 to use old algorithm

    public void initiate() throws Exception {
        //Initialize the RBSChooser
        rbsChooser = new RBSChooser();  //Delete the 2 to use old algorithm
        rbsChooser.initiate();
        
        //Construct a map between each amino acid and the highest-CAI codon for E coli
        aminoAcidToCodon = new HashMap<>();

        aminoAcidToCodon.put('A', "GCG");
        aminoAcidToCodon.put('C', "TGC");
        aminoAcidToCodon.put('D', "GAT");
        aminoAcidToCodon.put('E', "GAA");
        aminoAcidToCodon.put('F', "TTC");
        aminoAcidToCodon.put('G', "GGT");
        aminoAcidToCodon.put('H', "CAC");
        aminoAcidToCodon.put('I', "ATC");
        aminoAcidToCodon.put('K', "AAA");
        aminoAcidToCodon.put('L', "CTG");
        aminoAcidToCodon.put('M', "ATG");
        aminoAcidToCodon.put('N', "AAC");
        aminoAcidToCodon.put('P', "CCG");
        aminoAcidToCodon.put('Q', "CAG");
        aminoAcidToCodon.put('R', "CGT");
        aminoAcidToCodon.put('S', "TCT");
        aminoAcidToCodon.put('T', "ACC");
        aminoAcidToCodon.put('V', "GTT");
        aminoAcidToCodon.put('W', "TGG");
        aminoAcidToCodon.put('Y', "TAC");
    }

    public Transcript run(String peptide, Set<RBSOption> ignores) throws Exception {
        //Choose codons for each amino acid
        String[] codons = new String[peptide.length()];
        for(int i=0; i<peptide.length(); i++) {
            char aa = peptide.charAt(i);
            String codon = aminoAcidToCodon.get(aa);
            codons[i] = codon;
        }
        
        //Choose an RBS
        StringBuilder cds = new StringBuilder();
        for(String codon : codons) {
            cds.append(codon);
        }
        RBSOption selectedRBS = rbsChooser.run(cds.toString(), ignores);
        
        //Construct the Transcript and return it
        Transcript out = new Transcript(selectedRBS, peptide, codons);
        return out;
    }
}
