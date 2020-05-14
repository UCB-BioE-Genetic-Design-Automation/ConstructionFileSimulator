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
    private final Map<String, Polynucleotide> sequences;


    public ConstructionFile(List<Step> steps, String pdtName, Map<String, Polynucleotide> sequences) {
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

    public Map<String, Polynucleotide> getSequences() {
        return sequences;
    }
    
    public static void main(String[] args) {
        //Hard-code a ConstructionFile describing a CRISPR Experiment
        List<Step> steps = new ArrayList<>();
        
        //>Construction of pTarg-amilGFP1
        String pdtName = "pTarg-amilGFP";
        
        //pcr ca4238,ca4239 on pTargetF	(3927 bp, ipcr)
        List<String> templates = new ArrayList();
        templates.add("pTargetF");
        steps.add(new PCR("ca4238", "ca4239", templates, "ipcr"));
        
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
        ConstructionFile constf = new ConstructionFile(steps, "pdt", null);
        
        //Print it out
        for(Step astep : constf.getSteps()) {
            System.out.println(astep.getOperation() + "   " + astep.getProduct() + "   " +  astep.getClass().toString());
       }
    }
}
