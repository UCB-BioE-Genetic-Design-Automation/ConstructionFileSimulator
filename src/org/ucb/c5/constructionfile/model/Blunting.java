package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author yisheng
 */
public class Blunting extends Step{
    private final String substrate;
    private final String types;
    private final String product;
    

public Blunting(String substrate, String types, String product) {
        this.substrate = substrate;
        this.types = types;
        this.product = product;
        List<String> substrates = new ArrayList<>();
        substrates.add(substrate);
        this.setSubstrates(substrates);

    }

public String getSubstrate() {
        return substrate;
    }
    
    public String getTypes() {
        return types;
    }    
    
    @Override
    public Operation getOperation() {
        return Operation.blunting;
    }

    @Override
    public String getProduct() {
        return product;
    }    
}