package org.ucb.c5.constructionfile;

import java.util.HashMap;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.utils.FileUtils;
import org.junit.Test;

public class BluntingSimulateConstructionFileTest {
    
    @Test
    public void testpBRpdig() throws Exception {
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        
        String text = FileUtils.readResourceFile("constructionfile/data/Construction of pBRpdig.txt");
        ConstructionFile CF = pCF.run(text);
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        Polynucleotide product = simulateConstructionFile.run(CF, new HashMap<>());
        String pBRpdigSeq = FileUtils.readResourceFile("constructionfile/data/pBRpdig.seq");
        int origin = pBRpdigSeq.lastIndexOf("ORIGIN");
        String rawSeq = pBRpdigSeq.substring(origin + 6);
        String expectSeq = rawSeq.replaceAll("[^A-za-z]", "");
        Polynucleotide expected = new Polynucleotide(expectSeq, true);
        boolean temp = expected.equals(product);
        assert(temp==true);
    }

    @Test
    public void testpBRxdig() throws Exception {
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        
        String text = FileUtils.readResourceFile("constructionfile/data/Construction of pBRxdig.txt");
        ConstructionFile CF = pCF.run(text);
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        Polynucleotide product = simulateConstructionFile.run(CF, new HashMap<>());
        String pBRpdigSeq = FileUtils.readResourceFile("constructionfile/data/pBRpdig.ape");
        int origin = pBRpdigSeq.lastIndexOf("ORIGIN");
        String rawSeq = pBRpdigSeq.substring(origin + 6);
        String expectSeq = rawSeq.replaceAll("[^A-za-z]", "");
        Polynucleotide expected = new Polynucleotide(expectSeq, true);
        boolean temp = expected.equals(product);
        assert(temp==true);
    }

    
}
