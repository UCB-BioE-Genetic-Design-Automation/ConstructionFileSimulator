/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.genbank.AutoAnnotate;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Experiment;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.utils.Log;

/**
 *
 * @author J. Christopher Anderson
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

    public void run(String dirPath) throws Exception {
        //Parse the directory to an Experiment
        Experiment exp = parseFolder.run(dirPath);

        //Run the simulator
        for (ConstructionFile cf : exp.getCfs()) {
            Log.info("Simulating construction file for product: " + cf.getPdtName());
            Polynucleotide pdt = simulator.run(cf, exp.getNameToPoly());
            autoanno.run(pdt.getSequence(), cf.getPdtName() + ".seq");
        }
    }

    public static void main(String[] args) throws Exception {
        //Enter Path name as a String
        String dirPath = "/Users/jca20n/Pimar/experiments/Lycopene6";

        //Simulate the experiment and write results
        SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
        sed.initiate();
        sed.run(dirPath);
    }
}
