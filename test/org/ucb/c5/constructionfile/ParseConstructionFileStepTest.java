/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.ucb.c5.constructionfile.model.Acquisition;
import org.ucb.c5.constructionfile.model.Antibiotic;
import org.ucb.c5.constructionfile.model.Assembly;
import org.ucb.c5.constructionfile.model.Blunting;
import org.ucb.c5.constructionfile.model.BluntingType;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Enzyme;
import org.ucb.c5.constructionfile.model.Ligation;
import org.ucb.c5.constructionfile.model.PCA;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

/**
 *
 * @author Zihang Shao
 */
public class ParseConstructionFileStepTest {
    
    
    @Test
    public void testPCR() throws Exception {
        String rawText = 
                "pcr A1,B1 on C1\t(S1 bp,D1)\n" +
                "pcr A2 B2 on C2\t( S2 bp,D2)\n" +
                "pcr A3, B3 on  C3\t (S3 bp, D3)\n"+
                "pcr A4\tB4 on \tC4 \t(S4 bp, D4)\n"+
                "pcr A5   B5 on   C5    (S5 bp, D5 )\n"+
                "pcr A6 and B6 on C6 (S6bp,D6)\n"+
                "pcr A7/B7 on C7 \t ( S7bp,D7 )\n"+
                
                "pcr A8,B8 on C8\t(D8)\n" +
                "pcr A9 B9 on C9 \t( D9)\n" +
                "pcr A10, B10 on  C10\t (D10 )\n"+
                "pcr A11\tB11 on \tC11 \t (D11   )\n"+
                "pcr A12   B12 on   C12    (    D12 )\n"+
                "pcr A13 and B13 on C13 ( D13 )\n"+
                "pcr A14/B14 on C14 \t ( D14\t )\n"
                ;
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
               
        for(int i=1;i<=steps.size();i++){
            PCR pcr = (PCR) steps.get(i-1);
            assert(pcr.getOligo1().equals("A"+i));
            assert(pcr.getOligo2().equals("B"+i));
            assert(pcr.getTemplate().equals("C"+i));
            assert(pcr.getProduct().equals("D"+i));
        }    
                
    }
    
    
    @Test
    public void testPCRRange() throws Exception {
        String rawText = 
                "pcr a1-a2 on C1\t(S1 bp,D1)\n" +
                "pcr b3-b4   on C2\t( S2 bp,D2)\n" +
                "pcr c23-c24 on  C3\t (S3 bp, D3)\n"+
                "pcr d99-d100 \t on   C4 (    S4 bp,   D4    )"
                ;
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        
        PCR pcr0 = (PCR) steps.get(0);
        assert(pcr0.getOligo1().equals("a1"));
        assert(pcr0.getOligo2().equals("a2"));
        assert(pcr0.getTemplate().equals("C1"));
        assert(pcr0.getProduct().equals("D1"));
        
        PCR pcr1 = (PCR) steps.get(1);
        assert(pcr1.getOligo1().equals("b3"));
        assert(pcr1.getOligo2().equals("b4"));
        assert(pcr1.getTemplate().equals("C2"));
        assert(pcr1.getProduct().equals("D2"));
        
        PCR pcr2 = (PCR) steps.get(2);
        assert(pcr2.getOligo1().equals("c23"));
        assert(pcr2.getOligo2().equals("c24"));
        assert(pcr2.getTemplate().equals("C3"));
        assert(pcr2.getProduct().equals("D3"));
        
        PCR pcr3 = (PCR) steps.get(3);
        assert(pcr3.getOligo1().equals("d99"));
        assert(pcr3.getOligo2().equals("d100"));
        assert(pcr3.getTemplate().equals("C4"));
        assert(pcr3.getProduct().equals("D4"));
                       
    }
    
    
    @Test
    public void testPCA() throws Exception {
        String rawText = 
                "pca a1,b1\t(c1)\n" +
                "pca a2 b2 \t( c2)\n"+
                "pca a3, b3\t (c3 )\n"+
                "pca a4\tb4 \t ( c4 )\n"+
                "pca a5   b5    ( c5)\n"+
                "pca a6 and b6 (c6 )\n"+
                "pca a7/b7  ( c7 )\n"+
                "pca a8,  b8  (c8)"
                ;
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
                
        List<Step> steps = CF.getSteps();
        
        for(int i=1;i<=steps.size();i++){
            PCA pca = (PCA) steps.get(i-1);
            assert(pca.getOligoPool().get(0).equals("a"+i));
            assert(pca.getOligoPool().get(1).equals("b"+i));
            assert(pca.getProduct().equals("c"+i));
        }
                    
    }
    
    
    @Test
    public void testDigestion() throws Exception {
        String rawText = 
                "digest a1 with DpnI,SpeI\t(d1)\n" +
                "digest a2 with XbaI, XhoI \t( d2)\n"+
                "digest a3 with BsaI\tBsmBI\t (d3 )\n"+
                "digest a4 with Gibson AarI \t ( d4 )\n"+
                "digest a5 with BbsI    EcoRI (  d5)\n"+
                "digest a6 with BamHI and BglII   (d6   )\n"+
                "digest a7 with MfeI/PstI  (   d7   )\n"
                ;
                
        List[] ezlist = new List[7];
        ezlist[0] = Arrays.asList(Enzyme.valueOf("DpnI"),Enzyme.valueOf("SpeI"));
        ezlist[1] = Arrays.asList(Enzyme.valueOf("XbaI"),Enzyme.valueOf("XhoI"));
        ezlist[2] = Arrays.asList(Enzyme.valueOf("BsaI"),Enzyme.valueOf("BsmBI"));
        ezlist[3] = Arrays.asList(Enzyme.valueOf("Gibson"),Enzyme.valueOf("AarI"));
        ezlist[4] = Arrays.asList(Enzyme.valueOf("BbsI"),Enzyme.valueOf("EcoRI"));
        ezlist[5] = Arrays.asList(Enzyme.valueOf("BamHI"),Enzyme.valueOf("BglII"));
        ezlist[6] = Arrays.asList(Enzyme.valueOf("MfeI"),Enzyme.valueOf("PstI"));
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        
        for(int i=1;i<=steps.size();i++){
            Digestion digest = (Digestion) steps.get(i-1);
            assert(digest.getSubstrate().equals("a"+i));
            assert(digest.getEnzymes().equals(ezlist[i-1]));
            assert(digest.getProduct().equals("d"+i));
            
        }
                
    }
    
    
    @Test
    public void testLigation() throws Exception {
        String rawText = 
                "ligate a1,b1\t(c1)\n" +
                "ligate a2 b2 \t( c2)\n"+
                "ligate a3, b3\t (c3 )\n"+
                "ligate a4\tb4 \t ( c4 )\n"+
                "ligate a5   b5    ( c5)\n"+
                "ligate a6 and b6 (c6 )\n"+
                "ligate a7/b7  ( c7 )\n"+
                "ligate a8,  b8  (c8)"
                ;
                
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        
        for(int i=1;i<=steps.size();i++){
            Ligation ligate = (Ligation) steps.get(i-1);
            assert(ligate.getFragments().get(0).equals("a"+i));
            assert(ligate.getFragments().get(1).equals("b"+i));
            assert(ligate.getProduct().equals("c"+i));
        }
                   
    }
    
    
    
    @Test
    public void testTransformation() throws Exception {
        String rawText = 
                "transform a1\t(b1,Spec d1)\n" +
                "transform a2 \t( b2, Amp   d2)\n" +
                "transform a3\t (b3/Kan/ d3 )\n"+
                "transform a4 \t ( b4,  Cam   d4 )\n"+
                "transform a5 (  b5\tTet,d5   )\n"+
                "transform a6   ( b6,Gen d6)\n"+
                "transform a7 (b7, Pur)\n"+
                "transform a8 (b8  None)\n"+
                "transform a9 (b9/None)\n"
                ;
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        Antibiotic[] ab = new Antibiotic[9];
        ab[0] = Antibiotic.valueOf("Spec");
        ab[1] = Antibiotic.valueOf("Amp");
        ab[2] = Antibiotic.valueOf("Kan");
        ab[3] = Antibiotic.valueOf("Cam");
        ab[4] = Antibiotic.valueOf("Tet");
        ab[5] = Antibiotic.valueOf("Gen");
        ab[6] = Antibiotic.valueOf("Pur");
        ab[7] = Antibiotic.valueOf("None");
        ab[8] = Antibiotic.valueOf("None");

        
        List<Step> steps = CF.getSteps();
        
        for(int i=1;i<=steps.size();i++){
            Transformation transform = (Transformation) steps.get(i-1);
            assert(transform.getDna().equals("a"+i));
            assert(transform.getStrain().equals("b"+i));
            assert(transform.getAntibiotic().equals(ab[i-1]));
        }
                
    }  
    
    
    @Test
    public void testAssembly() throws Exception {
        String rawText = 
                "assemble a1,b1\t(DpnI,c1)\n" +
                "assemble a2, b2 \t( XbaI, c2)\n"+
                "assemble a3   b3\t (BsaI\tc3 )\n"+
                "assemble a4\tb4 \t ( Gibson c4 )\n"+
                "assemble a5 and b5 (BbsI    c5)\n"+
                "assemble a6/b6    (   BamHI/c6     )\n"
                ;
                
        Enzyme[] ez = new Enzyme[6];
        ez[0] = Enzyme.valueOf("DpnI");
        ez[1] = Enzyme.valueOf("XbaI");
        ez[2] = Enzyme.valueOf("BsaI");
        ez[3] = Enzyme.valueOf("Gibson");
        ez[4] = Enzyme.valueOf("BbsI");
        ez[5] = Enzyme.valueOf("BamHI");
                
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        
        for(int i=1;i<=steps.size();i++){
            Assembly assemble = (Assembly) steps.get(i-1);
            assert(assemble.getFragments().get(0).equals("a"+i));
            assert(assemble.getFragments().get(1).equals("b"+i));
            assert(assemble.getEnzyme().equals(ez[i-1]));
            assert(assemble.getProduct().equals("c"+i));
        }
               
    }
       
    
    @Test
    public void testBlunting() throws Exception {
        String rawText = 
                "blunting a1\t(b1,c1)\n" +
                "blunting a2 \t( b2, c2)\n"+
                "blunting a3\t (b3 c3 )\n"+
                "blunting a4 \t ( b4   c4 )\n"+
                "blunting a5       (    b5\tc5)\n"+
                "blunting a6 (b6 \t c6    )\n"+
                "blunting a7(    b7/c7   )\n"
                ;
                             
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        
        for(int i=1;i<=steps.size();i++){
            Blunting blunt = (Blunting) steps.get(i-1);
            assert(blunt.getSubstrate().equals("a"+i));
            assert(blunt.getTypes().equals("b"+i));
            assert(blunt.getProduct().equals("c"+i));
        }
        
    }
    
}
