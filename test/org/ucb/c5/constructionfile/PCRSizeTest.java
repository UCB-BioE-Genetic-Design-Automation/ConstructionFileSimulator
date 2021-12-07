package org.ucb.c5.constructionfile;
import org.ucb.c5.constructionfile.*;
import java.util.HashMap;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Polynucleotide;
import org.junit.Test;

public class PCRSizeTest {

    @Test
    public void PCRSizeTest1() throws Exception {
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();

        String text = ">Construction of pTarget-aspC1\n"
                + "acquire oligo aspC1\n"
                + "acquire oligo pTargRev\n"
                + "acquire plasmid pTargetF\n"
                + "pcr aspC1,pTargRev on pTargetF	(3927 bp, ipcr)\n"
                + "digest ipcr with SpeI 	(1,spedig)\n"
                + "ligate spedig	(lig)\n"
                + "transform lig	(Mach1, Spec)\n"
                + "\n"
                + ">aspC1\n"
                + "ccataACTAGTacgcgtagctgccgatttccGTTTTAGAGCTAGAAATAGCAAG\n"
                + ">pTargRev\n"
                + "ctcagACTAGTattatacctaggactgagctag\n"
                + ">pTargetF\n"
                + "catgttctttcctgcgttatcccctgattctgtggataaccgtattaccgcctttgagtgagctgataccgctcgccgcagccgaacgaccgagcgcagcgagtcagtgagcgaggaagcggaagagcgcctgatgcggtattttctccttacgcatctgtgcggtatttcacaccgcatatgctggatccttgacagctagctcagtcctaggtataatactagtcatcgccgcagcggtttcaggttttagagctagaaatagcaagttaaaataaggctagtccgttatcaacttgaaaaagtggcaccgagtcggtgctttttttgaattctctagagtcgacctgcagaagcttagatctattaccctgttatccctactcgagttcatgtgcagctccataagcaaaaggggatgataagtttatcaccaccgactatttgcaacagtgccgttgatcgtgctatgatcgactgatgtcatcagcggtggagtgcaatgtcatgagggaagcggtgatcgccgaagtatcgactcaactatcagaggtagttggcgtcatcgagcgccatctcgaaccgacgttgctggccgtacatttgtacggctccgcagtggatggcggcctgaagccacacagtgatattgatttgctggttacggtgaccgtaaggcttgatgaaacaacgcggcgagctttgatcaacgaccttttggaaacttcggcttcccctggagagagcgagattctccgcgctgtagaagtcaccattgttgtgcacgacgacatcattccgtggcgttatccagctaagcgcgaactgcaatttggagaatggcagcgcaatgacattcttgcaggtatcttcgagccagccacgatcgacattgatctggctatcttgctgacaaaagcaagagaacatagcgttgccttggtaggtccagcggcggaggaactctttgatccggttcctgaacaggatctatttgaggcgctaaatgaaaccttaacgctatggaactcgccgcccgactgggctggcgatgagcgaaatgtagtgcttacgttgtcccgcatttggtacagcgcagtaaccggcaaaatcgcgccgaaggatgtcgctgccgactgggcaatggagcgcctgccggcccagtatcagcccgtcatacttgaagctagacaggcttatcttggacaagaagaagatcgcttggcctcgcgcgcagatcagttggaagaatttgtccactacgtgaaaggcgagatcaccaaggtagtcggcaaataagatgccgctcgccagtcgattggctgagctcataagttcctattccgaagttccgcgaacgcgtaaaggatctaggtgaagatcctttttgataatctcatgaccaaaatcccttaacgtgagttttcgttccactgagcgtcagaccccgtagaaaagatcaaaggatcttcttgagatcctttttttctgcgcgtaatctgctgcttgcaaacaaaaaaaccaccgctaccagcggtggtttgtttgccggatcaagagctaccaactctttttccgaaggtaactggcttcagcagagcgcagataccaaatactgtccttctagtgtagccgtagttaggccaccacttcaagaactctgtagcaccgcctacatacctcgctctgctaatcctgttaccagtggctgctgccagtggcgataagtcgtgtcttaccgggttggactcaagacgatagttaccggataaggcgcagcggtcgggctgaacggggggttcgtgcacacagcccagcttggagcgaacgacctacaccgaactgagatacctacagcgtgagctatgagaaagcgccacgcttcccgaagggagaaaggcggacaggtatccggtaagcggcagggtcggaacaggagagcgcacgagggagcttccagggggaaacgcctggtatctttatagtcctgtcgggtttcgccacctctgacttgagcgtcgatttttgtgatgctcgtcaggggggcggagcctatggaaaaacgccagcaacgcggcctttttacggttcctggccttttgctggccttttgctca\n";
        ConstructionFile CF = pCF.run(text);
        SimulateConstructionFile simulateConstructionFile = new SimulateConstructionFile();
        ConstructionFile outputConstructionFile = simulateConstructionFile.run(CF, new HashMap<>());
        Polynucleotide product = outputConstructionFile.getSequences().get(outputConstructionFile.getPdtName());
        Integer actualProductSize = product.getSequence().length();
        Integer expectSize = 2117;
        boolean temp = expectSize.equals(actualProductSize);
        assert (temp == true);

    }
}
