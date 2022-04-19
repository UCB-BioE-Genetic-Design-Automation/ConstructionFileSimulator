package org.ucb.c5.constructionfile.model;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ucb.c5.genbank.model.RevComp;
import org.ucb.c5.sequtils.PolyRevComp;
import org.ucb.c5.utils.SequenceUtils;

/**
 * A Polynucleotide models the chemical entity described by that term. It is
 * adapted from Clotho3, but it is a new write.
 *
 * It is in distinction fromm NucSeq, which didn't correspond to any classical
 * abstraction. This is not an informational or pragmatic abstraction like a
 * part, or a plasmid. It seeks to describe a DNA or RNA molecule as a chemical
 * entity in all its glory, including modified bases, sticky ends, circularity,
 * strandedness and the like.
 *
 * @author J. Christopher Anderson
 */
public class Polynucleotide {

    private final String sequence;

    /*
     The ext5 variable corresponds to the lefthand side
     It does not imply that the extension is a 5' overhang
     but simply that it is the 5' side of current coding strand
    
     To indicate a 5' overhang, you use "GATC" like with BamHI
     To indicate a 3' overhang on the 5' end of the coding strand,
     you would sqy "-CTAG", as for PstI
     */
    private final String ext5;
    private final String ext3;
    private final boolean isDoubleStranded;
    private final boolean isRNA;
    private final boolean isCircular;

    private final Modifications mod_ext3;
    private final Modifications mod_ext5;

    /**
     * Full Constructor
     *
     * @param sequence
     * @param ext5
     * @param ext3
     * @param isDoubleStranded
     * @param isRNA
     * @param isCircular
     * @param mod_ext3
     * @param mod_ext5
     */

    public Polynucleotide(String sequence, String ext5, String ext3, boolean isDoubleStranded, boolean isRNA, boolean isCircular, Modifications mod_ext5, Modifications mod_ext3) {
        this.sequence = sequence;
        this.ext5 = ext5;
        this.ext3 = ext3;
        this.isDoubleStranded = isDoubleStranded;
        this.isRNA = isRNA;
        this.isCircular = isCircular;
        this.mod_ext3 = mod_ext3;
        this.mod_ext5 = mod_ext5;
    }

    /**
     * Convenience constructor for a circular DNA
     *
     * @param dnaseq
     * @param iscircular irrelevant what is supplied, will be circular
     */
    //Calling circular 
    public Polynucleotide(String dnaseq, boolean iscircular) {
        this(dnaseq, "", "", true, false, true, Modifications.circular, Modifications.circular); //!!
    }

    /**
     * Convenience constructor defaults to blunt PCR product, double stranded
     * DNA, which can then be edited -
     *
     * @param dnaseq
     */
    public Polynucleotide(String dnaseq) {
        this(dnaseq, "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
    }

    /**
     * Convenience constructor for a digested DNA fragment with sticky ends
     *
     * @param sequence
     * @param ext5
     * @param ext3
     */
    public Polynucleotide(String sequence, String ext5, String ext3) {
        this(sequence, ext5, ext3, true, false, false, Modifications.phos5, Modifications.phos5); 
    }
    
    
    public String getSequence() {
        return sequence;
    }

    public String getExt5() {
        return ext5;
    }

    public String getExt3() {
        return ext3;
    }

    public Modifications getMod5() {// !!!
        return mod_ext5;
    }

    public Modifications getMod3() { //!!!
        return mod_ext3;
    }

    public boolean isIsDoubleStranded() {
        return isDoubleStranded;
    }

    public boolean isIsRNA() {
        return isRNA;
    }

    //remove isCircular??
    public boolean isCircular() {
        return mod_ext5 == Modifications.circular;
    }

    public String getForwardStrand() {
        StringBuilder out = new StringBuilder();
        if (!ext5.startsWith("-")) {
            out.append(ext5);
        }
        out.append(this.sequence);
        if (!ext3.startsWith("-")) {
            out.append(ext3);
        }
        return out.toString();
    }

    public String ModtoString(Modifications mod) {

        String modS = mod.name();

        if (modS == "hydroxyl") {
            modS = "";

        } else if (modS == "phos5") {
            modS = "p";

        } else if (!(modS == "hydroxyl")) {

            switch (modS) {
                case "AmMC6":
                    modS = "/5AmMC6/";
                    break;
                case "AmMC12":
                    modS = "/5AmMC12/";
                    break;

                case "AmMC6T":
                    modS = "/5AmMC6T/";
                    break;
                case "UniAmM":
                    modS = "/5UniAmM/";
                    break;
                case "Biosg":
                    modS = "/5Biosg/";
                    break;
                case "BioK":
                    modS = "/5BioK/";
                    break;
                case "BiodT":
                    modS = "/5BiodT/";
                    break;
                case "BiotinTEG":
                    modS = "/5BiotinTEG/";
                    break;
                case "Dual_BT52":
                    modS = "/52-Bio/";
                    break;
                case "PCBT":
                    modS = "/5PCBio/";
                    break;
                case "deSBIOTEG":
                    modS = "/5deSBioTEG/";
                    break;
                case "DTPA":
                    modS = "/5DTPA/";
                    break;
                case "ThioMC6_D":
                    modS = "/5ThioMC6-D/";
                    break;
                case "Hexynyl":
                    modS = "/5Hexynyl/";
                    break;
                case "OctdU":
                    modS = "/55OctdU/";
                    break;
                case "Acryd":
                    modS = "/5Acryd/";
                    break;
                case "rApp":
                    modS = "/5rApp/";
                    break;
                case "AzideN":
                    modS = "/5AzideN/";
                    break;
                case "DigN":
                    modS = "/5DigN/";
                    break;
                case "ILink12":
                    modS = "/5ILink12/";
                    break;
                case "FAMN_56":
                    modS = "/56-FAMN/";
                    break;
                case "FAM_56":
                    modS = "/56-FAM/";
                    break;
                case "FluorT":
                    modS = "/5FluorT/";
                    break;
                case "CY3":
                    modS = "/5Cy3/";
                    break;
                case "Joen_56":
                    modS = "/56-JOEN/";
                    break;
                case "CY5":
                    modS = "/5Cy5/";
                    break;
                case "TAMN":
                    modS = "/56-TAMN/";
                    break;
                case "Maxn":
                    modS = "/5MAXN/";
                    break;
                case "TET":
                    modS = "/5TET/";
                    break;
                case "CY55":
                    modS = "/5Cy55/";
                    break;
                case "ROXN_56":
                    modS = "/56-ROXN/";
                    break;
                case "TYE563":
                    modS = "/5TYE563/";
                    break;
                case "YakYel":
                    modS = "/5YakYel/";
                    break;
                case "HEX1":
                    modS = "/5HEX/";
                    break;
                case "TEX615":
                    modS = "/5TEX615/";
                    break;
                case "TYE665":
                    modS = "/5TYE665/";
                    break;
                case "TYE705":
                    modS = "/5TYE705/";
                    break;
                case "SUN":
                    modS = "/5SUN/";
                    break;
                case "ATTO488N":
                    modS = "/5ATTO488N/";
                    break;
                case "ATTO532N":
                    modS = "/5ATTO532N/";
                    break;
                case "ATTO55ON":
                    modS = "/5ATTO550N/";
                    break;
                case "ATTO565N":
                    modS = "/5ATTO565N/";
                    break;
                case "ATTO101N":
                    modS = "/5RHO101N/";
                    break;
                case "ATTO590N":
                    modS = "/5ATTO590N/";
                    break;
                case "ATTO633N":
                    modS = "/5ATTO633N/";
                    break;
                case "ATTO647NN":
                    modS = "/5ATTO647NN/";
                    break;
                case "Alex488N":
                    modS = "/5Alex488N/";
                    break;
                case "Alex532N":
                    modS = "/5Alex532N/";
                    break;
                case "Alex456N":
                    modS = "/5Alex546N/";
                    break;
                case "Alex594N":
                    modS = "/5Alex594N/";
                    break;
                case "Alex647N":
                    modS = "/5Alex647N/";
                    break;
                case "Alex660N":
                    modS = "/5Alex660N/";
                    break;
                case "Alex750N":
                    modS = "/5Alex750N/";
                    break;
                case "IRD700":
                    modS = "/5IRD700/";
                    break;
                case "IRD800":
                    modS = "/5IRD800/";
                    break;
                case "IRD800CWN":
                    modS = "/5IRD800CWN/";
                    break;
                case "RhoG_XN":
                    modS = "/5RhoG-XN/";
                    break;
                case "RhoR_XN":
                    modS = "/5RhoR-XN/";
                    break;
                case "TAMK":
                    modS = "/55-TAMK/";
                    break;
                case "FAMK_6":
                    modS = "/56-FAMK/";
                    break;
                case "TexRd_XN":
                    modS = "/5TexRd-XN/";
                    break;
                case "LtC640N":
                    modS = "/5LtC640N/";
                    break;
                case "Dy750N":
                    modS = "/5Dy750N/";
                    break;
                case "IABKFQ":
                    modS = "/5IABkFQ/";
                    break;
                case "IAbRQ":
                    modS = "/5IAbRQ/";
                    break;
                case "SpC3":
                    modS = "/5SpC3/";
                    break;
                case "dSp":
                    modS = "/5dSp/";
                    break;
                case "SpPC":
                    modS = "/5SpPC/";
                    break;
                case "Sp9":
                    modS = "/5Sp9/";
                    break;
                case "Sp18":
                    modS = "/5Sp18/";
                    break;
                case "AmPr":
                    modS = "/52AmPr/";
                    break;
                case "Br_dU":
                    modS = "/55Br-dU/";
                    break;
                case "deoxyU":
                    modS = "5deoxyU";
                    break;
                case "Amda":
                    modS = "/5AmdA/";
                    break;
                case "deoxyI":
                    modS = "/5deoxyI/";
                    break;
                case "HydMe_dc":
                    modS = "/55HydMe-dC/";
                    break;
                case "isodG":
                    modS = "/5isodG/";
                    break;
                case "Me_isodC":
                    modS = "/5Me-isodC/";
                    break;
                case "InvddT":
                    modS = "/5InvddT/";
                    break;
                case "Me_dc":
                    modS = "/5Me-dC/";
                    break;
                case "Nitlnd":
                    modS = "/55NitInd/";
                    break;
                case "Super_dt":
                    modS = "/5Super-dT/";
                    break;
                case "Super_dg":
                    modS = "/5Super-dG/";
                    break;
            }
        }

        return modS;

    }

    public String toString() {
        String separator = "-";

        String out = "";

        String mod_5;
        String mod_3;

        if (this.mod_ext3 == Modifications.circular | this.mod_ext5 == Modifications.circular | this.isCircular == true) {
            separator = "...";
             mod_5 = "";
             mod_3 = "";
        }else{
            mod_5 = ModtoString(this.mod_ext5);
            mod_3 = ModtoString(this.mod_ext3);
        }

        
        out += "5'" + separator;
        if (this.ext5.startsWith("-")) {
            for (int i = 0; i < this.ext5.length() - 1; i++) {
                out += " ";
            }
            out += mod_5;

        } else {
            out += mod_5 + this.ext5;
        }

        out += this.sequence;

        if (!this.ext3.startsWith("-")) {
            for (int i = 0; i < this.ext3.length(); i++) {
                out += " ";
            }
        } else {
            out += this.ext3.substring(1);
        }

        if (this.isDoubleStranded == false) {
            out += "-3'";
            return out;
        }

        out += separator + "3'\n";

        //Do the other strand
        out += "3'" + separator;

        if (!(this.mod_ext5 == Modifications.hydroxyl | this.mod_ext5 == Modifications.circular)) {
            for (int i = 0; i < mod_5.length(); i++) {
                out += " ";
            }
        }

        if (!this.ext5.startsWith("-")) {
            for (int i = 0; i < this.ext5.length(); i++) {
                out += " ";
            }
        } else {
            out += SequenceUtils.complement(this.ext5);
        }

        out += SequenceUtils.complement(this.sequence);

        if (this.ext3.startsWith("-")) {
            out += mod_3;
            for (int i = 0; i < this.ext3.length() - 1; i++) {
                out += " ";
            }
        } else {
            out += SequenceUtils.complement(this.ext3) + mod_3;
        }

        out += separator + "5'\n";

        return out;
    }

    

    public static void main(String[] args) {
        System.out.println("Demo a blunt DNA, like a PCR product");
        Polynucleotide poly = new Polynucleotide("CTAGTttgacggctagcG", "", "", true, false, false, Modifications.hydroxyl, Modifications.hydroxyl);
        System.out.println(poly.toString());

        //System.out.println("Example Oligo"); // Oligos can come with or without phosphates
        //Polynucleotide poly = new Polynucleotide("CTAGTttgacggctagcG", "","", false, false, false,,Modifications.hydroxyl,Modifications.hydroxyl);
        //System.out.println(poly.toString());
        {
            System.out.println("Demo a BamHI/EcoRI digested DNA with 5' sticky ends");
            String ext5 = "GATC";
            String ext3 = "AATT";
            Polynucleotide poly2 = new Polynucleotide("caaacccg", ext5, ext3, true, false, false, Modifications.phos5, Modifications.phos5);
            System.out.println(poly2.toString());
        }

        {
            System.out.println("Demo a PstI/BseRI digested DNA with 3' overhangs");
            String ext5 = "-TGCA";
            String ext3 = "-CC";
            Polynucleotide poly3 = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", ext5, ext3, true, false, false, Modifications.phos5, Modifications.phos5);
            System.out.println(poly3.toString());
        }

        {
            System.out.println("A Circular DNA");
            Polynucleotide poly3 = new Polynucleotide("aaaaaaaa", true);
            System.out.println(poly3.toString());

            //System.out.println("Modifications");
            //Polynucleotide poly4 = new Polynucleotide("aaaaaaa",true, )
            //System.out.println(poly3.toString());
        }
    }
}
