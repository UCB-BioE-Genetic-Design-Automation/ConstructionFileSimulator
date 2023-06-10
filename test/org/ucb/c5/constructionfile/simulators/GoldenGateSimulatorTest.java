package org.ucb.c5.constructionfile.simulators;

import org.junit.Before;
import org.junit.Test;
import org.ucb.c5.constructionfile.model.Polynucleotide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ucb.c5.constructionfile.model.Assembly;
import org.ucb.c5.sequtils.ComparePolynucleotides;

/**
 *
 * @author michaelfernandez
 * @author jcaucb
 */
public class GoldenGateSimulatorTest {

    ComparePolynucleotides cps;

    @Before
    public void initialize() throws Exception {
        cps = new ComparePolynucleotides();
        cps.initiate();
    }

    @Test
    public void GoldenGateSimulatorTest() throws Exception {
        //From PCR_simulator object
        AssemblySimulator assemblySimulator = new AssemblySimulator();
        assemblySimulator.initiate();

        Polynucleotide frag1 = new Polynucleotide("CCATAGGTCTCGATCGTTGCAGGACCACTTCTGCGCTCGGCCCTTCCGGCTGCGAGACCTTTGG");
        Polynucleotide frag2 = new Polynucleotide("CCATAGGTCTCGGCTGGTTTATTGAACTACTTACTCTAGCATCGCGAGACCTTTGG");

        List<String> assemblyFragments = new ArrayList<>();
        assemblyFragments.add("frag1");
        assemblyFragments.add("frag2");

        Assembly assembly = new Assembly(assemblyFragments, "BsaI", "PDT");

        Map<String, Polynucleotide> fragments = new HashMap<>();
        fragments.put("frag1", frag1);
        fragments.put("frag2", frag2);

        //Run the assembly simulator
        Polynucleotide assemblyProduct = assemblySimulator.run(assembly, fragments);

        //Confirm that the two DNAs are identical
        Polynucleotide expected = new Polynucleotide("GCTGGTTTATTGAACTACTTACTCTAGCATCGTTGCAGGACCACTTCTGCGCTCGGCCCTTCCG", true);
        assert (cps.run(expected, assemblyProduct));
    }
    
    @Test
    /**
     * Tests if it rejects a scenario where a fragment anneals to itself
     */
    public void GoldenGateSelfLigation() throws Exception {
        //From PCR_simulator object
        AssemblySimulator assemblySimulator = new AssemblySimulator();
        assemblySimulator.initiate();

        //A self-ligating DNA
        Polynucleotide frag1 = new Polynucleotide("CCATAGGTCTCGATCGTTGCAGGACCACTTCTGCGCTCGGCCCTTCCGATCGCGAGACCTTTGG");
        Polynucleotide frag2 = new Polynucleotide("CCATAGGTCTCGGCTGGTTTATTGAACTACTTACTCTAGCATCGCGAGACCTTTGG");

        List<String> assemblyFragments = new ArrayList<>();
        assemblyFragments.add("frag1");
        assemblyFragments.add("frag2");

        Assembly assembly = new Assembly(assemblyFragments, "BsaI", "PDT");

        Map<String, Polynucleotide> fragments = new HashMap<>();
        fragments.put("frag1", frag1);
        fragments.put("frag2", frag2);

        //Run the assembly simulator
        try {
            Polynucleotide assemblyProduct = assemblySimulator.run(assembly, fragments);
            assert(false);
        } catch(Exception err) {
            assert(true);
        }
    }
}
