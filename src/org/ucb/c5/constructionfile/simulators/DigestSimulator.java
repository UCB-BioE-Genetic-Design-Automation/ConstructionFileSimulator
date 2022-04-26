package org.ucb.c5.constructionfile.simulators;

import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.sequtils.RestrictionEnzymeFactory;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.RestrictionEnzyme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Modifications;

/**
 * A Function that inputs a DNA sequence and a List of restriction enzymes and
 * computes all the fragments that will be present upon digestion to completion
 *
 * @author J. Christopher Anderson
 */
public class DigestSimulator {

    private PolyRevComp revcomp;

    public void initiate() throws Exception {
        revcomp = new PolyRevComp();
        revcomp.initiate();
    }

    /**
     * Higher-level digestion call from a loose-coupled Digestion object and 
     * sequences.  Returns the single Polynucleotide product described in the
     * Digestion object.
     * 
     * @param dig
     * @param fragments
     * @return
     * @throws Exception 
     */
    public Polynucleotide run(Digestion dig, Map<String, Polynucleotide> fragments) throws Exception {
        Polynucleotide substrate = fragments.get(dig.getSubstrate());
        List<RestrictionEnzyme> enzymes = new ArrayList<>();

        RestrictionEnzymeFactory rezfactory = new RestrictionEnzymeFactory();
        rezfactory.initiate();
        for (String enz : dig.getEnzymes()) {
            enzymes.add(rezfactory.run(enz));
        }

        List<Polynucleotide> frags = run(substrate, enzymes);
        return frags.get(dig.getFragSelection());
    }

    /**
     * Direct call of digestion algorithm operating on Polynucleotide.  Returns
     * all digestion products
     * 
     * @param substrate
     * @param enzymes
     * @return
     * @throws Exception 
     */
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

            if (isdone) {
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

                if (sub.isCircular()) {
                    //Construct the linearized fragment
                    Polynucleotide frag = createLinFrag(sub, threeover, start, enz);
                    out.add(frag);
                } else { //If it's linear
                    //check the distance in  restrictionenzyme object, if not pass continue 
                    if(start < enz.getMinCleavageDistance()) {
                        continue;
                    }
                    if((sub.getSequence().length()-end) < enz.getMinCleavageDistance()) {
                        continue;
                    }
                    //Construct the 5' remaining fragment
                    Polynucleotide frag5 = create5Frag(sub, threeover, start, enz);
                    out.add(frag5);

                    //Construct the 3' remaining fragment
                    Polynucleotide frag3 = create3Frag(sub, threeover, start, enz);
                    out.add(frag3);
                }
                
              
                return out;
            }
        }

        //Returns null if nothing is found //return null may allow failed digests to continue?
        return null;
        
    }

    private Polynucleotide create5Frag(Polynucleotide sub, boolean threeover, int start, RestrictionEnzyme enz) {
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

        Polynucleotide frag = new Polynucleotide(remaining, ext5, ext3,true,false,false,sub.getMod5(),Modifications.phos5);

        return frag;
    }

    private Polynucleotide create3Frag(Polynucleotide sub, boolean threeover, int start, RestrictionEnzyme enz) {
        String remaining = null;
        if (threeover) {
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

        Polynucleotide frag = new Polynucleotide(remaining, ext5, ext3,true,false,false,Modifications.phos5,sub.getMod3());

        return frag;
    }

    private Polynucleotide createLinFrag(Polynucleotide sub, boolean threeover, int start, RestrictionEnzyme enz) {
        String newseq = null;
        if (threeover) {
            newseq = sub.getSequence().substring(start + enz.getCut5()) + sub.getSequence().substring(0, start + enz.getCut3());
        } else {
            newseq = sub.getSequence().substring(start + enz.getCut3()) + sub.getSequence().substring(0, start + enz.getCut5());
        }

        String ext5 = null;
        if (threeover) {
            ext5 = "-" + sub.getSequence().substring(start + enz.getCut3(), start + enz.getCut5());
        } else {
            ext5 = sub.getSequence().substring(start + enz.getCut5(), start + enz.getCut3());
        }

        String ext3 = null;
        if (threeover) {
            ext3 = "-" + sub.getSequence().substring(start + enz.getCut3(), start + enz.getCut5());
        } else {
            ext3 = sub.getSequence().substring(start + enz.getCut5(), start + enz.getCut3());
        }
       
        return new Polynucleotide(newseq, ext5, ext3,true,false,false,Modifications.phos5,Modifications.phos5);   //
    }

    public static void main(String[] args) throws Exception {
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        DigestSimulator dig = new DigestSimulator();
        dig.initiate();

        {
            System.out.println("Two BamHI sites substrate:");
            Polynucleotide poly = new Polynucleotide("aaaGGATCCtttGGATCCcccaaa", "-ccccc", "ggggg");
            System.out.println(poly);
            System.out.println("fragments:");
            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run("BamHI"));

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
            enz.add(factory.run("BsaI"));

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
            enz.add(factory.run("BsaI"));

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
            enz.add(factory.run("PstI"));

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
            enz.add(factory.run("BseRI"));

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
            enz.add(factory.run("BseRI"));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }

        {
            System.out.println("A circular 5' ext substrate:");

            //Last true in the constructor indicates that it is circular
            Polynucleotide poly = new Polynucleotide("aaaGGATCCaaa", true);
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run("BamHI"));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }

        {
            System.out.println("A circular 3' ext substrate:");

            //Last true in the constructor indicates that it is circular
            Polynucleotide poly = new Polynucleotide("aaaGAGGAGaaaaaaaaGAaaa", true);
            System.out.println(poly);
            System.out.println("fragments:");

            List<RestrictionEnzyme> enz = new ArrayList<>();
            enz.add(factory.run("BseRI"));

            List<Polynucleotide> pdts = dig.run(poly, enz);

            for (Polynucleotide pol : pdts) {
                System.out.println(pol.toString());
            }

            System.out.println("number of products: " + pdts.size());
            System.out.println("-----\n");
        }
    }
}
