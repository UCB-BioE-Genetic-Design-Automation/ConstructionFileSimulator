package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public class Digestion implements Step {
    private final String substrate;
    private final List<String> enzymes;
    private final int fragSelection;
    private final String product;

    public Digestion(String substrate, List<String> enzymes, int fragSelection, String product) {
        this.substrate = substrate;
        this.enzymes = enzymes;
        this.product = product;
        this.fragSelection = fragSelection;
        List<String> substrates = new ArrayList<>();
        substrates.add(substrate);
    }
    
    public List<String> getEnzymes() {
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

    public int getFragSelection() {
        return fragSelection;
    }
}
