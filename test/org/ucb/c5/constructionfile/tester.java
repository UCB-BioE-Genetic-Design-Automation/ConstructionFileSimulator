/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.simulators.PCRSimulator;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author yishe
 */
public class tester {

    public static void main(String[] args) throws Exception {
        PCRSimulator sim = new PCRSimulator();
        sim.initiate();
        //run a lab-confirmed PCR on a genomic DNA (full length) template
        //pcr odxs3/odxs4 on MG1655	(1916 bp, dxs)
        String odxs3 = "ccataCGTCTCatAcaataagtattaataggcccc";
        String odxs4 = "gactaCGTCTCaagggttatgccagccaggccttg";
        List<Polynucleotide> templates = new ArrayList<>();
        String data = FileUtils.readResourceFile("constructionfile/data/MG1655_genome.txt");
        data = data.replaceAll("\\r|\\r?\\n", "");

        templates.add(new Polynucleotide(data, true));

        String pdt = sim.run(odxs3, odxs4, templates);
        System.out.println(pdt);
    }

}
