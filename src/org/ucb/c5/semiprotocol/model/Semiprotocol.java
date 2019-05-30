package org.ucb.c5.semiprotocol.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public class Semiprotocol {

    private final List<Task> steps;

    public Semiprotocol(List<Task> steps) {
        this.steps = steps;
    }

    public List<Task> getSteps() {
        return steps;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Task task : getSteps()) {
            sb.append(task.toString()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String pcrTube = "pcr";

        //Hard-code an instance of Semiprotocol to set up a PCR
        List<Task> steps = new ArrayList<>();

        //Label a new PCR tube "pcr" and put it at position A1 of the PCR rack
        steps.add(new AddContainer(Container.pcr_tube, pcrTube, "pcr/A1", true));

        //Get the oligo dilutions and template plasmid
        String primer1 = "10uM_ca4298";
        String primer2 = "10uM_ca4299";
        String template = "pTargetF";
        steps.add(new AddContainer(Container.eppendorf_1p5mL, primer1, "tube/A1", false));
        steps.add(new AddContainer(Container.eppendorf_1p5mL, primer2, "tube/A3", false));
        steps.add(new AddContainer(Container.eppendorf_1p5mL, template, "tube/A5", false));

        //Add 28.5 uL Water
        steps.add(new Dispense(Reagent.water, pcrTube, 28.5));

        //Add 10 uL Buffer
        steps.add(new Dispense(Reagent.Q5_Polymerase_Buffer_5x, pcrTube, 10.0));

        //Add 5 uL dNTPs
        steps.add(new Dispense(Reagent.dNTPs_2mM, pcrTube, 5.0));

        //Add Oligos and Template
        steps.add(new Transfer(primer1, pcrTube, 2.5));
        steps.add(new Transfer(primer2, pcrTube, 2.5));
        steps.add(new Transfer(template, pcrTube, 1.0));

        //Add the polymerase
        steps.add(new Dispense(Reagent.Q5_polymerase, pcrTube, 0.5));

        //Instantiate the Semiprotocol
        Semiprotocol semi = new Semiprotocol(steps);
        System.out.println(semi);
    }
}
