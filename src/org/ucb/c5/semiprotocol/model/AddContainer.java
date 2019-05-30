package org.ucb.c5.semiprotocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class AddContainer implements Task {
    private final Container tubetype;
    private final String name;
    
    /**
     * Location is either for a position on the deck (for a plate)
     * Or within the PCR rack (for a PCR tube or strip)
     * or within the tube rack (for 1.5 and 2 mL tubes)
     * 
     * Syntax is:
        deck/A3
        pcr/B2
        tube/A5
     */
    private final String location;
    private final boolean isnew;

    public AddContainer(Container tubetype, String name, String location, boolean isnew) {
        this.tubetype = tubetype;
        this.name = name;
        this.location = location;
        this.isnew = isnew;
    }

    public Container getTubetype() {
        return tubetype;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean isNew() {
        return isnew;
    }
    
    @Override
    public LabOp getOperation() {
        return LabOp.addContainer;
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getOperation()).append("\t");
        out.append(tubetype.toString()).append("\t");
        out.append(name).append("\t");
        out.append(location).append("\t");
        out.append(isnew);
        return out.toString();
    }
}
