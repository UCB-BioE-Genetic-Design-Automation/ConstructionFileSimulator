/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.util.HashMap;
import org.junit.Test;
import org.ucb.c5.constructionfile.simulators.TmCalculator;


/**
 *
 * @author yishe
 */
public class TmCalculatorTest {
    
    @Test
    /**
     * https://www.med.upenn.edu/naf/services/standard.html
     */
    public void UPenTest() throws Exception {
            String s1 = "ccaaaAGATCTatgagcggcttcccccgcag".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 72 && result <74);
    }
    @Test
        public void T7Promoter() throws Exception {
            String s1 = "TAATACGACTCACTATAGGG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 50 && result <52);
    }
    @Test    
        public void T3Promoter() throws Exception {
            String s1 = "CAATTAACCCTCACTAAAGG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 50 && result <52);
    }
    @Test
        public void M13Forward_20() throws Exception {
            String s1 = "GTAAAACGACGGCCAGTG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 53 && result <55);
    }
    @Test
        public void M13Forward_41() throws Exception {
            String s1 = "GGTTTTCCCAGTCACGAC".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 53 && result <55);
    }
    @Test
        public void M13Forward_27() throws Exception {
            String s1 = "GGAAACAGCTATGACCATG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 52 && result <54);
    }
    @Test
        public void M13Forward_48() throws Exception {
            String s1 = "AGCGGATAACAATTTCACAC".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 50 && result <52);
    }
    @Test
        public void SP6Promoter() throws Exception {
            String s1 = "TACGATTTAGGTGACACTATAG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 52 && result <54);
    }
    @Test
        public void pBluescriptSK() throws Exception {
            String s1 = "CGCTCTAGAACTAGTGGATC".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 54 && result <56);
    }
    @Test
        public void pBluescriptKS() throws Exception {
            String s1 = "CTCGAGGTCGACGGTATCG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 58 && result <60);
    }
    @Test
        public void LambdaGt11Forward() throws Exception {
            String s1 = "GGTGGCGACGACTCCTGGAGCCCG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 70 && result <72);
    }
    @Test
        public void LambdaGt11Reverse() throws Exception {
            String s1 = "TTGACACCAGACCAACTGGTAATG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 58 && result <60);
    }
    @Test
        public void LambdaGt10Forward() throws Exception {
            String s1 = "AGCAAGTTCAGCCTGGTTAAG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 55 && result <57);
    }
    @Test
        public void LambdaGt10Reverse() throws Exception {
            String s1 = "CTTATGAGTATTTCTTCCAGGGTA".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 54 && result <56);
    }
    @Test
        public void pBR322Bam_HI_CW() throws Exception {
            String s1 = "CACTATCGACTACGCGATCA".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 54 && result <56);
    }
    @Test
        public void pBR322Bam_HI_CCW	() throws Exception {
            String s1 = "ATGCGTCCGGCGTAGA".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 51 && result <53);
    }
    @Test
        public void pBR322Eco_RI_CW	() throws Exception {
            String s1 = "GTATCACGAGGCCCTT".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 49 && result <51);
    }
    @Test
        public void pBR322Eco_RI_CCW	() throws Exception {
            String s1 = "GATAAGCTGTCAAAC".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 41 && result <43);
    }
    @Test
        public void pBR322HIND_III_CW() throws Exception {
            String s1 = "GACAGCTTATCATCG".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 43 && result <45);
    }
    @Test
        public void pBR322HIND_III_CCW() throws Exception {
            String s1 = "GCAATTTAACTGTGAT".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 41 && result <43);
    }
    @Test
        public void pBR322Pst_I_CW() throws Exception {
            String s1 = "GCTAGAGTAAGTAGTT".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 43 && result <45);
    }
    @Test
        public void pBR322Pst_I_CCW() throws Exception {
            String s1 = "AACGACGAGCGTGAC".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 47 && result <49);
    }
    @Test
        public void pBR322Sal_I_CW() throws Exception {
            String s1 = "ATGCAGGAGTCGCAT".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 45 && result <47);
    }
    @Test
        public void pBR322Sal_I_CCW	() throws Exception {
            String s1 = "AGTCATGCCCCGCGC".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 51 && result <53);
    }
    @Test
        public void RandomHexamer() throws Exception {
            String s1 = "NNNNNN".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 57 && result <59);
    }
    @Test
        public void PolydT20() throws Exception {
            String s1 = "TTTTTTTTTTTTTTTTTTTT".toUpperCase();  
    
            TmCalculator tmCalculator = new TmCalculator();
            tmCalculator.initiate();
            double result = tmCalculator.run(s1.toCharArray(),s1.toCharArray());
            System.out.println(s1+result);
            assert(result > 34 && result <36);
    }

}
