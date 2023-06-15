package org.ucb.c5.sequtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ucb.c5.constructionfile.model.RestrictionEnzyme;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class RestrictionEnzymeFactory {

    public HashMap<String, RestrictionEnzyme> reHmap;
    public static List<String> reNameList = new ArrayList<>();
    public static List<String> testList = new ArrayList<>();
    public HashMap<String, Integer> nebHashmap;

    public void initiate() throws Exception {
        //Neb cleavage end cutting chart parser
        String nebData = FileUtils.readResourceFile("sequtils/data/NEBCleavageDistance.txt");
        String[] nebLines = nebData.split("\\r?\\n|\\r");
        nebHashmap = new HashMap<>();
        for (int i = 2; i < nebLines.length; i++) {
            String nebLineitems = nebLines[i];
            String[] nebTabs = nebLineitems.split("\t");
            String nebName = nebTabs[0];
            String indexOne = nebTabs[1];
            String indexTwo = nebTabs[2];
            String indexThree = nebTabs[3];
            String indexFour = nebTabs[4];
            String indexFive = nebTabs[5];
            if (indexOne.equalsIgnoreCase("+++")) {
                nebHashmap.put(nebName, 1);
            } else if (indexTwo.equalsIgnoreCase("+++")) {
                nebHashmap.put(nebName, 2);
            } else if (indexThree.equalsIgnoreCase("+++")) {
                nebHashmap.put(nebName, 3);
            } else if (indexFour.equalsIgnoreCase("+++")) {
                nebHashmap.put(nebName, 4);
            } else if (indexFive.equalsIgnoreCase("+++")) {
                nebHashmap.put(nebName, 5);
            }
        }
        // System.out.println(nebHashmap);

        //ReBASE parser     
        String rebaseData = FileUtils.readResourceFile("sequtils/data/RebaseDatabase.txt");
        String[] lines = rebaseData.split("\\r?\\n|\\r");
        reHmap = new HashMap<>();
        // List enumList = new ArrayList<>();
        for (int i = 23; i < lines.length; i++) {
            String reLines = lines[i];
            String[] reattr = reLines.split(";");
            String reName = reattr[0];
            String reSeq = reattr[1];
            String re5PrimeString = reattr[2];
            //TO DO: handle enzymes that cut more than once in each strand. Currently skipped. 
            if (re5PrimeString.contains(",")) {
                continue;
            }
            reNameList.add(reName);
            //add Gibson as enzyme (previously an enum)
            int re5Prime = Integer.parseInt(re5PrimeString);
            String re3PrimeString = reattr[3];
            int re3Prime = Integer.parseInt(re3PrimeString);
            int re5PrimeCorrected = reSeq.length() + re5Prime;
            int re3PrimeCorrected = reSeq.length() + re3Prime;
            //for positive cut sites outside of sequence, modify reSeq to have [ATCG] for N bps based on the max of  5' vs 3' cut sites. 
            if (re5Prime > 0) {
                int N = Math.max(re5Prime, re3Prime);
                //use for loop to create string of repeated [ATCG] based on number N 
                String ATCG = "";
                String atcg = "[ATCG]";
                // for (int j=0; <=input.length(); i++)
                for (int j = 0; j < N; j++) {
                    ATCG += atcg;
                }
                //concatenate the ATCG repeats to the end of the reSequence
                String reSeqNew = reSeq.concat(ATCG);
                //set this reSeqNew as reSeq as replacement
                reSeq = reSeqNew;
            }
           int cuttingDistanceMin = 5;
           if (nebHashmap.containsKey(reName)) {
              cuttingDistanceMin = nebHashmap.get(reName);
            }

            RestrictionEnzyme restrictionEnzyme = new RestrictionEnzyme(reSeq, re5PrimeCorrected, re3PrimeCorrected, cuttingDistanceMin);
            reHmap.put(reName, restrictionEnzyme); 
                 }
                }
    
    public RestrictionEnzyme run(String enzname) throws Exception {
        return this.reHmap.get(enzname);
    }

    public static void main(String[] args) throws Exception {
        RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
        factory.initiate();
    }
}

