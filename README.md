# ConstructionFileSimulator

This is code from 20n project C4, minimally altered to enable extraction from the larger codebase.

It is compilable code, and many of the classes are in a complete form, but the entire program was not completed.
It also does not follow the rebuild-the-world style of Functions and Models
It also has no tests

Polynucleotide: The most central class in here.  It should be an immutable Model, but currently is dynamic.  It describes 
the double strandedness and sticky ends of the DNA molecule.  The main method provides an example of creating an instance of a DNA with
sticky ends, and shows how to println the object for visualization.

