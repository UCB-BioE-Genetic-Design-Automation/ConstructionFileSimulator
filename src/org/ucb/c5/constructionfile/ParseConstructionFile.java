package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.utils.FileUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.ucb.c5.utils.Log;


/**
 *
 * @author J. Christopher Anderson
 * @rewritten by Connor Tou, Zexuan Zhao
 * @rewritten by Zihang Shao
 */
public class ParseConstructionFile {
    public void initiate() { }

    public ConstructionFile run(String rawText) throws Exception {
        // Create a map to store given sequences and a list to store the steps
        HashMap<String, Polynucleotide> sequences = new HashMap<>();
        List<Step> steps = new ArrayList<>();
        
        //Handle when a dividing line separates cf and seqs
        String[] underSplit = rawText.split("(-)\\1{3,}");
        if(underSplit.length == 2) {
            try {
                processSequences(underSplit[1], sequences);
            } catch(Exception err) {
                throw new IllegalArgumentException("Could not parse sequences below the line:\n" + underSplit[1]);
            }
            try {
                processSteps(underSplit[0], steps);
            } catch(Exception err) {
                throw new IllegalArgumentException("Could not parse steps above the line:\n" + underSplit[0]);
            }
            String plasmidName = steps.get(steps.size()-1).getProduct();
            return new ConstructionFile(steps, plasmidName, sequences);
        }

        //Handle when a > designates boundary
        int gtLocus = rawText.indexOf(">", 1);
        if(gtLocus > -1) {
            String stepSection = rawText.substring(0,gtLocus);
            String seqSection = rawText.substring(gtLocus);
            try {
                processSteps(stepSection, steps);
            } catch(Exception err) {
                throw new IllegalArgumentException("Could not parse steps before fasta in:\n" + stepSection);
            }
            try {
                processSequences(seqSection, sequences);
            } catch (Exception err) {
                throw new IllegalArgumentException("Could not parse fasta in:\n" + seqSection);
            }
            String plasmidName = steps.get(steps.size()-1).getProduct();
            return new ConstructionFile(steps, plasmidName, sequences);
        }
        
        //Handle if no sequences are provided
        String stepSection = rawText;
        processSteps(stepSection, steps);
        String plasmidName = steps.get(steps.size()-1).getProduct();
        return new ConstructionFile(steps, plasmidName, sequences);
    }

    private void processSteps(String rawText, List<Step> steps) throws Exception {
        //Replace common unnessary words
        String text = rawText.replace("to ", "");
        text = text.replace("from ", "");
        text = text.replace("on ", "");
        text = text.replace("with ", "");
        text = text.replace("the ", "");
        text = text.replace(" bp", "");
        text = text.replace("bp", "");
        text = text.replaceAll("\\(", "");
        text = text.replaceAll("\\)", "");
        text = text.replaceAll("\r", "\n");
        text = text.replaceAll("\\\\", "\n\\\\");
        text = text.replaceAll("\t", " ");

        //Break it into lines
        String[] lines = text.split("\\r|\\r?\\n");

        //Process each good line
        for (int i = 0; i < lines.length; i++) {
            String aline = lines[i];

            //Ignore blank lines
            if (aline.trim().isEmpty()) {
                continue;
            }

            //Ignore commented-out lines
            if (aline.startsWith("//")) {
                continue;
            }
            
            //Ignore title
            if (aline.toLowerCase().startsWith(">")){
                continue;
            }

            //Try to parse the operation, if fails will throw Exception
            String[] spaces = aline.split("\\s+");
            String sop = spaces[0].toLowerCase();
            
            //Cleanup was removed from ConstructionFile
            if(sop.equals("cleanup")) {
                continue;
            }
            
            Operation op;
            try {
                op = Operation.valueOf(sop);
            } catch(Exception err) {
                throw new IllegalArgumentException("Unable to parse operation: " + sop);
            }
            
            //If past the gauntlet, keep the line
            try {
                Step parsedStep = parseLine(op, spaces);
                steps.add(parsedStep);
            } catch(Exception err) {
                Log.severe(err.getMessage());
                throw new IllegalArgumentException("Could not parse the line:\n" + aline + "\nin text:\n" + rawText);
            }
        }
    }

    private void processSequences(String seqSection, HashMap<String, Polynucleotide> sequences){
        //Handle if it is a list of FASTA
        if(seqSection.contains(">")) {
            String[] fseqs = seqSection.split(">");
            for(String f : fseqs) {
                if(f.trim().isEmpty()) {
                    continue;
                }
                String[] lines = f.trim().split("\\r|\\r?\\n");
                String[] spaces = lines[0].split("\\s+");
                String name = spaces[0];
                String seq = lines[1];
                if(!seq.toUpperCase().matches("[ATCG]+")) {
                    throw new IllegalArgumentException("Sequence:\n" + seq + "\ncontains non-DNA sequences in:\n" + f);
                }
                sequences.put(name, createPoly(seq));
            }
            return;
        }
        
        //Handle if it is in TSV
        String[] lines = seqSection.split("\\r|\\r?\\n");
            for (String line : lines) {
                //Ignore blank lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                //Ignore commented-out lines
                if (line.startsWith("//")) {
                    continue;
                }

                String[] tabs = line.split("\t");
                String name = tabs[0];
                String seq = tabs[1];
                if(!seq.toUpperCase().matches("[ATCG]+")) {
                    throw new IllegalArgumentException("Sequence:\n" + seq + "\ncontains non-DNA sequences in:\n" + line);
                }
                sequences.put(name, createPoly(seq));
            }
    }

    
    private Polynucleotide createPoly(String seq) {
        //If it's an oligo
        if(seq.length() < 100) {
            return new Polynucleotide(seq, "", "", false, false, false);
        }
        
        //If it's a plasmid
        return new Polynucleotide(seq, true);
    }
    
    private Step parseLine(Operation op, String[] spaces) throws Exception {
        
        switch (op) {
            case pcr:
                return createPCR(
                        spaces[1].split(","),
                        spaces[2],
                        spaces[3],
                        spaces[4]);
            case pca:
                return createPCA(
                        spaces[1].split(","),
                        spaces[3]);
//            case cleanup:
//                return createCleanup(
//                        spaces[1],
//                        spaces[2]);
            case digest:
                return createDigest(
                        spaces[1],
                        spaces[2].split(","),
                        spaces[3]);
            case ligate:
                return createLigation(
                        spaces[1].split(","),
                        spaces[2]);
            case transform:
                String plasmidName = null;
                if(spaces.length>4) {
                    plasmidName = spaces[4];
                } else {
                    plasmidName = spaces[1];
                }
                return createTransform(
                        spaces[1],
                        spaces[2].replaceAll(",", ""),
                        spaces[3],
                        plasmidName);
            case acquire:
                return createAcquire(
                        spaces[2].split(","));
            case assemble:
                return createAssemble(
                        spaces[1].split(","),
                        spaces[2].replace(",", ""),
                        spaces[3]);
            default:
                throw new RuntimeException("Not implemented " + op);
        }
    }

    private Step createPCR(String[] oligos, String template, String size, String product) {    
        return new PCR(oligos[0], oligos[1], template, product);
    }
    
    private Step createPCA(String[] oligos, String product) {
        List<String> frags = new ArrayList<>();
        for (String frag : oligos) {
            frags.add(frag);
        }
        return new PCA(frags, product);
    }
    
//    private Step createCleanup(String substrate, String product) {
//        return new Cleanup(substrate, product);
//    }

    private Step createDigest(String substrate, String[] enzymes, String product) {
        List<Enzyme> enzList = new ArrayList<>();
        for (String enz : enzymes) {
            Enzyme enzyme;
            try {
                enzyme = Enzyme.valueOf(enz);
            }
            catch(Exception IllegalArgumentException) {
                enzyme = Enzyme.valueOf(enz.toLowerCase());
            }
            enzList.add(enzyme);
        }
        return new Digestion(substrate, enzList, product);
    }

    private Step createLigation(String[] fragments, String product) {
        List<String> frags = new ArrayList<>();
        frags.addAll(Arrays.asList(fragments));
        return new Ligation(frags, product);
    }

    private Step createTransform(String substrate, String strain, String antibiotic, String product) {
        Antibiotic ab = Antibiotic.valueOf(antibiotic);
        return new Transformation(substrate, strain, ab, product);
    }

    private Step createAcquire(String[] dnas) {
        return new Acquisition(dnas[0]);
    }
    
    private Step createAssemble(String[] fragments, String enzyme, String product){
        Enzyme ez = Enzyme.valueOf(enzyme);
        List<String> frags = new ArrayList<>();
        for (String frag:fragments){
            frags.add(frag);
        }
        return new Assembly(frags, ez, product);
    }

    public static void main(String[] args) throws Exception {
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        
        String text = FileUtils.readResourceFile("constructionfile/data/Construction of aspC1.txt");
        ConstructionFile cf = pCF.run(text);
        
        System.out.println(cf.toString());
    }

}
