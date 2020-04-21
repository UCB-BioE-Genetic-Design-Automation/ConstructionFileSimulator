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
        
        String text = rawText;

        text = text.replaceAll("\r", "\n");
        text = text.replaceAll("\\\\", "\n\\\\");
        



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
            if (aline.trim().startsWith("//")) {
                continue;
            }
            
            //Ignore title
            if (aline.toLowerCase().trim().startsWith(">")){
                continue;
            }

            //Try to parse the operation, if fails will throw Exception
            String[] spaces = aline.split("\\s+");
            String sop = spaces[0].toLowerCase();
            String lineNoOP = aline.substring(sop.length());
            
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
                ///parseLine input (Operation, String)
                Step parsedStep = parseLine(op, lineNoOP);
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
    
    //parseLine input (Operation, String)
    private Step parseLine(Operation op, String lineNoOP) throws Exception {
        
        //Standardize
        lineNoOP = lineNoOP.replaceAll("\\s+and\\s+", ",");
        lineNoOP = lineNoOP.replace("/", ",");

        
        switch (op) {
            
            case pcr://pcr oligo1,oligo2 on template \t (size bp,product) 
                               
                //split content w/i and w/o ()
                String[] parenPCR = lineNoOP.split("\\(");
                //split oligo and plasmid
                String[] oligoPlasmidPCR = parenPCR[0].split(" on ");
                //trim and split oligos
                String oligoPlasmidPCR0 = oligoPlasmidPCR[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+", ",");
                String[] oligosPCR = oligoPlasmidPCR0.split(",");
                //extract plasmid
                String templatePCR = oligoPlasmidPCR[1].trim();
                
                //eliminate ()
                String sizeProductPCR = parenPCR[1].trim();
                sizeProductPCR = sizeProductPCR.replaceAll("\\(", "");
                sizeProductPCR = sizeProductPCR.replaceAll("\\)", "");
                //split by "bp,"
                String[] spPCR = sizeProductPCR.split("bp,");
                String sizePCR = spPCR[0].trim();
                String productPCR = spPCR[1].trim();
                //evoke createPCR and return
                return createPCR(
                        oligosPCR,
                        templatePCR,
                        sizePCR,
                        productPCR
                );
                
                
            case pca://pca oligo1,oligo2 \t (product)
                
                String[] parenPCA = lineNoOP.split("\\(");
                //Extract oligos
                String oliPCA = parenPCA[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+",",");
                String[] oligosPCA = oliPCA.split(",");
                //Eliminate (), extract product
                String productPCA = parenPCA[1].replaceAll("\\(", "").replaceAll("\\)", "").trim();
                
                
                return createPCA(
                        oligosPCA,
                        productPCA
                );
                
                
            case digest://digest substrate with enzyme1,enzyme2 \t (product)
                
                //split ()
                String[] parenDig = lineNoOP.split("\\(");
                //split oligo and enzyme
                String[] oligoEnzyDig = parenDig[0].trim().split(" with ");
                //extract oligos
                String subDig = oligoEnzyDig[0].trim();
                //extract enzyme
                String enzDig = oligoEnzyDig[1].trim().replaceAll(",\\s+",",").replaceAll("\\s+", ",");
                String[] enzyDig = enzDig.split(",");
                //extract product
                String productDig = parenDig[1].replaceAll("\\(", "").replaceAll("\\)", "").trim();
                
                
                return createDigest(
                        subDig,
                        enzyDig,
                        productDig
                );
                
                
            case ligate://ligate frag1,frag2 \t (product)
                
                String[] segLig = lineNoOP.split("\\(");
                
                
                //extract frag
                String fragLig = segLig[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+",",");
                String[] fragsLig = fragLig.split(",");
                //extract product
                String productLig = segLig[1].replaceAll("\\(", "").replaceAll("\\)", "").trim();            
                
                return createLigation(
                        fragsLig,
                        productLig
                );
                
                                                
            case transform://transform substrate \t (strain,antibiotic,product)
                
                //split ()
                String[] parenTrans = lineNoOP.split("\\(");
                //extract substrate
                String subTrans = parenTrans[0].trim();
                
                //split strain, antibiotic, product(if there is)
                String[] parenInTrans = parenTrans[1].replaceAll("\\(", "").replaceAll("\\)", "").trim().replaceAll(",\\s+",",").replaceAll("\\s+", ",").split(",");
                //extract strain, antibiotic
                String strainTrans = parenInTrans[0].trim();
                String antibioticTrans = parenInTrans[1].trim();
                
                //extract product if there is one
                String plasmidNameTrans = null;
                if (parenInTrans.length > 2){
                    plasmidNameTrans = parenInTrans[2];
                } else {
                    plasmidNameTrans = subTrans;
                }
                
                
                return createTransform(
                        subTrans,
                        strainTrans,
                        antibioticTrans,
                        plasmidNameTrans
                );
                
                
            case acquire://acquire oligo oligo1,oligo oligo2, oligo3,plasmid plasmid1,plasmid2
                
                //eliminate excessive info oligo, plasmid
                String textAcq = lineNoOP.replaceAll("\\s+oligo\\s+", " ");
                textAcq = textAcq.replaceAll(",oligo\\s+", ",");
                textAcq = textAcq.replaceAll("\\s+plasmid\\s+", " ");
                textAcq = textAcq.replaceAll(",plasmid\\s+",",");
                //standardize connection between oligos, plasmids
                textAcq = textAcq.trim().replaceAll("\\s+", ",");
                
                //extract oligos, plasmids
                String[] combineAcq = textAcq.split(",");
                
                return createAcquire(
                        combineAcq
                );
                
                
            case assemble://assemble frag1,frag2 \t (enzyme,product)
                
                //split ()
                String[] parenASB = lineNoOP.split("\\(");
                
                //extract fragments
                String fragASB = parenASB[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+", ",");
                String[] fragsASB = fragASB.split(",");
                
                //split enzyme and product
                String parenInASB = parenASB[1].replaceAll("\\(","").replaceAll("\\)", "").trim();
                String[] enzyProdASB = parenInASB.replaceAll(",\\s+",",").replaceAll("\\s+", ",").split(",");
                //extract enzyme and product
                String enzymeASB = enzyProdASB[0].trim();
                String productASB = enzyProdASB[1].trim();
                
                return createAssemble(
                        fragsASB,
                        enzymeASB,
                        productASB
                );
                
                
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
