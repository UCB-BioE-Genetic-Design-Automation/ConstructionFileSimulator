package org.andersonlab.pipetteaid.controller;

import org.andersonlab.pipetteaid.model.Deck;
import org.andersonlab.pipetteaid.model.Well;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.*;
import org.andersonlab.pipetteaid.view.View;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.utils.FileUtils;

/**
 * Controller to initiate and control a PippeteAid GUI.
 * Similar to a Simulator in the sense that it simulates
 * what hap[pens when you perform a supplied protocol.  It
 * is different in the sense that it controls a GUI and
 * increments through the protocol in response to mouse 
 * clicks.
 * 
 * @author J. Christopher Anderson
 */
public class Controller {
    private final Semiprotocol protocol;
    private final Deck deck;
    private final View view;
    
    private int currpos = 0;
 
    public Controller(Semiprotocol protocol, View view) {
        this.protocol = protocol;
        this.view = view;
        this.deck = new Deck();
        
        view.initComponents();
        view.getNextBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    goNext();
                    System.out.println("Mouse go next");
                } catch (Exception ex) {
                    System.err.println("Error going forward");
                    ex.printStackTrace();
                }
            }
        });
    }
    
    private void goNext() throws Exception {
        //Handle scenario where it's reached the end
        if(protocol.getSteps().size() - 1 < currpos) {
            view.endProtocol();
            return;
        }
        
        Task task = protocol.getSteps().get(currpos);
        
        //Execute the step
        switch(task.getOperation()) {
            case addContainer:
                AddContainer addcon = (AddContainer) task;
                Pair<Integer, Integer> position = Deck.calcDeckPosition(addcon.getLocation());
                deck.addPlate(addcon.getName(), position.getKey(), position.getValue());
                //Update the view
                view.resetAll();
                view.addPlate(addcon.getName(), position.getKey(), position.getValue());
                break;
            case removeContainer:
                System.out.println("Remove container");
                RemoveContainer rc = (RemoveContainer) task;
                deck.removePlate(rc.getContainer());
                break;
            case transfer:
                Transfer tfer = (Transfer) task;
                Well srcwell = deck.getWell(tfer.getSource());
                srcwell.removeVolume(tfer.getVolume());
                Well dstwell = deck.getWell(tfer.getDest());
                dstwell.addVolume(tfer.getVolume());
                
                //Update the view
                view.resetAll();
                String srcLocation = tfer.getSource();
                System.out.println("srcLocation" + srcLocation);
                Pair<Integer,Integer> srcPlatePos = deck.getPlatePos(srcLocation);
                Pair<Integer,Integer> srcWell = Well.parseWellLabel(tfer.getSource());
                
                System.out.println("srcPlatePos" + srcPlatePos);
                System.out.println("srcWell" + srcWell);
                
                //Color color, int plateRow, int plateCol, int wellRow, int wellCol
                view.colorWell(Color.white, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());
                
                Pair<Integer,Integer> dstPlatePos = deck.getPlatePos(tfer.getDest());
                Pair<Integer,Integer> dstWell = Well.parseWellLabel(tfer.getDest());
                
                view.colorWell(Color.green, dstPlatePos.getKey(), dstPlatePos.getValue(), dstWell.getKey(), dstWell.getValue());
                break;
            case multichannel:
                Multichannel multi = (Multichannel) task;
                break;
            case dispense:
                Dispense disp = (Dispense) task;
                break;
        }
        
        view.notify(task);

        //Increment to the next position
        currpos++;
    }
    
    public static void main(String[] args) throws Exception {
        //Load the protocol
        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);
        
        //Create the View
        View view = new View(protocol);
        
        //Create the Controller and initiate the GUI
        Controller controller = new Controller(protocol, view);
    }
}
