package org.ucb.c5.constructionfile.simulators;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Polynucleotide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.ucb.c5.constructionfile.ParseOligo;
import org.ucb.c5.constructionfile.model.Modifications;

/**
 *
 * @author michaelfernandez
 */
public class PCRSimulatorTest3 {
    //modifications are made on the product of a PCR 
    //after running pcrsimulator
    
    @Test(timeout = 3000)
    public void testModification() throws Exception {

        PCRSimulator sim = new PCRSimulator();
        sim.initiate();

        ParseOligo ol = new ParseOligo();

        //run PCR simulator with a modification on the template
        String o1 = "/5Phos/ATTACCGCCTTTGAGTGAGC";
        String o2 = "/5Me-isodC/GTATCACGAGGCAGAATTTCAG";

        Polynucleotide o1p = ol.run(o1);
        Polynucleotide o2p = ol.run(o2);

        List<Polynucleotide> templates1 = new ArrayList<>();
        Polynucleotide template = new Polynucleotide("ATTACCGCCTTTGAGTGAGCAAAAAAAAAAAAAAAAAAAAAACTGAAATTCTGCCTCGTGATAC");
//      Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
        templates1.add(template);
//      templates.add(frag2);
        //(Polynucleotide primer1, Polynucleotide primer2, List<Polynucleotide> templates)
        
        Polynucleotide PCRproduct = sim.run(o1p, o2p, templates1);
        
        System.out.println("PCR_product\n" + PCRproduct);
        
        assert(PCRproduct.getMod5() == Modifications.phos5);
        assert(PCRproduct.getMod3() == Modifications.Me_isodC);
    }
}
