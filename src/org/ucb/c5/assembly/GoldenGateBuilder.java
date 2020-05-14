package org.ucb.c5.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.SimulateConstructionFile;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Polynucleotide;

/**
 * A Function that inputs a List of DNA fragments to be assembled into a circular
 * DNA by Golden Gate Assembly.  The algorithm designs oligonucleotides and outputs a
 * construction file.
 * 
 * @author J. Christopher Anderson
 */
public class GoldenGateBuilder {
    private void initiate() {
    }
    
    public ConstructionFile run(List<String> fragments) throws Exception {
        //Choose the 4bp ends
            //Iterate through the fragments, load a List with the options for the
            //4bp for each fragment
            //Sort the lists by order (palindromes bad, gc content, etc)
            //If the first choice in a list is the same as that of another list,
                //pick the one where the better of the next choices is chosen instead on one
        //Chooose the annealing region
            //Find the 3' anneal of each oligo using considerations of GC content, repetition, etc
        
        //Reverse complement the reverse oligos
        //Add tails, BsaI sites, and a spacer
        //Build a ConstructionFile
            //Add acquire for all the oligos
            //Inject names into the PCR steps, one PCR for each fragment
            //Inject names into the Assembly step
            //Rest is boilerplate
       
        return null;
    }
    
    
    public static void main(String[] args) throws Exception {
        GoldenGateBuilder builder = new GoldenGateBuilder();
        builder.initiate();
        
        //Stich together 3 genes, write a construction file
        List<String> frags = new ArrayList<>();
        frags.add("atgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaa");
        frags.add("ggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttga");
        frags.add("tgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctga");
        ConstructionFile cf = builder.run(frags);
        
        //Simulate the construction file
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        simulateConstructionFile.initiate();
        Polynucleotide result = simulateConstructionFile.run(cf, new HashMap<>());
        System.out.println(result.getSequence());        
    }
}
