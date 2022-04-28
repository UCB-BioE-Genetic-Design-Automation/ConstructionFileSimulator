package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public interface Step {
    public Operation getOperation();
    public String getProduct();
    public List<String> getInputs();
}
