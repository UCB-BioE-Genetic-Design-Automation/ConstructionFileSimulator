package org.ucb.c5.semiprotocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class Multichannel implements Task {
    //Define the ranges for the transfer
    private final String sourceStart;  //eg plate_name/B1
    private final String sourceEnd;    //eg plate_name/B8
    private final String destStart;    //eg dest_plate/A1
    private final String destEnd;      //eg dest_plate/A8
    
    private final double volume;       //in uL

    public Multichannel(String sourceStart, String sourceEnd, String destStart, String destEnd, double volume) {
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
        this.destStart = destStart;
        this.destEnd = destEnd;
        this.volume = volume;
    }

    public String getSourceStart() {
        return sourceStart;
    }

    public String getSourceEnd() {
        return sourceEnd;
    }

    public String getDestStart() {
        return destStart;
    }

    public String getDestEnd() {
        return destEnd;
    }

    public Double getVolume() {
        return volume;
    }
    
    @Override
    public LabOp getOperation() {
        return LabOp.multichannel;
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getOperation()).append("\t");
        out.append(sourceStart).append("\t");
        out.append(sourceEnd).append("\t");
        out.append(destStart).append("\t");
        out.append(destEnd).append("\t");
        out.append(volume);
        return out.toString();
    }
}