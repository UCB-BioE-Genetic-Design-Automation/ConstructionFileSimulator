package org.ucb.c5.constructionfile;

import static org.junit.Assert.assertEquals;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.junit.Test;
import org.ucb.c5.constructionfile.model.Operation;
import org.ucb.c5.constructionfile.model.Polynucleotide;

/**
 *
 * @author J. Christopher Anderson
 */
public class CFShorthandParserTest {
        
    CFShorthandParser sp = new CFShorthandParser();
    
    @Test
    /**
     * Test of Shorthand with comments of various types
     */
    public void testShorthandComments() throws Exception {
        String shorthand = "# This is a comment\n" +
            "PCR ca877F ca606R pAC-TetInv InvasinPCRProduct // This is another comment\n" +
            "/* This is a block \ncomment */\n" +
            "Digest InvasinPCRProduct BamHI,EcoRI 1 InvasinDigested";
        
        System.out.println(shorthand);
        ConstructionFile cf = sp.run(shorthand);
        
        // Check that the ConstructionFile has the expected steps
        assert(cf.getSteps().size() == 2);
        assert(cf.getSteps().get(0).getOperation() == Operation.pcr);
        assert(cf.getSteps().get(1).getOperation() == Operation.digest);
    }
    
    @Test
    /**
     * Tests that the parser correctly throws an IllegalArgumentException when given invalid input.
     * Each line in the test shorthand is designed to cause an error:
     * - The PCR line is missing the productName.
     * - The Digest line is missing the fragmentSelection and productName.
     * - The UnknownOperation line contains an operation name that the parser doesn't recognize.
     * - The first sequence line is missing the sequence.
     * - The second sequence line contains a sequence that doesn't match the DNA sequence pattern.
     * The test passes if the parser throws an IllegalArgumentException for each line.
     */
    public void testInvalidInput() throws Exception {
        String[] shorthands = new String[] {
            "PCR ca877F ca606R pAC-TetInv",  // Missing productName
            "Digest InvasinPCRProduct BamHI,EcoRI",  // Missing fragmentSelection and productName
            "UnknownOperation ca877F ca606R pAC-TetInv InvasinPCRProduct",  // Unknown operation
            "ca877F ",  // Missing sequence
            "ca877F GAGTTGGATCCxxTTTTCCAGCCAATCAGTG"  // Invalid sequence
        };

        for (String shorthand : shorthands) {
            try {
                sp.run(shorthand);
                System.out.println("No exception thrown for: " + shorthand);
                assert(false);
            } catch (IllegalArgumentException e) {
                assert(true);
            }
        }

    }
    
    @Test
    /**
     * Tests that the parser correctly parses valid input.
     * The test shorthand includes all the operations (PCR, Digest, Ligate, GoldenGate, Gibson, Transform) and sequence lines.
     * The test passes if the parser produces a ConstructionFile with the expected steps and sequences.
     */
    public void testCorrectParsing() throws Exception {
        String shorthand = "PCR ca877F ca606R pAC-TetInv InvasinPCRProduct\n" +
            "Digest InvasinPCRProduct BamHI,EcoRI 1 InvasinDigested\n" +
            "Ligate InvasinDigested pBACr-FdhDigested pBACr-Fdh-Invasin\n" +
            "GoldenGate Fragment1 Fragment2 BsaI ProductName\n" +
            "Gibson Fragment1 Fragment2 ProductName\n" +
            "Transform Plasmid Host Amp 37 ProductName\n" +
            "ca877F GAGTTGGATCCNNNGGAGNNNNNNRTGNTGGTTTTCCAGCCAATCAGTG\n" +
            "ca606R GTCGACGGCGCTATTCAGATCCTC";

        ConstructionFile cf = sp.run(shorthand);

        // Check that the ConstructionFile has the expected steps and sequences
        assertEquals(6, cf.getSteps().size());
        assertEquals(2, cf.getSequences().size());
    }

    @Test
    /**
     * Tests that the parser correctly handles varying amounts of whitespace.
     * The test shorthand includes lines with varying amounts of whitespace between tokens.
     * The test passes if the parser produces a ConstructionFile with the expected steps and sequences.
     */
    public void testWhitespaceHandling() throws Exception {
        String shorthand = "PCR   ca877F    ca606R    pAC-TetInv    InvasinPCRProduct\n" +
            "Digest\tInvasinPCRProduct\tBamHI,EcoRI\t1\tInvasinDigested\n" +
            "ca877F    GAGTTGGATCCNNNGGAGNNNNNNRTGNTGGTTTTCCAGCCAATCAGTG\n" +
            "ca606R\tGTCGACGGCGCTATTCAGATCCTC";

        ConstructionFile cf = sp.run(shorthand);

        // Check that the ConstructionFile has the expected steps and sequences
        assertEquals(2, cf.getSteps().size());
        assertEquals(2, cf.getSequences().size());
    }

    @Test
    /**
     * Tests that the parser correctly identifies oligos and plasmids based on sequence length.
     * The test shorthand includes sequence lines where the sequence is less than 80 characters long (should be identified as an oligo) and 80 or more characters long (should be identified as a plasmid).
     * The test passes if the parser produces a ConstructionFile with the expected sequences.
     */
    public void testOligoAndPlasmidIdentification() throws Exception {
        String shorthand = "Oligo GAGTTGGATCCNNNGGAGNNNNNNRTGNTGGTTTTCCAGCCAATCAGTG\n" +
            "Plasmid GGCCATGAATTTACGATCGAAGGTGTAGGAACTGGGTACCCTTACGAAGGGAAACAGATGTCCGAATTAGTGATCATCAAGCCTGCGGGAAAACCCCTTCCATTCTCCTTTGACATACTGTCATCAGTCTTTCAATATGGAAACCGTTGCTTCACAAAGTACCCGGCAGACATGCCTGACTATTTCAAGCAAGCATTCCCAGATGGAATGTCATATGAAAGGTCATTTCTATTTGAGGATGGAGCAGTTGCTACAGCCAGCTGGAACATTCGACTCGAAGGAAATTGCTTCATCCACAAATCCATCTTTCATGGCGTAAACTTTCCCGCTGATGGACCCGTAATGAAAAAGAAGACCATTGACTGGGATAAGTCCTTCGAAAAAATGACTGTGTCTAA";
        ConstructionFile cf = sp.run(shorthand);

        // Check that the ConstructionFile has the expected sequences
        assertEquals(2, cf.getSequences().size());
        Polynucleotide oligo = cf.getSequences().get("Oligo");
        Polynucleotide plasmid = cf.getSequences().get("Plasmid");
        assert(oligo.isIsDoubleStranded() == false);
        assert(plasmid.isIsDoubleStranded() == true);
    }
    
    @Test
    /**
     * Tests that the parser correctly handles operation names, enzyme names, and antibiotic names in different cases.
     * The test shorthand includes operation names, enzyme names, and antibiotic names in different cases (e.g., "PCR" vs "pcr", "BamHI" vs "bamhi", "Amp" vs "amp").
     * The test passes if the parser produces a ConstructionFile with the expected steps.
     */
    public void testCaseSensitivity() throws Exception {
        String shorthand = "pcr ca877F ca606R pAC-TetInv InvasinPCRProduct\n" +
            "DIGEST InvasinPCRProduct bamhi,ECORI 1 InvasinDigested\n" +
            "ligate InvasinDigested pBACr-FdhDigested pBACr-Fdh-Invasin\n" +
            "GOLDENGATE Fragment1 Fragment2 bsai ProductName\n" +
            "gibson Fragment1 Fragment2 ProductName\n" +
            "TRANSFORM Plasmid Host amp 37 ProductName";

        ConstructionFile cf = sp.run(shorthand);

        // Check that the ConstructionFile has the expected steps
        assert(cf.getSteps().size() == 6);
        assert(cf.getSteps().get(0).getOperation() == Operation.pcr);
        assert(cf.getSteps().get(1).getOperation() == Operation.digest);
        assert(cf.getSteps().get(2).getOperation() == Operation.ligate);
        assert(cf.getSteps().get(3).getOperation() == Operation.assemble);
        assert(cf.getSteps().get(4).getOperation() == Operation.assemble);
        assert(cf.getSteps().get(5).getOperation() == Operation.transform);
    }

    @Test
    /**
     * Tests that the parser correctly handles lines with extra whitespace at the beginning or end.
     * The test shorthand includes lines with extra whitespace at the beginning or end.
     * The test passes if the parser produces a ConstructionFile with the expected steps and sequences.
     */
    public void testExtraWhitespace() throws Exception {
        String shorthand = "  PCR ca877F ca606R pAC-TetInv InvasinPCRProduct  \n" +
            "  Digest InvasinPCRProduct BamHI,EcoRI 1 InvasinDigested  \n" +
            "  ca877F GAGTTGGATCCNNNGGAGNNNNNNRTGNTGGTTTTCCAGCCAATCAGTG  \n" +
            "  ca606R GTCGACGGCGCTATTCAGATCCTC  ";

        ConstructionFile cf = sp.run(shorthand);

        // Check that the ConstructionFile has the expected steps and sequences
        assert(cf.getSteps().size() == 2);
        assert(cf.getSequences().size() == 2);
    }
    
    @Test
    public void testCFParserWrapperOnShorthand() throws Exception {
        ParseConstructionFile pcf = new ParseConstructionFile();
        pcf.initiate();
        String shorthand = "PCR ca877F ca606R pAC-TetInv InvasinPCRProduct\n" +
            "Digest InvasinPCRProduct BamHI,EcoRI 1 InvasinDigested\n" +
            "Ligate InvasinDigested pBACr-FdhDigested pBACr-Fdh-Invasin\n" +
            "GoldenGate Fragment1 Fragment2 BsaI ProductName\n" +
            "Gibson Fragment1 Fragment2 ProductName\n" +
            "Transform Plasmid Host Amp 37 ProductName\n" +
            "ca877F GAGTTGGATCCNNNGGAGNNNNNNRTGNTGGTTTTCCAGCCAATCAGTG\n" +
            "ca606R GTCGACGGCGCTATTCAGATCCTC";

        ConstructionFile cf = pcf.run(shorthand);

        // Check that the ConstructionFile has the expected steps and sequences
        assertEquals(6, cf.getSteps().size());
        assertEquals(2, cf.getSequences().size());
    }
}
