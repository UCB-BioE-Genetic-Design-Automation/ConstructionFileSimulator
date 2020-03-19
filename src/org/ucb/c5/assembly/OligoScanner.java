/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.assembly;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.model.Oligo;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class OligoScanner {
    private Map<String,Oligo> nameToOligo;
    
    public void initiate() throws Exception {
        nameToOligo = new HashMap<>();
        
        String dir = "/Users/jca20n/Pimar";
        File fdir = new File(dir);
        readInOligos(fdir, nameToOligo);
    }
    
    public Map<String,Oligo> run() throws Exception {
        return nameToOligo;
    }

    private void readInOligos(File fdir, Map<String, Oligo> nameToOligo) {
        for(File afile : fdir.listFiles()) {
            if(afile.isDirectory()) {
                readInOligos(afile, nameToOligo);
                continue;
            }
            
            if(afile.getAbsolutePath().endsWith(".txt") || afile.getAbsolutePath().endsWith(".tsv")) {
                try {
                    parseTSV(afile, nameToOligo);
                }
                catch(Exception err) { 
                    System.out.println("OligoScanner skipping + " + afile.getAbsolutePath());
                }
            }
        }
    }

    private void parseTSV(File afile, Map<String, Oligo> nameToOligo) throws Exception {
        String data = FileUtils.readFile(afile.getAbsolutePath());
        String[] lines = data.split("\\r|\\r?\\n");
        for(String line : lines) {
            String[] tabs = line.split("\t");
            if(tabs[1].matches("[ATCGatcg]+") && tabs[1].length()>15 && tabs[1].length()<120) {
                String description = "n/a";
                if(tabs.length>2) {
                    description = tabs[2];
                }
                Oligo oligo = new Oligo(tabs[0], tabs[1].toUpperCase(), description);
                nameToOligo.put(oligo.getName(), oligo);
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        OligoScanner scanner = new OligoScanner();
        scanner.initiate();
        Map<String,Oligo> nameToOligos = scanner.run();
        for(String name : nameToOligos.keySet()) {
            Oligo oligo = nameToOligos.get(name);
            System.out.println(name + " " + oligo.getSequence() + " " + oligo.getDescription());
        }
    }
}
