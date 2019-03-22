/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.twentyn.c4.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.twentyn.c4.schemas.Oligo;
import org.twentyn.c4.utils.SequenceUtils;

/**
 *
 * @author jca20n
 */
public class PCR {
    String oligo1;
    String oligo2;
    String template;
    
    public PCR(String o1, String o2, String temp) {
        this.oligo1 = o1;
        this.oligo2 = o2;
        this.template = temp;
    }

    private class Anneal {
        int site;
        int length;
        boolean rc;
    }
    
    public String getProduct() throws Exception {
        //Find best binding site for 3' end of oligo1 and oligo2 on template
       Anneal anneal1 = getAnneal(oligo1, template);
       Anneal anneal2 = getAnneal(oligo2, template);
       
       //Fix the orientation of the template
       if(anneal1.rc == false && anneal2.rc == true) {
       } else if(anneal1.rc == true && anneal2.rc == false) {
           //Flip the oligo identities
           String tem = oligo1;
           oligo1 = oligo2;
           oligo2 = tem;
           return getProduct();
       } else {
           System.err.println("Best annealing is on same strand for these oligos and tmeplate");
           throw new Exception();
       }
       
       //Check that it's not an IPCR
       if(anneal2.site < anneal1.site) {
           String newstart = template.substring(anneal1.site - anneal1.length);
           String newend = template.substring(0, anneal1.site - anneal1.length);
           this.template = newstart + newend;
           System.out.println("This is an IPCR; handling it");
           return getProduct();
       }
       
       //Construct the output
       int start = anneal1.site;
       int end = anneal2.site;
       String midregion = template.substring(start, end);
       String o2rc = SequenceUtils.reverseComplement(oligo2);
       String out = oligo1 + midregion + o2rc;
       return out;
    }
    
    
    private Anneal getAnneal(String o, String t) {
        String oligo = o.toUpperCase();
        String temp = t.toUpperCase();
        
        Anneal forAnneal = getBestAnneal(oligo, temp, false);
        Anneal revAnneal =  getBestAnneal(oligo, temp, true);
        
        if(forAnneal.length > revAnneal.length) {
            return forAnneal;
        } else {
            return revAnneal;
        }
    }

    private Anneal getBestAnneal(String oligo, String temp, boolean rc) {
        if(rc==true) {
            temp = SequenceUtils.reverseComplement(temp);
        }
        //Tally up all potential initiation sites based on 6bp exact 3' matches
        List<Integer> initSites = new ArrayList<>();
        
        String sixbp = oligo.substring(oligo.length()-6);
        Pattern p = Pattern.compile(sixbp);
        Matcher m = p.matcher(temp);
        while(m.find()) {
            int site = m.end();
            initSites.add(site);
        }
        
        //Iterate through 6bp matches and find the best match with at most 1 bp off
        int bestForScore = -1;
        int bestForSite = -1;
        
        for(Integer end : initSites) {
            //Scan through the region between 6 and 40 behind the match and see how far it goes back
            int matches = 6;
            boolean firstMiss = false;
            boolean secondMiss = false;
            
            for(int x=6; x<25; x++) {
                if(x>oligo.length()) {
                    break;
                }
                if(end-x < 0) {
                    break;
                }
                char oligoChar = oligo.charAt(oligo.length()-x);
                char tempChar = temp.charAt(end-x);
                if(oligoChar == tempChar) {
                    matches++;
                } else {
                    if(firstMiss == false) {
                        firstMiss = true;
                    } else if(secondMiss == false) {
                        secondMiss = true;
                    } else {
                        break;
                    }
                }
            }
            if(matches > bestForScore) {
                bestForScore = matches;
                bestForSite = end;
            }
        }
        
        Anneal out = new Anneal();
        out.site = bestForSite;
        out.length = bestForScore;
        out.rc = rc;
        return out;
    }
    
    private static void test1() throws Exception {
        PCR pcr = new PCR(
                "ggatccTACCGaTCGCCGCAGCCGAACG",
                "tctagaTCGAGTTGCAGGCTTCCT",
                "taccgctcgccgcagccgaacgaccgagcgcagcgagtcaggccgaacgtgagcgaggaagcctgcaactcgagtgaggaggtctc");
        String pdt1 = pcr.getProduct();
        System.out.println("Product is:\n" + pdt1);
        
        pcr.template = SequenceUtils.reverseComplement(pcr.template);
        String pdt2 = pcr.getProduct();
        System.out.println("Product2 is:\n" + pdt2);
        
        String test1 = pdt1.toUpperCase();
        String test2 = SequenceUtils.reverseComplement(pdt2).toUpperCase();
        if(test1.equals(test2)) {
            System.out.println("OK");
        } else {
            System.out.println("!!!!!  wrong");
        }
    }
    
    private static void test2() throws Exception {
        String ca4106 = "ccatacgtctcATTCGGGCTCCCTTTTTTATTTCAgatttctgGAGGAGGGGTCTC";
        String ca4107 = "gactccgtctcTCGAAGGCTCCCTaTCATTTATAATAAatccttagcgaaagctaagg";
        String p20N16 = "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGtGAGGAGGTCTCAccagACTAGTttgacggctagctcagtcctaggtacagtgctagctactagagaaataggagaaatactagATGAGTGTGATCGCTAAACAAATGACCTACAAGGTTTATATGTCAGGCACGGTCAATGGACACTACTTTGAGGTCGAAGGCGATGGAAAAGGTAAGCCCTACGAGGGGGAGCAGACGGTAAAGCTCACTGTCACCAAGGGCGGACCTCTGCCATTTGCTTGGGATATTTTATCACCACAGTGTCAGTACGGAAGCATACCATTCACCAAGTACCCTGAAGACATCCCTGACTATGTAAAGCAGTCATTCCCGGAGGGCTATACATGGGAGAGGATCATGAACTTTGAAGATGGTGCAGTGTGTACTGTCAGCAATGATTCCAGCATCCAAGGCAACTGTTTCATCTACCATGTCAAGTTCTCTGGTTTGAACTTTCCTCCCAATGGACCTGTCATGCAGAAGAAGACACAGGGCTGGGAACCCAACACTGAGCGACTCTTTGCACGAGATGGAATGCTGCTAGGAAACAACTTTATGGCTCTGAAGTTAGAAGGAGGCGGTCACTATTTGTGTGAATTTAAAACTACTTACAAGGCAAAGAAGCCTGTGAAGATGCCAGGGTATCACTATGTTGACCGCAAACTGGATGTAACCAATCACAACAAGGATTACACTTCGGTTGAGCAGTGTGAAATTTCCATTGCACGCAAACCTGTGGTCGCCTAAtaaAAGCTTGGATCCaaataggagaaatactagATGTCTTATTCAAAGCATGGCATCGTACAAGAAATGAAGACGAAATACCATATGGAAGGCAGTGTCAATGGCCATGAATTTACGATCGAAGGTGTAGGAACTGGGTACCCTTACGAAGGGAAACAGATGTCCGAATTAGTGATCATCAAGCCTGCGGGAAAACCCCTTCCATTCTCCTTTGACATACTGTCATCAGTCTTTCAATATGGAAACCGTTGCTTCACAAAGTACCCGGCAGACATGCCTGACTATTTCAAGCAAGCATTCCCAGATGGAATGTCATATGAAAGGTCATTTCTATTTGAGGATGGAGCAGTTGCTACAGCCAGCTGGAACATTCGACTCGAAGGAAATTGCTTCATCCACAAATCCATCTTTCATGGCGTAAACTTTCCCGCTGATGGACCCGTAATGAAAAAGAAGACCATTGACTGGGATAAGTCCTTCGAAAAAATGACTGTGTCTAAAGAGGTGCTAAGAGGTGACGTGACTATGTTTCTTATGCTCGAAGGAGGTGGTTCTCACAGATGCCAATTTCACTCCACTTACAAAACAGAGAAGCCGGTCACACTGCCCCCGAATCATGTCGTAGAACATCAAATTGTGAGGACCGACCTTGGCCAAAGTGCAAAAGGCTTTACAGTCAAGCTGGAAGCACATGCCGCGGCTCATGTTAACCCTTTGAAGGTTAAATAAtaaGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctagtggAAAGCCACGTTGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGAACAATAAAACTGTCTGCTTACATAAACAGTAATACAAGGGGTGTTATGAGCCATATTCAACGGGAAACGTCTTGTTCGAGGCCGCGATTAAATTCCAACATGGACGCTGATTTATATGGGTATAAATGGGCTCGCGATAATGTCGGGCAATCAGGTGCGACAATCTATCGATTGTATGGGAAGCCCGATGCGCCAGAGTTGTTTCTGAAACATGGCAAAGGTAGCGTTGCCAATGATGTTACAGATGAGATGGTCAGACTAAACTGGCTGACGGAATTTATGCCTCTTCCGACCATCAAGCATTTTATCCGTACTCCTGATGATGCATGGTTACTCACCACTGCGATTCCGGGGAAAACAGCATTCCAGGTATTAGAAGAATATCCTGATTCAGGTGAAAATATTGTTGATGCGCTGGCAGTGTTCCTGCGCCGGTTGCATTCGATTCCTGTTTGTAATTGTCCTTTTAACAGCGATCGCGTATTTCGACTCGCTCAGGCGCAATCACGAATGAATAACGGTTTGGTTGATGCGAGTGATTTTGATGACGAGCGTAATGGCTGGCCTGTTGAACAAGTCTGGAAAGAAATGCATAAACTTTTGCCATTCTCACCGGATTCAGTCGTCACTCATGGTGATTTCTCACTTGATAACCTTATTTTTGACGAGGGGAAATTAATAGGTTGTATTGATGTTGGACGAGTCGGAATCGCAGACCGATACCAGGATCTTGCAATCCTATGGAACTGCCTCGGTGAGTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGActgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt";
    
        PCR N79pcr = new PCR(ca4106, ca4107, p20N16);
        String N79pdt = N79pcr.getProduct();
        System.out.println(N79pdt);
        System.out.println(N79pdt.length());
    }
    
    private static void test3() throws Exception {
        String ca4024 = "ccataCGTCTCgCTGActgtcagaccaagtttac";
        String ca4025 = "cagtaCGTCTCaCCACtagaaaaataaacaaataggggttccgcgcac";
        String p20N6 = "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGtGAGGAGGTCTCAccagACTAGTttgacggctagctcagtcctaggtacagtgctagctactagagaaataggagaaatactagATGAGTGTGATCGCTAAACAAATGACCTACAAGGTTTATATGTCAGGCACGGTCAATGGACACTACTTTGAGGTCGAAGGCGATGGAAAAGGTAAGCCCTACGAGGGGGAGCAGACGGTAAAGCTCACTGTCACCAAGGGCGGACCTCTGCCATTTGCTTGGGATATTTTATCACCACAGTGTCAGTACGGAAGCATACCATTCACCAAGTACCCTGAAGACATCCCTGACTATGTAAAGCAGTCATTCCCGGAGGGCTATACATGGGAGAGGATCATGAACTTTGAAGATGGTGCAGTGTGTACTGTCAGCAATGATTCCAGCATCCAAGGCAACTGTTTCATCTACCATGTCAAGTTCTCTGGTTTGAACTTTCCTCCCAATGGACCTGTCATGCAGAAGAAGACACAGGGCTGGGAACCCAACACTGAGCGACTCTTTGCACGAGATGGAATGCTGCTAGGAAACAACTTTATGGCTCTGAAGTTAGAAGGAGGCGGTCACTATTTGTGTGAATTTAAAACTACTTACAAGGCAAAGAAGCCTGTGAAGATGCCAGGGTATCACTATGTTGACCGCAAACTGGATGTAACCAATCACAACAAGGATTACACTTCGGTTGAGCAGTGTGAAATTTCCATTGCACGCAAACCTGTGGTCGCCTAAtaaAAGCTTggatccTCTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt";
    
        PCR N79pcr = new PCR(ca4024, ca4025, p20N6);
        String N10pcr = N79pcr.getProduct();
        System.out.println(N10pcr);
        System.out.println("Expect:  2653");
        System.out.println("Got: " + N10pcr.length());
    }
    
    private static void test4() throws Exception {
//        Oligo ca4024 = OligoManager.getOligo("ca4024");
//        Oligo ca4025 = OligoManager.getOligo("ca4025");
//        String p20N6 = "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGtGAGGAGGTCTCAccagACTAGTttgacggctagctcagtcctaggtacagtgctagctactagagaaataggagaaatactagATGAGTGTGATCGCTAAACAAATGACCTACAAGGTTTATATGTCAGGCACGGTCAATGGACACTACTTTGAGGTCGAAGGCGATGGAAAAGGTAAGCCCTACGAGGGGGAGCAGACGGTAAAGCTCACTGTCACCAAGGGCGGACCTCTGCCATTTGCTTGGGATATTTTATCACCACAGTGTCAGTACGGAAGCATACCATTCACCAAGTACCCTGAAGACATCCCTGACTATGTAAAGCAGTCATTCCCGGAGGGCTATACATGGGAGAGGATCATGAACTTTGAAGATGGTGCAGTGTGTACTGTCAGCAATGATTCCAGCATCCAAGGCAACTGTTTCATCTACCATGTCAAGTTCTCTGGTTTGAACTTTCCTCCCAATGGACCTGTCATGCAGAAGAAGACACAGGGCTGGGAACCCAACACTGAGCGACTCTTTGCACGAGATGGAATGCTGCTAGGAAACAACTTTATGGCTCTGAAGTTAGAAGGAGGCGGTCACTATTTGTGTGAATTTAAAACTACTTACAAGGCAAAGAAGCCTGTGAAGATGCCAGGGTATCACTATGTTGACCGCAAACTGGATGTAACCAATCACAACAAGGATTACACTTCGGTTGAGCAGTGTGAAATTTCCATTGCACGCAAACCTGTGGTCGCCTAAtaaAAGCTTggatccTCTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt";
//    
//        PCR N79pcr = new PCR(ca4024.sequence, ca4025.sequence, p20N6);
//        String N10pcr = N79pcr.getProduct();
//        System.out.println(N10pcr);
//        System.out.println("Expect:  2653");
//        System.out.println("Got: " + N10pcr.length());
    }
    
    public static void main(String[] args) throws Exception {
        String oligo1 = "gactccgtctcTtcagtcctaggtacagtgctagcAGGATCCaaataggag";
        String oligo2 = "ccatacgtctcActgagctagctgtcaaCGCTCTGAGGTATTATTCGC";
        String template = "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGtGAGGAGGTCTCAccctACTAGACCATAATCATAAGGGGCTTCGGCCCCTTTCTTCATTTTGAGCGAATAATACCTCAGAGCGtttacagctagctcagtcctaggtattatgctagcAGGATCCaaataggagaaatactagATGTCTTATTCAAAGCATGGCATCGTACAAGAAATGAAGACGAAATACCATATGGAAGGCAGTGTCAATGGCCATGAATTTACGATCGAAGGTGTAGGAACTGGGTACCCTTACGAAGGGAAACAGATGTCCGAATTAGTGATCATCAAGCCTGCGGGAAAACCCCTTCCATTCTCCTTTGACATACTGTCATCAGTCTTTCAATATGGAAACCGTTGCTTCACAAAGTACCCGGCAGACATGCCTGACTATTTCAAGCAAGCATTCCCAGATGGAATGTCATATGAAAGGTCATTTCTATTTGAGGATGGAGCAGTTGCTACAGCCAGCTGGAACATTCGACTCGAAGGAAATTGCTTCATCCACAAATCCATCTTTCATGGCGTAAACTTTCCCGCTGATGGACCCGTAATGAAAAAGAAGACCATTGACTGGGATAAGTCCTTCGAAAAAATGACTGTGTCTAAAGAGGTGCTAAGAGGTGACGTGACTATGTTTCTTATGCTCGAAGGAGGTGGTTCTCACAGATGCCAATTTCACTCCACTTACAAAACAGAGAAGCCGGTCACACTGCCCCCGAATCATGTCGTAGAACATCAAATTGTGAGGACCGACCTTGGCCAAAGTGCAAAAGGCTTTACAGTCAAGCTGGAAGCACATGCCGCGGCTCATGTTAACCCTTTGAAGGTTAAATAAtaaGAATTCagcaAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctagtggAAAGCCACGTTGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGAACAATAAAACTGTCTGCTTACATAAACAGTAATACAAGGGGTGTTATGAGCCATATTCAACGGGAAACGTCTTGTTCGAGGCCGCGATTAAATTCCAACATGGACGCTGATTTATATGGGTATAAATGGGCTCGCGATAATGTCGGGCAATCAGGTGCGACAATCTATCGATTGTATGGGAAGCCCGATGCGCCAGAGTTGTTTCTGAAACATGGCAAAGGTAGCGTTGCCAATGATGTTACAGATGAGATGGTCAGACTAAACTGGCTGACGGAATTTATGCCTCTTCCGACCATCAAGCATTTTATCCGTACTCCTGATGATGCATGGTTACTCACCACTGCGATTCCGGGGAAAACAGCATTCCAGGTATTAGAAGAATATCCTGATTCAGGTGAAAATATTGTTGATGCGCTGGCAGTGTTCCTGCGCCGGTTGCATTCGATTCCTGTTTGTAATTGTCCTTTTAACAGCGATCGCGTATTTCGACTCGCTCAGGCGCAATCACGAATGAATAACGGTTTGGTTGATGCGAGTGATTTTGATGACGAGCGTAATGGCTGGCCTGTTGAACAAGTCTGGAAAGAAATGCATAAACTTTTGCCATTCTCACCGGATTCAGTCGTCACTCATGGTGATTTCTCACTTGATAACCTTATTTTTGACGAGGGGAAATTAATAGGTTGTATTGATGTTGGACGAGTCGGAATCGCAGACCGATACCAGGATCTTGCAATCCTATGGAACTGCCTCGGTGAGTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGActgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt";
    
        PCR N79pcr = new PCR(oligo1, oligo2, template);
        String N79pdt = N79pcr.getProduct();
        System.out.println(N79pdt);
        System.out.println(N79pdt.length());
    }
}
