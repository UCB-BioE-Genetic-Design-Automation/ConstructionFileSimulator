 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ucb.c5.sequtils.CalcEditDistance;
import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.sequtils.RevComp;
import org.ucb.c5.sequtils.StringRotater;

/**
 * This Function simulates the process of two oligonucleotides annealing into a
 * complex that can function as a substrate of polymerase extension.
 *
 * @author J. Christopher Anderson
 */
public class ExtensionSimulator {

    private final double minTm = 40.0;  //degrees in celsius
    private final double maxTm = 55.0;  //degrees in celsius

    private CalcEditDistance ced;
    private PolyRevComp revcomp;
    private StringRotater rotator;
    private RevComp rc;
    private TmCalculator tmCalculator;
    private Matrix ednafull;

    public void initiate() throws Exception {
        Logger logger = Logger.getLogger("jaligner.SmithWatermanGotoh");
        logger.setLevel(Level.OFF);
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

    /**
     * Inputs an oligo and a single-stranded template (ssDNA, all caps, 5'-->3')
     * and returns the index on the template for the optimal positioning of the
     * 3' end of the oligo
     *
     * Returns -1 if there are no annealing site with Tm >40degC
     *
     * Returns -2 if more than one annealing site >55degC is present
     *
     * Throws an Exception if an error is encountered
     *
     * Otherwise returns the highest-Tm site
     *
     * @param oligo
     * @param template
     * @return
     * @throws Exception
     */
    public int run(String oligo, String template) throws Exception {
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
            Alignment alignment = SmithWatermanGotoh.align(s1, s2, ednafull, 15f, 2f);
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
            if (Tm > 55) {
                num55++;
            }
            if (Tm > bestTm) {
                bestTm = Tm;
                bestIndex = index;
            }
        }  //end while

        //return error code -1, if the best annealling Tm is less than 40 degrees
        if (bestTm < 40) {
            return -1;
        }
        if (num55 > 1) {
            return -2;
        }

        //Return the position on the template for the 3' end of the oligo
        return bestIndex + 6;
    }

    public static void main(String[] args) throws Exception {
        ExtensionSimulator sim = new ExtensionSimulator();
        sim.initiate();

        {
            System.out.println("Correct duplex of 22 mers");
            int result = sim.run("GTATCACGAGGCAGAATTTCAGAAAGCTCACTCAAAGGCGGTAATTCAAAGGCGGTAATCGAGGCAGAAGTATCACGAGGCAGAATTTCAGAAAGCTCACTCAAAGGCGGTAAT", "ATTACCGCCTTTGAGTGAGCTTTCTGAAATTCTGCCTCGTGATACTTCTGCCTCGTGATACGCCTTTGAATTACCGCCTTTGAGTGAGCTTTCTGAAATTCTGCCTCGTGATAC");
            System.out.println(result);
        }
        /* {
         System.out.println("Correct duplex of 22 mers");
         int result = sim.run("GTATCACGAGGCAGAATTTCAG", "CTGAAATTCTGCCTCGTGATAC");
         System.out.println(result);
         }
         {
         System.out.println("Mismatched 22 mers");
         int result = sim.run("CTCCTTCGGTCCTCCGATCGTT", "CTGAAATTCTGCCTCGTGATAC");
         System.out.println(result);
         }
         {
         System.out.println("Mismatched 22 mers");
         int result = sim.run("GGATCCCGTGGCAGTATTTAAG", "CTGAAATTCTGCCTCGTGATAC");
         System.out.println(result);
        
         
         System.out.println("Correct duplex of 22 mers");
         int result = sim.run("cgtatGAATTCattaccgcctttgagtgagc".toUpperCase(), "TATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCTCCGAATTGgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctgGAATTCATGAGATCTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACGGATCTGAAAGAGGAGAAAGGATCTATGGCAAGTAGCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAAGGATCTCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATAGGATCCtaaCTCGAcgtgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatCAATTCGACCCAGCTTTCTTGTACAAAGTTGGCATTATAAAAAATAATTGCTCATCAATTTGTTGCAACGAACAGGTCACTATCAGTCAAAATAAAATCATTATTTG".toUpperCase());
         System.out.println("final result"+ result);
         */
    }
}
