package org.ucb.c5.constructionfile;

import org.junit.Test;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Experiment;
import org.ucb.c5.constructionfile.model.Polynucleotide;

import java.util.*;

public class CFOrderExecutionTest {


    @Test
    /**
     * This is a simple linear example, output from file1 is used as input to file2 and so on
     */
    public void linearCFExample() throws Exception {

        String dirPath = "/test/org/ucb/C5/constructionfile/data/CF_order_execution_test_1";
        String projectDir = System.getProperty("user.dir");
        String newPath = projectDir + dirPath;

        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
//        Experiment exp = parseFolder.run(dirPath);
        Experiment exp = parseFolder.run(newPath);

        SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
        sed.initiate();
        Experiment simulatedExperiment = sed.run(exp);
        List<String> orderedPdtNamesActual = new ArrayList<>();
        for (ConstructionFile consFile : simulatedExperiment.getCfs()){
            orderedPdtNamesActual.add(consFile.getPdtName());
        }

        System.out.println(orderedPdtNamesActual);

        List<String> orderedPdtNamesExpected = new ArrayList<>();
        orderedPdtNamesExpected.add("pt4crRNA-1r");
        orderedPdtNamesExpected.add("pt4crRNA");
        orderedPdtNamesExpected.add("pCBA-1");

        System.out.println(orderedPdtNamesExpected);
        assert (orderedPdtNamesExpected.equals(orderedPdtNamesActual));

    }


    @Test
    /**
     * This is a branch construction file example, output from file1 is used as input to file2 and file 3
     */
    public void branchCFExample() throws Exception {

//        String dirPath = "/Users/sanjyotbakshi/Documents/Capstone_project/CF_order_execution_test_2";

        String dirPath = "/test/org/ucb/C5/constructionfile/data/CF_order_execution_test_2";
        String projectDir = System.getProperty("user.dir");
        String newPath = projectDir + dirPath;


        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        Experiment exp = parseFolder.run(newPath);

        SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
        sed.initiate();
//        List<String> orderedPdtNamesActual = sed.run(exp);
        Experiment simulatedExperiment = sed.run(exp);
        List<String> orderedPdtNamesActual = new ArrayList<>();
        for (ConstructionFile consFile : simulatedExperiment.getCfs()){
            orderedPdtNamesActual.add(consFile.getPdtName());
        }

        String pdtNameFirstCFActual = orderedPdtNamesActual.get(0);

        String pdtNameFirstCFExpected = "pLys22";

        assert (pdtNameFirstCFExpected.equals(pdtNameFirstCFActual));

    }



    @Test
    /**
     * This is a circular example, output from file1 is used as input to file2 and vice a versa,
     * so it should throw an exception
     */
    public void circularCFExample() throws Exception {

//        String dirPath = "/Users/sanjyotbakshi/Documents/Capstone_project/CF_order_execution_test_3";

        String dirPath = "/test/org/ucb/C5/constructionfile/data/CF_order_execution_test_3";
        String projectDir = System.getProperty("user.dir");
        String newPath = projectDir + dirPath;


        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        Experiment exp = parseFolder.run(newPath);

        SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
        sed.initiate();

        try {
            Experiment simulatedExperiment = sed.run(exp);
            assert (false);
        } catch (IllegalArgumentException e) {
            assert (true);
        }

    }
}
