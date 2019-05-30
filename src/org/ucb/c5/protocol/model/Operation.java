package org.ucb.c5.protocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public enum Operation {
    transfer,   //Transfer liquid from one tube to another
    dispense,   //Dispense a Reagent into a a tube
    mix,        //Mix the contents of a tube
    thermocycle,//Run a program on a sample on the thermocycler
    miniprep,   //Extract plasmid DNA from a culture
    cleanup,    //Perform a DNA cleanup reaction
    transform   //Transform an organism with a DNA sample
}
