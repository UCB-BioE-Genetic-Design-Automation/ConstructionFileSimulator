/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author J. Christopher Anderson
 */
public class Log {

    private static Logger msgLogger;
    private static FileHandler msgHandler;
    private static Logger seqLogger;
    private static FileHandler seqHandler;

    static {
        try {
            File logfil = new File("C5log.txt");
            FileUtils.writeFile("", logfil.getAbsolutePath());
            msgHandler = new FileHandler(logfil.getAbsolutePath(), true);
            msgLogger = Logger.getLogger("C5");
            msgLogger.setLevel(Level.ALL);
            msgLogger.addHandler(msgHandler);
            msgHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(record.getMessage());
                    sb.append("\n");
                    return sb.toString();
                }
            });
        } catch (Exception ex) {
            System.err.println("Unable to initiate logger");
//            System.exit(0);
        }

        try {
            File logfil = new File("C5seqs.txt");
            FileUtils.writeFile("", logfil.getAbsolutePath());
            seqHandler = new FileHandler(logfil.getAbsolutePath(), true);
            seqLogger = Logger.getLogger("Log");
            seqLogger.setLevel(Level.ALL);
            seqLogger.addHandler(seqHandler);
            seqHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(record.getMessage());
                    sb.append("\n");
                    return sb.toString();
                }
            });
        } catch (Exception ex) {
            System.err.println("Unable to initiate logger");
//            System.exit(0);
        }
    }

    public static void info(String msg) {
        msgLogger.info(msg);
    }

    public static void warning(String msg) {
        msgLogger.warning(msg);
    }

    public static void severe(String msg) {
        msgLogger.severe(msg);
    }

    public static void seq(String name, String seq, String note) {
        seqLogger.info(name + "\t" + seq + "\t" + note + "\t");
    }

    public static void main(String[] args) {
        Log.info("test of logging");
        Log.info("open up C5log.txt");
    }
}
