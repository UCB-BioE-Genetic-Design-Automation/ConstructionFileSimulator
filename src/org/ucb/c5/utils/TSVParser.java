/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses a TSV file into a list of objects expressed as key-value pairs
 * @author J. Christopher Anderson
 */
public class TSVParser {
    
    public List<Map<String,String>> run(String filedata) {
        //Split the data into lines
        String[] lines = filedata.split("\\r|\\r?\\n");
        
        //Split the first line which contains the terms
        String[] terms = lines[0].split("\t");
        
        //Construct Maps of data for the remaining lines and bundle up
        List<Map<String,String>> out = new ArrayList<>();
        for(int i=1; i<lines.length; i++) {
            String[] values = lines[i].split("\t");
            Map<String,String> obj = new HashMap<>();
            for(int x=0; x<terms.length; x++) {
                obj.put(terms[x], values[x]);
            }
            out.add(obj);
        }
        
        return out;
    }
}
