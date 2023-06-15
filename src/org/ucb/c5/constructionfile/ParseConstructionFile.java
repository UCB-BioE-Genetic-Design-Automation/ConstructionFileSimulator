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
        //There are 2 parsers, one for an old style, one for a new style
        //The first step is to try and detect which style is used
        boolean isOldStyle = false;
        String[] lines = rawText.split("\\r|\\r?\\n");
        for (String line : lines) {
            String lower = line.trim().toLowerCase();
            if (lower.endsWith(")")) {
                isOldStyle = true;
                break;
            }
            if (lower.startsWith("pcr")) {
                if (lower.indexOf(" on ") > 0) {
                    isOldStyle = true;
                    break;
                }
                if (lower.indexOf(" bp,") > 0) {
                    isOldStyle = true;
                    break;
                }
            }
        }

        //If it is oldstyle, try parsing it as such
        if (isOldStyle) {
            try {
                ConstructionFile cf = originalParser.run(rawText);
                Log.info("OriginalCFParser parsed Construction File:\n" + serializer.run(cf));
                return cf;
            } catch (Exception err) {
            }
        }

        //Parse it as newstyle
        ConstructionFile cf = shortParser.run(rawText);
        Log.info("shortParser parsed Construction File:\n" + serializer.run(cf));
        return cf;
    }
}
