package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.BluntingType;

/**
 *
 * @author yisheng
 * @edit Zihang Shao
 */
public class BluntingSimulator {
    
    public void initiate(){
        
    }
    
    public Polynucleotide run(Polynucleotide substrate, BluntingType type) throws Exception {
        
        Polynucleotide out = null;
        
        switch (type){
            case Polymerase:
                out = Polymerase(substrate);
                break;
            case Exonuclease:
                out = Exonuclease(substrate);
                break;
        }
                   
    return out;    
    }
    
    private Polynucleotide Polymerase(Polynucleotide sub){
        String sequence = sub.getSequence();
        String ext3 = sub.getExt3();
        String ext5 = sub.getExt5();
        
       
        String remaining = "";
        if (!ext5.startsWith("-")) {
            remaining += ext5;
            ext5 = "";
        }

        remaining += sequence;

        if (!ext3.startsWith("-")) {
            remaining += ext3;
            ext3 = "";
        }
        
        Polynucleotide product = new Polynucleotide( remaining, ext3, ext5 );
        return product;
    }
    
    public Polynucleotide Exonuclease(Polynucleotide sub){
        String sequence = sub.getSequence();

        String remaining = sequence;
        
        Polynucleotide product = new Polynucleotide( remaining, "", "");
        return product;
    }
    
    public static void main(String[] args) throws Exception{
        
        
        {
            System.out.println("Demo the blunting of a BamHI/EcoRI digested DNA with 5' sticky ends using Polymerase");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polynucleotide poly1 = new Polynucleotide("caaacccg", ext5, ext3);
            BluntingSimulator blu1 = new BluntingSimulator();
            blu1.initiate();
            System.out.println(blu1.run(poly1, BluntingType.Polymerase).toString());
        }

        {
            System.out.println("Demo the blunting of a PstI/BseRI digested DNA with 3' overhangs using Polymerase");
            String ext5 = "-TGCA";
            String ext3 = "-CC";
            Polynucleotide poly2 = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", ext5, ext3);
            BluntingSimulator blu2 = new BluntingSimulator();
            blu2.initiate();
            System.out.println(blu2.run(poly2, BluntingType.Polymerase).toString());
        }
        

        
        {
            System.out.println("Demo the blunting of a BamHI/EcoRI digested DNA with 5' sticky ends using Exonuclease");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polynucleotide poly3 = new Polynucleotide("caaacccg", ext5, ext3);
            BluntingSimulator blu3 = new BluntingSimulator();
            blu3.initiate();
            System.out.println(blu3.run(poly3, BluntingType.Exonuclease));
        }

        {
            System.out.println("Demo the blunting of a PstI/BseRI digested DNA with 3' overhangs using Exonuclease");
            String ext5 = "-TGCA";
            String ext3 = "-CC";
            Polynucleotide poly4 = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", ext5, ext3);
            BluntingSimulator blu4 = new BluntingSimulator();
            blu4.initiate();
            System.out.println(blu4.run(poly4, BluntingType.Exonuclease));
        }
               
    } 
}
        
        
    
    
    

