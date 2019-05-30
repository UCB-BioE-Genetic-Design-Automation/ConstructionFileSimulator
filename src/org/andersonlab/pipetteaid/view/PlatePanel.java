package org.andersonlab.pipetteaid.view;

import org.andersonlab.pipetteaid.model.DeckConfig;
import org.andersonlab.pipetteaid.model.PCRPlateConfig;
import org.andersonlab.pipetteaid.model.PlateConfig;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author J. Christopher Anderson
 */
public class PlatePanel extends JPanel {
    private JPanel currentDisplay;
    
    private final PlateConfig plateConfig;
    
    public PlatePanel() {
        setLayout(null);
        currentDisplay = createEmptyPosition();
        removePlate();
        plateConfig = PCRPlateConfig.getInstance();
    }
    
    private JPanel createEmptyPosition() {
        JPanel out = new JPanel();
        out.setBackground(Color.DARK_GRAY);
        JLabel label = new JLabel("Empty");
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
    }
    
    private JPanel createAddPlatePanel(String platename) {
        JPanel out = new JPanel();
        out.setBackground(new Color(0,0,50));
        JLabel label = new JLabel(platename);
        label.setForeground(Color.WHITE);
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
    }
    
    private JPanel createBlackPanel() {
        JPanel out = new JPanel();
        out.setLayout(null);
        out.setBackground(Color.BLACK);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
    }
     
    private JPanel createWellPanel(Color color, int row, int col) {
        JPanel out = new JPanel();
        out.setBackground(color);
        
        double posX = plateConfig.getXoffset() + (col-0.5) * plateConfig.getWellWidth();
        double posy = plateConfig.getYoffset() + (row-0.5) * plateConfig.getWellHeight();
        
        int width = DeckConfig.calcPixels(plateConfig.getWellWidth());
        int height = DeckConfig.calcPixels(plateConfig.getWellHeight());
        
        out.setBounds(DeckConfig.calcPixels(posX), DeckConfig.calcPixels(posy), width, height);
        return out;
    }
    
    //VIEW-RELAYED COMMANDS//
    
    public void reset() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                setBorder(null); 
                add(currentDisplay);
                revalidate();
                repaint();
            }
        });
    }
    
    public void addPlate(String plateName) {
        currentDisplay = this.createAddPlatePanel(plateName);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(currentDisplay);
                revalidate();
                repaint();
            }
        });
    }
    
    public void colorWell(Color color, int wellRow, int wellCol) {
        JPanel blackPanel = createBlackPanel();
        blackPanel.setBorder(BorderFactory.createLineBorder(color));
        JPanel wellPanel = createWellPanel(color, wellRow, wellCol);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(blackPanel);
                
                blackPanel.add(wellPanel);
                revalidate();
                repaint();
            }
        });
    }
    
    //Relay requested from View in response to an "removePlate" step
    private void removePlate() {
        currentDisplay = this.createEmptyPosition();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(currentDisplay);
                revalidate();
                repaint();
            }
        });
    }


}
