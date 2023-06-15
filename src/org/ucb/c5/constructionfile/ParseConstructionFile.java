package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.utils.Log;

/**
 *
 * @author J. Christopher Anderson, Connor Tou, Zexuan Zhao, Zihang Shao
 */
public class ParseConstructionFile {
    private CFShorthandParser shortParser;
    private OriginalCFParser originalParser;
    private SerializeConstructionFile serializer;
    
    public void initiate() throws Exception {
        shortParser = new CFShorthandParser();
        originalParser = new OriginalCFParser();
        originalParser.initiate();
        serializer = new SerializeConstructionFile();
        serializer.initiate();
    }

    public ConstructionFile run(String rawText) throws Exception {
        try {
            ConstructionFile cf = originalParser.run(rawText);
            Log.info("OriginalCFParser parsed Construction File:\n" + serializer.run(cf));
            return cf;
        } catch(Exception err) {}
        ConstructionFile cf = shortParser.run(rawText);
        Log.info("shortParser parsed Construction File:\n" + serializer.run(cf));
        return cf;
    }
}
