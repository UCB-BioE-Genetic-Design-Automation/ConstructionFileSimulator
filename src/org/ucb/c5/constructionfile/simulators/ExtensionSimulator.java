/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

/**
 * This Function simulates the process of two oligonucleotides annealing into
 * a complex that can function as a substrate of polymerase extension.
 * 
 * @author J. Christopher Anderson
 */
public class ExtensionSimulator {
    
    private final double minTm = 40.0;  //degrees in celsius
    private final double maxTm = 55.0;  //degrees in celsius
    

    public void initiate() throws Exception {

    }

    /**
     * Inputs an oligo and a single-stranded template (ssDNA, all caps, 5'-->3') 
     * and returns the index on the template for the optimal positioning of the 
     * 3' end of the oligo
     * 
     * Returns -1 if there are no annealing site with Tm >40degC
     * 
     * Throws an Exception if more than one annealing site >55degC is present
     * 
     * Otherwise returns the highest-Tm site
     * 
     * @param oligo
     * @param template
     * @return
     * @throws Exception 
     */
    public int run(String oligo, String template) throws Exception {
        //Find all sites in template exactly matching the last 6 bp of the oligo
        
        //For each 6bp match, use JAligner to align the oligo to the ~30bp window
        //terminating at 3' terminus
        //Determine which of the 6bp sites has the highest Tm, and consider
        //the two temperature constraints
        
        //Return the position on the template for the 3' end of the oligo
        
        return -1;
    }

    public static void main(String[] args) throws Exception {
        ExtensionSimulator sim = new ExtensionSimulator();
        sim.initiate();
        {
            System.out.println("Correct duplex of 22 mers");
            int result = sim.run("GTATCACGAGGCAGAATTTCAG", "CTGAAATTCTGCCTCGTGATAC");
            System.out.println(result);
        }
        {
            System.out.println("Mismatched 22 mers");
            int result = sim.run("CTCCTTCGGTCCTCCGATCGTT", "CTGAAATTCTGCCTCGTGATAC");
            System.out.println(result);
        }
        {
            System.out.println("Mismatched 22 mers");
            int result = sim.run("GGATCCCGTGGCAGTATTTAAG", "CTGAAATTCTGCCTCGTGATAC");
            System.out.println(result);
        }
    }
}
