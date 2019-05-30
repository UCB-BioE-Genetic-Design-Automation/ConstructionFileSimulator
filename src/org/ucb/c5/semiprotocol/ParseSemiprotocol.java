package org.ucb.c5.semiprotocol;

import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class ParseSemiprotocol {
    public void initiate() throws Exception {}
    
    public Semiprotocol run(String text) throws Exception {
        text = text.replaceAll("\"", "");
        String[] lines = text.split("\\r|\\r?\\n");
        List<Task> steps = new ArrayList<>();

        //Process each good line
        for (int i = 0; i < lines.length; i++) {
            String aline = lines[i];

            //Ignore blank lines
            if (aline.trim().isEmpty()) {
                continue;
            }
            
            //Ignore commented out lines
            if (aline.trim().startsWith("//")) {
                continue;
            }
            
            String[] tabs = aline.split("\t");
            LabOp operation = LabOp.valueOf(tabs[0]);
            
            steps.add(createTask(tabs, operation));
        }
        
        return new Semiprotocol(steps);
    }
    
    private Task createAddContainer(String[] tabs) {
        //addContainer	pcr_plate_96	alibaba_oligos	deck/A1
        Container tubeType = Container.valueOf(tabs[1]);
        String name = tabs[2];
        String location = tabs[3];
        boolean isnew = Boolean.parseBoolean(tabs[4]);
        return new AddContainer(tubeType, name, location, isnew);
    }

    private Task createRemoveContainer(String[] tabs) {
        return new RemoveContainer(tabs[1]);
    }

    private Task createTransfer(String[] tabs) {
        String source = tabs[1];
        String dest = tabs[2];
        double volume = Double.parseDouble(tabs[3]);
        return new Transfer(source, dest, volume);
    }

    private Task createDispense(String[] tabs) {
        Reagent reagent = Reagent.valueOf(tabs[1]);
        String dstContainer = tabs[2];
        double volume = Double.parseDouble(tabs[3]);
        return new Dispense( reagent,  dstContainer,  volume);
    }

    private Task createMultichannel(String[] tabs) {
        String sourceStart = tabs[1];
        String sourceEnd = tabs[2];
        String destStart = tabs[3];
        String destEnd = tabs[4];
        double volume = Double.parseDouble(tabs[5]);
        return new Multichannel( sourceStart,  sourceEnd,  destStart,  destEnd,  volume);
    }

    private Task createTask(String[] tabs, LabOp operation) {
        switch (operation) {
            case addContainer:
                return createAddContainer(tabs);
            case removeContainer:
                return createRemoveContainer(tabs);
            case transfer:
                return createTransfer(tabs);
            case dispense:
                return createDispense(tabs);
            case multichannel:
                return createMultichannel(tabs);
        }
        
        throw new RuntimeException("Operation requested that cannot be parsed " + operation);
    }
    
    public static void main(String[] args) throws Exception {
        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);
        System.out.println("# steps: " + protocol.getSteps().size());  //protocol has 10 steps
    }

}
