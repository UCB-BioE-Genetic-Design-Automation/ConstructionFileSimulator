package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Polynucleotide;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class PCRSimulatorTest {
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
    }

    @Test
    public void testDistinguishes() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggacgcttctagagtcccaaagaattcgcggccgcttctagagtcccaaagaattcgcggcagcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtcccaaagaattcgcggcagcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
    }

    @Test
    public void testRespectsSixBases() throws Exception {
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "gtatataaacgcagaaaggcc";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttcaagagtcccaaagaattcgaggccgcttctagagtcccaaagaattcgcggccgcttcaagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "gaattcgcggccgcttctagagtcccaaagaattcgcggccgcttcaagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatac";
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        pcrSimulator.run(pcr, polys);
        assertEquals(productSeq, polys.get(product).getSequence());
    }

    @Test
    public void testRevcomp() throws Exception{
        String oligo1 = "oligo1";
        String oligo1Seq = "gaattcgcggccgcttctag";
        String oligo2 = "oligo2";
        String oligo2Seq = "ggcctttctgcgtttatatac";
        String template = "template";
        String templateSeq = "aaagaattcgcggccgcttctagagtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcactactagagaaagaggagaaatactagatggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataacgctgatagtgctagtgtagatcgctactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttatatactagtagcggccgctgcagaaa";
        String product = "product";
        String productSeq = "?";
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
        PCR pcr = new PCR(oligo1, oligo2, template, product);
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
//        assertThrows(Exception.class, () -> {pcrSimulator.run(pcr, polys);});
    }
}