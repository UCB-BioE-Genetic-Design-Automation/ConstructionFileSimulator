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
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author Zihang Shao
 */
public class ParseFolderConstructionFile {

    public void initiate() {
    }

    public void run(String dirPath, List<ConstructionFile> cfs, Map<String, Polynucleotide> nameToSequencing) throws Exception {
        File dir = new File(dirPath);
        List<File> files = new ArrayList<>();

        //Handle sub-directory
        List<File> filesParse = parseDirectory(dir, files);

        //Dispose files as cf or seq 
        for (File file : filesParse) {
            if (file.getName().toLowerCase().startsWith("construction")) {
                cfs.add(runCF(file));
            } else {
                nameToSequencing.putAll(runSeq(file));
            }
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
        String cfContent = FileUtils.readFile(afile.getAbsolutePath());
        ParseConstructionFile parseConstruction = new ParseConstructionFile();
        parseConstruction.initiate();
        try {
            ConstructionFile cf = parseConstruction.run(cfContent);
            return cf;

        } catch (Exception e) {
            throw new Exception("Not a valid Construction File");
        }
    }

    //Handle seq
    private Map<String, Polynucleotide> runSeq(File afile) throws IOException, Exception {

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
                if(!seq.matches("[ATCGatcg]+")) {
                    throw new Exception("Oligo file has non-DNA sequence:\n" + seq);
                }
                nameToPoly.put(name, new Polynucleotide(seq, "", "", false, false, false));
            }
        }

        //Handle .ape .seq .str or .gb
        if (afile.getName().endsWith(".ape") || afile.getName().endsWith(".seq") || afile.getName().endsWith(".str") || afile.getName().endsWith(".gb")) {

            String fileContent = FileUtils.readFile(afile.getAbsolutePath());
            String seqName = afile.getName();

            int origin = fileContent.lastIndexOf("ORIGIN");

            String rawSeq = fileContent.substring(origin + 6);
            String seqContent = rawSeq.replaceAll("[^A-za-z]", "");

            nameToPoly.put(seqName, new Polynucleotide(seqContent, true));
        }
        return nameToPoly;
    }

    public static void main(String args[]) throws Exception {

        //Enter Path name as a String
        String dirPath = "/Users/jca20n/Pimar/experiments/2020_02_04-Lycopene6";

        //initiate
        ParseFolderConstructionFile parseFolder = new ParseFolderConstructionFile();
        parseFolder.initiate();

        Map<String, Polynucleotide> NtoS = new HashMap<>();
        List<ConstructionFile> CFiles = new ArrayList<>();

        parseFolder.run(dirPath, CFiles, NtoS);

        //for debug
        System.out.print(CFiles);
    }

}