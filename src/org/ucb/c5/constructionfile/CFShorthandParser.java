package org.ucb.c5.constructionfile;

import java.util.*;
import java.util.regex.*;
import org.ucb.c5.constructionfile.model.*;

public class CFShorthandParser {
    private static final Pattern DNA_SEQUENCE_PATTERN = Pattern.compile("[ACGTRYSWKMBDHVNacgtryswkmbdhvn]+");

    public ConstructionFile run(String content) {
        // Remove /* (anything) */ comments
        content = content.replaceAll("/\\*(.|\\r?\\n)*?\\*/", "");

        // Split into lines
        String[] lines = content.split("\\n");

        // Initialize sequences and steps
        Map<String, Polynucleotide> sequences = new HashMap<>();
        List<Step> steps = new ArrayList<>();
        String productName = null;

        for (String line : lines) {
            // Remove # or // comments
            line = line.split("#|//")[0].trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // Tokenize the line
            String[] tokens = line.split("\\s+");

            // Handle operation or sequence
            String firstToken = tokens[0].toLowerCase();
            switch (firstToken) {
                case "pcr":
                    steps.add(handlePCR(tokens));
                    break;
                case "digest":
                    steps.add(handleDigest(tokens));
                    break;
                case "ligate":
                    steps.add(handleLigate(tokens));
                    break;
                case "goldengate":
                    steps.add(handleGoldenGate(tokens));
                    break;
                case "gibson":
                    steps.add(handleGibson(tokens));
                    break;
                case "transform":
                    Step transformStep = handleTransform(tokens);
                    steps.add(transformStep);
                    productName = transformStep.getInputs().get(0);  // Get the input of the Transformation step
                    break;
                default:
                    if (tokens.length >= 2 && DNA_SEQUENCE_PATTERN.matcher(tokens[1]).matches()) {
                        sequences.put(tokens[0], handleSequence(tokens));
                    } else {
                        throw new IllegalArgumentException("Unknown operation or invalid sequence: " + line);
                    }
            }
        }

        // Create and return ConstructionFile
        return new ConstructionFile(steps, productName, sequences);
    }
    
    private Step handlePCR(String[] tokens) {
        if (tokens.length < 5) {
            throw new IllegalArgumentException("PCR step: " + String.join(" ", tokens) + " has insufficient sequence parameters; there should be a forwardOligo, reverseOligo, template, and productName");
        }
        // Create and return PCR step...
        String forwardOligo = tokens[1];
        String reverseOligo = tokens[2];
        List<String> template = Arrays.asList(tokens[3]);
        String productName = tokens[4];
        return new PCR(forwardOligo, reverseOligo, template, productName);
    }

    private Step handleDigest(String[] tokens) {
        if (tokens.length < 5) {
            throw new IllegalArgumentException("Digest step: " + String.join(" ", tokens) + " has insufficient sequence parameters; there should be a DNA, Enzymes, FragmentSelection, and productName");
        }
        // Create and return Digest step...
        String dna = tokens[1];
        List<String> enzymes = Arrays.asList(tokens[2].split(","));
        int fragmentSelection = Integer.parseInt(tokens[3]);
        String productName = tokens[4];
        return new Digestion(dna, enzymes, fragmentSelection, productName);
    }

    private Polynucleotide handleSequence(String[] tokens) {
        if (tokens.length < 2) {
            throw new IllegalArgumentException("Sequence line: " + String.join(" ", tokens) + " has insufficient parameters; there should be a name and sequence");
        }
        
        // Infer type (oligo or plasmid), create and return Polynucleotide...
        String name = tokens[0];
        String sequence = tokens[1];
        if (sequence.length() < 100) {
            // If the sequence length is less than 80, it's an oligo (single-stranded linear polynucleotide)
            return new Polynucleotide(sequence, "", "", false, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        } else {
            // If the sequence length is 100 or more, it's a plasmid (double-stranded circular polynucleotide)
            return new Polynucleotide(sequence, true);
        }
    }

    private Step handleLigate(String[] tokens) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Ligate step: " + String.join(" ", tokens) + " has insufficient sequence parameters; there should be at least one Fragment and a productName");
        }
        // Create and return Ligate step...
        String productName = tokens[tokens.length - 1];
        List<String> fragments = new ArrayList<>();
        for (int i = 1; i < tokens.length - 1; i++) {
            fragments.add(tokens[i]);
        }
        return new Ligation(fragments, productName);
    }

    private Step handleGoldenGate(String[] tokens) {
        if (tokens.length < 4) {
            throw new IllegalArgumentException("GoldenGate step: " + String.join(" ", tokens) + " has insufficient sequence parameters; there should be at least one Fragment, an Enzyme, and a productName");
        }
        // Create and return GoldenGate step...
        String enzyme = tokens[tokens.length - 2];
        String productName = tokens[tokens.length - 1];
        List<String> fragments = new ArrayList<>();
        for (int i = 1; i < tokens.length - 2; i++) {
            fragments.add(tokens[i]);
        }
        return new Assembly(fragments, enzyme, productName);
    }

    private Step handleGibson(String[] tokens) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Gibson step: " + String.join(" ", tokens) + " has insufficient sequence parameters; there should be at least one Fragment and a productName");
        }
        // Create and return Gibson step...
        String productName = tokens[tokens.length - 1];
        List<String> fragments = new ArrayList<>();
        for (int i = 1; i < tokens.length - 1; i++) {
            fragments.add(tokens[i]);
        }
        return new Assembly(fragments, "Gibson", productName);
    }

    private Step handleTransform(String[] tokens) {
        if (tokens.length < 6) {
            throw new IllegalArgumentException("Transform step: " + String.join(" ", tokens) + " has insufficient sequence parameters; there should be a Plasmid, Host, Antibiotic, Temperature, and productName");
        }
        // Create and return Transform step...
        String plasmid = tokens[1];
        String host = tokens[2];
        String antibioticName = tokens[3];
        Antibiotic antibiotic = Arrays.stream(Antibiotic.values())
                                      .filter(a -> a.name().equalsIgnoreCase(antibioticName))
                                      .findFirst()
                                      .orElseThrow(() -> new IllegalArgumentException("Unknown antibiotic: " + antibioticName));
        String temperature = tokens[4];
        String productName = tokens[5];
        return new Transformation(plasmid, host, antibiotic, productName);
    }

}