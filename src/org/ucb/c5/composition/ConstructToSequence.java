package org.ucb.c5.composition;

import java.util.List;
import org.ucb.c5.composition.model.Construct;
import org.ucb.c5.composition.model.Transcript;

/**
 * Compiles a Construct to a DNA sequence, adding PCR spacers
 * between each rbs.CDS
 * 
 * @author J. Christopher Anderson
 */
public class ConstructToSequence {
    private final PCRSpacers spacers;
    
    public ConstructToSequence() {
        spacers = new PCRSpacers();
    }
    
    public void initiate() throws Exception { }
    
    public String run(Construct construct, boolean includeSpacers) throws Exception {
        StringBuilder out = new StringBuilder();
        
        //Put in spacer-promoter-spacer
        if(includeSpacers)
            out.append(spacers.getPromoter5Prime());
        out.append(construct.getPromoter());
        if(includeSpacers)
            out.append(spacers.getPromoter3Prime());
        
        //Put in each spacer
        List<Transcript> mrnas = construct.getmRNAs();
        for(int i=0; i<mrnas.size(); i++) {
            Transcript mrna = mrnas.get(i);
            out.append(TranscriptToSequence(mrna));
            if(includeSpacers)
                out.append(spacers.getGene3Prime(i));
        }
        
        //Put in the remaining spacers
        if(includeSpacers) {
            for(int i=mrnas.size(); i<spacers.size() - 3; i++) {
                out.append(spacers.getGene3Prime(i));
            }
        }
        
        //Put in the terminator and the final spacer
        out.append(construct.getTerminator());
        if(includeSpacers)
            out.append(spacers.getTerminator3Prime());
        
        return out.toString();
    }
    
    private String TranscriptToSequence(Transcript mrna)  throws Exception {
        //Construct the mRNA sequence one codon at a time
        StringBuilder out = new StringBuilder();
        for(int i=0; i<mrna.getPeptide().length(); i++) {
            out.append(mrna.getCodons()[i]);
        }
        
        //Add a stop codon to the coding sequence
        out.append("TAA");
            
        //To visually distinguish, make the RBS lowercase and the CDS uppercase
        return mrna.getRbs().getRbs().toLowerCase() + out.toString().toUpperCase();
    }
}
