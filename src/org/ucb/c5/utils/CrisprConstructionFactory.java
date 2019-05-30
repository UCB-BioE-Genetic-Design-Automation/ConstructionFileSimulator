package org.ucb.c5.utils;

import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.constructionfile.model.Acquisition;
import org.ucb.c5.constructionfile.model.Antibiotic;
import org.ucb.c5.constructionfile.model.Cleanup;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Enzyme;
import org.ucb.c5.constructionfile.model.Ligation;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

/**
 *
 * @author J. Christopher Anderson
 */
public class CrisprConstructionFactory {
    private final String template = "pTargetF";
    private final String strain = "DH10B";
    private final Antibiotic antibiotic = Antibiotic.Spec;
    
    public void initiate() throws Exception {}

    public ConstructionFile run(String oligo1name, String oligo2name, String pdtName) throws Exception {
        //Hard-code a ConstructionFile describing a CRISPR Experiment
        List<Step> steps = new ArrayList<>();
        
        ////acquire oligo ca4238,ca4239
        steps.add(new Acquisition(oligo1name));
        steps.add(new Acquisition(oligo2name));
        steps.add(new Acquisition(template));
        
        //pcr ca4238,ca4239 on pTargetF	(3927 bp, ipcr)
        steps.add(new PCR(oligo1name, oligo2name, template, "ipcr"));
        
        //cleanup ipcr	(pcr)
        steps.add(new Cleanup("ipcr", "pcr"));
        
        //digest pcr with SpeI,DpnI	(spedig)
        List<Enzyme> enzymes = new ArrayList<>();
        enzymes.add(Enzyme.SpeI);
        enzymes.add(Enzyme.DpnI);
        steps.add(new Digestion("pcr", enzymes, "spedig"));
        
        //cleanup spedig	(dig)
        steps.add(new Cleanup("spedig", "dig"));
        
        //ligate dig	(lig)
        List<String> digs = new ArrayList<>();
        digs.add("dig");
        steps.add(new Ligation(digs, "lig"));
        
        //transform lig	(DH10B, Spec)
        steps.add(new Transformation("lig", strain, antibiotic, pdtName));
        
        //Instantiate the Construction File
        ConstructionFile constf = new ConstructionFile("ID", steps, "pdt", null);
        return constf;
    }
    
    public static void main(String[] args) throws Exception {
        //Initializze the Function
        CrisprConstructionFactory factory = new CrisprConstructionFactory();
        factory.initiate();
        
        //Run the factory
        ConstructionFile constf = factory.run("fb21", "fb22", "pTarg-amilGFP");
    
        //Print it out
        for(Step astep : constf.getSteps()) {
            System.out.println(astep.getOperation() + "   " + astep.getProduct() + "   " +  astep.getClass().toString());
        }
    }

}
