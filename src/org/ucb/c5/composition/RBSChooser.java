package org.ucb.c5.composition;

import org.ucb.c5.composition.model.RBSOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a simple RBS selection algorithm. Provided a protein sequence,
 * it arbitrarily selects an RBS to include from a list of options. It also
 * allows the user to supply a list of RBSOptions to exclude in making the 
 * selection
 *
 * @author J. Christopher Anderson
 * 
 * TODO:  REPLACE WITH YOUR VERSION
 */
public class RBSChooser {

    private List<RBSOption> rbsOptions;

    public void initiate() throws Exception {
        //Populate with some RBS choices
        rbsOptions = new ArrayList<>();

        //From http://parts.igem.org/Part:BBa_I13521
        RBSOption opt1 = new RBSOption(
            "BBa_b0034",
            "RBS based on Elowitz repressilator",
            "aaagaggagaaatactag",
            "atggcttcctccgaagacgttatcaaagagttcatgcgtttcaaagttcgtatggaaggttccgttaacggtcacgagttcgaaatcgaaggtgaaggtgaaggtcgtccgtacgaaggtacccagaccgctaaactgaaagttaccaaaggtggtccgctgccgttcgcttgggacatcctgtccccgcagttccagtacggttccaaagcttacgttaaacacccggctgacatcccggactacctgaaactgtccttcccggaaggtttcaaatgggaacgtgttatgaacttcgaagacggtggtgttgttaccgttacccaggactcctccctgcaagacggtgagttcatctacaaagttaaactgcgtggtaccaacttcccgtccgacggtccggttatgcagaaaaaaaccatgggttgggaagcttccaccgaacgtatgtacccggaagacggtgctctgaaaggtgaaatcaaaatgcgtctgaaactgaaagacggtggtcactacgacgctgaagttaaaaccacctacatggctaaaaaaccggttcagctgccgggtgcttacaaaaccgacatcaaactggacatcacctcccacaacgaagactacaccatcgttgaacagtacgaacgtgctgaaggtcgtcactccaccggtgcttaa",
            "MASSED");
        rbsOptions.add(opt1);
        
        //http://parts.igem.org/wiki/index.php?title=Part:BBa_K156033
        RBSOption opt2 = new RBSOption(
            "BBa_b0032",
            "RBS.3 (medium) -- derivative of BBa_0030",
            "tcacacaggaaagtactag",
            "atgactcaacgtatcgcatatgtaactggtggtatgggtggtatcggtactgcaatttgccagcgtctggcgaaagacggtttccgtgttgttgcgggctgcggtccgaactccccgcgtcgtgaaaagtggctggaacaacagaaagccctgggcttcgacttcattgcctccgagggtaatgtagctgactgggattccaccaagactgccttcgataaagttaaatctgaagtgggcgaagtagatgtactgatcaacaacgccggtattactcgtgatgtcgtattccgcaaaatgacccgtgcagactgggatgcagttatcgacaccaacctgacgtctctgttcaacgttaccaaacaggttattgatggtatggctgaccgtggctggggccgcatcgtgaacatctctagcgttaacggccaaaaaggccaatttggtcagacgaattacagcacggctaaagcaggcctgcacggtttcaccatggcactggcgcaggaagtggcgaccaaaggtgttaccgttaataccgtttctccaggttacatcgccaccgatatggttaaggctatccgccaagatgttctggacaagatcgtggctaccattccggttaaacgcctgggcctgccggaagaaattgcgtccatctgtgcgtggctgagctccgaagagtctggtttttccaccggtgcggatttctctctgaacggtggtctgcacatgggttga",
            "MTQRIA");
        rbsOptions.add(opt2);
        
        RBSOption opt3 = new RBSOption(
            "Pbad_rbs",
            "RBS from pBADmychisA with T7RNAP inserted",
            "CCATACCCGTTTTTTTGGGCTAACAGGAGGAATTAAcc",
            "atgGacacAattaacatcgctaagaacgacttctctgacatcgaactggctgctatcccgttcaacactctggctgaccattacggtgagcgtttagctcgcgaacagttggcccttgagcatgagtcttacgagatgggtgaagcacgcttccgcaagatgtttgagcgtcaacttaaagctggtgaggttgcggataacgctgccgccaagcctctcatcactaccctactccctaagatgattgcacgcatcaacgactggtttgaggaagtgaaagctaagcgcggcaagcgcccgacagccttccagttcctgcaagaaatcaagccggaagccgtagcgtacatcaccattaagaccactctggcttgcctaaccagtgctgacaatacaaccgttcaggctgtagcaagcgcaatcggtcgggccattgaggacgaggctcgcttcggtcgtatccgtgaccttgaagctaagcacttcaagaaaaacgttgaggaacaactcaacaagcgcgtagggcacgtctacaagaaagcatttatgcaagttgtcgaggctgacatgctctctaagggtctactcggtggcgaggcgtggtcttcgtggcataaggaagactctattcatgtaggagtacgctgcatcgagatgctcattgagtcaaccggaatggttagcttacaccgccaaaatgctggcgtagtaggtcaagactctgagactatcgaactcgcacctgaatacgctgaggctatcgcaacccgtgcaggtgcgctggctggcatctctccgatgttccaaccttgcgtagttcctcctaagccgtggactggcattactggtggtggctattgggctaacggtcgtcgtcctctggcgctggtgcgtactcacagtaagaaagcactgatgcgctacgaagacgtttacatgcctgaggtgtacaaagcgattaacattgcgcaaaacaccgcatggaaaatcaacaagaaagtcctagcggtcgccaacgtaatcaccaagtggaagcattgtccggtcgaggacatccctgcgattgagcgtgaagaactcccgatgaaaccggaagacatcgacatgaatcctgaggctctcaccgcgtggaaacgtgctgccgctgctgtgtaccgcaaggacaaggctcgcaagtctcgccgtatcagccttgagttcatgcttgagcaagccaataagtttgctaaccataaggccatctggttcccttacaacatggactggcgcggtcgtgtttacgctgtgtcaatgttcaacccgcaaggtaacgatatgaccaaaggactgcttacgctggcgaaaggtaaaccaatcggtaaggaaggttactactggctgaaaatccacggtgcaaactgtgcgggtgtcgataaggttccgttccctgagcgcatcaagttcattgaggaaaaccacgagaacatcatggcttgcgctaagtctccactggagaacacttggtgggctgagcaagattctccgttctgcttccttgcgttctgctttgagtacgctggggtacagcaccacggcctgagctataactgctcccttccgctggcgtttgacgggtcttgctctggcatccagcacttctccgcgatgctccgagatgaggtaggtggtcgcgcggttaacttgcttcctagtgaaaccgttcaggacatctacgggattgttgctaagaaagtcaacgagattctacaagcagacgcaatcaatgggaccgataacgaagtagttaccgtgaccgatgagaacactggtgaaatctctgagaaagtcaagctgggcactaaggcactggctggtcaatggctggcttacggtgttactcgcagtgtgactaagcgttcagtcatgacgctggcttacgggtccaaagagttcggcttccgtcaacaagtgctggaagataccattcagccagctattgattccggcaagggtctgatgttcactcagccgaatcaggctgctggatacatggctaagctgatttgggaatctgtgagcgtgacggtggtagctgcggttgaagcaatgaactggcttaagtctgctgctaagctgctggctgctgaggtcaaagataagaagactggagagattcttcgcaagcgttgcgctgtgcattgggtaactcctgatggtttccctgtgtggcaggaatacaagaagcctattcagacgcgcttgaacctgatgttcctcggtcagttccgcttacagcctaccattaacaccaacaaagatagcgagattgatgcacacaaacaggagtctggtatcgctcctaactttgtacacagccaagacggtagccaccttcgtaagactgtagtgtgggcacacgagaagtacggaatcgaatcttttgcactgattcacgactccttcggtaccattccggctgacgctgcgaacctgttcaaagcagtgcgcgaaactatggttgacacatatgagtcttgtgatgtactggctgatttctacgaccagttcgctgaccagttgcacgagtctcaattggacaaaatgccagcacttccggctaaaggtaacttgaacctccgtgacatcttagagtcggacttcgcgttcgcAtaa",
            "MDTINI");
        rbsOptions.add(opt3);
    }

    public RBSOption run(String dnaseq, Set<RBSOption> ignores) throws Exception {
        for(RBSOption rbsopt : rbsOptions) {
            if(!ignores.contains(rbsopt)) {
                return rbsopt;
            }
        }

        throw new Exception();
    }
    
    public static void main(String[] args) throws Exception {
        //Create an example
        String peptide = "MLSDTIDTKQQQQQLHVLFIDSYDSFTYNVVRLIEQQTDISPGVNAVHVTTVHSDTFQSMDQLLPLLPLFDAIVVGPGPGNPNNGAQDMGIISELFENANGKLDEVPILGICLGFQAMCLAQGADVSELNTIKHGQVYEMHLNDAARACGLFSGYPDTFKSTRYHSLHVNAEGIDTLLPLCTTEDENGILLMSAQTKNKPWFGVQYHPESCCSELGGLLVSNFLKLSFINNVKTGRWEKKKLNGEFSDILSRLDRTIDRDPIYKVKEKYPKGEDTTYVKQFEVSEDPKLTFEICNIIREEKFVMSSSVISENTGEWSIIALPNSASQVFTHYGAMKKTTVHYWQDSEISYTLLKKCLDGQDSDLPGSLEVIHEDKSQFWITLGKFMENKIIDNHREIPFIGGLVGILGYEIGQYIACGRCNDDENSLVPDAKLVFINNSIVINHKQGKLYCISLDNTFPVALEQSLRDSFVRKKNIKQSLSWPKYLPEEIDFIITMPDKLDYAKAFKKCQDYMHKGDSYEMCLTTQTKVVPSAVIEPWRIFQTLVQRNPAPFSSFFEFKDIIPRQDETPPVLCFLSTSPERFLKWDADTCELRPIKGTVKKGPQMNLAKATRILKTPKEFGENLMILDLIRNDLYELVPDVRVEEFMSVQEYATVYQLVSVVKAHGLTSASKKTRYSGIDVLKHSLPPGSMTGAPKKITVQLLQDKIESKLNKHVNGGARGVYSGVTGYWSVNSNGDWSVNIRCMYSYNGGTSWQLGAGGAITVLSTLDGELEEMYNKLESNLQIFM";
        
        //Initiate the chooser
        RBSChooser chooser = new RBSChooser();
        chooser.initiate();
        
        //Make the first choice with an empty Set of ignores
        Set<RBSOption> ignores = new HashSet<>();
        RBSOption selected1 = chooser.run( peptide, ignores);
        
        //Add the first selection to the list of things to ignore
        ignores.add(selected1);
        
        //Choose again with an ignore added
        RBSOption selected2 = chooser.run( peptide, ignores);
        
        //Print out the two options, which should be different
        System.out.println("Selected1:\n");
        System.out.println(selected1.toString());
        System.out.println();
        System.out.println("Selected2:\n");
        System.out.println(selected2.toString());
    }
}
