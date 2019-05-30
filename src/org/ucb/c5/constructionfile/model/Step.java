package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 * @author lucaskampman
 * @author Connor Tou
 */
public class Step {
    private String _stepID;

    /**
     * The step immediately above this one in their ConstructionFile.
     */
    private Step _previousStep = null;

    /** substrates, e.g. fragments in a ligation or assembly */
    private List<String> _substrates;

    /**
     * One or more steps whose products are used in the CF as substrates in this step.
     * This will not necessarily contain the previousStep.
     * */
    private List<Step> _parentSteps;
    private String _outputID;
    // private Container _outputContainer;
    private Operation _operation;

    private String _product;
    private String _sessionID;

    private String _str;

    public Step (){
        _sessionID = "_";
        _substrates = null;
        _previousStep = null;
        _parentSteps = new ArrayList<>();
        _substrates = new ArrayList<>();
    }

    /** Get the ID associated with the session this Step belonds to. */
    public String getSessionID() {
        return _sessionID;
    }

    /** Set the ID associated with the session this Step belonds to. */
    public void setSessionID(String sessionID) {
        this._sessionID = sessionID;
    }

    /** Set the ID associated with this step. */
    public void setStepID(String stepID){
        _stepID = stepID;
    }

    /** Set the line for the step, i.e. the line it appears as in the ConstructionFile. */
    public void set_str(String str){this._str = str;}

    /** Get the line for the step, i.e. the line it appears as in the ConstructionFile. */
    public String get_str() {return _str;}

    /** Get the ID associated with this step. */
    public String getStepID(){
        return _stepID;
    }

    public void setSubstrates(Collection<String> substrates){
        _substrates.addAll(substrates);
    }
    public Collection<String> getSubstrates(){return this._substrates;}
    /** returns the type of operation of the step. */
    public Operation getOperation() {
        return _operation;
    };

    /** returns the product of the step, which is used to connect steps which have dependencies in the ConstructionFile. */
    public String getProduct(){
        return _product;
    };

    /** Set the product of the Step to be a value specifies by the
     * @param _product
     */
    public void setProduct(String _product) {
        this._product = _product;
    }

    /** Get the previous step, i.e. the one that appears directly before it in a ConstructionFile. */
    public Step getPreviousStep(){
        return _previousStep;
    }

    /** Set the previous step, i.e. the one that appears directly before it in a ConstructionFile. */
    public void setPreviousStep(Step previousStep){
        _previousStep = previousStep;
    }

    /** Get the ID of the product of this step. */
    public String getOutputID(){
        return _outputID;
    };

    /** Set the ID of the product of this step
     * @param outputID is the ID of the product.
     *
     *                 TODO: it's a problem that this is faded, because I've called getOutputID elsewhere (in MakeInoculate)
     */
    public void setOutputID(String outputID){
        _outputID = outputID;
    };
    /*
    public Container getOutputContainer(){
        return _outputContainer;
    };

    public void setOutputContainer(Container outputContainer){
        _outputContainer = outputContainer;
    }; */
    //change back to private, just changed for sake of manual input for now
    public void addParentStep(Step ancestorStep) {
        _parentSteps.add(ancestorStep);
    }

    public List<Step> getParentSteps(){
        return _parentSteps;
    }

    /**
     * If it has not been done before, run through all of the previous steps and add those
     * which contain substrates of this step to a list of ParentSteps
     */
    public void populateParentSteps(){
        // Case 0: _substrates is empty, and _parentSteps is too
        if (_substrates.size() == 0 && _parentSteps.size() == 0){
            return;
        }

        // Case 1: ParentSteps is already populated and its size is the same as substrates, which is not empty.
        if (_parentSteps.size() == _substrates.size() && _substrates.size() > 0 ) {
            return;
        }

        // Case 2: _parentSteps is empty, but there are items in _substrate
        else {
            Step currStep = this;
            while (_parentSteps.size() < _substrates.size() && currStep != null){
                currStep = this.getPreviousStep();
                if (currStep == null) {
                    throw new IllegalArgumentException("A substrate in the construction file does " +
                            "not have a corresponding product in a previous step.");
                }
                for (String substrate : _substrates) {
                    if (currStep.getProduct().equalsIgnoreCase(substrate)){
                        _parentSteps.add(currStep);
                    }
                }
            }
        }
    }
}
