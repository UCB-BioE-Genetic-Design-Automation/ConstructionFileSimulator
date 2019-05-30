/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.protocol;

import org.ucb.c5.protocol.model.Operation;
import org.ucb.c5.protocol.model.Protocol;
import org.ucb.c5.protocol.model.Step;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class SerializeProtocol {
    public String run(Protocol protocol) throws Exception {
        StringBuilder serialization = new StringBuilder();
        
        //Scan through each Step and serialize
        for(int i=0; i<protocol.getSize(); i++) {
            Step astep = protocol.getStep(i);
            //Start a new line
            serialization.append("\n");
            
            //Put in the operation
            Operation operation = astep.getOperation();
            serialization.append(operation.toString()).append("\t");
            
            switch(operation) {
                case dispense:
                    serialization.append(astep.getReagent1().toString()).append("\t");
                    serialization.append(astep.getDestination()).append("\t");
                    serialization.append(astep.getVolume()).append("\t");
                    break;
                case transfer:
                    serialization.append(astep.getSource()).append("\t");
                    serialization.append(astep.getDestination()).append("\t");
                    serialization.append(astep.getVolume()).append("\t");
                    break;
                case mix:
                    serialization.append(astep.getSource()).append("\t");
                    break;
                case cleanup:
                    serialization.append(astep.getSource()).append("\t");
                    serialization.append(astep.getDestination()).append("\t");
                    serialization.append(astep.getVolume()).append("\t");
                    break;
                case thermocycle:
                    serialization.append(astep.getSource()).append("\t");
                    serialization.append(astep.getProgram().toString()).append("\t");
                    break;
                case transform:
                    serialization.append(astep.getSource()).append("\t");
                    serialization.append(astep.getReagent1().toString()).append("\t");
                    serialization.append(astep.getReagent2().toString()).append("\t");
                    serialization.append(astep.getDestination()).append("\t");
                    break;
                default:
                    throw new RuntimeException("Unsupported operation: " + operation);
            
            }
        }
        
        return serialization.toString();
    }
    public static void main(String[] args) throws Exception {
        //Parse the human-written protocol
        String text = FileUtils.readResourceFile("protocol/data/crispr_example_protocol.txt");
        ParseProtocol parser = new ParseProtocol();
        Protocol protocol = parser.run(text);
        
        //Serialize it to String
        SerializeProtocol serializer = new SerializeProtocol();
        String data = serializer.run(protocol);
        System.out.println(data);
        
        //Parse it back to a Protocol
        Protocol protocol2 = parser.run(data);
        
        //Inspect it
        if(protocol.getSize() == protocol2.getSize()) {
            System.out.println("OK on size");
        }
    }
}
