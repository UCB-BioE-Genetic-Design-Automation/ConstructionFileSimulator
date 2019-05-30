package org.ucb.c5.composition.model;

import java.util.List;

/**
 * Describes the specification for a genetically engineered organism derived
 * from a cell line specified by the Host augmented with a list of proteins.
 *
 * @author J. Christopher Anderson
 */
public class Composition {

    private final Host host;
    private final String promoter;
    private final List<String> proteins;
    private final String terminator;

    public Composition(Host host, String promoter, List<String> proteins, String terminator) {
        this.host = host;
        this.promoter = promoter;
        this.proteins = proteins;
        this.terminator = terminator;
    }

    public Host getHost() {
        return host;
    }

    public String getPromoter() {
        return promoter;
    }

    public List<String> getProteins() {
        return proteins;
    }

    public String getTerminator() {
        return terminator;
    }
    
}
