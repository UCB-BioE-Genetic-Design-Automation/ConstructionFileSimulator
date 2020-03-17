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
    
    public void run(String dirpath) throws Exception{
        String dirPath = "";
        File dir = new File(dirPath);
        
        Map<String,String> nameToSequencing = new HashMap<>();
        List<ConstructionFile> cfs = new ArrayList<>();
        
        parseDirectory(dir, nameToSequencing, cfs);
        
        

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
    
    
    private void parseDirectory(File dir, Map<String, String> nameToSequencing, List<ConstructionFile> cfs) throws IOException {
        
//        FilenameFilter cfFilter = new FilenameFilter(){
//            @Override
//            public boolean accept(File file, String name){
//                return name.toLowerCase().startsWith("construction");
//            }
//        };
        
        
        List<File> allFiles = new ArrayList<>();
        for(File afile : dir.listFiles()) {
            //Handle nested directories
            if(afile.isDirectory()) {
                parseDirectory(afile, nameToSequencing, cfs);
            }
            if (afile.getName().toLowerCase().startsWith("construction") ){
                
                String cfContent = readFile(afile);
                //Question: String or ConstructionFile
                ParseConstructionFile parseConstruction = new ParseConstructionFile();
                parseConstruction.initiate();
                try {
                    ConstructionFile cf = parseConstruction.run(cfContent);
                    cfs.add(cf);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                
                
                
            }else{
                
                //read txt
                if (afile.getName().endsWith(".txt")){
                    
                    String fileContent = readFile(afile);
                    
                    String[] byLine = fileContent.split("\n");
                    for (String str: byLine){
                        String[] str2 = str.split("\\W+");
                        String seqName = str2[0];
                        String seqContent = str2[1];
                        nameToSequencing.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContent.replaceAll("(\\r|\\n|\\s)", ""));

                    }
                                        
                    
                }
                
                
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
    }
}


    
    
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
    

