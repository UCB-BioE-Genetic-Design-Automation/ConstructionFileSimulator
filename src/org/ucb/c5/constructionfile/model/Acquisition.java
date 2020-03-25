package org.ucb.c5.constructionfile.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class Acquisition implements Step {
    private final String dnaName;
    
    public Acquisition(String dnaName) {
        this.dnaName = dnaName;
    }

    @Override
    public Operation getOperation() {
        return Operation.acquire;
    }

    @Override
    public String getProduct() {
        return dnaName;
    }
}
