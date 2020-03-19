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
import org.ucb.c5.constructionfile.model.Oligo;
import org.ucb.c5.sequtils.CalcEditDistance;

/**
 *
 * @author J. Christopher Anderson
 */
public class OligoFinder {
    private OligoScanner scanner;
    private CalcEditDistance distancer;
    
    public void initiate() throws Exception {
        scanner = new OligoScanner();
        scanner.initiate();
        distancer = new CalcEditDistance();
        distancer.initiate();
    }
    
    public List<Oligo> run(String sequence) throws Exception {
        String template = sequence.toUpperCase();
        List<Oligo> out = new ArrayList<>();
        Map<String,Oligo> oligos = scanner.run();
        for(String name : oligos.keySet()) {
            Oligo oligo = oligos.get(name);
            String seq = oligo.getSequence();
            String last6 = seq.substring(seq.length()-6);
            
            if(seq.contains("ATGAGTGGATAGTACGTTGC")) {
                System.out.println();
            }
            
            int length = 20;
            int start = 0;
            if(seq.length()>length) {
                start = seq.length() - length;
            } else {
                length = seq.length();
            }
            
            int end = template.indexOf(last6, length-6);
            if(end == -1) {
                continue;
            }
            end = end + 6;
            

            String matchOligo = seq.substring(start);
            String matchTemplate = template.substring(end - length, end);
            
            if(distancer.run(matchOligo, matchTemplate) < 3) {
                out.add(oligo);
            }
        }
        return out;
        
    }
    
    public static void main(String[] args) throws Exception {
        OligoFinder finder = new OligoFinder();
        finder.initiate();
        
        List<Oligo> oligos = finder.run("agggctgcttgaacccaaaagggcggtatcaggcgattttcatgaccg"); //
        List<Oligo> oligos2 = finder.run("aaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccgg");
        
        System.out.println("oligos1");
        for(Oligo oligo : oligos) {
            System.out.println(oligo.getName() + "  " + oligo.getSequence());
        }
        
        System.out.println("oligos2");
        for(Oligo oligo : oligos2) {
            System.out.println(oligo.getName() + "  " + oligo.getSequence());
        }
    }
}
