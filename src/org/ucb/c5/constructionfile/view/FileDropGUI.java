/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.c5.constructionfile.view;

/*
 * BorderLayoutDemo.java
 *
 */
import javax.swing.*;
import javax.swing.border.Border;
import org.ucb.c5.constructionfile.SimulateExperimentDirectory;
import org.ucb.c5.utils.FileUtils;
import org.ucb.c5.utils.Log;

public class FileDropGUI extends JFrame {

    Border redborder = javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 51, 51), new java.awt.Color(255, 153, 153), new java.awt.Color(255, 153, 153), new java.awt.Color(255, 204, 204));
    Border greenborder = javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 153, 51), new java.awt.Color(102, 255, 102), new java.awt.Color(0, 102, 0), new java.awt.Color(204, 255, 204));
    Border whiteborder = javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white);
    JLabel imgLabel;

    public FileDropGUI() {
        super("ConstructionFileSimulator");
        initComponents();
        imgLabel.setBorder(whiteborder);


        
        //Pack and show
        pack();
        setVisible(true);
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event dispatch thread.
     */
    private void initComponents() {
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content
        imgLabel = new JLabel();
        imgLabel.setBackground(new java.awt.Color(255, 255, 255));
        imgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/ucb/c5/constructionfile/view/filedropimg.png"))); // NOI18N
        imgLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mouseEnterHandler(evt);
            }
        });
        getContentPane().add(imgLabel);
        
        
        new FileDrop(System.out, this.getContentPane(), new FileDrop.Listener() {
            @Override
            public void filesDropped(java.io.File[] files) {
                getGlassPane().setVisible(false);
                for (int i = 0; i < files.length; i++) {
                    try {
                        String dirPath = files[i].getCanonicalPath();
                        if (!files[i].isDirectory()) {
                            if (dirPath.endsWith(".txt")) {
                                String data = "default_features\t" + dirPath + "\n";
                                FileUtils.writeFile(data, "data_paths.txt");
                                imgLabel.setBorder(greenborder);
                                return;
                            }

                            imgLabel.setBorder(redborder);
                            return;
                        }

                        //Simulate the experiment and write results
                        SimulateExperimentDirectory sed = new SimulateExperimentDirectory();
                        sed.initiate();
                        sed.run(dirPath);
                        Log.info("--> -->  Success!!");
                        imgLabel.setBorder(greenborder);
                    } catch (Exception ex) {
                        Log.severe(ex.getMessage());
                        imgLabel.setBorder(redborder);
                    }
                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener
    }

    private void mouseEnterHandler(java.awt.event.MouseEvent evt) {
        imgLabel.setBorder(whiteborder);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileDropGUI();
            }
        });
    }
}
