package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.*;

/**
 *
 * @author J. Christopher Anderson, Connor Tou, Zexuan Zhao, Zihang Shao
 */
public class ParseConstructionFile {
    private CFShorthandParser shortParser;
    private OriginalCFParser originalParser;
    
    public void initiate() {
        shortParser = new CFShorthandParser();
        originalParser = new OriginalCFParser();
        originalParser.initiate();
    }

    public ConstructionFile run(String rawText) throws Exception {
        try {
            return originalParser.run(rawText);
        } catch(Exception err) {}
        return shortParser.run(rawText);
    }
}
