package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.sequtils.RestrictionEnzymeFactory;
import org.ucb.c5.constructionfile.model.Assembly;
import org.ucb.c5.constructionfile.model.Enzyme;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
import org.ucb.c5.sequtils.RevComp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssemblySimulator {
    DigestSimulator digestSimulator;
    LigateSimulator ligateSimulator;
    RevComp revcomp;
            
    public void initiate() throws Exception {
            // Create digest and ligate simulators
            digestSimulator = new DigestSimulator();
            digestSimulator.initiate();
            ligateSimulator = new LigateSimulator();
            ligateSimulator.initiate();
            revcomp = new RevComp();
            revcomp.initiate();
    }

    public Polynucleotide run(Assembly assembly, Map<String, Polynucleotide> fragments) throws Exception{
        List<String> assemblyNames = assembly.getFragments();
        List<Polynucleotide> assemblyFragments = new ArrayList<>();
        for (String name : assemblyNames) {
            assemblyFragments.add(fragments.get(name));
        }
        Enzyme enzyme = assembly.getEnzyme();
        if (enzyme == Enzyme.BsmBI || enzyme == Enzyme.BsaI) {
            //Find all the BsaI site-free fragments of the digestion products of each sequence using the given enzyme and put into a list
            List<Polynucleotide> validFrags = new ArrayList<>();
            List<RestrictionEnzyme> enzymeList = new ArrayList<>();
            RestrictionEnzyme res = new RestrictionEnzymeFactory().run(enzyme);
            enzymeList.add(res);
            String ressite = res.getSite();
            String rcsite = revcomp.run(ressite);
            
            for (Polynucleotide assemblyFrag : assemblyFragments) {
                List<Polynucleotide> digestFrags = digestSimulator.run(assemblyFrag, enzymeList);
                for (Polynucleotide polyTemp : digestFrags) {
                    String seq = polyTemp.getSequence();
                    if(seq.contains(ressite)) {
                        continue;
                    }
                    if(seq.contains(rcsite)) {
                        continue;
                    }
                    validFrags.add(polyTemp);
                }
            }
            
            // Ligate the valid fragments together
            Polynucleotide ligationProduct;
            ligationProduct = ligateSimulator.run(validFrags);
            return ligationProduct;
        }
        // Test multiple fragment assembly
        // Test homologous region is on reverse comp
        // Test common errors and throw errors correctly (doesn't recombine correctly, 20 bp are revcomp)
        else if (enzyme == Enzyme.Gibson) { // Gibson
            RevComp revComp = new RevComp();
            revComp.initiate();
            while (assemblyFragments.size() > 1) {
                Polynucleotide currFrag = assemblyFragments.remove(0);
                Polynucleotide matchedFrag = null;
                int currHomologousRegionStartIndex; // The index of the beginning of the homologous region on the current strand
                int matchedHomologousRegionStartIndex; // The index of the beginning of the homologous region on the matched strand
                int matchedHomologousRegionEndIndex = 0; // Index on matchedFrag of the end of the homologous region (including 20 bp)
                for (Polynucleotide tempFrag : assemblyFragments) {
                    int currLen = currFrag.getSequence().length();
                    // If there is a match between the last 20 bp of currFrag and the forward strand of tempFrag
                    if (tempFrag.getSequence().contains(currFrag.getSequence().substring(currLen-20))) {
                        matchedFrag = tempFrag;
                        matchedHomologousRegionEndIndex = 20 + tempFrag.getSequence().indexOf(currFrag.getSequence().substring(currLen-20));
                        assemblyFragments.remove(tempFrag);
                        break;
                    }
                    // Else if there is a match between the last 20 bp of currFrag and the backwards strand of tempFrag
                    else if (revComp.run(tempFrag.getSequence()).contains(currFrag.getSequence().substring(currLen-20))) {
                        matchedFrag = new Polynucleotide(revComp.run(tempFrag.getSequence()));
                        matchedHomologousRegionEndIndex = 20 + revComp.run(tempFrag.getSequence()).indexOf(currFrag.getSequence().substring(currLen-20));
                        assemblyFragments.remove(tempFrag);
                        break;
                    }
                }
                if (matchedFrag == null) {
                    throw new Exception("The provided assembly fragments cannot be joined together because there are not enough homologous regions between them");
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
            String lastSixBP = forwardStrand.substring(forwardStrand.length()-20);
            int firstIndex = forwardStrand.indexOf(lastSixBP);
            if (firstIndex == forwardStrand.length()-20 || firstIndex < 0) {
                throw new Exception("Assembly product cannot be re-circularized");
            }
            Polynucleotide circularProduct = new Polynucleotide(forwardStrand.substring(firstIndex, forwardStrand.length()-20), true);
            return circularProduct;
        }
        else {
            throw new Exception("The given enzyme cannot be used for an assembly reaction");
        }
    }
}
