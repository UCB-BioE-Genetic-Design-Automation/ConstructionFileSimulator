package org.andersonlab.pipetteaid.model;

import javafx.util.Pair;

/**
 *
 * @author J. Christopher Anderson
 */
public class Well {
    
//    private final Map<String, Double> wellContent;  TODO in future:  encode content of each well
    private Double volume;
    
    public Well() {
        volume = 0.0;
    }
    
    public Double getVolume() {
        return volume;
    }
    
    public void addVolume(Double amount) throws Exception {
        //TODO in future:  check if well can take this much volume
        volume += amount;
        //TODO in future:  adjust composition of the well
    }
    
    public void removeVolume(Double amount) throws Exception {
        //TODO in future:  check for sufficient volume
//        if(volume < amount) {
//            throw new Exception();
//        }
//        
        volume -= amount;
        //TODO in future:  adjust composition of the well
    }

    public static Pair<Integer, Integer> parseWellLabel(String location)  throws Exception {
        String A1 = location;
        if(A1.contains("/")) {
            String[] splitted = A1.split("/");
            A1 = splitted[1];
        }
        //Figure out the row
        Integer row = null;
        try {
            String letters = A1.replaceAll("[0-9]+", "");
            char crow = letters.charAt(0);
            row = ((int) crow) - 65;
        } catch(Exception err) {}
        
        //TODO:  Handle the case where there are two letters, ie with a 384-well plate
        
        //Figure out the column
        Integer col = null;
        try {
            String numbers = A1.replaceAll("[A-Z]+", "");
            col  = Integer.parseInt(numbers) - 1;
        } catch(Exception err) {}
        
        return new Pair(row, col);
    }
    
    
    public static String calcWellLabel(Pair<Integer, Integer> srcWell) {
        int col = srcWell.getValue() + 1;
        int irow = 65 + srcWell.getKey();
        char row = (char) irow;
        String out = "" + row + col;
        return out;
    }
}
