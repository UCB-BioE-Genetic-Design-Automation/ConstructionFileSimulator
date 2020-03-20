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
        
        List<File> files = new ArrayList<>();
        
        //Handle sub-directory
        List<File> filesParse = parseDirectory(dir,files);
        
        //Dispose files as cf or seq 
        for(File file:filesParse){
            
            if (file.getName().toLowerCase().startsWith("construction") ){

                cfs.add(runCF(file));

            }
            else{

                nameToSequencing.putAll(runSeq(file));


            }
            
            
            
        }
        


    }
    
    //File reader
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
    
    
    
    //Recursive folder parser
    private List<File> parseDirectory(File dir, List<File> files) throws IOException {
        
        
        for(File afile : dir.listFiles()) {
            //Handle nested directories
            if(afile.isDirectory()) {
                parseDirectory(afile,files);
            }
            else{
                files.add(afile);
            }
        }
        return files;
    }

    
    //Handle cf
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
                

    //Handle seq
    private Map<String,String> runSeq(File afile) throws IOException {
        
        Map<String,String> nameToSequencing = new HashMap<>();
        
//        String ext = afile.getName().substring(afile.getName().length() - 3);
//        
//        switch(ext){
//            case "txt":
//                
//            
        //read .txt or .tsv
        if (afile.getName().endsWith(".txt") || afile.getName().endsWith(".tsv") ){

            String fileContent = readFile(afile);

            String[] byLine = fileContent.split("\n");
            for (String str: byLine){
                
                //Ignore blank lines
                if (str.trim().isEmpty()) {
                    continue;
                }

                //Ignore commented-out lines
                if (str.startsWith("//")) {
                    continue;
                }
                
                String[] str2 = str.split("(\\r|\\n|\\s)");
                StringBuilder sb2 = new StringBuilder();
                
                for(String str3:str2){
                    //ignore ""
                    if (str3.trim().isEmpty()){
                        continue;                      
                    }
                    //ignore comments
                    if (str3.startsWith("//")) {
                        continue;
                    }
                    sb2.append(str3);
                    sb2.append("\n");
                }
                String[] strNew = sb2.toString().split("\n");

                
                
                if(strNew[1].matches("[ATCGatcg]+")){
                    String seqName = strNew[0];
                    String seqContent = strNew[1];
                    nameToSequencing.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContent.replaceAll("(\\r|\\n|\\s)", ""));                    
                }
                
            }

        }

        //Handle .ape .seq .str or .gb
        if (afile.getName().endsWith(".ape") || afile.getName().endsWith(".seq") || afile.getName().endsWith(".str") || afile.getName().endsWith(".gb")){

            String fileContent = readFile(afile);
              

            String seqName =  afile.getName();

            int origin = fileContent.lastIndexOf("ORIGIN");


            String rawSeq = fileContent.substring(origin + 6);
            String seqContent = rawSeq.replaceAll("[^A-za-z ]", "");


            nameToSequencing.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContent.replaceAll("(\\r|\\n|\\s)", ""));


        }
        return nameToSequencing;

    }

            
        
        
    
    
    
    public static void main(String args[]) throws Exception  {
        
        //Enter Path name as a String
        String dirPath = "/Users/Star/Documents/Capstone/Examples";
        
        //initiate
        ParseFolderConstructionFile parseFolder = new ParseFolderConstructionFile();
        parseFolder.initiate();
        
        Map<String,String> NtoS = new HashMap<>();
        List<ConstructionFile> CFiles = new ArrayList<>();
        
        parseFolder.run(dirPath, CFiles, NtoS);
        
        //for debug
        System.out.print(CFiles);
    }

    
        
}        
        
        
            
 