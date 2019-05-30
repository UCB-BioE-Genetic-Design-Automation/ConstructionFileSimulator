package org.ucb.c5.constructionfile.model;

import java.util.List;

/**
 *assemble p20N45,p20N82,p20N24,pBsmGG1	(BsaI, gg)

 * @author J. Christopher Anderson
 */
public class Assembly extends Step {
    private final List<String> fragments;
    private final Enzyme enzyme;
    private final String product;

    public Assembly(List<String> fragments, Enzyme enzyme, String product) {
        this.fragments = fragments;
        this.enzyme = enzyme;
        this.product = product;
    }

    public List<String> getFragments() {
        return fragments;
    }

    public Enzyme getEnzyme() {
        return enzyme;
    }

    @Override
    public Operation getOperation() {
        return Operation.assemble;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
