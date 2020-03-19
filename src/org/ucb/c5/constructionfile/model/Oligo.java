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
public class Oligo {
    private String name;
    private String sequence;
    private String description;

    public Oligo(String name, String sequence, String description) {
        this.name = name;
        this.sequence = sequence;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getSequence() {
        return sequence;
    }

    public String getDescription() {
        return description;
    }    
}
