/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import examples.MainTest;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author yishe
 */
public class EX {
    public static void main(String[] args) {

		ArrayList<String> CNGmethods = new ArrayList<String>();
		CNGmethods.add("bro05");
		
		Properties CNGSequences = MainTest.loadSequencesTest("src/examples/test/CNGSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t bro05 \n");

		MainTest.displayResultsSelf(CNGSequences, CNGmethods, "rnarna", "Na=1", "0.0001", "-CNG");
	}
}
