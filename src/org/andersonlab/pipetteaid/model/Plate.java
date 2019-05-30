package org.andersonlab.pipetteaid.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class Plate {
    private final String name;
    
    private final Well[][] wells;
    
    PlateConfig config;

    public Plate(String name, PlateConfig config) {
        this.name = name;
        this.config = config;
        
        wells = new Well[config.getNumRows()][config.getNumCols()];
        for(int i=0; i<wells.length; i++) {
            for(int j=0; j<wells[i].length; j++) {
                wells[i][j] = new Well();
            }
        }
    }

    public Well getWell(int row, int col) {
        return wells[row][col];
    }

    public String getName() {
        return name;
    }
    
    public PlateConfig getConfig() {
        return config;
    }
}
