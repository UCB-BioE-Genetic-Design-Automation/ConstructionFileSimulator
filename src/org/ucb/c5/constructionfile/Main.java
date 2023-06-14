package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.Experiment;
import org.ucb.c5.constructionfile.view.SimulatorView;

/**
 *
 * @author J. Christopher Anderson
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            //creating and showing this application's GUI.
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SimulatorView();
                }
            });
        } else {
            String dirPath = args[0];
            ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
            parseFolder.initiate();
            Experiment exp = parseFolder.run(dirPath);

            //Simulate the experiment and write results
            SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
            sed.initiate();
            Experiment simulatedExperiment = sed.run(exp);
        }
    }
}
