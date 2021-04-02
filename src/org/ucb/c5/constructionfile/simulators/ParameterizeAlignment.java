/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ucb.c5.sequtils.CalcEditDistance;
import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.sequtils.RevComp;
import org.ucb.c5.sequtils.StringRotater;

/**
 *
 * @author J. Christopher Anderson
 */
public class ParameterizeAlignment {

    private final double minTm = 40.0;  //degrees in celsius
    private final double maxTm = 55.0;  //degrees in celsius

    private CalcEditDistance ced;
    private PolyRevComp revcomp;
    private StringRotater rotator;
    private RevComp rc;
    private TmCalculator tmCalculator;
    private Matrix ednafull;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
        rc = new RevComp();
        rc.initiate();
        rotator = new StringRotater();
        rotator.initiate();
        ced = new CalcEditDistance();
        ced.initiate();
        tmCalculator = new TmCalculator();
        tmCalculator.initiate();

        ednafull = MatrixLoader.load("EDNAFULL_1");
    }

    private double[] scanParam(String oligo, String template) {
        double highestTm = 0;
        int bestO = -1;
        int bestE = -1;
        for(int o=0; o<=50; o++)  {
            for(int e=0; e<=10; e++) {
                double Tm = singleAlign(oligo, template, o, e);
                System.out.println(o + "  " + e + "  " + Tm );
                if(Tm > highestTm) {
                    highestTm = Tm;
                    bestO = o;
                    bestE = e;
                }
            }
        }
        double[] out = new double[3];
        out[0] = bestO;
        out[1] = bestE;
        out[2] = highestTm;
        
        return out;
    }

    private double singleAlign(String oligo, String template, float o, float e) {
        //Find all sites in template exactly matching the last 6 bp of the oligo
        String sixbp = oligo.substring(oligo.length() - 6);

        int index = 11; //index + 6 = 17, the first site it could be
        int num55 = 0;
        int bestIndex = -1;
        double bestTm = -1.0;
        String rcB = rc.run(template);
        while (true) {

            //Find the next 6bp match, quit if there are no more
            index = rcB.indexOf(sixbp, index + 1);
            if (index == -1) {
                break;
            }

            //replace the last 6 bases with U          
            String oligoU = oligo.substring(0, oligo.length() - 6) + "UUUUUU";
            String rcU = rcB.substring(0, index) + "UUUUUU";

            //Consider only the last ~40 bp of the oligo if it's long
            if (oligoU.length() > 40) {
                oligoU = oligoU.substring(oligoU.length() - 40);
            }
            if (rcU.length() > 40) {
                rcU = rcU.substring(rcU.length() - 40);
            }

            //align the sequences with Jaligner
            Sequence s1 = new Sequence(oligoU);
            Sequence s2 = new Sequence(rcU);

            //Use a modified EDNAFULL matrix to align and force the U's to align
            Alignment alignment = SmithWatermanGotoh.align(s1, s2, ednafull, o, e);
            Matrix matrix = alignment.getMatrix();

            char[] S1 = alignment.getSequence1();
            char[] S2 = alignment.getSequence2();

            //Put the sixbp back into the ends to replace U's
            for (int i = 0; i < 6; i++) {
                S1[S1.length - 6 + i] = sixbp.charAt(i);
                S2[S2.length - 6 + i] = sixbp.charAt(i);
            }

            //Calculate the melting temperature of the duplex
            double Tm = tmCalculator.run(S1, S2);
            if (Tm > 75) {
                num55++;
            }
            if (Tm > bestTm) {
                bestTm = Tm;
                bestIndex = index;
            }
        }  //end while

        return bestTm;
    }

    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger("jaligner.SmithWatermanGotoh");
        logger.setLevel(Level.OFF);

        ParameterizeAlignment pa = new ParameterizeAlignment();
        pa.initiate();
        double[] params = pa.scanParam("ccataGGTCTCACAGTTTGTGTCGTATATGG".toUpperCase(), "AAACAAAGCAGCACTTAGCGGCATAACACCAAACCACTTATTCATAAATGGCTCTTTTTCAGCACGGCGTTGTTCTTTCTTTTGTTTACGATCTTCTTTTGGAGCATCCATATACGACACAAACTGTTGTGTCACAAAGCGAACATAATCATTAGTAGACATATTATCATCACCTCTACTTGATTAGTATGTCCACTTTTTTATTCATTAATCTTTTCTTTTAATTCATTCATAATTTGCTTTACAGACTTATTCGTTGTATCAATATGAATATGAGCCTCTTCATAATAAGCTCGGCGCGATTCAAATTTCGTTACAAATGCATCT".toUpperCase());

        System.out.println("Best parameters:");
        System.out.println(params[0] + "   " + params[1] + "   " + params[2]);
    }
}
