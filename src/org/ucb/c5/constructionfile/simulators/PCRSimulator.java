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
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.CalcEditDistance;
import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.sequtils.RevComp;
import org.ucb.c5.sequtils.StringRotater;

/**
 *
 * @author J. Christopher Anderson
 */
public class PCRSimulator {

    private CalcEditDistance ced;
    private PolyRevComp revcomp;
    private StringRotater rotator;
    private RevComp rc;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
        rc = new RevComp();
        rc.initiate();
        rotator = new StringRotater();
        rotator.initiate();
        ced = new CalcEditDistance();
        ced.initiate();
    }

    public void run(PCR pcr, Map<String, Polynucleotide> fragments) throws Exception {
        //String oligo1, String oligo2, List<Polynucleotide> templates
        String oligo1 = null;
        try {
            Polynucleotide poly = fragments.get(pcr.getOligo1());
            oligo1 = poly.getSequence().toUpperCase();
        } catch (Exception err) {
            throw new IllegalArgumentException("Oligo " + oligo1 + " is missing");
        }

        //String oligo1, String oligo2, List<Polynucleotide> templates
        String oligo2 = null;
        try {
            Polynucleotide poly = fragments.get(pcr.getOligo2());
            oligo2 = poly.getSequence().toUpperCase();
        } catch (Exception err) {
            throw new IllegalArgumentException("Oligo " + oligo2 + " is missing");
        }

        List<Polynucleotide> templates = new ArrayList<>();
        for (String temp : pcr.getTemplates()) {
            Polynucleotide poly = fragments.get(temp);
            if (poly == null) {
                throw new IllegalArgumentException("Template " + temp + " is missing");
            }
            templates.add(poly);
        }

        String pdtSeq = run(oligo1, oligo2, templates);
        Polynucleotide pdtPoly = new Polynucleotide(pdtSeq);
//        System.out.println(pcr.getProduct() + "\t" + pdtSeq);
        fragments.put(pcr.getProduct(), pdtPoly);
    }

    public String run(String oligo1, String oligo2, List<Polynucleotide> templates) throws Exception {
        oligo1 = oligo1.toUpperCase();
        oligo2 = oligo2.toUpperCase();
        
        //Combine all the species in a set and denature them
        Set<Polynucleotide> species = new HashSet<>();
        species.addAll(templates);
        Set<String> singleStrands = simulateDissociation(oligo1, oligo2, species);

        //Create another set for storing the previous round strands
        Set<String> oldStrands = new HashSet<>();

        //Simulate thermocycling
        int breaker = 0;
        while (true) {
            //Abort if exceeded 30 cycles
            if (breaker > 30) {
                throw new IllegalArgumentException("PCRSimulator stuck in a loop");
            }
            breaker++;

            //Add oligos then anneal and polymerize
            singleStrands.add(oligo1);
            singleStrands.add(oligo2);
            singleStrands = simulateAnneal(singleStrands);

            //End loop if this last round of single strands is unchanged from last round
            if (singleStrands.equals(oldStrands)) {
                break;
            }
            oldStrands.clear();
            oldStrands.addAll(singleStrands);
        }

        //Determine which of the species is the PCR product
         String rc2 = rc.run(oligo2);
         Set<String> ans = new HashSet<>();
        for (String seq : singleStrands) {
            if (seq.startsWith(oligo1) && seq.endsWith(rc2)) {
                ans.add(seq);
            }
            String rcseq = rc.run(seq);
            if (rcseq.startsWith(oligo1) && rcseq.endsWith(rc2)) {
                ans.add(rcseq);
            }
        }
        if (ans.size() > 1){
            throw new IllegalArgumentException("Multiple PCR products");
        } 
        if (ans.size() == 1){
            return ans.iterator().next();
        }
        

        throw new IllegalArgumentException("No PCR product generated");
    }

    private Set<String> simulateDissociation(String oligo1, String oligo2, Set<Polynucleotide> species) throws Exception {
        Set<String> newspecies = new HashSet<>();
        for (Polynucleotide poly : species) {
            //Handle if it is circular
            if (poly.isIsCircular()) {
                String plas = poly.getSequence().toUpperCase();
                String fpdt = anneal(oligo1, plas);
                if (fpdt != null) {
                    String fpdt2 = fpdt.substring(oligo1.length());
                    String fpdt3 = rc.run(fpdt2);
                    int start = plas.indexOf(fpdt3) + fpdt3.length() + oligo1.length();
                    String rotated = rotator.run(plas, start);
                    String rotRC = rc.run(rotated);
                    poly = new Polynucleotide(rotRC);
                } else {
                    String rpdt = anneal(oligo2, plas);
                    if (rpdt != null) {
                        String rpdt2 = rpdt.substring(oligo2.length());
                        String rpdt3 = rc.run(rpdt2);
                        int start = plas.indexOf(rpdt3) + rpdt3.length() + oligo2.length();
                        String rotated = rotator.run(plas, start);
                        poly = new Polynucleotide(rotated);
                    } else {
                        throw new IllegalArgumentException("Could not handle circular template");
                    }
                }
            }

            //Handle as linear, double stranded
            String forstrand = dissociate(poly);
            Polynucleotide rev = revcomp.run(poly);
            newspecies.add(forstrand);

            //If it's already single stranded, no need to do anything
            if (!poly.isIsDoubleStranded()) {
                continue;
            }

            //Handle the reverse strand
            String revstrand = dissociate(rev);
            newspecies.add(revstrand);
        }
        return newspecies;
    }

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

    private Set<String> simulateAnneal(Set<String> singlestrands) throws Exception {
        Set<String> species = new HashSet<>();

        //Try all comparisons of any two oligos considering if the first one will anneal and polymerize
        for (String oligoA : singlestrands) {
            for (String oligoB : singlestrands) {
                String pdt = anneal(oligoA, oligoB);
                if (pdt != null) {
                    species.add(pdt);
                }
            }
        }

        return species;
    }

    private String anneal(String oligoA, String oligoB) throws Exception {
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

        //Grab the last 6bp of oligoA
        String sixbp = oligoA.substring(oligoA.length() - 6);

        //Scan through and find the best annealing index (bestIndex) if any
        int index = 11; //index + 6 = 17, the first site it could be
        int bestSimilarity = 17;  //17 is one short of the cutoff of 18
        int bestIndex = -1;
        int breaker = 0;
        while (true) {
            //Abort if exceeded excessive cycles
            if (breaker > 5000) {
                throw new IllegalArgumentException("PCRSimulator anneal stuck in a loop");
            }
            breaker++;

            //Find the next 6bp match, quit if there are no more
            index = rcB.indexOf(sixbp, index + 1);
            if (index == -1) {
                break;
            }

            //Find the 30bp (or less) region of oligoA
            int startA = 0;
            if (oligoA.length() > 30) {
                startA = oligoA.length() - 30;
            }
            String annealA = oligoA.substring(startA);

            //Find the 30bp (or less) region ending in index+6
            int startB = (index + 6) - 30;
            if (startB < 0) {
                startB = 0;
            }
            String annealB = rcB.substring(startB, index + 6);
            int maxlength = Math.max(annealA.length(), annealB.length());

            //Find the edit distance
            int distance = ced.run(annealA, annealB);
            int similarity = maxlength - distance;
            if (similarity > bestSimilarity) {
                bestIndex = index;
                bestSimilarity = similarity;
            }
        }  //end while

        //If bestIndex was not updated, no 15+ annealing site was found
        if (bestIndex < 0) {
            return null;
        }

        //If gets here there is a match, so construct the extension product
        String pdt = oligoA + rcB.substring(bestIndex + 6);
        return pdt;
    }

    public static void main(String[] args) throws Exception {
        PCRSimulator sim = new PCRSimulator();
        sim.initiate();

        //run a regular pcr (works)
        {
            String oligo1 = "gtatcacgaggcagaatttcag";
            String oligo2 = "attaccgcctttgagtgagc";
            List<Polynucleotide> templates = new ArrayList<>();
            Polynucleotide template = new Polynucleotide("TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
            templates.add(template);

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("Exact match oligos on single template:");
            System.out.println(pdt);
        }
    }
}
