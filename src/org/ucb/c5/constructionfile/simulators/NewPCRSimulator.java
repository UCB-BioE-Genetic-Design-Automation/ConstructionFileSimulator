/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.CalcEditDistance;
import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.sequtils.RevComp;

/**
 *
 * @author J. Christopher Anderson
 */
public class NewPCRSimulator {

    private CalcEditDistance ced;
    private PolyRevComp revcomp;
    private RevComp rc;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
        rc = new RevComp();
        rc.initiate();
        ced = new CalcEditDistance();
        ced.initiate();
    }

    public String run(String oligo1, String oligo2, List<Polynucleotide> templates) throws Exception {
        oligo1 = oligo1.toUpperCase();
        oligo2 = oligo2.toUpperCase();

        //Combine all the species in a set and denature them
        Set<Polynucleotide> species = new HashSet<>();
        species.addAll(templates);
        Set<String> singleStrands = simulateDissociation(species);

        //Create another set for storing the previous round strands
        Set<String> oldStrands = new HashSet<>();

        //Simulate thermocycling
        while (true) {
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
        for (String seq : singleStrands) {
            if (seq.startsWith(oligo1) && seq.endsWith(rc2)) {
                return seq;
            }
        }

        throw new IllegalArgumentException("No PCR product generated");
    }

    private Set<String> simulateDissociation(Set<Polynucleotide> species) throws Exception {
        Set<String> newspecies = new HashSet<>();
        for (Polynucleotide poly : species) {
            //Handle if it is circular
            if (poly.isIsCircular()) {
                poly = new Polynucleotide(poly.getSequence() + poly.getSequence());
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
        while (true) {
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

            //Find the edit distance
            int distance = ced.run(annealA, annealB);
            int similarity = 30 - distance;
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
        NewPCRSimulator sim = new NewPCRSimulator();
        sim.initiate();

        //run a regular pcr (works)
        {
            String oligo1 = "gtatcacgaggcagaatttcag";
            String oligo2 = "attaccgcctttgagtgagc";
            List<Polynucleotide> templates = new ArrayList<>();
            Polynucleotide template = new Polynucleotide("TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
            templates.add(template);

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("\nExact match oligos on single template:");
            System.out.println(pdt);
        }

        //run a pcr with 5' tails (works)
        {
            String oligo1 = "ccataGGATCCgtatcacgaggcagaatttcag";
            String oligo2 = "cgtatGAATTCattaccgcctttgagtgagc";
            List<Polynucleotide> templates = new ArrayList<>();
            Polynucleotide template = new Polynucleotide("TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
            templates.add(template);

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("\nOligos with 5' tails on single template:");
            System.out.println(pdt);
        }

        //run a pcr on a circular template over the origin (works)
        {
            String oligo1 = "ccataGGATCCgtatcacgaggcagaatttcag";
            String oligo2 = "cgtatGAATTCattaccgcctttgagtgagc";
            List<Polynucleotide> templates = new ArrayList<>();
            Polynucleotide template = new Polynucleotide("TCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTGCCATCCAGCTGATATCCCCTATAGTGAGTCGTATTACATGGTCATAGCTGTTTCCTGGCAGCTCTGGCCCGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGCCTCCTCTAGACCAGCCAGGACAGAAATGCCTCGACTTCGCTGCTGCCCAAGGTTGCCGGGTGACGCACACCGTGGAAACGGATGAAGGCACGAACCCAGTGGACATAAGCCTGTTCGGTTCGTAAGCTGTAATGCAAGTAGCGTATGCGCTCACGCAACTGGTCCAGAACCTTGACCGAACGCAGCGGTGGTAACGGCGCAGTGGCGGTTTTCATGGCTTGTTATGACTGTTTTTTTGGGGTACAGTCTATGCCTCGGGCATCCAAGCAGCAAGCGCGTTACGCCGTGGGTCGATGTTTGATGTTATGGAGCAGCAACGATGTTACGCAGCAGGGCAGTCGCCCTAAAACAAAGTTAAACATCATGAGGGAAGCGGTGATCGCCGAAGTATCGACTCAACTATCAGAGGTAGTTGGCGTCATCGAGCGCCATCTCGAACCGACGTTGCTGGCCGTACATTTGTACGGCTCCGCAGTGGATGGCGGCCTGAAGCCACACAGTGATATTGATTTGCTGGTTACGGTGACCGTAAGGCTTGATGAAACAACGCGGCGAGCTTTGATCAACGACCTTTTGGAAACTTCGGCTTCCCCTGGAGAGAGCGAGATTCTCCGCGCTGTAGAAGTCACCATTGTTGTGCACGACGACATCATTCCGTGGCGTTATCCAGCTAAGCGCGAACTGCAATTTGGAGAATGGCAGCGCAATGACATTCTTGCAGGTATCTTCGAGCCAGCCACGATCGACATTGATCTGGCTATCTTGCTGACAAAAGCAAGAGAACATAGCGTTGCCTTGGTAGGTCCAGCGGCGGAGGAACTCTTTGATCCGGTTCCTGAACAGGATCTATTTGAGGCGCTAAATGAAACCTTAACGCTATGGAACTCGCCGCCCGACTGGGCTGGCGATGAGCGAAATGTAGTGCTTACGTTGTCCCGCATTTGGTACAGCGCAGTAACCGGCAAAATCGCGCCGAAGGATGTCGCTGCCGACTGGGCAATGGAGCGCCTGCCGGCCCAGTATCAGCCCGTCATACTTGAAGCTAGACAGGCTTATCTTGGACAAGAAGAAGATCGCTTGGCCTCGCGCGCAGATCAGTTGGAAGAATTTGTCCACTACGTGAAAGGCGAGATCACCAAGGTAGTCGGCAAATAACCCTCGAGCCACCCATGACCAAAATCCCTTAACGTGAGTTACGCGTCGTTCCACTGAGCGTCAGACCCCGTAGAAAAGATCAAAGGATCTTCTTGAGATCCTTTTTTTCTGCGCGTAATCTGCTGCTTGCAAACAAAAAAACCACCGCTACCAGCGGTGGTTTGTTTGCCGGATCAAGAGCTACCAACTCTTTTTCCGAAGGTAACTGGCTTCAGCAGAGCGCAGATACCAAATACTGTCCTTCTAGTGTAGCCGTAGTTAGGCCACCACTTCAAGAACTCTGTAGCACCGCCTACATACCTCGCTCTGCTAATCCTGTTACCAGTGGCTGCTGCCAGTGGCGATAAGTCGTGTCTTACCGGGTTGGACTCAAGACGATAGTTACCGGATAAGGCGCAGCGGTCGGGCTGAACGGGGGGTTCGTGCACACAGCCCAGCTTGGAGCGAACGACCTACACCGAACTGAGATACCTACAGCGTGAGCATTGAGAAAGCGCCACGCTTCCCGAAGGGAGAAAGGCGGACAGGTATCCGGTAAGCGGCAGGGTCGGAACAGGAGAGCGCACGAGGGAGCTTCCAGGGGGAAACGCCTGGTATCTTTATAGTCCTGTCGGGTTTCGCCACCTCTGACTTGAGCGTCGATTTTTGTGATGCTCGTCAGGGGGGCGGAGCCTATGGAAAAACGCCAGCAACGCGGCCTTTTTACGGTTCCTGGCCTTTTGCTGGCCTTTTGCTCACATGTTCTTTCCTGCGTTATCCCCTGATTCTGTGGATAACCGTcctaggTGTAAAACGACGGCCAGTCTTAAGCTCGGGCCCCAAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCC", true);
            templates.add(template);

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("\nOligos with 5' tails on a circular template over the origin:");
            System.out.println(pdt);
        }

        //run a soeing reaction on 2 templates (works)
        {
            String oligo1 = "ccataGGATCCgtatcacgaggcagaatttcag";
            String oligo2 = "cgtatGAATTCattaccgcctttgagtgagc";
            List<Polynucleotide> templates = new ArrayList<>();
            Polynucleotide frag1 = new Polynucleotide("TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGAC");
            Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
            templates.add(frag1);
            templates.add(frag2);

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("\nSOEing of two templates:");
            System.out.println(pdt);
        }

        //run a soeing reaction on 4 templates out of order (works)
        {
            String oligo1 = "ccataGGATCCgtatcacgaggcagaatttcag";
            String oligo2 = "cgtatGAATTCattaccgcctttgagtgagc";
            List<Polynucleotide> templates = new ArrayList<>();
            Polynucleotide frag1 = new Polynucleotide("TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAG");
            Polynucleotide frag2 = new Polynucleotide("TGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTA");
            Polynucleotide frag3 = new Polynucleotide("ACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG");
            Polynucleotide frag4 = new Polynucleotide("TTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGAC");

            templates.add(frag1);
            templates.add(frag2);
            templates.add(frag3);
            templates.add(frag4);

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("\nSOEing of 4 unordered templates:");
            System.out.println(pdt);
        }
        
//TODO: This one is failing
        //run a PCA with many oligos as template and 2 oligos as primers (from genedesign)
//        {
//            String oligo1 = "ccataGGATCCgtatcacgaggcagaatttcag";
//            String oligo2 = "cgtatGAATTCattaccgcctttgagtgagc";
//            List<Polynucleotide> templates = new ArrayList<>();
//            Polynucleotide[] temps = new Polynucleotide[46];
//            temps[0] = new Polynucleotide("AAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACC", "", "", false, false, false);
//            temps[1] = new Polynucleotide("AATCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTG", "", "", false, false, false);
//            temps[2] = new Polynucleotide("AGCGAACGGCAGCGGACCACCTTTGGTAACTTTCAGTTTAGCGGTCTG", "", "", false, false, false);
//            temps[3] = new Polynucleotide("AGGATCCTATAAACGCAGAAAGGCCCACCCGAAGGTGAGCCAGTGTGACTCT", "", "", false, false, false);
//            temps[4] = new Polynucleotide("ATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGGTATCACGAGG", "", "", false, false, false);
//            temps[5] = new Polynucleotide("ATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG", "", "", false, false, false);
//            temps[6] = new Polynucleotide("CAAATAATGATTTTATTTTGACTGAT", "", "", false, false, false);
//            temps[7] = new Polynucleotide("CAGCCGGGTGTTTAACGTAAGCTTTGGAACCGTACTGGAACTGCGGGGACA", "", "", false, false, false);
//            temps[8] = new Polynucleotide("CCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCG", "", "", false, false, false);
//            temps[9] = new Polynucleotide("CCATAGATCCTTTCTCCTCTTTCAGATCCGTGCTCAGTATCTCTATCACTGATAG", "", "", false, false, false);
//            temps[10] = new Polynucleotide("CCGGACCGTCGGACGGGAAGTTGGTACCACGCAGTTTAACTTTGTAGA", "", "", false, false, false);
//            temps[11] = new Polynucleotide("CGTACGGACGACCTTCACCTTCACCTTCGATTTCGAACTCGTGACCGTTA", "", "", false, false, false);
//            temps[12] = new Polynucleotide("CGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGA", "", "", false, false, false);
//            temps[13] = new Polynucleotide("CTTAGCGAAAGCTAAGGATTTTTTTTATCTGAAATTCTGCCTCGTGATACCAATTCGGAGC", "", "", false, false, false);
//            temps[14] = new Polynucleotide("CTTCAGCACGTTCGTACTGTTCAACGATGGTGTAGTCTTCGTTGTGGGAGGT", "", "", false, false, false);
//            temps[15] = new Polynucleotide("GAACCTTCCATACGAACTTTGAAACGCATGAACTCTTTGATAACGTCTTCGCTAC", "", "", false, false, false);
//            temps[16] = new Polynucleotide("GAACGACCGAGCGCAGCGAGTCAGTGAGCGAGGAAGCCTGCACGTCG", "", "", false, false, false);
//            temps[17] = new Polynucleotide("GAAGTTCATAACACGTTCCCATTTGAAACCTTCCGGGAAGGACAGTTTCAGGTAGTCC", "", "", false, false, false);
//            temps[18] = new Polynucleotide("GACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCG", "", "", false, false, false);
//            temps[19] = new Polynucleotide("GAGAGCGTTCACCGACAAACAACAGATAAAACGAAAGGCCCAGTCTTTCGAC", "", "", false, false, false);
//            temps[20] = new Polynucleotide("GATGTCAATCTCTATCACTGATAGGGAAGATCTCATGAATTCCAGAAATCATCCTTA", "", "", false, false, false);
//            temps[21] = new Polynucleotide("GATGTCCAGTTTGATGTCGGTTTTGTAAGCACCCGGCAGCTGAACCGGTTTTTTAG", "", "", false, false, false);
//            temps[22] = new Polynucleotide("GCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCG", "", "", false, false, false);
//            temps[23] = new Polynucleotide("GCATTTTGATTTCACCTTTCAGAGCACCGTCTTCCGGGTACATACGTTCGGTGGA", "", "", false, false, false);
//            temps[24] = new Polynucleotide("GCCTGCTTTTTTGTACAAAGTTGGCATTATAAAAAAGCATTGCTCATCAATTTGTTGC", "", "", false, false, false);
//            temps[25] = new Polynucleotide("GCCTTTCGTTTTATTTGATGCCTGGAGATCCTTATTAAGCACCGGTGGAGTGA", "", "", false, false, false);
//            temps[26] = new Polynucleotide("GCCTTTCTGCGTTTATAGGATCCTAACTCGACGTGCAGGCTTCCTCG", "", "", false, false, false);
//            temps[27] = new Polynucleotide("GCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCC", "", "", false, false, false);
//            temps[28] = new Polynucleotide("GGAAGCTTCCCAACCCATGGTTTTT", "", "", false, false, false);
//            temps[29] = new Polynucleotide("GGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACC", "", "", false, false, false);
//            temps[30] = new Polynucleotide("GTACAAGAAAGCTGGGTCGAATTGATTACCGCCTTTGAGTGAGCTGATACCGCTC", "", "", false, false, false);
//            temps[31] = new Polynucleotide("GTAGGTGGTTTTAACTTCAGCGTCGTAGTGACCACCGTCTTTCAGTTTCAGA", "", "", false, false, false);
//            temps[32] = new Polynucleotide("GTCTTGCAGGGAGGAGTCCTGGGTAACGGTAACAACACCACCGTCTTC", "", "", false, false, false);
//            temps[33] = new Polynucleotide("GTGACCTGTTCGTTGCAACAAATTGATGAGCAATTATTTTTTATAATGCCAACTT", "", "", false, false, false);
//            temps[34] = new Polynucleotide("GTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCA", "", "", false, false, false);
//            temps[35] = new Polynucleotide("GTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCG", "", "", false, false, false);
//            temps[36] = new Polynucleotide("TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATG", "", "", false, false, false);
//            temps[37] = new Polynucleotide("TCGCTGCGCTCGGTCGTTCGGCTGCGGCGAGCGGTATCAGCTCACTCAAAG", "", "", false, false, false);
//            temps[38] = new Polynucleotide("TCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTC", "", "", false, false, false);
//            temps[39] = new Polynucleotide("TGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTAC", "", "", false, false, false);
//            temps[40] = new Polynucleotide("TGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTC", "", "", false, false, false);
//            temps[41] = new Polynucleotide("TTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACAC", "", "", false, false, false);
//            temps[42] = new Polynucleotide("TTCAGATAAAAAAAATCCTTAGCTTTCGCTAAGGATGATTTCTGGAATTCATGAGATCT", "", "", false, false, false);
//            temps[43] = new Polynucleotide("TTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTC", "", "", false, false, false);
//            temps[44] = new Polynucleotide("TTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCAC", "", "", false, false, false);
//            temps[45] = new Polynucleotide("TTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGG", "", "", false, false, false);
//            for (Polynucleotide poly : temps) {
//                templates.add(poly);
//            }
//            String pdt = sim.run(oligo1, oligo2, templates);
//            System.out.println("\nPCA of 46 oligos:");
//            System.out.println(pdt);
//        }

        //run a wobble with 2 oligos no template (ser2 N5 lib, works)
        {
            String oligo1 = "CcatagaattcggagagatgccggagcggctgaacggaccggtNNNNNNNNaccggagtaggggcaactctacc";
            String oligo2 = "gtcatctgcagtggcggagagagggggatttgaacccccggtagagttgcccctactccgg";
            List<Polynucleotide> templates = new ArrayList<>();

            String pdt = sim.run(oligo1, oligo2, templates);
            System.out.println("\nOverlap extension of 2 oligos no template:");
            System.out.println(pdt);
        }

//TODO: This one is failing
//        //Run an ipcr (crispr example)
//        {
//            String oligo1 = "ccataACTAGTcatcgccgcagcggtttcaggttttagagctagaaatagcaag";
//            String oligo2 = "ctcagACTAGTattatacctaggactgagctag";
//            List<Polynucleotide> templates = new ArrayList<>();
//            Polynucleotide template = new Polynucleotide("catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtGGGTACATACGTTCGGTGGAgttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca", true);
//            templates.add(template);
//
//            String pdt = sim.run(oligo1, oligo2, templates);
//            System.out.println("\niPCR including origin:");
//            System.out.println(pdt);
//        }
//TODO: This one is failing
//        //run a pcr with mutagenic oligos
//        {
//            String oligo1 = "ccataGGATCCgtatcacgaggcagGatttcag";
//            String oligo2 = "cgtatGAATTCattaccgcctCtgagtgagc";
//            List<Polynucleotide> templates = new ArrayList<>();
//            Polynucleotide template = new Polynucleotide("TCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTGCCATCCAGCTGATATCCCCTATAGTGAGTCGTATTACATGGTCATAGCTGTTTCCTGGCAGCTCTGGCCCGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGCCTCCTCTAGACCAGCCAGGACAGAAATGCCTCGACTTCGCTGCTGCCCAAGGTTGCCGGGTGACGCACACCGTGGAAACGGATGAAGGCACGAACCCAGTGGACATAAGCCTGTTCGGTTCGTAAGCTGTAATGCAAGTAGCGTATGCGCTCACGCAACTGGTCCAGAACCTTGACCGAACGCAGCGGTGGTAACGGCGCAGTGGCGGTTTTCATGGCTTGTTATGACTGTTTTTTTGGGGTACAGTCTATGCCTCGGGCATCCAAGCAGCAAGCGCGTTACGCCGTGGGTCGATGTTTGATGTTATGGAGCAGCAACGATGTTACGCAGCAGGGCAGTCGCCCTAAAACAAAGTTAAACATCATGAGGGAAGCGGTGATCGCCGAAGTATCGACTCAACTATCAGAGGTAGTTGGCGTCATCGAGCGCCATCTCGAACCGACGTTGCTGGCCGTACATTTGTACGGCTCCGCAGTGGATGGCGGCCTGAAGCCACACAGTGATATTGATTTGCTGGTTACGGTGACCGTAAGGCTTGATGAAACAACGCGGCGAGCTTTGATCAACGACCTTTTGGAAACTTCGGCTTCCCCTGGAGAGAGCGAGATTCTCCGCGCTGTAGAAGTCACCATTGTTGTGCACGACGACATCATTCCGTGGCGTTATCCAGCTAAGCGCGAACTGCAATTTGGAGAATGGCAGCGCAATGACATTCTTGCAGGTATCTTCGAGCCAGCCACGATCGACATTGATCTGGCTATCTTGCTGACAAAAGCAAGAGAACATAGCGTTGCCTTGGTAGGTCCAGCGGCGGAGGAACTCTTTGATCCGGTTCCTGAACAGGATCTATTTGAGGCGCTAAATGAAACCTTAACGCTATGGAACTCGCCGCCCGACTGGGCTGGCGATGAGCGAAATGTAGTGCTTACGTTGTCCCGCATTTGGTACAGCGCAGTAACCGGCAAAATCGCGCCGAAGGATGTCGCTGCCGACTGGGCAATGGAGCGCCTGCCGGCCCAGTATCAGCCCGTCATACTTGAAGCTAGACAGGCTTATCTTGGACAAGAAGAAGATCGCTTGGCCTCGCGCGCAGATCAGTTGGAAGAATTTGTCCACTACGTGAAAGGCGAGATCACCAAGGTAGTCGGCAAATAACCCTCGAGCCACCCATGACCAAAATCCCTTAACGTGAGTTACGCGTCGTTCCACTGAGCGTCAGACCCCGTAGAAAAGATCAAAGGATCTTCTTGAGATCCTTTTTTTCTGCGCGTAATCTGCTGCTTGCAAACAAAAAAACCACCGCTACCAGCGGTGGTTTGTTTGCCGGATCAAGAGCTACCAACTCTTTTTCCGAAGGTAACTGGCTTCAGCAGAGCGCAGATACCAAATACTGTCCTTCTAGTGTAGCCGTAGTTAGGCCACCACTTCAAGAACTCTGTAGCACCGCCTACATACCTCGCTCTGCTAATCCTGTTACCAGTGGCTGCTGCCAGTGGCGATAAGTCGTGTCTTACCGGGTTGGACTCAAGACGATAGTTACCGGATAAGGCGCAGCGGTCGGGCTGAACGGGGGGTTCGTGCACACAGCCCAGCTTGGAGCGAACGACCTACACCGAACTGAGATACCTACAGCGTGAGCATTGAGAAAGCGCCACGCTTCCCGAAGGGAGAAAGGCGGACAGGTATCCGGTAAGCGGCAGGGTCGGAACAGGAGAGCGCACGAGGGAGCTTCCAGGGGGAAACGCCTGGTATCTTTATAGTCCTGTCGGGTTTCGCCACCTCTGACTTGAGCGTCGATTTTTGTGATGCTCGTCAGGGGGGCGGAGCCTATGGAAAAACGCCAGCAACGCGGCCTTTTTACGGTTCCTGGCCTTTTGCTGGCCTTTTGCTCACATGTTCTTTCCTGCGTTATCCCCTGATTCTGTGGATAACCGTcctaggTGTAAAACGACGGCCAGTCTTAAGCTCGGGCCCCAAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCC", true);
//            templates.add(template);
//
//            String pdt = sim.run(oligo1, oligo2, templates);
//            System.out.println("\nMutagenic oligos on a circular template over the origin:");
//            System.out.println(pdt);
//        }
        
        //run a pcr with degenerate oligos
        //Run a quikchange
        //run a hybrid with 2 oligos, and both oligo and ds templates
    }
}
