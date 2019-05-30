package org.ucb.c5.semiprotocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class RemoveContainer implements Task {
    private final String container;

    public RemoveContainer(String container) {
        this.container = container;
    }

    public String getContainer() {
        return container;
    }
    
    @Override
    public LabOp getOperation() {
        return LabOp.removeContainer;
    }
        
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getOperation()).append("\t");
        out.append(container);
        return out.toString();
    }
}
