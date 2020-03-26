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
    private static Logger logger;
    private static FileHandler handler;
    
    static {
        try {
            File logfil = new File("C5log.txt");
            FileUtils.writeFile("", logfil.getAbsolutePath());
            handler = new FileHandler(logfil.getAbsolutePath(), true);
            logger = Logger.getLogger("C5");
            logger.setLevel(Level.ALL);
            logger.addHandler(handler);
            handler.setFormatter(new Formatter() {
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
            System.exit(0);
        }
    }
    
    public static void info(String msg) {
        logger.info(msg);
    }
    
    public static void warning(String msg) {
        logger.warning(msg);
    }
    
    public static void severe(String msg) {
        logger.severe(msg);
    }
       
    public static void main(String[] args) {
        Log.info("test of logging");
        Log.info("open up C5log.txt");
    }
}
