package org.ucb.c5.constructionfile.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class RestrictionEnzyme {

    private final String site;
    private final int cut5;
    private final int cut3;
    private final int minCleavageDistance;

    public RestrictionEnzyme(String site, int cut5, int cut3, int nebdistanceCut) {
        this.site = site;
        this.cut5 = cut5;
        this.cut3 = cut3;
        this.minCleavageDistance = nebdistanceCut;
    }

    public String getSite() {
        return site;
    }

    public int getCut5() {
        return cut5;
    }

    public int getCut3() {
        return cut3;
    }
    
    public int getMinCleavageDistance() {
        return minCleavageDistance;
    }
}
