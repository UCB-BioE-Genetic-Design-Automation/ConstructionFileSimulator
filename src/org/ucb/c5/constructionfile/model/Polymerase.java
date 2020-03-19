/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.model;

import org.ucb.c5.utils.SequenceUtils;

/**
 *
 * @author yisheng chen
 */
public class Polymerase{
    
    private final String sequence;
    private final String ext5;
    private final String ext3;
    
    public Polymerase(String sequence, String ext5, String ext3) {
        this.sequence = sequence;
        this.ext5 = ext5;
        this.ext3 = ext3;

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
    
    
        public String toString() {
        String separator = "-";
       
        String out = "";
        out += "5'" + separator;
        if (this.ext5.startsWith("-")) {
            out += this.ext5.substring(1);
        } else {            
            out += this.ext5;
        }

        out += this.sequence;

        if (!this.ext3.startsWith("-")) {
            out += this.ext3;
        } else {
            out += this.ext3.substring(1);
        }

        out += separator + "3'\n";

        //Do the other strand
        out += "3'" + separator;
        out += SequenceUtils.complement(this.ext5);
        out += SequenceUtils.complement(this.sequence);
        out += SequenceUtils.complement(this.ext3);
        out += separator + "5'\n";
        
        return out;
    }
    
    public static void main(String[] args) {
        
        {
            System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polymerase poly1 = new Polymerase("caaacccg", ext5, ext3);
            System.out.println(poly1.toString());
        }

        {
            System.out.println("Demo a PstI/BseRI digested DNA with 3' overhangs");
            String ext5 = "-TGCA";
            String ext3 = "-CC";
            Polymerase poly2 = new Polymerase("gaaacccGAGGAGaaaaaaaa", ext5, ext3);
            System.out.println(poly2.toString());
        }
               
    }
}