package org.ucb.c5.constructionfile;
import java.util.HashMap;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Polynucleotide;

/*
@author Zachary Scheftel
Instantiate a ValidateConstructionFile object, which can decide if a given ConstructionFile will yield the given product
 */

// ValidateConstructionFile validator = ValidateConstructionFile();
// for whatever sequence
// boolean currCFValid = validator.run(currCF, currProduct)

// Returns true if the construction file matches the input string
public class ValidateConstructionFile {
    public void initiate() {
    }
    public boolean run(ConstructionFile CF, Polynucleotide product) throws Exception {
        SimulateConstructionFile simulator = new SimulateConstructionFile();
        Polynucleotide seq = simulator.run(CF, new HashMap<>());
        // Take into account that the product may be rotated
        return (seq.equals(product));
    }
}
