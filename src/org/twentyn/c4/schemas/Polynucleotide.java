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
    public String sequence;
    public String ext5;
    public String ext3;
    public boolean isDoubleStranded = true;
    public boolean isRNA = false;
    public boolean isCircular = false;
    
    /**
     * The constructor defaults to blunt, double stranded DNA, which can then be edited
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
    
    public String toString() {
        String out = "";
        out += "5'-";
        if(!this.ext5.startsWith("-")) {
            for(int i=0; i<this.ext5.length(); i++) {
                out += " ";
            }
        }
        out += this.sequence;
        out += "-3'\n";
        
        out += "3'-";
        if(this.ext5.startsWith("-")) {
            for(int i=0; i<this.ext5.length()-1; i++) {
                out += " ";
            }
        }
        
        //Do the start
        int start = 0;
        if(this.ext5.startsWith("-")) {
            int offset = this.ext3.length();
            for(int i=0; i<offset; i++) {
                out += "";
                start = offset;
            }
        } else {
            out += this.ext5;
            start = 0;
        }
        
        //do the end
        int end = -1;

        if(ext3.startsWith("-")) {
            end = this.sequence.length() - this.ext3.length() -1;
            out += SequenceUtils.reverseComplement(sequence.substring(start,end));
        } else {
            out += SequenceUtils.complement(sequence.substring(start));
            out += this.ext3;
        }
        return out + "-5'\n";
    }
    
    public static void main(String[] args) {
        Polynucleotide poly = new Polynucleotide("CTAGTttgacggctagcG");
        
        //This is an edge case that fails
//        poly.ext5 = "-CTAG";
//        poly.ext3 = "-AATT";
        
        //This works
        poly.ext5 = "-CTAG";
        poly.ext3 = "AATT";
        
        System.out.println(poly.toString());
    }
}
