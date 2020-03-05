/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import org.junit.Test;
import java.util.*;

/**
 *
 * @author mimirrr
 */
public class GeneBankTranslator {
    public static String getLetter(String a) { 
          Scanner in = new Scanner(System.in);
          String seq = in.nextLine();
          StringBuffer sb = new StringBuffer();     
          for(int i = 0;i<seq.length();i++){    
              char c = seq.charAt(i);            
              if((c<='z'&&c>='a')||(c<='Z'&&c>='A')){  
                  sb.append(c);     
              }      
          }    
          System.out.println(sb.toString());   
          return sb.toString();

      }

}
