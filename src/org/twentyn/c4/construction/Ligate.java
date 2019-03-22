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
    
    public void initiate() throws Exception {}
    
    public Polynucleotide run(List<Polynucleotide> polys) throws Exception {
        //This needs to relay to an internal private function
        //Needs to check ends
        //Needs to keep track of current ends
        //Needs to throw exception if not all frags collapse to one
        //Needs to handle the - for 3' products
        
        
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
        {
        System.out.println("Two BamHI-digested DNAs:");
        String BamHIExt = "GATC";
        
        Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "");
        Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt);

        System.out.println("poly1:\n" + poly1);
        System.out.println("poly2:\n" + poly2);
        
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);
        
        Ligate lig = new Ligate();
        lig.initiate();
        Polynucleotide pdt = lig.run(frags);
        
        System.out.println("Ligation product:");
        System.out.println(pdt.toString());
        }
        
        {
        System.out.println("Two BamHI-digested DNAs in other orientation:");
        String BamHIExt = "GATC";
        
        Polynucleotide poly1 = new Polynucleotide("ttttttG", "", BamHIExt);
        Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt);

        System.out.println("poly1:\n" + poly1);
        System.out.println("poly2:\n" + poly2);
        
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);
        
        Ligate lig = new Ligate();
        lig.initiate();
        Polynucleotide pdt = lig.run(frags);
        
        System.out.println("Ligation product:");
        System.out.println(pdt.toString());
        }
        
        {
        System.out.println("Two PstI-digested DNAs:");
        String PstIExt = "-TGCA";
        
        Polynucleotide poly1 = new Polynucleotide("Gaaaaaa", PstIExt, "");
        Polynucleotide poly2 = new Polynucleotide("ccccccC", "", PstIExt);

        System.out.println("poly1:\n" + poly1);
        System.out.println("poly2:\n" + poly2);
        
        List<Polynucleotide> frags = new ArrayList<>();
        frags.add(poly1);
        frags.add(poly2);
        
        Ligate lig = new Ligate();
        lig.initiate();
        Polynucleotide pdt = lig.run(frags);
        
        System.out.println("Ligation product:");
        System.out.println(pdt.toString());
        }
        
    }
}
