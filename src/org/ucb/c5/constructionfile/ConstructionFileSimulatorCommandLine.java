package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.Experiment;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author Sanjyot Bakshi
 */

@Command(name = "cfs", mixinStandardHelpOptions = true, version = "cfs v1.0",description = "Simulates recombinant DNA experiments")
class ConstructionFileSimulatorCommandLine implements Callable<Integer> {

    @Option(names = {"-i", "--experimentPath"}, description = "Enter the experiment directory path.")
    private String experimentPath = "";

    @Option(names = {"-c", "--cfPath"}, description = "Enter the construction file path.")
    private String cfPath = "";

    @Option(names = {"-s", "--seqPath"}, split=",", description = "Enter one sequence file path or a comma separated list of sequence file paths.")
//    private String[] seqPath = new String[]{};
    private List<String> seqPath = new ArrayList<>();


    @Override
    public Integer call() throws Exception {

        if (!experimentPath.isEmpty()){
            System.out.println("Starting simulation");
            ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
            parseFolder.initiate();
            Experiment exp = parseFolder.run(experimentPath);

            SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
            sed.initiate();
            sed.run(exp);
            System.out.println("Simulation completed");
            return 0;
        }


//        Below code for separate cfPath and seqPath
        if (!cfPath.isEmpty() && !seqPath.isEmpty()){
            System.out.println("Starting simulation");
            ParseExperimentDirectory parseFolder2 = new ParseExperimentDirectory();
            parseFolder2.initiate();
            Experiment exp2 = parseFolder2.run(cfPath, seqPath);

            SimulateExperimentDirectory sed2 = new SimulateExperimentDirectory();
            sed2.initiate();
            sed2.run(exp2);
            System.out.println("Simulation completed");
            return 0;
        }


        return 0;
    }


    public static void main(String[] args){
        int exitCode = new CommandLine(new ConstructionFileSimulatorCommandLine()).execute(args);
        System.exit(exitCode);
    }
}
