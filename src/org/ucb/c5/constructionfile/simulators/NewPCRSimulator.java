/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.model.Polynucleotide;

/**
 *
 * @author J. Christopher Anderson
 */
public class NewPCRSimulator {
    public void initiate() throws Exception {}
    
    public Polynucleotide run(Polynucleotide oligo1, Polynucleotide oligo2, List<Polynucleotide> templates) {
        if(oligo1.isIsDoubleStranded()) {
            throw new IllegalArgumentException("Oligo1 must be single stranded");
        }
        if(oligo2.isIsDoubleStranded()) {
            throw new IllegalArgumentException("Oligo2 must be single stranded");
        }
        
        //Combine all the species in a set
        Set<Polynucleotide> species = new HashSet<>();
        species.add(oligo1);
        species.add(oligo2);
        species.addAll(templates);
        
        //Simulate thermocycling
        int speciecount = species.size();
        while(true) {
            simulateDissociation(species);
            simulateAnneal(species);
            simulatePolymerization(species);
            
            if(species.size() == speciecount) {
                break;
            }
            speciecount = species.size();
        }
        
        //Determine which of the species is the PCR product
            //Check each species 3' and 5' ends if they match oligo1 and oligo2
        
        //Return the single PCR product
        return null;
    }

    private void simulateDissociation(Set<Polynucleotide> species) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void simulateAnneal(Set<Polynucleotide> species) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void simulatePolymerization(Set<Polynucleotide> species) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
