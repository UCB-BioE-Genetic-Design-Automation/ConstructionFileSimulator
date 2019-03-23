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
        List<Polynucleotide> worklist = new ArrayList<>();
        List<Polynucleotide> temp = new ArrayList<>(polys);

        outer:
        while (true) {
            worklist.clear();
            worklist.addAll(temp);

            //Start iterating through all pairwise joins
            for (int i = 0; i < worklist.size() - 1; i++) {
                Polynucleotide p1 = worklist.get(i);
                for (int j = i + 1; j < worklist.size(); j++) {
                    Polynucleotide p2 = worklist.get(j);
                    Polynucleotide result = join(p1, p2);
                    if (result != null) {

                        temp.remove(p1);
                        temp.remove(p2);
                        temp.add(result);
                        continue outer;
                    }
                }
            }

            break outer;
        }

        if (temp.size() != 1) {
            throw new Exception("Fragments to not ligate into a single product");
        }

        return temp.get(0);
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

        if (p1.getExt3().equals(p2.getExt5())) {
            poly1 = p1;
            poly2 = p2;
        } else if (p1.getExt5().equals(p2.getExt3())) {
            poly1 = p2;
            poly2 = p1;
        } else {
            Polynucleotide p2rc = revcomp.run(p2);
            if (p1.getExt3().equals(p2rc.getExt5())) {
                poly1 = p1;
                poly2 = p2rc;
            } else if (p1.getExt5().equals(p2rc.getExt3())) {
                poly1 = p2rc;
                poly2 = p1;

                //Otherwise they do not ligate, so return null
            } else {
                return null;
            }
        }

        //If got this far, they match
        String newseq = "";
        newseq += poly1.getSequence();
        newseq += poly1.getExt3().replaceAll("-", "");
        newseq += poly2.getSequence();

        Polynucleotide out = new Polynucleotide(newseq, poly1.getExt5(), poly2.getExt3());
        return out;
    }

    public static void main(String[] args) throws Exception {

        Ligate lig = new Ligate();
        lig.initiate();

        {
            System.out.println("Two BamHI-digested DNAs:");
            String BamHIExt = "GATC";

            Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "c");
            Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Two BamHI-digested DNAs in other orientation:");
            String BamHIExt = "GATC";

            Polynucleotide poly1 = new Polynucleotide("ttttttG", "c", BamHIExt);
            Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Two PstI-digested DNAs:");
            String PstIExt = "-TGCA";

            Polynucleotide poly1 = new Polynucleotide("Gaaaaaa", PstIExt, "c");
            Polynucleotide poly2 = new Polynucleotide("ccccccC", "", PstIExt);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Two BsaI-digested DNAs:");
            String ext = "GTTT";

            Polynucleotide poly1 = new Polynucleotide("aaaaaa", ext, "c");
            Polynucleotide poly2 = new Polynucleotide("cccccc", "", ext);

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Two frags can't ligate:");
            Polynucleotide poly1 = new Polynucleotide("Gaaaaaa", "aa", "c");
            Polynucleotide poly2 = new Polynucleotide("ccccccC", "ttt", "cg");

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            try {
                Polynucleotide pdt = lig.run(frags);
            } catch (Exception err) {
                System.out.println("Expect exception thrown");
            }
        }

    }
}
