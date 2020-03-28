/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.genbank.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *     promoter        9483..9518
                     /locus_tag="Ptrc promoter"
                     /label="Ptrc promoter"
                     /ApEinfo_label="Ptrc promoter"
                     /ApEinfo_fwdcolor="#0080c0"
                     /ApEinfo_revcolor="#0080c0"
                     /ApEinfo_graphicformat="arrow_data {{0 1 2 0 0 -1} {} 0}
                     width 5 offset 0"
                     * 
 * @author J. Christopher Anderson
 */
public class Annotation {
    private final String name;
    private final String color;
    private final int start;
    private final int end;
    private final boolean isRevComp;

    public Annotation(String name, String color, int start, int end, boolean isRevComp) {
        this.name = name;
        this.color = color;
        this.start = start;
        this.end = end;
        this.isRevComp = isRevComp;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isRevComp() {
        return isRevComp;
    }
}
