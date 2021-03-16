/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.CalcEditDistance;
import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.sequtils.RevComp;
import org.ucb.c5.sequtils.StringRotater;
import org.ucb.c5.utils.Log;

/**
 *
 * @author J. Christopher Anderson
 */
public class PCRSimulator {

    private CalcEditDistance ced;
    private PolyRevComp revcomp;
    private StringRotater rotator;
    private RevComp rc;
    private ExtensionSimulator exSim;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
        rc = new RevComp();
        rc.initiate();
        rotator = new StringRotater();
        rotator.initiate();
        ced = new CalcEditDistance();
        ced.initiate();
        exSim = new ExtensionSimulator();
        exSim.initiate();
    }

    public void run(PCR pcr, Map<String, Polynucleotide> fragments) throws Exception {
        //String oligo1, String oligo2, List<Polynucleotide> templates
        String oligo1 = null;
        try {
            Polynucleotide poly = fragments.get(pcr.getOligo1());
            oligo1 = poly.getSequence().toUpperCase();
        } catch (Exception err) {
            throw new IllegalArgumentException("Oligo " + pcr.getOligo1() + " is missing");
        }

        //String oligo1, String oligo2, List<Polynucleotide> templates
        String oligo2 = null;
        try {
            Polynucleotide poly = fragments.get(pcr.getOligo2());
            oligo2 = poly.getSequence().toUpperCase();
        } catch (Exception err) {
            throw new IllegalArgumentException("Oligo " + pcr.getOligo2() + " is missing");
        }

        List<Polynucleotide> templates = new ArrayList<>();
        for (String temp : pcr.getTemplates()) {
            Polynucleotide poly = fragments.get(temp);
            if (poly == null) {
                throw new IllegalArgumentException("Template " + temp + " is missing");
            }
            templates.add(poly);
        }
        
        Log.seq(pcr.getOligo1(), oligo1, "Simulating PCR with this oligo1");
        Log.seq(pcr.getOligo2(), oligo2, "Simulating PCR with this oligo2");
        for(String tname : pcr.getTemplates()) {
            Polynucleotide poly = fragments.get(tname);
            Log.seq(tname, poly.getForwardStrand(), "Simulating PCR with this template");
        }

        String pdtSeq = run(oligo1, oligo2, templates);
        Polynucleotide pdtPoly = new Polynucleotide(pdtSeq);
//        System.out.println(pcr.getProduct() + "\t" + pdtSeq);
        fragments.put(pcr.getProduct(), pdtPoly);
        
        
    }

    public String run(String oligo1, String oligo2, List<Polynucleotide> templates) throws Exception {
        oligo1 = oligo1.toUpperCase();
        oligo2 = oligo2.toUpperCase();
        String rc2 = rc.run(oligo2);
        String rc1 = rc.run(oligo1);

        //Combine all the species in a set and denature them
        Set<Polynucleotide> species = new HashSet<>();
        species.addAll(templates);
        Set<String> singleStrands = initialDissociation(oligo1, oligo2, species);

        //Create another set for storing the previous round strands
        Set<String> oldStrands = new HashSet<>();

        //Simulate thermocycling
        int breaker = 0;

        //Keep track of sequences already compared
        Set<Pair<String, String>> alreadyTried = new HashSet<>();

        while (true) {
            //Abort if exceeded 30 cycles
            if (breaker > 30) {
                throw new IllegalArgumentException("PCRSimulator stuck in a loop");
            }
            breaker++;

            //Add oligos then anneal and polymerize
            singleStrands.add(oligo1);
            singleStrands.add(oligo2);

            singleStrands = simulateAnneal(singleStrands, alreadyTried);
            
            for(String ss : singleStrands) {
                Log.seq("", ss, "PCR intermediate from round " + breaker);
            }

            //End loop if a PCR product is generated
            for (String seq : singleStrands) {
                if (seq.startsWith(oligo1) && seq.endsWith(rc2)) {
                    return seq;
                }
                if (seq.startsWith(oligo2) && seq.endsWith(rc1)) {
                    return seq;
                }
            }

            oldStrands.clear();
            oldStrands.addAll(singleStrands);
        }
    }

    private Set<String> initialDissociation(String oligo1, String oligo2, Set<Polynucleotide> species) throws Exception {
        Set<String> newspecies = new HashSet<>();
        Set<String> forspecies = new HashSet<>();
        Set<String> revspecies = new HashSet<>();
        for (Polynucleotide poly : species) {
            //Handle if it is circular
            if (poly.isIsCircular()) {
                String plas = poly.getSequence().toUpperCase();
                String fpdt = anneal(oligo1, plas);

                //If there is an anneal with the forward oligo
                if (fpdt != null) {
                    String fpdt2 = fpdt.substring(oligo1.length());
                    String fpdt3 = rc.run(fpdt2);
                    int start = plas.indexOf(fpdt3) + fpdt3.length() + oligo1.length();
                    String rotated = rotator.run(plas, start);
                    String rotRC = rc.run(rotated);
                    poly = new Polynucleotide(rotRC);

                    //Otherwise see if anneals to the reverse oligo
                } else {
                    String rpdt = anneal(oligo2, plas);
                    if (rpdt != null) {
                        String rpdt2 = rpdt.substring(oligo2.length());
                        String rpdt3 = rc.run(rpdt2);
                        int start = plas.indexOf(rpdt3) + rpdt3.length() + oligo2.length();
                        String rotated = rotator.run(plas, start);
                        poly = new Polynucleotide(rotated);
                    } else {
                        //If it gets here, the oligos do not anneal to either strand
                        throw new IllegalArgumentException("Could not anneal oligos to circular template");
                    }
                }
            }

            //Handle as linear, double stranded
            String forstrand = dissociate(poly);
            newspecies.add(forstrand);

            //If it's already single stranded, no need to do anything
            if (!poly.isIsDoubleStranded()) {
                continue;
            }

            //Handle the reverse strand
            Polynucleotide rev = revcomp.run(poly);
            String revstrand = dissociate(rev);
            newspecies.add(revstrand);
        }

        //Truncate each single-stranded specie with the reverse oligo
        return newspecies;
    }

    /**
     * Dissociates the forward strand of a linear Polynucleotide and returns it
     * as a String
     *
     * @param poly
     * @return
     */
    private String dissociate(Polynucleotide poly) {
        String forstrand = poly.getSequence();

        if (!poly.getExt5().startsWith("-")) {
            forstrand = poly.getExt5() + forstrand;
        }

        if (poly.getExt3().startsWith("-")) {
            forstrand += poly.getExt3();
        }

        return forstrand.toUpperCase();
    }

    private Set<String> simulateAnneal(Set<String> singlestrands, Set<Pair<String, String>> alreadyTried) throws Exception {
        Set<String> species = new HashSet<>();

        //Try all comparisons of any two oligos considering if the first one will anneal and polymerize
        for (String oligoA : singlestrands) {
            for (String oligoB : singlestrands) {
                Pair currpair = new Pair(oligoA, oligoB);
                if (alreadyTried.contains(currpair)) {
                    continue;
                }
                alreadyTried.add(currpair);
                String pdt = anneal(oligoA, oligoB);
                if (pdt != null) {
                    species.add(pdt);
                }
            }
        }

        return species;
    }

    private String anneal(String oligoA, String oligoB) throws Exception {
        oligoA = oligoA.toUpperCase();
        oligoB = oligoB.toUpperCase();

        //Reverse complement oligoB
        String rcB = rc.run(oligoB);

        //If oligoA and oligoB are identical, terminate
        if (oligoA.equals(oligoB)) {
            return null;
        }

        //If oligoA and oligoB are partner strands, terminate
        if (oligoA.equals(rcB)) {
            return null;
        }

        //Replace any degenerate bases with 'A' and all-caps
        String oligoAn = replaceDegenerateBase(oligoA);
        String oligoBn = replaceDegenerateBase(oligoB);

        //Scan through and find the best annealing index (bestIndex) if any
        int bestIndex = exSim.run(oligoAn, oligoBn);

        //If ExtensionSimulator returns error code -1, no anneal > 40degC was found
        if (bestIndex == -1) {
            return null;
        }

        //If ExtensionSimulator gives -2, then there are multiple annealing sites, abort
        if (bestIndex == -2) {
            throw new IllegalArgumentException("Multiple annealing sites >55degC for:\noligoA: " + oligoA + "\ntemplate: " + oligoB);
        }

        //If gets here there is a match, so construct the extension product
        String pdt = oligoA + rcB.substring(bestIndex);
        return pdt;
    }

    private String replaceDegenerateBase(String oligoA) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < oligoA.length(); i++) {
            char base = oligoA.charAt(i);
            if (base != 'A' && base != 'T' && base != 'C' && base != 'G') {
                sb.append("A");
            } else {
                sb.append(base);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        PCRSimulator sim = new PCRSimulator();
        sim.initiate();

/*        
        String EB1F = "agtcaGGTCTCataatGCCACGTATCGCCAGATGTT";
        String EB1R = "ccagtggtctcAgactgatcctgcgtgaacgtcatg";
        List<Polynucleotide> templates1 = new ArrayList<>();
        Polynucleotide pLYC25 = new Polynucleotide("ggcagttattggtgcctagaaatattttatctgattaataagatgatcttcttgagatcgttttggtctgcgcgtaatctcttgctctgaaaacgaaaaaaccgccttgcagggcggtttttcgaaggttctctgagctaccaactctttgaaccgaggtaactggcttggaggagcgcagtcaccaaaacttgtcctttcagtttagccttaaccggcgcatgacttcaagactaactcctctaaatcaattaccagtggctgctgccagtggtgcttttgcatgtctttccgggttggactcaagacgatagttaccggataaggcgcagcggtcggactgaacggggggttcgtgcatacagtccagcttggagcgaactgcctacccggaactgagtgtcaggcgtggaatgagacaaacgcggccataacagcggaatgacaccggtaaaccgaaaggcaggaacaggagagcgcacgagggagccgccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccaccactgatttgagcgtcagatttcgtgatgcttgtcaggggggcggagcctatggaaaaacggctttgccgcggccctctcacttccctgttaagtatcttcctggcatcttccaggaaatctccgccccgttcgtaagccatttccgctcgccgcagtcgaacgaccgagcgtagcgagtcagtgagcgaggaagcggaatatatcctgtatcacatattctgctgacgcaccggtgcagccttttttctcctgccacatgaagcacttcactgacaccctcatcagtgcCAACATAGTAAGCCAGTATACACTCCGCTACAGCCACGTATCGCCAGATGTTCCAGACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACATTTTTGCTTCCGTGTGGTATTATGGGAGCAGTAGGTCTACGGTTAACTGATACTAAAAGACAATTCAGCGGGTAACCTTGCAATGGTGAGTGGCAGTAAAGCGGGCGTTTCGCCTCATCGCGAAATAGAAGTAATGAGACAATCCATTGACGATCACCTGGCTGGCCTGTTACCTGAAACCGACAGCCAGGATATCGTCAGCCTTGCGATGCGTGAAGGCGTCATGGCACCCGGTAAACGGATCCGTCCGCTGCTGATGCTGCTGGCCGCCCGCGACCTCCGCTACCAGGGCAGTATGCCTACGCTGCTCGATCTCGCCTGCGCCGTTGAACTGACCCATACCGCGTCGCTGATGCTCGACGACATGCCCTGCATGGACAACGCCGAGCTGCGCCGCGGTCAGCCCACTACCCACAAAAAATTTGGTGAGAGCGTGGCGATCCTTGCCTCCGTTGGGCTGCTCTCTAAAGCCTTTGGTCTGATCGCCGCCACCGGCGATCTGCCGGGGGAGAGGCGTGCCCAGGCGGTCAACGAGCTCTCTACCGCCGTGGGCGTGCAGGGCCTGGTACTGGGGCAGTTTCGCGATCTTAACGATGCCGCCCTCGACCGTACCCCTGACGCTATCCTCAGCACCAACCACCTCAAGACCGGCATTCTGTTCAGCGCGATGCTGCAGATCGTCGCCATTGCTTCCGCCTCGTCGCCGAGCACGCGAGAGACGCTGCacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtcaccggtcatgaaaatcgcctgataccgcccttttgggttcaagcagtacataacgatggaaccacattacaggagtagtgatgaatgaaggacgagcgccttgttcagcgtaagaacgatcatctggatatcgttctcgacccccgtcgcgccgtaactcaggctagcgcaggttttgagcgctggcgctttacccactgcgccctgccagagctgaattttagcgacatcacgctggaaaccaccttcctgaatcggcagctacaggctccgctgctgatcagctccatgaccggcggcgttgagcgctcgcgccatatcaaccgccacctcgccgaggcggcgcaggtgctaaaaattgcgatgggggtgggctcccagcgcgtcgccattgagagcgacgcgggcttagggctggataaaaccctgcggcagctggctccggacgtgccgctgctggcgaacctcggcgcggcgcagctgaccggcagaaaaggtattgattacgcccgacgggccgtggagatgatcgaggcggatgcgctgattgtgcacctaaacccgctgcaggaggcgctacagcccggcggcgatcgcgactggcgcggacggctggcggctattgaaactctggtccgcgagctgcccgttccgctggtggtgaaagaggtgggagccggtatctcccgaaccgtggccgggcagctgatcgatgccggcgttaccgtgattgacgtcgcgggcgcgggcggcaccagctgggccgccgttgaaggcgagcgggcggccaccgagcagcagcgcagcgtggccaacgtctttgccgactgggggatccccaccgctgaggcgctggttgacattgccgaggcctggccgcagatgccccttattgcctcgggaatgcgggctgggctatttcaccctaccactggctattcgctgccgctggcggtggcccttgccgacgcgattgccgacagcccgcggctgggcagcgttccgctctatcagctcacccggcagtttgccgaacgccactggcgcaggcagggattcttccgcctgctgaaccggatgcttttcctggccgggcgcgaggagaaccgctggcgggtgatgcagcgcttttatgggctgccggagcccaccgtagagcgcttttacgccggtcggctctctctctttgataaggcccgcattttgacgggcaagccaccggttccgctgggcgaagcctggcgggcggcgctgaaccattttcctgacagacgagataaaggatgaaaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccggcaggcgcatggaggattacgtcaggctgctgccggtaaaacccttctaccgactctgctgggagtccgggaagaccctcgactatgctaacgacagcgccgagcttgaggcgcagattacccagttcaacccccgcgacgtcgagggctaccggcgctttctggcttactcccaggcggtattccaggagggatatttgcgcctcggcagcgtgccgttcctctcttttcgcgacatgctgcgcgccgggccgcagctgcttaagctccaggcgtggcagagcgtctaccagtcggtttcgcgctttattgaggatgagcatctgcggcaggccttctcgttccactccctgctggtaggcggcaaccccttcaccacctcgtccatctacaccctgatccacgcccttgagcgggagtggggggtctggttccctgagggcggcaccggggcgctggtgaacggcatggtgaagctgtttaccgatctgggcggggagatcgaactcaacgcccgggtcgaagagctggtggtggccgataaccgcgtaagccaggtccggctggcggatggtcggatctttgacaccgacgccgtagcctcgaacgctgacgtggtgaacacctataaaaagctgctcggccaccatccggtggggcagaagcgggcggcagcgctggagcgcaagagcatgagcaactcgctgtttgtgctctacttcggcctgaaccagcctcattcccagctggcgcaccataccatctgttttggtccccgctaccgggagctgatcgacgagatctttaccggcagcgcgctggcggatgacttctcgctctacctgcactcgccctgcgtgaccgatccctcgctcgcgcctcccggctgcgccagcttctacgtgctggccccggtgccgcatcttggcaacgcgccgctggactgggcgcaggaggggccgaagctgcgcgaccgcatctttgactaccttgaagagcgctatatgcccggcctgcgtagccagctggtgacccagcggatctttaccccggcagacttccacgacacgctggatgcgcatctgggatcggccttctccatcgagccgctgctgacccaaagcgcctggttccgcccgcacaaccgcgacagcgacattgccaacctctacctggtgggcgcaggtactcaccctggggcgggcattcctggcgtagtggcctcggcgaaagccaccgccagcctgatgattgaggatctgcaatgagccaaccgccgctgcttgaccacgccacgcagaccatggccaacggctcgaaaagttttgccaccgctgcgaagctgttcgacccggccacccgccgtagcgtgctgatgctctacacctggtgccgccactgcgatgacgtcattgacgaccagacccacggcttcgccagcgaggccgcggcggaggaggaggccacccagcgcctggcccggctgcgcacgctgaccctggcggcgtttgaaggggccgagatgcaggatccggccttcgctgcctttcaggaggtggcgctgacccacggtattacgccccgcatggcgctcgatcacctcgacggctttgcgatggacgtggctcagacccgctatgtcacctttgaggatacgctgcgctactgctatcacgtggcgggcgtggtgggtctgatgatggccagggtgatgggcgtgcgggatgagcgggtgctggatcgcgcctgcgatctggggctggccttccagctgacgaatatcgcccgggatattattgacgatgcggctattgaccgctgctatctgcccgccgagtggctgcaggatgccgggctgaccccggagaactatgccgcgcgggagaatcgggccgcgctggcgcgggtggcggagcggcttattgatgccgcagagccgtactacatctcctcccaggccgggctacacgatctgccgccgcgctgcgcctgggcgatcgccaccgcccgcagcgtctaccgggagatcggtattaaggtaaaagcggcgggaggcagcgcctgggatcgccgccagcacaccagcaaaggtgaaaaaattgccatgctgatggcggcaccggggcaggttattcgggcgaagacgacgagggtgacgccgcgtccggccggtctttggcagcgtcccgtttaggcgggcggccatgacgttcacgcaggatcagtcgcctgtaggtcggcaggcttgggaagctgtggtatggctgtgcaggtcgtaaatcactgcataattcgtgtcgctcaaggcgcactcccgttctggataatgttttttgcgccgacatcataacggttctggcaaatattctgaaatgagctgttgacaattaatcatccggctcgtataatgtgtggaattgtgagcggataacaatttcacacaggaaacagcgccgctgagaaaaagcgaagcggcactgctctttaacaatttatcagacaatctgtgtgggcactcgaccggaattatcgattaactttattattaaaaattaaagaggtatatattaatgtatcgattaaataaggaggaataaaccatgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctgaacatctttttttaaagtttttaaatcaatcaactttctcttcatcatttttatcttttcgatgataataatttgggatatgtgagacacttacaaaacttccaaggtctgcggggcaaaacaatcgataaatcagcccgggaattaacatggcaaccactcatttggatgtttgcgccgtggttccggcggccggatttggccgtcgaatgcaaacggaatgtcctaagcaatatctctcaatcggtaatcaaaccattcttgaacactcggtgcatgcgctgctggcgcatccccgggtgaaacgtgtcgtcattgccataagtcctggcgatagccgttttgcacaacttcctctggcgaatcatccgcaaatcaccgttgtagatggcggtgatgagcgtgccgattccgtgctggcaggtctgaaagccgctggcgacgcgcagtgggtattggtgcatgacgccgctcgtccttgtttgcatcaggatgacctcgcgcgattgttggcgttgagcgaaaccagccgcacgggggggatcctcgccgcaccagtgcgcgatactatgaaacgtgccgaaccgggcaaaaatgccattgctcataccgttgatcgcaacggcttatggcacgcgctgacgccgcaatttttccctcgtgagctgttacatgactgtctgacgcgcgctctaaatgaaggcgcgactattaccgacgaagcctcggcgctggaatattgcggattccatcctcagttggtcgaaggccgtgcggataacattaaagtcacgcgcccggaagatttggcactggccgagttttacctcacccgaaccatccatcaggagaatacataatgcgaattggacacggttttgacgtacatgcctttggcggtgaaggcccaattatcattggtggcgtacgcattccttacgaaaaaggattgctggcgcattctgatggcgacgtggcgctccatgcgttgaccgatgcattgcttggcgcggcggcgctgggggatatcggcaagctgttcccggataccgatccggcatttaaaggtgccgatagccgcgagctgctacgcgaagcctggcgtcgtattcaggcgaagggttatacccttggcaacgtcgatgtcactatcatcgctcaggcaccgaagatgttgccgcacattccacaaatgcgcgtgtttattgccgaagatctcggctgccatatggatgatgttaacgtgaaagccactactacggaaaaactgggatttaccggacgtggggaagggattgcctgtgaagcggtggcgctactcattaaggcaacaaaatgattgagtttgataatctcacttacctccacggtaaaccgcacctcaggcaataataaagtttgcggccgcgaattcctgcagcccgggggatccactagttctagagcggccgccaccgcggtggagctccagcttttgttccctttagtgagggttaatttcgagcttggcgtaatcatggtcatagctgtttcctgtgtggtggtagatcctctacgccggacgcatcgtggccggcatcaccggcgccacaggtgcggttgctggcgcctatatcgccgacatcacccagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaatttttttaa");
//        Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
        templates1.add(pLYC25);
//        templates.add(frag2);
        String Eback1 = sim.run(EB1F, EB1R, templates1);
        System.out.println("Eback1" + Eback1);
*/        
        
        String EB2F = "ccataggtctcAagtcgcctgtaggtcggcaggctt";
        String EB2R = "tggtgGGTCTCtcgagctaggcaccaataactgcc";
        List<Polynucleotide> templates1 = new ArrayList<>();
        Polynucleotide pLYC25 = new Polynucleotide("ggcagttattggtgcctagaaatattttatctgattaataagatgatcttcttgagatcgttttggtctgcgcgtaatctcttgctctgaaaacgaaaaaaccgccttgcagggcggtttttcgaaggttctctgagctaccaactctttgaaccgaggtaactggcttggaggagcgcagtcaccaaaacttgtcctttcagtttagccttaaccggcgcatgacttcaagactaactcctctaaatcaattaccagtggctgctgccagtggtgcttttgcatgtctttccgggttggactcaagacgatagttaccggataaggcgcagcggtcggactgaacggggggttcgtgcatacagtccagcttggagcgaactgcctacccggaactgagtgtcaggcgtggaatgagacaaacgcggccataacagcggaatgacaccggtaaaccgaaaggcaggaacaggagagcgcacgagggagccgccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccaccactgatttgagcgtcagatttcgtgatgcttgtcaggggggcggagcctatggaaaaacggctttgccgcggccctctcacttccctgttaagtatcttcctggcatcttccaggaaatctccgccccgttcgtaagccatttccgctcgccgcagtcgaacgaccgagcgtagcgagtcagtgagcgaggaagcggaatatatcctgtatcacatattctgctgacgcaccggtgcagccttttttctcctgccacatgaagcacttcactgacaccctcatcagtgcCAACATAGTAAGCCAGTATACACTCCGCTACAGCCACGTATCGCCAGATGTTCCAGACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACATTTTTGCTTCCGTGTGGTATTATGGGAGCAGTAGGTCTACGGTTAACTGATACTAAAAGACAATTCAGCGGGTAACCTTGCAATGGTGAGTGGCAGTAAAGCGGGCGTTTCGCCTCATCGCGAAATAGAAGTAATGAGACAATCCATTGACGATCACCTGGCTGGCCTGTTACCTGAAACCGACAGCCAGGATATCGTCAGCCTTGCGATGCGTGAAGGCGTCATGGCACCCGGTAAACGGATCCGTCCGCTGCTGATGCTGCTGGCCGCCCGCGACCTCCGCTACCAGGGCAGTATGCCTACGCTGCTCGATCTCGCCTGCGCCGTTGAACTGACCCATACCGCGTCGCTGATGCTCGACGACATGCCCTGCATGGACAACGCCGAGCTGCGCCGCGGTCAGCCCACTACCCACAAAAAATTTGGTGAGAGCGTGGCGATCCTTGCCTCCGTTGGGCTGCTCTCTAAAGCCTTTGGTCTGATCGCCGCCACCGGCGATCTGCCGGGGGAGAGGCGTGCCCAGGCGGTCAACGAGCTCTCTACCGCCGTGGGCGTGCAGGGCCTGGTACTGGGGCAGTTTCGCGATCTTAACGATGCCGCCCTCGACCGTACCCCTGACGCTATCCTCAGCACCAACCACCTCAAGACCGGCATTCTGTTCAGCGCGATGCTGCAGATCGTCGCCATTGCTTCCGCCTCGTCGCCGAGCACGCGAGAGACGCTGCacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtcaccggtcatgaaaatcgcctgataccgcccttttgggttcaagcagtacataacgatggaaccacattacaggagtagtgatgaatgaaggacgagcgccttgttcagcgtaagaacgatcatctggatatcgttctcgacccccgtcgcgccgtaactcaggctagcgcaggttttgagcgctggcgctttacccactgcgccctgccagagctgaattttagcgacatcacgctggaaaccaccttcctgaatcggcagctacaggctccgctgctgatcagctccatgaccggcggcgttgagcgctcgcgccatatcaaccgccacctcgccgaggcggcgcaggtgctaaaaattgcgatgggggtgggctcccagcgcgtcgccattgagagcgacgcgggcttagggctggataaaaccctgcggcagctggctccggacgtgccgctgctggcgaacctcggcgcggcgcagctgaccggcagaaaaggtattgattacgcccgacgggccgtggagatgatcgaggcggatgcgctgattgtgcacctaaacccgctgcaggaggcgctacagcccggcggcgatcgcgactggcgcggacggctggcggctattgaaactctggtccgcgagctgcccgttccgctggtggtgaaagaggtgggagccggtatctcccgaaccgtggccgggcagctgatcgatgccggcgttaccgtgattgacgtcgcgggcgcgggcggcaccagctgggccgccgttgaaggcgagcgggcggccaccgagcagcagcgcagcgtggccaacgtctttgccgactgggggatccccaccgctgaggcgctggttgacattgccgaggcctggccgcagatgccccttattgcctcgggaatgcgggctgggctatttcaccctaccactggctattcgctgccgctggcggtggcccttgccgacgcgattgccgacagcccgcggctgggcagcgttccgctctatcagctcacccggcagtttgccgaacgccactggcgcaggcagggattcttccgcctgctgaaccggatgcttttcctggccgggcgcgaggagaaccgctggcgggtgatgcagcgcttttatgggctgccggagcccaccgtagagcgcttttacgccggtcggctctctctctttgataaggcccgcattttgacgggcaagccaccggttccgctgggcgaagcctggcgggcggcgctgaaccattttcctgacagacgagataaaggatgaaaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccggcaggcgcatggaggattacgtcaggctgctgccggtaaaacccttctaccgactctgctgggagtccgggaagaccctcgactatgctaacgacagcgccgagcttgaggcgcagattacccagttcaacccccgcgacgtcgagggctaccggcgctttctggcttactcccaggcggtattccaggagggatatttgcgcctcggcagcgtgccgttcctctcttttcgcgacatgctgcgcgccgggccgcagctgcttaagctccaggcgtggcagagcgtctaccagtcggtttcgcgctttattgaggatgagcatctgcggcaggccttctcgttccactccctgctggtaggcggcaaccccttcaccacctcgtccatctacaccctgatccacgcccttgagcgggagtggggggtctggttccctgagggcggcaccggggcgctggtgaacggcatggtgaagctgtttaccgatctgggcggggagatcgaactcaacgcccgggtcgaagagctggtggtggccgataaccgcgtaagccaggtccggctggcggatggtcggatctttgacaccgacgccgtagcctcgaacgctgacgtggtgaacacctataaaaagctgctcggccaccatccggtggggcagaagcgggcggcagcgctggagcgcaagagcatgagcaactcgctgtttgtgctctacttcggcctgaaccagcctcattcccagctggcgcaccataccatctgttttggtccccgctaccgggagctgatcgacgagatctttaccggcagcgcgctggcggatgacttctcgctctacctgcactcgccctgcgtgaccgatccctcgctcgcgcctcccggctgcgccagcttctacgtgctggccccggtgccgcatcttggcaacgcgccgctggactgggcgcaggaggggccgaagctgcgcgaccgcatctttgactaccttgaagagcgctatatgcccggcctgcgtagccagctggtgacccagcggatctttaccccggcagacttccacgacacgctggatgcgcatctgggatcggccttctccatcgagccgctgctgacccaaagcgcctggttccgcccgcacaaccgcgacagcgacattgccaacctctacctggtgggcgcaggtactcaccctggggcgggcattcctggcgtagtggcctcggcgaaagccaccgccagcctgatgattgaggatctgcaatgagccaaccgccgctgcttgaccacgccacgcagaccatggccaacggctcgaaaagttttgccaccgctgcgaagctgttcgacccggccacccgccgtagcgtgctgatgctctacacctggtgccgccactgcgatgacgtcattgacgaccagacccacggcttcgccagcgaggccgcggcggaggaggaggccacccagcgcctggcccggctgcgcacgctgaccctggcggcgtttgaaggggccgagatgcaggatccggccttcgctgcctttcaggaggtggcgctgacccacggtattacgccccgcatggcgctcgatcacctcgacggctttgcgatggacgtggctcagacccgctatgtcacctttgaggatacgctgcgctactgctatcacgtggcgggcgtggtgggtctgatgatggccagggtgatgggcgtgcgggatgagcgggtgctggatcgcgcctgcgatctggggctggccttccagctgacgaatatcgcccgggatattattgacgatgcggctattgaccgctgctatctgcccgccgagtggctgcaggatgccgggctgaccccggagaactatgccgcgcgggagaatcgggccgcgctggcgcgggtggcggagcggcttattgatgccgcagagccgtactacatctcctcccaggccgggctacacgatctgccgccgcgctgcgcctgggcgatcgccaccgcccgcagcgtctaccgggagatcggtattaaggtaaaagcggcgggaggcagcgcctgggatcgccgccagcacaccagcaaaggtgaaaaaattgccatgctgatggcggcaccggggcaggttattcgggcgaagacgacgagggtgacgccgcgtccggccggtctttggcagcgtcccgtttaggcgggcggccatgacgttcacgcaggatcagtcgcctgtaggtcggcaggcttgggaagctgtggtatggctgtgcaggtcgtaaatcactgcataattcgtgtcgctcaaggcgcactcccgttctggataatgttttttgcgccgacatcataacggttctggcaaatattctgaaatgagctgttgacaattaatcatccggctcgtataatgtgtggaattgtgagcggataacaatttcacacaggaaacagcgccgctgagaaaaagcgaagcggcactgctctttaacaatttatcagacaatctgtgtgggcactcgaccggaattatcgattaactttattattaaaaattaaagaggtatatattaatgtatcgattaaataaggaggaataaaccatgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctgaacatctttttttaaagtttttaaatcaatcaactttctcttcatcatttttatcttttcgatgataataatttgggatatgtgagacacttacaaaacttccaaggtctgcggggcaaaacaatcgataaatcagcccgggaattaacatggcaaccactcatttggatgtttgcgccgtggttccggcggccggatttggccgtcgaatgcaaacggaatgtcctaagcaatatctctcaatcggtaatcaaaccattcttgaacactcggtgcatgcgctgctggcgcatccccgggtgaaacgtgtcgtcattgccataagtcctggcgatagccgttttgcacaacttcctctggcgaatcatccgcaaatcaccgttgtagatggcggtgatgagcgtgccgattccgtgctggcaggtctgaaagccgctggcgacgcgcagtgggtattggtgcatgacgccgctcgtccttgtttgcatcaggatgacctcgcgcgattgttggcgttgagcgaaaccagccgcacgggggggatcctcgccgcaccagtgcgcgatactatgaaacgtgccgaaccgggcaaaaatgccattgctcataccgttgatcgcaacggcttatggcacgcgctgacgccgcaatttttccctcgtgagctgttacatgactgtctgacgcgcgctctaaatgaaggcgcgactattaccgacgaagcctcggcgctggaatattgcggattccatcctcagttggtcgaaggccgtgcggataacattaaagtcacgcgcccggaagatttggcactggccgagttttacctcacccgaaccatccatcaggagaatacataatgcgaattggacacggttttgacgtacatgcctttggcggtgaaggcccaattatcattggtggcgtacgcattccttacgaaaaaggattgctggcgcattctgatggcgacgtggcgctccatgcgttgaccgatgcattgcttggcgcggcggcgctgggggatatcggcaagctgttcccggataccgatccggcatttaaaggtgccgatagccgcgagctgctacgcgaagcctggcgtcgtattcaggcgaagggttatacccttggcaacgtcgatgtcactatcatcgctcaggcaccgaagatgttgccgcacattccacaaatgcgcgtgtttattgccgaagatctcggctgccatatggatgatgttaacgtgaaagccactactacggaaaaactgggatttaccggacgtggggaagggattgcctgtgaagcggtggcgctactcattaaggcaacaaaatgattgagtttgataatctcacttacctccacggtaaaccgcacctcaggcaataataaagtttgcggccgcgaattcctgcagcccgggggatccactagttctagagcggccgccaccgcggtggagctccagcttttgttccctttagtgagggttaatttcgagcttggcgtaatcatggtcatagctgtttcctgtgtggtggtagatcctctacgccggacgcatcgtggccggcatcaccggcgccacaggtgcggttgctggcgcctatatcgccgacatcacccagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaatttttttaa");
//        Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
        templates1.add(pLYC25);
//        templates.add(frag2);
        String Eback2 = sim.run(EB2F, EB2R, templates1);
        System.out.println("Eback2" + Eback2);
        
/*
        String EPF0 = "atgctGGTCTCactcggaagatcctttttgataatc";
        String EPR0 = "agctaGGTCTCgattaagcaaaaggccaggaaccgt";
        List<Polynucleotide> templates2 = new ArrayList<>();
        Polynucleotide p20N87 = new Polynucleotide("ggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctagtggAAAGCCACGTTGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGAACAATAAAACTGTCTGCTTACATAAACAGTAATACAAGGGGTGTTATGAGCCATATTCAACGGGAAACGTCTTGTTCGAGGCCGCGATTAAATTCCAACATGGACGCTGATTTATATGGGTATAAATGGGCTCGCGATAATGTCGGGCAATCAGGTGCGACAATCTATCGATTGTATGGGAAGCCCGATGCGCCAGAGTTGTTTCTGAAACATGGCAAAGGTAGCGTTGCCAATGATGTTACAGATGAGATGGTCAGACTAAACTGGCTGACGGAATTTATGCCTCTTCCGACCATCAAGCATTTTATCCGTACTCCTGATGATGCATGGTTACTCACCACTGCGATTCCGGGGAAAACAGCATTCCAGGTATTAGAAGAATATCCTGATTCAGGTGAAAATATTGTTGATGCGCTGGCAGTGTTCCTGCGCCGGTTGCATTCGATTCCTGTTTGTAATTGTCCTTTTAACAGCGATCGCGTATTTCGACTCGCTCAGGCGCAATCACGAATGAATAACGGTTTGGTTGATGCGAGTGATTTTGATGACGAGCGTAATGGCTGGCCTGTTGAACAAGTCTGGAAAGAAATGCATAAACTTTTGCCATTCTCACCGGATTCAGTCGTCACTCATGGTGATTTCTCACTTGATAACCTTATTTTTGACGAGGGGAAATTAATAGGTTGTATTGATGTTGGACGAGTCGGAATCGCAGACCGATACCAGGATCTTGCAATCCTATGGAACTGCCTCGGTGAGTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGActgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt");
//        Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
        templates2.add(p20N87);
//        templates.add(frag2);
        String Eori0 = sim.run(EPF0, EPR0, templates2);
        System.out.println("Eori0" + Eori0);
*/ 

    }
}
