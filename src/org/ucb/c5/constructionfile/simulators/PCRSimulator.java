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
import org.ucb.c5.Pair;
import org.ucb.c5.constructionfile.ParseOligo;
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
    private ParseOligo ol;

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
        ol = new ParseOligo();
        
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
        
        System.out.println("oligo1");
        System.out.println(oligo1);
        
        Polynucleotide oligo1p = ol.run(oligo1);
        Polynucleotide oligo2p = ol.run(oligo2);
        
        Polynucleotide pdtPoly= run(oligo1p, oligo2p, templates);
        //Polynucleotide pdtPoly = new Polynucleotide(pdtSeq); !!!
        //System.out.println(pcr.getProduct() + "\t" + pdtSeq); 
        fragments.put(pcr.getProduct(), pdtPoly);
        
        
    }

    public Polynucleotide run(Polynucleotide primer1, Polynucleotide primer2, List<Polynucleotide> templates) throws Exception {
        
        String oligo1 = primer1.getSequence().toUpperCase();
        String oligo2 = primer2.getSequence().toUpperCase();
        
        
        String rc2 = rc.run(oligo2);
        String rc1 = rc.run(oligo1);
        
        //First try the simple method
        if(templates.size() == 1) {
            try {
                String pcrpdt = perfect18Simulation(oligo1, rc2, templates.get(0));
                
                return rewrap(pcrpdt,primer1,primer2);
                
                
            } catch(Exception err) {
                Log.info("Failed to simulate PCR based on perfect 18bp anneal");
            }
        }

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
                    
                    return rewrap(seq,primer1,primer2);
                }
                if (seq.startsWith(oligo2) && seq.endsWith(rc1)) {
                    return rewrap(seq,primer1,primer2);
                }
            }

            oldStrands.clear();
            oldStrands.addAll(singleStrands);
        }
    }
    
    private Polynucleotide rewrap(String pcrpdt, Polynucleotide oligo1, Polynucleotide oligo2){
        if (!pcrpdt.toUpperCase().startsWith(oligo1.getSequence().toUpperCase())){
            pcrpdt = rc.run(pcrpdt);
        };

        Polynucleotide out = new Polynucleotide(pcrpdt,"","",true,false,false,oligo1.getMod5(),oligo2.getMod5());
        
        return out;
    }
    
    private String perfect18Simulation(String oligo1, String rc2, Polynucleotide poly) throws Exception {
        String template = poly.getSequence().toUpperCase();
        String forAnneal = oligo1.substring(oligo1.length()-18);
        int start = template.indexOf(forAnneal);
        if(start == -1) {
            template = rc.run(template);
            start = template.indexOf(forAnneal);
        }
        
        if(start == -1) {
            throw new IllegalArgumentException("Perfect 18 Simulation could not align oligo1");
        }
        
        start = start + 18;
        
        //Check that there isn't another internal annealing site
        int internal = template.indexOf(forAnneal, start);
        if(internal > 0) {
            throw new IllegalArgumentException("Multiple annealing sites for oligo1");
        }
        
        //Spin the plasmid around if it is circular
        if(poly.isCircular()) {
            template = rotator.run(template, start);
            start = 0;
        }
        
        //Find the annealing site of oligo2
        String revAnneal = rc2.substring(0, 18);
        int end = template.indexOf(revAnneal);
        if(end == -1) {
            throw new IllegalArgumentException("Perfect 18 Simulation could not align oligo2");
        }
        
        //Check that there isn't another internal annealing site
        internal = template.indexOf(revAnneal, end + 1);
        if(internal > 0 && internal < 40000) {
            throw new IllegalArgumentException("Multiple annealing sites for oligo2");
        }
        
        return oligo1 + template.substring(start, end) + rc2;
    }

    private Set<String> initialDissociation(String oligo1, String oligo2, Set<Polynucleotide> species) throws Exception {
        Set<String> newspecies = new HashSet<>();
        Set<String> forspecies = new HashSet<>();
        Set<String> revspecies = new HashSet<>();
        for (Polynucleotide poly : species) {
            //Handle if it is circular
            if (poly.isCircular()) {
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
        
        
        ParseOligo ol = new ParseOligo();
        

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
           
//        String EB2F = "ccataggtctcAagtcgcctgtaggtcggcaggctt";
//        String EB2R = "tggtgGGTCTCtcgagctaggcaccaataactgcc";
//        List<Polynucleotide> templates1 = new ArrayList<>();
//        Polynucleotide pLYC25 = new Polynucleotide("ggcagttattggtgcctagaaatattttatctgattaataagatgatcttcttgagatcgttttggtctgcgcgtaatctcttgctctgaaaacgaaaaaaccgccttgcagggcggtttttcgaaggttctctgagctaccaactctttgaaccgaggtaactggcttggaggagcgcagtcaccaaaacttgtcctttcagtttagccttaaccggcgcatgacttcaagactaactcctctaaatcaattaccagtggctgctgccagtggtgcttttgcatgtctttccgggttggactcaagacgatagttaccggataaggcgcagcggtcggactgaacggggggttcgtgcatacagtccagcttggagcgaactgcctacccggaactgagtgtcaggcgtggaatgagacaaacgcggccataacagcggaatgacaccggtaaaccgaaaggcaggaacaggagagcgcacgagggagccgccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccaccactgatttgagcgtcagatttcgtgatgcttgtcaggggggcggagcctatggaaaaacggctttgccgcggccctctcacttccctgttaagtatcttcctggcatcttccaggaaatctccgccccgttcgtaagccatttccgctcgccgcagtcgaacgaccgagcgtagcgagtcagtgagcgaggaagcggaatatatcctgtatcacatattctgctgacgcaccggtgcagccttttttctcctgccacatgaagcacttcactgacaccctcatcagtgcCAACATAGTAAGCCAGTATACACTCCGCTACAGCCACGTATCGCCAGATGTTCCAGACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACATTTTTGCTTCCGTGTGGTATTATGGGAGCAGTAGGTCTACGGTTAACTGATACTAAAAGACAATTCAGCGGGTAACCTTGCAATGGTGAGTGGCAGTAAAGCGGGCGTTTCGCCTCATCGCGAAATAGAAGTAATGAGACAATCCATTGACGATCACCTGGCTGGCCTGTTACCTGAAACCGACAGCCAGGATATCGTCAGCCTTGCGATGCGTGAAGGCGTCATGGCACCCGGTAAACGGATCCGTCCGCTGCTGATGCTGCTGGCCGCCCGCGACCTCCGCTACCAGGGCAGTATGCCTACGCTGCTCGATCTCGCCTGCGCCGTTGAACTGACCCATACCGCGTCGCTGATGCTCGACGACATGCCCTGCATGGACAACGCCGAGCTGCGCCGCGGTCAGCCCACTACCCACAAAAAATTTGGTGAGAGCGTGGCGATCCTTGCCTCCGTTGGGCTGCTCTCTAAAGCCTTTGGTCTGATCGCCGCCACCGGCGATCTGCCGGGGGAGAGGCGTGCCCAGGCGGTCAACGAGCTCTCTACCGCCGTGGGCGTGCAGGGCCTGGTACTGGGGCAGTTTCGCGATCTTAACGATGCCGCCCTCGACCGTACCCCTGACGCTATCCTCAGCACCAACCACCTCAAGACCGGCATTCTGTTCAGCGCGATGCTGCAGATCGTCGCCATTGCTTCCGCCTCGTCGCCGAGCACGCGAGAGACGCTGCacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtcaccggtcatgaaaatcgcctgataccgcccttttgggttcaagcagtacataacgatggaaccacattacaggagtagtgatgaatgaaggacgagcgccttgttcagcgtaagaacgatcatctggatatcgttctcgacccccgtcgcgccgtaactcaggctagcgcaggttttgagcgctggcgctttacccactgcgccctgccagagctgaattttagcgacatcacgctggaaaccaccttcctgaatcggcagctacaggctccgctgctgatcagctccatgaccggcggcgttgagcgctcgcgccatatcaaccgccacctcgccgaggcggcgcaggtgctaaaaattgcgatgggggtgggctcccagcgcgtcgccattgagagcgacgcgggcttagggctggataaaaccctgcggcagctggctccggacgtgccgctgctggcgaacctcggcgcggcgcagctgaccggcagaaaaggtattgattacgcccgacgggccgtggagatgatcgaggcggatgcgctgattgtgcacctaaacccgctgcaggaggcgctacagcccggcggcgatcgcgactggcgcggacggctggcggctattgaaactctggtccgcgagctgcccgttccgctggtggtgaaagaggtgggagccggtatctcccgaaccgtggccgggcagctgatcgatgccggcgttaccgtgattgacgtcgcgggcgcgggcggcaccagctgggccgccgttgaaggcgagcgggcggccaccgagcagcagcgcagcgtggccaacgtctttgccgactgggggatccccaccgctgaggcgctggttgacattgccgaggcctggccgcagatgccccttattgcctcgggaatgcgggctgggctatttcaccctaccactggctattcgctgccgctggcggtggcccttgccgacgcgattgccgacagcccgcggctgggcagcgttccgctctatcagctcacccggcagtttgccgaacgccactggcgcaggcagggattcttccgcctgctgaaccggatgcttttcctggccgggcgcgaggagaaccgctggcgggtgatgcagcgcttttatgggctgccggagcccaccgtagagcgcttttacgccggtcggctctctctctttgataaggcccgcattttgacgggcaagccaccggttccgctgggcgaagcctggcgggcggcgctgaaccattttcctgacagacgagataaaggatgaaaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccggcaggcgcatggaggattacgtcaggctgctgccggtaaaacccttctaccgactctgctgggagtccgggaagaccctcgactatgctaacgacagcgccgagcttgaggcgcagattacccagttcaacccccgcgacgtcgagggctaccggcgctttctggcttactcccaggcggtattccaggagggatatttgcgcctcggcagcgtgccgttcctctcttttcgcgacatgctgcgcgccgggccgcagctgcttaagctccaggcgtggcagagcgtctaccagtcggtttcgcgctttattgaggatgagcatctgcggcaggccttctcgttccactccctgctggtaggcggcaaccccttcaccacctcgtccatctacaccctgatccacgcccttgagcgggagtggggggtctggttccctgagggcggcaccggggcgctggtgaacggcatggtgaagctgtttaccgatctgggcggggagatcgaactcaacgcccgggtcgaagagctggtggtggccgataaccgcgtaagccaggtccggctggcggatggtcggatctttgacaccgacgccgtagcctcgaacgctgacgtggtgaacacctataaaaagctgctcggccaccatccggtggggcagaagcgggcggcagcgctggagcgcaagagcatgagcaactcgctgtttgtgctctacttcggcctgaaccagcctcattcccagctggcgcaccataccatctgttttggtccccgctaccgggagctgatcgacgagatctttaccggcagcgcgctggcggatgacttctcgctctacctgcactcgccctgcgtgaccgatccctcgctcgcgcctcccggctgcgccagcttctacgtgctggccccggtgccgcatcttggcaacgcgccgctggactgggcgcaggaggggccgaagctgcgcgaccgcatctttgactaccttgaagagcgctatatgcccggcctgcgtagccagctggtgacccagcggatctttaccccggcagacttccacgacacgctggatgcgcatctgggatcggccttctccatcgagccgctgctgacccaaagcgcctggttccgcccgcacaaccgcgacagcgacattgccaacctctacctggtgggcgcaggtactcaccctggggcgggcattcctggcgtagtggcctcggcgaaagccaccgccagcctgatgattgaggatctgcaatgagccaaccgccgctgcttgaccacgccacgcagaccatggccaacggctcgaaaagttttgccaccgctgcgaagctgttcgacccggccacccgccgtagcgtgctgatgctctacacctggtgccgccactgcgatgacgtcattgacgaccagacccacggcttcgccagcgaggccgcggcggaggaggaggccacccagcgcctggcccggctgcgcacgctgaccctggcggcgtttgaaggggccgagatgcaggatccggccttcgctgcctttcaggaggtggcgctgacccacggtattacgccccgcatggcgctcgatcacctcgacggctttgcgatggacgtggctcagacccgctatgtcacctttgaggatacgctgcgctactgctatcacgtggcgggcgtggtgggtctgatgatggccagggtgatgggcgtgcgggatgagcgggtgctggatcgcgcctgcgatctggggctggccttccagctgacgaatatcgcccgggatattattgacgatgcggctattgaccgctgctatctgcccgccgagtggctgcaggatgccgggctgaccccggagaactatgccgcgcgggagaatcgggccgcgctggcgcgggtggcggagcggcttattgatgccgcagagccgtactacatctcctcccaggccgggctacacgatctgccgccgcgctgcgcctgggcgatcgccaccgcccgcagcgtctaccgggagatcggtattaaggtaaaagcggcgggaggcagcgcctgggatcgccgccagcacaccagcaaaggtgaaaaaattgccatgctgatggcggcaccggggcaggttattcgggcgaagacgacgagggtgacgccgcgtccggccggtctttggcagcgtcccgtttaggcgggcggccatgacgttcacgcaggatcagtcgcctgtaggtcggcaggcttgggaagctgtggtatggctgtgcaggtcgtaaatcactgcataattcgtgtcgctcaaggcgcactcccgttctggataatgttttttgcgccgacatcataacggttctggcaaatattctgaaatgagctgttgacaattaatcatccggctcgtataatgtgtggaattgtgagcggataacaatttcacacaggaaacagcgccgctgagaaaaagcgaagcggcactgctctttaacaatttatcagacaatctgtgtgggcactcgaccggaattatcgattaactttattattaaaaattaaagaggtatatattaatgtatcgattaaataaggaggaataaaccatgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctgaacatctttttttaaagtttttaaatcaatcaactttctcttcatcatttttatcttttcgatgataataatttgggatatgtgagacacttacaaaacttccaaggtctgcggggcaaaacaatcgataaatcagcccgggaattaacatggcaaccactcatttggatgtttgcgccgtggttccggcggccggatttggccgtcgaatgcaaacggaatgtcctaagcaatatctctcaatcggtaatcaaaccattcttgaacactcggtgcatgcgctgctggcgcatccccgggtgaaacgtgtcgtcattgccataagtcctggcgatagccgttttgcacaacttcctctggcgaatcatccgcaaatcaccgttgtagatggcggtgatgagcgtgccgattccgtgctggcaggtctgaaagccgctggcgacgcgcagtgggtattggtgcatgacgccgctcgtccttgtttgcatcaggatgacctcgcgcgattgttggcgttgagcgaaaccagccgcacgggggggatcctcgccgcaccagtgcgcgatactatgaaacgtgccgaaccgggcaaaaatgccattgctcataccgttgatcgcaacggcttatggcacgcgctgacgccgcaatttttccctcgtgagctgttacatgactgtctgacgcgcgctctaaatgaaggcgcgactattaccgacgaagcctcggcgctggaatattgcggattccatcctcagttggtcgaaggccgtgcggataacattaaagtcacgcgcccggaagatttggcactggccgagttttacctcacccgaaccatccatcaggagaatacataatgcgaattggacacggttttgacgtacatgcctttggcggtgaaggcccaattatcattggtggcgtacgcattccttacgaaaaaggattgctggcgcattctgatggcgacgtggcgctccatgcgttgaccgatgcattgcttggcgcggcggcgctgggggatatcggcaagctgttcccggataccgatccggcatttaaaggtgccgatagccgcgagctgctacgcgaagcctggcgtcgtattcaggcgaagggttatacccttggcaacgtcgatgtcactatcatcgctcaggcaccgaagatgttgccgcacattccacaaatgcgcgtgtttattgccgaagatctcggctgccatatggatgatgttaacgtgaaagccactactacggaaaaactgggatttaccggacgtggggaagggattgcctgtgaagcggtggcgctactcattaaggcaacaaaatgattgagtttgataatctcacttacctccacggtaaaccgcacctcaggcaataataaagtttgcggccgcgaattcctgcagcccgggggatccactagttctagagcggccgccaccgcggtggagctccagcttttgttccctttagtgagggttaatttcgagcttggcgtaatcatggtcatagctgtttcctgtgtggtggtagatcctctacgccggacgcatcgtggccggcatcaccggcgccacaggtgcggttgctggcgcctatatcgccgacatcacccagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaatttttttaa");
//        Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
//        templates1.add(pLYC25);
//        templates.add(frag2);
//        String Eback2 = sim.run(EB2F, EB2R, templates1);
//        System.out.println("Eback2" + Eback2);
        
        //PCRSimulatorTest 3, focused on testing the differences for modifications
        //Examples such as this
        

        String EB2F = "/5Phos/ATTACCGCCTTTGAGTGAGC";
        String EB2R = "/5Me-isodC/GTATCACGAGGCAGAATTTCAG";
        
        
        
        Polynucleotide EB2Fp = ol.run(EB2F);
        Polynucleotide EB2Rp = ol.run(EB2R);
        
        List<Polynucleotide> templates1 = new ArrayList<>();
        Polynucleotide pLYC25 = new Polynucleotide("ATTACCGCCTTTGAGTGAGCAAAAAAAAAAAAAAAAAAAAAACTGAAATTCTGCCTCGTGATAC");
//        Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
        templates1.add(pLYC25);
//        templates.add(frag2);
        //(Polynucleotide primer1, Polynucleotide primer2, List<Polynucleotide> templates)

        
        Polynucleotide Eback2 = sim.run(EB2Fp, EB2Rp, templates1);
        System.out.println("Eback2\n" + Eback2);

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
