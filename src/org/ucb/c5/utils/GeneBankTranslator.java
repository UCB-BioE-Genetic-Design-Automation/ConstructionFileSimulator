/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.utils;

import java.util.Scanner;

/**
 *
 * @author mimirrr
 */
public class GeneBankTranslator {
    
  public static void main(String[] args) { 
          String dirtyseq=" 1 tcgggcactt tacggccggg aacaaaattc ttaggaattc tcttaagcaa tgcagatgtt\n" +
"       61 gcaataactt tacaataatg ttactctaaa gatgagaaaa taaaatcttt aaagttgagt\n" +
"      121 actgggaaaa aaaaaaaaaa aaaaaaaaaa aaacttttgg gggccccccg gcccccatcc\n" +
"      181 ttttttaatt cgaacaattt tttgccgggg gcccaatttg ggcccccttg ggccaattcc";
          StringBuffer sb = new StringBuffer();     
          for(int i = 0;i<dirtyseq.length();i++){    
              char seq = dirtyseq.charAt(i);            
              if((seq<='z'&&seq>='a')||(seq<='Z'&&seq>='A')){  
                  sb.append(seq);     
              }      
          }    
          System.out.println(sb.toString());   

      }

}
