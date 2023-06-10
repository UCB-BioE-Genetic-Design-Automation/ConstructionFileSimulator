/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.ucb.c5.constructionfile.model.Modifications;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.ComparePolynucleotides;

/**
 *
 * @author michaelfernandez
 * @author jcaucb
 */
public class LigateSimulatorTest {

    ComparePolynucleotides cps;
    LigateSimulator lig;
    String BamHIExt = "GATC";

    @Before
    public void initialize() throws Exception {
        cps = new ComparePolynucleotides();
        cps.initiate();
        lig = new LigateSimulator();
        lig.initiate();
    }

    @Test
    public void testLigation_single_fragment() throws Exception {
        //Circularization of a single fragment with 5' phosphates on both ends
        Polynucleotide poly = new Polynucleotide("CCCCCCCCCCCC", "GGGG", "GGGG", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("GGGGCCCCCCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_single_fragment_lefty_phosphate() throws Exception {
        //Circularization of a single fragment with a 5' phosphates on lefty end
        Polynucleotide poly = new Polynucleotide("CCCCCCCCCCCC", "GGGG", "GGGG", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("GGGGCCCCCCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_single_fragment_righty_phosphate() throws Exception {
        //Circularization of a single fragment with a 5' phosphates on righty end
        Polynucleotide poly = new Polynucleotide("CCCCCCCCCCCC", "GGGG", "GGGG", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("GGGGCCCCCCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);

        assert (cps.run(pdt, expected));
    }

    /**
     * Circularization of a single fragment with a 5' phosphates on righty end
     * An exception test since no ligation should occur
     */
    @Test
    public void testLigation_single_fragment_no_phosphate() throws Exception {
        Polynucleotide poly = new Polynucleotide("CCCCCCCCCCCC", "GGGG", "GGGG", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly);

        try {
            Polynucleotide pdt = lig.run(frags);
            assert (false);
        } catch (Exception err) {
            assert (true);
        }
    }

    @Test
    public void testLigation_two_frags_GATC_linear_twophosphates() throws Exception {
        //Phosphate on both strands of the ligation junction
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "GATC", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "GATC", "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAAGATCCCCCCCC", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_two_frags_GATC_linear_leftyphosphate() throws Exception {
        //Only one phosphate on the lefty side
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "GATC", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "GATC", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAAGATCCCCCCCC", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    @Test

    public void testLigation_two_frags_GATC_linear_rightyphosphate() throws Exception {
        //Only one phosphate on the righty side
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "GATC", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "GATC", "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAAGATCCCCCCCC", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    /**
     * Joining two sticky ends that lack 5' phosphates. An exception test since
     * no ligation should occur
     */
    @Test
    public void testLigation_two_frags_GATC_linear_nophosphate() throws Exception {
        //Only one phosphate on the righty side
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "GATC", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "GATC", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        try {
            Polynucleotide pdt = lig.run(frags);
            assert (false);
        } catch (Exception err) {
            assert (true);
        }
    }

    @Test
    public void testLigation_two_frags_nonpalindromic_fiveprime_sticky() throws Exception {
        //Joining a 5' non-palindromic sticky end
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "CAAA", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "CAAA", "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAACAAACCCCCCC", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_two_frags_nonpalindromic_threeprime_sticky() throws Exception {
        //Joining 3' nonpalindromic sticky ends
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "-CAAA", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "-CAAA", "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAACAAACCCCCCC", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_two_frags_threeprime_sticky() throws Exception {
        //Joining 3' sticky ends
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "-GATC", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "-GATC", "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAAGATCCCCCCCC", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_two_frags_blunt_sticky() throws Exception {
        //Joining two blunt ends
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "c", "", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "", "a", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAACCCCCCC", "c", "a", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);

        assert (cps.run(pdt, expected));
    }

    /**
     * Joining two sticky ends that are not the same sticky end An exception
     * test since no ligation should occur
     */
    @Test
    public void testLigation_two_frags_mismatched_ends() throws Exception {
        //Only one phosphate on the righty side
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "CCCC", true, false, false, Modifications.hydroxyl, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "CACC", "", true, false, false, Modifications.phos5, Modifications.hydroxyl);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        try {
            Polynucleotide pdt = lig.run(frags);
            assert (false);
        } catch (Exception err) {
            assert (true);
        }
    }

    @Test
    public void testLigation_two_frags_circular_fiveprime() throws Exception {
        //Recircularizing over 5' extensions
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "GATC", "AATT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "AATT", "GATC", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("GATCAAAAAAAAATTCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);

        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_two_frags_circular_threeprime() throws Exception {
        //Recircularizing over 3' extensions
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "-GATC", "AATT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "AATT", "-GATC", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("GATCAAAAAAAAATTCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);
    
        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_two_frags_circular_blunt() throws Exception {
        //Recircularizing over blunt ends
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "", "AATT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "AATT", "", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("AAAAAAAAATTCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);
    
        assert (cps.run(pdt, expected));
    }

    /**
     * Two fragments, one of which can self-circularize An exception test since
     * ambiugous products would occur
     */
    @Test
    public void testLigation_two_frags_self_circularization() throws Exception {
        Polynucleotide poly1 = new Polynucleotide("AAAAAAA", "CCCC", "CCCC", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCCCC", "CCCC", "GATC", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);

        try {
            Polynucleotide pdt = lig.run(frags);
            assert (false);
        } catch (Exception err) {
            assert (true);
        }
    }

    @Test
    public void testLigation_three_frags() throws Exception {
        //Three non-palindromic fragments ligate to a circle
        Polynucleotide poly1 = new Polynucleotide("AAAAA", "ATTT", "CTTT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCC", "CTTT", "GTTT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly3 = new Polynucleotide("GGGGG", "GTTT", "ATTT", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);
        frags.add(poly3);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("ATTTAAAAACTTTCCCCCGTTTGGGGG", "", "", true, false, true, Modifications.circular, Modifications.circular);
    
        assert (cps.run(pdt, expected));
    }

    @Test
    public void testLigation_three_frags_rearranged() throws Exception {
        //Three non-palindromic fragments ligate to a circle, out of order
        Polynucleotide poly1 = new Polynucleotide("AAAAA", "ATTT", "CTTT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly2 = new Polynucleotide("CCCCC", "CTTT", "GTTT", true, false, false, Modifications.phos5, Modifications.phos5);
        Polynucleotide poly3 = new Polynucleotide("GGGGG", "GTTT", "ATTT", true, false, false, Modifications.phos5, Modifications.phos5);
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly3);  //rearranged here
        frags.add(poly2);

        Polynucleotide pdt = lig.run(frags);
        Polynucleotide expected = new Polynucleotide("ATTTAAAAACTTTCCCCCGTTTGGGGG", "", "", true, false, true, Modifications.circular, Modifications.circular);
    
        assert (cps.run(pdt, expected));
    }
}
