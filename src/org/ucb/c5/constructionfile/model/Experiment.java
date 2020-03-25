/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author J. Christopher Anderson
 */
public class Experiment {
    private final String name;
    private final List<ConstructionFile> cfs;
    private final Map<String, Polynucleotide> nameToPoly;

    public Experiment(String name, List<ConstructionFile> cfs, Map<String, Polynucleotide> nameToPoly) {
        this.name = name;
        this.cfs = cfs;
        this.nameToPoly = nameToPoly;
    }

    public String getName() {
        return name;
    }

    public List<ConstructionFile> getCfs() {
        return cfs;
    }

    public Map<String, Polynucleotide> getNameToPoly() {
        return nameToPoly;
    }
    
    
}
