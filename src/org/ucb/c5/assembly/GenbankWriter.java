/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ucb.c5.assembly.model.Annotation;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class GenbankWriter {
    
    /**
     * 
     * @param sequence  The DNA sequence of a plasmid
     * @param annotations Map from feature label to start and end positions
     * @param filepath  Where to write the genbank file
     * @throws Exception 
     */
    public String run(String sequence, List<Annotation> annotations, String filepath) throws Exception {
        String seqportion = breakUpSequence(sequence);
        String annotportion = serializeAnnotations(annotations);
        
        StringBuilder sb = new StringBuilder();
        sb.append("LOCUS       New_DNA                 ");
        sb.append(sequence.length());
        sb.append(" bp    DNA        circular     19-MAR-2020\n");
        sb.append("DEFINITION  .\nACCESSION   \nVERSION     \nSOURCE      .\n" +
"  ORGANISM  .\nCOMMENT     \nCOMMENT     ApEinfo:methylated:1\nFEATURES             Location/Qualifiers\n");
        sb.append(annotportion);
        sb.append(seqportion);
        
        String output = sb.toString();
        FileUtils.writeFile(output, filepath);
        
        return output;
    }


    private String breakUpSequence(String sequence) {
        int rem = sequence.length() % 60;
        for(int i=0; i< 60-rem; i++) {
            sequence+='x';
        }
        
        StringBuilder out = new StringBuilder();
        for(int i=0; i<sequence.length(); i=i+60) {
           //Add a new line
           out.append("\n");
            
           //Put in spaces and number
           String numstart = "" + (i+1);
           int numspaces = 9 - numstart.length();
           for(int k = 0; k< numspaces; k++) {
               out.append(" ");
           } 
           out.append(numstart);

           //Put in the line of sequences
           for(int j=0; j<6; j++) {
               out.append(" ");
               String tenmer = sequence.substring(i+j*10, i+j*10+10);
               out.append(tenmer);
           }
        }
        
        //Clean it up and return
        String seq = out.toString();
        seq = seq.replaceAll("x", "");
        seq = "ORIGIN" + seq;
        seq = seq.trim();
        return seq;
    }
    
    /*
     misc_feature    2232..2251
                     /locus_tag="Region H"
                     /label="Region H"
                     /ApEinfo_label="Region H"
                     /ApEinfo_fwdcolor="pink"
                     /ApEinfo_revcolor="pink"
                     /ApEinfo_graphicformat="arrow_data {{0 1 2 0 0 -1} {} 0}
                     width 5 offset 0"
    */
    private String serializeAnnotations(List<Annotation> annotations) {
        StringBuilder out = new StringBuilder();
        for(Annotation annot : annotations) {
            
            if(annot.isRevComp()) {
                out.append("     misc_feature    complement(");
            } else {
                out.append("     misc_feature    ");
            }
            out.append(annot.getStart());
            out.append("..");
            out.append(annot.getEnd());
            if(annot.isRevComp()) {
                out.append("}");
            }
            out.append("\n                     /locus_tag=\"");
            out.append(annot.getName());
            out.append("\"\n                     /label=\"");
            out.append(annot.getName());
            out.append("\"\n                     /ApEinfo_label=\"");
            out.append(annot.getName());
            out.append("\"\n                     /ApEinfo_fwdcolor=\"");
            out.append(annot.getColor());
            out.append("\"\n                     /ApEinfo_revcolor=\"");
            out.append(annot.getColor());
            out.append("\"\n                     /ApEinfo_graphicformat=\"arrow_data {{0 1 2 0 0 -1} {} 0}\n                     width 5 offset 0\"");
            out.append("\n");
        }
        
        return out.toString();
    }
    
    public static void main(String[] args) throws Exception {
        String seq = "cctactatggaactgcctcggtgagTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGAACTCtttacaaatctaaaatgtttgtgactgtattataAgaacagaaatcccccttacacggaggcatcagtgaccaaacaggaaaaaaccgcccttaacatggcccgctttatcagaagccagacattaacgcttctggagaaactcaacgagctggacgcggatgaacaggcagacatctgtgaatcgcttcacgaccacgctgatgagctttaccgcagctgcctcgcgcgtttcggtgatgacggtgaaaacctctgacacatgcagcTCCCagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccataggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaaggacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccTTCGattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaatgtctgccacgtatcgccagatgttcCAGACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACATTTTTGCTTCCGTGTGGTATTATGGGAGCAGTAGGTCTACGGTTAACTGATACTAAAAGACAATTCAGCGGGTAACCTTGCAATGGTGAGTGGCAGTAAAGCGGGCGTTTCGCCTCATCGCGAAATAGAAGTAATGAGACAATCCATTGACGATCACCTGGCTGGCCTGTTACCTGAAACCGACAGCCAGGATATCGTCAGCCTTGCGATGCGTGAAGGCGTCATGGCACCCGGTAAACGGATCCGTCCGCTGCTGATGCTGCTGGCCGCCCGCGACCTCCGCTACCAGGGCAGTATGCCTACGCTGCTCGATCTCGCCTGCGCCGTTGAACTGACCCATACCGCGTCGCTGATGCTCGACGACATGCCCTGCATGGACAACGCCGAGCTGCGCCGCGGTCAGCCCACTACCCACAAAAAATTTGGTGAGAGCGTGGCGATCCTTGCCTCCGTTGGGCTGCTCTCTAAAGCCTTTGGTCTGATCGCCGCCACCGGCGATCTGCCGGGGGAGAGGCGTGCCCAGGCGGTCAACGAGCTCTCTACCGCCGTGGGCGTGCAGGGCCTGGTACTGGGGCAGTTTCGCGATCTTAACGATGCCGCCCTCGACCGTACCCCTGACGCTATCCTCAGCACCAACCACCTCAAGACCGGCATTCTGTTCAGCGCGATGCTGCAGATCGTCGCCATTGCTTCCGCCTCGTCGCCGAGCACGCGAGAGACGCTGCacgccttcgccctcgacttcggccaggcgtttcaactgctggacgatctgcgtgacgatcacccggaaaccggtaaagatcgcaataaggacgcgggaaaatcgacgctggtcaaccggctgggcgcagacgcggcccggcaaaagctgcgcgagcatattgattccgccgacaaacacctcacttttgcctgtccgcagggcggcgccatccgacagtttatgcatctgtggtttggccatcaccttgccgactggtcaccggtcatgaaaatcgcctgataccgcccttttgggttcaagcagtacataacgatggaaccacattacaggagtagtgatgaatgaaggacgagcgccttgttcagcgtaagaacgatcatctggatatcgttctcgacccccgtcgcgccgtaactcaggctagcgcaggttttgagcgctggcgctttacccactgcgccctgccagagctgaattttagcgacatcacgctggaaaccaccttcctgaatcggcagctacaggctccgctgctgatcagctccatgaccggcggcgttgagcgctcgcgccatatcaaccgccacctcgccgaggcggcgcaggtgctaaaaattgcgatgggggtgggctcccagcgcgtcgccattgagagcgacgcgggcttagggctggataaaaccctgcggcagctggctccggacgtgccgctgctggcgaacctcggcgcggcgcagctgaccggcagaaaaggtattgattacgcccgacgggccgtggagatgatcgaggcggatgcgctgattgtgcacctaaacccgctgcaggaggcgctacagcccggcggcgatcgcgactggcgcggacggctggcggctattgaaactctggtccgcgagctgcccgttccgctggtggtgaaagaggtgggagccggtatctcccgaaccgtggccgggcagctgatcgatgccggcgttaccgtgattgacgtcgcgggcgcgggcggcaccagctgggccgccgttgaaggcgagcgggcggccaccgagcagcagcgcagcgtggccaacgtctttgccgactgggggatccccaccgctgaggcgctggttgacattgccgaggcctggccgcagatgccccttattgcctcgggaatgcgggctgggctatttcaccctaccactggctattcgctgccgctggcggtggcccttgccgacgcgattgccgacagcccgcggctgggcagcgttccgctctatcagctcacccggcagtttgccgaacgccactggcgcaggcagggattcttccgcctgctgaaccggatgcttttcctggccgggcgcgaggagaaccgctggcgggtgatgcagcgcttttatgggctgccggagcccaccgtagagcgcttttacgccggtcggctctctctctttgataaggcccgcattttgacgggcaagccaccggttccgctgggcgaagcctggcgggcggcgctgaaccattttcctgacagacgagataaaggatgaaaaaaaccgttgtgattggcgcaggctttggtggcctggcgctggcgattcgcctgcaggcggcagggatcccaaccgtactgctggagcagcgggacaagcccggcggtcgggcctacgtctggcatgaccagggctttacctttgacgccgggccgacggtgatcaccgatcctaccgcgcttgaggcgctgttcaccctggccggcaggcgcatggaggattacgtcaggctgctgccggtaaaacccttctaccgactctgctgggagtccgggaagaccctcgactatgctaacgacagcgccgagcttgaggcgcagattacccagttcaacccccgcgacgtcgagggctaccggcgctttctggcttactcccaggcggtattccaggagggatatttgcgcctcggcagcgtgccgttcctctcttttcgcgacatgctgcgcgccgggccgcagctgcttaagctccaggcgtggcagagcgtctaccagtcggtttcgcgctttattgaggatgagcatctgcggcaggccttctcgttccactccctgctggtaggcggcaaccccttcaccacctcgtccatctacaccctgatccacgcccttgagcgggagtggggggtctggttccctgagggcggcaccggggcgctggtgaacggcatggtgaagctgtttaccgatctgggcggggagatcgaactcaacgcccgggtcgaagagctggtggtggccgataaccgcgtaagccaggtccggctggcggatggtcggatctttgacaccgacgccgtagcctcgaacgctgacgtggtgaacacctataaaaagctgctcggccaccatccggtggggcagaagcgggcggcagcgctggagcgcaagagcatgagcaactcgctgtttgtgctctacttcggcctgaaccagcctcattcccagctggcgcaccataccatctgttttggtccccgctaccgggagctgatcgacgagatctttaccggcagcgcgctggcggatgacttctcgctctacctgcactcgccctgcgtgaccgatccctcgctcgcgcctcccggctgcgccagcttctacgtgctggccccggtgccgcatcttggcaacgcgccgctggactgggcgcaggaggggccgaagctgcgcgaccgcatctttgactaccttgaagagcgctatatgcccggcctgcgtagccagctggtgacccagcggatctttaccccggcagacttccacgacacgctggatgcgcatctgggatcggccttctccatcgagccgctgctgacccaaagcgcctggttccgcccgcacaaccgcgacagcgacattgccaacctctacctggtgggcgcaggtactcaccctggggcgggcattcctggcgtagtggcctcggcgaaagccaccgccagcctgatgattgaggatctgcaatgagccaaccgccgctgcttgaccacgccacgcagaccatggccaacggctcgaaaagttttgccaccgctgcgaagctgttcgacccggccacccgccgtagcgtgctgatgctctacacctggtgccgccactgcgatgacgtcattgacgaccagacccacggcttcgccagcgaggccgcggcggaggaggaggccacccagcgcctggcccggctgcgcacgctgaccctggcggcgtttgaaggggccgagatgcaggatccggccttcgctgcctttcaggaggtggcgctgacccacggtattacgccccgcatggcgctcgatcacctcgacggctttgcgatggacgtggctcagacccgctatgtcacctttgaggatacgctgcgctactgctatcacgtggcgggcgtggtgggtctgatgatggccagggtgatgggcgtgcgggatgagcgggtgctggatcgcgcctgcgatctggggctggccttccagctgacgaatatcgcccgggatattattgacgatgcggctattgaccgctgctatctgcccgccgagtggctgcaggatgccgggctgaccccggagaactatgccgcgcgggagaatcgggccgcgctggcgcgggtggcggagcggcttattgatgccgcagagccgtactacatctcctcccaggccgggctacacgatctgccgccgcgctgcgcctgggcgatcgccaccgcccgcagcgtctaccgggagatcggtattaaggtaaaagcggcgggaggcagcgcctgggatcgccgccagcacaccagcaaaggtgaaaaaattgccatgctgatggcggcaccggggcaggttattcgggcgaagacgacgagggtgacgccgcgtccggccggtctttggcagcgtcccgtttaggcgggcggccatgacgttcacgcaggatcagtcgcctgtaggtcggcaggcttgggaagctgtggtatggctgtgcaggtcgtaaatcactgcataattcgtgtcgctcaaggcgcactcccgttctggataatgttttttgcgccgacatcataacggttctggcaaatattctgaaatgagctgttgacaattaatcatccggctcgtataatgtgtggaattgtgagcggataacaatttcacacaggaaacagcgccgctgagaaaaagcgaagcggcactgctctttaacaatttatcagacaatctgtgtgggcactcgaccggaattatcgattaactttattattaaaaattaaagaggtatatattaatgtatcgattaaataaggaggaataaaccatgtcgagatctgcagctggtaccgctatgacagatactaaagatgctggtatggatgctgttcagagacgtctcatgtttgaggatgaatgcattcttgttgatgaaactgatcgtgttgtggggcatgacagcaagtataattgtcatctgatggaaaatattgaagccaagaatttgctgcacagggcttttagtgtatttttattcaactcgaagtatgagttgcttctccagcaaaggtcaaacacaaaggttacgttccctctagtgtggactaacacttgttgcagccatcctctttaccgtgaatcagagcttatccaggacaatgcactaggtgtgaggaatgctgcacaaagaaagcttctcgatgagcttggtattgtagctgaagatgtaccagtcgatgagttcactcccttgggacgtatgctgtacaaggctccttctgatggcaaatggggagagcatgaacttgattacttgctcttcatcgtgcgagacgtgaaggttcaaccaaacccagatgaagtagctgagatcaagtatgtgagccgggaagagctgaaggagctggtgaagaaagcagatgcaggtgaggaaggtttgaaactgtcaccatggttcagattggtggtggacaatttcttgatgaagtggtgggatcatgttgagaaaggaactttggttgaagctatagacatgaaaaccatccacaaactctgaacatctttttttaaagtttttaaatcaatcaactttctcttcatcatttttatcttttcgatgataataatttgggatatgtgagacacttacaaaacttccaaggtctgcggggcaaaacaatcgataaatcagcccgggaattaacatggcaaccactcatttggatgtttgcgccgtggttccggcggccggatttggccgtcgaatgcaaacggaatgtcctaagcaatatctctcaatcggtaatcaaaccattcttgaacactcggtgcatgcgctgctggcgcatccccgggtgaaacgtgtcgtcattgccataagtcctggcgatagccgttttgcacaacttcctctggcgaatcatccgcaaatcaccgttgtagatggcggtgatgagcgtgccgattccgtgctggcaggtctgaaagccgctggcgacgcgcagtgggtattggtgcatgacgccgctcgtccttgtttgcatcaggatgacctcgcgcgattgttggcgttgagcgaaaccagccgcacgggggggatcctcgccgcaccagtgcgcgatactatgaaacgtgccgaaccgggcaaaaatgccattgctcataccgttgatcgcaacggcttatggcacgcgctgacgccgcaatttttccctcgtgagctgttacatgactgtctgacgcgcgctctaaatgaaggcgcgactattaccgacgaagcctcggcgctggaatattgcggattccatcctcagttggtcgaaggccgtgcggataacattaaagtcacgcgcccggaagatttggcactggccgagttttacctcacccgaaccatccatcaggagaatacataatgcgaattggacacggttttgacgtacatgcctttggcggtgaaggcccaattatcattggtggcgtacgcattccttacgaaaaaggattgctggcgcattctgatggcgacgtggcgctccatgcgttgaccgatgcattgcttggcgcggcggcgctgggggatatcggcaagctgttcccggataccgatccggcatttaaaggtgccgatagccgcgagctgctacgcgaagcctggcgtcgtattcaggcgaagggttatacccttggcaacgtcgatgtcactatcatcgctcaggcaccgaagatgttgccgcacattccacaaatgcgcgtgtttattgccgaagatctcggctgccatatggatgatgttaacgtgaaagccactactacggaaaaactgggatttaccggacgtggggaagggattgcctgtgaagcggtggcgctactcattaaggcaacaaaatgattgagtttgataatctcacttacctccacggtaaaccgcacctcaggcaataataaagtttgcggccgcgaattcctgcagcccgggggatccactagttctagagcggccgccaccgcggtggagctccagcttttgttccctttagtgagggttaatttcgagcttggcgtaatcatggtcatagctgtttcctgtgtggtggtagatcctctacgccggacgcatcgtggccggcatcaccggcgccacaggtgcggttgctggcgcctatatcgccgacatcacccagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaatttttttaaggcagttattggtg";
//        String seq = "cctactatggaactgcctcggtgagTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAA";
        List<Annotation> annot = new ArrayList<>();
        annot.add(new Annotation("Ptrc promoter", "#0080c0", 6194, 6229, false ));
        annot.add(new Annotation("Arabidopsis thaliana isopentenyl pyrophosphate:dimethylallyl pyrophosphate isomerase", "#ff8040", 6414, 7142, false));
        annot.add(new Annotation("Bla", "#ff5261", 9010, 9870, false));

        GenbankWriter writer = new GenbankWriter();
        String data = writer.run(seq, annot, "testseq.seq");
        
        System.out.println(data);
    }


}
