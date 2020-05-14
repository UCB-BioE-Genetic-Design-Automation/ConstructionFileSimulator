/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * DNA by Gibson Assembly.  The algorithm designs oligonucleotides and outputs a
 * construction file.
 * 
 * @author J. Christopher Anderson
 */
public class GibsonBuilder {
    private void initiate() {
    }
    
    public ConstructionFile run(List<String> fragments) throws Exception {
        return null;
    }
    
    
    public static void main(String[] args) throws Exception {
        GibsonBuilder builder = new GibsonBuilder();
        builder.initiate();
        
        //Stich together 3 genes, write a construction file
        List<String> frags = new ArrayList<>();
        frags.add("atgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctga");
        frags.add("atgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaa");
        frags.add("atggtgagtggcagtaaagcgggcgtttcgcctcatcgcgaaatagaagtaatgagacaatccattgacgatcacctggctggcctgttacctgaaaccgacagccaggatatcgtcagccttgcgatgcgtgaaggcgtcatggcacccggtaaacggatccgtccgctgctgatgctgctggccgcccgcgacctccgctaccagggcagtatgcctacgctgctcgatctcgcctgcgccgttgaactgacccataccgcgtcgctgatgctcgacgacatgccctgcatggacaacgccgagctgcgccgcggtcagcccactacccacaaaaaatttggtgagagcgtggcgatccttgcctccgttgggctgctctctaaagcctttggtctgatcgccgccaccggcgatctgccgggggagaggcgtgcccaggcggtcaacgagctctctaccgccgtgggcgtgcagggcctggtactggggcagtttcgcgatcttaacgatgccgccctcgaccgtacccctgacgctatcctcagcaccaaccacctcaagaccggcattctgttcagcgcgatgctgcagatcgtcgccattgcttccgcctcgtcgccgagcacgcgagagacgctgcacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtcaccggtcatgaaaatcgcctga");
        ConstructionFile cf = builder.run(frags);
        
        //Simulate the construction file
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        simulateConstructionFile.initiate();
        Polynucleotide result = simulateConstructionFile.run(cf, new HashMap<>());
        System.out.println(result.getSequence());        
    }
}
