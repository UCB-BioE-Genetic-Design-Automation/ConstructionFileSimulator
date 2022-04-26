package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.simulators.AssemblySimulator;
import org.ucb.c5.constructionfile.simulators.DigestSimulator;
import org.ucb.c5.constructionfile.simulators.LigateSimulator;
import org.ucb.c5.constructionfile.simulators.PCRSimulator;
import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.utils.FileUtils;
import org.ucb.c5.sequtils.RestrictionEnzymeFactory;
import java.util.*;
import org.ucb.c5.constructionfile.simulators.BluntingSimulator;
import org.ucb.c5.utils.Log;

public class SimulateConstructionFile {


    public void initiate() {
    }

    public Polynucleotide run(ConstructionFile CF, Map<String, Polynucleotide> nameToPoly) throws Exception {
        // Populate a Map with all of the intermediate polypeptides and directory-parsed sequences  
        Map<String, Polynucleotide> fragments = new HashMap<>(); 
        fragments.putAll(nameToPoly);
        
        Map<String, Polynucleotide> CFMap = CF.getSequences();
        fragments.putAll(CFMap);
        for (Step step : CF.getSteps()) {
            try {
                processStep(step, CFMap, fragments, CF.getPdtName());
            } catch(Exception err) {
                Log.severe(err.getMessage());
                throw err;
            }
            // curr = processStep(step, curr, CF.getSequences()); // Update the Map
        }
        String pdtName = CF.getPdtName();
        Polynucleotide poly = fragments.get(pdtName);
        Log.seq(pdtName, poly.getForwardStrand(), "Final product");
        Log.info(pdtName + " successfully simulated!!!");
        return poly;
    }

    // Process an individual step
    private void processStep(Step step, Map<String, Polynucleotide> CFMap, Map<String, Polynucleotide> fragments, String pdtName) throws Exception {
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
            case blunting:
                simulateBlunting((Blunting) step, fragments);
                break;
                
            default:
                throw new RuntimeException("Not implemented " + step.getOperation());
        }
        String stepPdt = step.getProduct();
        Polynucleotide pseq = CFMap.get(stepPdt);
        if(pseq == null) {
            pseq = fragments.get(stepPdt);
        }
        if(pseq == null) {
            Log.seq(stepPdt, "", "No sequence available");
        } else {
            String seq = pseq.getForwardStrand();
            Log.seq(stepPdt, seq, "Product of " + step.getOperation() + " step");
        }
    }

    // Load the specified name into the local dictionary from the global CF dictionary so the sequence can be found
    // This should be a check that there is a polypeptide in the dictionary that matches the specified name, otherwise throw error
    private void simulateAcquisition(Acquisition acquisition, Map<String, Polynucleotide> CFMap, Map<String, Polynucleotide> fragments) throws Exception{
        Log.info("Simulating Acquisition: " + acquisition.getProduct());
        String dnaName = acquisition.getProduct();
        if (CFMap.containsKey(dnaName)) {
            fragments.put(dnaName, CFMap.get(dnaName));
        }
        else {
            throw new Exception("There is an acquire statement that references a polypeptide sequence not provided in the construction file");
        }
    }

    private void simulatePCR(PCR pcr, Map<String, Polynucleotide> fragments) throws Exception{
        StringBuilder msg = new StringBuilder();
        msg.append("Simulating PCR (o1,o2,template(s),pdt): ");
        msg.append(pcr.getOligo1()).append(", ").append(pcr.getOligo2()).append(", ");
        for(String temp : pcr.getTemplates()) {
            msg.append(temp).append(", ");
        }
        msg.append(pcr.getProduct());
        Log.info(msg.toString());
        PCRSimulator PCRSimulator = new PCRSimulator();
        PCRSimulator.initiate();
        PCRSimulator.run(pcr, fragments);
    }

    private String simulatePCA(PCA pca, String curr, Map<String, String> CFMap) {
        return null;
    }

    private void simulateDigest(Digestion digestion, Map<String, Polynucleotide> fragments) throws Exception {
        //Log it
        String enzlog = "";
        for(String enz : digestion.getEnzymes()) {
            enzlog+=enz;
            enzlog+=", ";
        }
        enzlog = enzlog.substring(0, enzlog.length()-2);
        Log.info("Simulating Digestion of " + digestion.getSubstrate() + " with " + enzlog);
        
        //Digest it
        DigestSimulator digestSimulator = new DigestSimulator();
        digestSimulator.initiate();
        
        Polynucleotide product = digestSimulator.run(digestion, fragments);
        fragments.put(digestion.getProduct(), product);
    }

    private void simulateLigate(Ligation ligation, Map<String, Polynucleotide> fragments) throws Exception{
        //Log it
        String fraglog = "";
        for(String frag : ligation.getFragments()) {
            fraglog+=frag;
            fraglog+=", ";
        }
        fraglog = fraglog.substring(0, fraglog.length()-2);
        Log.info("Simulating Ligation of " + fraglog + " for product " + ligation.getProduct());

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
        //Log it
        String fraglog = "";
        for (String frag : assembly.getFragments()) {
            fraglog += frag;
            fraglog += ", ";
        }
        fraglog = fraglog.substring(0, fraglog.length()-2);
        Log.info("Simulating Assembly of " + fraglog + " for product " + assembly.getProduct());

        AssemblySimulator AssemblySimulator = new AssemblySimulator();
        AssemblySimulator.initiate();
        Polynucleotide assemblyProduct = AssemblySimulator.run(assembly, fragments);
        fragments.put(assembly.getProduct(), assemblyProduct);
    }

    private void simulateTransform(Transformation transformation, Map<String, Polynucleotide> fragments, String pdtName) {
        Log.info("Simulating Transformation of " + transformation.getProduct());
        fragments.put(pdtName, fragments.get(transformation.getDna()));
    }
    
    private void simulateBlunting(Blunting blunting, Map<String, Polynucleotide> fragments) throws Exception{
        Log.info("Simulating " + blunting.getTypes() + " Blunting of " + blunting.getSubstrate() + " for product " + blunting.getProduct());
        
        BluntingSimulator bluntSimulator = new BluntingSimulator();
        bluntSimulator.initiate();
        BluntingType type = BluntingType.valueOf(blunting.getTypes());
        Polynucleotide substrate = fragments.get(blunting.getSubstrate());
        Polynucleotide bluntingProduct = bluntSimulator.run(substrate, type);
        fragments.put(blunting.getProduct(), bluntingProduct);
    }
    
    public static void main(String[] args) throws Exception {
        //Parse an example file
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        String text = FileUtils.readResourceFile("constructionfile/data/Construction of aspC1.txt");
        ConstructionFile cf = pCF.run(text);
        
        //Simulate the construction file
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        Polynucleotide product = simulateConstructionFile.run(cf, new HashMap<>());
        System.out.println(product);
    }
}
