///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.ucb.c5.constructionfile.simulators;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import org.ucb.c5.sequtils.RestrictionEnzymeFactory;
//import org.ucb.c5.constructionfile.model.Assembly;
//import org.ucb.c5.constructionfile.model.Enzyme;
//import org.ucb.c5.constructionfile.model.Polynucleotide;
//import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
//import org.ucb.c5.sequtils.RevComp;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import org.ucb.c5.constructionfile.ParseConstructionFile;
//import org.ucb.c5.constructionfile.SimulateConstructionFile;
//import org.ucb.c5.constructionfile.model.ConstructionFile;
//import org.ucb.c5.constructionfile.simulators.AssemblySimulator;
//import org.ucb.c5.utils.FileUtils;
//import org.ucb.c5.constructionfile.model.Assembly;
//
//
///**
// *
// * @author michaelfernandez
// */
//public class GoldenGateSimulatorTest {
//
//    @Test
//    public void GoldenGateSimulatorT() throws Exception {
//        //From PCR_simulator object
//        AssemblySimulator assemblySimulator = new AssemblySimulator();
//        assemblySimulator.initiate();
//
//        Polynucleotide frag1 = new Polynucleotide("AGTGG"+"GGTCTCG"+"ATATAggtttcttagacgtcaggtggcacttttcggggaaatgt");
//        Polynucleotide frag2 = new Polynucleotide("AGTGG"+"GGTCTCG"+"TATATGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTG");
//
//        List<String> assemblyFragments = new ArrayList<>();
//        assemblyFragments.add("frag1");
//        assemblyFragments.add("frag2");
//
//        Assembly assembly= new Assembly(assemblyFragments,Enzyme.BsaI,"PDT");
//
//         Map<String, Polynucleotide> fragments = new HashMap<>();
//         fragments.put("frag1",frag1);
//         fragments.put("frag2",frag2);
//
//        //Run the assembly simulator
//        Polynucleotide assemblyProduct = assemblySimulator.run(assembly, fragments);
//
//        Polynucleotide expected = new Polynucleotide("CACCGTCTTGCAGGGAGGAGTCCTGGGTAACGGTAACAACATATAAggtttcttagacgtcaggtggcacttttcggggaaatgt");
//        //Assert that the product is the polynucleotide that would result from a golden gate reaction
//        assert (expected.equals(assemblyProduct));
//}
//
//}
