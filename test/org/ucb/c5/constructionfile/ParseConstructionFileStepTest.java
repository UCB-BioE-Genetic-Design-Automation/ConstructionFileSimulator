/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.ucb.c5.constructionfile.model.Antibiotic;
import org.ucb.c5.constructionfile.model.Assembly;
import org.ucb.c5.constructionfile.model.Blunting;
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
        String rawText
                = "pcr A1,B1 on C1\t(61 bp,D1)\n"
                + "pcr A2 B2 on C2\t( 61 bp,D2)\n"
                + "pcr A3, B3 on  C3\t (61 bp, D3)\n"
                + "pcr A4\tB4 on \tC4 \t(61 bp, D4)\n"
                + "pcr A5   B5 on   C5    (61 bp, D5 )\n"
                + "pcr A6 and B6 on C6 (61bp,D6)\n"
                + "pcr A7/B7 on C7 \t ( 61bp,D7 )\n"
                + "pcr A8,B8 on C8\t(D8)\n"
                + "pcr A9 B9 on C9 \t( D9)\n"
                + "pcr A10, B10 on  C10\t (D10 )\n"
                + "pcr A11\tB11 on \tC11 \t (D11   )\n"
                + "pcr A12   B12 on   C12    (    D12 )\n"
                + "pcr A13 and B13 on C13 ( D13 )\n"
                + "pcr A14/B14 on C14 \t ( D14\t )\n";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        for (int i = 1; i <= steps.size(); i++) {
            PCR pcr = (PCR) steps.get(i - 1);
            assert (pcr.getOligo1().equals("A" + i));
            assert (pcr.getOligo2().equals("B" + i));
            assert (pcr.getTemplates().get(0).equals("C" + i));
            assert (pcr.getProduct().equals("D" + i));
        }
    }

    @Test
    public void testPCRRange() throws Exception {
        String rawText
                = "pcr a1-a2 on C1\t(61 bp,D1)\n"
                + "pcr b3-b4   on C2\t( 61 bp,D2)\n"
                + "pcr c23-c24 on  C3\t (61 bp, D3)\n"
                + "pcr d99-d100 \t on   C4 (    61 bp,   D4    )";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        PCR pcr0 = (PCR) steps.get(0);
        assert (pcr0.getOligo1().equals("a1"));
        assert (pcr0.getOligo2().equals("a2"));
        assert (pcr0.getTemplates().get(0).equals("C1"));
        assert (pcr0.getProduct().equals("D1"));

        PCR pcr1 = (PCR) steps.get(1);
        assert (pcr1.getOligo1().equals("b3"));
        assert (pcr1.getOligo2().equals("b4"));
        assert (pcr1.getTemplates().get(0).equals("C2"));
        assert (pcr1.getProduct().equals("D2"));

        PCR pcr2 = (PCR) steps.get(2);
        assert (pcr2.getOligo1().equals("c23"));
        assert (pcr2.getOligo2().equals("c24"));
        assert (pcr2.getTemplates().get(0).equals("C3"));
        assert (pcr2.getProduct().equals("D3"));

        PCR pcr3 = (PCR) steps.get(3);
        assert (pcr3.getOligo1().equals("d99"));
        assert (pcr3.getOligo2().equals("d100"));
        assert (pcr3.getTemplates().get(0).equals("C4"));
        assert (pcr3.getProduct().equals("D4"));

    }

    @Test
    public void testPCRMultiPla() throws Exception {
        String rawText
                = "pcr a1,a2 on A1,A2\t(500 bp,N1)\n"
                + "pcr b3,b4   on B4 B5 \t( 500 bp,N2)\n"
                + "pcr c23,c24  on  C7\tC8\t (500 bp, N3)\n"
                + "pcr d2 d3 on  D11   D12 (500 bp, N4)\n"
                + "pcr d2 d3 on  D11   D12 (500 bp, N4)\n"
                + "pcr f23\tf24 on\tF99/F100     (500 bp, N6)\n"
                + "pcr g99   and g100\ton\tG999\tand     G1000 (    500 bp,   N7    )";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        PCR pcr0 = (PCR) steps.get(0);
        assert (pcr0.getOligo1().equals("a1"));
        assert (pcr0.getOligo2().equals("a2"));
        assert (pcr0.getTemplates().get(0).equals("A1"));
        assert (pcr0.getTemplates().get(1).equals("A2"));
        assert (pcr0.getProduct().equals("N1"));

        PCR pcr1 = (PCR) steps.get(1);
        assert (pcr1.getOligo1().equals("b3"));
        assert (pcr1.getOligo2().equals("b4"));
        assert (pcr1.getTemplates().get(0).equals("B4"));
        assert (pcr1.getTemplates().get(1).equals("B5"));
        assert (pcr1.getProduct().equals("N2"));

        PCR pcr2 = (PCR) steps.get(2);
        assert (pcr2.getOligo1().equals("c23"));
        assert (pcr2.getOligo2().equals("c24"));
        assert (pcr2.getTemplates().get(0).equals("C7"));
        assert (pcr2.getTemplates().get(1).equals("C8"));
        assert (pcr2.getProduct().equals("N3"));

        PCR pcr3 = (PCR) steps.get(3);
        assert (pcr3.getOligo1().equals("d2"));
        assert (pcr3.getOligo2().equals("d3"));
        assert (pcr3.getTemplates().get(0).equals("D11"));
        assert (pcr3.getTemplates().get(1).equals("D12"));
        assert (pcr3.getProduct().equals("N4"));

        PCR pcr5 = (PCR) steps.get(5);
        assert (pcr5.getOligo1().equals("f23"));
        assert (pcr5.getOligo2().equals("f24"));
        assert (pcr5.getTemplates().get(0).equals("F99"));
        assert (pcr5.getTemplates().get(1).equals("F100"));
        assert (pcr5.getProduct().equals("N6"));

        PCR pcr6 = (PCR) steps.get(6);
        assert (pcr6.getOligo1().equals("g99"));
        assert (pcr6.getOligo2().equals("g100"));
        assert (pcr6.getTemplates().get(0).equals("G999"));
        assert (pcr6.getTemplates().get(1).equals("G1000"));
        assert (pcr6.getProduct().equals("N7"));

    }

    @Test
    public void testPCRMultiPlaRange() throws Exception {
        String rawText
                = "pcr a1-a2 on A1-A2\t(61 bp,N1)\n"
                + "pcr b3-b4   on B4-B5 \t( 61 bp,N2)\n"
                + "pcr c23-c24  on  C7-C8\t (61 bp, N3)\n"
                + "pcr d2-d3 on  D11-D12 (61 bp, N4)\n"
                + "pcr e9-e10\ton  E76-E77 \t (61 bp, N5)\n"
                + "pcr f23-f24 on\tF99-F100     (61 bp, N6)\n"
                + "pcr g99-g100\ton\tG999-G1000 (    61 bp,   N7    )";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        PCR pcr0 = (PCR) steps.get(0);
        assert (pcr0.getOligo1().equals("a1"));
        assert (pcr0.getOligo2().equals("a2"));
        assert (pcr0.getTemplates().get(0).equals("A1"));
        assert (pcr0.getTemplates().get(1).equals("A2"));
        assert (pcr0.getProduct().equals("N1"));

        PCR pcr1 = (PCR) steps.get(1);
        assert (pcr1.getOligo1().equals("b3"));
        assert (pcr1.getOligo2().equals("b4"));
        assert (pcr1.getTemplates().get(0).equals("B4"));
        assert (pcr1.getTemplates().get(1).equals("B5"));
        assert (pcr1.getProduct().equals("N2"));

        PCR pcr2 = (PCR) steps.get(2);
        assert (pcr2.getOligo1().equals("c23"));
        assert (pcr2.getOligo2().equals("c24"));
        assert (pcr2.getTemplates().get(0).equals("C7"));
        assert (pcr2.getTemplates().get(1).equals("C8"));
        assert (pcr2.getProduct().equals("N3"));

        PCR pcr3 = (PCR) steps.get(3);
        assert (pcr3.getOligo1().equals("d2"));
        assert (pcr3.getOligo2().equals("d3"));
        assert (pcr3.getTemplates().get(0).equals("D11"));
        assert (pcr3.getTemplates().get(1).equals("D12"));
        assert (pcr3.getProduct().equals("N4"));

        PCR pcr4 = (PCR) steps.get(4);
        assert (pcr4.getOligo1().equals("e9"));
        assert (pcr4.getOligo2().equals("e10"));
        assert (pcr4.getTemplates().get(0).equals("E76"));
        assert (pcr4.getTemplates().get(1).equals("E77"));
        assert (pcr4.getProduct().equals("N5"));

        PCR pcr5 = (PCR) steps.get(5);
        assert (pcr5.getOligo1().equals("f23"));
        assert (pcr5.getOligo2().equals("f24"));
        assert (pcr5.getTemplates().get(0).equals("F99"));
        assert (pcr5.getTemplates().get(1).equals("F100"));
        assert (pcr5.getProduct().equals("N6"));

        PCR pcr6 = (PCR) steps.get(6);
        assert (pcr6.getOligo1().equals("g99"));
        assert (pcr6.getOligo2().equals("g100"));
        assert (pcr6.getTemplates().get(0).equals("G999"));
        assert (pcr6.getTemplates().get(1).equals("G1000"));
        assert (pcr6.getProduct().equals("N7"));

    }

    @Test
    public void testPCA() throws Exception {
        String rawText
                = "pca a1,b1\t(c1)\n"
                + "pca a2 b2 \t( c2)\n"
                + "pca a3, b3\t (c3 )\n"
                + "pca a4\tb4 \t ( c4 )\n"
                + "pca a5   b5    ( c5)\n"
                + "pca a6 and b6 (c6 )\n"
                + "pca a7/b7  ( c7 )\n"
                + "pca a8,  b8  (c8)";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        for (int i = 1; i <= steps.size(); i++) {
            PCA pca = (PCA) steps.get(i - 1);
            assert (pca.getOligoPool().get(0).equals("a" + i));
            assert (pca.getOligoPool().get(1).equals("b" + i));
            assert (pca.getProduct().equals("c" + i));
        }

    }

    @Test
    public void testPCARange() throws Exception {
        String rawText
                = "pca a1-a2 (p1)\n"
                + "pca b3-b4\t( p2)\n"
                + "pca c23-c24      (p3  )\n"
                + "pca d99-d100   \t    (    p4    )";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        PCA pca0 = (PCA) steps.get(0);
        assert (pca0.getOligoPool().get(0).equals("a1"));
        assert (pca0.getOligoPool().get(1).equals("a2"));
        assert (pca0.getProduct().equals("p1"));

        PCA pca1 = (PCA) steps.get(1);
        assert (pca1.getOligoPool().get(0).equals("b3"));
        assert (pca1.getOligoPool().get(1).equals("b4"));
        assert (pca1.getProduct().equals("p2"));

        PCA pca2 = (PCA) steps.get(2);
        assert (pca2.getOligoPool().get(0).equals("c23"));
        assert (pca2.getOligoPool().get(1).equals("c24"));
        assert (pca2.getProduct().equals("p3"));

        PCA pca3 = (PCA) steps.get(3);
        assert (pca3.getOligoPool().get(0).equals("d99"));
        assert (pca3.getOligoPool().get(1).equals("d100"));
        assert (pca3.getProduct().equals("p4"));

    }

    @Test
    public void testDigestion() throws Exception {
        String rawText
                = "digest a1 with DpnI,SpeI\t(1,d1)\n"
                + "digest a2 with XbaI, XhoI \t( 2, d2)\n"
                + "digest a3 with BsaI\tBsmBI\t (3 ,d3 )\n"
                + "digest a4 with Gibson AarI \t ( 4   ,d4 )\n"
                + "digest a5 with BbsI    EcoRI (  5 ,  d5)\n"
                + "digest a6 with BamHI and BglII   (6\t,d6   )\n"
                + "digest a7 with MfeI/PstI  (   7\t,\td7   )\n";

        List[] ezlist = new List[7];
        ezlist[0] = Arrays.asList(Enzyme.valueOf("DpnI"), Enzyme.valueOf("SpeI"));
        ezlist[1] = Arrays.asList(Enzyme.valueOf("XbaI"), Enzyme.valueOf("XhoI"));
        ezlist[2] = Arrays.asList(Enzyme.valueOf("BsaI"), Enzyme.valueOf("BsmBI"));
        ezlist[3] = Arrays.asList(Enzyme.valueOf("Gibson"), Enzyme.valueOf("AarI"));
        ezlist[4] = Arrays.asList(Enzyme.valueOf("BbsI"), Enzyme.valueOf("EcoRI"));
        ezlist[5] = Arrays.asList(Enzyme.valueOf("BamHI"), Enzyme.valueOf("BglII"));
        ezlist[6] = Arrays.asList(Enzyme.valueOf("MfeI"), Enzyme.valueOf("PstI"));

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        for (int i = 1; i <= steps.size(); i++) {
            Digestion digest = (Digestion) steps.get(i - 1);
            assert (digest.getSubstrate().equals("a" + i));
            assert (digest.getEnzymes().equals(ezlist[i - 1]));
            assert (digest.getFragSelection() == i);
            assert (digest.getProduct().equals("d" + i));

        }

    }

    @Test
    public void testLigation() throws Exception {
        String rawText
                = "ligate a1,b1\t(c1)\n"
                + "ligate a2 b2 \t( c2)\n"
                + "ligate a3, b3\t (c3 )\n"
                + "ligate a4\tb4 \t ( c4 )\n"
                + "ligate a5   b5    ( c5)\n"
                + "ligate a6 and b6 (c6 )\n"
                + "ligate a7/b7  ( c7 )\n"
                + "ligate a8,  b8  (c8)";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        for (int i = 1; i <= steps.size(); i++) {
            Ligation ligate = (Ligation) steps.get(i - 1);
            assert (ligate.getFragments().get(0).equals("a" + i));
            assert (ligate.getFragments().get(1).equals("b" + i));
            assert (ligate.getProduct().equals("c" + i));
        }

    }

    @Test
    public void testLigationRange() throws Exception {
        String rawText
                = "ligate a1-a2 (p1)\n"
                + "ligate b3-b4\t( p2)\n"
                + "ligate c23-c24      (p3  )\n"
                + "ligate d99-d100   \t    (    p4    )";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        Ligation ligate0 = (Ligation) steps.get(0);
        assert (ligate0.getFragments().get(0).equals("a1"));
        assert (ligate0.getFragments().get(1).equals("a2"));
        assert (ligate0.getProduct().equals("p1"));

        Ligation ligate1 = (Ligation) steps.get(1);
        assert (ligate1.getFragments().get(0).equals("b3"));
        assert (ligate1.getFragments().get(1).equals("b4"));
        assert (ligate1.getProduct().equals("p2"));

        Ligation ligate2 = (Ligation) steps.get(2);
        assert (ligate2.getFragments().get(0).equals("c23"));
        assert (ligate2.getFragments().get(1).equals("c24"));
        assert (ligate2.getProduct().equals("p3"));

        Ligation ligate3 = (Ligation) steps.get(3);
        assert (ligate3.getFragments().get(0).equals("d99"));
        assert (ligate3.getFragments().get(1).equals("d100"));
        assert (ligate3.getProduct().equals("p4"));

    }

    @Test
    public void testTransformation() throws Exception {
        String rawText
                = "transform a1\t(b1,Spec d1)\n"
                + "transform a2 \t( b2, Amp   d2)\n"
                + "transform a3\t (b3/Kan/ d3 )\n"
                + "transform a4 \t ( b4,  Cam   d4 )\n"
                + "transform a5 (  b5\tTet,d5   )\n"
                + "transform a6   ( b6,Gen d6)\n"
                + "transform a7 (b7, Pur)\n"
                + "transform a8 (b8  None)\n"
                + "transform a9 (b9/None)\n";

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

        for (int i = 1; i <= steps.size(); i++) {
            Transformation transform = (Transformation) steps.get(i - 1);
            assert (transform.getDna().equals("a" + i));
            assert (transform.getStrain().equals("b" + i));
            assert (transform.getAntibiotic().equals(ab[i - 1]));
        }

    }

    @Test
    public void testAssembly() throws Exception {
        String rawText
                = "assemble a1,b1\t(DpnI,c1)\n"
                + "assemble a2, b2 \t( XbaI, c2)\n"
                + "assemble a3   b3\t (BsaI\tc3 )\n"
                + "assemble a4\tb4 \t ( Gibson c4 )\n"
                + "assemble a5 and b5 (BbsI    c5)\n"
                + "assemble a6/b6    (   BamHI/c6     )\n";

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

        for (int i = 1; i <= steps.size(); i++) {
            Assembly assemble = (Assembly) steps.get(i - 1);
            assert (assemble.getFragments().get(0).equals("a" + i));
            assert (assemble.getFragments().get(1).equals("b" + i));
            assert (assemble.getEnzyme().equals(ez[i - 1]));
            assert (assemble.getProduct().equals("c" + i));
        }

    }

    @Test
    public void testAssemblyRange() throws Exception {
        String rawText
                = "assemble a1-a2\t(DpnI,p1)\n"
                + "assemble a9-a10 \t( XbaI, p2)\n"
                + "assemble b35-b36\t (BsaI\tp3 )\n"
                + "assemble c99-c100 \t ( Gibson p4 )\n"
                + "assemble c207-c208 (BbsI    p5)\n"
                + "assemble d999-d1000    (   BamHI/p6     )\n";

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

        Assembly asb0 = (Assembly) steps.get(0);
        assert (asb0.getFragments().get(0).equals("a1"));
        assert (asb0.getFragments().get(1).equals("a2"));
        assert (asb0.getEnzyme().equals(ez[0]));
        assert (asb0.getProduct().equals("p1"));

        Assembly asb1 = (Assembly) steps.get(1);
        assert (asb1.getFragments().get(0).equals("a9"));
        assert (asb1.getFragments().get(1).equals("a10"));
        assert (asb1.getEnzyme().equals(ez[1]));
        assert (asb1.getProduct().equals("p2"));

        Assembly asb2 = (Assembly) steps.get(2);
        assert (asb2.getFragments().get(0).equals("b35"));
        assert (asb2.getFragments().get(1).equals("b36"));
        assert (asb2.getEnzyme().equals(ez[2]));
        assert (asb2.getProduct().equals("p3"));

        Assembly asb3 = (Assembly) steps.get(3);
        assert (asb3.getFragments().get(0).equals("c99"));
        assert (asb3.getFragments().get(1).equals("c100"));
        assert (asb3.getEnzyme().equals(ez[3]));
        assert (asb3.getProduct().equals("p4"));

        Assembly asb4 = (Assembly) steps.get(4);
        assert (asb4.getFragments().get(0).equals("c207"));
        assert (asb4.getFragments().get(1).equals("c208"));
        assert (asb4.getEnzyme().equals(ez[4]));
        assert (asb4.getProduct().equals("p5"));

        Assembly asb5 = (Assembly) steps.get(5);
        assert (asb5.getFragments().get(0).equals("d999"));
        assert (asb5.getFragments().get(1).equals("d1000"));
        assert (asb5.getEnzyme().equals(ez[5]));
        assert (asb5.getProduct().equals("p6"));

    }

    @Test
    public void testBlunting() throws Exception {
        String rawText
                = "blunting a1\t(b1,c1)\n"
                + "blunting a2 \t( b2, c2)\n"
                + "blunting a3\t (b3 c3 )\n"
                + "blunting a4 \t ( b4   c4 )\n"
                + "blunting a5       (    b5\tc5)\n"
                + "blunting a6 (b6 \t c6    )\n"
                + "blunting a7(    b7/c7   )\n";

        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        ConstructionFile CF = pcf.run(rawText);

        List<Step> steps = CF.getSteps();

        for (int i = 1; i <= steps.size(); i++) {
            Blunting blunt = (Blunting) steps.get(i - 1);
            assert (blunt.getSubstrate().equals("a" + i));
            assert (blunt.getTypes().equals("b" + i));
            assert (blunt.getProduct().equals("c" + i));
        }

    }

}
