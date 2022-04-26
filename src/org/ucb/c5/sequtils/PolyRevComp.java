package org.ucb.c5.sequtils;

import org.ucb.c5.constructionfile.model.Modifications;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.utils.SequenceUtils;

/**
 * Reverse complement Function for a Polynucleotide
 * 
 * @author J. Christopher Anderson
 */
public class PolyRevComp {
    private RevComp revcomp;

    public void initiate() throws Exception {
        revcomp = new RevComp();
        revcomp.initiate();
    }

    public Polynucleotide run(Polynucleotide frag) throws Exception {          
        String rc = revcomp.run(frag.getSequence());

        String new5 = null;
        String new3 = null;

        if (frag.getExt3().startsWith("-")) {
            String old3 = frag.getExt3().substring(1);
            new5 = "-" + revcomp.run(old3);
        } else {
            new5 = revcomp.run(frag.getExt3());
        }

        if (frag.getExt5().startsWith("-")) {
            String old5 = frag.getExt5().substring(1);
            new3 = "-" + revcomp.run(old5);
        } else {
            new3 = revcomp.run(frag.getExt5());
        }
        
        
        //flip the modifications
        Polynucleotide out = new Polynucleotide(rc, new5, new3,frag.isIsDoubleStranded(),false,false,frag.getMod3(),frag.getMod5());
        
        
        return out;
    }

    public static void main(String[] args) throws Exception {
        PolyRevComp revcomp = new PolyRevComp();
        revcomp.initiate();
        {
            System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polynucleotide poly = new Polynucleotide("caaacccg", ext5, ext3);
            
            System.out.println("poly.tostring");
            System.out.println(poly.toString());

            Polynucleotide rc = revcomp.run(poly);
            System.out.println(rc.toString());
        }

        {
            System.out.println("Demo a PstI/BseRI digested DNA with 3' overhangs");
            String ext5 = "-TGCA";
            String ext3 = "-CC";
            Polynucleotide poly = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", ext5, ext3);
            System.out.println(poly.toString());

            Polynucleotide rc = revcomp.run(poly);
            System.out.println(rc.toString());
        }

        {
            System.out.println("Demo a BsaI digested DNA with 5' overhangs");
            String ext5 = "CCCT";
            String ext3 = "GAAA";
            Polynucleotide poly = new Polynucleotide("ttttt", ext5, ext3);
            System.out.println(poly.toString());

            Polynucleotide rc = revcomp.run(poly);
            System.out.println(rc.toString());
        }
        
        {
            System.out.println("Demo a synthetic DNA with modified ends");
            String ext5 = "CCCT";
            String ext3 = "-GAAA";
            Polynucleotide poly = new Polynucleotide("ttttt", ext5, ext3,true,false,false,Modifications.ILink12,Modifications.ATTO590N);
            System.out.println(poly.toString());

            Polynucleotide rc = revcomp.run(poly);
            System.out.println(rc.toString());
            
            
        }
    }
}
