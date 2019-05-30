package org.ucb.c5.composition.model;

/**
 * Encodes a monocistronic mRNA from an RBS and a coding sequence
 * 
 * @author J. Christopher Anderson
 */
public class Transcript {
    private final RBSOption rbs;
    private final String peptide;
    private final String[] codons;

    public Transcript(RBSOption rbs, String peptide, String[] codons) {
        this.rbs = rbs;
        this.peptide = peptide;
        this.codons = codons;
    }

    public RBSOption getRbs() {
        return rbs;
    }

    public String getPeptide() {
        return peptide;
    }

    public String[] getCodons() {
        return codons;
    }
}
