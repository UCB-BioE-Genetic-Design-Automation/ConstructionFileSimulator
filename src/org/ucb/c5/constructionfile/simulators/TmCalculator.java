/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import java.util.ArrayList;


/**
 *
 * @author yishe
 */
public class TmCalculator {


    private double Adjustment = 60.0; 
    
    public void initiate() throws Exception {
    
    }
    
    public double run(char[] S1, char[] S2){
   
    
    double percentGC = computesPercentGC(S1);
    double percentMismatching = computesPercentMismatching(S1, S2);
    double duplexLength = S1.length; 
    double NaConcentration = 0.1;
    
    //double Tm = 81.5 + 16.6 * Math.log10(NaConcentration / (1.0 + 0.7 * NaConcentration)) + 0.41 * percentGC - 500.0 / duplexLength - percentMismatching;
    
    double Tm = Adjustment + 0.41 * percentGC - 500.0 / duplexLength - percentMismatching;
    
    return Tm;
    }
    

        
        /**
	 * computes the percentage of GC base pairs in the duplex of NucleotidSequences.
	 * @return double percentage of GC base pairs in the duplex.
	 */
	private double computesPercentGC(char[] S1){
            
		double numberGC = 0.0;
		
		for (int i = 0; i < S1.length;i++){
                    char Base = S1[i];
			if (Base == 'G' || Base == 'C'){
				numberGC++;
			}
		}
		
		return numberGC / (double)S1.length * 100.0;
	}
	
	/**
	 * Computes the percentage of mismatching base pairs in the duplex of NucleotidSequences.
	 * @return double percentage of mismatching base pairs in the duplex
	 */
	private double computesPercentMismatching(char[] S1, char[] S2){
		double numberMismatching = 0.0;
	
		for (int i = 0; i < S1.length;i++){
			char Base1 = S1[i];
                        char Base2 = S2[i];
			if (Base1 != Base2){
				numberMismatching++;
			}
		}
		return numberMismatching / (double)S1.length * 100.0;
	}
        
    
    public static void main(String[] args) throws Exception {
            Sequence s1 = new Sequence("ccaaaAGATCTatgagcggcttcccccgcag".toUpperCase());  
            Sequence s2 = new Sequence("ccaaaAGATCTatgagcggcttcccccgcag".toUpperCase());
	        
	    Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("EDNAFULL_1"), 10f, 0.5f);
            
            Matrix matrix = alignment.getMatrix();

            char[] S1 = alignment.getSequence1();
            char[] S2 = alignment.getSequence2();
        
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(S1,S2);
            System.out.println(result);
            
    }
}    


