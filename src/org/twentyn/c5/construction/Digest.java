package org.twentyn.c5.construction;

import org.twentyn.c5.construction.model.RestrictionEnzyme;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.twentyn.c5.construction.RestrictionEnzymeFactory.Enzyme;
import org.twentyn.c5.construction.model.Polynucleotide;
import org.twentyn.c5.utils.SequenceUtils;

/**
 *
 * A Function that inputs a DNA sequence and a List of restriction enzymes and
 * computes all the fragments that will be present upon digestion to completion
 *
 * @author J. Christopher Anderson
 */
public class Digest {

    public void initiate() throws Exception {

    }

    public List<Polynucleotide> run(Polynucleotide substrate, List<RestrictionEnzyme> enzymes) throws Exception {
        List<Polynucleotide> out = new ArrayList<>();
        List<Polynucleotide> temp = new ArrayList<>();
        out.add(substrate);

        outer: while (true) {
            boolean isdone = true;
            temp.clear();
            temp.addAll(out);
            out.clear();

            inner: for (Polynucleotide poly : temp) {
                List<Polynucleotide> result = cutOnce(poly, enzymes);
                if (result == null) {
                    Polynucleotide rc = revcomp(poly);
                    result = cutOnce(rc, enzymes);
                }
                if (result == null) {
                    out.add(poly);
                    continue inner;
                }

                out.addAll(result);
                isdone = false;
            }
            
            if(isdone == true) {
                break outer;
            }
        }
        return out;
    }

    /**
     * Helper method.  Finds the first of the enzymes provided as arguments, to
     * find the first site it encounters.  The DNA is split into two fragments,
     * which are returned in a List along with their sticky ends.
     * 
     * @param sub
     * @param enzymes
     * @return 
     */
    private List<Polynucleotide> cutOnce(Polynucleotide sub, List<RestrictionEnzyme> enzymes) {
        List<Polynucleotide> out = new ArrayList<>();

        for (RestrictionEnzyme enz : enzymes) {
            Pattern p = Pattern.compile(enz.getSite());
            Matcher m = p.matcher(sub.getSequence().toUpperCase());
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                
                //Construct the 5' remaining fragment
                {
                String remaining = sub.getSequence().substring(0, start + enz.getCut5());
                String ext5 = sub.getExt5();
                String ext3 = sub.getSequence().substring(start + enz.getCut5(), start + enz.getCut3());

                Polynucleotide frag = new Polynucleotide(remaining, ext5, ext3);
                out.add(frag);
                }
                
                //Construct the 3' remaining fragment
                {
                String remaining = sub.getSequence().substring(start + enz.getCut3());
                String ext5 = sub.getSequence().substring(start + enz.getCut5(), start + enz.getCut3());
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

    private Polynucleotide revcomp(Polynucleotide frag) {
        String rc = SequenceUtils.reverseComplement(frag.getSequence());

        //Transfer the 3' end
        String outext3 = SequenceUtils.reverseComplement(frag.getExt5());
        String outext5 = SequenceUtils.reverseComplement(frag.getExt3());

        Polynucleotide out = new Polynucleotide(rc, outext5, outext3);
        return out;
    }

    public static void main(String[] args) throws Exception {
        List<RestrictionEnzyme> enz = new ArrayList<>();
//        Polynucleotide poly = new Polynucleotide("aaaaGGTCTCtCCCCttCAATTGccccc");
        Polynucleotide poly = new Polynucleotide("aaaGGATCCtttGGATCCcccaaa", "", "ggggg");


        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();

        enz.add(factory.run(Enzyme.BamHI));

        Digest dig = new Digest();
        List<Polynucleotide> pdts = dig.run(poly, enz);

        for (Polynucleotide pol : pdts) {
            System.out.println(pol.toString());
        }

        System.out.println("number of products: " + pdts.size());

    }

}
