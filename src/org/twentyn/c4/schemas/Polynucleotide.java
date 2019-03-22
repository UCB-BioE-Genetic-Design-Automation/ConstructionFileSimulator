/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.twentyn.c4.schemas;

import org.twentyn.c4.utils.SequenceUtils;

/**
 * A Polynucleotide models the chemical entity described by that term.  It is adapted
 * from Clotho3, but it is a new write.
 * 
 * It is in distinction fromm NucSeq, which didn't correspond to any classical abstraction.
 * This is not an informational or pragmatic abstraction like a part, or a plasmid.  It seeks
 * to describe a DNA or RNA molecule as a chemical entity in all its glory, including modified
 * bases, sticky ends, circularity, strandedness and the like.
 * 
 * @author jca20n
 */
public class Polynucleotide {
    private final String sequence;
    private final String ext5;
    private final String ext3;
    private final boolean isDoubleStranded;
    private final boolean isRNA;
    private final boolean isCircular;

    /**
     * Full Constructor
     * 
     * @param sequence
     * @param ext5
     * @param ext3
     * @param isDoubleStranded
     * @param isRNA
     * @param isCircular 
     */
    public Polynucleotide(String sequence, String ext5, String ext3, boolean isDoubleStranded, boolean isRNA, boolean isCircular) {
        this.sequence = sequence;
        this.ext5 = ext5;
        this.ext3 = ext3;
        this.isDoubleStranded = isDoubleStranded;
        this.isRNA = isRNA;
        this.isCircular = isCircular;
    }
    
    /**
     * Convenience constructor defaults to blunt, double stranded DNA, which can then be edited
     * @param dnaseq 
     */
    public Polynucleotide(String dnaseq) {
        this.sequence = dnaseq.toUpperCase();
        this.ext5 = "";
        this.ext3 = "";
        isDoubleStranded = true;
        isRNA = false;
        isCircular = false;
    }

    /**
     * Convenience constructor for fragment with sticky ends
     * @param sequence
     * @param ext5
     * @param ext3 
     */
    public Polynucleotide(String sequence, String ext5, String ext3) {
        this.sequence = sequence;
        this.ext5 = ext5;
        this.ext3 = ext3;
        isDoubleStranded = true;
        isRNA = false;
        isCircular = false;
    }

    public String getSequence() {
        return sequence;
    }

    public String getExt5() {
        return ext5;
    }

    public String getExt3() {
        return ext3;
    }

    public boolean isIsDoubleStranded() {
        return isDoubleStranded;
    }

    public boolean isIsRNA() {
        return isRNA;
    }

    public boolean isIsCircular() {
        return isCircular;
    }
    
    public String toString() {
        String out = "";
        out += "5'-";
        if(this.ext5.startsWith("-")) {
            for(int i=0; i<this.ext5.length(); i++) {
                out += " ";
            }
        } else {
            out+= this.ext5;
        }
        
        out += this.sequence;
        
        if(!this.ext3.startsWith("-")) {
            for(int i=0; i<this.ext3.length(); i++) {
                out += " ";
            }
        } else {
            out+= this.ext3;
        }
        
        out += "-3'\n";
        
        //Do the other strand
        out += "3'-";
        if(!this.ext5.startsWith("-")) {
            for(int i=0; i<this.ext5.length(); i++) {
                out += " ";
            }
        } else {
            out+= SequenceUtils.complement(this.ext5);
        }
        
        out += SequenceUtils.complement(this.sequence);
        
        if(this.ext3.startsWith("-")) {
            for(int i=0; i<this.ext3.length(); i++) {
                out += " ";
            }
        } else {
            out+= SequenceUtils.complement(this.ext3);
        }
        
        out += "-5'\n";
        
        return out;
    }
    
    public static void main(String[] args) {
        System.out.println("Demo a blunt DNA, like a PCR product");
        Polynucleotide poly = new Polynucleotide("CTAGTttgacggctagcG");
        System.out.println(poly.toString());
        
        //This is an edge case that fails
//        poly.ext5 = "-CTAG";
//        poly.ext3 = "-AATT";
        
        System.out.println("Demo a BamHI/EcoRI digested DNA with sticky ends");
        String ext5 = "GATC";
        String ext3 = "AATT";
        Polynucleotide poly2 = new Polynucleotide("caaacccg", ext5, ext3);

        System.out.println(poly2.toString());
    }
}
