package org.ucb.c5.semiprotocol.model;

/**
 * Operation for dispensing a stock Reagent
 * 
 * It is not possible to multichannel a reagent.
 * You must transfer to each well of a row, then
 * multichannel from that row.
 * 
 * @author J. Christopher Anderson
 */
public class Dispense implements Task {
    private final Reagent reagent;
    private final String dstContainer; //eg plate_name/B5 or pcr_tube
    private final double volume;       //in uL

    public Dispense(Reagent reagent, String dstContainer, double volume) {
        this.reagent = reagent;
        this.dstContainer = dstContainer;
        this.volume = volume;
    }

    public Reagent getReagent() {
        return reagent;
    }

    public String getDstContainer() {
        return dstContainer;
    }

    public Double getVolume() {
        return volume;
    }

    @Override
    public LabOp getOperation() {
        return LabOp.dispense;
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getOperation()).append("\t");
        out.append(reagent.toString()).append("\t");
        out.append(dstContainer).append("\t");
        out.append(volume);
        return out.toString();
    }
}
