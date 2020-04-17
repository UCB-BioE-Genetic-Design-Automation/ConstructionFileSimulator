/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.constructionfile.simulators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.PolyRevComp;

/**
 *
 * @author J. Christopher Anderson
 */
public class NewPCRSimulator {
    private PolyRevComp revcomp;
    
    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
    }
    
    public Polynucleotide run(Polynucleotide oligo1, Polynucleotide oligo2, List<Polynucleotide> templates) throws Exception {
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
        
        Set<Polynucleotide> oldspecies = new HashSet<>(species);
        
        //Simulate thermocycling
        simulateDissociation(species);
        while(true) {
            if(species == oldspecies) {
                break;
            }
            
            oldspecies = species;
            
            simulateAnneal(species);
            simulatePolymerization(species);
            simulateDissociation(species);
        }
        
        //Determine which of the species is the PCR product
            //Check each species 3' and 5' ends if they match oligo1 and oligo2
        
        //Return the single PCR product
        return null;
    }

    private void simulateDissociation(Set<Polynucleotide> species) throws Exception {
        Set<Polynucleotide> newspecies =  new HashSet<>();
        for(Polynucleotide poly : species) {
            //If it's already single stranded, no need to do anything
            if(!poly.isIsDoubleStranded()) {
                continue;
            }
            
            //Handle if it is circular
            if(poly.isIsCircular()) {
                //TODO, not yet implemented
                continue;
            }
      
            //Handle as linear, double stranded
            Polynucleotide forstrand = dissociate(poly);
            Polynucleotide rev = revcomp.run(poly);
            Polynucleotide revstrand = dissociate(rev);
            newspecies.add(forstrand);
            newspecies.add(revstrand);
            
            //TODO:  need to override .equals and hashcode functions in Polynucleotide for this to collapse properly
        }
        species.addAll(newspecies);
    }
    
    private Polynucleotide dissociate(Polynucleotide poly) {
            String forstrand = poly.getSequence();
            
            if (!poly.getExt5().startsWith("-")) {
                forstrand = poly.getExt5() + forstrand;
            }
            
            if (poly.getExt3().startsWith("-")) {
                forstrand += poly.getExt3();
            }
            
            return new Polynucleotide(forstrand);
    }

    private void simulateAnneal(Set<Polynucleotide> species) {
        //similar code exists in old PCRSimulator
        //Move the single stranded ends of the annealed products to 5ext and 3ext of poly
    }

    private void simulatePolymerization(Set<Polynucleotide> species) {
        //For each Polynucleotide, if there is a recessed 5' ext, fill it in
    }
    
    public static void main(String[] args) throws Exception {
        NewPCRSimulator sim = new NewPCRSimulator();
        sim.initiate();

        //run a regular pcr
        
        //run an ipcr on a circular template over the origin
        
        //run a pcr with degenerate oligos
        
        //run a soeing reaction on 2+ templates
        
        //run a wobble with 2 oligos no template
        
        //run a PCA with many oligos as template and 2 oligos as primers
        
        //run a hybrid with 2 oligos, and both oligo and ds templates
        
    }
}
