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

        cfPane.setText(">Construction of pTarg2\n" +
                "acquire oligo targAf\n" +
                "acquire oligo targAr\n" +
                "acquire oligo targBf\n" +
                "acquire oligo targBr\n" +
                "acquire plasmid p20N5\n" +
                "acquire plasmid pTargetF\n" +
                "PCR targAf,targAr on p20N5\t(1200 bp, pcrA)\n" +
                "PCR targBf,targBr on pTargetF\t(1172 bp, pcrB)\n" +
                "Assemble pcrA,pcrB\t(Gibson, pTarg2)\n" +
                "Transform pTarg2\t(Mach1, Amp)\n" +
                "\n" +
                ">targAf\n" +
                "GAGTTCATGTGCAGCTCCATAAGCTGAAATTCTGCCTCGTGATAC\n" +
                ">targAr\n" +
                "GTTAAGGGATTTTGGTCATGAGATTATCAAAAAGGATCTTC\n" +
                ">targBf\n" +
                "GAAGATCCTTTTTGATAATCTCATGACCAAAATCCCTTAAC\n" +
                ">targBr\n" +
                "GTATCACGAGGCAGAATTTCAGCTTATGGAGCTGCACATGAACTC\n" +
                ">p20N5\n" +
                "attaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcctgcaaCTCGAGcgatcagatACTAGTaaccaatataccaaataaagagttgaggacgtcaaggATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAGGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGATTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAGGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAAGAATTCccctAGAGACCCCTCCTCcagaaatcatccttagcgaaagctaaggattttttttatctgaaattctgcctcgtgatacgcctatttttataggttaatgtcatgataataatggtttcttagacgtcaggtggcacttttcggggaaatgtgcgcggaacccctatttgtttatttttctaaatacattcaaatatgtatccgctcatgagacaataaccctgataaatgcttcaataatattgaaaaaggaagagtatgagtattcaacatttccgtgtcgcccttattcccttttttgcggcattttgccttcctgtttttgctcacccagaaacgctggtgaaagtaaaagatgctgaagatcagttgggtgcacgagtgggttacatcgaactggatctcaacagcggtaagatccttgagagttttcgccccgaagaacgttttccaatgatgagcacttttaaagttctgctatgtggcgcggtattatcccgtattgacgccgggcaagagcaactcggtcgccgcatacactattctcagaatgacttggttgagtactcaccagtcacagaaaagcatcttacggatggcatgacagtaagagaattatgcagtgctgccataaccatgagtgataacactgcggccaacttacttctgacaacgatcggaggaccgaaggagctaaccgcttttttgcacaacatgggggatcatgtaactcgccttgatcgttgggaaccggagctgaatgaagccataccaaacgacgagcgtgacaccacgatgcctgtagcaatggcaacaacgttgcgcaaactattaactggcgaactacttactctagcttcccggcaacaattaatagactggatggaggcggataaagttgcaggaccacttctgcgctcggcccttccggctggctggtttattgctgataaatctggagccggtgagcgtggCtctcgcggtatcattgcagcactggggccagatggtaagccctcccgtatcgtagttatctacacgacggggagtcaggcaactatggatgaacgaaatagacagatcgctgagataggtgcctcactgattaagcattggtaactgtcagaccaagtttactcatatatactttagattgatttaaaacttcatttttaatttaaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctcacatgttctttcctgcgttatcccctgattctgtggataaccgt\n" +
                ">pTargetF\n" +
                "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n");
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
