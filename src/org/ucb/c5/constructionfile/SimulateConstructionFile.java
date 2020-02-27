package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.simulators.AssemblySimulator;
import org.ucb.c5.constructionfile.simulators.DigestSimulator;
import org.ucb.c5.constructionfile.simulators.LigateSimulator;
import org.ucb.c5.constructionfile.simulators.PCRSimulator;
import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.utils.FileUtils;
import org.ucb.c5.utils.RestrictionEnzymeFactory;

import java.io.File;
import java.util.*;

public class SimulateConstructionFile {
    public static void main(String[] args) throws Exception {
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        String CFPath = System.getProperty("user.dir") + "/src/org/ucb/c5/constructionfile/data/pcrtest.txt";
        File CFFile = new File(CFPath);
        String text = FileUtils.readFile(CFFile.getAbsolutePath());
        ConstructionFile cf = pCF.run(text);
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        Polynucleotide product = simulateConstructionFile.run(cf);
        System.out.println(product);
    }

    public void initiate() {
    }

    public Polynucleotide run(ConstructionFile CF) throws Exception {
        Map<String, String> CFMap = CF.getSequences();
        Map<String, Polynucleotide> fragments = new HashMap<>(); // Map with all of the intermediate polypeptides
        for (Step step : CF.getSteps()) {
            processStep(step, CFMap, fragments, CF.getPdtName());
            // curr = processStep(step, curr, CF.getSequences()); // Update the Map
        }
        String PdtName = CF.getPdtName();
        return fragments.get(PdtName);
    }

    // Process an individual step
    private void processStep(Step step, Map<String, String> CFMap, Map<String, Polynucleotide> fragments, String pdtName) throws Exception {
        switch (step.getOperation()) {
            case acquire:
                simulateAcquisition((Acquisition) step, CFMap, fragments);
                break;
            case pca:
                break; // simulatePCA(step, CFMap, fragments);
            case pcr:
                simulatePCR((PCR) step, fragments);
                break;
            case digest:
                simulateDigest((Digestion) step, fragments);
                break;
            case ligate:
                simulateLigate((Ligation) step, fragments);
                break;
            case assemble:
                simulateAssemble((Assembly) step, fragments);
                break;
//            case cleanup:
//                simulateCleanup((Cleanup) step, fragments);
//                break;
            case transform:
                simulateTransform((Transformation) step, fragments, pdtName);
                break;
//            case miniprep:
//                break; // simulateMiniprep(step, CFMap, fragments);
//            case sequence:
//                break; // simulateSequence(step, CFMap, fragments);
//            case inoculate:
//                break; // simulateInoculate(step, CFMap, fragments);
            default:
                throw new RuntimeException("Not implemented " + step.getOperation());
        }
    }

    // Load the specified name into the local dictionary from the global CF dictionary so the sequence can be found
    // This should be a check that there is a polypeptide in the dictionary that matches the specified name, otherwise throw error
    private void simulateAcquisition(Acquisition acquisition, Map<String, String> CFMap, Map<String, Polynucleotide> fragments) throws Exception{
        String dnaName = acquisition.getProduct();
        if (CFMap.containsKey(dnaName)) {
            fragments.put(dnaName, new Polynucleotide(CFMap.get(dnaName)));
        }
        else {
            throw new Exception("There is an acquire statement that references a polypeptide sequence not provided in the construction file");
        }
    }

    private void simulatePCR(PCR pcr, Map<String, Polynucleotide> fragments) throws Exception{
        PCRSimulator PCRSimulator = new PCRSimulator();
        PCRSimulator.run(pcr, fragments);
    }

    private String simulatePCA(PCA pca, String curr, Map<String, String> CFMap) {
        return null;
    }

    private void simulateDigest(Digestion digestion, Map<String, Polynucleotide> fragments) throws Exception {
        DigestSimulator digestSimulator = new DigestSimulator();
        digestSimulator.initiate();
        Polynucleotide substrate = fragments.get(digestion.getSubstrate());
        List<Enzyme> enzymeList = digestion.getEnzymes();
        String productName = digestion.getProduct();

        RestrictionEnzymeFactory rezfactory = new RestrictionEnzymeFactory();
        rezfactory.initiate();
        List<RestrictionEnzyme> restrictionEnzymeList = new ArrayList<>();
        for (Enzyme enzname: enzymeList) {
            restrictionEnzymeList.add(rezfactory.run(enzname));
        }
        List<Polynucleotide> products = digestSimulator.run(substrate, restrictionEnzymeList);
        // Figure out which of the products is the desired one and save in fragments
        int longest = 0;
        Polynucleotide poly = null;
        for (Polynucleotide polyTemp : products) {
            if (polyTemp.getSequence().length() > longest) {
                longest = polyTemp.getSequence().length();
                poly = polyTemp;
            }
        }
        if (poly != null) {
            fragments.put(productName, poly);
        }
        else {
            throw new Exception("There were no products generated from the digestion reaction");
        }
    }

    private void simulateLigate(Ligation ligation, Map<String, Polynucleotide> fragments) throws Exception{
        LigateSimulator LigateSimulator = new LigateSimulator();
        LigateSimulator.initiate();
        List<Polynucleotide> polys = new ArrayList<>();
        for (String poly: ligation.getFragments()) {
            polys.add(fragments.get(poly));
        }
        Polynucleotide ligProduct = LigateSimulator.run(polys);
        fragments.put(ligation.getProduct(), ligProduct);
    }

    private void simulateAssemble(Assembly assembly, Map<String, Polynucleotide> fragments) throws Exception {
        AssemblySimulator AssemblySimulator = new AssemblySimulator();
        AssemblySimulator.initiate();
        Polynucleotide assemblyProduct = AssemblySimulator.run(assembly, fragments);
        fragments.put(assembly.getProduct(), assemblyProduct);
    }

    // This is not implemented completely, right now it will just transfer the sequence to the product name in the map
//    private void simulateCleanup(Cleanup cleanup, Map<String, Polynucleotide> fragments) {
//        fragments.put(cleanup.getProduct(), fragments.get(cleanup.getSubstrate()));
//    }

    private void simulateTransform(Transformation transformation, Map<String, Polynucleotide> fragments, String pdtName) {
        fragments.put(pdtName, fragments.get(transformation.getDna()));
    }
}
