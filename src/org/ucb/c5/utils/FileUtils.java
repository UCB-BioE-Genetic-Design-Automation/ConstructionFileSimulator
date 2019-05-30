/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import org.ucb.c5.C5;


/**
 * @author sixpi
 */
public class FileUtils {
    /**
     * dump an InputStream, returning a newly created byte[] array
     * @author Kelvin Li
     */
    public static byte[] dumpInputStream(InputStream instream) 
                                        throws IOException {
        int lastindex = 0;
        int increment;
        byte[] A = new byte[2048];
        byte[] B;
        do {
            B = new byte[2*A.length];
            System.arraycopy(A, 0, B, 0, A.length);
            A = B;
            try {
                increment = instream.read(A, lastindex, A.length - lastindex);
            } catch (IOException e) {
                throw e;
            }
            if (increment == -1)
                /* EOF, no characters read */
                break;
            lastindex += increment;
        } while (lastindex >= A.length);

        /* copy A into B of precisely correct length */
        B = new byte[lastindex];
        System.arraycopy(A, 0, B, 0, B.length);
        return B;
    }

    /**
     * @param fileName
     * @return
     */
    public static boolean isXMLFile(String fileName) {
        return isFileType(fileName, "xml");
    }

    /**
     * @param fileName
     * @param extension without the .
     * @return
     */
    public static boolean isFileType(String fileName, String extension) {
        return fileName.endsWith( "." + extension );
    }

    public static void writeFile(String datafile, String filePath) {
        try {
            Writer output = null;
            File file = new File(filePath);
            output = new FileWriter(file);
            output.write(datafile);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

   /**
    * Convenience method to determine the filepath of the persisted data.
    * @param fileName
    * @return 
    */
    public static String getFilePath(String fileName, String DBName) {
        File afile = new File(DBName);
        createDir(afile);
        return afile.getAbsolutePath() + File.separator + fileName;
    }

    /**
     * Helper method, creates directory with parent directories recursiverly.
     */
    private static void createDir(File f) {
        int limit = 10;
        while (!f.exists()) {
            if (!f.mkdir()) {
                createDir(f.getParentFile());
            }
            limit --;
            if(limit < 1) {
                break;
            }
        }
        if (limit == 0) {
        }
    }

    public static String readFile(String path) throws Exception {
        File f = new File(path);
        if (!f.isFile()) {
            System.err.println("Path is not a file: " + path);
            throw new Exception();
        }

        FileInputStream s;
        try {
            s = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + path);
            throw e;
        }

        byte[] content;
        try {
            content = dumpInputStream(s);
        } catch (IOException e) {
            System.err.println("Error reading: " + path);
            throw e;
        }

        return new String(content);    }

    public static String readResourceFile(String relPath) throws Exception {
        URL url = new C5().getClass().getResource(relPath);
        
        BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine).append("\n");
        in.close();
        
        return sb.toString();
    }
    
    public static void writeResourceFile(String datafile, String relPath) throws Exception {
        OutputStream os = new FileOutputStream(relPath);
        final PrintStream printStream = new PrintStream(os);
        printStream.println(datafile);
        printStream.close();
    }
}