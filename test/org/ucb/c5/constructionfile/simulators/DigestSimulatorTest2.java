/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
import org.ucb.c5.sequtils.RestrictionEnzymeFactory;
import org.ucb.c5.utils.SequenceUtils;

/**
 *
 * @author nassimnatalieataii
 */
public class DigestSimulatorTest2 {
     List<RestrictionEnzyme> enzymes = null;
     
    @Test 
    /**
     * Test EcORI digest on linear polynucleotide object, check sticky ends of both products.
     */
    public void cutLinearDNA() throws Exception {
       //Input a linear poly with EcoRI recognition site >5 bps from the ends 
       Polynucleotide poly1 = new Polynucleotide("caaaaagaattccccccg");
       
       //Run Digest Simulator on it
       RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
       factory.initiate();
       String enzname = "EcoRI";
       RestrictionEnzyme ecoRI = factory.run(enzname);
       ArrayList<RestrictionEnzyme> enzymes = new ArrayList<RestrictionEnzyme>();
       enzymes.add(ecoRI);
       
       //Run  
       DigestSimulator digestSimulator = new DigestSimulator(); 
       digestSimulator.initiate();
       List<Polynucleotide> testDigest = digestSimulator.run(poly1, enzymes);
        
       //Assert the two products have the correct sticky end
       Polynucleotide product1 = testDigest.get(0);
       Polynucleotide product2 = testDigest.get(1);
       String stickyEnd3Prime = product1.getExt3();
       String stickyEnd5Prime = product2.getExt5(); 
        
       //Assertion 
       String ecorIStickyEnd = "aatt"; //getExt3 will read back the complement of the overhang which will also be AATT. 
       assert(product1.getExt5().isEmpty());
       assert (stickyEnd3Prime.equals(ecorIStickyEnd));
       assert (stickyEnd5Prime.equals(ecorIStickyEnd));
       assert (product2.getExt3().isEmpty());
     } 
    
    @Test
     /**
     * Test EcORI digest on circular polynucleotide object, check sticky ends of one product.
     */
    public void cutCircularDNA() throws Exception {
       //Input circular Poly (pTargetF) with EcoRI recognition site 
       Polynucleotide poly1 = new Polynucleotide("catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca", true);
        
       //Run Digest Simulator on it
       RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
       factory.initiate();
       String enzname = "EcoRI";
       RestrictionEnzyme ecoRI = factory.run(enzname);
       ArrayList<RestrictionEnzyme> enzymes = new ArrayList<RestrictionEnzyme>();
       enzymes.add(ecoRI);

       //Run  
       DigestSimulator digestSimulator = new DigestSimulator(); 
       digestSimulator.initiate();
       List<Polynucleotide> testDigest = digestSimulator.run(poly1, enzymes);
       Polynucleotide product = testDigest.get(0);
       String stickyEnd3Prime = product.getExt3();
       String stickyEnd5Prime = product.getExt5(); 
       
       //Assertion 
       String ecorIStickyEnd = "aatt"; //getExt3 will read back the complement of the overhang which will also be AATT 
       assert(stickyEnd3Prime.equals(ecorIStickyEnd));
       assert(stickyEnd5Prime.equals(ecorIStickyEnd));    
    }
    
    @Test
    /**
     * Test BsaI digest on circular polynucleotide object, check sticky ends of one product.
     */
    public void TypeIIsCutting() throws Exception {
       //Input circular sequence with BsaI recognition site, pTargF with added BsaI recognition site  
       Polynucleotide poly = new Polynucleotide("atgtcatcagcggtggagtgGGTCTCcaatgtcatgagggaagcgg", true);
        
       //Run Digest Simulator on it
       RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
       factory.initiate();
       String enzname = "BsaI";
       RestrictionEnzyme bsaI = factory.run(enzname);
       ArrayList<RestrictionEnzyme> enzymes = new ArrayList<RestrictionEnzyme>();
       enzymes.add(bsaI);
       
       //Run  
       DigestSimulator digestSimulator = new DigestSimulator(); 
       digestSimulator.initiate();
       List<Polynucleotide> testDigest = digestSimulator.run(poly, enzymes);
       Polynucleotide product = testDigest.get(0);
       String stickyEnd3Prime = product.getExt3();
       String stickyEnd5Prime = product.getExt5(); 
       String bsaIStickyEnd = "aatg";  
       
       //Assert a single product, 5' overhang is expected stickyend, and 3' overhang is expected sticky end (get3Ext will give complement of 3' sticky end)
       assert((testDigest.size() == 1));
       assert(stickyEnd5Prime.equals(bsaIStickyEnd)); 
       assert(stickyEnd3Prime.equals(bsaIStickyEnd));
    }
    
    @Test 
     /**
     * Test EcORI digest on linear polynucleotide object with less than 5bp distance to ends
     * Currently this test retains original poly instead of throwing exception, in future may need to change assert statement into try-catch block.
     */
     public void TooCloseToEnds() throws Exception {
       //Input a linear poly <5 bps from the ends, EcoRI
       Polynucleotide poly = new Polynucleotide("gaattcaaaaaa");
       
       //Run Digest Simulator on it
       RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
       factory.initiate();
       String enzname = "EcoRI";
       RestrictionEnzyme ecoRI = factory.run(enzname);
       ArrayList<RestrictionEnzyme> enzymes = new ArrayList<RestrictionEnzyme>();
       enzymes.add(ecoRI);
       DigestSimulator digestSimulator = new DigestSimulator(); 
       digestSimulator.initiate();
       List<Polynucleotide> testDigest = digestSimulator.run(poly, enzymes); 
            
        //Assert it testDigest does not make two objects
            assert(testDigest.size() < 2);                   
        }
     }
    
    
    
