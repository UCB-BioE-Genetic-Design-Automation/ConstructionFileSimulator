/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import melting.ThermoResult;
import melting.approximativeMethods.Wetmur91;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.NamedMethod;
import melting.sequences.BasePair;

/**
 * This class represents the model wetdna91. It extends Wetmur91.
 */
public class WetmurDNA91 extends Wetmur91
  implements NamedMethod
{
	// Instance variables
	
	/**
	 * Temperature formula
	 */
	private static String temperatureEquation = "Tm = 81.5 + 16.6 * log10(Na / (1.0 + 0.7 * Na)) + 0.41 * percentGC - 500 / duplexLength - percentMismatching";

  /**
   * Full name of the method.
   */
  private static String methodName = "Wetmur (1991) (DNA duplexes)";
	
  	/**
	 * ArrayList duplex : an ArrayList containing the sorted BasePair objects.
	 */
	private ArrayList<BasePair> duplex;
        
	// public methods
	
	@Override
	public ThermoResult computesThermodynamics() {
		double percentGC = computesPercentGC();
		double percentMismatching = computesPercentMismatching();
		double duplexLength = getDuplexLength(); 
		
		double Tm = 81.5 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 * this.environment.getNa())) + 0.41 * percentGC - 500.0 / duplexLength - percentMismatching;

		this.environment.setResult(Tm);
		
    OptionManagement.logMethodName(methodName);
    OptionManagement.logTemperatureEquation(temperatureEquation);
		
		return this.environment.getResult();
	}
	
        public int getDuplexLength(){
		return duplex.size();
	}
        
        /**
	 * computes the percentage of GC base pairs in the duplex of NucleotidSequences.
	 * @return double percentage of GC base pairs in the duplex.
	 */
	public double computesPercentGC(){
		double numberGC = 0.0;
		
		for (int i = 0; i < getDuplexLength();i++){
			BasePair pair = duplex.get(i);
			if (pair.isBasePairEqualTo("G", "C")){
				numberGC++;
			}
		}
		
		return numberGC / (double)getDuplexLength() * 100.0;
	}
	
	/**
	 * Computes the percentage of mismatching base pairs in the duplex of NucleotidSequences.
	 * @return double percentage of mismatching base pairs in the duplex
	 */
	public double computesPercentMismatching(){
		double numberMismatching = 0.0;
	
		for (int i = 0; i < getDuplexLength();i++){
			BasePair pair = duplex.get(i);
			if (pair.isComplementaryBasePair() == false){
				numberMismatching++;
			}
		}
		return numberMismatching / (double)getDuplexLength() * 100.0;
	}
	@Override
	public boolean isApplicable() {
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			OptionManagement.logWarning("\n The wetmur equation used here was originally established for DNA duplexes.");
		}
		
		return super.isApplicable();
	}

  /**
   * Gets the full name of the method.
   * @return The full name of the method.
   */
  @Override
  public String getName()
  {
    return methodName;
  }
}
