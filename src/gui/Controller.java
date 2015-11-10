package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import simulator.Simulator;
import world.Entity;

/**
 * Created by Max on 11/8/2015.
 */
public class Controller {
    Simulator sim;
    Entity focused;

    public Controller(){
        sim = new Simulator();
        sim.parseWorld("world.txt");
    }

    public void hexClick(MouseEvent event){
        System.out.println("hi");
        WorldHex clicked = (WorldHex) event.getSource();
        System.out.println(clicked.getCoordinate());
        //TODO: determine return type
    }
}
