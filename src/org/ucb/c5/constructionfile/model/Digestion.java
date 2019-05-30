package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public class Digestion extends Step {
    private final String substrate;
    private final List<Enzyme> enzymes;
    private final String product;

    public Digestion(String substrate, List<Enzyme> enzymes, String product) {
        this.substrate = substrate;
        this.enzymes = enzymes;
        this.product = product;
        List<String> substrates = new ArrayList<>();
        substrates.add(substrate);
        this.setSubstrates(substrates);
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
}
