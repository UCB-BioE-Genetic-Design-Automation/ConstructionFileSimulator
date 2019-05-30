/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.constructionfile.model;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author J. Christopher Anderson
 * @author lucaskampman
 */
public class Cleanup extends Step {
    private final String Digproduct;
    private final String CleanedUp;
    
    public Cleanup(String substrate, String product) {
        this.Digproduct = substrate;
        List<String> substrates = new ArrayList<>();
        substrates.add(substrate);
        this.setSubstrates(substrates);
        this.CleanedUp = product;
    }
    
    public String getSubstrate() {
        return Digproduct;
    }

    @Override
    public Operation getOperation() {
        return Operation.cleanup;
    }

    @Override
    public String getProduct() {
        return CleanedUp;
    }
}
