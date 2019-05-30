package org.ucb.c5.composition.model;

import java.util.List;

/**
 * Encodes a genetic construct described in terms of a single operon
 * encoding multiple cocistronic transcripts
 * 
 * @author J. Christopher Anderson
 */
public class Construct {
    private final List<Transcript> mRNAs;
    private final String promoter;
    private final String terminator;

    public Construct(List<Transcript> mRNAs, String promoter, String terminator) {
        this.mRNAs = mRNAs;
        this.promoter = promoter;
        this.terminator = terminator;
    }

    public List<Transcript> getmRNAs() {
        return mRNAs;
    }

    public String getPromoter() {
        return promoter;
    }

    public String getTerminator() {
        return terminator;
    }
}
