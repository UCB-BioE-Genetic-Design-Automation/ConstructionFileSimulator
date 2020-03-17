/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import org.ucb.c5.constructionfile.model.ConstructionFile;

/**
 *
 * @author Zihang Shao
 */
public class ParseFolderConstructionFile {
    
    public void initiate(){
    
}
    
    public void run(String dirPath, List<ConstructionFile> cfs, Map<String,String> nameToSequencing) throws Exception{
        
        File dir = new File(dirPath);
        
//        Map<String,String> nameToSequencing = new HashMap<>();
//        List<ConstructionFile> cfs = new ArrayList<>();
        
        List<File> files = parseDirectory(dir, nameToSequencing, cfs);
        
        for(File file:files){
            
            if (file.getName().toLowerCase().startsWith("construction") ){

                cfs.add(runCF(file));

            }
            else{

                nameToSequencing.putAll(runSeq(file));


            }
            
            
            
        }
        

        

//        FilenameFilter txtFilter = new FilenameFilter() {
//
//            @Override
//            public boolean accept(File file, String name) {
//                return name.endsWith(".txt");
//            }
//        };
//
//
//        FilenameFilter tsvFilter = new FilenameFilter() {
//
//            @Override
//            public boolean accept(File file, String name) {
//                return name.endsWith(".tsv");
//            }
//        };
//
//
//        File[] txtFiles = dir.listFiles(txtFilter);
//        File[] tsvFiles = dir.listFiles(tsvFilter);
//
//        Path txtPath = Paths.get(txtFiles[0].getAbsolutePath());
//
//        String stepSec  = new String(Files.readAllBytes(txtPath), StandardCharsets.UTF_8);
//        StringBuilder seqSec = new StringBuilder();
//        
//        for (File seqtsv: tsvFiles){
//            
//            String currSeq  = new String(Files.readAllBytes(Paths.get(seqtsv.getAbsolutePath())), StandardCharsets.UTF_8);
//            seqSec.append("\n");
//            seqSec.append(currSeq);
//
//        }
//        
//        StringBuilder CF = new StringBuilder();
//        CF.append(stepSec);
//        CF.append(seqSec.toString());
//        
//        
//        String cfile = CF.toString();
//        return cfile;


    }
    
    
    String readFile(File file) throws IOException {
        
        String filePath = file.getAbsolutePath();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
    
    
//    private static String readAllBytesJava7(String filePath) 
//    {
//        String content = "";
// 
//        try
//        {
//            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
//        } 
//        catch (IOException e) 
//        {
//            e.printStackTrace();
//        }
// 
//        return content;
//    }
    
    
    private List<File> parseDirectory(File dir, Map<String, String> nameToSequencing, List<ConstructionFile> cfs) throws IOException {
        
        List<File> files = new ArrayList<>();
        
        for(File afile : dir.listFiles()) {
            //Handle nested directories
            if(afile.isDirectory()) {
                parseDirectory(afile, nameToSequencing, cfs);
            }
            else{
            files.add(afile);
            }
        }
        return files;
    }

    private ConstructionFile runCF(File afile) throws IOException, Exception{
        
 
        String cfContent = readFile(afile);
        ParseConstructionFile parseConstruction = new ParseConstructionFile();
        parseConstruction.initiate();
        try {
            ConstructionFile cf = parseConstruction.run(cfContent);
            return cf;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Not a valid Construction File");
        }
            
        
       
    }            
                

    
    private Map<String,String> runSeq(File afile) throws IOException {
        
        Map<String,String> nameToSequencing = new HashMap<>();
        
//        String ext = afile.getName().substring(afile.getName().length() - 3);
//        
//        switch(ext){
//            case "txt":
//                
//            
        //read txt
        if (afile.getName().endsWith(".txt") || afile.getName().endsWith(".tsv") || afile.getName().endsWith(".csv")){

            String fileContent = readFile(afile);

            String[] byLine = fileContent.split("\n");
            for (String str: byLine){
                String[] str2 = str.split("\\W+");
                String seqName = str2[0];
                String seqContent = str2[1];
                nameToSequencing.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContent.replaceAll("(\\r|\\n|\\s)", ""));

            }

        }

        if (afile.getName().endsWith(".ape") || afile.getName().endsWith(".seq") || afile.getName().endsWith(".str") || afile.getName().endsWith(".gb")){

            String fileContent = readFile(afile);

//                    int locus = fileContent.indexOf("LOCUS");
//                    int def = fileContent.indexOf("DEFINITION");
//                    String seqName = fileContent.substring(locus + 5, def);
//                    

            String seqName =  afile.getName();

            int origin = fileContent.lastIndexOf("ORIGIN");


            String rawSeq = fileContent.substring(origin + 6);
            String seqContent = rawSeq.replaceAll("[^A-za-z ]", "");


            nameToSequencing.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContent.replaceAll("(\\r|\\n|\\s)", ""));


        }
        return nameToSequencing;

    }

            
        
        
    
    
    
    public static void main(String args[]) throws Exception  {
        
        String dirPath = "/Users/Star/Downloads/2020_01_20-Lycopene5/Construction Files";
        
        
        ParseFolderConstructionFile parseFolder = new ParseFolderConstructionFile();
        parseFolder.initiate();
        
        Map<String,String> NtoS = new HashMap<>();
        List<ConstructionFile> CFiles = new ArrayList<>();
        
        parseFolder.run(dirPath, CFiles, NtoS);
        
        System.out.print(CFiles);
    }

    
        
}        
        
        
            
            
//            
//            
//            //Handle .txt or .tsv files as cfs or oligos
//            FilenameFilter txtFilter = new FilenameFilter() {
//
//                @Override
//                public boolean accept(File file, String name) {
//                    return name.endsWith(".txt");
//                }
//            };
//            
//            //Handle .ape, .seq, .str, .gb as genbank
//            
//            
//            
//            
//            File[] txtFiles = dir.listFiles(txtFilter);
//
//        Path txtPath = Paths.get(txtFiles[0].getAbsolutePath());
//
//        String stepSec  = new String(Files.readAllBytes(txtPath), StandardCharsets.UTF_8);
//        StringBuilder seqSec = new StringBuilder();
//        
//        for (File seqtsv: tsvFiles){
//            
//            String currSeq  = new String(Files.readAllBytes(Paths.get(seqtsv.getAbsolutePath())), StandardCharsets.UTF_8);
//            seqSec.append("\n");
//            seqSec.append(currSeq);
//
//        }
//        
//        StringBuilder CF = new StringBuilder();
//        CF.append(stepSec);
//        CF.append(seqSec.toString());
//        
//        
//        String cfile = CF.toString();
//        }



    
    
//    public static void main(String[] args) {
//      File folder = new File("G:\\Test");
//      ParseFolderConstructionFile listFiles = new ParseFolderConstructionFile();
//      listFiles.listAllFiles(folder);
//
//     }
//     // Uses listFiles method  
//     public void listAllFiles(File folder){
//         File[] fileNames = folder.listFiles();
//         for(File file : fileNames){
//             // if directory call the same method again
//             if(file.isDirectory()){
//                 listAllFiles(file);
//             }else{
//                 try {
//                     readContent(file);
//                 } catch (IOException e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                 }
//        
//             }
//         }
//     }
//
//    public void readContent(File file) throws IOException{
//         try(BufferedReader br  = new BufferedReader(new FileReader(file))){
//               String strLine;
//               // Read lines from the file, returns null when end of stream 
//               // is reached
//               while((strLine = br.readLine()) != null){
//               }
//         }
//     }
//     
//     public void readContent(Path filePath) throws IOException{
//         List<String> fileList = Files.readAllLines(filePath);
//     }
    

