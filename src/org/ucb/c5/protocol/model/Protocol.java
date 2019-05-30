package org.ucb.c5.protocol.model;

import java.util.List;

/**
 * Describes a sequence of wetlab manipulations modeled
 * as abstract Steps
 * 
 * @author J. Christopher Anderson
 */
public class Protocol {
    private final List<Step> steps;
    
    public Protocol(List<Step> steps) {
        this.steps = steps;
    }
    
    public Step getStep(int index) {
        return steps.get(index);
    }
    
    public int getSize() {
        return steps.size();
    }
}
