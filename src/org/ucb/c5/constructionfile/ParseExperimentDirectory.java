/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Experiment;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.utils.FileUtils;
import org.ucb.c5.utils.Log;

/**
 * @author Zihang Shao
 */
public class ParseExperimentDirectory {
    
    private ParseOligo po;
    
    public void initiate() {
//        ParseOligo po = new ParseOligo();
        po = new ParseOligo();
    }

    public Experiment run(String dirPath) throws Exception {
        Log.info("Parsing experiment directory: " + dirPath);
        File dir = new File(dirPath);
        List<File> files = new ArrayList<>();
        List<ConstructionFile> cfs = new ArrayList<>();
        Map<String, Polynucleotide> nameToPoly = new HashMap<>();

        //Handle sub-directory
        List<File> filesParse = parseDirectory(dir, files);

        //Dispose files as cf or seq 
        for (File file : filesParse) {
            if (file.getName().toLowerCase().startsWith("construction")) {
                cfs.add(runCF(file));
            } else {
                Map<String, Polynucleotide> newSeq = runSeq(file);
                for (Map.Entry<String, Polynucleotide> entry : newSeq.entrySet()) {
                    AddToMapNoRepetition(entry.getValue(), entry.getKey(), nameToPoly);
                }
            }
        }
        for (ConstructionFile cf : cfs) {
            checkRepeat(cf, nameToPoly);
        }

        return new Experiment(dir.getAbsolutePath(), cfs, nameToPoly);
    }

    /**
     * check repeat sequences between construction file and nameToPoly map
     * if there is an conflict that one name has two different sequences, throw an IllegalArgumentException
     * @param cf
     * @param nameToPoly
     * @throws IllegalArgumentException
     */
    private void checkRepeat(ConstructionFile cf, Map<String, Polynucleotide> nameToPoly) throws IllegalArgumentException {
        Map<String, Polynucleotide> cfMap = cf.getSequences();
        if (cfMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Polynucleotide> entry : cfMap.entrySet()) {
            Polynucleotide poly = nameToPoly.get(entry.getKey());
            if (poly != null && !entry.getValue().equals(poly)) {
                throw new IllegalArgumentException("different Polynucleotide sequences of " + entry.getKey());
            }
        }
    }

    /**
     * add new sequence to map when there is no repetition
     * if there is a conflict that two different sequences have the same name, throw an IllegalArgument exception.
     * Else, add the new Entry to the nameToPloy map
     * @param newPoly
     * @param nameToPoly
     * @param name
     */
    private void AddToMapNoRepetition(Polynucleotide newPoly, String name, Map<String, Polynucleotide> nameToPoly) {
        Polynucleotide existPoly = nameToPoly.get(name);
        if (existPoly == null || existPoly.equals(newPoly)) {
            nameToPoly.put(name, newPoly);
        } else {
            throw new IllegalArgumentException("Two different polynucleotides:" + newPoly +
                    "and " + existPoly + "with same name:" + name);
        }
    }

    //Recursive folder parser
    private List<File> parseDirectory(File dir, List<File> files) throws IOException {
        for (File afile : dir.listFiles()) {
            //Handle nested directories
            if (afile.isDirectory()) {
                parseDirectory(afile, files);
            } else {
                files.add(afile);
            }
        }
        return files;
    }

    //Handle cf
    private ConstructionFile runCF(File afile) throws IOException, Exception {
        Log.info("Parsing construction file: " + afile.getAbsolutePath());
        String cfContent = FileUtils.readFile(afile.getAbsolutePath());
        ParseConstructionFile parseConstruction = new ParseConstructionFile();
        parseConstruction.initiate();
        try {
            ConstructionFile cf = parseConstruction.run(cfContent);
            return cf;

        } catch (Exception e) {
            throw new Exception("Construction File contains invalid syntax");
        }
    }

    //Handle seq
    private Map<String, Polynucleotide> runSeq(File afile) throws IOException, Exception {
        Log.info("Extracting sequences from: " + afile.getAbsolutePath());
        Map<String, Polynucleotide> nameToPoly = new HashMap<>();
        
        
        //read .txt or .tsv files with oligo sequences
        if (afile.getName().endsWith(".txt") || afile.getName().endsWith(".tsv")) {

            String fileContent = FileUtils.readFile(afile.getAbsolutePath());

            String[] byLine = fileContent.split("\\r|\\r?\\n");
            for (String str : byLine) {

                //Ignore blank lines
                if (str.trim().isEmpty()) {
                    continue;
                }

                //Ignore commented-out lines
                if (str.startsWith("//")) {
                    continue;
                }

                String[] tabs = str.split("\t");
                String name = tabs[0];
                String seq = tabs[1];
                
                //parse oligo
                Polynucleotide oligo = po.run(seq);
                
                nameToPoly.put(name,oligo);
              
               //Polynucleotide newPoly = new Polynucleotide(seq, "", "", false, false, false);
               //AddToMapNoRepetition(newPoly, name, nameToPoly);
              

                Log.info("Added oligo:\t" + name + "\t" + seq);
                Log.seq(name, seq, "oligo seq parse from tsv files");
            }
        }

        //Handle .ape .seq .str or .gb
        if (afile.getName().endsWith(".ape") || afile.getName().endsWith(".seq") || afile.getName().endsWith(".str") || afile.getName().endsWith(".gb")) {

            String fileContent = FileUtils.readFile(afile.getAbsolutePath());
            String filename = afile.getName();
            String seqName = filename.split("\\.")[0];

            int origin = fileContent.lastIndexOf("ORIGIN");

            String rawSeq = fileContent.substring(origin + 6);
            String seqContent = rawSeq.replaceAll("[^A-za-z]", "");
            if (!seqContent.matches("[ACGTRYSWKMBDHVNacgtryswkmbdhvn]+")) {
                throw new IllegalArgumentException("Sequence " + seqName + "is not DNA");
            }
            Polynucleotide newPoly = new Polynucleotide(seqContent, true);
            AddToMapNoRepetition(newPoly, seqName, nameToPoly);
            Log.info("Added plasmid:\t" + seqName + " of length " + seqContent.length());
            Log.seq(seqName, seqContent, "plasmid seq parse from gb files");
        }
        return nameToPoly;
    }

    public static void main(String args[]) throws Exception {

        //Enter Path name as a String
        String dirPath = "/Users/jca20n/Pimar/experiments/2020_02_04-Lycopene6";

        //initiate
        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();

        Experiment exp = parseFolder.run(dirPath);
        //for debug
        System.out.print(exp);
    }

}