package org.ucb.c5.constructionfile;

import java.util.List;
import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.utils.CrisprConstructionFactory;

/**
 *
 * @author J. Christopher Anderson
 */
public class SerializeConstructionFile {
    
    public void initiate() throws Exception {}
    
    /**
     * Serialize a ConstructionFile to a human-readable String
     * 
     * @param constf  The Construction File
     * @return A human-readable String representation
     * @throws Exception 
     */
    public String run(ConstructionFile constf) throws Exception {
        StringBuilder sb = new StringBuilder();
        
        //Figure out the product name
        List<Step> steps = constf.getSteps();
        Step laststep = steps.get(steps.size()-1);
        String productname = laststep.getProduct();
        
        //Abort if malformed
        if(laststep.getOperation() != Operation.transform) {
            throw new RuntimeException("Construction Files should end with a transformation");
        }
        
        sb.append(">Construction of " + productname).append("\n");
        
        //Serialize each Step
        for(Step astep : constf.getSteps()) {
            Operation op = astep.getOperation();
            switch(op) {
                case acquire:
                    Acquisition aq = (Acquisition) astep;
                    sb.append(op.toString()).append(" ").append(aq.getProduct()).append("\n");
                    break;
                case pcr:
                    PCR pcr = (PCR) astep;
                    sb.append(op.toString()).append(" ").append(pcr.getOligo1()).append(",").append(pcr.getOligo1()).append(" on ").append(pcr.getTemplate()).append("\t(").append(pcr.getProduct()).append(")").append("\n");
                    break;
                case pca:
                    PCA pca = (PCA) astep;
                    sb.append(op.toString()).append(" ");
                    for(int i=0; i< pca.getOligoPool().size(); i++) {
                        String oligo = pca.getOligoPool().get(i);
                        sb.append(oligo);
                        if(i < pca.getOligoPool().size()-1) {
                            sb.append(",");
                        }
                    }
                    sb.append("\t(").append(pca.getProduct()).append(")").append("\n");
                    break;
                case digest:
                    Digestion dig = (Digestion) astep;
                    sb.append(op.toString()).append(" ").append(dig.getSubstrate()).append(" with ");
                    
                    for(int i=0; i< dig.getEnzymes().size(); i++) {
                        Enzyme enz = dig.getEnzymes().get(i);
                        sb.append(enz.toString());
                        if(i < dig.getEnzymes().size()-1) {
                            sb.append(",");
                        }
                    }
                    sb.append("\t(").append(dig.getProduct()).append(")").append("\n");
                    break;
                case ligate:
                    Ligation lig = (Ligation) astep;
                    sb.append(op.toString()).append(" ");
                    for(int i=0; i< lig.getFragments().size(); i++) {
                        String frag = lig.getFragments().get(i);
                        sb.append(frag);
                        if(i < lig.getFragments().size()-1) {
                            sb.append(",");
                        }
                    }
                    sb.append("\t(").append(lig.getProduct()).append(")").append("\n");
                    break;
                case transform:
                    Transformation trans = (Transformation) astep;
                    sb.append(op.toString()).append(" ").append(trans.getDna());
                    sb.append("\t(").append(trans.getStrain()).append(", ").append(trans.getAntibiotic()).append(")").append("\n");
                    break;
//                case cleanup:
//                    Cleanup cleanup = (Cleanup) astep;
//                    sb.append(op.toString()).append(" ").append(cleanup.getSubstrate()).append("\t(").append(cleanup.getProduct()).append(")").append("\n");
//                    break;
                case assemble:
                    Assembly assem = (Assembly) astep;
                    sb.append(op.toString()).append(" ");
                    for(int i=0; i< assem.getFragments().size(); i++) {
                        String frag = assem.getFragments().get(i);
                        sb.append(frag);
                        if(i < assem.getFragments().size()-1) {
                            sb.append(",");
                        }
                    }
                    sb.append("\t(").append(assem.getProduct()).append(")").append("\n");
                    break;
            }
        }
        
        return sb.toString().trim();
    }
    
    public static void main(String[] args) throws Exception {
        //Initializze the Function
        SerializeConstructionFile serilaizer = new SerializeConstructionFile();
        serilaizer.initiate();
        CrisprConstructionFactory factory = new CrisprConstructionFactory();
        factory.initiate();
        
        //Run the factory
        ConstructionFile constf = factory.run("fb21", "fb22", "pTarg-amilGFP");
    
        //Serilizae
        String text = serilaizer.run(constf);
        System.out.println(text);
    }
}
