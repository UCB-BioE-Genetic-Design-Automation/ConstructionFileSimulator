/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import org.junit.Test;
import org.ucb.c5.constructionfile.simulators.ExtensionSimulator;
/**
 *
 * @author yishe
 */
public class ExtensionSimulatorTest {
    
    @Test
        public void T7Promoter() throws Exception {
            String s1 = "TAATACGACTCACTATAGGG".toUpperCase();  
            String s2 = "TTTCCCTATAGTGAGTCGTATTA".toUpperCase();
            
            ExtensionSimulator exCalculator = new ExtensionSimulator();
            exCalculator.initiate();
            int result = exCalculator.run(s1, s2);
            System.out.println(result);
            assert(result == s2.length()-3);
            
            
        }
        
            @Test
        public void Ex1() throws Exception {
            String s1 = "TCAGATCCTTCCGTATTTAGCAAA".toUpperCase();  
            String s2 = "ACCACACTAGAGAACATACTGGCTAAATACGGAAGGATCTGA".toUpperCase();
            
            ExtensionSimulator exCalculator = new ExtensionSimulator();
            exCalculator.initiate();
            int result = exCalculator.run(s1, s2);
            System.out.println(result);
            assert(result == -1);
        }
    
}
