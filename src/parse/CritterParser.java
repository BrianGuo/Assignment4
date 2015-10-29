package parse;


import ast.Program;
import exceptions.SyntaxError;
import world.Coordinate;
import world.Critter;
import world.WorldConstants;

import java.io.Reader;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CritterParser {

    public static Critter parseCritter(Reader r, WorldConstants constants) throws SyntaxError {
        Scanner sc = new Scanner(r);
        String species = "";
        int memsize= 0;
        int defense = 0;
        int offense = 0;
        int size = 0;
        int energy = 0;
        int posture = 0;
        //Read the next 7 lines and initialize these variables yay
        //While reading directly into a string array could make the code simpler, this allows
        //for the order of attributes to be switched around
        for(int i = 0; i < 7; i++) {
            String[] next = sc.nextLine().split(":\\s+");
            int value = 0;
            try{
                value = Integer.parseInt(next[1]);
            }
            catch(NumberFormatException e){
                //ignore and deal with later
            }
            //TODO Take maximum of the value and the mininum value. Or keep inside bounds. 
            switch (next[0]) {
                case "species":
                    species = next[1];
                case "memsize":
                    memsize = value;
                    break;
                case "defense":
                    defense = value;
                    break;
                case "offense":
                    offense = value;
                    break;
                case "size":
                    size = value;
                    break;
                case "energy":
                    energy = value;
                    break;
                case "posture":
                    posture = value;
                    break;
                default:
                    break;
            }
        }
        //The scanner has now advanced past the attributes and the remainder contains the ruleset
        Program program = ParserFactory.getParser().parse(r);
        //{memsize, defense, offense, size, energy, pass, tag,posture}
        int[] memory = {memsize, defense, offense, size, energy, 0, 0, posture};
        return new Critter(memory, (int)(Math.random() * 6), species, new Coordinate(0,0), constants, program);

    }
}
