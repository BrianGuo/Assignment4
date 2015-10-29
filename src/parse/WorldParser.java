package parse;

import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import world.Factory;
import world.World;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.Scanner;


public class WorldParser {
    /**
     * Creates a world, given a reader.
     * Ignores any line that does not start with "name", "size", "rock", "food", or "critter",
     * including blank lines and comments.
     * @param r Reader input.
     * @return The created world, as according to the file.
     * @throws SyntaxError if the world file has invalid irrecoverable syntax errors
     * (ex. rock -1 0 or rock asdf)
     */
    public World parseWorld(Reader r) throws SyntaxError, FileNotFoundException{
        Scanner sc = new Scanner(r);
        String[] cur;

        cur = sc.nextLine().split("\\s+");
        while(!cur[0].equals("name")){
            cur = sc.nextLine().split("\\s+", 1);
        }
        String name = cur[1];

        cur = sc.nextLine().split("\\s+");
        while(!cur[0].equals("size")){
            cur = sc.nextLine().split("\\s+");
        }
        World world = Factory.getWorld(cur[1], cur[2], name);

        while(sc.hasNext()){
            cur = sc.nextLine().split("\\s+");
            try {
                switch (cur[0]) {
                    case "rock":
                        world.add(Factory.getRock(cur[1],cur[2],world.constants));
                        break;
                    case "food":
                        world.add(Factory.getFood(cur[1], cur[2], cur[3],world.constants));
                        break;
                    case "critter":
                        world.add(Factory.getCritter(cur[1], cur[2], cur[3], cur[4], world.constants));
                        break;
                    default:
                        //ignore
                        break;
                }
            }
            //IllegalCoordinate is for negative coords, array index for too few args
            //IllegalArg if wrong type is provided or food amount <= 0
            catch(IllegalCoordinateException | ArrayIndexOutOfBoundsException | IllegalArgumentException e){
                //e.getStackTrace();
                throw new SyntaxError("Syntax error in world file");
            }

        }
        //TODO: Complete the rest...?
        return world;

    }
}
