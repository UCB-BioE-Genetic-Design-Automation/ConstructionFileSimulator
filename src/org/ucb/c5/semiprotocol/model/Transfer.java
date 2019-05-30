package org.ucb.c5.semiprotocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class Transfer implements Task {
    private final String sourceWell; //eg src_plate_name/B5 or pcr_tube
    private final String destWell;   //eg dst_plate_name/A2 or pcr_tube
    private final double volume;     //in uL

    public Transfer(String source, String dest, double volume) {
        this.sourceWell = source;
        this.destWell = dest;
        this.volume = volume;
    }

    public String getSource() {
        return sourceWell;
    }

    public String getDest() {
        return destWell;
    }

    public double getVolume() {
        return volume;
    }
    
    @Override
    public LabOp getOperation() {
        return LabOp.transfer;
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getOperation()).append("\t");
        out.append(sourceWell).append("\t");
        out.append(destWell).append("\t");
        out.append(volume);
        return out.toString();
    }
}