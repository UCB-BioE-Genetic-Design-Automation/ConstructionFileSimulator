package org.twentyn.c5.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.twentyn.c5.construction.model.Polynucleotide;

/**
 *
 * @author J. Christopher Anderson
 */
public class Ligate {

    private PolyRevComp revcomp;
    
    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
    }

    public Polynucleotide run(List<Polynucleotide> polys) throws Exception {
        //This needs to relay to an internal private function
        //Needs to check ends
        //Needs to keep track of current ends
        //Needs to throw exception if not all frags collapse to one
        //Needs to handle the - for 3' products

        StringBuilder seq = new StringBuilder();
        for (int i = 1; i < polys.size(); i++) {
            Polynucleotide poly = polys.get(i);
            seq.append(poly.getExt5());
            seq.append(poly.getSequence());
        }

        Polynucleotide poly = polys.get(0);
        seq.append(poly.getExt5());
        seq.append(poly.getSequence());

        Polynucleotide out = new Polynucleotide(seq.toString());
        return out;
    }

    /**
     * Dpes one ligation event on two Polynuceotides
     *
     * If they can be ligated, it returns the product. If they cannot be joined,
     * it returns null
     *
     * @param poly1
     * @param poly2
     * @return
     * @throws Exception
     */
    private Polynucleotide join(Polynucleotide p1, Polynucleotide p2) throws Exception {
        //Get them in the right orientation to ligate
        Polynucleotide poly1 = null;
        Polynucleotide poly2 = null;
        
        if(p1.getExt3().equals(p2.getExt5())) {
            poly1 = p1;
            poly2 = p2;
        } else if(p1.getExt5().equals(p2.getExt3())) {
            poly1 = p2;
            poly2 = p1;
        } else {
            Polynucleotide p2rc = revcomp.run(p2);
        }
        
        
        String newseq = "";
        newseq += poly1.getSequence();

        newseq += poly2.getSequence();

        Polynucleotide out = new Polynucleotide(newseq, poly1.getExt5(), poly2.getExt3());
        return out;
    }

    public static void main(String[] args) throws Exception {
        {
            System.out.println("Two BamHI-digested DNAs:");
            String BamHIExt = "GATC";

            Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "");
            Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Ligate lig = new Ligate();
            lig.initiate();
            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Two BamHI-digested DNAs in other orientation:");
            String BamHIExt = "GATC";

            Polynucleotide poly1 = new Polynucleotide("ttttttG", "", BamHIExt);
            Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Ligate lig = new Ligate();
            lig.initiate();
            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Two PstI-digested DNAs:");
            String PstIExt = "-TGCA";

            Polynucleotide poly1 = new Polynucleotide("Gaaaaaa", PstIExt, "");
            Polynucleotide poly2 = new Polynucleotide("ccccccC", "", PstIExt);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Ligate lig = new Ligate();
            lig.initiate();
            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

    }
}
