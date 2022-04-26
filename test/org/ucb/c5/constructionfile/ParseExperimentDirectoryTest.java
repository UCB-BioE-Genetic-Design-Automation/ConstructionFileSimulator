package org.ucb.c5.constructionfile;

import org.junit.Test;

/**
 *
 * @author Yuting Wang
 */
public class ParseExperimentDirectoryTest {

    @Test
    /**
     * Parses the lyc20 example containing a correct construction file for pLYC73H.
     * Should correctly parse the file without throwing an Exception.
     *
     * This folder contains duplicate oligo blah in the oligo file, and duplicate
     * plasmid p20N100 in the duplicate_seq folder.  These duplications do not
     * change the sequence, and thus it should still parse correctly.
     */
    public void TestLYC21() throws Exception {
        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        //correct
        String relative = "/test/org/ucb/c5/constructionfile/data/lyc20";
        String absolute = getResourcePath(relative);
        parseFolder.run(absolute);

        assert(true);
    }


    @Test
    /**
     * Runs the parser on an Experiment folder containing a modified version
     * of the lyc20 example in which one plasmid (p20N100) has been duplicated with
     * a different, truncated sequence
     */
    public void TestRepeatedPlasmidName() throws Exception {
        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        try {
            String relative = "/test/org/ucb/c5/constructionfile/data/lyc20_repeat1";
            String absolute = getResourcePath(relative);
            parseFolder.run(absolute);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    @Test
    /**
     * Runs the parser on an Experiment folder containing a modified version
     * of the lyc20 example in which one oligo (TP5H) has been duplicated with
     * a different sequence
     */
    public void TestRepeatedOligoName() throws Exception {
        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        try {
            String relative = "/test/org/ucb/c5/constructionfile/data/lyc20_repeat2";
            String absolute = getResourcePath(relative);
            parseFolder.run(absolute);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    @Test
    /**
     * Runs the parser on an Experiment folder containing a modified version
     * of the pLYC73H construction file in which there is an oligo called TP5H in the CF
     * as well as another in the CF itself with a different sequence.  Should throw an Exception.
     */
    public void TestRepeatNamesCFvsExpt() throws Exception {
        ParseExperimentDirectory parseFolder = new ParseExperimentDirectory();
        parseFolder.initiate();
        try {
            String relative = "/test/org/ucb/c5/constructionfile/data/lyc20_repeat3";
            String absolute = getResourcePath(relative);
            parseFolder.run(absolute);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }

    /**
     * return the absolute path of a path relative to the project path
     * @param relativePath should start with /src/
     * @return
     */
    private static String getResourcePath(String relativePath) {
        String projectDir = System.getProperty("user.dir");
        if (relativePath.charAt(0) != '/') {
            projectDir = projectDir + '/';
        }
        return projectDir + relativePath;
    }
}
