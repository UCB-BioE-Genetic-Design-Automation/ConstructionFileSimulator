/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.simulators;

import java.util.List;
import melting.Environment;
import melting.Helper;
import static melting.Main.writeMeltingResults;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.MeltingComputationMethod;
import meltinggui.MeltingGui;

/**
 *
 * @author yishe
 */
public class Test {
    
    public static void main(String args[])
  {
    // Get the Nimbus look and feel, if supported (Java SE 6 or later).  
//    try {
//      for (javax.swing.UIManager.LookAndFeelInfo info :
//        UIManager.getInstalledLookAndFeels()) {
//        if ("Nimbus".equals(info.getName())) {
//          UIManager.setLookAndFeel(info.getClassName());
//          break;
//        }
//      }
//    }
//    catch (ClassNotFoundException exception) {
//      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
//                                                         null,
//                                                         exception);
//    } 
//    catch (InstantiationException exception) {
//      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
//                                                         null,
//                                                         exception);
//    } 
//    catch (IllegalAccessException exception) {
//      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
//                                                         null,
//                                                         exception);
//    } 
//    catch (UnsupportedLookAndFeelException exception) {
//      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
//                                                         null,
//                                                         exception);
//    } 

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run()
      {
        new MeltingGui();
      }
    });
  }
    
}
