package console;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import interpret.CritterInterpreter;
import simulator.Simulator;
import world.World;
import exceptions.MissingElementException;

/** The console user interface for Assignment 5. */
public class Console {
    private Scanner scan;
    public boolean done;
    private Simulator sim;

    //TODO world representation...

    public static void main(String[] args) {
        Console console = new Console();

        while (!console.done) {
            System.out.print("Enter a command or \"help\" for a list of commands.\n> ");
            console.handleCommand();
        }
    }

    /**
     * Processes a single console command provided by the user. 
     */
    void handleCommand() {
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
            try{
            	loadCritters(filename, n);
            }
            catch(MissingElementException e){
            	System.out.println("You have not loaded a world");
            }
            break;
        }
        case "step": {
            int n = scan.nextInt();
            try{
            	advanceTime(n);
            }
            catch(MissingElementException e) {
            	System.out.println("You have not loaded a world");
            }
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
    	try{
	    	FileReader f = new FileReader(filename);
	        Simulator s = new Simulator(c);
	        s.parseWorld(f);
    	}
    	catch(FileNotFoundException e) {
    		System.out.println("File Not Found");
    	}
    }

    /**
     * Loads critter definition from filename and randomly places 
     * n critters with that definition into the world.
     * @param filename
     * @param n
     * @throws MissingElementException 
     */
    private void loadCritters(String filename, int n) throws MissingElementException {
        if (!(sim.hasWorld()))
        	sim.putCritterRandomly(filename);
        else
        	throw new MissingElementException("World");
    }

    /**
     * Advances the world by n time steps.
     * @param n
     * @throws MissingElementException 
     */
    private void advanceTime(int n) throws MissingElementException {
        if(sim != null)
        	sim.advance(n);
        else
        	throw new MissingElementException("Simulator");
    }

    /**
     * Prints current time step, number of critters, and world
     * map of the simulation.
     */
    private void worldInfo() {
        //TODO implement
    }

    /**
     * Prints description of the contents of hex (c,r).
     * @param c column of hex
     * @param r row of hex
     */
    private void hexInfo(int c, int r) {
        //TODO implement
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
