package org.ucb.c5.protocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class Step {
    private final Operation operation;    //An enum, like "transfer"
    private final Reagent reagent1;       //A Reagent to be dispensed
    private final Reagent reagent2;       //A Reagent to be dispensed
    private final String source;          //The sample in question, aspirate container for a transfer
    private final String destination;     //The container in which dispense would occur
    private final Double volume;          //The volume transferred
    private final ThermoProgram program;  //A thermocycler program

    public Step(Operation operation, Reagent reagent1, Reagent reagent2, String source, String destination, Double volume, ThermoProgram program) {
        this.operation = operation;
        this.reagent1 = reagent1;
        this.reagent2 = reagent2;
        this.source = source;
        this.destination = destination;
        this.volume = volume;
        this.program = program;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getVolume() {
        return volume;
    }
    
    public Reagent getReagent1() {
        return reagent1;
    }
    
    public Reagent getReagent2() {
        return reagent2;
    }
    
    public ThermoProgram getProgram() {
        return program;
    }
}
