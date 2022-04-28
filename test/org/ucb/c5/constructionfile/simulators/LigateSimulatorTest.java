/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ucb.c5.constructionfile.ParseOligo;
import org.ucb.c5.constructionfile.model.Modifications;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.ComparePolynucleotides;

/**
 *
 * @author michaelfernandez
 */
public class LigateSimulatorTest {

    PCRSimulator sim;
    ParseOligo po;
    ComparePolynucleotides cps;
    LigateSimulator lig;

    @Before
    public void initializeNewLigateSimulatorTest() throws Exception {
        sim = new PCRSimulator();
        sim.initiate();
        po = new ParseOligo();
        cps = new ComparePolynucleotides();
        cps.initiate();
        lig = new LigateSimulator();
        lig.initiate();
    }

    @Test(timeout = 3000)
    public void testDigest_modcombinations_5to3() throws Exception {
        LigateSimulator lig = new LigateSimulator();
        lig.initiate();

        ComparePolynucleotides cps = new ComparePolynucleotides();
        cps.initiate();

        System.out.println("Two BamHI-digested DNAs examples: 5 to 3");
        String BamHIExt = "GATC";

        //5' p1 onto 3'p2 
        //Only a phosphate leftover on the 5' side
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.phos5, Modifications.phos5);

        List<Polynucleotide> frags1 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        Polynucleotide pdt1 = lig.run(frags1);

        System.out.println("poly1");
        System.out.println(poly1);

        System.out.println("poly2");
        System.out.println(poly2);

        System.out.println("Ligation Product: 1");
        System.out.println(pdt1);

        assert (pdt1.getSequence().equals("ccccccGGATCCaaaaaa"));
        assert (pdt1.getExt3().equals("c"));
        assert (pdt1.getMod5().equals(Modifications.phos5));
        assert (pdt1.getMod3().equals(Modifications.hydroxyl));

        //5 to 3
        //Only a phosphate leftover on the 3' side
        Polynucleotide poly3 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly4 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.phos5);

        List<Polynucleotide> frags2 = new ArrayList<>();
        frags2.add(poly3);
        frags2.add(poly4);

        Polynucleotide pdt2 = lig.run(frags2);

        System.out.println("poly3");
        System.out.println(poly3);

        System.out.println("poly4");
        System.out.println(poly4);

        System.out.println("Ligation Product: 2");
        System.out.println(pdt2);

        assert (pdt2.getSequence().equals("ccccccGGATCCaaaaaa"));
        assert (pdt2.getExt3().equals("c"));
        assert (pdt2.getMod5().equals(Modifications.hydroxyl));
        assert (pdt2.getMod3().equals(Modifications.phos5));

        //Just 5' phosphate 
        //No remaining phosphates
        Polynucleotide poly5 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        Polynucleotide poly6 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags3 = new ArrayList<>();
        frags3.add(poly5);
        frags3.add(poly6);

        Polynucleotide pdt3 = lig.run(frags3);

        System.out.println("poly5");
        System.out.println(poly5);

        System.out.println("poly6");
        System.out.println(poly6);

        System.out.println("Ligation Product: 3");
        System.out.println(pdt3);

        assert (pdt3.getSequence().equals("ccccccGGATCCaaaaaa"));
        assert (pdt3.getExt3().equals("c"));
        assert (pdt3.getMod5().equals(Modifications.hydroxyl));
        assert (pdt3.getMod3().equals(Modifications.hydroxyl));

    }

    @Test(timeout = 3000)
    public void testDigest_modcombinations_3to5() throws Exception {
        LigateSimulator lig = new LigateSimulator();
        lig.initiate();

        ComparePolynucleotides cps = new ComparePolynucleotides();
        cps.initiate();

        System.out.println("Two BamHI-digested DNAs examples: 3 to 5");
        String BamHIExt = "GATC";

        //3' p1 onto 5'p2
        //Final product only has 5' phosphate
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", "", BamHIExt, true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", BamHIExt, "c", true, false, false, Modifications.phos5, Modifications.hydroxyl);

        List<Polynucleotide> frags1 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        Polynucleotide pdt1 = lig.run(frags1);

        System.out.println("poly1");
        System.out.println(poly1);

        System.out.println("poly2");
        System.out.println(poly2);

        System.out.println("Ligation Product: 1");
        System.out.println(pdt1);

        assert (pdt1.getSequence().equals("CaaaaaaGATCccccccG"));
        assert (pdt1.getExt3().equals("c"));
        assert (pdt1.getMod5().equals(Modifications.phos5));
        assert (pdt1.getMod3().equals(Modifications.hydroxyl));

        //Final product only has 3' phosphate
        Polynucleotide poly3 = new Polynucleotide("Caaaaaa", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly4 = new Polynucleotide("ccccccG", BamHIExt, "c", true, false, false, Modifications.hydroxyl, Modifications.phos5);

        List<Polynucleotide> frags2 = new ArrayList<>();
        frags2.add(poly3);
        frags2.add(poly4);

        Polynucleotide pdt2 = lig.run(frags2);

        System.out.println("poly3");
        System.out.println(poly3);

        System.out.println("poly4");
        System.out.println(poly4);

        System.out.println("Ligation Product: 2");
        System.out.println(pdt2);

        assert (pdt2.getSequence().equals("CaaaaaaGATCccccccG"));
        assert (pdt2.getExt3().equals("c"));
        assert (pdt2.getMod5().equals(Modifications.hydroxyl));
        assert (pdt2.getMod3().equals(Modifications.phos5));

        //Final product has no phosphates
        Polynucleotide poly5 = new Polynucleotide("Caaaaaa", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly6 = new Polynucleotide("ccccccG", BamHIExt, "c", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags3 = new ArrayList<>();
        frags3.add(poly5);
        frags3.add(poly6);

        Polynucleotide pdt3 = lig.run(frags3);

        System.out.println("poly5");
        System.out.println(poly5);

        System.out.println("poly6");
        System.out.println(poly6);

        System.out.println("Ligation Product: 3");
        System.out.println(pdt3);

        assert (pdt3.getSequence().equals("CaaaaaaGATCccccccG"));
        assert (pdt3.getExt3().equals("c"));
        assert (pdt3.getMod5().equals(Modifications.hydroxyl));
        assert (pdt3.getMod3().equals(Modifications.hydroxyl));

    }

    @Test(timeout = 3000)
    public void testDigest_modcombinations_5to3_rc() throws Exception {

        System.out.println("Two BamHI-digested DNAs in other orientation:5 to 3");
        String BamHIExt = "GATC";

        //5' p1 onto 3'p2
        //No phosphate on 3' side
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", "c", BamHIExt, true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.phos5);

        List<Polynucleotide> frags1 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        Polynucleotide pdt1 = lig.run(frags1);

        System.out.println("poly1");
        System.out.println(poly1);

        System.out.println("poly2");
        System.out.println(poly2);

        System.out.println("Ligation Product: 1");
        System.out.println(pdt1);

        assert (pdt1.getSequence().equals("CaaaaaaGATCCgggggg"));
        assert (pdt1.getExt5().equals("c"));
        assert (pdt1.getMod5().equals(Modifications.phos5));
        assert (pdt1.getMod3().equals(Modifications.hydroxyl));

        //5 to 3
        //No phosphate on 5' side
        Polynucleotide poly3 = new Polynucleotide("Caaaaaa", "c", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        Polynucleotide poly4 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.phos5, Modifications.phos5);

        List<Polynucleotide> frags2 = new ArrayList<>();
        frags2.add(poly3);
        frags2.add(poly4);

        Polynucleotide pdt2 = lig.run(frags2);

        System.out.println("poly3");
        System.out.println(poly3);

        System.out.println("poly4");
        System.out.println(poly4);

        System.out.println("Ligation Product: 2");
        System.out.println(pdt2);

        assert (pdt2.getSequence().equals("CaaaaaaGATCCgggggg"));
        assert (pdt2.getExt5().equals("c"));
        assert (pdt2.getMod5().equals(Modifications.hydroxyl));
        assert (pdt2.getMod3().equals(Modifications.phos5));

        //No phosphate on either side
        Polynucleotide poly5 = new Polynucleotide("Caaaaaa", "c", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly6 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags3 = new ArrayList<>();
        frags3.add(poly5);
        frags3.add(poly6);

        Polynucleotide pdt3 = lig.run(frags3);

        System.out.println("poly5");
        System.out.println(poly5);

        System.out.println("poly6");
        System.out.println(poly6);

        System.out.println("Ligation Product: 3");
        System.out.println(pdt3);

        assert (pdt3.getSequence().equals("CaaaaaaGATCCgggggg"));
        assert (pdt3.getExt5().equals("c"));
        assert (pdt3.getMod5().equals(Modifications.hydroxyl));
        assert (pdt3.getMod3().equals(Modifications.hydroxyl));
    }

    @Test(timeout = 3000)
    public void testDigest_modcombinations_3to5_rc() throws Exception {

        System.out.println("Two BamHI-digested DNAs in other orientation: 3 to 5");
        String BamHIExt = "GATC";

        //3' p1 onto 5'p2
        //Only a phosphate on the 5' side
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", BamHIExt, "", true, false, false, Modifications.phos5, Modifications.phos5);

        List<Polynucleotide> frags1 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        System.out.println("poly1");
        System.out.println(poly1);

        System.out.println("poly2");
        System.out.println(poly2);

        Polynucleotide pdt1 = lig.run(frags1);

        System.out.println("Ligation Product: 1");
        System.out.println(pdt1);

        assert (pdt1.getSequence().equals("CggggggGATCCaaaaaa"));
        assert (pdt1.getExt3().equals("c"));
        assert (pdt1.getMod5().equals(Modifications.phos5));
        assert (pdt1.getMod3().equals(Modifications.hydroxyl));

        //5 to 3
        //only phosphate on the 5' side
        Polynucleotide poly3 = new Polynucleotide("Caaaaaa", BamHIExt, "", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly4 = new Polynucleotide("ccccccG", BamHIExt, "c", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags2 = new ArrayList<>();
        frags2.add(poly3);
        frags2.add(poly4);

        Polynucleotide pdt2 = lig.run(frags2);

        System.out.println("poly3");
        System.out.println(poly3);

        System.out.println("poly4");
        System.out.println(poly4);

        System.out.println("Ligation Product: 2");
        System.out.println(pdt2);

        assert (pdt2.getSequence().equals("CggggggGATCCaaaaaa"));
        assert (pdt2.getExt5().equals("g"));
        assert (pdt2.getMod5().equals(Modifications.hydroxyl));
        assert (pdt2.getMod3().equals(Modifications.phos5));

        //Just 5
        //No phosphates
        Polynucleotide poly5 = new Polynucleotide("Caaaaaa", BamHIExt, "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        Polynucleotide poly6 = new Polynucleotide("ccccccG", BamHIExt, "c", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags3 = new ArrayList<>();
        frags3.add(poly5);
        frags3.add(poly6);

        Polynucleotide pdt3 = lig.run(frags3);

        System.out.println("poly5");
        System.out.println(poly5);

        System.out.println("poly6");
        System.out.println(poly6);

        System.out.println("Ligation Product: 3");
        System.out.println(pdt3);

        assert (pdt3.getSequence().equals("CggggggGATCCaaaaaa"));
        assert (pdt3.getExt5().equals("g"));
        assert (pdt3.getMod5().equals(Modifications.hydroxyl));
        assert (pdt3.getMod3().equals(Modifications.hydroxyl));
    }

    @Test(timeout = 3000)
    public void testDigest_modcombinations_circular() throws Exception {

        System.out.println("Two BamHI-digested DNAs to make circular");
        String BamHIExt = "GATC";

        //circular example that works
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", BamHIExt, "", true, false, false, Modifications.phos5, Modifications.phos5);

        List<Polynucleotide> frags1 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        System.out.println("poly1");
        System.out.println(poly1);

        System.out.println("poly2");
        System.out.println(poly2);

        Polynucleotide pdt1 = lig.run(frags1);

        System.out.println("Circular Ligation Product: 1");
        System.out.println(pdt1);

        assert (pdt1.getSequence().equals("GATCCaaaaaaCgggggg"));
        assert (pdt1.getMod5().equals(Modifications.circular));
        assert (pdt1.getMod3().equals(Modifications.circular));

        //circular example that doesnt work: no phosphate
        Polynucleotide poly3 = new Polynucleotide("Caaaaaa", BamHIExt, "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        Polynucleotide poly4 = new Polynucleotide("ccccccG", BamHIExt, "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags2 = new ArrayList<>();
        frags2.add(poly3);
        frags2.add(poly4);

        System.out.println("poly3");
        System.out.println(poly3);

        System.out.println("poly4");
        System.out.println(poly4);

        Polynucleotide pdt2 = lig.run(frags2);

        if (pdt2.isCircular()) {
            System.out.println("created a circular polynucleotide");
            System.out.println(pdt2);
        } else {
            System.out.println("Ligation Product: 2");
            System.out.println("Did not created a circular polynucleotide");
            System.out.println(pdt2);
        }

        assert (pdt2.getSequence().equals("CggggggGATCCaaaaaa"));
        assert (pdt2.getMod5().equals(Modifications.hydroxyl));
        assert (pdt2.getMod3().equals(Modifications.hydroxyl));

    }

    @Test(timeout = 3000)
    public void testDigest_modcombinations_error() throws Exception {
        LigateSimulator lig = new LigateSimulator();
        lig.initiate();

        ComparePolynucleotides cps = new ComparePolynucleotides();
        cps.initiate();

        System.out.println("Two BamHI-digested DNAs examples: ERROR");
        String BamHIExt = "GATC";

        //ERROR ONES
        //Just hydroxyls
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        List<Polynucleotide> frags1 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        System.out.println("poly1");
        System.out.println(poly1);

        System.out.println("poly2");
        System.out.println(poly2);

        try {
            Polynucleotide pdt1 = lig.run(frags1);
        } catch (Exception err) {
            System.out.println("(Expected exception thrown)\n All hydroxyl Modifications no ligation\n ");
        }

        //wrong phosphates
        Polynucleotide poly3 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly4 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.phos5, Modifications.hydroxyl);

        List<Polynucleotide> frags2 = new ArrayList<>();
        frags1.add(poly1);
        frags1.add(poly2);

        System.out.println("poly3");
        System.out.println(poly3);

        System.out.println("poly4");
        System.out.println(poly4);

        
        try {
            Polynucleotide pdt2 = lig.run(frags2);
        } catch (Exception err) {
            System.out.println("(Expected exception thrown)\n Wrong phosphates\n ");
        }

    }

}
