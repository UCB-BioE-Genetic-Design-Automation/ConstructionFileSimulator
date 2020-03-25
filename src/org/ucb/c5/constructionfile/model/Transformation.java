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
 */
public class Transformation implements Step {

    private final String dna;
    private final String strain;
    private final Antibiotic antibiotic;
    private final String product;

    public Transformation(String dna, String strain, Antibiotic antibiotic, String product) {
        this.dna = dna;
        this.strain = strain;
        this.antibiotic = antibiotic;
        this.product = product;
        List<String> substrates = new ArrayList<>();
        substrates.add(dna);
    }

    public String getDna() {
        return dna;
    }

    public String getStrain() {
        return strain;
    }

    public Antibiotic getAntibiotic() {
        return antibiotic;
    }

    @Override
    public Operation getOperation() {
        return Operation.transform;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
