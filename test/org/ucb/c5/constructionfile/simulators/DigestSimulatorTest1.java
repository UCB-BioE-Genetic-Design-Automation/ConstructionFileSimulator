package org.ucb.c5.constructionfile.simulators;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ucb.c5.constructionfile.ParseOligo;
import org.ucb.c5.constructionfile.model.Enzyme;
import org.ucb.c5.constructionfile.model.Modifications;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
import org.ucb.c5.sequtils.RestrictionEnzymeFactory;

/**
 *
 * @author michaelfernandez
 */
public class DigestSimulatorTest1 {

    RestrictionEnzymeFactory factory;
    DigestSimulator dig;
    PCRSimulator sim;
    ParseOligo po;

    @Before
    public void initializeNewDigestSimulatorTest() throws Exception {
        sim = new PCRSimulator();
        sim.initiate();
        po = new ParseOligo();

        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        DigestSimulator dig = new DigestSimulator();
        dig.initiate();
    }

    @Test(timeout = 3000)
    public void testDigest_linear() throws Exception {
        System.out.println("linear");
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        DigestSimulator dig = new DigestSimulator();
        dig.initiate();

        Polynucleotide poly = new Polynucleotide("AAAGAATTCAAA", "", "", true, false, false, Modifications.Alex456N, Modifications.TET);

        List<RestrictionEnzyme> enz = new ArrayList<>();
        enz.add(factory.run(Enzyme.EcoRI));

        List<Polynucleotide> pdts = dig.run(poly, enz);

        Polynucleotide frag1 = pdts.get(0);
        Polynucleotide frag2 = pdts.get(1);

        System.out.println(frag1);
        System.out.println(frag2);

        assert (frag1.getMod5() == Modifications.Alex456N);
        assert (frag1.getMod3() == Modifications.phos5);

        assert (frag1.getSequence().equals("AAAG"));
        assert (frag1.getExt3().equals("AATT"));

        assert (frag2.getMod5() == Modifications.phos5);
        assert (frag2.getMod3() == Modifications.TET);

        assert (frag2.getSequence().equals("CAAA"));
        assert (frag2.getExt5().equals("AATT"));
    }

    @Test(timeout = 3000)
    public void testDigest_circular() throws Exception {
        System.out.println("circular");
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        DigestSimulator dig = new DigestSimulator();
        dig.initiate();

        //EcoR1 site circular example
        Polynucleotide poly_circ = new Polynucleotide("AAAGAATTCAAA", "", "", true, false, true, Modifications.circular, Modifications.circular);

        List<RestrictionEnzyme> enz_c = new ArrayList<>();
        enz_c.add(factory.run(Enzyme.EcoRI));

        List<Polynucleotide> pdts_c = dig.run(poly_circ, enz_c);

        Polynucleotide frag_c = pdts_c.get(0);

        System.out.println(frag_c);
        
        assert(frag_c.getMod5() == Modifications.phos5);
        assert(frag_c.getMod3() == Modifications.phos5);

        assert(frag_c.getSequence().equals("CAAAAAAG"));
        assert(frag_c.getExt3().equals("AATT"));
        
        assert(frag_c.getExt5().equals("AATT"));

        
    }

}
