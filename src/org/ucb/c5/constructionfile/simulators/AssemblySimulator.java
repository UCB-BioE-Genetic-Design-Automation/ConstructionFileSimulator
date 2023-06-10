package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.sequtils.RestrictionEnzymeFactory;
import org.ucb.c5.constructionfile.model.Assembly;
//import org.ucb.c5.constructionfile.model.Enzyme;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
import org.ucb.c5.sequtils.RevComp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.SimulateConstructionFile;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Modifications;
import org.ucb.c5.utils.FileUtils;

public class AssemblySimulator {

    DigestSimulator digestSimulator;
    LigateSimulator ligateSimulator;
    RestrictionEnzymeFactory resEnzFactory;
    RevComp revcomp;

    public void initiate() throws Exception {
        // Create digest and ligate simulators
        digestSimulator = new DigestSimulator();
        digestSimulator.initiate();
        ligateSimulator = new LigateSimulator();
        ligateSimulator.initiate();
        resEnzFactory = new RestrictionEnzymeFactory();
        resEnzFactory.initiate();
        revcomp = new RevComp();
        revcomp.initiate();
    }

    public Polynucleotide run(Assembly assembly, Map<String, Polynucleotide> fragments) throws Exception {
        List<String> assemblyNames = assembly.getFragments();
        List<Polynucleotide> assemblyFragments = new ArrayList<>();
        for (String name : assemblyNames) {
            assemblyFragments.add(fragments.get(name));
        }
        //Changed
        String enzyme = assembly.getEnzyme();
        if (enzyme.equals("Gibson")) { // Gibson
            return simGibson(assemblyFragments); 
        }
        
// Test multiple fragment assembly
        // Test homologous region is on reverse comp
        // Test common errors and throw errors correctly (doesn't recombine correctly, 20 bp are revcomp) 
        return simGoldenGate(enzyme, assemblyFragments);
    }

    private Polynucleotide simGoldenGate(String enzyme, List<Polynucleotide> assemblyFragments) throws Exception {
        //Retrieve the enzyme
        List<RestrictionEnzyme> enzymeList = new ArrayList<>();
        RestrictionEnzyme res = resEnzFactory.run(enzyme);
        enzymeList.add(res);

        //Digest all the fragments
        List<Polynucleotide> allDigFrags = new ArrayList<>();
        for (Polynucleotide assemblyFrag : assemblyFragments) {
            List<Polynucleotide> digPdts = digestSimulator.run(assemblyFrag, enzymeList);
            allDigFrags.addAll(digPdts); //Assumes 3 fragments, with the correct one in the middle
        }
        
        //Eliminate all but ones with sticky ends and 5' phosphates
        List<Polynucleotide> stickyFrags = new ArrayList<>();
        for(Polynucleotide frag : allDigFrags) {
            //Check for their being sticky ends on both sides
            if(frag.getExt5()==null || frag.getExt5().isEmpty()) {
                continue;
            }
            if(frag.getExt3()==null || frag.getExt3().isEmpty()) {
                continue;
            }
            
            //Check for their being 5' phosphates
            if(frag.getMod5()!=Modifications.phos5) {
                continue;
            }
            if(frag.getMod3()!=Modifications.phos5) {
                continue;
            }
            stickyFrags.add(frag);
        }
        
        //Validate that all ends are non-palindromic
        for(Polynucleotide frag : stickyFrags) {
            if(isPalindromic(frag.getExt5().replaceAll("-", ""))) {
                throw new IllegalArgumentException("Fragment is pandindromic: " + frag.getExt5() + " in " + frag.toString());
            }
        }

        // Ligate the valid fragments together
        Polynucleotide ligationProduct;
        ligationProduct = ligateSimulator.run(stickyFrags);
        return ligationProduct;
    }


    /**
     * Determines whether a given DNA sequence is palindromic.
     * A palindromic DNA sequence is one that reads the same forward and backward when complemented.
     * This function also checks for invalid characters in the input sequence and throws an exception if any are found.
     *
     * @param seq The input DNA sequence to be checked for palindromicity.
     * @return Returns true if the input DNA sequence is palindromic, false otherwise.
     * @throws IllegalArgumentException Throws an error if the input sequence contains characters other than A, T, C, or G.
     *
     * Usage:
     *   boolean result = isPalindromic("AATT");
     *   System.out.println(result); // Output: true
     *
     * Example:
     *   1. isPalindromic("AATT") returns true
     *   2. isPalindromic("AGCT") returns false
     *   3. isPalindromic("GAATTC") returns true
     *   4. isPalindromic("AATN") throws an error (invalid character 'N')
     */
    private boolean isPalindromic(String seq) throws IllegalArgumentException {
        seq = seq.toUpperCase();
        
        // Check for invalid characters and throw an exception if found
        for (char nucleotide : seq.toCharArray()) {
            if (nucleotide != 'A' && nucleotide != 'T' && nucleotide != 'C' && nucleotide != 'G') {
                throw new IllegalArgumentException("Error: Invalid character '" + nucleotide + "' found in sequence '" + seq + "'. Sequence must contain only A, T, C, or G.");
            }
        }

        String rc = revcomp.run(seq);
        return seq.equals(rc);
    }

    private Polynucleotide simGibson(List<Polynucleotide> assemblyFragments) throws Exception {
        RevComp revComp = new RevComp();
        revComp.initiate();
        while (assemblyFragments.size() > 1) {
            Polynucleotide currFrag = assemblyFragments.remove(0);
            int currLen = currFrag.getSequence().length(); //* declared currLen outside of loop 
            String homologyRegion = currFrag.getSequence().substring(currLen - 20); //can now use currLen
            Polynucleotide matchedFrag = null;
            int currHomologousRegionStartIndex; // The index of the beginning of the homologous region on the current strand
            int matchedHomologousRegionStartIndex; // The index of the beginning of the homologous region on the matched strand
            int matchedHomologousRegionEndIndex = 0; // Index on matchedFrag of the end of the homologous region (including 20 bp)
            for (Polynucleotide tempFrag : assemblyFragments) {
                //int currLen = currFrag.getSequence().length(); moved this outside of the for loop

                // If there is a match between the last 20 bp of currFrag and the forward strand of tempFrag
                if (tempFrag.getSequence().contains(homologyRegion)) {
                    matchedFrag = tempFrag;
                    matchedHomologousRegionEndIndex = 20 + tempFrag.getSequence().indexOf(currFrag.getSequence().substring(currLen - 20));
                    assemblyFragments.remove(tempFrag);
                    break;
                } // Else if there is a match between the last 20 bp of currFrag and the backwards strand of tempFrag
                else if (revComp.run(tempFrag.getSequence()).contains(currFrag.getSequence().substring(currLen - 20))) {
                    matchedFrag = new Polynucleotide(revComp.run(tempFrag.getSequence()));
                    matchedHomologousRegionEndIndex = 20 + revComp.run(tempFrag.getSequence()).indexOf(currFrag.getSequence().substring(currLen - 20));
                    assemblyFragments.remove(tempFrag);
                    break;
                }
            }
            if (matchedFrag == null) {
                throw new Exception("The provided assembly fragments cannot be joined together because there are not enough homologous regions between them");
            }
            //added if statement to catch degenerate base pairs 
           if (!homologyRegion.matches("[ATCG]+")) {
               throw new IllegalArgumentException("The provided assembly contains degenerate base pairs, assembly failed. ");
           }
            currHomologousRegionStartIndex = currFrag.getSequence().length() - matchedHomologousRegionEndIndex;
            String currHomologousRegion = currFrag.getSequence().substring(currHomologousRegionStartIndex);
            String matchedHomologousRegion = matchedFrag.getSequence().substring(0, matchedHomologousRegionEndIndex);
            if (!(currHomologousRegion.equals(matchedHomologousRegion))) {
                throw new Exception("In a Gibson assembly step, the fragment ends do not match");
            }
            /* // Find start indices of homologous region on both strands
             currHomologousRegionStartIndex = currFrag.getSequence().length() - 1 - 6;
             matchedHomologousRegionStartIndex = matchedHomologousRegionEndIndex;
             while (currHomologousRegionStartIndex > 0 && matchedHomologousRegionStartIndex > 0 &&
             currFrag.getSequence().charAt(currHomologousRegionStartIndex) == matchedFrag.getSequence().charAt(matchedHomologousRegionStartIndex)) {
             currHomologousRegionStartIndex--;
             matchedHomologousRegionStartIndex--;
             } */
            // Sanity Check: currHomologousRegionStartIndex now points to the index on the current fragment that heads the
            // homologous region, matchedHomologousRegionStartIndex now points to the index on the matched fragment that heads
            // the homologous region, and matchedHomologousRegionEndIndex now points to the last index (including 6 bp matching seq)
            // of the homologous region on the matching strand
            String currFragRegion = currFrag.getSequence().substring(0, currHomologousRegionStartIndex);
            String matchedFragRegion = matchedFrag.getSequence();
            Polynucleotide assembledProduct = new Polynucleotide(currFragRegion.concat(matchedFragRegion));
            assemblyFragments.add(assembledProduct); 
        }
        Polynucleotide linearProduct = assemblyFragments.remove(0);
        String forwardStrand = linearProduct.getSequence();
        String lastSixBP = forwardStrand.substring(forwardStrand.length() - 20);
        int firstIndex = forwardStrand.indexOf(lastSixBP);
        if (firstIndex == forwardStrand.length() - 20 || firstIndex < 0) {
            throw new Exception("Assembly product cannot be re-circularized");
        }
        Polynucleotide circularProduct = new Polynucleotide(forwardStrand.substring(firstIndex, forwardStrand.length() - 20), true);
        return circularProduct;
    }

    public static void main(String[] args) throws Exception {
        //JBAG BsmBI golden gate example
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();

        String text = FileUtils.readResourceFile("constructionfile/data/Construction of pTarg2.txt");
        ConstructionFile CF = pCF.run(text);
  
  
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        ConstructionFile outputConstructionFile = simulateConstructionFile.run(CF, new HashMap<>());
        Polynucleotide product = outputConstructionFile.getSequences().get(outputConstructionFile.getPdtName());
        System.out.println(product.getSequence());
    }
}
