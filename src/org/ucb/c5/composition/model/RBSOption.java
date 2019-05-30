package org.ucb.c5.composition.model;

/**
 * Descriptes a ribosome binding site Part
 *
 * @author J. Christopher Anderson
 */
public class RBSOption {

    private final String name;
    private final String description;
    private final String rbs;
    private final String cds;
    private final String first6aas;

    public RBSOption(String name, String description, String rbs, String cds, String first6aas) {
        this.name = name;
        this.description = description;
        this.rbs = rbs;
        this.cds = cds;
        this.first6aas = first6aas;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRbs() {
        return rbs;
    }

    public String getCds() {
        return cds;
    }

    public String getFirst6aas() {
        return first6aas;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(name);
        out.append("\n").append(rbs);
        out.append("\n").append(cds);
        out.append("\n").append(first6aas);
        return out.toString();
    }
}
