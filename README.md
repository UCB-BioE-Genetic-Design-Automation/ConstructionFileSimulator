# ConstructionFileSimulator

The primary objective of this project is to verify the correctness of a ConstructionFile (CF) by simulating its operations. This is achieved through a verification function which is designed to detect design errors in the CF. If an error or inconsistency is found during simulation, an exception is thrown, alerting the user to the issue.

While this project doesn't include the design algorithms that generate CF models detailing individual molecular biology processes like PCR, digestion, ligation, and assembly reactions, it does simulate these models. The simulation acts as a critical verification tool, detecting and reporting design errors that might otherwise go unnoticed.

## Getting Started
### Prerequisites
Java Runtime Environment (JRE) 8 or later

### Download
The project files, including the required .jar file and libraries, can be downloaded from the [Releases section of this GitHub repository](https://github.com/UCB-BioE-Genetic-Design-Automation/ConstructionFileSimulator/releases/tag/v1.0) as dist.zip.

### Usage
Here are the various ways you can run this project:

Double Click Run: After extracting the zip file, you can double click on the ConstructionFileSimulator.jar file. This will launch SimulatorView, a GUI where you can paste in a 'construction file' text, then click the 'run' button to output the simulated product.

Drag and Drop: You can also drag an 'Experiment' folder containing Construction File text files, Genbank, and sequence files as TSV onto SimulatorView to simulate it.

Command Line Launch: From a terminal/command line, navigate to the directory where ConstructionFileSimulator.jar is located. You can then run the application using the command java -jar ConstructionFileSimulator.jar. This will also launch SimulatorView.

Command Line Simulation: If you wish to simulate an experiment folder directly, use the command java -jar ConstructionFileSimulator.jar /path/to/experiment/folder.

A demo of these methods in action can be found in this video. (Provide a link to your video here.)

As for where to make the jar/zip file available, you can utilize GitHub's release feature. This allows you to version your software, provide release notes, and upload binary files (like your zip file). Additionally, it provides a single, consistent location for users to download your project.

## Models:
#### Polynucleotide: An immutable Model encoding the structure of a nucleic acid molecule. 
It is needed to simulate the enzymatic reactions occuring during digestion and ligation. The complex it describes can be DNA or RNA (not used here), single stranded or double stranded, have sticky ends on either side, or can be circular. It contains a toString() method that gives a visual representation of the molecule showing the duplex with its ends:

A linear DNA with BamHI sticky ends on both sides:

<pre>
5'-GATCCtttG    -3'
3'-    GaaaCCTAG-5'
</pre>

There are convenience Constructors for instantiating blunt-ended, linear fragments, and circular fragments.  The above fragment would be described as follows:

<pre>
Polynucleotide poly = new Polynucleotide("CtttG", "GATC", "GATC");
</pre>

The first argument (the "sequence") corresponds to the region of fully duplexed DNA. The second two strings are the 5' and 3' sticky ends.  If the value is "", it is a blunt end.  All sticky ends are represented in the 5' to 3' direction referring to the top strand.  Thus both arguments here are "GATC".  A 3' extension is represented by the symbol "-" such as:

<pre>
Polynucleotide poly = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", "-TGCA", "-CC");

5'-    gaaacccGAGGAGaaaaaaaaCC-3'
3'-ACGTctttgggCTCCTCtttttttt  -5'
</pre>

A circular DNA is indicated by a boolean, and the sticky end fields are ignored in algorithms. They are instantiated with a different convenience constructor requiring the sequence and a boolean. The toString() method illustrates circularity with "..." symbols:

<pre>
Polynucleotide poly = new Polynucleotide("aaaaaaaa", true);

5'...aaaaaaaa...3'
3'...tttttttt...5'
</pre>

#### RestrictionEnzyme: Describes the cutting behavior of a type II, type IIs, or type IIg restriction enzyme
The molecular function of type II restriction enzymes is described by an instance of RestrictionEnzyme.  The enzyme's recognition pattern is modeled as a regular expression String, and two integers define the cut sites on the coding and non-coding strands.  To add additional enzymes, the best way would be to describe it in RestrictionEnzymeFactory, discussed below.  For example:

<pre>
RestrictionEnzyme BamHI = new RestrictionEnzyme("GGATCC", 1, 5);
RestrictionEnzyme BsaI = new RestrictionEnzyme("GGTCTC[ATCG][ATCG][ATCG][ATCG][ATCG]", 7, 11);
</pre>

Functions:

All Functions are deterministic pure functions containing initiate and run methods. To invoke one, you instantiate and initiate the Function once, and then use this instance for many calls of run. For example:

<pre>
PolyRevComp func = new PolyRevComp();
func.initiate();
for(ini i=0; i<1000000; i++) {
    func.run(...);
}
</pre>

The Functions below are in the org.ucb.c5.construction package.  Initialize them as above, then run them with examples provided.  More examples are present in each class's main method. There are multiple additional simulators including Golden Gate Assembly and Gibson Assembly. The following are provided as examples of usage:

#### PolyRevComp: Reverse complement Function for a Polynucleotide
Inputs a Polynucleotide and returns a new Polynucleotide that is 'flipped around'.

<pre>
Demo a PstI/BseRI digested DNA with 3' overhangs

Polynucleotide poly = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", "-TGCA", "-CC");
Polynucleotide rc = func.run(poly);
            
5'-    gaaacccGAGGAGaaaaaaaaCC-3'
3'-ACGTctttgggCTCCTCtttttttt  -5'

Reverse complemented result:

5'-  ttttttttCTCCTCgggtttcTGCA-3'
3'-CCaaaaaaaaGAGGAGcccaaag    -5'
</pre>

#### RestrictionEnzymeFactory:  Generates models of several commonly used restriction enzymes
Provides RestrictionEnzyme instances for type II (BamHI, EcoRI, PstI, etc.), type IIs (BamHI, BsmBI), and type IIg (BseRI).  An example of its usage is in the DemoDigAndLig example discussed below.

#### DigestSimulator:  Simulates digestion to completion
Inputs a single Polynucleotide and a List of restriction enzymes, and returns a list of Polynucleotides representing the full digestion products by all the enzymes.  The enzymes are provided as RestrictionEnzyme instances encoding cutting details.  These restriction enzyme Models can be generated by RestrictionEnzymeFactory. Running DigestSimulator will linearize circular DNAs and cut linear DNAs into multiple fragments. The runnable demo below is from demo package class DemoDigAndLig illustrating digestion of a circular DNA and then re-ligation of the products.

<pre>
Demo digestion of a circular DNA with BamHI and BseRI

//Initialize the functions
RestrictionEnzymeFactory factory = new RestrictionEnzymeFactory();
factory.initiate();
DigestSimulator dig = new DigestSimulator();
dig.initiate();

//Construct the polynucleotide and restriction enzymes
Polynucleotide poly = new Polynucleotide("atatatatGGATCCacacacacacGAGGAGaaaaaaaaCCagagagagag", true);
List enz = new ArrayList<>();
enz.add(factory.run(Enzyme.BseRI));
enz.add(factory.run(Enzyme.BamHI));
    
//Run the DigestSimulator Function
List<Polynucleotide> digfrags = dig.run(poly, enz);

Starting plasmid:
5'...atatatatGGATCCacacacacacGAGGAGaaaaaaaaCCagagagagag...3'
3'...tatatataCCTAGGtgtgtgtgtgCTCCTCttttttttGGtctctctctc...5'

Digestion fragments:
5'-  agagagagagatatatatG    -3'
3'-GGtctctctctctatatataCCTAG-5'

5'-GATCCacacacacacGAGGAGaaaaaaaaCC-3'
3'-    GtgtgtgtgtgCTCCTCtttttttt  -5'
</pre>

#### LigateSimulator:  Simulates ligation to completion
Inputs a List of Polynucleotide representing the DNA fragments, and returns a single Polynucleotide.  It will throw an Exception if the sequences do not assemble into a single product.  It will also circularize a DNA able to do so.  Here is the second portion of DemoDigAndLig, where the digestion fragments generated with DigestSimulator (digfrags) are reassembled with LigateSimulator:

<pre>
LigateSimulator lig = new LigateSimulator();
lig.initiate();
Polynucleotide pdt = lig.run(digfrags);

Re-ligated product:
5'...agagagagagatatatatGGATCCacacacacacGAGGAGaaaaaaaaCC...3'
3'...tctctctctctatatataCCTAGGtgtgtgtgtgCTCCTCttttttttGG...5'
</pre>
