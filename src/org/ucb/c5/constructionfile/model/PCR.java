/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.constructionfile.model;

import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public class PCR implements Step {
    //Loose-coupled references by name
    private final String oligo1;
    private final String oligo2;
    private final List<String> templates;
    private final String product;
    private final Integer size;
    

    public PCR(String oligo1, String oligo2, List<String> templates, String product, Integer size) {
        this.oligo1 = oligo1;
        this.oligo2 = oligo2;
        this.templates = templates;
        this.product = product;
        this.size = size;
    }

    public String getOligo1() {
        return oligo1;
    }

    public String getOligo2() {
        return oligo2;
    }

    public List<String> getTemplates() {
        return templates;
    }
    
    @Override
    public String getProduct() {
        return product;
    }
    
    @Override
    public Operation getOperation() {
        return Operation.pcr;
    }

    public Integer getSize() {
        return size;
    }
    
    @Override
    public String toString() {
        String out = "Oligo1: " + this.oligo1;
        out += "\nOligo2: " + this.oligo2;
        
        out += "\ntemplate:";
        for(String template : templates) {
            out+= "\n\t" + template;
        }
        out += "\nproduct: " + this.product;
        return out;
    }
}
