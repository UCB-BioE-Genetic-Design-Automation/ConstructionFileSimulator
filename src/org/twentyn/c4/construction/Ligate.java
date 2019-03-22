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
            seq.append(poly.getExt5());
            seq.append(poly.getSequence());
        }
        
        Polynucleotide poly =  polys.get(0);
        seq.append(poly.getExt5());
        seq.append(poly.getSequence());
        
        Polynucleotide out = new Polynucleotide(seq.toString());
        return out;
    }
    
    public static void main(String[] args) throws Exception {
        String BamHIExt = "-GATC";
        
        Polynucleotide poly1 = new Polynucleotide("gatccaaaaaa", BamHIExt, "");
        Polynucleotide poly2 = new Polynucleotide("ccccccggatc", "", BamHIExt);

        System.out.println("poly1:\n" + poly1);
        System.out.println("poly2:\n" + poly2);
        
        List<Polynucleotide> listy = new ArrayList<>();
        listy.add(poly1);
        listy.add(poly2);
        
        Ligate lig = new Ligate(listy);
        Polynucleotide pdt = lig.getProduct();
        
        System.out.println(pdt.toString());
        
    }
}
