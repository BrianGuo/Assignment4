package gui;

import simulator.Simulator;

/**
 * Created by Max on 11/8/2015.
 */
public class Controller {
    Simulator sim;

    public Controller(){
        sim = new Simulator();
        sim.parseWorld("world.txt");

    }
}
