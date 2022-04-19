/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.sequtils;

import org.ucb.c5.constructionfile.model.Modifications;
import org.ucb.c5.constructionfile.model.Polynucleotide;

/**
 *
 * @author michaelfernandez
 */
public class ComparePolynucleotides {

    private PolyRevComp prc;
    private RevComp revcomp;

    public void initiate() throws Exception {
        prc = new PolyRevComp();
        prc.initiate();
        
        revcomp = new RevComp();
        revcomp.initiate();
        
    }

    //Testing if two circular polynucleotides are equal
    private boolean testCircular(Polynucleotide polyA, Polynucleotide polyB) throws Exception {
        
        //Confirm that all modifications are circular
        if (polyA.getMod5() != Modifications.circular) {
            return false;
        }
        if (polyA.getMod3() != Modifications.circular) {
            return false;
        }
        if (polyB.getMod5() != Modifications.circular) {
            return false;
        }
        if (polyB.getMod3() != Modifications.circular) {
            return false;
        }

        if (polyA.isIsDoubleStranded() != polyB.isIsDoubleStranded()) {
            return false;
        }
        if (polyA.isIsRNA() != polyB.isIsRNA()) {
            return false;
        }
        
        String seqA = polyA.getSequence().toLowerCase();
        String seqB = polyB.getSequence().toLowerCase();
        
        String comboA = seqA.concat(seqA);
        
        
        if (comboA.contains(seqB)){ 
            return true;
        }
        
        String rc = revcomp.run(seqB);
        
        return comboA.contains(rc);
        
    }

    //Testing if two linear polynucleotides are equal
    private boolean testLinear(Polynucleotide polyA, Polynucleotide polyB) throws Exception {

        if (polyA.getMod5() != polyB.getMod5()) {
            return false;
        }
        if (polyA.getMod3() != polyB.getMod3()) {
            return false;
        }
        if (!polyA.getExt5().equals(polyB.getExt5())) {
            return false;
        }
        if (!polyA.getExt3().equals(polyB.getExt3())) {
            return false;
        }
        if (!polyA.getSequence().toLowerCase().equals(polyB.getSequence().toLowerCase())) {
            return false;
        }
        if (polyA.isIsDoubleStranded() != polyB.isIsDoubleStranded()) {
            return false;
        }
        if (polyA.isIsRNA() != polyB.isIsRNA()) {
            return false;
        }

        return true;
    }

    public boolean run(Polynucleotide polyA, Polynucleotide polyB) throws Exception {
        //if it is not circular
        if (polyA.isCircular()) {
            return testCircular(polyA, polyB);
        }

        if (testLinear(polyA, polyB)) {
            return true;
        }
        Polynucleotide rc = prc.run(polyA);

        return testLinear(rc, polyB);
    }
    
    public static void main(String[] args) throws Exception {
        
            ComparePolynucleotides cps = new ComparePolynucleotides();
            cps.initiate();
            
            System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polynucleotide polyA = new Polynucleotide("caaacccg", ext5, ext3, true, false, false, Modifications.phos5, Modifications.phos5);
            System.out.println(polyA.toString());
            
            System.out.println("Changing case of sequence (True)");
            
            Polynucleotide polyB = new Polynucleotide("caaacccg".toUpperCase(), ext5, ext3, true, false, false, Modifications.phos5, Modifications.phos5);
            System.out.println(polyB.toString());
            
            System.out.println(cps.run(polyA, polyB));
            
            
            System.out.println("Changing mod5 of the sequence (False)");
            
            
            Polynucleotide polyC = new Polynucleotide("caaacccg", ext5, ext3, true, false, false, Modifications.hydroxyl, Modifications.phos5);
            System.out.println(polyC.toString());
            
            System.out.println(cps.run(polyA, polyC));
 
            //Circular and rotated: true
            
            //Circular and reverse complemented: true
            
            //Circular vs single stranded:false
            
            
            
            
    }
        
}
