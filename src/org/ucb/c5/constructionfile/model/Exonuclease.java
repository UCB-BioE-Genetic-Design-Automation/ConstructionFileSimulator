package org.ucb.c5.constructionfile.model;

import org.ucb.c5.utils.SequenceUtils;

/**
 *
 * @author yisheng chen
 */
public class Exonuclease{
    
    private final String sequence;
    private final String ext5;
    private final String ext3;

    
    public Exonuclease(String sequence, String ext5, String ext3) {
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
    

//       
//    public String toString() {
//        String separator = "-";
//     
//        String out = "";
//        out += "5'" + separator;
//        
//        for (int i = 0; i < this.ext5.length() - 1; i++) {
//            out += " ";    
//        } 
//
//        out += this.sequence;
//
//        for (int i = 1; i < this.ext3.length(); i++) {
//            out += " ";
//        }
//
//        out += separator + "3'\n";
//
//        //Do the other strand
//        out += "3'" + separator;
//        for (int i = 1; i < this.ext5.length(); i++) {
//            out += " ";
//        }
//
//        out += SequenceUtils.complement(this.sequence);
//
//        for (int i = 0; i < this.ext3.length() - 1; i++) {
//            out += " ";
//        }
//
//        out += separator + "5'\n";
//
//        return out;
//    }
    
    public static void main(String[] args) {
        
        {
            System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Exonuclease poly1 = new Exonuclease("caaacccg", ext5, ext3);
            System.out.println(poly1.toString());
        }

        {
            System.out.println("Demo a PstI/BseRI digested DNA with 3' overhangs");
            String ext5 = "-TGCA";
            String ext3 = "-CC";
            Exonuclease poly2 = new Exonuclease("gaaacccGAGGAGaaaaaaaa", ext5, ext3);
            System.out.println(poly2.toString());
        }
               
    } 
}