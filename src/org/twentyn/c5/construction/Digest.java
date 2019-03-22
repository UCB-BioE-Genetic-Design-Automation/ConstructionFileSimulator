/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.twentyn.c5.construction;

import org.twentyn.c5.construction.model.RestrictionEnzyme;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.twentyn.c5.construction.RestrictionEnzymeFactory.Enzyme;
import org.twentyn.c5.construction.model.Polynucleotide;
import org.twentyn.c5.utils.SequenceUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class Digest {

    public List<Polynucleotide> run(Polynucleotide substrate, List<RestrictionEnzyme> enzymes) throws Exception {
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
    private List<Polynucleotide> cutOneSide(Polynucleotide sub, RestrictionEnzyme data) {
        List<Polynucleotide> out = new ArrayList<>();
        Pattern p = Pattern.compile(data.getSite());
        Matcher m = p.matcher(sub.getSequence().toUpperCase());
        
        Polynucleotide remaining = new Polynucleotide(sub.getSequence(), sub.getExt5(), sub.getExt3());
        
        while(m.find()) {
            int start = m.start();
            int end = m.end();
            
            //Construct the 3' remaining fragment
            String remainingSequence = sub.getSequence().substring(m.start() + data.getCut3());
            String remainingExt5 = sub.getSequence().substring(start + data.getCut5(), start + data.getCut3());
            String newfrag = sub.getSequence().substring(0, start + data.getCut5());
            String fragExt3 = sub.getSequence().substring(start + data.getCut5(), start + data.getCut3());
            
            Polynucleotide frag = new Polynucleotide(newfrag, sub.getExt5(), fragExt3);
            out.add(frag);
        }
        out.add(remaining);
        return out;
    }

    private Polynucleotide revcomp(Polynucleotide frag) {
        String rc = SequenceUtils.reverseComplement(frag.getSequence());
        
        //Transfer the 3' end
        String outext3 = SequenceUtils.reverseComplement(frag.getExt5());
        String outext5 = SequenceUtils.reverseComplement(frag.getExt3());
        
        Polynucleotide out = new Polynucleotide(rc, outext5, outext3);
        return out;
    }
    
    public static void main(String[] args) throws Exception {
        List<RestrictionEnzyme> enz = new ArrayList<>();
        Polynucleotide poly = new Polynucleotide("aaaaGGTCTCtCCCCttCAATTGccccc");
        
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        
        enz.add(factory.run(Enzyme.BsaI));
        
        Digest dig = new Digest();
        List<Polynucleotide> pdts = dig.run(poly, enz);
       
        for(Polynucleotide pol : pdts) {
            System.out.println(pol.toString());
        }
        
        System.out.println("number of products: " + pdts.size());
        
    }
}
