package org.andersonlab.pipetteaid.view;

import org.andersonlab.pipetteaid.model.DeckConfig;
import org.andersonlab.pipetteaid.model.PCRPlateConfig;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.ucb.c5.semiprotocol.model.*;

/**
 *
 * @author J. Christopher Anderson
 */
public class View extends JFrame {
    
    private final Semiprotocol protocol;
    
    private PlatePanel[][] platePanels;
    private JButton nextBtn;
    private NotificationPanel notifPanel;
    
    //Caching of dimension values
    public static final DeckConfig deckConfig;
    public static final int plateWidth;
    public static final int plateHeight;

    static {      
        deckConfig = DeckConfig.getInstance();
        PCRPlateConfig plateConfig = PCRPlateConfig.getInstance();
        plateWidth = DeckConfig.calcPixels(plateConfig.getWidth());
        plateHeight = DeckConfig.calcPixels(plateConfig.getHeight());
    }
    
    public View(Semiprotocol protocol) {
        this.protocol = protocol;
    }

    public void initComponents() {        
        //Format the frame
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.black);
        
        //Add the "next" button
        nextBtn = new JButton("Next");
        nextBtn.setBounds(5, 5, 100, 50);
        getContentPane().add(nextBtn);
        
        //Add the notification panel
        notifPanel = new NotificationPanel();
        notifPanel.setBounds(5, 100, 200, 300);
        getContentPane().add(notifPanel);
        
        //Add a panel for each deck position
        platePanels = new PlatePanel[deckConfig.num_rows][deckConfig.num_cols];

        for(int row=0; row<deckConfig.num_rows; row++) {
            for(int col=0; col<deckConfig.num_cols; col++) {
                //Calculate the dimensions and positioning of the panel

                int xpos = DeckConfig.calcPixels(deckConfig.plate_offset_x + col*deckConfig.plate_spacing_x);
                int ypos = DeckConfig.calcPixels(deckConfig.plate_offset_y + row*deckConfig.plate_spacing_y);
                
                //Create and add the panel
                platePanels[row][col] = new PlatePanel();
                platePanels[row][col].setBounds(xpos, ypos, plateWidth, plateHeight);
                getContentPane().add(platePanels[row][col]);
            }
        }
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
        setVisible(true);
    }
    
    public JButton getNextBtn() {
        return nextBtn;
    }
    
    //CONTROLLER COMMANDS//
    
    public void resetAll() {
        for(int row=0; row<platePanels.length; row++) {
            for(int col=0; col<platePanels[row].length; col++) {
                platePanels[row][col].reset();
            }
            notifPanel.removeAll();
            revalidate();
            repaint();
        }
    }
    
    
    public void endProtocol() {
        resetAll();
        notifPanel.showComplete();
    }
    
    public void notify(Task step) {
        notifPanel.notify(step);
    }
    
    public void addPlate(String plateName, int row, int col) throws Exception {
        platePanels[row][col].addPlate(plateName);
    }
    
    public void colorWell(Color color, int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
        platePanels[plateRow][plateCol].colorWell(color, wellRow, wellCol);
    }
}
