/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class Trashy {
    public static void main(String[] args) throws Exception {
        Map<String, Set<Integer>> ecToChems = new HashMap<>();
        String data = FileUtils.readFile("/Users/jca20n/NetBeansProjects/BioE132_Act/brenda_reactions.txt");
        String[] lines = data.split("\\r|\\r?\\n");
        for(int x=1; x<lines.length; x++) {
            String[] tabs = lines[x].split("\t");
            String ec = tabs[1];
            String schem = tabs[2];
            String[] chems = schem.split("\\s");
            Set<Integer> ichems = ecToChems.get(ec);
            if(ichems==null) {
                ichems = new HashSet<>();
            }
            for(String str : chems) {
                Integer i = Integer.parseInt(str);
                ichems.add(i);
            }
            ecToChems.put(ec, ichems);
        }
        
        String best = null;
        int bestsize = 0;
        for(String ec : ecToChems.keySet()) {
            Set<Integer> ichems = ecToChems.get(ec);
            if(ichems.size() > bestsize) {
                best = ec;
                bestsize = ichems.size();
            }
        }
        
        System.out.println(best + " " + bestsize);
    }
}
