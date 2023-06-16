package org.ucb.c5.constructionfile;

import java.util.*;


import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.genbank.AutoAnnotate;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Experiment;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.utils.Log;

/**
 *
 * @author J. Christopher Anderson
 * @author sanjyotbakshi
 */
public class SimulateExperimentDirectory {

    private ParseExperimentDirectory parseFolder;
    private SimulateConstructionFile simulator;
    private AutoAnnotate autoanno;

    public void initiate() throws Exception {
        parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        simulator = new SimulateConstructionFile();
        simulator.initiate();
        autoanno = new AutoAnnotate();
        autoanno.initiate();
    }

    private List<ConstructionFile> orderOfExecution(List<ConstructionFile> inlist, Set<String> nameList) {
        List<ConstructionFile> cfOut = new ArrayList<ConstructionFile>();
        Queue<ConstructionFile> workQueue = new LinkedList<>();


        for (ConstructionFile cfile : inlist) {
            workQueue.add(cfile);
            nameList.remove(cfile.getPdtName());
        }

        Integer numberOfCFs = inlist.size();
        Integer oldCount = workQueue.size();
        Integer count = 0;
        StringBuilder missingInput = new StringBuilder();
            outer: while (!workQueue.isEmpty()) {

                count ++;
                if (count > oldCount) {
                    String finalMissingInput = missingInput.toString();

                    if (numberOfCFs == 1){
                        throw new IllegalArgumentException("The following input sequences are missing: " + finalMissingInput);
                    }
                    else if (numberOfCFs > 1){
                        throw new IllegalArgumentException("The construction files order cannot be determined because the following input sequences are missing or the order of construction files is cyclic: " + finalMissingInput);
                    }

                }
                ConstructionFile cfile = workQueue.poll();

                List<String> cfileInputs = new ArrayList<String>();
                for(Step stp : cfile.getSteps()) {
                    cfileInputs.addAll(stp.getInputs());
                }
                for(Step stp : cfile.getSteps()) {
                    cfileInputs.remove(stp.getProduct());
                }
                for (String input : cfileInputs) {
                    if (!nameList.contains(input)) {
                        missingInput.append(input + " ");
                        workQueue.add(cfile);
                        continue outer;
                    }
                }

                    cfOut.add(cfile);
                    nameList.add(cfile.getPdtName());
                    oldCount = workQueue.size();
                    count = 0;
            }

                return cfOut;

    }

    public Experiment run(Experiment exp) throws Exception {
        List<ConstructionFile> cfsFromExperiment = exp.getCfs();
        Set<String> onlySeqNames = new HashSet<String>();
        onlySeqNames.addAll(exp.getNameToPoly().keySet()) ;

        //Making members of a new experiment object to return
        String productofExperiment = exp.getName();
        List<ConstructionFile> productExperimentConstructionFiles = new ArrayList<ConstructionFile>();
        Map<String,Polynucleotide> productExperimentSeqs = new HashMap<>();

        List<ConstructionFile> finalSortedConstructionFilesFromExperiment = orderOfExecution(cfsFromExperiment, onlySeqNames);
        List <String> finalProductSequenceList = new ArrayList<String>();
        
        //Run the simulator
        for (ConstructionFile cf : finalSortedConstructionFilesFromExperiment) {
            Log.info("Simulating construction file for product: " + cf.getPdtName());
            ConstructionFile outputConstructionFile = simulator.run(cf, exp.getNameToPoly());
            productExperimentConstructionFiles.add(outputConstructionFile);
            Polynucleotide pdt = outputConstructionFile.getSequences().get(outputConstructionFile.getPdtName());
            autoanno.run(pdt.getSequence(), cf.getPdtName() + ".seq");
            finalProductSequenceList.add(cf.getPdtName());
            productExperimentSeqs.putAll(outputConstructionFile.getSequences());
        }

        Experiment productExperiment = new Experiment(productofExperiment, productExperimentConstructionFiles, productExperimentSeqs);

        for (String productName : finalProductSequenceList) {
            System.out.println("The products from experiment after running the construction files are:" + productName);
        }
        return productExperiment;

    }

    public static void main(String[] args) throws Exception {
        //Enter Path name as a String
        String dirPath = "/Path/To/Your/Experiment/folder";

        //ENTERS PATH TO FOLDER OF THE EXPERIMENT DIRECTLY WITHOUT THE GUI
        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        Experiment exp = parseFolder.run(dirPath);

        //Simulate the experiment and write results
        SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
        sed.initiate();
        Experiment simulatedExperiment = sed.run(exp);
    }
}
