package console;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import simulator.Simulator;
import world.Factory;
import world.World;
import exceptions.MissingElementException;

/** The console user interface for Assignment 5. */
public class Console {
    private Scanner scan;
    public boolean done;
    private Simulator sim;

    //TODO world representation...
//TODO fix the exception business
    public static void main(String[] args) {
        Console console = new Console();

        while (!console.done) {
            System.out.print("Enter a command or \"help\" for a list of commands.\n> ");
            try {
                console.handleCommand();
            }
            catch(MissingElementException e){
                System.out.println("Why does this happen?");
            }
            catch(FileNotFoundException e){
                System.out.println("File not found.");
                System.out.println(e.getMessage());
            }
            catch(SyntaxError e){
                System.out.println("Syntax error in file.");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Processes a single console command provided by the user. 
     */
    void handleCommand() throws MissingElementException, FileNotFoundException, SyntaxError{
        String command = scan.next();
        switch (command) {
        case "new": {
            newWorld();
            break;
        }
        case "load": {
            String filename = scan.next();
            loadWorld(filename);
            break;
        }
        case "critters": {
            String filename = scan.next();
            int n = scan.nextInt();
            loadCritters(filename, n);
            break;
        }
        case "step": {
            int n = scan.nextInt();
            advanceTime(n);
            break;
        }
        case "info": {
            worldInfo();
            break;
        }
        case "hex": {
            int c = scan.nextInt();
            int r = scan.nextInt();
            hexInfo(c, r);
            break;
        }
        case "help": {
            printHelp();
            break;
        }
        case "exit": {
            done = true;
            break;
        }
        default:
            System.out.println(command + " is not a valid command.");
        }
    }

    /**
     * Constructs a new Console capable of reading the standard input.
     */
    public Console() {
        scan = new Scanner(System.in);
        done = false;
        sim = new Simulator(); //Piazza question mentioned we should start a new simulator on every loadup
    }

    /**
     * Starts new random world simulation.
     */
    private void newWorld() {
    	CritterInterpreter c = new CritterInterpreter();
        World w = Factory.getRandomWorld();

    	c.setWorld(w);
        Simulator s = new Simulator(w,c);
        this.sim = s;
    }

    /**
     * Starts new simulation with world specified in filename.
     * @param filename
     */
    private void loadWorld(String filename) {
    	CritterInterpreter c = new CritterInterpreter();
	    Simulator s = new Simulator(c);
	    s.parseWorld(filename);
	    this.sim = s;
    }

    /**
     * Loads critter definition from filename and randomly places 
     * n critters with that definition into the world.
     * @param filename
     * @param n
     * @throws MissingElementException 
     */
    private void loadCritters(String filename, int n)  throws MissingElementException, FileNotFoundException, SyntaxError{
        sim.putCritterRandomly(filename);
    }

    /**
     * Advances the world by n time steps.
     * @param n
     * @throws MissingElementException 
     */
    private void advanceTime(int n) {
        if(sim != null)
        	sim.advance(n);
        else
        	System.out.println("You haven't loaded a simulator");
    }

    /**
     * Prints current time step, number of critters, and world
     * map of the simulation.
     */
    private void worldInfo() {
        System.out.println("Timesteps: " +sim.getTimesteps());
        System.out.println("Number of Critters: " + sim.getNumCritters());
        sim.hexWorld();
    }

    /**
     * Prints description of the contents of hex (c,r).
     * @param c column of hex
     * @param r row of hex
     */
    private void hexInfo(int c, int r) {
        sim.hexLocation(c, r);
    }

    /**
     * Prints a list of possible commands to the standard output.
     */
    private void printHelp() {
        System.out.println("new: start a new simulation with a random world");
        System.out.println("load <world_file>: start a new simulation with "
                + "the world loaded from world_file");
        System.out.println("critters <critter_file> <n>: add n critters "
                + "defined by critter_file randomly into the world");
        System.out.println("step <n>: advance the world by n timesteps");
        System.out.println("info: print current timestep, number of critters "
                + "living, and map of world");
        System.out.println("hex <c> <r>: print contents of hex "
                + "at column c, row r");
        System.out.println("exit: exit the program");
    }
}
