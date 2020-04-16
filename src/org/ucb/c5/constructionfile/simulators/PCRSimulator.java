package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.CalcEditDistance;
import org.ucb.c5.genbank.model.RevComp;
import org.ucb.c5.sequtils.StringRotater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.ucb.c5.utils.Log;

public class PCRSimulator {
    /*
     PF2a	BR2a
     BF2a	PR2a
     ok
	
     PF3a	BR3a
     BF3a	PR3a
	
     PF4a	BR4a
     BF4a	PR4a
	
     PF5a	BR5a
     BF5a	PR5a
	
     PF6a	BR6a
     BF6a	PR6a
     */

    public static void main(String[] args) throws Exception {
        String template = "ggcagttattggtgcctagaaatattttatctgattaataagatgatcttcttgagatcgttttggtctgcgcgtaatctcttgctctgaaaacgaaaaaaccgccttgcagggcggtttttcgaaggttctctgagctaccaactctttgaaccgaggtaactggcttggaggagcgcagtcaccaaaacttgtcctttcagtttagccttaaccggcgcatgacttcaagactaactcctctaaatcaattaccagtggctgctgccagtggtgcttttgcatgtctttccgggttggactcaagacgatagttaccggataaggcgcagcggtcggactgaacggggggttcgtgcatacagtccagcttggagcgaactgcctacccggaactgagtgtcaggcgtggaatgagacaaacgcggccataacagcggaatgacaccggtaaaccgaaaggcaggaacaggagagcgcacgagggagccgccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccaccactgatttgagcgtcagatttcgtgatgcttgtcaggggggcggagcctatggaaaaacggctttgccgcggccctctcacttccctgttaagtatcttcctggcatcttccaggaaatctccgccccgttcgtaagccatttccgctcgccgcagtcgaacgaccgagcgtagcgagtcagtgagcgaggaagcggaatatatcctgtatcacatattctgctgacgcaccggtgcagccttttttctcctgccacatgaagcacttcactgacaccctcatcagtgcCAACATAGTAAGCCAGTATACACTCCGCTACAGCCACGTATCGCCAGATGTTCCAGACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACATTTTTGCTTCCGTGTGGTATTATGGGAGCAGTAGGTCTACGGTTAACTGATACTAAAAGACAATTCAGCGGGTAACCTTGCAATGGTGAGTGGCAGTAAAGCGGGCGTTTCGCCTCATCGCGAAATAGAAGTAATGAGACAATCCATTGACGATCACCTGGCTGGCCTGTTACCTGAAACCGACAGCCAGGATATCGTCAGCCTTGCGATGCGTGAAGGCGTCATGGCACCCGGTAAACGGATCCGTCCGCTGCTGATGCTGCTGGCCGCCCGCGACCTCCGCTACCAGGGCAGTATGCCTACGCTGCTCGATCTCGCCTGCGCCGTTGAACTGACCCATACCGCGTCGCTGATGCTCGACGACATGCCCTGCATGGACAACGCCGAGCTGCGCCGCGGTCAGCCCACTACCCACAAAAAATTTGGTGAGAGCGTGGCGATCCTTGCCTCCGTTGGGCTGCTCTCTAAAGCCTTTGGTCTGATCGCCGCCACCGGCGATCTGCCGGGGGAGAGGCGTGCCCAGGCGGTCAACGAGCTCTCTACCGCCGTGGGCGTGCAGGGCCTGGTACTGGGGCAGTTTCGCGATCTTAACGATGCCGCCCTCGACCGTACCCCTGACGCTATCCTCAGCACCAACCACCTCAAGACCGGCATTCTGTTCAGCGCGATGCTGCAGATCGTCGCCATTGCTTCCGCCTCGTCGCCGAGCACGCGAGAGACGCTGCacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtcaccggtcatgaaaatcgcctgataccgcccttttgggttcaagcagtacataacgatggaaccacattacaggagtagtgatgaatgaaggacgagcgccttgttcagcgtaagaacgatcatctggatatcgttctcgacccccgtcgcgccgtaactcaggctagcgcaggttttgagcgctggcgctttacccactgcgccctgccagagctgaattttagcgacatcacgctggaaaccaccttcctgaatcggcagctacaggctccgctgctgatcagctccatgaccggcggcgttgagcgctcgcgccatatcaaccgccacctcgccgaggcggcgcaggtgctaaaaattgcgatgggggtgggctcccagcgcgtcgccattgagagcgacgcgggcttagggctggataaaaccctgcggcagctggctccggacgtgccgctgctggcgaacctcggcgcggcgcagctgaccggcagaaaaggtattgattacgcccgacgggccgtggagatgatcgaggcggatgcgctgattgtgcacctaaacccgctgcaggaggcgctacagcccggcggcgatcgcgactggcgcggacggctggcggctattgaaactctggtccgcgagctgcccgttccgctggtggtgaaagaggtgggagccggtatctcccgaaccgtggccgggcagctgatcgatgccggcgttaccgtgattgacgtcgcgggcgcgggcggcaccagctgggccgccgttgaaggcgagcgggcggccaccgagcagcagcgcagcgtggccaacgtctttgccgactgggggatccccaccgctgaggcgctggttgacattgccgaggcctggccgcagatgccccttattgcctcgggaatgcgggctgggctatttcaccctaccactggctattcgctgccgctggcggtggcccttgccgacgcgattgccgacagcccgcggctgggcagcgttccgctctatcagctcacccggcagtttgccgaacgccactggcgcaggcagggattcttccgcctgctgaaccggatgcttttcctggccgggcgcgaggagaaccgctggcgggtgatgcagcgcttttatgggctgccggagcccaccgtagagcgcttttacgccggtcggctctctctctttgataaggcccgcattttgacgggcaagccaccggttccgctgggcgaagcctggcgggcggcgctgaaccattttcctgacagacgagataaaggatgaaaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccggcaggcgcatggaggattacgtcaggctgctgccggtaaaacccttctaccgactctgctgggagtccgggaagaccctcgactatgctaacgacagcgccgagcttgaggcgcagattacccagttcaacccccgcgacgtcgagggctaccggcgctttctggcttactcccaggcggtattccaggagggatatttgcgcctcggcagcgtgccgttcctctcttttcgcgacatgctgcgcgccgggccgcagctgcttaagctccaggcgtggcagagcgtctaccagtcggtttcgcgctttattgaggatgagcatctgcggcaggccttctcgttccactccctgctggtaggcggcaaccccttcaccacctcgtccatctacaccctgatccacgcccttgagcgggagtggggggtctggttccctgagggcggcaccggggcgctggtgaacggcatggtgaagctgtttaccgatctgggcggggagatcgaactcaacgcccgggtcgaagagctggtggtggccgataaccgcgtaagccaggtccggctggcggatggtcggatctttgacaccgacgccgtagcctcgaacgctgacgtggtgaacacctataaaaagctgctcggccaccatccggtggggcagaagcgggcggcagcgctggagcgcaagagcatgagcaactcgctgtttgtgctctacttcggcctgaaccagcctcattcccagctggcgcaccataccatctgttttggtccccgctaccgggagctgatcgacgagatctttaccggcagcgcgctggcggatgacttctcgctctacctgcactcgccctgcgtgaccgatccctcgctcgcgcctcccggctgcgccagcttctacgtgctggccccggtgccgcatcttggcaacgcgccgctggactgggcgcaggaggggccgaagctgcgcgaccgcatctttgactaccttgaagagcgctatatgcccggcctgcgtagccagctggtgacccagcggatctttaccccggcagacttccacgacacgctggatgcgcatctgggatcggccttctccatcgagccgctgctgacccaaagcgcctggttccgcccgcacaaccgcgacagcgacattgccaacctctacctggtgggcgcaggtactcaccctggggcgggcattcctggcgtagtggcctcggcgaaagccaccgccagcctgatgattgaggatctgcaatgagccaaccgccgctgcttgaccacgccacgcagaccatggccaacggctcgaaaagttttgccaccgctgcgaagctgttcgacccggccacccgccgtagcgtgctgatgctctacacctggtgccgccactgcgatgacgtcattgacgaccagacccacggcttcgccagcgaggccgcggcggaggaggaggccacccagcgcctggcccggctgcgcacgctgaccctggcggcgtttgaaggggccgagatgcaggatccggccttcgctgcctttcaggaggtggcgctgacccacggtattacgccccgcatggcgctcgatcacctcgacggctttgcgatggacgtggctcagacccgctatgtcacctttgaggatacgctgcgctactgctatcacgtggcgggcgtggtgggtctgatgatggccagggtgatgggcgtgcgggatgagcgggtgctggatcgcgcctgcgatctggggctggccttccagctgacgaatatcgcccgggatattattgacgatgcggctattgaccgctgctatctgcccgccgagtggctgcaggatgccgggctgaccccggagaactatgccgcgcgggagaatcgggccgcgctggcgcgggtggcggagcggcttattgatgccgcagagccgtactacatctcctcccaggccgggctacacgatctgccgccgcgctgcgcctgggcgatcgccaccgcccgcagcgtctaccgggagatcggtattaaggtaaaagcggcgggaggcagcgcctgggatcgccgccagcacaccagcaaaggtgaaaaaattgccatgctgatggcggcaccggggcaggttattcgggcgaagacgacgagggtgacgccgcgtccggccggtctttggcagcgtcccgtttaggcgggcggccatgacgttcacgcaggatcagtcgcctgtaggtcggcaggcttgggaagctgtggtatggctgtgcaggtcgtaaatcactgcataattcgtgtcgctcaaggcgcactcccgttctggataatgttttttgcgccgacatcataacggttctggcaaatattctgaaatgagctgttgacaattaatcatccggctcgtataatgtgtggaattgtgagcggataacaatttcacacaggaaacagcgccgctgagaaaaagcgaagcggcactgctctttaacaatttatcagacaatctgtgtgggcactcgaccggaattatcgattaactttattattaaaaattaaagaggtatatattaatgtatcgattaaataaggaggaataaaccatgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctgaacatctttttttaaagtttttaaatcaatcaactttctcttcatcatttttatcttttcgatgataataatttgggatatgtgagacacttacaaaacttccaaggtctgcggggcaaaacaatcgataaatcagcccgggaattaacatggcaaccactcatttggatgtttgcgccgtggttccggcggccggatttggccgtcgaatgcaaacggaatgtcctaagcaatatctctcaatcggtaatcaaaccattcttgaacactcggtgcatgcgctgctggcgcatccccgggtgaaacgtgtcgtcattgccataagtcctggcgatagccgttttgcacaacttcctctggcgaatcatccgcaaatcaccgttgtagatggcggtgatgagcgtgccgattccgtgctggcaggtctgaaagccgctggcgacgcgcagtgggtattggtgcatgacgccgctcgtccttgtttgcatcaggatgacctcgcgcgattgttggcgttgagcgaaaccagccgcacgggggggatcctcgccgcaccagtgcgcgatactatgaaacgtgccgaaccgggcaaaaatgccattgctcataccgttgatcgcaacggcttatggcacgcgctgacgccgcaatttttccctcgtgagctgttacatgactgtctgacgcgcgctctaaatgaaggcgcgactattaccgacgaagcctcggcgctggaatattgcggattccatcctcagttggtcgaaggccgtgcggataacattaaagtcacgcgcccggaagatttggcactggccgagttttacctcacccgaaccatccatcaggagaatacataatgcgaattggacacggttttgacgtacatgcctttggcggtgaaggcccaattatcattggtggcgtacgcattccttacgaaaaaggattgctggcgcattctgatggcgacgtggcgctccatgcgttgaccgatgcattgcttggcgcggcggcgctgggggatatcggcaagctgttcccggataccgatccggcatttaaaggtgccgatagccgcgagctgctacgcgaagcctggcgtcgtattcaggcgaagggttatacccttggcaacgtcgatgtcactatcatcgctcaggcaccgaagatgttgccgcacattccacaaatgcgcgtgtttattgccgaagatctcggctgccatatggatgatgttaacgtgaaagccactactacggaaaaactgggatttaccggacgtggggaagggattgcctgtgaagcggtggcgctactcattaaggcaacaaaatgattgagtttgataatctcacttacctccacggtaaaccgcacctcaggcaataataaagtttgcggccgcgaattcctgcagcccgggggatccactagttctagagcggccgccaccgcggtggagctccagcttttgttccctttagtgagggttaatttcgagcttggcgtaatcatggtcatagctgtttcctgtgtggtggtagatcctctacgccggacgcatcgtggccggcatcaccggcgccacaggtgcggttgctggcgcctatatcgccgacatcacccagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaatttttttaa";

        PCR pcr = new PCR("BF6a", "PR6a", "template", "pcrpdt");

        PCRSimulator sim = new PCRSimulator();
        sim.initiate();

        Map<String, Polynucleotide> frags = new HashMap<>();
        frags.put("template", new Polynucleotide(template));

        frags.put("PF2a", new Polynucleotide("ccataGGTCTCTgtagcataacgatggaaccacattacag"));
        frags.put("PF3a", new Polynucleotide("ccataGGTCTCTgtagccgcgcttgaggcgctgttc"));
        frags.put("PF4a", new Polynucleotide("ccataGGTCTCTgtagccgccgctgcttgaccacgc"));
        frags.put("PF5a", new Polynucleotide("ccataGGTCTCTgtaggaggtatatattaatgtatcg"));
        frags.put("PF6a", new Polynucleotide("ccataGGTCTCTgtaggtctgcggggcaaaacaatcg"));

        frags.put("PR2a", new Polynucleotide("cagttGGTCTCActggctgcttgaacccaaaagggcgg"));
        frags.put("PR3a", new Polynucleotide("cagttGGTCTCActgggcctgcgccaatcacaacgg"));
        frags.put("PR4a", new Polynucleotide("cagttGGTCTCActggggttggctcattgcagatcc"));
        frags.put("PR5a", new Polynucleotide("cagttGGTCTCActgggcgtgaacgtcatggccgcc"));
        frags.put("PR6a", new Polynucleotide("cagttGGTCTCActgggatgttcagagtttgtggatg"));

        frags.put("BF2a", new Polynucleotide("ccataGGTCTCTgatggcaaatggggagagcatg"));
        frags.put("BF3a", new Polynucleotide("ccataGGTCTCTgatgttgccgcacattccac"));
        frags.put("BF4a", new Polynucleotide("ccataGGTCTCTgatggtaagccctcccgtatcgt"));
        frags.put("BF5a", new Polynucleotide("ccataGGTCTCTGATGCGTGAAGGCGTCATGGC"));
        frags.put("BF6a", new Polynucleotide("ccataGGTCTCTgatgggggtgggctcccagcg"));
        frags.put("BR2a", new Polynucleotide("cagttGGTCTCAcatcagaaggagccttgtacag"));
        frags.put("BR3a", new Polynucleotide("cagttGGTCTCAcatcttcggtgcctgagcgatg"));
        frags.put("BR4a", new Polynucleotide("cagttGGTCTCAcatctggccccagtgctgcaatg"));
        frags.put("BR5a", new Polynucleotide("cagttGGTCTCACATCGCAAGGCTGACGATATCC"));
        frags.put("BR6a", new Polynucleotide("cagttGGTCTCAcatcgcaatttttagcacctgc"));

        sim.run(pcr, frags);

        Polynucleotide pdt = frags.get("pcrpdt");
        System.out.println(pdt.getSequence());
    }

    public void initiate() {
    }

    public void run(PCR pcr, Map<String, Polynucleotide> fragments) throws Exception {
        
        String oligo1Seq = null;
        try {
            oligo1Seq = fragments.get(pcr.getOligo1()).getSequence().toLowerCase(); // Check local Map for oligo1
        } catch (Exception err) {
            Log.severe("Sequence " + pcr.getOligo1() + " is missing");
            throw err;
        }

        String oligo2Seq = null;
        try {
            oligo2Seq = fragments.get(pcr.getOligo2()).getSequence().toLowerCase(); // Check local Map for oligo2
        } catch (Exception err) {
            Log.severe("Sequence " + pcr.getOligo2() + " is missing");
            throw err;
        }

        String templateSeq = null;
        try {
            templateSeq = fragments.get(pcr.getTemplate()).getSequence().toLowerCase(); // Check local Map for template sequence
        } catch (Exception err) {
            Log.severe("Sequence " + pcr.getTemplate() + " is missing");
            throw err;
        }

        try {
            String productSeq = fragments.get(pcr.getProduct()).getSequence().toLowerCase(); // Check local Map for product (probably not given in CF)
        } catch (Exception NullPointerException) {
            String productSeq = null;
        }
        // Check validity of sequences
        validateSequence(oligo1Seq);
        validateSequence(oligo2Seq);
        validateSequence(templateSeq);

        // Create util objects
        RevComp revcomp = new RevComp();
        revcomp.initiate();
        StringRotater stringRotater = new StringRotater();
        CalcEditDistance calcEditDistance = new CalcEditDistance();
        // Find indices of sequences starting with the last 6 bases 5' to 3' of oligo1 (or the first 6 bases 3' to 5' rev comp of the sequence)
        String lastSixOligo1Seq = oligo1Seq.substring(oligo1Seq.length() - 6); // Last six bases of 5' to 3' oligo1
        String revFirstSixOligo1Seq = revcomp.run(oligo1Seq).substring(0, 6); // First six bases of 3' to 5' revcompOligo1
        ArrayList<Integer> forwardIndicesOligo1 = new ArrayList<>(); // Last indices of 5' to 3' matching sequences
        ArrayList<Integer> revIndicesOligo1 = new ArrayList<>(); // First indices of 3' to 5' reverse complement matching sequences
        for (int i = -1; (i = templateSeq.indexOf(lastSixOligo1Seq, i + 1)) != -1; i++) {
            forwardIndicesOligo1.add(i + 6); // If the six bp matching sequence is found starting at index i, add the index of the last 5' to 3' bp (i+6)
        }
        for (int i = -1; (i = templateSeq.indexOf(revFirstSixOligo1Seq, i + 1)) != -1; i++) {
            revIndicesOligo1.add(i); // If the six bp matching revcomp sequence is found starting at index i, add the index of the first 3' to 5' bp (i)
        }
        // Check that a 6 bp match was found
        if (forwardIndicesOligo1.size() == 0 && revIndicesOligo1.size() == 0) {
            throw new Exception("There was no matching 6 bp sequence for the provided oligo1");
        }

        // Sanity Check:
        // forwardIndicesOligo1 contains the indices (on the 5' to 3' template sequence) of the END of 5' to 3' sequences potentially matching oligo1
        // revIndicesOligo1 contains the indices (on the 5' to 3' template sequence) of the FIRST index of 3' to 5' sequences potentially matching revOligo1
        int lowestForwardEditDistanceOligo1SeqIndex = 0; // Will be assigned to the END index (on the 5' to 3' template seq) of the 5' to 3' sequence that best matches oligo1
        int lowestForwardEditDistanceOligo1 = Integer.MAX_VALUE;
        for (int i : forwardIndicesOligo1) {
            int offset = i - oligo1Seq.length(); // Offset assigned to the index of the FIRST index of the 5' to 3' sequence potentially matching oligo1
            String tempRotatedTemplateSeq = stringRotater.run(templateSeq, offset); // Rotate the string such that the first bp of the 5' to 3' matching seq is at index 0
            String potentialMatchSeq = tempRotatedTemplateSeq.substring(Math.max(0, oligo1Seq.length() - 25), oligo1Seq.length()); // Select the last 25 bases of the potential matching seq, or all bases if less than 25 bp long
            String annealingRegionOligo1 = oligo1Seq.substring(Math.max(0, oligo1Seq.length() - 25), oligo1Seq.length()); // Select the last 25 bases of oligo1, or all bases if less than 25 bp long
            int tempEditDistance = calcEditDistance.run(potentialMatchSeq, annealingRegionOligo1);
            if (tempEditDistance < lowestForwardEditDistanceOligo1) {
                lowestForwardEditDistanceOligo1SeqIndex = i;
                lowestForwardEditDistanceOligo1 = tempEditDistance;
            }
        }
        int lowestRevEditDistanceOligo1SeqIndex = 0; // Will be assigned to the FIRST index (on the 5' to 3' template seq) of the 3' to 5' sequence that best matches oligo1
        int lowestRevEditDistanceOligo1 = Integer.MAX_VALUE;
        for (int i : revIndicesOligo1) {
            String tempRotatedTemplateSeq = stringRotater.run(templateSeq, i); // Rotate such that the matching 3' to 5' seq head is at index 0
            String potentialMatchSeq = tempRotatedTemplateSeq.substring(Math.max(0, oligo1Seq.length() - 25), oligo1Seq.length()); // Select the last 25 bases, or all bases if less than 25 bp long
            String revPotentialMatchSeq = revcomp.run(potentialMatchSeq); // Reverse complement the 5' to 3' sequence to find the potentially matching 3' to 5' seq
            String revOligo1Seq = revcomp.run(oligo1Seq); // Reverse complement oligo1
            String revAnnealingRegionOligo1 = revOligo1Seq.substring(0, Math.min(25, revOligo1Seq.length())); // Select the last 25 bases of rev comp oligo1, or all bases if less than 25 bp long
            int tempEditDistance = calcEditDistance.run(revPotentialMatchSeq, revAnnealingRegionOligo1);

            if (tempEditDistance < lowestRevEditDistanceOligo1) {
                lowestRevEditDistanceOligo1SeqIndex = i;
                lowestRevEditDistanceOligo1 = tempEditDistance;
            }
        }

        // Sanity Check:
        // lowestForwardEditDistanceOligo1SeqIndex is now assigned to the END index (on the 5' to 3' template sequence) of the 5' to 3' sequence that has the best match with oligo1
        // lowestRevEditDistanceOligo1SeqIndex is now assigned to the FIRST index (on the 5' to 3' template sequence) of the 3' to 5' sequence that has the best match with oligo1
        boolean oligo1Forward = true;
        if (lowestForwardEditDistanceOligo1 < lowestRevEditDistanceOligo1) { // If the forward direction has a lower edit distance
            templateSeq = stringRotater.run(templateSeq, lowestForwardEditDistanceOligo1SeqIndex); // Rotate the template such that oligo1 is at the very end
        } else {
            templateSeq = stringRotater.run(templateSeq, lowestRevEditDistanceOligo1SeqIndex);  // Rotate the template such that revcomp of oligo1 ends at the first index
            templateSeq = revcomp.run(templateSeq); // Rev comp the entire template seq such that the revcomp of oligo1 starts at the very end
            oligo1Forward = false;
        }
        int endIndexOligo1 = 0;
        // Sanity Check:
        // After the above steps, the template sequence should now either end with oligo1 or with the reverse complement of oligo1,
        // and the end index of the oligo1 seq is saved as endIndexOligo1
        // Find indices of sequences starting with the first 6 bases of oligo2 (or the rev comp of the sequence)

        String lastSixOligo2Seq = oligo2Seq.substring(oligo2Seq.length() - 6); // Last six bases of 5' to 3' oligo2
        String revFirstSixOligo2Seq = revcomp.run(oligo2Seq).substring(0, 6);  // First six bases of 3' to 5' revcompOligo2
        ArrayList<Integer> forwardIndicesOligo2 = new ArrayList<>(); // Last indices of 5' to 3' matching sequences
        ArrayList<Integer> revIndicesOligo2 = new ArrayList<>(); // First indices of 3' to 5' reverse complement matching sequences
        for (int i = -1; (i = templateSeq.indexOf(lastSixOligo2Seq, i + 1)) != -1; i++) {
            forwardIndicesOligo2.add(i + 6); // If the six bp matching sequence is found starting at index i, add the index of the last 5' to 3' bp (i+6)
        }
        for (int i = -1; (i = templateSeq.indexOf(revFirstSixOligo2Seq, i + 1)) != -1; i++) {
            revIndicesOligo2.add(i); // If the six bp matching revcomp sequence is found starting at index i, add the index of the first 3' to 5' bp (i)
        }
        // Check that a 6 bp match was found
        if (forwardIndicesOligo2.size() == 0 && revIndicesOligo2.size() == 0) {
            throw new Exception("There was no matching 6 bp sequence for the provided oligo2");
        }

        int startIndexOligo2;
        int lowestForwardEditDistanceOligo2SeqIndex = 0; // Will be assigned to the END index (on the 5' to 3' template seq) of the 5' to 3' sequence that best matches oligo2
        int lowestForwardEditDistanceOligo2 = Integer.MAX_VALUE;
        for (int i : forwardIndicesOligo2) {
            String potentialMatchSeq = templateSeq.substring(Math.max(0, i - 25), i); // Select the last 25 bases of the potential matching seq, or all bases if less than 25 bp long
            String annealingRegionOligo2 = oligo2Seq.substring(Math.max(0, oligo1Seq.length() - 25), oligo2Seq.length()); // Select the last 25 bases of oligo2, or all bases if less than 25 bp long
            int tempEditDistance = calcEditDistance.run(potentialMatchSeq, annealingRegionOligo2);
            if (tempEditDistance < lowestForwardEditDistanceOligo2) {
                lowestForwardEditDistanceOligo2SeqIndex = i;
                lowestForwardEditDistanceOligo2 = tempEditDistance;
            }
        }
        int lowestRevEditDistanceOligo2SeqIndex = 0; // Will be assigned to the FIRST index (on the 5' to 3' template seq) of the 3' to 5' sequence that best matches oligo2
        int lowestRevEditDistanceOligo2 = Integer.MAX_VALUE;
        for (int i : revIndicesOligo2) {
            String potentialMatchSeq = templateSeq.substring(i, Math.min(i + 25, templateSeq.length())); // Select the last 25 bases of the potential matching seq, or all bases if less than 25 bp long
            String revOligo2Seq = revcomp.run(oligo2Seq);
            String revAnnealingRegionOligo2 = revOligo2Seq.substring(0, Math.min(25, revOligo2Seq.length())); // Select the last 25 bases of rev comp oligo2, or all bases if less than 25 bp long
            int tempEditDistance = calcEditDistance.run(potentialMatchSeq, revAnnealingRegionOligo2);
            if (tempEditDistance < lowestRevEditDistanceOligo2) {
                lowestRevEditDistanceOligo2SeqIndex = i;
                lowestRevEditDistanceOligo2 = tempEditDistance;
            }
        }

        boolean oligo2Forward = true;
        if (lowestForwardEditDistanceOligo2 < lowestRevEditDistanceOligo2) {
            startIndexOligo2 = lowestForwardEditDistanceOligo2SeqIndex - oligo2Seq.length();
        } else {
            startIndexOligo2 = lowestRevEditDistanceOligo2SeqIndex;
            oligo2Forward = false;
        }
        // Sanity Check:
        // StartIndexOligo2 should now be assigned to the FIRST index (on the 5' to 3' template) of the sequence that best matches oligo2 (whether 5' to 3' OR 3' to 5')

        // Check if the oligos will anneal in the same directions
        if (oligo1Forward == oligo2Forward) {
            throw new Exception("The provided oligos will both anneal in the same direction for:\n" + pcr.toString());
        }

        String flankedRegion = templateSeq.substring(endIndexOligo1, startIndexOligo2); // Region between the two oligos that does not contain bp from either
        String pcrPdt = oligo1Seq + flankedRegion + revcomp.run(oligo2Seq);
        /*if (productSeq != null && !(pcrPdt.equals(productSeq)) && !(revcomp.run(pcrPdt).equals(productSeq))) { // If a product sequence was explicitly given in the CF and our simulated product does not match it, throw error
         throw new Exception("The product sequence given in this construction file does not match the sequence that will be generated by the reaction"));
         }*/

        fragments.put(pcr.getProduct(), new Polynucleotide(pcrPdt));
    }

    private void validateSequence(String seq) throws Exception {
        if (seq == null) {
            throw new Exception("A provided sequence was not specified in the construction file");
        }
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            if (c != 'a' && c != 'c' && c != 'g' && c != 't') {
                throw new Exception("A provided sequence contains invalid characters");
            }
        }
    }
}
