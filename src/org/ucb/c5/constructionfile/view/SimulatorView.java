package org.ucb.c5.constructionfile.view;

import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.SimulateConstructionFile;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Polynucleotide;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import static javax.swing.text.StyleConstants.TabSet;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 *
 * @author jcaucb
 */
public class SimulatorView extends javax.swing.JFrame {

    /**
     * Creates new form SimulatorView
     */
    public SimulatorView() {
        initComponents();

        int w = cfPane.getFontMetrics(cfPane.getFont()).charWidth(' ');
        TabStop[] stops = {new TabStop(w * 40)};
        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setTabSet(attrs, new TabSet(stops));
        cfPane.setParagraphAttributes(attrs, false);

        cfPane.setText(
////Gibson Assembly 1                
//                ">Construction of pTarg2\n" +
//                "acquire oligo targAf\n" +
//                "acquire oligo targAr\n" +
//                "acquire oligo targBf\n" +
//                "acquire oligo targBr\n" +
//                "acquire plasmid p20N5\n" +
//                "acquire plasmid pTargetF\n" +
//                "PCR targAf,targAr on p20N5\t(1200 bp, pcrA)\n" +
//                "PCR targBf,targBr on pTargetF\t(1172 bp, pcrB)\n" +
//                "Assemble pcrA,pcrB\t(Gibson, pTarg2)\n" +
//                "Transform pTarg2\t(Mach1, Amp)\n" +
//                "\n" +
//                ">targAf\n" +
//                "GAGTTCATGTGCAGCTCCATAAGCTGAAATTCTGCCTCGTGATAC\n" +
//                ">targAr\n" +
//                "GTTAAGGGATTTTGGTCATGAGATTATCAAAAAGGATCTTC\n" +
//                ">targBf\n" +
//                "GAAGATCCTTTTTGATAATCTCATGACCAAAATCCCTTAAC\n" +
//                ">targBr\n" +
//                "GTATCACGAGGCAGAATTTCAGCTTATGGAGCTGCACATGAACTC\n" +
//                ">p20N5\n" +
//                "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGcgatcagatACTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n"
////    
                
//////Wobble（failed)
//                ">Construction of Ala2\n" +
//                "acquire oligo ca9939\n" +
//                "acquire oligo ca9940\n" +
//                "acquire plasmid pBca9145-Bca1144#5\n" +
//                "Wobble ca9939/ca9940           (107bp, wobpdt)\n" +
//                "Digest wobpdt                  (EcoRI/BamHI, L, wobdig)\n" +
//                "Digest pBca9145-Bca1144#5      (EcoRI/BamHI, 2057+910, L, vectdig)\n" +
//                "Ligate wobdig + vectdig        (pBca9145-Bca9939)\n" +
//                "Transform pBca9145-Bca9939     (Mach1, Amp) \n" +
//                " >ca9939   Forward construction of Ala2 basic part\n" +
//                " CCATAgaattcATGagatctGGGGCTATAGCTCAGCTGGGAGAGCGCCTGCTTCTAACGCAG\n" +
//                " >ca9940   Reverse construction of Ala2 basic part\n" +
//                " CTGATggatccTGGTGGAGCTATGCGGGATCGAACCGCAGACCTCCTGCGTTAGAAGCAGGCGCTC\n" +
//                " >pBca9145-Bca1144#5\n" +
//                "gaattcatgAGATCTtccctatcagtgatagagattgacatccctatcagtgatagagatactgagcacGGATCTGAAAGAGGAGAAAGGATCTatggcgagtagcgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaataaGGATCTccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttataGGATCCtaaCTCGAGctgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatacggttatccacagaatcaggggataacgcaggaaagaacatgtgagcaaaaggccagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccacaggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaaggacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccttttaaattaaaaatgaagttttaaatcaatctaaagtatatatgagtaaacttggtctgacagttaccaatgcttaatcagtgaggcacctatctcagcgatctgtctatttcgttcatccatagttgcctgactccccgtcgtgtagataactacgatacgggagggcttaccatctggccccagtgctgcaatgataccgcgagacccacgctcaccggctccagatttatcagcaataaaccagccagccggaagggccgagcgcagaagtggtcctgcaactttatccgcctccatccagtctattaattgttgccgggaagctagagtaagtagttcgccagttaatagtttgcgcaacgttgttgccattgctacaggcatcgtggtgtcacgctcgtcgtttggtatggcttcattcagctccggttcccaacgatcaaggcgagttacatgatcccccatgttgtgcaaaaaagcggttagctccttcggtcctccgatcgttgtcagaagtaagttggccgcagtgttatcactcatggttatggcagcactgcataattctcttactgtcatgccatccgtaagatgcttttctgtgactggtgagtactcaaccaagtcattctgagaatagtgtatgcggcgaccgagttgctcttgcccggcgtcaatacgggataataccgcgccacatagcagaactttaaaagtgctcatcattggaaaacgttcttcggggcgaaaactctcaaggatcttaccgctgttgagatccagttcgatgtaacccactcgtgcacccaactgatcttcagcatcttttactttcaccagcgtttctgggtgagcaaaaacaggaaggcaaaatgccgcaaaaaagggaataagggcgacacggaaatgttgaatactcatactcttcctttttcaatattattgaagcatttatcagggttattgtctcatgagcggatacatatttgaatgtatttagaaaaataaacaaataggggttccgcgcacatttccccgaaaagtgccacctgacgtctaagaaaccattattatcatgacattaacctataaaaataggcgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctg\n" +

//// EIPCR aspC1(succeed)
//                ">Construction of pTarget-aspC1\n" +
//                "acquire oligo aspC1\n" +
//                "acquire oligo pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr aspC1,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI	(spedig)\n" +      
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                ">aspC1\n" +
//                "ccataACTAGTacgcgtagctgccgatttccGTTTTAGAGCTAGAAATAGCAAG\n" +
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

//// EIPCR aspC2(succeed)
//                ">Construction of pTarget-aspC2\n" +
//                "acquire oligo aspC2,"+
//                "acquire oligo pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr aspC2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                ">aspC2\n" +
//                "ccataACTAGTataccagcgttaagcgagtgGTTTTAGAGCTAGAAATAGCAAG\n" +
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

///// EIPCR cscB1(succeed)
//                ">Construction of pTarget-cscB1\n" +
//                "acquire oligo cscB1,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB1,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">cscB1\n" +
//                "ccataACTAGTattctatttatgatgttctaGTTTTAGAGCTAGAAATAGCAAG\n" +
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 
//            
////// EIPCR aspC3(succeed)
//                ">Construction of pTarget-aspC3\n" +
//                "acquire oligo aspC3\n" +
//                "acquire oligo pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr aspC3,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                ">aspC3\n" +
//                "ccataACTAGTaagagcgtctttaactctgcGTTTTAGAGCTAGAAATAGCAAG\n" +
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

///////EIPCR cscB2(succeed)
//                ">Construction of pTarget-cscB2\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">cscB2\n" +
//                "ccataACTAGTatccgtcttcaaatacagcgGTTTTAGAGCTAGAAATAGCAAG\n" +
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
/////////EIPCR cscR1(succeed)
//                ">Construction of pTarget-cscR1\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">cscR1\n" +
//                "ccataACTAGTaacgcgtgaccgcgtattgcGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
                
/////////EIPCR cscR2(succeed)
//                ">Construction of pTarget-cscR2\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">cscR2\n" +
//                "ccataACTAGTacgctgcctgagtctctgtaGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
///////EIPCR cscR3(succeed)
//                ">Construction of pTarget-cscR3\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">cscR3\n" +
//                "ccataACTAGTatcaacacagcatttgctcgGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
///////EIPCR ilvE1(succeed)
//                ">Construction of pTarget-ilvE1\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">ilvE1\n" +
//                "ccataACTAGTatgtcgcacgcgctgcactaGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
                
/////EIPCR ilvE2(succeed)
//                ">Construction of pTarget-ilvE2\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">ilvE2\n" +
//                "ccataACTAGTacagagcattgatgagctgaGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 
//
//                
/////EIPCR ilvE3(succeed)
//                ">Construction of pTarget-ilvE3\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">ilvE3\n" +
//                "ccataACTAGTatccgtccgctgatcttcgtGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
////EIPCR tyrB1(succeed)
//                ">Construction of pTarget-tyrB1\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">tyrB1\n" +
//                "ccataACTAGTaaccacgtagcaatattcgcGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
////EIPCR tyrB2(succeed)
//                ">Construction of pTarget-tyrB2\n" +
//                "acquire oligo cscB2,pTargRev\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
//                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
//                "ligate dig	(lig)\n" +
//                "transform lig	(Mach1, Spec)\n" +
//                "\n" +
//                ">tyrB2\n" +
//                "ccataACTAGTaccacgtagcaatattcgccGTTTTAGAGCTAGAAATAGCAAG\n"+
//                ">pTargRev\n" +
//                "ctcagACTAGTattatacctaggactgagctag\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

                
////EIPCR tyrB3(succeed)
                ">Construction of pTarget-tyrB3\n" +
                "acquire oligo cscB2,pTargRev\n" +
                "acquire plasmid pTargetF\n" +
                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
                "ligate dig	(lig)\n" +
                "transform lig	(Mach1, Spec)\n" +
                "\n" +
                ">tyrB3\n" +
                "ccataACTAGTattcgcgccattgccagcgcGTTTTAGAGCTAGAAATAGCAAG\n"+
                ">pTargRev\n" +
                "ctcagACTAGTattatacctaggactgagctag\n" +
                ">pTargetF\n" +
                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

////EIPCR cscB3(succeed)
                ">Construction of pTarget-cscB3\n" +
                "acquire oligo cscB2,pTargRev\n" +
                "acquire plasmid pTargetF\n" +
                "pcr cscB2,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
                "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
                "ligate dig	(lig)\n" +
                "transform lig	(Mach1, Spec)\n" +
                "\n" +
                ">cscB3\n" +
                "ccataACTAGTacagttttcttcgcaatttcGTTTTAGAGCTAGAAATAGCAAG\n" +
                ">pTargRev\n" +
                "ctcagACTAGTattatacctaggactgagctag\n" +
                ">pTargetF\n" +
                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 


//////SOE(succeed)
                " >Construction of nahR\n" +
                "acquire oligo ca1110F\n" +
                "acquire oligo ca1111F\n" +
                "acquire oligo ca1111R\n" +
                "acquire oligo ca899R\n" +
                "acquire plasmid pBACr899\n" +
                "PCR ca1110F/ca1111R on pBACr899     (814 bp, A)\n" +
                " PCR ca1111F/ca899R on pBACr899      (497 bp, B)\n" +
                " PCR ca1110F/ca899R on A+B           (1287 bp, pcrpdt)\n" +
                " Digest pcrpdt                       (EcoRI/BamHI, L, pcrdig)\n" +
                " Digest pBca1100                     (EcoRI/BamHI, 2927+754, L, plasdig)\n" +
                " Ligate pcrdig and plasdig, product is pBca1100-Bca1111  {nahR-Psal}\n" +
                " >ca1110F  Forward EcoRI for Biobrick extreme variant of nahR-Psal  \n" +
                " ctctggaattcatgAGATCTGCGATCCCGCGAAGAACC\n" +
                " >ca1111F  Removing the BglII site in nahR  \n" +
                " catgaagtagatTtcgccaatgtc\n" +
                " >ca1111R  Removing the BglII site in nahR\n" +
                " gacattggcgaAatctacttcatg\n" +
                " >ca899R   Reverse BamHI for nahR promoter  \n" +
                " GCAAAggatccTCTATGGTACTCGTGATGGC\n" +
                " >pBACr899\n" +
                "ggatcGCGGCCGCtgcgatcccgcgaagaaccaaaaaagctcgacagagggcgcggtcattttaggtcgggcggatcggcgccgccggctcggctggtgtgccgcacagcaccgcctacgtgagctgccagttgatgaacttcccccgttgccagctagggcgcaagcgggctgtataagatcactgcccatcacattgatcggctcggattttttctcaatccgtaaacaggtcaaacatcagttgccgcaaccaaatattggctaggtccttgtggtacttcgcatgccagaacatgttgatggctatttcaggcaagacgactgggtgcggcaaggcgcttaggccgaagggctccacgcagcagtcggctaaacgtatcggcacagtggcgagcagatcggtgcgctggaggatgtggccaacggcggcgaagtgcggcacttccagacggatgtcgcgccggatgccgacccgtgtcatgtacgtgtccacctcgccgtggccggtgccagcggcgatgacacgcacgtggccgtaggaacagaagcgctccagagtcaggggttcgcgggtgactggatggtccttgcgacataggcacacgtagtgattctggagcagccggcgctgaaagaagccagtttgcagattgggaagcaggcccacggccaagtccacggttccgttctgcaaggcctgcatcaggctcatcgaactgtcgcgcaccgtactgatcacgcaattgggggcctggtgagccagcacatccatcagccgcggcatgaagtagatctcgccaatgtcggtcatggccagggtgaaggtacgctcgctggtcagcggatcgaagctttcatggtgctgtagggcgttgcgcagtgcgtgcatggccgaagtgacgggctcggccagatgcgcggcatagggtgtgggttccattccctgatgtgtgcgcacgaagagtgggtcctgtagcgaggtgcgcaggcgtttcagcgcattgctcacggcaggctgggtcaggcccaggttctccgcagtgatagagacgcgtctgtcgaccagcaactggttgaacaccaccagcaggtttaaatccaggtcacgcagttccatggggcctcgcttgggttattgctggtgcccggccgggcgcaatattcatgttgatgatttattatatatcgagtggtgtatttatcaatattgtttgctccgttatcgttattaacaagtcatcaataaagccatcacgagtaccatagaGGATCCTCTGTTTATTTTCAGGAGTCATCATTATTTATGAGTAAAGGAGAAGAACTTTTCACTGGAGTTGTCCCAATTCTTGTTGAATTAGATGGTGATGTTAATGGGCACAAATTTTCTGTCAGTGGAGAGGGTGAAGGTGATGCAACATACGGAAAACTTACCCTTAAATTTATTTGCACTACTGGAAAACTACCTGTTCCATGGCCAACACTTGTCACTACTTTGACTTATGGTGTTCAATGCTTTTCAAGATACCCAGATCATATGAAACGGCATGACTTTTTCAAGAGTGCCATGCCCGAAGGTTATGTACAGGAAAGAACTATATTTTTCAAAGATGACGGGAACTACAAGACACGTGCTGAAGTCAAGTTTGAAGGTGATACCCTTGTTAATAGAATCGAGTTAAAAGGTATTGATTTTAAAGAAGATGGAAACATTCTTGGACACAAATTGGAATACAACTATAACTCACACAATGTATACATCATGGCAGACAAACAAAAGAATGGAATCAAAGTTAACTTCAAAATTAGACACAACATTGAAGATGGAAGCGTTCAACTAGCAGACCATTATCAACAAAATACTCCAATTGGCGATGGCCCTGTCCTTTTACCAGACAACCATTACCTGTCCACACAATCTGCCCTTTCGAAAGATCCCAACGAAAAGAGAGACCACATGGTCCTTCTTGAGTTTGTAACAGCTGCTGGGATTACACATGGCATGGATGAACTATACAAAAGGCCTGCAGCAAACGACGAAAACTACGCTTTAGTAGCTTAATAAGCTTAATTAGCTGAGCTTGGACTCCTGTTGATAGATCCAGTAATGACCTCAGAACTCCATCTGGATTTGTTCAGAACGCTCGGTTGCCGCCGGGCGTTTTTTATTGGTGAGAATCCAAGCTAGCTTGGCGAGATTTTCAGGAGCTAAGGGCATGCCGCTTCGCCTTCGCGCGCGAATTGATCTGCTGCCTCGCGCGTTTCGGTGATGACGGTGAAAACCTCTGACACATGCAGCTCCCGGAGACGGTCACAGCTTGTCTGTAAGCGGATGCCGGGAGCAGACAAGCCCGTCAGGGCGCGTCAGCGGGTGTTGGCGGGTGTCGGGGCGCAGCCATGACCCAGTCACGTAGCGATAGCGGAGTGTATACTGGCTTAACTATGCGGCATCAGAGCAGATTGTACTGAGAGTGCACCATATGCGGTGTGAAATACCGCACAGATGCGTAAGGAGAAAATACCGCATCAGGCGCTCTTCCGCTTCCTCGCTCACTGACTCGCTGCGCTCGGTCGTTCGGCTGCGGCGAGCGGTATCAGCTCACTCAAAGGCGGTAATACGGTTATCCACAGAATCAGGGGATAACGCAGGAAAGAACATGTGAGCAAAAGGCCAGCAAAAGGCCAGGAACCGTAAAAAGGCCGCGTTGCTGGCGTTTTTCCATAGGCTCCGCCCCCCTGACGAGCATCACAAAAATCGACGCTCAAGTCAGAGGTGGCGAAACCCGACAGGACTATAAAGATACCAGGCGTTTCCCCCTGGAAGCTCCCTCGTGCGCTCTCCTGTTCCGACCCTGCCGCTTACCGGATACCTGTCCGCCTTTCTCCCTTCGGGAAGCGTGGCGCTTTCTCATAGCTCACGCTGTAGGTATCTCAGTTCGGTGTAGGTCGTTCGCTCCAAGCTGGGCTGTGTGCACGAACCCCCCGTTCAGCCCGACCGCTGCGCCTTATCCGGTAACTATCGTCTTGAGTCCAACCCGGTAAGACACGACTTATCGCCACTGGCAGCAGCCACTGGTAACAGGATTAGCAGAGCGAGGTATGTAGGCGGTGCTACAGAGTTCTTGAAGTGGTGGCCTAACTACGGCTACACTAGAAGGACAGTATTTGGTATCTGCGCTCTGCTGAAGCCAGTTACCTTCGGAAAAAGAGTTGGTAGCTCTTGATCCGGCAAACAAACCACCGCTGGTAGCGGTGGTTTTTTTGTTTGCAAGCAGCAGATTACGCGCAGAAAAAAAGGATCTCAAGAAGATCCTTTGATCTTTTCTACGGGGTCTGACGCTCAGTGGAACGAAAACTCACGTTAAGGGATTTTGGTCATGAGATTATCAAAAAGGATCTTCACCTAGATCCTTTTAAATTAAAAATGAAGTTTTAAATCAATCTAAAGTATATATGAGTAAACTTGGTCTGACAGTTACCAATGCTTAATCAGTGAGGCACCTATCTCAGCGATCTGTCTATTTCGTTCATCCATAGTTGCCTGACTCCCCGTCGTGTAGATAACTACGATACGGGAGGGCTTACCATCTGGCCCCAGTGCTGCAATGATACCGCGAGACCCACGCTCACCGGCTCCAGATTTATCAGCAATAAACCAGCCAGCCGGAAGGGCCGAGCGCAGAAGTGGTCCTGCAACTTTATCCGCCTCCATCCAGTCTATTAATTGTTGCCGGGAAGCTAGAGTAAGTAGTTCGCCAGTTAATAGTTTGCGCAACGTTGTTGCCATTGCTACAGGCATCGTGGTGTCACGCTCGTCGTTTGGTATGGCTTCATTCAGCTCCGGTTCCCAACGATCAAGGCGAGTTACATGATCCCCCATGTTGTGCAAAAAAGCGGTTAGCTCCTTCGGTCCTCCGATCGTTGTCAGAAGTAAGTTGGCCGCAGTGTTATCACTCATGGTTATGGCAGCACTGCATAATTCTCTTACTGTCATGCCATCCGTAAGATGCTTTTCTGTGACTGGTGAGTACTCAACCAAGTCATTCTGAGAATAGTGTATGCGGCGACCGAGTTGCTCTTGCCCGGCGTCAACACGGGATAATACCGCGCCACATAGCAGAACTTTAAAAGTGCTCATCATTGGAAAACGTTCTTCGGGGCGAAAACTCTCAAGGATCTTACCGCTGTTGAGATCCAGTTCGATGTAACCCACTCGTGCACCCAACTGATCTTCAGCATCTTTTACTTTCACCAGCGTTTCTGGGTGAGCAAAAACAGGAAGGCAAAATGCCGCAAAAAAGGGAATAAGGGCGACACGGAAATGTTGAATACTCATACTCTTCCTTTTTCAATATTATTGAAGCATTTATCAGGGTTATTGTCTCATGAGCGGATACATATTTGAATGTATTTAGAAAAATAAACAAAAAGAGTTTGTAGAAACGCAAAAAGGCCATCCGTCAGGCATATGGGAATTCGAAGCTTGGGCCCGAACAAAAACTCATCTCAGAAGAGGATCTGAATAGCGCCGTCGACCATCATCATCATCATCATTGAGTTTAAACGGTCTCCAGCTTGGCTGTTTTGGCGGATGAGAGAAGATTTTCAGCCTGATACAGATTAAATCAGAACGCAGAAGCGGTCTGATAAAACAGAATTTGCCTGGCGGCAGTAGCGCGGTGGTCCCACCTGACCCCATGCCGAACTCAGAAGTGAAACGCCGTAGCGCCGATGGTAGTGTGGGGTCTCCCCATGCGAGAGTAGGGAACTGCCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACTAATTCtgattcgcacgggcccatggctaattcccatgtcagccgttaagtgttcctgtgtcactcaaaattgctttgagaggctctaagggcttctcagtgcgttacatccctggcttgttgtccacaaccgttaaaccttaaaagctttaaaagccttatatattcttttttttcttataaaacttaaaaccttagaggctatttaagttgctgatttatattaattttattgttcaaacatgagagcttagtacgtgaaacatgagagcttagtacgttagccatgagagcttagtacgttagccatgagggtttagttcgttaaacatgagagcttagtacgttaaacatgagagcttagtacgtgaaacatgagagcttagtacgtactatcaacaggttgaactgctgatcttcagatcctctacgccggacgcatCGTGGCCGGATCTTGCGCAAGCGGTCTCCAATTCAGAAGAACTCGTCAAGAAGGCGATAGAAGGCGATGCGCTGCGAATCGGGAGCGGCGATACCGTAAAGCACGAGGAAGCGGTCAGCCCATTCGCCGCCAAGCTCTTCAGCAATATCACGGGTATCCAACGCTATGTCCTGATAGCGGTCCGCCACACCCAGCCGGCCACAGTCGTTGAATCCAGAAAAGCGGCCATTTTCCACCATGATATTCGGCAAGCAGGCATCGCCATGGATCACGACGAGATCCTCGCCGTCGGGCATGCGCGCCTTGAGCCTGGCGAACAGTTCGGCTGGCGCGAGCCCCTGATGCTCTTCGTCCAGATCATCCTGATCGACAAGACCGGCTTCCATCCGAGTACGTGCTCGCTCGATGCGATGTTTCGCTTGGTGGTCGAATGGGCAGGTAGCCGGATCAAGCGTATGCAGCCGCCGCATTGCATCAGCCATGATGGATACTTTCTCGGCAGGAGCAAGGCGAGATGACAGGAGATCCTGCCCCGGCACTTCGCCCAATAGCAGCCAGTCCCTTCCCGCTTCAGTGACAACGTCGAGCACAGCTGCGCAAGGAACGCCCGTCGTGGCCAGCCACGATAGCCGCGCTGCCTCGTCCTGCAGTTCATTCAGGGCACCGGACAGGTCGGTCTTGACAAAAAGAACCGGGCGCCCCTGCGCTGACAGCCGGAACACGGCGGCATCAGAGCAGCCGATTGTCTGTTGTGCCCAGTCATAGCCGAATAGCCTCTCCACCCAAGCGGCCGGAGAACCTGCGTGCAATCCATCTTGTTCAATCATGCGAAACGATCCTCATCCTGTCTCTTGATCAGATCTTGATCCCCTGCGCCATCAGATCCTTGGCGGCAAGAAAGCCATCCAGTTTACTTTGCAGGGCTTCCCAACCTTACCAGAGGGCGCCCCAGCTGGCAATTCCGGTTCGCTTGCTGTCCATAAAACCGCCCAGTCTAGCTATCGCCATGTAAGCCCACTGCAAGCTACCTGCTTTCTCTTTGCGCTTGCGTTTTCCCTTGTCCAGATAGCCCAGTAGCTGACATTCATCAAGCTTATGATGTGGTCTGTCCTTTTACAGCCAGTAGTGCTCGCCGCAGTCGAGCGACAGGGCGAAGCCCTCGAGTGAGCGAGGAAGCACCAGGGAACAGCACTTATATATTCTGCTTACACACGATGCCTGAAAAAACTTCCCTTGGGGTTATCCACTTATCCACGGGGATATTTTTATAATTATTTTTTTTATAGTTTTTAGATCTTCTTTTTTAGAGCGCCTTGTAGGCCTTTATCCATGCTGGTTCTAGAGAAGGTGTTGTGACAAATTGCCCTTTCAGTGTGACAAATCACCCTCAAATGACAGTCCTGTCTGTGACAAATTGCCCTTAACCCTGTGACAAATTGCCCTCAGAAGAAGCTGTTTTTTCACAAAGTTATCCCTGCTTATTGACTCTTTTTTATTTAGTGTGACAATCTAAAAACTTGTCACACTTCACATGGATCTGTCATGGCGGAAACAGCGGTTATCAATCACAAGAAACGTAAAAATAGCCCGCGAATCGTCCAGTCAAACGACCTCACTGAGGCGGCATATAGTCTCTCCCGGGATCAAAAACGTATGCTGTATCTGTTCGTTGACCAGATCAGAAAATCTGATGGCACCCTACAGGAACATGACGGTATCTGCGAGATCCATGTTGCTAAATATGCTGAAATATTCGGATTGACCTCTGCGGAAGCCAGTAAGGATATACGGCAGGCATTGAAGAGTTTCGCGGGGAAGGAAGTGGTTTTTTATCGCCCTGAAGAGGATGCCGGCGATGAAAAAGGCTATGAATCTTTTCCTTGGTTTATCAAACGTGCGCACAGTCCATCCAGAGGGCTTTACAGTGTACATATCAACCCATATCTCATTCCCTTCTTTATCGGGTTACAGAACCGGTTTACGCAGTTTCGGCTTAGTGAAACAAAAGAAATCACCAATCCGTATGCCATGCGTTTATACGAATCCCTGTGTCAGTATCGTAAGCCGGATGGCTCAGGCATCGTCTCTCTGAAAATCGACTGGATCATAGAGCGTTACCAGCTGCCTCAAAGTTACCAGCGTATGCCTGACTTCCGCCGCCGCTTCCTGCAGGTCTGTGTTAATGAGATCAACAGCAGAACTCCAATGCGCCTCTCATACATTGAGAAAAAGAAAGGCCGCCAGACGACTCATATCGTATTTTCCTTCCGCGATATCACTTCCATGACGACAGGATAGTCTGAGGGTTATCTGTCACAGATTTGAGGGTGGTTCGTCACATTTGTTCTGACCTACTGAGGGTAATTTGTCACAGTTTTGCTGTTTCCTTCAGCCTGCATGGATTTTCTCATACTTTTTGAACTGTAATTTTTAAGGAAGCCAAATTTGAGGGCAGTTTGTCACAGTTGATTTCCTTCTCTTTCCCTTCGTCATGTGACCTGATATCGGGGGTTAGTTCGTCATCATTGATGAGGGTTGATTATCACAGTTTATTACTCTGAATTGGCTATCCGCGTGTGTACCTCTACCTGGAGTTTTTCCCACGGTGGATATTTCTTCTTGCGCTGAGCGTAAGAGCTATCTGACAGAACAGTTCTTCTTTGCTTCCTCGCCAGTTCGCTCGCTATGCTCGGTTACACGGCTGCGGCGAGCGCTAGTGATAATAAGTGACTGAGGTATGTGCTCTTCTTATCTCCTTTTGTAGTGTTGCTCTTATTTTAAACAACTTTGCGGTTTTTTGATGACTTTGCGATTTTGTTGTTGCTTTGCAGTAAATTGCAAGATTTAATAAAAAAACGCAAAGCAATGATTAAAGGATGTTCAGAATGAAACTCATGGAAACACTTAACCAGTGCATAAACGCTGGTCATGAAATGACGAAGGCTATCGCCATTGCACAGTTTAATGATGACAGCCCGGAAGCGAGGAAAATAACCCGGCGCTGGAGAATAGGTGAAGCAGCGGATTTAGTTGGGGTTTCTTCTCAGGCTATCAGAGATGCCGAGAAAGCAGGGCGACTACCGCACCCGGATATGGAAATTCGAGGACGGGTTGAGCAACGTGTTGGTTATACAATTGAACAAATTAATCATATGCGTGATGTGTTTGGTACGCGATTGCGACGTGCTGAAGACGTATTTCCACCGGTGATCGGGGTTGCTGCCCATAAAGGTGGCGTTTACAAAACCTCAGTTTCTGTTCATCTTGCTCAGGATCTGGCTCTGAAGGGGCTACGTGTTTTGCTCGTGGAAGGTAACGACCCCCAGGGAACAGCCTCAATGTATCACGGATGGGTACCAGATCTTCATATTCATGCAGAAGACACTCTCCTGCCTTTCTATCTTGGGGAAAAGGACGATGTCACTTATGCAATAAAGCCCACTTGCTGGCCGGGGCTTGACATTATTCCTTCCTGTCTGGCTCTGCACCGTATTGAAACTGAGTTAATGGGCAAATTTGATGAAGGTAAACTGCCCACCGATCCACACCTGATGCTCCGACTGGCCATTGAAACTGTTGCTCATGACTATGATGTCATAGTTATTGACAGCGCGCCTAACCTGGGTATCGGCACGATTAATGTCGTATGTGCTGCTGATGTGCTGATTGTTCCCACGCCTGCTGAGTTGTTTGACTACACCTCCGCACTGCAGTTTTTCGATATGCTTCGTGATCTGCTCAAGAACGTTGATCTTAAAGGGTTCGAGCCTGATGTACGTATTTTGCTTACCAAATACAGCAATAGTAATGGCTCTCAGTCCCCGTGGATGGAGGAGCAAATTCGGGATGCCTGGGGAAGCATGGTTCTAAAAAATGTTGTACGTGAAACGGATGAAGTTGGTAAAGGTCAGATCCGGATGAGAACTGTTTTTGAACAGGCCATTGATCAACGCTCTTCAACTGGTGCCTGGAGAAATGCTCTTTCTATTTGGGAACCTGTCTGCAATGAAATTTTCGATCGTCTGATTAAACCACGCTGGGAGATTAGATAATGAAGCGTGCGCCTGTTATTCCAAAACATACGCTCAATACTCAACCGGTTGAAGATACTTCGTTATCGACACCAGCTGCCCCGATGGTGGATTCGTTAATTGCGCGCGTAGGAGTAATGGCTCGCGGTAATGCCATTACTTTGCCTGTATGTGGTCGGGATGTGAAGTTTACTCTTGAAGTGCTCCGGGGTGATAGTGTTGAGAAGACCTCTCGGGTATGGTCAGGTAATGAACGTGACCAGGAGCTGCTTACTGAGGACGCACTGGATGATCTCATCCCTTCTTTTCTACTGACTGGTCAACAGACACCGGCGTTCGGTCGAAGAGTATCTGGTGTCATAGAAATTGCCGATGGGAGTCGCCGTCGTAAAGCTGCTGCACTTACCGAAAGTGATTATCGTGTTCTGGTTGGCGAGCTGGATGATGAGCAGATGGCTGCATTATCCAGATTGGGTAACGATTATCGCCCAACAAGTGCTTATGAACGTGGTCAGCGTTATGCAAGCCGATTGCAGAATGAATTTGCTGGAAATATTTCTGCGCTGGCTGATGCGGAAAATATTTCACGTAAGATTATTACCCGCTGTATCAACACCGCCAAATTGCCTAAATCAGTTGTTGCTCTTTTTTCTCACCCCGGTGAACTATCTGCCCGGTCAGGTGATGCACTTCAAAAAGCCTTTACAGATAAAGAGGAATTACTTAAGCAGCAGGCATCTAACCTTCATGAGCAGAAAAAAGCTGGGGTGATATTTGAAGCTGAAGAAGTTATCACTCTTTTAACTTCTGTGCTTAAAACGTCATCTGCATCAAGAACTAGTTTAAGCTCACGACATCAGTTTGCTCCTGGAGCGACAGTATTGTATAAGGGCGATAAAATGGTGCTTAACCTGGACAGGTCTCGTGTTCCAACTGAGTGTATAGAGAAAATTGAGGCCATTCTTAAGGAACTTGAAAAGCCAGCACCCTGATGCGACCACGTTTTAGTCTACGTTTATCTGTCTTTACTTAATGTCCTTTGTTACAGGCCAGAAAGCATAACTGGCCTGAATATTCTCTCTGGGCCCACTGTTCCACTTGTATCGTCGGTCTGATAATCAGACTGGGACCACGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGGACCACGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGGACCACGGTCCCACTCGTATCGTCGGTCTGATAATCAGACTGGGACCACGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGGACCATGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGGACCACGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGAACCACGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGGACCACGGTCCCACTCGTATCGTCGGTCTGATTATTAGTCTGGGACCACGATCCCACTCGTGTTGTCGGTCTGATTATCGGTCTGGGACCACGGTCCCACTTGTATTGTCGATCAGACTATCAGCGTGAGACTACGATTCCATCAATGCCTGTCAAGGGCAAGTATTGACATGTCGTCGTAACCTGTAGAACGGAGTAACCTCGGTGTGCGGTTGTATGCCTGCTGTGGATTG\n" 

//                
////   (succeed)         
//">Construction of pJBAG\n" +
//"acquire oligo jbag3F\n" +
//"acquire oligo jbag3R\n" +
//"acquire plasmid p20N5\n" +
//"acquire oligo jbag2F\n" +
//"acquire oligo jbag2R\n" +
//"acquire plasmid pMCL200Y\n" +
//"acquire oligo jbag1F\n" +
//"acquire oligo jbag1R\n" +
//"acquire plasmid p20N190\n" +
//"acquire oligo jbag4F\n" +
//"acquire oligo jbag4R\n" +
//"acquire plasmid p20N189-A\n" +
//"PCR jbag3F,jbag3R on p20N5	(1125 bp, Bladt)\n" +
//"PCR jbag2F,jbag2R on pMCL200Y	(897 bp, p15Adt)\n" +
//"PCR jbag1F,jbag1R  on p20N190	(136 bp, slowdt)\n" +
//"PCR jbag4F,jbag4R on  p20N189-A	(766 bp, amilCPdt)\n" +
//"Assemble Bladt,p15Adt,slowdt,amilCPdt    (BsmBI, pJBAG)\n" +
//"\n" +
//">jbag3F Forward Biobricking on p20N5\n" +
//"ccaaaCGTCTCgcagaaatcatccttagcgaaag	\n" +
//"	\n" +
//">jbag3R Reverse Biobricking on p20N5	\n" +
//"gctagCGTCTCcttaccaatgcttaatcag	\n" +
//"	\n" +
//">jbag2F Forward Biobricking on pMCL200Y	\n" +
//"ccaaaCGTCTCggtaatttttttaaggcagttattg	\n" +
//"	\n" +
//">jbag2R Reverse Biobricking on pMCL200Y	\n" +
//"gctagCGTCTCctagcggagtgtatactggc	\n" +
//"	\n" +
//">jbag1F Forward Biobricking on p20N190	\n" +
//"ccaaaCGTCTCggctacagccacgtatcgccagatg	\n" +
//"	\n" +
//">jbag1R Reverse Biobricking on p20N190	\n" +
//"gctagCGTCTCctaattcctaatttttgttgacac	\n" +
//"	\n" +
//">jbag4F Forward Biobricking on p20N189-A	\n" +
//"ccaaaCGTCTCgattaACTGtGAGACCttgacggctagctcagtcctag	\n" +
//"	\n" +
//">jbag4R Reverse Biobricking on p20N189-A	\n" +
//"gctagCGTCTCctctgGAACaGAGACCttaggcgaccacaggtttgc	\n" +
//"	\n" +
//">Bladt	\n" +
//"ccaaaCGTCTCgcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggctctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaagGAGACGctagc	\n" +
//"	\n" +
//">p15Adt\n" +
//"ccaaaCGTCTCggtaatttttttaaggcagttattggtgcctagaaatattttatctgattaataagatgatcttcttgagatcgttttggtctgcgcgtaatctcttgctctgaaaacgaaaaaaccgccttgcagggcggtttttcgaaggttctctgagctaccaactctttgaaccgaggtaactggcttggaggagcgcagtcaccaaaacttgtcctttcagtttagccttaaccggcgcatgacttcaagactaactcctctaaatcaattaccagtggctgctgccagtggtgcttttgcatgtctttccgggttggactcaagacgatagttaccggataaggcgcagcggtcggactgaacggggggttcgtgcatacagtccagcttggagcgaactgcctacccggaactgagtgtcaggcgtggaatgagacaaacgcggccataacagcggaatgacaccggtaaaccgaaaggcaggaacaggagagcgcacgagggagccgccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccaccactgatttgagcgtcagatttcgtgatgcttgtcaggggggcggagcctatggaaaaacggctttgccgcggccctctcacttccctgttaagtatcttcctggcatcttccaggaaatctccgccccgttcgtaagccatttccgctcgccgcagtcgaacgaccgagcgtagcgagtcagtgagcgaggaagcggaatatatcctgtatcacatattctgctgacgcaccggtgcagccttttttctcctgccacatgaagcacttcactgacaccctcatcagtgccaacatagtaagccagtatacactccgctagGAGACGctagc	\n" +
//"	\n" +
//">slowdt	\n" +
//"ccaaaCGTCTCggctacagccacgtatcgccagatgtttacatttaatgataatgtattgactgtaacagaaggatctaaaataactctatcaatgatagagtgtcaacaaaaattaggaattagGAGACGctagc	\n" +
//"	\n" +
//">amilCPdt	\n" +
//"ccaaaCGTCTCgattaACTGtGAGACCttgacggctagctcagtcctaggtacagtgctagctactagagaaataggagaaatactagatgagtgtgatcgctaaacaaatgacctacaaggtttatatgtcaggcacggtcaatggacactactttgaggtcgaaggcgatggaaaaggtaagccctacgagggggagcagacggtaaagctcactgtcaccaagggcggacctctgccatttgcttgggatattttatcaccacagtgtcagtacggaagcataccattcaccaagtaccctgaagacatccctgactatgtaaagcagtcattcccggagggctatacatgggagaggatcatgaactttgaagatggtgcagtgtgtactgtcagcaatgattccagcatccaaggcaactgtttcatctaccatgtcaagttctctggtttgaactttcctcccaatggacctgtcatgcagaagaagacacagggctgggaacccaacactgagcgactctttgcacgagatggaatgctgctaggaaacaactttatggctctgaagttagaaggaggcggtcactatttgtgtgaatttaaaactacttacaaggcaaagaagcctgtgaagatgccagggtatcactatgttgaccgcaaactggatgtaaccaatcacaacaaggattacacttcggttgagcagtgtgaaatttccattgcacgcaaacctgtggtcgcctaaGGTCTCtGTTCcagagGAGACGctagc\n" +
//"\n" +
//">p20N5\n" +
//"attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGcgatcagatACTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt\n" +
//"\n" +
//">pMCL200Y\n" +
//"TTTTTTTAAGGCAGTTATTGGTGCCTAGAAATATTTTATCTGATTAATAAGATGATCTTCTTGAGATCGTTTTGGTCTGCGCGTAATCTCTTGCTCTGAAAACGAAAAAACCGCCTTGCAGGGCGGTTTTTCGAAGGTTCTCTGAGCTACCAACTCTTTGAACCGAGGTAACTGGCTTGGAGGAGCGCAGTCACCAAAACTTGTCCTTTCAGTTTAGCCTTAACCGGCGCATGACTTCAAGACTAACTCCTCTAAATCAATTACCAGTGGCTGCTGCCAGTGGTGCTTTTGCATGTCTTTCCGGGTTGGACTCAAGACGATAGTTACCGGATAAGGCGCAGCGGTCGGACTGAACGGGGGGTTCGTGCATACAGTCCAGCTTGGAGCGAACTGCCTACCCGGAACTGAGTGTCAGGCGTGGAATGAGACAAACGCGGCCATAACAGCGGAATGACACCGGTAAACCGAAAGGCAGGAACAGGAGAGCGCACGAGGGAGCCGCCAGGGGGAAACGCCTGGTATCTTTATAGTCCTGTCGGGTTTCGCCACCACTGATTTGAGCGTCAGATTTCGTGATGCTTGTCAGGGGGGCGGAGCCTATGGAAAAACGGCTTTGCCGCGGCCCTCTCACTTCCCTGTTAAGTATCTTCCTGGCATCTTCCAGGAAATCTCCGCCCCGTTCGTAAGCCATTTCCGCTCGCCGCAGTCGAACGACCGAGCGTAGCGAGTCAGTGAGCGAGGAAGCGGAATATATCCTGTATCACATATTCTGCTGACGCACCGGTGCAGCCTTTTTTCTCCTGCCACATGAAGCACTTCACTGACACCCTCATCAGTGCCAACATAGTAAGCCAGTATACACTCCGCTAGCGCCCAATACGCAAACCGCCTCTCCCCGCGCGTTGGCCGATTCATTAATGCAGCTGGCACGACAGGTTTCCCGACTGGAAAGCGGGCAGTGAGCGCAACGCAATTAATGTGAGTTAGCTCACTCATTAGGCACCCCAGGCTTTACACTTTATGCTTCCGGCTCGTATGTTGTGTGGAATTGTGAGCGGATAACAATTTCACACAGGAAACAGCTATGACCATGATTACGCCAAGCTCGAAATTAACCCTCACTAAAGGGAACAAAAGCTGGTACCGGGCCCCCCCTCGAGGTCGACGGTATCGATAAGCTTGATATCGAATTCCTGCAGCCCGGGGGATCCACTAGTTCTAGAGCGGCCGCCACCGCGGTGGAGCTCCAATTCGCCCTATAGTGAGTCGTATTACAATTCACTGGCCGTCGTTTTACAACGTCGTGACTGGGAAAACCCTGGCGTTACCCAACTTAATCGCCTTGCAGCACATCCCCCTTTCGCCAGCTGGCGTAATAGCGAAGAGGCCCGCACCGATCGCCCTTCCCAACAGTTGCGCAGCCTGAATGGCGAATGGGACGCGCCCTGTAGCGGCGCATTAAGCGCGGCGGGTGTGGTGGTTACGCGCAGCGTGACCGCTACACTTGCCAGCagcttttcaattcaattcatcattttttttttattcttttttttgatttcggtttctttgaaatttttttgattcggtaatctccgaacagaaggaagaacgaaggaaggagcacagacttagattggtatatatacgcatatgtagtgttgaagaaacatgaaattgcccagtattcttaacccaactgcacagaacaaaaacctgcaggaaacgaagataaatcatgtcgaaagctacatataaggaacgtgctgctactcatcctagtcctgttgctgccaagctatttaatatcatgcacgaaaagcaaacaaacttgtgtgcttcattggatgttcgtaccaccaaggaattactggagttagttgaagcattaggtcccaaaatttgtttactaaaaacacatgtggatatcttgactgatttttccatggagggcacagttaagccgctaaaggcattatccgccaagtacaattttttactcttcgaagacagaaaatttgctgacattggtaatacagtcaaattgcagtactctgcgggtgtatacagaatagcagaatgggcagacattacgaatgcacacggtgtggtgggcccaggtattgttagcggtttgaagcaggcggcagaagaagtaacaaaggaacctagaggccttttgatgttagcagaattgtcatgcaagggctccctatctactggagaatatactaagggtactgttgacattgcgaagagcgacaaagattttgttatcggctttattgctcaaagagacatgggtggaagagatgaaggttacgattggttgattatgacacccggtgtgggtttagatgacaagggagacgcattgggtcaacagtatagaaccgtggatgatgtggtctctacaggatctgacattattattgttggaagaggactatttgcaaagggaagggatgctaaggtagagggtgaacgttacagaaaagcaggctgggaagcatatttgagaagatgcggccagcaaaactaaaaaactgtattataagtaaatgcatgtatactaaactcacaaattagagcttcaatttaattatatcagttattacccgggaatctcggtcgtaatgatttttataatgacgaaaaaaaaaaaattggaaagaaaaagctgggcgcgccggccggcccttttcatcacgtgctataaaaataattataatttaaattttttaatataaatatataaattaaaaatagaaagtaaaaaaagaaattaaagaaaaaatagtttttgttttccgaagatgtaaaagactctagggggatcgccaacaaatactaccttttatcttgctcttcctgctctcaggtattaatgccgaattgtttcatcttgtctgtgtagaagaccacacacgaaaatcctgtgattttacattttacttatcgttaatcgaatgtatatctatttaatctgcttttcttgtctaataaatatatatgtaaagtacgctttttgttgaaattttttaaacctttgtttatttttttttcttcattccgtaactcttctaccttctttatttactttctaaaatccaaatacaaaacataaaaataaataaacacagagtaaattcccaaattattccatcattaaaagatacgaggcgcgtgtaagttacaggcaagcgatcggccggcccgggcatttaaatgcaggccgcgtacgcgtcgacggtaccgaattcgcttaaacgagcGCTGATGTCCGGCGGTGCTTTTGCCGTTACGCACCACCCCGTCAGTAGCTGAACAGGAGGGACAGCTGATAGAAACAGAAGCCACTGGAGCACCTCAAAAACACCATCATACACTAAATCAGTAAGTTGGCAGCATCACCCGACGCACTTTGCGCCGAATAAATACCTGTGACGGAAGATCACTTCGCAGAATAAATAAATCCTGGTGTCCCTGTTGATACCGGGAAGCCCTGGGCCAACTTTTGGCGAAAATGAGACGTTGATCGGCACGTAAGAGGTTCCAACTTTCACCATAATGAAATAAGATCACTACCGGGCGTATTTTTTGAGTTATCGAGATTTTCAGGAGCTAAGGAAGCTAAAATGGAGAAAAAAATCACTGGATATACCACCGTTGATATATCCCAATGGCATCGTAAAGAACATTTTGAGGCATTTCAGTCAGTTGCTCAATGTACCTATAACCAGACCGTTCAGCTGGATATTACGGCCTTTTTAAAGACCGTAAAGAAAAATAAGCACAAGTTTTATCCGGCCTTTATTCACATTCTTGCCCGCCTGATGAATGCTCATCCGGAATTTCGTATGGCAATGAAAGACGGTGAGCTGGTGATATGGGATAGTGTTCACCCTTGTTACACCGTTTTCCATGAGCAAACTGAAACGTTTTCATCGCTCTGGAGTGAATACCACGACGATTTCCGGCAGTTTCTACACATATATTCGCAAGATGTGGCGTGTTACGGTGAAAACCTGGCCTATTTCCCTAAAGGGTTTATTGAGAATATGTTTTTCGTCTCAGCCAATCCCTGGGTGAGTTTCACCAGTTTTGATTTAAACGTGGCCAATATGGACAACTTCTTCGCCCCCGTTTTCACCATGGGCAAATATTATACGCAAGGCGACAAGGTGCTGATGCCGCTGGCGATTCAGGTTCATCATGCCGTTTGTGATGGCTTCCATGTCGGCAGAATGCTTAATGAATTACAACAGTACTGCGATGAGTGGCAGGGCGGGGCGTAA\n" +
//"\n" +
//">p20N190\n" +
//"attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGtGAGGAGGTCTCAccagACTAGActgtaacagagcattagcgcaaggtgatttttgtcttcttgcgctaattttttTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATACTGAGCACATCAGCAGGACGCACTGACCggatccaaataggagaaatactagATGTCTTATTCAAAGCATGGCATCGTACAAGAAATGAAGACGAAATACCATATGGAAGGCAGTGTCAATGGCCATGAATTTACGATCGAAGGTGTAGGAACTGGGTACCCTTACGAAGGGAAACAGATGTCCGAATTAGTGATCATCAAGCCTGCGGGAAAACCCCTTCCATTCTCCTTTGACATACTGTCATCAGTCTTTCAATATGGAAACCGTTGCTTCACAAAGTACCCGGCAGACATGCCTGACTATTTCAAGCAAGCATTCCCAGATGGAATGTCATATGAAAGGTCATTTCTATTTGAGGATGGAGCAGTTGCTACAGCCAGCTGGAACATTCGACTCGAAGGAAATTGCTTCATCCACAAATCCATCTTTCATGGCGTAAACTTTCCCGCTGATGGACCCGTAATGAAAAAGAAGACCATTGACTGGGATAAGTCCTTCGAAAAAATGACTGTGTCTAAAGAGGTGCTAAGAGGTGACGTGACTATGTTTCTTATGCTCGAAGGAGGTGGTTCTCACAGATGCCAATTTCACTCCACTTACAAAACAGAGAAGCCGGTCACACTGCCCCCGAATCATGTCGTAGAACATCAAATTGTGAGGACCGACCTTGGCCAAAGTGCAAAAGGCTTTACAGTCAAGCTGGAAGCACATGCCGCGGCTCATGTTAACCCTTTGAAGGTTAAATAAtaaGAATTCccctACTAGACGAATAACATTAGTCTCCTTCGGGAGACTtTTTTTCATTTTACCAGCCACGTATCGCCAGATGtttacatttaatgataatgtattgactgtaacagaAGGATCTaaaataactctatcaatgatagagtgtcaacaaaaattaggaattaATGATGAGTAGATTAGATAAAAGTAAAGTGATTAACAGCGCATTAGAGCTGCTTAATGAGTTTGGTTTGGAAGGTTTAAAAACCCGTAAACTCGCCCAGAAACTTGGTGTAGAGCAGCCTACATTGTATTGGCATGTAAAAAATAAGCGGGCTTTGCTCGACGCCTTAGCCATTGAGATGTTAGATAGGCACCATACTCACTTTTGCCCTTTAGAAGGGGAAAGCTGGCAAGATTTTTTACGTAATAACGCTAAAAGTTTTAGAGGGACTGCTCTAAGTCATCGCGATGGAGCAAAAGTACATTTAGGTACACGGCCTACAGAAAAACAGTATGAAACTCTCGAAAATCAATTAGCCTTTTTATGCCAACAAGGTTTTTCACTAGAGAATGCATTATATGCACTCAGCGCTGTGGGGCATTTTACTTTAGGTTGCGTATTGGAAGATCAAGAGCATCAAGTCGCTAAAGAAGAAAGGGAAACACCTACTACTGATAGTATGCCGGCATTATTACGACAAGCTATCGAATTATTTGAACACCAAGGTGCAGAGCCAGCCTTCTTATTCGGCCTTGAATTGATCATATGCGGATTAGAAAAACAACTTAAATGTGAAAGTGGGTCTTAAcAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctagtggAAAGCCACGTTGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGAACAATAAAACTGTCTGCTTACATAAACAGTAATACAAGGGGTGTTATGAGCCATATTCAACGGGAAACGTCTTGTTCGAGGCCGCGATTAAATTCCAACATGGACGCTGATTTATATGGGTATAAATGGGCTCGCGATAATGTCGGGCAATCAGGTGCGACAATCTATCGATTGTATGGGAAGCCCGATGCGCCAGAGTTGTTTCTGAAACATGGCAAAGGTAGCGTTGCCAATGATGTTACAGATGAGATGGTCAGACTAAACTGGCTGACGGAATTTATGCCTCTTCCGACCATCAAGCATTTTATCCGTACTCCTGATGATGCATGGTTACTCACCACTGCGATTCCGGGGAAAACAGCATTCCAGGTATTAGAAGAATATCCTGATTCAGGTGAAAATATTGTTGATGCGCTGGCAGTGTTCCTGCGCCGGTTGCATTCGATTCCTGTTTGTAATTGTCCTTTTAACAGCGATCGCGTATTTCGACTCGCTCAGGCGCAATCACGAATGAATAACGGTTTGGTTGATGCGAGTGATTTTGATGACGAGCGTAATGGCTGGCCTGTTGAACAAGTCTGGAAAGAAATGCATAAACTTTTGCCATTCTCACCGGATTCAGTCGTCACTCATGGTGATTTCTCACTTGATAACCTTATTTTTGACGAGGGGAAATTAATAGGTTGTATTGATGTTGGACGAGTCGGAATCGCAGACCGATACCAGGATCTTGCAATCCTATGGAACTGCCTCGGTGAGTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGActgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt\n" +
//"\n" +
//">p20N189-A\n" +
//"attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaTGTCTGAGACGACTAGTttgacggctagctcagtcctaggtacagtgctagctactagagaaataggagaaatactagATGAGTGTGATCGCTAAACAAATGACCTACAAGGTTTATATGTCAGGCACGGTCAATGGACACTACTTTGAGGTCGAAGGCGATGGAAAAGGTAAGCCCTACGAGGGGGAGCAGACGGTAAAGCTCACTGTCACCAAGGGCGGACCTCTGCCATTTGCTTGGGATATTTTATCACCACAGTGTCAGTACGGAAGCATACCATTCACCAAGTACCCTGAAGACATCCCTGACTATGTAAAGCAGTCATTCCCGGAGGGCTATACATGGGAGAGGATCATGAACTTTGAAGATGGTGCAGTGTGTACTGTCAGCAATGATTCCAGCATCCAAGGCAACTGTTTCATCTACCATGTCAAGTTCTCTGGTTTGAACTTTCCTCCCAATGGACCTGTCATGCAGAAGAAGACACAGGGCTGGGAACCCAACACTGAGCGACTCTTTGCACGAGATGGAATGCTGCTAGGAAACAACTTTATGGCTCTGAAGTTAGAAGGAGGCGGTCACTATTTGTGTGAATTTAAAACTACTTACAAGGCAAAGAAGCCTGTGAAGATGCCAGGGTATCACTATGTTGACCGCAAACTGGATGTAACCAATCACAACAAGGATTACACTTCGGTTGAGCAGTGTGAAATTTCCATTGCACGCAAACCTGTGGTCGCCTAAtaaAAGCTTGGATCCCGTCTCAACGTGACCCCTGCTCcagaaatcTGAAATAAAAAAGGGAGCCCGAAGGCTCCCTaTCATTTATAATAAatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctagtggAAAGCCACGTTGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGAACAATAAAACTGTCTGCTTACATAAACAGTAATACAAGGGGTGTTATGAGCCATATTCAACGGGAAACGTCTTGTTCGAGGCCGCGATTAAATTCCAACATGGACGCTGATTTATATGGGTATAAATGGGCTCGCGATAATGTCGGGCAATCAGGTGCGACAATCTATCGATTGTATGGGAAGCCCGATGCGCCAGAGTTGTTTCTGAAACATGGCAAAGGTAGCGTTGCCAATGATGTTACAGATGAGATGGTCAGACTAAACTGGCTGACGGAATTTATGCCTCTTCCGACCATCAAGCATTTTATCCGTACTCCTGATGATGCATGGTTACTCACCACTGCGATTCCGGGGAAAACAGCATTCCAGGTATTAGAAGAATATCCTGATTCAGGTGAAAATATTGTTGATGCGCTGGCAGTGTTCCTGCGCCGGTTGCATTCGATTCCTGTTTGTAATTGTCCTTTTAACAGCGATCGCGTATTTCGACTCGCTCAGGCGCAATCACGAATGAATAACGGTTTGGTTGATGCGAGTGATTTTGATGACGAGCGTAATGGCTGGCCTGTTGAACAAGTCTGGAAAGAAATGCATAAACTTTTGCCATTCTCACCGGATTCAGTCGTCACTCATGGTGATTTCTCACTTGATAACCTTATTTTTGACGAGGGGAAATTAATAGGTTGTATTGATGTTGGACGAGTCGGAATCGCAGACCGATACCAGGATCTTGCAATCCTATGGAACTGCCTCGGTGAGTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGAACTCtttacaaatctaaaatgtttgtgactgtattataAgaacagaaatcccccttacacggaggcatcagtgaccaaacaggaaaaaaccgcccttaacatggcccgctttatcagaagccagacattaacgcttctggagaaactcaacgagctggacgcggatgaacaggcagacatctgtgaatcgcttcacgaccacgctgatgagctttaccgcagctgcctcgcgcgtttcggtgatgacggtgaaaacctctgacacatgcagcTCCCagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccataggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaaggacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccTTCG"       
//   
////GoldenGate 1                
//                ">Construction of pTarg1\n"+
//                "acquire oligo yyBla-F\n" +
//                "acquire oligo yyBla-R\n" +
//                "acquire oligo yyEI-F\n" +
//                "acquire oligo yyEI-R\n" +
//                "acquire plasmid p20N5\n" +
//                "acquire plasmid pTargetF\n" +
//                "pcr yyBla-F,yyBla-R on p20N5\t(1183 bp, fragA)\n" +
//                "pcr yyEI-F,yyEI-R on pTargetF\t(1174 bp, fragB)\n" +
//                "assemble fragA,fragB\t(BsaI, pTarg1)\n" +
//                "Transform pTarg1\t(Mach1, Amp)\n" +
//                "\n" +
//                ">yyBla-F\n" +
//                "atgctGGTCTCactcgctgaaattctgcctcgtg\n"+
//                ">yyBla-R\n"+
//                "agctaGGTCTCgattatcaaaaaggatcttcacc\n"+
//                ">yyEI-F\n"+    
//                "agtcaGGTCTCataatcgcgtaaaggatctaggtga\n"+ 
//                ">yyEI-R\n"+   
//                "tggtgGGTCTCtcgagtagggataacagggt\n"+ 
//                ">p20N5\n" +
//                "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGcgatcagatACTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt\n" +
//                ">pTargetF\n" +
//                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n"
//    
//////Gibson(succeed)
//">Construction of pTarg2\n" +
//"acquire oligo targAf\n" +
//"acquire oligo targAr\n" +
//"acquire oligo targBf\n" +
//"acquire oligo targBr\n" +
//"acquire plasmid p20N5\n" +
//"acquire plasmid pTargetF\n" +
//"PCR targAf,targAr on p20N5	(1200 bp, pcrA)\n" +
//"PCR targBf,targBr on pTargetF	(1172 bp, pcrB)\n" +
//"Assemble pcrA,pcrB	(Gibson, pTarg2)\n" +
//"Transform pTarg2	(Mach1, Amp)\n" +
//"\n" +
//">targAf\n" +
//"GAGTTCATGTGCAGCTCCATAAGCTGAAATTCTGCCTCGTGATAC\n" +
//">targAr\n" +
//"GTTAAGGGATTTTGGTCATGAGATTATCAAAAAGGATCTTC\n" +
//">targBf\n" +
//"GAAGATCCTTTTTGATAATCTCATGACCAAAATCCCTTAAC\n" +
//">targBr\n" +
//"GTATCACGAGGCAGAATTTCAGCTTATGGAGCTGCACATGAACTC\n" +
//">p20N5\n" +
//"attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGcgatcagatACTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt\n" +
//">pTargetF\n" +
//"catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n" 

//Wobble
//                ">Wobble\n"+
//                "acquire oligo ca9939\n"+
//                "acquire oligo ca9940\n"+
//                "Wobble ca9939/ca9940\t(107bp, wobpdt)\n"+
//                "Digest wobpdt\t(EcoRI/BamHI, L, wobdig)\n"+
//                "Digest pBca9145-Bca1144#5\t(EcoRI/BamHI, 2057+910, L, vectdig)\n"+
//                "Ligate wobdig + vectdig\t(pBca9145-Bca9939)\n"+
//                "\n"+
//                ">ca9939\n"+
//                "CCATAgaattcATGagatctGGGGCTATAGCTCAGCTGGGAGAGCGCCTGCTTCTAACGCAG\n"+
//                ">ca9940\n"+
//                "CTGATggatccTGGTGGAGCTATGCGGGATCGAACCGCAGACCTCCTGCGTTAGAAGCAGGCGCTC\n"
//

                
//                
////EIPCR                
//                ">EIPCR\n"+
//                "EIPCR ca9941/ca1168R on pBca9145-Bca1144#5\t(2108 bp, eipcr)\n"+
//                "Digest eipcr\t(BglII, L, pcrdig)\n"+
//                "Ligate pcrdig\t(pBca9145-Bca9941)\n"+
//                "\n"+
//                ">ca9941\n"+
//                "CCATAagatctAAAAAAAAAAAAAAAAAAAAggatcctaaCTCGAGctgcag\n"+
//                ">ca1168R\n"+
//                "CCAATAGATCTcatgaattccagaaatc\n"


                
////
//                ">Construction of KanR Basic Part Bca9128\n"+
//                "PCR ca1067F/R on pSB1AK3-b0015\t(1055bp, EcoRI/SpeI/DpnI)\n"+
//                "Sub into pSB1A2-I13521\t(EcoRI/SpeI, 2062+946, L)\n"+
//                "Product is pSB1A2-Bca9128\t[KanR]\n"+
//                "\n"+
//                ">ca1067F\n"+
//                "ccagtGAATTCgtccTCTAGAgagctgatccttcaactc\n"+
//                ">ca1067R\n"+ 
//                "gcagtACTAGTtccgtcaagtcagcgtaatg\n"                
//


                
                
//                
////BioBrick
//                ">Construction of GFP Biobrick 2.0 basic part\n"+
//                "PCR ca1123F/ca1123R on pSB1A2-I13522\t(748 bp, BglII/XhoI/DpnI)\n"+
//                "Sub into pBca1102\t(BglII/XhoI, 2159+697, L)\n"+
//                "Product is pBca1102-Bca1123\n"+
//                 "\n"+
//                ">ca1123F\n"+
//                "ctctgAGATCTatgcgtaaaggagaagaac\n"+
//                ">ca1123R\n"+
//                "gcaaaCTCGAGttaGGATCCttatttgtatagttcatccatgc\n"
        
        
        );

    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        resultArea = new javax.swing.JTextArea();
        clearBtn = new javax.swing.JButton();
        runBtn = new javax.swing.JButton();
        copyBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        cfPane = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        resultArea.setColumns(20);
        resultArea.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 13)); // NOI18N
        resultArea.setLineWrap(true);
        resultArea.setRows(5);
        resultArea.setText("attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggcatCAGTGCTTGCTCTtttacgacataaaaactttgtgttactatctcaatAggcgggtgctgcacctgtgagttttgtttattgATGAGCGGCTTACCTCTTATTTCGCGCCGACGACTGTTAACGGCGATGGCGCTTTCTCCGTTGTTATGGCAGATGAATACCGCCCACGCGACTAGACGTAATCCCACGCTGTTACAATGTTTTCACTGGTATTACCCGGAAGGCGGTAAGCTCTGGCCTGAACTGGCCGAGCGCGCCGACGGTTTTAATGATATTGGTATCAATATGGTCTGGTTGCCGCCCGCCTATAAAGGCGCATCGGGCGGGTATTCGGTCGGCTACGACTCCTATGATTTATTTGATTTAGGCGAGTTTGATCAGAAAGGCAGCATCCCTACTAAATATGGCGATAAAGCACAACTGCTGGCCGCCATTGATGCTCTGAAACGTAATGACATTGCGGTGCTGTTGGATGTGGTAGTCAACCACAAAATGGGCGCGGATGAAAAAGAAGCTATTCGCGTGCAGCGTGTAAATGCTGATGACCGTACGCAAATTGACGAAGAAATCATTGAGTGTGAAGGCTGGACGCGTTACACCTTCCCCGCCCGTGCCGGGCAATACTCGCAGTTTATCTGGGATTTCAAATGTTTTAGCGGTATCGACCATATCGAAAACCCTGACGAAGATGGCATTTTTAAAATTGTTAACGACTACACCGGCGAAGGCTGGAACGATCAGGTTGATGATGAATTAGGTAATTTCGATTATCTGATGGGCGAGAATATCGATTTTCGCAATCATGCCGTGACGGAAGAGATTAAATACTGGGCGCGCTGGGTGATGGAACAAACGCAATGCGACGGTTTTCGTCTTGATGCGGTCAAACATATTCCAGCCTGGTTTTATAAAGAGTGGATCGAACACGTACAGGAAGTTGCGCCAAAGCCGCTGTTTATTGTGGCGGAGTACTGGTCGCATGAAGTTGATAAGCTGCAAACGTATATTGATCAGGTGGAAGGCAAAACCATGCTGTTTGATGCGCCGCTCCAGATGAAATTCCATGAAGCATCGCGCATGGGGCGCGACTACGACATGACGCAGATTTTCACGGGTACATTAGTGGAAGCCGATCCTTTCCACGCCGTGACGCTCGTTGCCAATCACGACACCCAACCGTTGCAAGCCCTCGAAGCGCCGGTCGAACCGTGGTTTAAACCGCTGGCGTATGCCTTAATTTTGTTGCGGGAAAATGGCGTTCCTTCGGTATTCTATCCGGACCTCTACGGTGCGCATTACGAAGATGTCGGTGGTGACGGGCAAACCTATCCGATAGATATGCCAATAATCGAACAGCTTGATGAGTTAATTCTCGCCCGTCAGCGTTTCGCCCACGGTGTACAGACGTTATTTTTCGACCATCCGAACTGCATTGCCTTTAGCCGCAGTGGCACCGACGAATTTCCCGGCTGCGTGGTGGTCATGTCGAACGGGGATGATGGCGAAAAAACCATTCATCTGGGAGAGAATTACGGCAATAAAACCTGGCGTGATTTTCTGGGGAACCGGCAAGAGAGAGTAGTGACCGACGAAAACGGCGAAGCAACCTTCTTTTGCAACGGCGGCAGCGTCAGCGTGTGGGTTATCGAAGAGGTGATTTAAagtcggaagcctgcaaTGTCccagACTAGAATAAAGAAAAAGGGAGCCCATGGGCTCCCTTAATTTAAAATGGTTGTCTTAAGAACGACTTCTTTACGTTTTTGCTTCCGTGTGGTACTATGGGAGCAGGATCTGTCCCGGACGAGGTTCGACGACGAAACGAGGTGCCAGGATGAGCAAGCCCGACGACCCCGCCTACCACTGGAACGGCGCGGAGCTGGACCTGGACGCGTACCTGGCCAGGATCGGGTTCGCCGGCGAGCGCGCGCCCACCCTGGCCACGCTGCGTGAACTCGTGTACCGGCACACCACCGCGATCCCCTTCGAGAATCTGGAGGCCGTGCTCGGCCGGCCCGTGCGCCTCGACCTGGCGACCCTACAAGACAAGCTCGTGCACAGCAGACGCGGCGGCTACTGCTACGAGAACGCCGGCCTGTTCGCCGCCGCGCTGGAACGACTCGGGTTCGGTGTCACCGGCCACACCGGCCGGGTCACCATGGGGGCGGGCGGCCTACGCCCGGCCACGCACGCGCTGTTACGGGTCACCACCGCCGACGACGACCGGGTGTGGATGTGCGACGTCGGCTTCGGCCGCGGCCCGCTGCGCCCCTACGAGCTGCGCCCGCAACCGGACGAGTTCACCCTGGGGGACTGGCGTTTCCGGCTGGAGCGCCGCACCGGCGAACTCGGCACCGACCTGTGGGTGCTGCACCAGTTCGGCAGGGACGGCTGGGTGGACCGCTACACCTTCACCACCGCGCCGCAGTACCGGATCGACTTCGAGGTCGGCAACCACTTCGTTTCGACCTCGCCGCGCTCGCCGTTCACGACCCGTCCGTTCCTCCAGCGGTTCCATTCCGACCGCCACCACGTGCTCGACGGACTGACCCTGATCACCGAACGCCCCGACGGCAGCGCCGACATCCGTGCCCTGACACCGGGCGAGTTGCCCGAGGTCATCAACGAGCTCTTCGACATCGAACTGCCCGGCCCGGACCTGGATGCGCTCACCACCGGCAGCTGGCTGGAGCGCGTCGCTGCGGGCACCCCGTAACAATTCccctACTAGACCATAATCATAAGGGGCTTCGGCCCCTTTCTTCATTTTGAGCGAATAATACCTCAGAGCGTTTACAGCTAGCTCAGTCCTAGGGACTGTGCTAGCAGGATCTACTTCAGTTAAGGAGGTAAAAAAAATGTCTCAGCAGGAACGTACCCGTGTTGCTATCGTTGGTGCTGGTATCGTTGGTCTGACCCTGGCTATCGCTCTGAACGCTTTCGACAAAGAACGTAAACTGGCTATCGACATCTACGAAAACGCTTCTGAACTGGCTGAAATCGGTGCTGGTATCAACGTTTGGCCGCGTACCCTGGCTATCTTCAAACAGATCGGTGTTGAAGACGCTCTGATCCCGCTGCTGGACCACATCCCGGACCTGGAACCGCGTATCATCTTCGGTATCCGTAAAGGTGACGAAAAAAACGGTTACCAGGTTTACGACACCATGAACAACGGTGGTGCTCTGCGTGTTCACCGTGCTCACCTCCAGAACACCCTGATCCAGCACCTGCCGCTGCCGGGTTCTAAAGTTACCGAAATCAACTCTATCTGCGGTTTCCACCTGGGTCACAACCTGATCGACTACTCTCACCACTCTTCTTCTGGTCAGGGTCCGCTGACCCTGCACTTCTCTGACGGTAAACCGTCCCGTACCTGCGACATCCTGGTTGGTGCTGACGGTATCAAATCTACCCTGCGTCACCTGTTCCTGCCGCGTCTGCCGAACCCGGAAAAATACCTGAACTGCTACGAACCGAAATGGAAAGGTCTGCTGGCTTACCGTGGTCTGGTTCCGAAAGAAAAACTGGAAGCTGTTTCTCCGGGTCACCGTGCTCTGACCCACCCGGGTCTGATGTACTCTGGTAAATCTGCTTACGCTGTTGTTTACCCGGTTTCTAACGGTAAATTCATCAACGTTGTTGCTATCGTTCACGACAACCCGACCAACTCTACCGTTTGGCCGGGTCCGTGGCGTATGGACGTTACCCAGTCTGAGTTCTTCGAAGTTTACAAAGGTTGGGACGAAGAAGTTCTGGACCTGATCCGTTGCGTTGACAAACCGACCAAATGGGCTCTGCACGCTCTGGACCACCTGGACGTTTACGCTAAGGGCCGTGTGTTCCTGATGGGGGACGCTGCTCACGCTATGCTGCCGCACCTGGGTGCTGGTGCTCACGTTGGTATGGAAGACGCTTACATCCTGGCTTCTCTGATCACCCACTCTTCTACCCCGATCTGGCCGTCTACCCAGCACGTTTCTGAAATCGCTAACATCTACAACACCATGCGTATCCCACGTGCTGTGAGCATGTCTAACTCTACCGACGAAGCTGGTTACCTGTGCAACCTGGAAAACCCGGGTCTGGAAGAGTTCAAAGTTGGTGACCACATCCCGAAAGAACTGCTGATCCAGACCGCTCGTACCATGGAAAAAAAATGGGCTTGGACCACCACCTACGCTGACGAAGACCGTATCAAAGCTATCTCTCTGCTGGAAGGTCCGCGTGCTGTTCTGTAACAATTCagcaACTAGAGTTAAATAAAAAGGGACCGAAAGGTCCCTTTGTTTTATTCATGATTCTATAAGATTGCACTATTGACATGATAGAAGCACTCTACTATAATCTCAATAGGATCTCCGGCGGTGCCTTCACCCATTCTTAAAAGGATTCCACGAAGGACCCCCGTATGCGCGTACTGATCATCGACAATTATGATTCCTTCACCTTCAACCTCGCCACGTATGTGGAAGAGGTCACCGGTGCGGCACCGACGGTGGTGCCCAATGATGCGCAGATCGACGAAACGCTGTTCGACGCCGTCATCATCTCACCCGGCCCCGGCCACCCCGGGGTGGCGGCGGATTTCGGCAGCTGCCGCGGGGTGATCGAACGTGGTCTGGTCCCGGTCCTCGGGGTGTGCCTGGGGCATCAGGGCATCGCGCTCGCCCACGGCGGCGCGGTGGGCCCGGCGCCGGTCCCGGTGCACGGTCAGGTCACCCGCATCCACCACGACGGGTCCGAGCTTTTCGACGCCATCCCGCCCCAGTTCGACGCCGTCCGTTACCACTCCCTGGTGGCCACCGATCTGCCGCCGGAACTGGAGGTCACCGCGAGGACCGGGGACGGGCTGATCATGGCGCTGCGCCACCGCGAGCTGCCCCAGTGGGGTGTGCAGTTCCACCCGGAATCCATCGGCGGGCAGTTCGGGCACCGGATCATGGCGAATTTCCTGAGCCTGGCGCGACGACAAGCGCACCGGTGGGAGATCACCGAGCATGTGGTGGAGACAAGCGTGGACCCGGCGGCGGTGTTCGAAACGCTCTTCGCCGGGTCGGAGCACGCGTTCTGGCTCGATGATCCGCAGGGCACCACCTATATGGGTGATGCCTCCGGGCCACATGCACGAATCCGCACCCACCGGGTGGGGGAGGGGGAGCTGTTCGACTGGCTGCGTGATGATCTGCGTCGCAACCGCGTGGCCCCGGGGGTGGGTTTCCGCCTGGGGTGGGTGGGATACCTCGGGTATGAGATGAAGGCCGAATGCGGGGTGGACAATCGGCACGCCTCATCGCATCCCGATGCCCACCTGATCTTCGCCGACCGGGCCATCGCCATCGAACCCGGCCGCGTGTGGCTCATGGCGCTCGGTGAGCAGGGGGAGTGGTTCGCGGAGATGACCGCCGCCCTGGGGCAGCTGCGCCCACCCCGTGCCGCTGCCGCCCCGGCCGCCCAGCTCACCGTCCGGGATGACCGCGACAGCTACCTGGACATGATCGCCCGCGCCCAGGAGTTGATCACCCGCGGGGAGTCCTATGAAATCTGCCTGACCACCCAGCTGCGCGCGGAGGTGGAGGTTGATCCCCTCGCCGCCTATCTGGCGCTGCGGGCGGCCAACCCCACCTCCTATGGATCATTCCTCCAGCTGGGGGAGATGGCGGTGCTGAGCTCGTCGCCGGAACGGTTCATCACCATCGACGCCTCCGGACGTGTGGAATCCAAACCCATCAAGGGCACCCGCCCGCGGGGCAGTACCGAGCAGGAGGATGCGGCCTTGATCGCCGATCTCACCGACAACCCCAAGGACCGCGCCGAGAACCTCATGATCGTGGACCTGGTCCGCAATGATCTCGCCCGCGGGGCGCAACCGGCAACGGTTCAGGTGGAGAAACTTTTCGACGTCGAAACCTACGCCACCGTCCACCAGCTCGTCAGCACCATTACCGCCCAGCTGGAGGGTAAGGACCCCATTGACTGCGTGCGGGCGGCGTTCCCCGGTGGTTCCATGACCGGTGCACCGAAGATCCGCACAATGGAGATCATCGACGAGCTGGAAACCGGCCCCCGTGGTGTCTATTCCGGTGGCCTGGGCTATTTCTCCCTTGATGGGGCGGTGGATCTGTCGATGGTGATCCGCACCGTGGTCTACACCCCCGGCGTCCTGGAGTACGGGGTCGGCGGGGCGATCCTGGCCCTGTCGGACCCCGCCGCAGAGTGGGAAGAGATCCGGGTGAAATCGAGGCCCCTGCTGGGTCTGCTCGGGGTGGAGTTCCCGTAAAATTTTCGTACTGAAACATCTTAATCATGCTGCGGAGGGTTTCTAATGTTCTTAATTAACGGTCATAAGCAGGAATCGCTGGCAGTAAGCGATCGGGCAACGCAGTTTGGTGATGGTTGTTTTACCACCGCCAGAGTTATCGACGGTAAAGTCAGTTTGTTATCGGCGCATATCCAGCGACTACAGGATGCTTGTCAGCGGTTGATGATTTCCTGTGACTTCTGGCCTCAGCTTGAACAAGAGATGAAAACGCTGGCAGCAGAACAGCAAAATGGTGTGCTGAAAGTCGTGATCAGTCGCGGTAGTGGCGGGCGAGGGTACAGCACATTGAACAGCGGACCGGCAACGCGGATTCTCTCCGTTACGGCTTATCCTGCACATTACGACCGTTTGCGTAACGAGGGGATTACGTTGGCGCTAAGCCCGGTGCGGCTGGGGCGCAATCCTCATCTTGCAGGTATTAAACATCTCAATCGTCTTGAGCAAGTATTGATTCGCTCTCATCTTGAGCAGACAAACGCTGATGAGGCGCTGGTCCTTGACAGCGAAGGGTGGGTTACGGAATGCTGTGCGGCTAATTTGTTCTGGCGGAAGGGCAACGTAGTTTATACGCCGCGACTGGATCAGGCCGGTGTTAACGGCATTATGCGACAATTCTGTATCCGTTTGCTGGCACAATCCTCTTATCAGCTTGTCGAAGTGCAAGCCTCTCTGGAAGAGTCGTTGCAGGCAGATGAGATGGTTATTTGTAATGCGTTAATGCCAGTGATGCCCGTATGTGCCTGTGGCGATGTCTCCTTTTCGTCAGCAACGTTATATGAATATTTAGCCCCACTTTGTGAGCGCCCGAATTAACAATTCgttcGTCAccagACTAGATATAAATGATAGGGAGCCTTCGGGCTCCCTTTTTTATTTCAACTAGATGCAGTGCTTGCTCTtttacaacataaaaactttgtgtgactgtctcaatAGGATCTatcctcgctgaggatcaactatcgcaaacgagcataaacaggatcgccatcATGCAAAAAGACGCGCTGAATAACGTACATATTACCGACGAACAGGTTTTAATGACTCCGGAACAACTGAAGGCCGCTTTTCCATTGAGCCTGCAACAAGAAGCCCAGATTGCTGACTCGCGTAAAAGCATTTCAGATATTATCGCCGGGCGCGATCCTCGTCTGCTGGTAGTATGTGGTCCTTGTTCCATTCATGATCCGGAAACTGCTCTGGAATATGCTCGTCGATTTAAAGCCCTTGCCGCAGAGGTCAGCGATAGCCTCTATCTGGTAATGCGCGTCTATTTTGAAAAACCCCGTACCACTGTCGGCTGGAAAGGGTTAATTAACGATCCCCATATGGATGGCTCTTTTGATGTAGAAGCCGGGCTCCAGATCGCGCGTAAATTGCTGCTTGAGCTGGTGAATATGGGACTGCCACTGGCGACGGAAGCGCTGGATCTGAATAGCCCGCAATACCTGGGCGATCTGTTTAGCTGGTCAGCCATTGGTGCTCGTACAACGGAATCGCAAACTCACCGTGAAATGGCCTCCGGGCTTTCCATGCCGGTTGGTTTTAAAAACGGCACCGACGGCAGTCTGGCAACAGCAATTAACGCTATGCGCGCCGCCGCCCAGCCGCACCGTTTTGTTGGCATTAACCAGGCAGGGCAGGTTGCGTTGCTACAAACTCAGGGGAATCCGGACGGCCATGTGATCCTGCGCGGTGGTAAAGCGCCGAACTATAGCCCTGCGGATGTTGCGCAATGTGAAAAAGAGATGGAACAGGCGGGACTGCGCCCTTCTCTGATGGTAGATTGCAGCCACGGTAATTCCAATAAAGATTATCGCCGTCAGCCTGCGGTGGCAGAATCCGTGGTTGCTCAAATCAAAGATGGCAATCGCTCAATTATTGGTCTGATGATCGAAAGTAATATCCACGAGGGCAATCAGTCTTCCGAGCAACCGCGCAGTGAAATGAAATACGGTGTATCCGTAACCGATGCCTGCATTAGCTGGGAAATGACCGATGCCTTGCTGCGTGAAATTCATCAGGATCTGAACGGGCAGCTGACGGCTCGCGTGGCTTAACAATTCccctACTAGAGCATAAGTTGAGGACTCCTTCGGGAGTCCTTTTTTATTTTCCCAGGTGGTATGGAAGCTATCtttacatcttcagtattatgtggtattatcgagtcAGGATCTggctttcgccgcattgcgacctattggggaaaacccacgATGACACAACCTCTTTTTCTGATCGGGCCTCGGGGCTGTGGTAAAACAACGGTCGGAATGGCCCTTGCCGATTCGCTTAACCGTCGGTTTGTCGATACCGATCAGTGGTTGCAATCACAGCTCAATATGACGGTCGCGGAGATCGTCGAAAGGGAAGAGTGGGCGGGATTTCGCGCCAGAGAAACGGCGGCGCTGGAAGCGGTAACTGCGCCATCCACCGTTATCGCTACAGGCGGCGGCATTATTCTGACGGAATTTAATCGTCACTTCATGCAAAATAACGGGATCGTGGTTTATTTGTGTGCGCCAGTATCAGTCCTGGTTAACCGACTGCAAGCTGCACCGGAAGAAGATTTACGGCCAACCTTAACGGGAAAACCGCTGAGCGAAGAAGTTCAGGAAGTGCTGGAAGAACGCGATGCGCTATATCGCGAAGTTGCGCATATTATCATCGACGCAACAAACGAACCCAGCCAGGTGATTTCTGAAATTCGCAGCGCCCTGGCACAGACGATTAATTGTTAACAATTCagcaACTAGAACTAATTAATTGGGGACCCTAGAGGTCCCCTTTTTTATTTTAAGTTCCTCACACTACGTCATGtttacattaactttaatttgtgttattattagcatAGGATCTgtttttatttctgttgtagagagttgagttcATGGAATCCCTGACGTTACAACCCATCGCTCGTGTCGATGGCACTATTAATCTGCCCGGTTCCAAGAGCGTTTCTAACCGCGCTTTATTGCTGGCGGCATTAGCACACGGCAAAACAGTATTAACCAATCTGCTGGATAGCGATGACGTGCGCCATATGCTGAATGCATTAACAGCGTTAGGGGTAAGCTATACGCTTTCAGCCGATCGTACGCGTTGCGAAATTATCGGTAACGGCGGTCCATTACACGCAGAAGGTGCCCTGGAGTTGTTCCTCGGTAACGCCGGAACGGCAATGCGTCCGCTGGCGGCAGCTCTTTGTCTGGGTAGCAATGATATTGTGCTGACCGGTGAGCCGCGTATGAAAGAACGCCCGATTGGTCATCTGGTGGATGCGCTGCGCCTGGGCGGGGCGAAGATCACTTACCTGGAACAAGAAAATTATCCGCCGTTGCGTTTACAGGGCGGCTTTACTGGCGGCAACGTTGACGTTGATGGCTCCGTTTCCAGCCAATTCCTCACCGCACTGTTAATGACTGCGCCTCTTGCGCCGGAAGATACGGTGATTCGTATTAAAGGCGATCTGGTTTCTAAACCTTATATCGACATCACACTCAATCTGATGAAGACGTTTGGTGTTGAAATTGAAAATCAGCACTATCAACAATTTGTCGTAAAAGGCGGGCAGTCTTATCAGTCTCCGGGTACTTATTTGGTCGAAGGCGATGCATCTTCGGCTTCTTACTTTCTGGCAGCAGCAGCAATCAAAGGCGGCACTGTAAAAGTGACCGGTATTGGACGTAACAGTATGCAGGGTGATATTCGCTTTGCTGATGTGCTGGAAAAAATGGGCGCGACCATTTGCTGGGGCGATGATTATATTTCCTGCACGCGTGGTGAACTGAACGCTATTGATATGGATATGAACCATATTCCTGATGCGGCGATGACCATTGCCACGGCGGCGTTATTTGCAAAAGGCACCACCACGCTGCGCAATATCTATAACTGGCGTGTTAAAGAAACCGATCGCCTGTTTGCGATGGCAACAGAACTGCGTAAAGTCGGCGCGGAAGTGGAAGAGGGGCACGATTACATTCGTATCACACCTCCGGAAAAACTGAACTTTGCCGAGATCGCGACATACAATGATCACCGGATGGCGATGTGTTTCTCGCTGGTGGCGTTGTCAGATACACCAGTGACGATTCTTGATCCCAAATGCACGGCCAAAACATTTCCGGATTATTTCGAGCAGCTGGCGCGGATTAGCCAGGCAGCCTAACAATTCgttcCAAGccagAGAGACCCCTCCTCACTAGTCGAATAACATTAGTCTCCTTCGGGAGACTtTTTTTCATTTTACCAGCCACGTATCGCCAGATGtttacatttaatgataatgtattgactgtaacagaAGGATCCaaataggagaaatactagATGTCTTATTCAAAGCATGGCATCGTACAAGAAATGAAGACGAAATACCATATGGAAGGCAGTGTCAATGGCCATGAATTTACGATCGAAGGTGTAGGAACTGGGTACCCTTACGAAGGGAAACAGATGTCCGAATTAGTGATCATCAAGCCTGCGGGAAAACCCCTTCCATTCTCCTTTGACATACTGTCATCAGTCTTTCAATATGGAAACCGTTGCTTCACAAAGTACCCGGCAGACATGCCTGACTATTTCAAGCAAGCATTCCCAGATGGAATGTCATATGAAAGGTCATTTCTATTTGAGGATGGAGCAGTTGCTACAGCCAGCTGGAACATTCGACTCGAAGGAAATTGCTTCATCCACAAATCCATCTTTCATGGCGTAAACTTTCCCGCTGATGGACCCGTAATGAAAAAGAAGACCATTGACTGGGATAAGTCCTTCGAAAAAATGACTGTGTCTAAAGAGGTGCTAAGAGGTGACGTGACTATGTTTCTTATGCTCGAAGGAGGTGGTTCTCACAGATGCCAATTTCACTCCACTTACAAAACAGAGAAGCCGGTCACACTGCCCCCGAATCATGTCGTAGAACATCAAATTGTGAGGACCGACCTTGGCCAAAGTGCAAAAGGCTTTACAGTCAAGCTGGAAGCACATGCCGCGGCTCATGTTAACCCTTTGAAGGTTAAATAAtaaAGATCCGAGGAGGTCTCAgttcACGTGACCCCTGCTCcagaaatcTGAAATAAAAAAGGGAGCCCGAAGGCTCCCTaTCATTTATAATAAatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctagtggAAAGCCACGTTGTGTCTCAAAATCTCTGATGTTACATTGCACAAGATAAAAATATATCATCATGAACAATAAAACTGTCTGCTTACATAAACAGTAATACAAGGGGTGTTATGAGCCATATTCAACGGGAAACGTCTTGTTCGAGGCCGCGATTAAATTCCAACATGGACGCTGATTTATATGGGTATAAATGGGCTCGCGATAATGTCGGGCAATCAGGTGCGACAATCTATCGATTGTATGGGAAGCCCGATGCGCCAGAGTTGTTTCTGAAACATGGCAAAGGTAGCGTTGCCAATGATGTTACAGATGAGATGGTCAGACTAAACTGGCTGACGGAATTTATGCCTCTTCCGACCATCAAGCATTTTATCCGTACTCCTGATGATGCATGGTTACTCACCACTGCGATTCCGGGGAAAACAGCATTCCAGGTATTAGAAGAATATCCTGATTCAGGTGAAAATATTGTTGATGCGCTGGCAGTGTTCCTGCGCCGGTTGCATTCGATTCCTGTTTGTAATTGTCCTTTTAACAGCGATCGCGTATTTCGACTCGCTCAGGCGCAATCACGAATGAATAACGGTTTGGTTGATGCGAGTGATTTTGATGACGAGCGTAATGGCTGGCCTGTTGAACAAGTCTGGAAAGAAATGCATAAACTTTTGCCATTCTCACCGGATTCAGTCGTCACTCATGGTGATTTCTCACTTGATAACCTTATTTTTGACGAGGGGAAATTAATAGGTTGTATTGATGTTGGACGAGTCGGAATCGCAGACCGATACCAGGATCTTGCAATCCTATGGAACTGCCTCGGTGAGTTTTCTCCTTCATTACAGAAACGGCTTTTTCAAAAATATGGTATTGATAATCCTGATATGAATAAATTGCAGTTTCATTTGATGCTCGATGAGTTTTTCTAACTGActgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt");
        jScrollPane2.setViewportView(resultArea);

        clearBtn.setText("clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        runBtn.setText("run");
        runBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runBtnActionPerformed(evt);
            }
        });

        copyBtn.setText("copy result");
        copyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyBtnActionPerformed(evt);
            }
        });

        cfPane.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 13)); // NOI18N
        cfPane.setToolTipText("");
        jScrollPane3.setViewportView(cfPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(copyBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runBtn))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearBtn)
                    .addComponent(runBtn)
                    .addComponent(copyBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runBtnActionPerformed
        // TODO add your handling code here:
        String cfText = cfPane.getText();
        ParseConstructionFile parseConstructionFile = new ParseConstructionFile();
        parseConstructionFile.initiate();
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        simulateConstructionFile.initiate();
        Polynucleotide result;
        try {
            ConstructionFile cf = parseConstructionFile.run(cfText);
            result = simulateConstructionFile.run(cf);
            resultArea.setText(result.getSequence());
        }
        catch (Exception e){
            resultArea.setText(e.toString());
            e.printStackTrace();
        }

    }//GEN-LAST:event_runBtnActionPerformed

    private void copyBtnActionPerformed(java.awt.event.ActionEvent evt)  {//GEN-FIRST:event_copyBtnActionPerformed
        try {
            StringSelection stringSelection = new StringSelection(resultArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
        catch (Exception e) {
            resultArea.setText("Exception occurred during copy to clipboard, should not have happened.");
        }
    }//GEN-LAST:event_copyBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        cfPane.setText("");
    }//GEN-LAST:event_clearBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])  {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimulatorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimulatorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimulatorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimulatorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SimulatorView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane cfPane;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton copyBtn;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea resultArea;
    private javax.swing.JButton runBtn;
    // End of variables declaration//GEN-END:variables
}
