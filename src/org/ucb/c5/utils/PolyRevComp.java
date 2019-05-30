package org.ucb.c5.utils;

import org.ucb.c5.constructionfile.model.Polynucleotide;

/**
 * Reverse complement Function for a Polynucleotide
 * 
 * @author J. Christopher Anderson
 */
public class PolyRevComp {

    public void initiate() throws Exception {
    }

    public Polynucleotide run(Polynucleotide frag) throws Exception {
        String rc = SequenceUtils.reverseComplement(frag.getSequence());

        String new5 = null;
        String new3 = null;

        if (frag.getExt3().startsWith("-")) {
            String old3 = frag.getExt3().substring(1);
            new5 = "-" + SequenceUtils.reverseComplement(old3);
        } else {
            new5 = SequenceUtils.reverseComplement(frag.getExt3());
        }

        if (frag.getExt5().startsWith("-")) {
            String old5 = frag.getExt5().substring(1);
            new3 = "-" + SequenceUtils.reverseComplement(old5);
        } else {
            new3 = SequenceUtils.reverseComplement(frag.getExt5());
        }

        Polynucleotide out = new Polynucleotide(rc, new5, new3);
        return out;
    }

    public static void main(String[] args) throws Exception {
        PolyRevComp revcomp = new PolyRevComp();

        {
            System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polynucleotide poly = new Polynucleotide("caaacccg", ext5, ext3);
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
    }
}
