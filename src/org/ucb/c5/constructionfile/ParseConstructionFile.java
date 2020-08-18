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

                String seq = lines[1].toUpperCase();
                if(!seq.toUpperCase().matches("[ACGTRYSWKMBDHVNacgtryswkmbdhvn]+")) {
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
                String seq = tabs[1].toUpperCase();
                if(!seq.matches("[ATCG]+")) {
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
                //Separate the oligos, templates, and product info
                //First split between oligos+templates and product info
                String[] parenPCR = lineNoOP.split("\\(");
                //Then split the oligos from the templates
                String[] oligoPlasmidPCR = parenPCR[0].split("\\s+on\\s+");
                
                //trim and split oligos
                //TODO:  This could be more explicit:  there are 2 and and only 2 oligos in PCR
                String oligosPCR0 = oligoPlasmidPCR[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",");
                //evoke rangeInString to split oligos
                String[] oligosPCR = rangeInString(oligosPCR0);

                //identify product and its size (if available)
                //eliminate ()
                String sizeProductPCR = parenPCR[1].trim();
                sizeProductPCR = sizeProductPCR.replaceAll("\\(", "");
                sizeProductPCR = sizeProductPCR.replaceAll("\\)", "");
                
                //split by "bp,"
                String sizePCR;
                String productPCR;
                if(sizeProductPCR.contains("bp,")){
                    String[] spPCR = sizeProductPCR.split("bp,");
                    sizePCR = spPCR[0].trim();
                    productPCR = spPCR[1].trim();
                }else{
                    sizePCR = null;
                    productPCR = sizeProductPCR.trim();
                }   
                
                //extract the list of templates
                String templateStr = oligoPlasmidPCR[1].trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",");
                String[] templates = rangeInString(templateStr);
                List<String> templateList = new ArrayList<>();
                for(String template : templates) {
                    templateList.add(template);
                }
                
                //evoke createPCR and return
                return createPCR(
                        oligosPCR,
                        templateList,
                        sizePCR,
                        productPCR
                );
                
                
            case pca://pca oligo1,oligo2 \t (product)
                
                String[] parenPCA = lineNoOP.split("\\(");
                //extract oligos
                String oliPCA = parenPCA[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+",",");
                //split oligos
                String[] oligosPCA = rangeInString(oliPCA);                
                //eliminate (), extract product
                String productPCA = parenPCA[1].replaceAll("\\(", "").replaceAll("\\)", "").trim();               
                //evoke createPCA and return                
                return createPCA(
                        oligosPCA,
                        productPCA
                );
                
                
            case digest://digest substrate with enzyme1,enzyme2 \t (product)
                
                //split ()
                String[] parenDig = lineNoOP.split("\\(");
                //split oligo and enzyme
                String[] oligoEnzyDig = parenDig[0].trim().split("\\s+with\\s+");
                //extract oligos
                String subDig = oligoEnzyDig[0].trim();
                //extract enzyme
                String enzDig = oligoEnzyDig[1].trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",");
                String[] enzyDig = enzDig.split("\\,");
                //extract product
                String pdtsection = parenDig[1].replaceAll("\\(", "").replaceAll("\\)", "").trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",");
                String[] pdtsplit = pdtsection.split(",");
                String fragsel = pdtsplit[0];
                String productDig = pdtsplit[1];
                //evoke createDigest and return
                return createDigest(
                        subDig,
                        enzyDig,
                        fragsel,
                        productDig
                );
                
                
            case ligate://ligate frag1,frag2 \t (product)
                
                //split()
                String[] segLig = lineNoOP.split("\\(");                               
                //extract frag
                String fragLig = segLig[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+",",");
                //split fragments
                String[] fragsLig = rangeInString(fragLig);                
                //extract product
                String productLig = segLig[1].replaceAll("\\(", "").replaceAll("\\)", "").trim();            
                //evoke createLigation and return
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
                String[] parenInTrans = parenTrans[1].replaceAll("\\(", "").replaceAll("\\)", "").trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",").split(",");
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
                //evoke createTransform and return
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
                String fragASB = parenASB[0].trim().replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",");
                //split fragments
                String[] fragsASB = rangeInString(fragASB);
                
                //split enzyme and product
                String parenInASB = parenASB[1].replaceAll("\\(","").replaceAll("\\)", "").trim();
                String[] enzyProdASB = parenInASB.replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",").split(",");
                //extract enzyme and product
                String enzymeASB = enzyProdASB[0].trim();
                String productASB = enzyProdASB[1].trim();
                
                //evoke createAssemble and return
                return createAssemble(
                        fragsASB,
                        enzymeASB,
                        productASB
                );
                
                
            case blunting://blunting sub1 \t (type,product)
                
                //split()
                String[] parenBlu = lineNoOP.split("\\(");
                
                //extract substrate
                String subBlu = parenBlu[0].trim();
                
                //split type and product
                String parenInBlu = parenBlu[1].replaceAll("\\(","").replaceAll("\\)", "").trim();
                String[] typeProdBlu = parenInBlu.replaceAll(",\\s+",",").replaceAll("\\s+,", ",").replaceAll("\\s+", ",").split(",");
                
                //extract type
                String typeBlu = typeProdBlu[0].trim();
                //extract product
                String productBlu = typeProdBlu[1].trim();
                
                return createBlunting(
                        subBlu,
                        typeBlu,
                        productBlu
                );
                                            
            default:
                throw new RuntimeException("Not implemented " + op);
        }
    }

    private Step createPCR(String[] oligos, List<String> templates, String size, String product) {    
        return new PCR(oligos[0], oligos[1], templates, product);
    }
    
    private Step createPCA(String[] oligos, String product) {
        List<String> frags = new ArrayList<>();
        for (String frag : oligos) {
            frags.add(frag);
        }
        return new PCA(frags, product);
    }
    
    private Step createDigest(String substrate, String[] enzymes, String fragsel, String product) {
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
        int fragSelect = Integer.parseInt(fragsel) - 1;
        return new Digestion(substrate, enzList, fragSelect, product);
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
    
    private Step createBlunting(String substrate, String type, String product){
        return new Blunting(substrate, type, product);
    }
    
    
    private String identifyRange(String input) throws Exception{
        
        String[] startEnd = input.split("-");
        
        String token1 = startEnd[0].trim();
        String token2 = startEnd[1].trim();

        // Find the boundary, i, between the base name and the numbers
        int i;
        for (i = 0; i < token1.length(); i++) {
            if (token1.charAt(i) != token2.charAt(i) || Character.isDigit(token1.charAt(i)) ) {
                break;
            }
        }

        // calculate the base name from the boundary
        String baseName = token1.substring(0, i);
        String output = token1;

        // obtain the range values from the rest of the tokens, and add to the out list by generating strings in that range
        try {
            int startInt = Integer.parseInt(token1.substring(i, token1.length()));
            int endInt = Integer.parseInt(token2.substring(i, token2.length()));
            for (int j = startInt + 1; j <= endInt; j++) {
                output = output + "," + baseName + j;
            }
        } catch (Exception e) {
            return input;//original str
            //throw new IllegalArgumentException("Invalid name range described. Must have same base name and only differ by integer values at the end.");
        }
        
        return output;
    }
    
    private String[] rangeInString(String in) throws Exception{
        
        String[] parts = in.split(",");
        String expand = null;
        for (String str: parts){
            if (str.contains("-")){
                expand = expand + identifyRange(str) + ",";
            }else{
                expand = expand + str + ",";
            }            
        }
        String result = expand.substring(4,expand.length()-1);
        String[] out = result.split(",");
        return out;
    }

    
    
    public static void main(String[] args) throws Exception {
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        
        String text = FileUtils.readResourceFile("constructionfile/data/Construction of aspC1.txt");
        ConstructionFile cf = pCF.run(text);
        
        System.out.println(cf.toString());
    }

}
