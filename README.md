# ConstructionFileSimulator

This is code from 20n project C4, minimally altered to enable extraction from the larger codebase.  The direct copy-over is in the first committ.  The original code was not complete and had many errors.  It was also not in rebuild-the-world style of Functions and Models
It also has no tests.

2019_03_23
The code has been reworked, and is now functional on all examples considered, which is complete (until we find out it's not).

*******
Models:
*******

'''Polynucleotide''':  This is an immutable Model encoding the structure of a nucleic acid molecule. It is needed to simulate the enzymatic reactions occuring during digestion and ligation. The complex it describes can be DNA or RNA (not used here), single stranded or double stranded, have sticky ends on either side, or can be circular. It contains a toString() method that gives a visual representation of the molecule showing the duplex with its ends:

A linear DNA with BamHI sticky ends on both sides:

<pre>
5'-GATCCtttG    -3'
3'-    GaaaCCTAG-5'
</pre>

There are convenience Constructors for instantiating blunt-ended, linear fragments, and circular fragments.  The above digest would be described as follows:

<pre>
Polynucleotide poly = new Polynucleotide("CtttG", "GATC", "GATC");
</pre>

The first argument, the "sequence" corresponds to the region of duplexed DNA. The second two strings are the 5' and 3' sticky ends.  If "" it is a blunt end.  All sticky ends are represented in the 5' to 3' direction referring to the top strand.  Thus both arguments here are "GATC".  A 3' extension is represented by the symbol "-" such as:

<pre>
Polynucleotide poly = new Polynucleotide("gaaacccGAGGAGaaaaaaaa", "-TGCA", "-CC");

5'-    gaaacccGAGGAGaaaaaaaaCC-3'
3'-ACGTctttgggCTCCTCtttttttt  -5'
</pre>

A circular DNA is indicated by a boolean, and the sticky end fields are ignored in algorithms:  The toString() method illustrates
circularity with "..." symbols

<pre>
Polynucleotide poly = new Polynucleotide("aaaaaaaa", true);

5'...aaaaaaaa...3'
3'...tttttttt...5'
</pre>

*******
Functions:
*******

'''PolyRevComp''' Reverse complement Function for a Polynucleotide
