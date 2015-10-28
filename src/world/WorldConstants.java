package world;

import java.io.Reader;
import java.util.Scanner;

/**
 * An object that holds all of the constants for a given world.
 * This allows each world to have their own set of constants, and for critters to know these values.
 */
public class WorldConstants {
    public int BASE_DAMAGE, ENERGY_PER_SIZE, FOOD_PER_SIZE, MAX_SMELL_DISTANCE, ROCK_VALUE,
            DEFAULT_COLS, DEFAULT_ROWS, MAX_RULES_PER_TURN, SOLAR_FLUX, MOVE_COST, ATTACK_COST,
            GROW_COST, BUD_COST, MATE_COST, RULE_COST, ABILITY_COST, INITIAL_ENERGY, MIN_MEMORY;
    public double DAMAGE_INC;

    public WorldConstants(Reader r){
        Scanner sc = new Scanner(r);


        //at least there's only one double value...
        //didn't want to have to get constants out of a map.
        //java doesn't seem to support the level of reflection required to avoid this switch case block
        while(sc.hasNext()) {
            int value = 0;
            String[] next = sc.nextLine().split("\\s+");
            try{
                value = Integer.parseInt(next[1]);
            }
            catch(NumberFormatException e){
                //handled later
            }
            switch (next[0]) {
                case "BASE_DAMAGE":
                    BASE_DAMAGE = value;
                    break;
                case "ENERGY_PER_SIZE":
                    ENERGY_PER_SIZE = value;
                    break;
                case "FOOD_PER_SIZE":
                    FOOD_PER_SIZE = value;
                    break;
                case "MAX_SMELL_DISTANCE":
                    MAX_SMELL_DISTANCE = value;
                    break;
                case "ROCK_VALUE":
                    ROCK_VALUE = value;
                    break;
                case "COLUMNS":
                    DEFAULT_COLS = value;
                    break;
                case "ROWS":
                    DEFAULT_ROWS = value;
                    break;
                case "MAX_RULES_PER_TURN":
                    MAX_RULES_PER_TURN = value;
                    break;
                case "SOLAR_FLUX":
                    SOLAR_FLUX = value;
                    break;
                case "MOVE_COST":
                    MOVE_COST = value;
                    break;
                case "ATTACK_COST":
                    ATTACK_COST = value;
                    break;
                case "GROW_COST":
                    GROW_COST = value;
                    break;
                case "BUD_COST":
                    BUD_COST = value;
                    break;
                case "MATE_COST":
                    MATE_COST = value;
                    break;
                case "RULE_COST":
                    RULE_COST = value;
                    break;
                case "ABILITY_COST":
                    ABILITY_COST = value;
                    break;
                case "INITIAL_ENERGY":
                    INITIAL_ENERGY = value;
                    break;
                case "MIN_MEMORY":
                    MIN_MEMORY = value;
                    break;
                case "DAMAGE_INC":
                    DAMAGE_INC = Double.parseDouble(next[1]);
                    break;


            }
        }
    }
}
