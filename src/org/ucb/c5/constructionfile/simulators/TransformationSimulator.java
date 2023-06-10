package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import java.util.Map;
import org.ucb.c5.constructionfile.model.Transformation;

/**
 * A Function that inputs a DNA sequence and a List of restriction enzymes and
 * computes all the fragments that will be present upon digestion to completion
 *
 * @author J. Christopher Anderson
 */
public class TransformationSimulator {

    private PolyRevComp revcomp;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
    }

    /**
     * Higher-level digestion call from a loose-coupled Digestion object and 
     * sequences.  Returns the single Polynucleotide product described in the
     * Digestion object.
     * 
     * @param dig
     * @param fragments
     * @return
     * @throws Exception 
     */
    public Polynucleotide run(Transformation trans, Map<String, Polynucleotide> fragments) throws Exception {
        Polynucleotide substrate = fragments.get(trans.getDna());
        if(!substrate.isCircular()) {
            throw new Exception("Cannot transform a linear DNA");
        }
        return substrate;
    }

}
