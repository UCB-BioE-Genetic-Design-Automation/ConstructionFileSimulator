package org.ucb.c5.utils;

import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
import org.ucb.c5.constructionfile.model.Enzyme;
/**
 *
 * @author J. Christopher Anderson
 */
public class RestrictionEnzymeFactory {

    public void initiate() {
    }
    /*
    public enum Enzyme {
        BamHI,
        BglII,
        MfeI,
        EcoRI,
        SpeI,
        XbaI,
        PstI,
        SphI,
        HindIII,
        XhoI,
        BsaI,
        BsmBI,
        BseRI
    } */

    public RestrictionEnzyme run(Enzyme enzname) throws Exception {
        switch (enzname) {
            case BamHI:
                return new RestrictionEnzyme("GGATCC", 1, 5);

            case BglII:
                return new RestrictionEnzyme("AGATCT", 1, 5);

            case EcoRI:
                return new RestrictionEnzyme("GAATTC", 1, 5);

            case MfeI:
                return new RestrictionEnzyme("CAATTG", 1, 5);

            case SpeI:
                return new RestrictionEnzyme("ACTAGT", 1, 5);

            case XbaI:
                return new RestrictionEnzyme("TCTAGA", 1, 5);

            case XhoI:
                return new RestrictionEnzyme("CTCGAG", 1, 5);

            case HindIII:
                return new RestrictionEnzyme("AAGCTT", 1, 5);

            case PstI:
                return new RestrictionEnzyme("CTGCAG", 5, 1);

            case BsaI:
                return new RestrictionEnzyme("GGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]", 7, 11);

            case BseRI:
                return new RestrictionEnzyme("GAGGAG[ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG]", 16, 14);

            case BsmBI:
                return new RestrictionEnzyme("CGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]", 7, 11);
        }
        throw new Exception("Given enzyme is not a restriction enzyme");
    }

    public static void main(String[] args) throws Exception {
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
        RestrictionEnzyme enz = factory.run(Enzyme.HindIII);
    }
}
