package org.twentyn.c5.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.twentyn.c5.construction.model.RestrictionData;

/**
 *
 * @author J. Christopher Anderson
 */
public class EnzymeFactory {

    public void initiate() {
    }
    

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
    }

    public RestrictionData run(Enzyme enzname) throws Exception {
        switch (enzname) {
            case BamHI:
                return new RestrictionData("GGATCC", 1, 5);

            case BglII:
                return new RestrictionData("AGATCT", 1, 5);

            case EcoRI:
                return new RestrictionData("GAATTC", 1, 5);

            case MfeI:
                return new RestrictionData("CAATTG", 1, 5);

            case SpeI:
                return new RestrictionData("ACTAGT", 1, 5);
            
            case XbaI:
                return new RestrictionData("TCTAGA", 1, 5);
            
            case XhoI:
                return new RestrictionData("CTCGAG", 1, 5);
            
            case HindIII:
                return new RestrictionData("AAGCTT", 1, 5);

            case BsaI:
                return new RestrictionData("GGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]", 7, 11);

            case BseRI:
                return new RestrictionData("GAGGAG[ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG]", 14, 16);
            
            case BsmBI:
                return new RestrictionData("CGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]", 7, 11);

        }
        throw new Exception();
    }

    public static void main(String[] args) {

    }
}
