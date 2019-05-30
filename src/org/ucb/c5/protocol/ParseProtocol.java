package org.ucb.c5.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.protocol.model.Operation;
import org.ucb.c5.protocol.model.Protocol;
import org.ucb.c5.protocol.model.Reagent;
import org.ucb.c5.protocol.model.Step;
import org.ucb.c5.protocol.model.ThermoProgram;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class ParseProtocol {

    public Protocol run(String rawText) throws Exception {
        String[] lines = rawText.split("\\r|\\r?\\n");
        List<Step> steps = new ArrayList<>();

        for (int i=1; i<lines.length; i++) {
            String aline = lines[i];
            
            //Ignore blank lines
            if (aline.trim().isEmpty()) {
                continue;
            }

            //Ignore commented-out lines
            if (aline.startsWith("//")) {
                continue;
            }

            //Parse the step
            Step step = null;
            String[] tabs = aline.split("\t");

            Operation operation = Operation.valueOf(tabs[0]);
            
            switch(operation) {
                case dispense:
                    Reagent rgt = Reagent.valueOf(tabs[1]);
                    String dest = tabs[2];
                    Double vol = Double.parseDouble(tabs[3]);
                    step = new Step(Operation.dispense, rgt, null, null, dest, vol, null);
                    break;
                case transfer:
                    String src2 = tabs[1];
                    String dest2 = tabs[2];
                    Double vol2 = Double.parseDouble(tabs[3]);
                    step = new Step(Operation.transfer, null, null, src2, dest2, vol2, null);
                    break;
                case mix:
                    String src3 = tabs[1];
                    step = new Step(Operation.mix, null, null, src3, null, null, null);
                    break;
                case cleanup:
                    String src3b = tabs[1];
                    String nameb = tabs[2];
                    Double vol2b = Double.parseDouble(tabs[3]);
                    step = new Step(Operation.mix, null, null, src3b, nameb, vol2b, null);
                    break;
                case thermocycle:
                    String src4 = tabs[1];
                    ThermoProgram program = ThermoProgram.valueOf(tabs[2]);
                    step = new Step(Operation.thermocycle, null, null, src4, null, null, program);
                    break;
                case transform:
                    String lig = tabs[1];
                    Reagent dh10b = Reagent.valueOf(tabs[2]);
                    Reagent spec = Reagent.valueOf(tabs[3]);
                    String targ = tabs[4];
                    step = new Step(Operation.transform, dh10b, spec, lig, targ, null, null);
                    break;
                default:
                    throw new RuntimeException("Unsupported operation: " + operation);
            }
            
            steps.add(step);
        }

        return new Protocol(steps);
    }

    public static void main(String[] args) throws Exception {
        String text = FileUtils.readResourceFile("protocol/data/crispr_example_protocol.txt");
        
        ParseProtocol parser = new ParseProtocol();
        
        Protocol protocol = parser.run(text);
        
        for(int i=0; i<protocol.getSize(); i++) {
            Step astep = protocol.getStep(i);
            System.out.println(astep.getOperation() + "  " + astep.getDestination());
        }
        System.out.println("done");
    }
}
