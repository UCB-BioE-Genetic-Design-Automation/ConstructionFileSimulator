/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.ucb.c5.constructionfile.model.Modifications;
/**
 *
 * @author michaelfernandez
 */
public class ParseOligo {
    //The purpose of this class is to parse out an example modification example
    //take in a sample string such as '/mod/ACGTGTGTG
    
    public  Polynucleotide run(String oligo) throws Exception{

        Modifications mod_= Modifications.hydroxyl;
        
        if(!oligo.contains("/")) {
            Polynucleotide poly_new = new Polynucleotide(oligo,"","", false, false, false, mod_, mod_);
            return poly_new;
        }
        
        //Split string and mod
        //use split on string 
        String[] mod_seq = oligo.split("/");
        
        String mod = mod_seq[1];
        String seq = mod_seq[2];
        
        
        //check for illegal sequence parts if there are then send exception
        if(!seq.matches("[ACGTRYSWKMBDHVNacgtryswkmbdhvn]+")) {
                    throw new Exception("Oligo file has non-DNA sequence:\n" + seq);
                }
        
        //run through the sequence and convert the modifications to proper 
        switch (mod) {
            case "5Phos":
                mod_ = Modifications.phos5;
                break;
            case "5AmMC6":
                mod_= Modifications.AmMC6;
                break;
            case "5AmMC12":
                mod_= Modifications.AmMC12;
                break;
            case "5AmMC6T":
                mod_= Modifications.AmMC6T;
                break;
            case "5UniAmM":
                mod_= Modifications.UniAmM;
                break;
            case "5Biosg":
                mod_= Modifications.Biosg;
                break;
            case "5BioK":
                mod_= Modifications.BioK;
                break;
            case "5BiodT":
                mod_= Modifications.BiodT;
                break;
            case "5BiotinTEG":
                mod_= Modifications.BiotinTEG;
                break;
            case "52-Bio":
                mod_= Modifications.Dual_BT52;
                break;
            case "5PCBio":
                mod_= Modifications.PCBT;
                break;
            case "5deSBioTEG":
                mod_= Modifications.deSBIOTEG;
                break;
            case "5DTPA":
                mod_= Modifications.DTPA;
                break;
            case "5ThioMC6-D":
                mod_= Modifications.ThioMC6_D;
                break;
            case "5Hexynyl":
                mod_= Modifications.Hexynyl;
                break;
            case "55OctdU":
                mod_= Modifications.OctdU;
                break;
            case "5Acryd":
                mod_= Modifications.Acryd;
                break;
            case "5rApp":
                mod_= Modifications.rApp;
                break;
            case "5AzideN":
                mod_= Modifications.AzideN;
                break;
            case "5DigN":
                mod_= Modifications.DigN;
                break;
            case "5ILink12":
                mod_= Modifications.ILink12;
                break;
            case "56-FAMN":
                mod_= Modifications.FAMN_56;
                break;
            case "56-FAM":
                mod_= Modifications.FAM_56;
                break;
            case "5FluorT":
                mod_= Modifications.FluorT;
                break;
            case "5Cy3":
                mod_= Modifications.CY3;
                break;
            case "56-JOEN":
                mod_= Modifications.Joen_56;
                break;
            case "5Cy5":
                mod_= Modifications.CY5;
                break;
            case "56-TAMN":
                mod_= Modifications.TAMN;
                break;
            case "5MAXN":
                mod_= Modifications.Maxn;
                break;
            case "5TET":
                mod_= Modifications.TET;
                break;
            case "5Cy55":
                mod_= Modifications.CY55;
                break;
            case "56-ROXN":
                mod_= Modifications.ROXN_56;
                break;
            case "5TYE563":
                mod_= Modifications.TYE563;
                break;
            case "5YakYel":
                mod_= Modifications.YakYel;
                break;
            case "5HEX":
                mod_= Modifications.HEX1;
                break;
            case "5TEX615":
                mod_= Modifications.TEX615;
                break;
            case "5TYE665":
                mod_= Modifications.TYE665;
                break;
            case "5TYE705":
                mod_= Modifications.TYE705;
                break;
            case "5SUN":
                mod_= Modifications.SUN;
                break;
            case "5ATTO488N":
                mod_= Modifications.ATTO488N;
                break;
            case "5ATTO532N":
                mod_= Modifications.ATTO532N;
                break;
            case "5ATTO550N":
                mod_= Modifications.ATTO55ON;
                break;
            case "5ATTO565N":
                mod_= Modifications.ATTO565N;
                break;
            case "5RHO101N":
                mod_= Modifications.ATTO101N;
                break;
            case "5ATTO590N":
                mod_= Modifications.ATTO590N;
                break;
            case "5ATTO633N":
                mod_= Modifications.ATTO633N;
                break;
            case "5ATTO647NN":
                mod_= Modifications.ATTO647NN;
                break;
            case "5Alex488N":
                mod_= Modifications.Alex488N;
                break;
            case "5Alex532N":
                mod_= Modifications.Alex532N;
                break;
            case "5Alex546N":
                mod_= Modifications.Alex456N;
                break;
            case "5Alex594N":
                mod_= Modifications.Alex594N;
                break;
            case "5Alex647N":
                mod_= Modifications.Alex647N;
                break;
            case "5Alex660N":
                mod_= Modifications.Alex660N;
                break;
            case "5Alex750N":
                mod_= Modifications.Alex750N;
                break;
            case "5IRD700":
                mod_= Modifications.IRD700;
                break;
            case "5IRD800":
                mod_= Modifications.IRD800;
                break;
            case "5IRD800CWN":
                mod_= Modifications.IRD800CWN;
                break;
            case "5RhoG-XN":
                mod_= Modifications.RhoG_XN;
                break;
            case "5RhoR-XN":
                mod_= Modifications.RhoR_XN;
                break;
            case "55-TAMK":
                mod_= Modifications.TAMK;
                break;
            case "56-FAMK":
                mod_= Modifications.FAMK_6;
                break;
            case "5TexRd-XN":
                mod_= Modifications.TexRd_XN;
                break;
            case "5LtC640N":
                mod_= Modifications.LtC640N;
                break;
            case "5Dy750N":
                mod_= Modifications.Dy750N;
                break;
            case "5IABkFQ":
                mod_= Modifications.IABKFQ;
                break;
            case "5IAbRQ":
                mod_= Modifications.IAbRQ;
                break;
            case "5SpC3":
                mod_= Modifications.SpC3;
                break;
            case "5dSp":
                mod_= Modifications.dSp;
                break;
            case "5SpPC":
                mod_= Modifications.SpPC;
                break;
            case "5Sp9":
                mod_= Modifications.Sp9;
                break;
            case "5Sp18":
                mod_= Modifications.Sp18;
                break;
            case "52AmPr":
                mod_= Modifications.AmPr;
                break;
            case "55Br-dU":
                mod_= Modifications.Br_dU;
                break;
            case "5deoxyU":
                mod_= Modifications.deoxyU;
                break;
            case "5AmdA":
                mod_= Modifications.Amda;
                break;
            case "5deoxyI":
                mod_= Modifications.deoxyI;
                break;
            case "55HydMe-dC":
                mod_= Modifications.HydMe_dc;
                break;
            case "5isodG":
                mod_= Modifications.isodG;
                break;
            case "5Me-isodC":
                mod_= Modifications.Me_isodC;
                break;
            case "5InvddT":
                mod_= Modifications.InvddT;
                break;
            case "5Me-dC":
                mod_= Modifications.Me_dc;
                break;
            case "55NitInd":
                mod_ = Modifications.Nitlnd;
                break;
            case "5Super-dT":
                mod_= Modifications.Super_dt;
                break;
            case "/5Super-dG":
                mod_= Modifications.Super_dg;
                break;  
            default: 
                throw new Exception("The given mod is not found in Modifications.java");
        }
        
        //(String sequence, String ext5, String ext3, boolean isDoubleStranded, boolean isRNA, boolean isCircular, Modifications mod_ext3, Modifications mod_ext5)
        //Join up the seq and mod then create polynucleotide object to output
        
        Polynucleotide poly_mod_new = new Polynucleotide(seq,"","", false, false, false, mod_, mod_);
        
        return poly_mod_new;
    }
    

    public static void main(String[] args) throws Exception {
        
        String polyM = ("/5AmMC6/CTAGTttgacggctagcG");
        String polyp = ("/5Phos/CTAGTttgacggcta");
        
        //With modification
        ParseOligo po1 = new ParseOligo();
        Polynucleotide result1 = po1.run(polyM);
 
        //Without modification
        ParseOligo po2 = new ParseOligo();
        Polynucleotide result2 = po2.run(polyp);
        
        
        //5'-pCTAGTttgacggctagcG-3'
        
        //5'-/5AmMC6/CTAGTttgacggctagcG"
        
        //Question. maybe here check if it is double stranded?
        //Assume that this is oligos, but what of double stranded phosphoralytion
        
        
        //System.out.println(result2.mod_ext3());
        
        System.out.println(result1.toString());
        System.out.println(result2.toString());
    
}
}

/**
 *
 * @author michaelfernandez
 * 
 */

