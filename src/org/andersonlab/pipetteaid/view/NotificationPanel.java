package org.andersonlab.pipetteaid.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.ucb.c5.semiprotocol.model.*;

/**
 *
 * @author J. Christopher Anderson
 */
public class NotificationPanel extends JPanel {
       
    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 3, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);
    
    
    public NotificationPanel() {
        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
        
        //Create a welcome message
        add(Box.createVerticalGlue());
        
        JLabel lbl1 = new JLabel("Click");
        lbl1.setFont(secondary_font);
        add(lbl1);
        
        JLabel lbl2 = new JLabel("Next");
        lbl2.setFont(emphasis_font);
        add(lbl2);
        
        add(Box.createVerticalGlue());

        JLabel lbl3 = new JLabel("to begin protocol");
        lbl3.setFont(body_font);
        add(lbl3);
        
        add(Box.createVerticalGlue());
    }
    
    public void showComplete() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                add(Box.createVerticalGlue());

                JLabel lbl1 = new JLabel("done.");
                lbl1.setFont(emphasis_font);
                add(lbl1);
                
                revalidate();
                repaint();
            }
        });
    }
    
    public void notify(Task step) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                
                switch (step.getOperation()) {
                    case addContainer:
                        AddContainer addcon = (AddContainer) step;
                        add(Box.createVerticalGlue());
                        
                        JLabel lop = new JLabel("Add plate:");
                        lop.setFont(body_font);
                        add(lop);
                        
                        JLabel lname = new JLabel(addcon.getName());
                        lname.setFont(secondary_font);
                        add(lname);
                        
                        JLabel ltopos = new JLabel("to position:");
                        ltopos.setFont(body_font);
                        add(Box.createVerticalGlue());
                        add(ltopos);
                        
                        JLabel lpos = new JLabel(calcWellLabel(addcon.getLocation()));
                        lpos.setFont(emphasis_font);
                        add(lpos);
                        break;
                    case removeContainer:
                        break;
                    case transfer:
                        Transfer tfer = (Transfer) step;
                        JLabel ttop = new JLabel("transfer:");
                        ttop.setFont(body_font);
                        add(Box.createVerticalGlue());
                        add(ttop);
                        
                        JLabel tlname = new JLabel("" + tfer.getVolume() + " uL");
                        tlname.setFont(emphasis_font);
                        add(tlname);
                        
                        JLabel tltopos = new JLabel("from: " + calcWellLabel(tfer.getSource()));
                        tltopos.setFont(secondary_font);
                        add(Box.createVerticalGlue());
                        add(tltopos);
                        
                        JLabel tltopos2 = new JLabel("to: " + calcWellLabel(tfer.getDest()));
                        tltopos2.setFont(secondary_font);
                        add(Box.createVerticalGlue());
                        add(tltopos2);
                        
                        add(Box.createVerticalGlue());
                        break;
                    case multichannel:
                        break;
                    case dispense:
                        break;
                }

                revalidate();
                repaint();
            }
        });
    }
    
    private static String calcWellLabel(String platenameslashA1) {
        String well = platenameslashA1;
        if(well.contains("/")) {
            String[] splitted = well.split("/");
            well = splitted[1];
        }
        return well;
    }
}
