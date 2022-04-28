package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List getInputs() {
        List<String> inputs = new ArrayList<String>();
        inputs.add(dnaName);
        return inputs;
    }
}
