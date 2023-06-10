package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ucb.c5.constructionfile.model.Modifications;

import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.sequtils.ComparePolynucleotides;

/**
 *
 * @author J. Christopher Anderson
 */
public class LigateSimulator {

    private PolyRevComp revcomp;
    private ComparePolynucleotides cps;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
        cps = new ComparePolynucleotides();
        cps.initiate();
    }

    public Polynucleotide run(List<Polynucleotide> polys) throws Exception {
        //Handle the edge case of a single fragment
        if (polys.size() == 1) {
            Polynucleotide poly = polys.get(0);
            poly = ligateEnds(poly);
            if (poly == null) {
                throw new IllegalArgumentException("Single fragment does not circularize:\n" + polys.get(0));
            }
            return poly;
        }

        //Index all poly by their 5' ends, and check for duplications
        Map<String, Polynucleotide> fiveToPoly = new HashMap<>();
        for (int i = 0; i < polys.size(); i++) {
            Polynucleotide poly = polys.get(i);
            fiveToPoly.put(poly.getExt5(), poly);
            Polynucleotide polyRC = revcomp.run(poly);
            fiveToPoly.put(polyRC.getExt5(), polyRC);
        }

        //Find the first poly that can ligate with another fragment
        Polynucleotide lefty = null;
        for (Polynucleotide poly : polys) {
            String threePrime = poly.getExt3();
            Polynucleotide righty = fiveToPoly.get(threePrime);
            if (righty == null) {
                continue;
            }
            if (join(poly, righty) == null) {
                continue;
            }
            lefty = poly;
            break;
        }
        
        if (lefty == null) {
            throw new IllegalArgumentException("No valid ligations junctions found");
        }

        //Iteratively add righty fragments until can't ligate any more
        while (true) {
            //Try to circularize, and if so exit loop
            Polynucleotide result = ligateEnds(lefty);
            if (result != null) {
                lefty = result;
                break;
            }

            //Find the next fregment to add on
            String threePrime = lefty.getExt3();
            Polynucleotide righty = fiveToPoly.get(threePrime);
            fiveToPoly.remove(threePrime);

            //If there is no next fragment, stop ligating
            if (righty == null) {
                break;
            }

            //Try to ligate them
            Polynucleotide pdt = join(lefty, righty);
            if (pdt != null) {
                lefty = pdt;
            }
        }

        //Check that all the initial fragments are in the assembled DNA
        for (int i = 1; i < polys.size(); i++) {
            Polynucleotide poly = polys.get(i);
            if (lefty.getSequence().indexOf(poly.getSequence()) == -1) {
                Polynucleotide polyRC = revcomp.run(poly);
                if (lefty.getSequence().indexOf(polyRC.getSequence()) == -1) {
                    throw new IllegalArgumentException("Not all input fragments incorporated into assembled product");
                }
            }
        }

        return lefty;
    }

    /**
     * Tries to pairwise join DNAs, checking for a 5' phosphate
     *
     * @param lefty
     * @param righty
     * @return
     */
    private Polynucleotide join(Polynucleotide lefty, Polynucleotide righty) throws Exception {
        //One or the other ends needs a 5' phosphate
        boolean hasPhosphate = lefty.getMod3().equals(Modifications.phos5) || righty.getMod5().equals(Modifications.phos5);
        if (!hasPhosphate) {
            return null;
        }

        //The ends can only be 5' phosphate or hydroxyls, nothing else
        boolean hydroxyOrPhosLefty = lefty.getMod3().equals(Modifications.phos5) || lefty.getMod3().equals(Modifications.hydroxyl);
        if (!hydroxyOrPhosLefty) {
            return null;
        }
        boolean hydroxyOrPhosRighty = righty.getMod5().equals(Modifications.phos5) || righty.getMod5().equals(Modifications.hydroxyl);
        if (!hydroxyOrPhosRighty) {
            return null;
        }
        
        if(cps.run(lefty, righty)) {
            return null;
        }

        //Ligate them
        String newseq = "";
        newseq += lefty.getSequence();
        newseq += lefty.getExt3().replaceAll("-", "");
        newseq += righty.getSequence();

        //(String sequence, String ext5, String ext3, boolean isDoubleStranded, boolean isRNA, boolean isCircular, Modifications mod_ext5, Modifications mod_ext3) {
        return new Polynucleotide(newseq, lefty.getExt5(), righty.getExt3(), true, false, false, lefty.getMod5(), righty.getMod3());
    }

    /**
     * Tries to circularize the ends of the DNA
     *
     * @param poly The DNA being ligated
     * @return null or the poly circularized
     */
    private Polynucleotide ligateEnds(Polynucleotide poly) {
        //One or the other ends needs a 5' phosphate
        boolean hasPhosphate = poly.getMod3().equals(Modifications.phos5) || poly.getMod5().equals(Modifications.phos5);
        if (!hasPhosphate) {
            return null;
        }

        //The ends can only be 5' phosphate or hydroxyls, nothing else
        boolean hydroxyOrPhos5 = poly.getMod5().equals(Modifications.phos5) || poly.getMod5().equals(Modifications.hydroxyl);
        if (!hydroxyOrPhos5) {
            return null;
        }
        boolean hydroxyOrPhos3 = poly.getMod3().equals(Modifications.phos5) || poly.getMod3().equals(Modifications.hydroxyl);
        if (!hydroxyOrPhos3) {
            return null;
        }

        //The sticky ends must match
        if (!poly.getExt3().toUpperCase().equals(poly.getExt5().toUpperCase())) {
            return null;
        }

        //Deal with strandedness of extension
        String sticky = poly.getExt5();
        if (sticky.startsWith("-")) {
            sticky = sticky.substring(1);
        }

        return new Polynucleotide(sticky + poly.getSequence(), true);
    }

    public static void main(String[] args) throws Exception {

        LigateSimulator lig = new LigateSimulator();
        lig.initiate();

        {
            System.out.println("Recircularization of a single fragment");

            Polynucleotide poly1 = new Polynucleotide("CCCCCCCCCCCC", "GGGG", "GGGG", true, false, false, Modifications.phos5, Modifications.phos5);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);

            Polynucleotide pdt = lig.run(frags);
            Polynucleotide expected = new Polynucleotide("GGGGCCCCCCCCCCCC", "", "", true, false, true, Modifications.circular, Modifications.circular);

            ComparePolynucleotides cps = new ComparePolynucleotides();
            cps.initiate();
            cps.run(pdt, expected);

            System.out.println("Input:\n" + poly1);
            System.out.println("Product:\n" + pdt);
        }

        {
            System.out.println("Single fragment with no 5' phosphates");

            Polynucleotide poly = new Polynucleotide("CCCCCCCCCCCC", "GGGG", "GGGG", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly);

            System.out.println("Input:\n" + poly);
            
            try {
                Polynucleotide pdt = lig.run(frags);
                System.err.println("Invalid return of product: " + pdt);
            } catch (Exception err) {
                System.out.println("Exception correctly thrown\n");
            }
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
            System.out.println("Two BsaI-digested DNAs:");
            String ext = "GTTT";

            Polynucleotide poly1 = new Polynucleotide("aaaaaa", ext, "");
            Polynucleotide poly2 = new Polynucleotide("cccccc", "c", ext);

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
                System.out.println("(Expected exception thrown)\n");
            }
        }

        {
            System.out.println("Circularizing 5' ext aDNA:");
            Polynucleotide poly1 = new Polynucleotide("aaaaaa", "CCCC", "CCCC");
            System.out.println("poly1:\n" + poly1);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Circularizing 3' ext aDNA:");
            Polynucleotide poly1 = new Polynucleotide("aaaaaa", "-CCCC", "-CCCC");
            System.out.println("poly1:\n" + poly1);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }

        {
            System.out.println("Cicularizaing two digested DNAs:");

            Polynucleotide poly1 = new Polynucleotide("aaaaaaG", "-CC", "GATC");
            Polynucleotide poly2 = new Polynucleotide("Ctttttt", "GATC", "-CC");

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }
    }
}
