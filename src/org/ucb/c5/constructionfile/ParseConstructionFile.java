package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.utils.FileUtils;
import org.ucb.c5.utils.ChangeableString;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author J. Christopher Anderson
 * @rewritten by Connor Tou, Zexuan Zhao
 * @rewritten by Zihang Shao
 * New thing for steps: after instantiate a step...
 * 1. set previous step, which is the direct previous step in CF in a linear order
 * 2. populate parental steps, which generate a tree relationship of steps in a CF (not visualizable)
 * 3. set stepID, which is a concatenation of CFID and it's position in the CF
 *
 */
public class ParseConstructionFile {

    
    public void initiate() {
    }
    
    
    public ConstructionFile run(String rawText) throws Exception {
        // Create a map to store given sequences and a list to store the steps
        HashMap<String, String> sequences = new HashMap<>();
        List<Step> steps = new ArrayList<>();
        // Initiate CFID and plasmidName
        ChangeableString plasmidName = new ChangeableString("");
        ChangeableString CFID = new ChangeableString("");
        // Split the raw text into the steps and sequences section
        
//        int stepSym = rawText.indexOf(">");
//        String rawText1 = rawText.substring(stepSym+1,rawText.length());
//        int seqSym = rawText1.indexOf(">");
        //String[] sections = rawText.split("\\>");
        
        String[] lines = rawText.split("\n");
        int seqStart = 0;
        StringBuilder stepSecSB = new StringBuilder();
        
        for(int i=0;i<lines.length-2;i++){
            
            if(lines[i+2].matches("[ATCGatcg]+")){
                break;
            }else{
                
                stepSecSB.append(lines[i]);
                stepSecSB.append("\n");
            }
        }
        
        
        String stepSection = stepSecSB.toString();
        int start = stepSection.length();
//        stepSection = stepSection.replace(">", "");
        String seqSection = rawText.substring(start);
        
        
//        //step section without starting >
//        String stepSection = rawText1.substring(0,seqSym-1);
//        //seq section with all >
//        String seqSection = rawText1.substring(seqSym,rawText.length()-1);
//        
        
        
        processSteps(stepSection, CFID, plasmidName, steps);
        processSequences(seqSection, sequences);
        return new ConstructionFile(CFID.toString(), steps, plasmidName.toString(), sequences);
    }
    
    
    
    
    
    
    
    
    
    

    private void processSteps(String rawText, ChangeableString CFID, ChangeableString plasmidName, List<Step> steps) throws Exception {
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
        text = text.replaceAll("\t", " ");

        //Break it into lines
        String[] lines = text.split("\n");
        //Parse out the name of the plasmid
        plasmidName.changeTo(lines[0].split("\\s+")[1]);

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

            //Parse out the construction file ID
            if (aline.startsWith("ID")) {
                CFID.changeTo(aline.split("\\s+")[1]);
                continue;
            }
            
            //Ignore title
            if (aline.toLowerCase().startsWith(">")){
                continue;
            }

            //Try to parse the operation, if fails will throw Exception
            String[] spaces = aline.split("\\s+");
            Operation op;
            try {
                op = Operation.valueOf(spaces[0]);
            }
            catch(Exception IllegalArgumentException) {
                op = Operation.valueOf(spaces[0].toLowerCase());
            }
            //If past the gauntlet, keep the line
            Step parsedStep = parseLine(op, spaces, plasmidName.toString());
            parsedStep.set_str(aline);
            parsedStep.setSessionID("_");
            steps.add(parsedStep);
        }
        /*
        if(CFID.equals("")){
            throw new Exception("No CFID");
        } */
        //set Step ID and previous Step
        /* for (int i = 0; i < steps.size(); i++){
            steps.get(i).setStepID(CFID + String.valueOf(i));
            if(i >0){
                steps.get(i).setPreviousStep(steps.get(i-1));
            }
            steps.get(i).populateParentSteps();
        } */
    }

    private void processSequences(String seqSection, HashMap<String, String> sequences){
        //Break it into lines
////        String[] seqSections = rawText.split(">");        
////        
////        for (int i = 1; i < seqSections.length; i++) {        
////          
////            String currSection = seqSections[i];
////            
////            // Ignore blank lines
////            if (currSection.trim().isEmpty()) {                
////                          
////                continue;
////            }
////            // Ignore commented-out lines
////            if (currSection.startsWith("//")) {
////                continue;
////            }
////            // Split the sequence section by the newline character
////            currSection = currSection.replaceAll("\r", "\n");
////            currSection = currSection.replaceAll("\t", "");
////            String[] splitSection = currSection.split("\n");
////            String seqName = splitSection[0].split(" ")[0];
////            String seqContents = "";
////            for (int j = 1; j < splitSection.length; j++) {
////                seqContents = seqContents.concat(splitSection[j]);
////            }
////            // Save to the sequences dictionary
////            sequences.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContents.replaceAll("(\\r|\\n|\\s)", ""));
////        }
//
//        String[] seqSections = seqSection.split(">");
//
//        for (int i = 1; i < seqSections.length; i++) {
//    
//            String currSection = seqSections[i];
//            
//            String[] currSecLn = currSection.split("\n");
//                       
////            for (int i1 = 0; i<currSecLn.length; i1++){
////                // Ignore blank lines
////                if (currSecLn[i1].trim().isEmpty() == true) {
////                //currSecLn[i1] = null;
////                continue;
////                }
////                // Ignore commented-out lines
////                if (currSecLn[i1].startsWith("//") == true) {
////                //currSecLn[i1] = null;
////                continue;
////                }
////                
////            }
//                    
//            String seqName = currSecLn[0];
//            String seqContents = currSecLn[1];
//            sequences.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContents.replaceAll("(\\r|\\n|\\s)", ""));
//            
//            // Split the sequence section by the newline character
////            currSection = currSection.replaceAll("\r", "\n");
////            currSection = currSection.replaceAll("\t", "");
////            String[] splitSection = currSection.split("\n");
////            String seqName = splitSection[0].split(" ")[0];
////            String seqContents = "";
////            for (int j = 1; j < splitSection.length; j++) {
////                seqContents = seqContents.concat(splitSection[j]);
////            }
////            // Save to the sequences dictionary
////            sequences.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContents.replaceAll("(\\r|\\n|\\s)", ""));
////        
//        }
        
        
        String[] lines = seqSection.split("\n");
        
        for(int j=0;j<lines.length-1;j++){
            
            //Ignore blank lines
            if (lines[j].trim().isEmpty()) {
                continue;
            }

            //Ignore commented-out lines
            if (lines[j].startsWith("//")) {
                continue;
            }
            if(lines[j].matches("[ATCGatcg]+") == false && lines[j+1].matches("[ATCGatcg]+")){
                String seqName = lines[j].replace(">", "");
                String seqContent = lines[j+1];
                sequences.put(seqName.replaceAll("(\\r|\\n|\\s)", ""), seqContent.replaceAll("(\\r|\\n|\\s)", ""));

                
            }
        }
        
        
    }

    private Step parseLine(Operation op, String[] spaces, String plasmidName) {
        
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
                return createTransform(
                        spaces[1],
                        spaces[2].replace(",", ""),
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
        String CFPath = System.getProperty("user.dir") + "/src/org/ucb/c5/constructionfile/data/pcrtest.txt";
        File CFFile = new File(CFPath);
        String text = FileUtils.readFile(CFFile.getAbsolutePath());
        ConstructionFile cf = pCF.run(text);
        /*
        String CFDir = System.getProperty("user.dir") + "/src/org/ucb/c5/Assigner/constructionfile/data";
        System.out.println(CFDir);
        ParseConstructionFile pCF = new ParseConstructionFile();
        pCF.initiate();
        File CFdir = new File(CFDir);
        File[] CFfiles = CFdir.listFiles();
        if (CFfiles != null) {
            for (int i = 0; i < CFfiles.length; i++) {
                if (CFfiles[i].getName().equals(".DS_Store")) {
                    continue;
                }
                String text = FileUtils.readFile(CFfiles[i].getAbsolutePath());
                ConstructionFile cf = pCF.run(text);
                for (Step step : cf.getSteps()) {
                    System.out.println(step.get_str() + ":");
                    System.out.println(step.getSubstrates());
                }
            }
        } */
    }
}
