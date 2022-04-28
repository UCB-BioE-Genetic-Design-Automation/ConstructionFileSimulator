package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.constructionfile.model.Modifications;

import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.constructionfile.model.Polynucleotide;

/**
 *
 * @author J. Christopher Anderson
 */
//TODO:
//if one of the fragments, or both has a 5' phosphate, have it ligate
//if no phosphates just return PCR product
//junit test put in PCR product with no 5' phosphates
//get the blunt PCR products back
public class LigateSimulator {

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

        //If the frags condense to one sequence, that should happen by here
        if (temp.size() != 1) {
            throw new Exception("Fragments to not ligate into a single product");
        }

        //Try circularizing the returned DNA
        Polynucleotide result = ligateEnds(temp.get(0));
        if (result == null) {
            return temp.get(0);
        }

        return result;
    }

    private Polynucleotide ligateEnds(Polynucleotide poly) throws Exception {

        boolean mod_phos5 = poly.getMod3().equals(Modifications.phos5) | poly.getMod5().equals(Modifications.phos5);
        
        if (!poly.getExt3().toUpperCase().equals(poly.getExt5().toUpperCase())) {
            return null;
            //throw new Exception("Ends of fragment don't ligate together");
        }
        
        //If there are no phosphates then no ligation can happen
        if (!mod_phos5) {
            return null;
            //throw new Exception("Ends of fragment don't ligate together");
        }

        String newseq = null;
        if (poly.getExt5().startsWith("-")) {
            newseq = poly.getSequence() + poly.getExt3().replaceAll("-", "");
        } else {
            newseq = poly.getExt5() + poly.getSequence();
        }

        return new Polynucleotide(newseq, true);
    }

    /**
     * Dpes one ligation event on two Polynuceotides
     *
     * If they can be ligated, it returns the product. If they cannot be joined,
     * it returns null
     *
     * @param p1
     * @param p2
     * @return
     * @throws Exception
     */
    private Polynucleotide join(Polynucleotide p1, Polynucleotide p2) throws Exception {
        //Get them in the right orientation to ligate
        Polynucleotide poly1 = null;
        Polynucleotide poly2 = null;

        Modifications mod5;
        Modifications mod3;
        //Add here for the phosphates on at least one of them
        /*
        System.out.println("p1");
        System.out.println(p1);
        System.out.println("p2");
        System.out.println(p2);
         */

        //If either doesnt have phosphate then they wont join 
        if (!(p1.getMod5().equals(Modifications.phos5) | p1.getMod3().equals(Modifications.phos5) | p2.getMod5().equals(Modifications.phos5) | p1.getMod3().equals(Modifications.phos5))) {
            return null;
        }

        boolean p1_onto_p2_53 = (p1.getMod5().equals(Modifications.phos5) | p2.getMod3().equals(Modifications.phos5));
        boolean p1_onto_p2_35 = (p1.getMod3().equals(Modifications.phos5) | p2.getMod5().equals(Modifications.phos5));

        if (p1.getExt3().toUpperCase().equals(p2.getExt5().toUpperCase()) & p1_onto_p2_35) {
            poly1 = p1;
            poly2 = p2;
            mod5 = p1.getMod5();
            mod3 = p2.getMod3();

        } else if (p1.getExt5().toUpperCase().equals(p2.getExt3().toUpperCase()) & p1_onto_p2_53) {
            poly1 = p2;
            poly2 = p1;
            mod5 = p2.getMod5();
            mod3 = p1.getMod3();
        } else {
            Polynucleotide p2rc = revcomp.run(p2);

            boolean p1_onto_p2rc_53 = (p1.getMod5().equals(Modifications.phos5) | p2rc.getMod3().equals(Modifications.phos5));
            boolean p1_onto_p2rc_35 = (p1.getMod3().equals(Modifications.phos5) | p2rc.getMod5().equals(Modifications.phos5));

            if (p1.getExt3().toUpperCase().equals(p2rc.getExt5().toUpperCase()) & p1_onto_p2rc_35) {
                poly1 = p1;
                poly2 = p2rc;

                mod5 = p1.getMod5();
                mod3 = p2rc.getMod3();

            } else if (p1.getExt5().toUpperCase().equals(p2rc.getExt3().toUpperCase()) & p1_onto_p2rc_53) {
                poly1 = p2rc;
                poly2 = p1;

                mod5 = p2rc.getMod5();
                mod3 = p1.getMod3();

                //Otherwise they do not ligate, so return null
            } else {
                return null;
            }
        }

        //If got this far, they match, so ligate them
        String newseq = "";
        newseq += poly1.getSequence();
        newseq += poly1.getExt3().replaceAll("-", "");
        newseq += poly2.getSequence();

        //(String sequence, String ext5, String ext3, boolean isDoubleStranded, boolean isRNA, boolean isCircular, Modifications mod_ext5, Modifications mod_ext3) {
        Polynucleotide out = new Polynucleotide(newseq, poly1.getExt5(), poly2.getExt3(), true, false, false, mod5, mod3);
        return out;
    }

    public static void main(String[] args) throws Exception {

        LigateSimulator lig = new LigateSimulator();
        lig.initiate();

        {
            System.out.println("Two BamHI-digested DNAs:");
            String BamHIExt = "GATC";

            Polynucleotide poly1 = new Polynucleotide("Caaaaaa", BamHIExt, "c", true, false, false, Modifications.phos5, Modifications.phos5);
            Polynucleotide poly2 = new Polynucleotide("ccccccG", "", BamHIExt, true, false, false, Modifications.phos5, Modifications.phos5);

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
            System.out.println("Two BamHI-digested DNAs in other orientation:");
            String BamHIExt = "GATC";

            Polynucleotide poly1 = new Polynucleotide("ttttttG", BamHIExt, "c");
            Polynucleotide poly2 = new Polynucleotide("ccccccG", BamHIExt, "");

            System.out.println("poly1:\n" + poly1);
            System.out.println("poly2:\n" + poly2);

            List<Polynucleotide> frags = new ArrayList<>();
            frags.add(poly1);
            frags.add(poly2);

            Polynucleotide pdt = lig.run(frags);

            System.out.println("Ligation product:");
            System.out.println(pdt.toString());
        }
        /*
        
/*
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
         */

    }

}
