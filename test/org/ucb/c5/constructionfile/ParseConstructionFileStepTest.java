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
                "pcr A1,A2 on B1\t(C1 bp,D1)\n" +
                "pcr A3 A4 on B2\t( C2 bp,D2)\n" +
                "pcr A5, A6 on  B3\t (C3 bp, D3)\n"+
                "pcr A7\tA8 on \tB4 \t(C4 bp, D4)\n"+
                "pcr A9   A10 on   B5    (C5 bp, D5 )\n"+
                "pcr A11 and A12 on B6 (C6bp,D6)\n"+
                "pcr A13/A14 on B7 \t ( C7bp,D7 )\n"
                ;
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        Step step1 = new PCR("A1", "A2", "B1", "D1");
        Step step2 = new PCR("A3", "A4", "B2", "D2");
        Step step3 = new PCR("A5", "A6", "B3", "D3");
        Step step4 = new PCR("A7", "A8", "B4", "D4");
        Step step5 = new PCR("A9", "A10", "B5", "D5");
        Step step6 = new PCR("A11", "A12", "B6", "D6");
        Step step7 = new PCR("A13", "A14", "B7", "D7");

        if ( step1 == steps.get(1) &&
             step2 == steps.get(2) &&
             step3 == steps.get(3) &&
             step4 == steps.get(4) &&
             step5 == steps.get(5) &&
             step6 == steps.get(6) &&
             step7 == steps.get(7) 
           ){
        assert(true);
        }        
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
        
        List<String> str1 = Arrays.asList("a1","b1");
        List<String> str2 = Arrays.asList("a2","b2");
        List<String> str3 = Arrays.asList("a3","b3");
        List<String> str4 = Arrays.asList("a4","b4");
        List<String> str5 = Arrays.asList("a5","b5");
        List<String> str6 = Arrays.asList("a6","b6");
        List<String> str7 = Arrays.asList("a7","b7");
        List<String> str8 = Arrays.asList("a8","b8");
                
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        Step step1 = new PCA(str1, "c1");
        Step step2 = new PCA(str2, "c2");
        Step step3 = new PCA(str3, "c3");
        Step step4 = new PCA(str4, "c4");
        Step step5 = new PCA(str5, "c5");
        Step step6 = new PCA(str6, "c6");
        Step step7 = new PCA(str7, "c7");
        Step step8 = new PCA(str8, "c8");


        if (step1 == steps.get(1) &&
            step2 == steps.get(2) &&
            step3 == steps.get(3) &&
            step4 == steps.get(4) &&
            step5 == steps.get(5) &&
            step6 == steps.get(6) &&
            step7 == steps.get(7) &&
            step8 == steps.get(8)
           ){
        assert(true);
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
        
        List<Enzyme> str1 = Arrays.asList(Enzyme.valueOf("DpnI"),Enzyme.valueOf("SpeI"));
        List<Enzyme> str2 = Arrays.asList(Enzyme.valueOf("XbaI"),Enzyme.valueOf("XhoI"));
        List<Enzyme> str3 = Arrays.asList(Enzyme.valueOf("BsaI"),Enzyme.valueOf("BsmBI"));
        List<Enzyme> str4 = Arrays.asList(Enzyme.valueOf("Gibson"),Enzyme.valueOf("AarI"));
        List<Enzyme> str5 = Arrays.asList(Enzyme.valueOf("BbsI"),Enzyme.valueOf("EcoRI"));
        List<Enzyme> str6 = Arrays.asList(Enzyme.valueOf("BamHI"),Enzyme.valueOf("BglII"));
        List<Enzyme> str7 = Arrays.asList(Enzyme.valueOf("MfeI"),Enzyme.valueOf("PstI"));
                
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        Step step1 = new Digestion("a1", str1, "d1");
        Step step2 = new Digestion("a2", str2, "d2");
        Step step3 = new Digestion("a3", str3, "d3");
        Step step4 = new Digestion("a4", str4, "d4");
        Step step5 = new Digestion("a5", str5, "d5");
        Step step6 = new Digestion("a6", str6, "d6");
        Step step7 = new Digestion("a7", str7, "d7");


        if (step1 == steps.get(1) &&
            step2 == steps.get(2) &&
            step3 == steps.get(3) &&
            step4 == steps.get(4) &&
            step5 == steps.get(5) &&
            step6 == steps.get(6) &&
            step7 == steps.get(7) 
           ){
        assert(true);
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
        
        List<String> str1 = Arrays.asList("a1","b1");
        List<String> str2 = Arrays.asList("a2","b2");
        List<String> str3 = Arrays.asList("a3","b3");
        List<String> str4 = Arrays.asList("a4","b4");
        List<String> str5 = Arrays.asList("a5","b5");
        List<String> str6 = Arrays.asList("a6","b6");
        List<String> str7 = Arrays.asList("a7","b7");
        List<String> str8 = Arrays.asList("a8","b8");
                
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        Step step1 = new Ligation(str1, "c1");
        Step step2 = new Ligation(str2, "c2");
        Step step3 = new Ligation(str3, "c3");
        Step step4 = new Ligation(str4, "c4");
        Step step5 = new Ligation(str5, "c5");
        Step step6 = new Ligation(str6, "c6");
        Step step7 = new Ligation(str7, "c7");
        Step step8 = new Ligation(str8, "c8");


        if (step1 == steps.get(1) &&
            step2 == steps.get(2) &&
            step3 == steps.get(3) &&
            step4 == steps.get(4) &&
            step5 == steps.get(5) &&
            step6 == steps.get(6) &&
            step7 == steps.get(7) &&
            step8 == steps.get(8)
           ){
        assert(true);
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
        
        Antibiotic ab1 = Antibiotic.valueOf("Spec");
        Antibiotic ab2 = Antibiotic.valueOf("Amp");
        Antibiotic ab3 = Antibiotic.valueOf("Kan");
        Antibiotic ab4 = Antibiotic.valueOf("Cam");
        Antibiotic ab5 = Antibiotic.valueOf("Tet");
        Antibiotic ab6 = Antibiotic.valueOf("Gen");
        Antibiotic ab7 = Antibiotic.valueOf("Pur");
        Antibiotic ab8 = Antibiotic.valueOf("None");

        
        List<Step> steps = CF.getSteps();
        Step step1 = new Transformation("a1", "b1", ab1, "d1");
        Step step2 = new Transformation("a2", "b2", ab2, "d2");
        Step step3 = new Transformation("a3", "b3", ab3, "d3");
        Step step4 = new Transformation("a4", "b4", ab4, "d4");
        Step step5 = new Transformation("a5", "b5", ab5, "d5");
        Step step6 = new Transformation("a6", "b6", ab6, "d6");
        Step step7 = new Transformation("a7", "b7", ab7, "a7");
        Step step8 = new Transformation("a8", "b8", ab8, "a8");
        Step step9 = new Transformation("a9", "b9", ab8, "a9");

        if (step1 == steps.get(1) &&
            step2 == steps.get(2) &&
            step3 == steps.get(3) &&
            step4 == steps.get(4) &&
            step5 == steps.get(5) &&
            step6 == steps.get(6) &&
            step7 == steps.get(7) &&
            step8 == steps.get(8) &&
            step9 == steps.get(9) 
           ){
        assert(true);
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
        
        List<String> str1 = Arrays.asList("a1","b1");
        Enzyme ez1 = Enzyme.valueOf("DpnI");
        List<String> str2 = Arrays.asList("a2","b2");
        Enzyme ez2 = Enzyme.valueOf("XbaI");
        List<String> str3 = Arrays.asList("a3","b3");
        Enzyme ez3 = Enzyme.valueOf("BsaI");
        List<String> str4 = Arrays.asList("a4","b4");
        Enzyme ez4 = Enzyme.valueOf("Gibson");
        List<String> str5 = Arrays.asList("a5","b5");
        Enzyme ez5 = Enzyme.valueOf("BbsI");
        List<String> str6 = Arrays.asList("a6","b6");
        Enzyme ez6 = Enzyme.valueOf("BamHI");
        
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);
        
        List<Step> steps = CF.getSteps();
        Step step1 = new Assembly(str1, ez1, "c1");
        Step step2 = new Assembly(str2, ez2, "c2");
        Step step3 = new Assembly(str3, ez3, "c3");
        Step step4 = new Assembly(str4, ez4, "c4");
        Step step5 = new Assembly(str5, ez5, "c5");
        Step step6 = new Assembly(str6, ez6, "c6");
        
        if (step1 == steps.get(1) &&
            step2 == steps.get(2) &&
            step3 == steps.get(3) &&
            step4 == steps.get(4) &&
            step5 == steps.get(5) &&
            step6 == steps.get(6)  
           ){
        assert(true);
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
        Step step1 = new Blunting("a1", "b1", "c1");
        Step step2 = new Blunting("a2", "b2", "c2");
        Step step3 = new Blunting("a3", "b3", "c3");
        Step step4 = new Blunting("a4", "b4", "c4");
        Step step5 = new Blunting("a5", "b5", "c5");
        Step step6 = new Blunting("a6", "b6", "c6");
        Step step7 = new Blunting("a7", "b7", "c7");


        if (step1 == steps.get(1) &&
            step2 == steps.get(2) &&
            step3 == steps.get(3) &&
            step4 == steps.get(4) &&
            step5 == steps.get(5) &&
            step6 == steps.get(6) &&
            step7 == steps.get(7) 
           ){
        assert(true);
        }        
    }
    
}
