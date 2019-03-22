package org.twentyn.c5.construction.model;

import org.twentyn.c5.utils.SequenceUtils;

/**
 * A Polynucleotide models the chemical entity described by that term.  It is adapted
 * from Clotho3, but it is a new write.
 * 
 * It is in distinction fromm NucSeq, which didn't correspond to any classical abstraction.
 * This is not an informational or pragmatic abstraction like a part, or a plasmid.  It seeks
 * to describe a DNA or RNA molecule as a chemical entity in all its glory, including modified
 * bases, sticky ends, circularity, strandedness and the like.
 * 
 * @author J. Christopher Anderson
 */
public class Polynucleotide {
    private final String sequence;
    
    /*
    The ext5 variable corresponds to the lefthand side
    It does not imply that the extension is a 5' overhang
    but simply that it is the 5' side of current coding strand
    
    To indicate a 5' overhang, you use "GATC" like with BamHI
    To indicate a 3' overhang on the 5' end of the coding strand,
    you would sqy "-CTAG", as for PstI
    */
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
        this(dnaseq, "", "");
    }

    /**
     * Convenience constructor for fragment with sticky ends
     * @param sequence
     * @param ext5
     * @param ext3 
     */
    public Polynucleotide(String sequence, String ext5, String ext3) {
        this(sequence, ext5, ext3, true, false, true);
//        isDoubleStranded = true;
//        isRNA = false;
//        isCircular = false;
        
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
            for(int i=0; i<this.ext5.length() - 1; i++) {
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
            out+= this.ext3.substring(1);
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
            for(int i=0; i<this.ext3.length() - 1; i++) {
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
        
        {
        System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
        String ext5 = "GATC";
        String ext3 = "AATT";
        Polynucleotide poly2 = new Polynucleotide("caaacccg", ext5, ext3);
        System.out.println(poly2.toString());
        }
        
        {
        System.out.println("Demo a PstI/BseRI digested DNA with 3' overhangs");
        String ext5 = "-TGCA";
        String ext3 = "-CC";
        Polynucleotide poly3 = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", ext5, ext3);
        System.out.println(poly3.toString());
        }
    }
}
