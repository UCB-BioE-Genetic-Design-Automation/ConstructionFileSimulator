package org.ucb.c5.constructionfile.simulators;



import java.util.ArrayList;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Polynucleotide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests of PCRSimulator using PCR objects
 *
 * @author J. Christopher Anderson
 */
public class PCRSimulatorTest2 {

    @Test
    public void testSimplePCR() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq.toUpperCase(), polys.get(product).getSequence().toUpperCase());
    }

    @Test
    public void testOrdinaryPCR() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "ccataggatccgaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "cagtggaattcgtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "ccataggatccgaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatacgaattccactg";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq.toUpperCase(), polys.get(product).getSequence().toUpperCase());
    }

    @Test
    ////product seq is expected seq, however the simlator seq contains a large insertion. we need to dedug the simulator
    public void testDistinguishes() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "cagtggaattcgtatataaacgcagaaaggcc";
//"gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagagccagagacgcttctagagtcccaaagaattcgcggccgcttctagagtcccaaagatcatacgcgtcagcttctagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
//"aaagaattcgcggacgcttctagagtcccaaagaattcgcggccgcttctagagtcccaaagaattcgcggcagcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtcccaaagatcatacgcgtcagcttctagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatacgaattccactg";
        // "gaattcgcggccgcttctagagtcccaaagaattcgcggcagcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        String pseq = productSeq.toUpperCase();
        String simseq = polys.get(product).getSequence().toUpperCase();
        System.out.println(simseq);
        assertEquals(pseq, simseq);
    }
//product seq is expected seq, however the simlator seq contains a large insertion. we need to dedug the simulator

    @Test
    public void testRespectsSixBases() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
//"gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtcccttcttgatcagtgatagagattgtagacatcctagctatcagttctactgatagagatactgagcactaccatagagaaagaggagaaatactgggagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttcccccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtaagctagtgtagatcgctactcggagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactttagagtcacactggctcaccttcgggtgggcctttctgcgtttatatacaaaatagtagcggccgctgcagaaa";
//"aaagaattcgcggccgcttcaagagtcccaaagaattcgaggccgcttctagagtcccaaagaattcgcggccgcttcaagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtcccttcttgatcagtgatagagattgtagacatcctagctatcagttctactgatagagatactgagcactaccatagagaaagaggagaaatactgggagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttcccccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtaagctagtgtagatcgctactcggagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactttagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        //"gaattcgcggccgcttctagagtcccaaagaattcgcggccgcttcaagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        String pseq = productSeq.toUpperCase();
        String simseq = polys.get(product).getSequence().toUpperCase();
        assertEquals(pseq, simseq);
    }

    @Test
    public void testBubble() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcgatagccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcgatagccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        String pseq = productSeq.toUpperCase();
        String simseq = polys.get(product).getSequence().toUpperCase();
        assertEquals(pseq, simseq);
//        assertEquals(productSeq, polys.get(product).getSequence());
    }

    @Test
    public void testCircular() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "gaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaaaaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttc";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        String pseq = productSeq.toUpperCase();
        String simseq = polys.get(product).getSequence().toUpperCase();
        assertEquals(pseq, simseq);
    }

    @Test
    public void testIndependent() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "GTATATAAACGCAGAAAGGCC";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq.toUpperCase(), polys.get(product).getSequence().toUpperCase());
    }

    @Test
    public void testRevcomp() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "ggcctttctgcgtttatatac";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "?";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
//        assertThrows(Exception.class, () -> {pcrSimulator.run(pcr, polys);});
    }

    @Test
    public void testReversed() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "ccggaaagacgcaaatatatg";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "?";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
//        assertThrows(Exception.class, () -> {pcrSimulator.run(pcr, polys);});
    }

    @Test
    public void testSixBP() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttatag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "?";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
//        assertThrows(Exception.class, () -> {pcrSimulator.run(pcr, polys);});
    }

    @Test
    public void testIllegalArgument() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttatag";
        String oligo2 = "oligo2";
        String oligo2Seq = "ca998";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "?";
        List<String> templates = new ArrayList<>();
        templates.add(template);
        PCR pcr = new PCR(oligo1, oligo2, templates, product);
        Map<String, String> fragments = new HashMap<>();
        fragments.put(oligo1, oligo1Seq);
        fragments.put(oligo2, oligo2Seq);
        fragments.put(template, templateSeq);
        fragments.put(product, productSeq);
        Map<String, Polynucleotide> polys = new HashMap<>();
        for (String fragment : fragments.keySet()) {
            polys.put(fragment, new Polynucleotide(fragments.get(fragment)));
        }
        PCRSimulator pcrSimulator = new PCRSimulator();
        pcrSimulator.initiate();
//        assertThrows(Exception.class, () -> {pcrSimulator.run(pcr, polys);});
    }
}
