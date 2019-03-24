package org.ucb.c5.construction.demos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.construction.DigestSimulator;
import org.ucb.c5.construction.LigateSimulator;
import org.ucb.c5.construction.RestrictionEnzymeFactory;
import org.ucb.c5.construction.RestrictionEnzymeFactory.Enzyme;
import org.ucb.c5.construction.model.Polynucleotide;

/**
 *
 * @author J. Christopher Anderson
 */
public class DemoDigAndLig {

    public static void main(String[] args) throws Exception {
        //Initialize the functions
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        DigestSimulator dig = new DigestSimulator();
        dig.initiate();

        //Construct the polynucleotide and restriction enzymes
        Polynucleotide poly = new Polynucleotide("atatatatGGATCCacacacacacGAGGAGaaaaaaaaCCagagagagag", true);
        System.out.println("Starting plasmid:");
        System.out.println(poly.toString());

        List enz = new ArrayList<>();
        enz.add(factory.run(Enzyme.BseRI));
        enz.add(factory.run(Enzyme.BamHI));

        //Run the Digest Function
        List<Polynucleotide> digfrags = dig.run(poly, enz);

        System.out.println("Digestion fragments:");
        for (Polynucleotide pol : digfrags) {
            System.out.println(pol.toString());
        }

        //Religate the fragments
        LigateSimulator lig = new LigateSimulator();
        lig.initiate();
        
        //Run the Ligate Function
        Polynucleotide pdt = lig.run(digfrags);
        
        System.out.println("Religated product:");
        System.out.println(pdt.toString());
    }

}
