/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.twentyn.c4.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.twentyn.c4.schemas.Polynucleotide;

/**
 *
 * @author jca20n
 */
public class Ligate {
    List<Polynucleotide> polys;
    
    public Ligate(List<Polynucleotide> polys) {
        this.polys = polys;
    }
    
    public Polynucleotide getProduct() throws Exception {
        StringBuilder seq = new StringBuilder();
        for(int i=1; i<polys.size(); i++) {
            Polynucleotide poly =  polys.get(i);
            seq.append(poly.ext5);
            seq.append(poly.sequence);
        }
        
        Polynucleotide poly =  polys.get(0);
        seq.append(poly.ext5);
        seq.append(poly.sequence);
        
        Polynucleotide out = new Polynucleotide(seq.toString());
        return out;
    }
    
    public static void main(String[] args) throws Exception {
        Polynucleotide poly1 = new Polynucleotide("tttttG");
        poly1.ext3 = "GATC";
        
        Polynucleotide poly2 = new Polynucleotide("Caaaaaa");
        poly1.ext5 = "GATC";
        
        List<Polynucleotide> listy = new ArrayList<>();
        listy.add(poly1);
        listy.add(poly2);
        
        Ligate lig = new Ligate(listy);
        Polynucleotide pdt = lig.getProduct();
        
        System.out.println(pdt.toString());
        
    }
}
