package org.ucb.c5.construction;

import org.ucb.c5.construction.model.RestrictionEnzyme;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ucb.c5.construction.RestrictionEnzymeFactory.Enzyme;
import org.ucb.c5.construction.model.Polynucleotide;

/**
 *
 * A Function that inputs a DNA sequence and a List of restriction enzymes and
 * computes all the fragments that will be present upon digestion to completion
 *
 * @author J. Christopher Anderson
 */
public class Digest {
    private PolyRevComp revcomp;
    
    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
    }

    public List<Polynucleotide> run(Polynucleotide substrate, List<RestrictionEnzyme> enzymes) throws Exception {
        List<Polynucleotide> out = new ArrayList<>();
        List<Polynucleotide> temp = new ArrayList<>();
        out.add(substrate);

        outer:
        while (true) {
            boolean isdone = true;
            temp.clear();
            temp.addAll(out);
            out.clear();

            inner:
            for (Polynucleotide poly : temp) {
                List<Polynucleotide> result = cutOnce(poly, enzymes);
                if (result == null) {
                    Polynucleotide rc = revcomp.run(poly);
                    result = cutOnce(rc, enzymes);
                }
                if (result == null) {
                    out.add(poly);
                    continue inner;
                }

                out.addAll(result);
                isdone = false;
            }

            if (isdone == true) {
                break outer;
            }
        }
        return out;
    }

    /**
     * Helper method. Finds the first of the enzymes provided as arguments, to
     * find the first site it encounters. The DNA is split into two fragments,
     * which are returned in a List along with their sticky ends.
     *
     * @param sub
     * @param enzymes
     * @return
     */
    private List<Polynucleotide> cutOnce(Polynucleotide sub, List<RestrictionEnzyme> enzymes) {
        List<Polynucleotide> out = new ArrayList<>();

        for (RestrictionEnzyme enz : enzymes) {
            //Check if the enzyme is a 3' overhanger
            boolean threeover = false;
            if (enz.getCut3() < enz.getCut5()) {
                threeover = true;
            }

            //Find the first enzyme binding site
            Pattern p = Pattern.compile(enz.getSite());
            Matcher m = p.matcher(sub.getSequence().toUpperCase());
            while (m.find()) {
                int start = m.start();
                int end = m.end();

                //Construct the 5' remaining fragment
                {
                    String remaining = null;
                    if (threeover) {
                        remaining = sub.getSequence().substring(0, start + enz.getCut3());
                    } else {
                        remaining = sub.getSequence().substring(0, start + enz.getCut5());
                    }
                    String ext5 = sub.getExt5();
                    String ext3 = null;
                    if (threeover) {
                        ext3 = "-" + sub.getSequence().substring(start + enz.getCut3(), start + enz.getCut5());
                    } else {
                        ext3 = sub.getSequence().substring(start + enz.getCut5(), start + enz.getCut3());
                    }

                    Polynucleotide frag = new Polynucleotide(remaining, ext5, ext3);
                    out.add(frag);
                }

                //Construct the 3' remaining fragment
                {
                    String remaining = null;
                    if(threeover) {
                        remaining = sub.getSequence().substring(start + enz.getCut5());
                    } else {
                        remaining = sub.getSequence().substring(start + enz.getCut3());
                    }
                    String ext5 = null;
                    if (threeover) {
                        ext5 = "-" + sub.getSequence().substring(start + enz.getCut3(), start + enz.getCut5());
                    } else {
                        ext5 = sub.getSequence().substring(start + enz.getCut5(), start + enz.getCut3());
                    }
                    String ext3 = sub.getExt3();

                    Polynucleotide frag = new Polynucleotide(remaining, ext5, ext3);
                    out.add(frag);
                }

                return out;
            }
        }

        //Returns null if nothing is found
        return null;
    }

    public static void main(String[] args) throws Exception {
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        Digest dig = new Digest();
        dig.initiate();

        {
            System.out.println("Two BamHI sites substrate:");
            Polynucleotide poly = new Polynucleotide("aaaGGATCCtttGGATCCcccaaa", "-ccccc", "ggggg");
            System.out.println(poly);
            System.out.println("fragments:");
            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run(Enzyme.BamHI));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");

        }

        {
            System.out.println("Forward BsaI Site substrate:");
            Polynucleotide poly = new Polynucleotide("aaaGGTCTCtCCCCaaa");
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run(Enzyme.BsaI));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");

        }

        {
            System.out.println("Reverse BsaI Site substrate:");
            Polynucleotide poly = new Polynucleotide("aaaGGGGaGAGACCaaa");
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run(Enzyme.BsaI));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }

        {
            System.out.println("PstI Site substrate:");
            Polynucleotide poly = new Polynucleotide("aaaCTGCAGaaa");
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run(Enzyme.PstI));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }
        
        {
            System.out.println("Forward BseRI Site substrate:");
            Polynucleotide poly = new Polynucleotide("aaaGAGGAGaaaaaaaaGAaaa");
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run(Enzyme.BseRI));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }
        
        {
            System.out.println("Reverse BseRI Site substrate:");
            Polynucleotide poly = new Polynucleotide("aaaTCttttttttCTCCTCaaa");
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run(Enzyme.BseRI));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }
    }

}
