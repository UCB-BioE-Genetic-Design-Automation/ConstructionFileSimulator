/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.genbank;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ucb.c5.genbank.model.Annotation;
import org.ucb.c5.sequtils.RevComp;
import org.ucb.c5.utils.FileUtils;
import org.ucb.c5.utils.Log;

/**
 *
 * @author J. Christopher Anderson
 */
public class AutoAnnotate {

    private Map<String, Annotation> features;
    private GenbankWriter gbwriter;
    private RevComp rc;

    public void initiate() throws Exception {
        gbwriter = new GenbankWriter();
        rc = new RevComp();
        rc.initiate();

        //Read in directory locations
        Map<String, String> fileLocations = new HashMap<>();
        File afile = new File("data_paths.txt");
        if (!afile.exists()) {
            Log.warning("File data_paths.txt is missing");
        } else {

            try {
                String data2 = FileUtils.readFile("data_paths.txt");
                String[] lines = data2.split("\\r|\\r?\\n");
                for (String line : lines) {
                    String[] tabs = line.split("\t");
                    Path ppath = Paths.get(tabs[1]);
                    String filepath = ppath.toString();
                    fileLocations.put(tabs[0], filepath);
                }
            } catch (Exception err) {
                Log.warning("Unable to read data_paths.txt");
            }
        }

        //Read in the features if available
        features = new HashMap<>();
        try {
            String data = FileUtils.readFile(fileLocations.get("default_features"));

            String[] lines = data.split("\\r|\\r?\\n");
            for (String line : lines) {
                String[] tabs = line.split("\t");
                String name = tabs[0];
                String seq = tabs[1].toUpperCase().replaceAll("[^ATCGRYSWKMBDHVN]", "");
                String color = tabs[3];
                Annotation annot = new Annotation(name, color, 0, 0, false);
                Annotation annotrc = new Annotation(name, color, 0, 0, true);
                features.put(seq, annot);
                features.put(rc.run(seq), annotrc);
            }
            return;
        } catch (Exception err) {
            Log.warning("Error parsing 'default_features'");
        }
        
        //Pull from backup resource data otherwise
        try {
            String data = FileUtils.readResourceFile("genbank/data/Default_Features.txt");

            String[] lines = data.split("\\r|\\r?\\n");
            for (String line : lines) {
                String[] tabs = line.split("\t");
                String name = tabs[0];
                String seq = tabs[1].toUpperCase().replaceAll("[^ATCGRYSWKMBDHVN]", "");
                String color = tabs[3];
                Annotation annot = new Annotation(name, color, 0, 0, false);
                Annotation annotrc = new Annotation(name, color, 0, 0, true);
                features.put(seq, annot);
                features.put(rc.run(seq), annotrc);
            }
            return;
        } catch (Exception err) {
            Log.warning("Error parsing backup resource 'default_features'");
            throw new RuntimeException("Unable to find features for AutoAnnotate");
        }
    }

    public void run(String sequence, String path) throws Exception {
        String seqUpper = sequence.toUpperCase();

        //Identify G00101 if it is present, and rotate
        int g00101Pos = seqUpper.indexOf("GCTCACTCAAAGGCGGTAAT");
        if(g00101Pos > 0) {
            sequence = rc.run(sequence);
            seqUpper = sequence.toUpperCase();
        }
        g00101Pos = seqUpper.indexOf("ATTACCGCCTTTGAGTGAGC");
        if(g00101Pos > 0) {
            sequence = sequence.substring(g00101Pos) + sequence.substring(0, g00101Pos);
            seqUpper = sequence.toUpperCase();
        }
        
        //Find the features and annotation
        seqUpper = seqUpper + seqUpper;
        List<Annotation> annots = new ArrayList<>();
        for (String feat : features.keySet()) {
            int index = seqUpper.indexOf(feat);
            if (index == -1) {
                continue;
            }

            int end = index + feat.length();
            if (end > seqUpper.length()) {
                end = end - seqUpper.length();
            }

            Annotation existing = features.get(feat);
            Annotation newan = new Annotation(existing.getName(), existing.getColor(), index + 1, end, existing.isRevComp());
            annots.add(newan);
        }

        gbwriter.run(sequence, annots, path);
    }

    public static void main(String[] args) throws Exception {
        AutoAnnotate aa = new AutoAnnotate();
        aa.initiate();
        aa.run("tcatgaaaatcgcctgataccgcccttttgggttcaagcagtacataacgatggaaccacattacaggagtagtgatgaCACCtcatcctttatctcgtctgtcaggaaaatggttcagcgccgcccgccaggcttcgcccagcggaaccggtggcttgcccgtcaaaatgcgggccttatcaaagagagagagccgaccggcgtaaaagcgctctacggtgggctccggcagcccataaaagcgctgcatcacccgccagcggttctcctcgcgcccggccaggaaaagcatccggttcagcaggcggaagaatccctgcctgcgccagtggcgttcggcaaactgccgggtgagctgatagagcggaacgctgcccagccgcgggctgtcggcaatcgcgtcggcaagggccaccgccagcggcagcgaatagccagtggtagggtgaaatagcccagcccgcattcccgaggcaataaggggcatctgcggccaggcctcggcaatgtcaaccagcgcctcagcggtggggatcccccagtcggcaaagacgttggccacgctgcgctgctgctcggtggccgcccgctcgccttcaacggcggcccagctggtgccgcccgcgcccgcgacgtcaatcacggtaacgccggcatcgatcagctgcccggccacggttcgggagataccggctcccacctctttcaccaccagcggaacgggcagctcgcggaccagagtttcaatagccgccagccgtccgcgccagtcgcgatcgccgccgggctgtagcgcctcctgcagcgggtttaggtgcacaatcagcgcatccgcctcgatcatctccacggcccgtcgggcgtaatcaataccttttctgccggtcagctgcgccgcgccgaggttcgccagcagcggcacgtccggagccagctgccgcagggttttatccagccctaagcccgcgtcgctctcaatggcgacgcgctgggagcccacccccatcgcaatttttagcacctgcgccgcctcggcgaggtggcggttgatatggcgcgagcgctcaacgccgccggtcatggagctgatcagcagcggagcctgtagctgccgattcaggaaggtggtttccagcgtgatgtcgctaaaattcagctctggcagggcgcagtgggtaaagcgccagcgctcaaaacctgcgctagcctgagttacggcgcgacgggggtcgagaacgatatccagatgatcgttcttacgctgaacaaggcgctcgtccttcataaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccggcaggcgcatggaggattacgtcaggctgctgccggtaaaacccttctaccgactctgctgggagtccgggaagaccctcgactatgctaacgacagcgccgagcttgaggcgcagattacccagttcaacccccgcgacgtcgagggctaccggcgctttctggcttactcccaggcggtattccaggagggatatttgcgcctcggcagcgtgccgttcctctcttttcgcgacatgctgcgcgccgggccgcagctgcttaagctccaggcgtggcagagcgtctaccagtcggtttcgcgctttattgaggatgagcatctgcggcaggccttctcgttccactccctgctggtaggcggcaaccccttcaccacctcgtccatctacaccctgatccacgcccttgagcgggagtggggggtctggttccctgagggcggcaccggggcgctggtgaacggcatggtgaagctgtttaccgatctgggcggggagatcgaactcaacgcccgggtcgaagagctggtggtggccgataaccgcgtaagccaggtccggctggcggatggtcggatctttgacaccgacgccgtagcctcgaacgctgacgtggtgaacacctataaaaagctgctcggccaccatccggtggggcagaagcgggcggcagcgctggagcgcaagagcatgagcaactcgctgtttgtgctctacttcggcctgaaccagcctcattcccagctggcgcaccataccatctgttttggtccccgctaccgggagctgatcgacgagatctttaccggcagcgcgctggcggatgacttctcgctctacctgcactcgccctgcgtgaccgatccctcgctcgcgcctcccggctgcgccagcttctacgtgctggccccggtgccgcatcttggcaacgcgccgctggactgggcgcaggaggggccgaagctgcgcgaccgcatctttgactaccttgaagagcgctatatgcccggcctgcgtagccagctggtgacccagcggatctttaccccggcagacttccacgacacgctggatgcgcatctgggatcggccttctccatcgagccgctgctgacccaaagcgcctggttccgcccgcacaaccgcgacagcgacattgccaacctctacctggtgggcgcaggtactcaccctggggcgggcattcctggcgtagtggcctcggcgaaagccaccgccagcctgatgattgaggatctgcaatgagccaaccgccgctgcttgaccacgccacgcagaccatggccaacggctcgaaaagttttgccaccgctgcgaagctgttcgacccggccacccgccgtagcgtgctgatgctctacacctggtgccgccactgcgatgacgtcattgacgaccagacccacggcttcgccagcgaggccgcggcggaggaggaggccacccagcgcctggcccggctgcgcacgctgaccctggcggcgtttgaaggggccgagatgcaggatccggccttcgctgcctttcaggaggtggcgctgacccacggtattacgccccgcatggcgctcgatcacctcgacggctttgcgatggacgtggctcagacccgctatgtcacctttgaggatacgctgcgctactgctatcacgtggcgggcgtggtgggtctgatgatggccagggtgatgggcgtgcgggatgagcgggtgctggatcgcgcctgcgatctggggctggccttccagctgacgaatatcgcccgggatattattgacgatgcggctattgaccgctgctatctgcccgccgagtggctgcaggatgccgggctgaccccggagaactatgccgcgcgggagaatcgggccgcgctggcgcgggtggcggagcggcttattgatgccgcagagccgtactacatctcctcccaggccgggctacacgatctgccgccgcgctgcgcctgggcgatcgccaccgcccgcagcgtctaccgggagatcggtattaaggtaaaagcggcgggaggcagcgcctgggatcgccgccagcacaccagcaaaggtgaaaaaattgccatgctgatggcggcaccggggcaggttattcgggcgaagacgacgagggtgacgccgcgtccggccggtctttggcagcgtcccgtttaggcgggcggccatgacgttcacgcaggatcagtcgcctgtaggtcggcaggcttgggaagctgtggtatggctgtgcaggtcgtaaatcactgcataattcgtgtcgctcaaggcgcactcccgttctggataatgttttttgcgccgacatcataacggttctggcaaatattctgaaatgagctgttgacaattaatcatccggctcgtataatgtgtggaattgtgagcggataacaatttcacacaggaaacagcgccgctgagaaaaagcgaagcggcactgctctttaacaatttatcagacaatctgtgtgggcactcgaccggaattatcgattaactttattattaaaaattaaagaggtatatattaatgtatcgattaaataaggaggaataaaccatgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctgaacatctttttttaaagtttttaaatcaatcaactttctcttcatcatttttatcttttcgatgataataatttgggatatgtgagacacttacaaaacttccaaggtctgcggggcaaaacaatcgataaatcagcccgggaattaacatggcaaccactcatttggatgtttgcgccgtggttccggcggccggatttggccgtcgaatgcaaacggaatgtcctaagcaatatctctcaatcggtaatcaaaccattcttgaacactcggtgcatgcgctgctggcgcatccccgggtgaaacgtgtcgtcattgccataagtcctggcgatagccgttttgcacaacttcctctggcgaatcatccgcaaatcaccgttgtagatggcggtgatgagcgtgccgattccgtgctggcaggtctgaaagccgctggcgacgcgcagtgggtattggtgcatgacgccgctcgtccttgtttgcatcaggatgacctcgcgcgattgttggcgttgagcgaaaccagccgcacgggggggatcctcgccgcaccagtgcgcgatactatgaaacgtgccgaaccgggcaaaaatgccattgctcataccgttgatcgcaacggcttatggcacgcgctgacgccgcaatttttccctcgtgagctgttacatgactgtctgacgcgcgctctaaatgaaggcgcgactattaccgacgaagcctcggcgctggaatattgcggattccatcctcagttggtcgaaggccgtgcggataacattaaagtcacgcgcccggaagatttggcactggccgagttttacctcacccgaaccatccatcaggagaatacataatgcgaattggacacggttttgacgtacatgcctttggcggtgaaggcccaattatcattggtggcgtacgcattccttacgaaaaaggattgctggcgcattctgatggcgacgtggcgctccatgcgttgaccgatgcattgcttggcgcggcggcgctgggggatatcggcaagctgttcccggataccgatccggcatttaaaggtgccgatagccgcgagctgctacgcgaagcctggcgtcgtattcaggcgaagggttatacccttggcaacgtcgatgtcactatcatcgctcaggcaccgaagatgttgccgcacattccacaaatgcgcgtgtttattgccgaagatctcggctgccatatggatgatgttaacgtgaaagccactactacggaaaaactgggatttaccggacgtggggaagggattgcctgtgaagcggtggcgctactcattaaggcaacaaaatgattgagtttgataatctcacttacctccacggtaaaccgcacctcaggcaataataaagtttgcggccgcgaattcctgcagcccgggggatccactagttctagagcggccgccaccgcggtggagctccagcttttgttccctttagtgagggttaatttcgagcttggcgtaatcatggtcatagctgtttcctgtgtggtggtagatcctctacgccggacgcatcgtggccggcatcaccggcgccacaggtgcggttgctggcgcctatatcgccgacatcacccagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaatttttttaaggcagttattgcctactatggaactgcctcggtgagTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGAACTCtttacaaatctaaaatgtttgtgactgtattataAgaacagaaatcccccttacacggaggcatcagtgaccaaacaggaaaaaaccgcccttaacatggcccgctttatcagaagccagacattaacgcttctggagaaactcaacgagctggacgcggatgaacaggcagacatctgtgaatcgcttcacgaccacgctgatgagctttaccgcagctgcctcgcgcgtttcggtgatgacggtgaaaacctctgacacatgcagcTCCCagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccataggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaaggacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccTTCGattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaatgtctgccacgtatcgccagatgttcCAGACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACATTTTTGCTTCCGTGTGGTATTATGGGAGCAGTAGGTCTACGGTTAACTGATACTAAAAGACAATTCAGCGGGTAACCTTGCAATGGTGAGTGGCAGTAAAGCGGGCGTTTCGCCTCATCGCGAAATAGAAGTAATGAGACAATCCATTGACGATCACCTGGCTGGCCTGTTACCTGAAACCGACAGCCAGGATATCGTCAGCCTTGCGATGCGTGAAGGCGTCATGGCACCCGGTAAACGGATCCGTCCGCTGCTGATGCTGCTGGCCGCCCGCGACCTCCGCTACCAGGGCAGTATGCCTACGCTGCTCGATCTCGCCTGCGCCGTTGAACTGACCCATACCGCGTCGCTGATGCTCGACGACATGCCCTGCATGGACAACGCCGAGCTGCGCCGCGGTCAGCCCACTACCCACAAAAAATTTGGTGAGAGCGTGGCGATCCTTGCCTCCGTTGGGCTGCTCTCTAAAGCCTTTGGTCTGATCGCCGCCACCGGCGATCTGCCGGGGGAGAGGCGTGCCCAGGCGGTCAACGAGCTCTCTACCGCCGTGGGCGTGCAGGGCCTGGTACTGGGGCAGTTTCGCGATCTTAACGATGCCGCCCTCGACCGTACCCCTGACGCTATCCTCAGCACCAACCACCTCAAGACCGGCATTCTGTTCAGCGCGATGCTGCAGATCGTCGCCATTGCTTCCGCCTCGTCGCCGAGCACGCGAGAGACGCTGCacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtc"
                + "accgg", "trashy33K.seq");
        System.out.println("done!");
    }
}
