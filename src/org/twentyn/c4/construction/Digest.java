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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.twentyn.c4.schemas.Polynucleotide;
import org.twentyn.c4.utils.SequenceUtils;

/**
 *
 * @author jca20n
 */
public class Digest {
    Polynucleotide substrate;
    List<RestrictionEnzyme> enzymes;
    
    public Digest(Polynucleotide substrate, List<RestrictionEnzyme> enzymes) {
        this.substrate = substrate;
        this.enzymes = enzymes;
    }

    public List<Polynucleotide> getProducts() {
        List<Polynucleotide> out = new ArrayList<>();
        
        out.add(substrate);
        
        //Iteratively cut the substrate with each enzyme
        for(RestrictionEnzyme enz : enzymes) {
            //Move all fragments into temp
            List<Polynucleotide> temp = new ArrayList<>();
            temp.addAll(out);
            out.clear();
            
            //Scan through the first strand, breaking into fragments
            for(Polynucleotide poly : temp) {
                List<Polynucleotide> frags = cutOneSide(poly, enz);
                for(Polynucleotide frag : frags) {
                    Polynucleotide rc = revcomp(frag);
                    List<Polynucleotide> rcfrags = cutOneSide(rc, enz);
                    out.addAll(rcfrags);
                }
            }
        }
        return out;
    }
    
    /**
     * Exhaustively cut one polynucleotide with one restriction enzyme, and 
     * return the fragments as a list including their sticky ends
     * @param sub
     * @param enz
     * @return 
     */
    private List<Polynucleotide> cutOneSide(Polynucleotide sub, RestrictionEnzyme enz) {
        List<Polynucleotide> out = new ArrayList<>();
        RestrictionData data = RestrictionData.getFor(enz);
        Pattern p = Pattern.compile(data.site);
        Matcher m = p.matcher(sub.getSequence());
        
        Polynucleotide remaining = new Polynucleotide(sub.getSequence(), sub.getExt5(), sub.getExt3());
        
        while(m.find()) {
            int start = m.start();
            int end = m.end();
            
            //Construct the 3' remaining fragment
            String remainingSequence = sub.getSequence().substring(m.start() + data.cut3);
            String remainingExt5 = sub.getSequence().substring(start + data.cut5, start + data.cut3);
            
            String newfrag = sub.getSequence().substring(0, start + data.cut5);
            
            String fragExt3 = sub.getSequence().substring(start + data.cut5, start + data.cut3);
            Polynucleotide frag = new Polynucleotide(newfrag, sub.getExt5(), fragExt3);
            out.add(frag);
        }
        out.add(remaining);
        return out;
    }

    private Polynucleotide revcomp(Polynucleotide frag) {
        String rc = SequenceUtils.reverseComplement(frag.getSequence());
        
        
        //Transfer the 3' end
        String new5 = SequenceUtils.reverseComplement(frag.getExt3());
        String outext3 = SequenceUtils.reverseComplement(frag.getExt5());
        String outext5 = new5;
        
        Polynucleotide out = new Polynucleotide(rc, outext5, outext3);
        return out;
    }
    
    public static void main(String[] args) {
        List<RestrictionEnzyme> enz = new ArrayList<>();
        Polynucleotide poly = new Polynucleotide("aaaaGGTCTCtCCCCttCAATTGccccc");
//        enz.add(RestrictionEnzyme.MfeI);
        enz.add(RestrictionEnzyme.BsaI);
        
        Digest dig = new Digest(poly, enz);
        List<Polynucleotide> pdts = dig.getProducts();
       
        for(Polynucleotide pol : pdts) {
            System.out.println(pol.toString());
        }
        
        System.out.println("number of products: " + pdts.size());
        
    }
}
