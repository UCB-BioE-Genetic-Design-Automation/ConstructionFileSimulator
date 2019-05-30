package org.andersonlab.pipetteaid.model;

/**
 * Interface for describing the dimensions of a Plate, in units of cm.
 *
 * @author jcaucb
 */
public interface PlateConfig {
    public double getXoffset();

    public double getYoffset();

    public double getWellWidth();

    public double getWellHeight();

    public double getWidth();

    public double getHeight();
    
    public int getNumRows();
    
    public int getNumCols();
}
