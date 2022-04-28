package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public class Digestion implements Step {
    private final String substrate;
    private final List<Enzyme> enzymes;
    private final int fragSelection;
    private final String product;

    public Digestion(String substrate, List<Enzyme> enzymes, int fragSelection, String product) {
        this.substrate = substrate;
        this.enzymes = enzymes;
        this.product = product;
        this.fragSelection = fragSelection;
        List<String> substrates = new ArrayList<>();
        substrates.add(substrate);
    }
    
    public List<Enzyme> getEnzymes() {
        return enzymes;
    }

    public String getSubstrate() {
        return substrate;
    }

    @Override
    public Operation getOperation() {
        return Operation.digest;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public List<String> getInputs() {
        List<String> inputs = new ArrayList<String>();
        inputs.add(getSubstrate()) ;
        return inputs;
    }

    public int getFragSelection() {
        return fragSelection;
    }
}
