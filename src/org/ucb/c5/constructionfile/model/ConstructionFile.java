package org.ucb.c5.constructionfile.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author J. Christopher Anderson
 * @edit Zihang Shao
 */
public class ConstructionFile {
    private final List<Step> steps;
    private String pdtName = null;
    private final String ID;
    private final Map<String, String> sequences;


    public ConstructionFile(String ID, List<Step> steps, String pdtName, Map<String, String> sequences) {
        this.ID = ID;
        this.steps = steps;
        this.pdtName = pdtName;
        this.sequences = sequences;
    }
    public List<Step> getSteps() {
        return steps;
    }

    public String getPdtName() {
        return pdtName;
    }

    public String getID(){
        return ID;
    }

    public Map<String, String> getSequences() {
        return sequences;
    }
    
    public static void main(String[] args) {
        //Hard-code a ConstructionFile describing a CRISPR Experiment
        List<Step> steps = new ArrayList<>();
        
        //>Construction of pTarg-amilGFP1
        String pdtName = "pTarg-amilGFP";
        
        ////acquire oligo ca4238,ca4239
        steps.add(new Acquisition("ca4238"));
        steps.add(new Acquisition("ca4239"));
        steps.add(new Acquisition("pTargetF"));
        
        //pcr ca4238,ca4239 on pTargetF	(3927 bp, ipcr)
        steps.add(new PCR("ca4238", "ca4239", "pTargetF", "ipcr"));
        
        //cleanup ipcr	(pcr)
//        steps.add(new Cleanup("ipcr", "pcr"));
        
        //digest pcr with SpeI,DpnI	(spedig)
        List<Enzyme> enzymes = new ArrayList<>();
        enzymes.add(Enzyme.SpeI);
        enzymes.add(Enzyme.DpnI);
        steps.add(new Digestion("pcr", enzymes, "spedig"));
        
        //cleanup spedig	(dig)
//        steps.add(new Cleanup("spedig", "dig"));
        
        //ligate dig	(lig)
        List<String> digs = new ArrayList<>();
        digs.add("dig");
        steps.add(new Ligation(digs, "lig"));
        
        //transform lig	(DH10B, Spec)
        steps.add(new Transformation("lig", "DH10B", Antibiotic.Spec, pdtName));

        //Instantiate the Construction File
        ConstructionFile constf = new ConstructionFile("ID", steps, "pdt", null);
        
        //Print it out
        for(Step astep : constf.getSteps()) {
            System.out.println(astep.getOperation() + "   " + astep.getProduct() + "   " +  astep.getClass().toString());
       }
    }
}
