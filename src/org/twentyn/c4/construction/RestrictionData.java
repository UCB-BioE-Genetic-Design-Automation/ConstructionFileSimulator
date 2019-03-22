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

/**
 *
 * @author jca20n
 */
public class RestrictionData {
    public int cut5;
    public int cut3;
    public String site;
    
    public static RestrictionData getFor(RestrictionEnzyme enz) {
        RestrictionData out = new RestrictionData();
        switch(enz) {
            case BamHI:
                out.site = "GGATCC";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case BglII:
                out.site = "AGATCT";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case EcoRI:
                out.site = "GAATTC";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case MfeI:
                out.site = "CAATTG";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case SpeI:
                out.site = "ACTAGT";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case XbaI:
                out.site = "TCTAGA";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case XhoI:
                out.site = "CTCGAG";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case HindIII:
                out.site = "TCTAGA";
                out.cut5 = 1;
                out.cut3 = 5;
                break;
            case BsaI:
                out.site = "GGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]";
                out.cut5 = 7;
                out.cut3 = 11;
                break;
            case BseRI:
                out.site = "GAGGAG[ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG][ATCG]";
                out.cut5 = 14;
                out.cut3 = 16;
                break;
            case BsmBI:
                out.site = "CGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]";
                out.cut5 = 7;
                out.cut3 = 11;
                break;
        }
        return out;
    }
}
