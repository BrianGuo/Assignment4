package main;
import ast.*;
import parse.*;
import exceptions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class Driver {
    public static void main(String[] args){

        FileReader inputFile;
        try {

            if(args[0].equals("--mutate")){
                int numMutations = Integer.parseInt(args[1]);
                inputFile = new FileReader(args[2]);

                Program prog = ParserFactory.getParser().parse(inputFile);

                for(int i = 0; i < numMutations; i++){
                prog.mutate();
                //print out what got changed
                    System.out.println(prog);
                }

            }
            else{
                inputFile = new FileReader(args[0]);
                Program prog = ParserFactory.getParser().parse(inputFile);
                System.out.println(prog);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }

        catch (Exception e){
            System.out.println("Usage: java -jar <jar_name> <input_file>\n" +
                    "java -jar <jar_name> --mutate <n> <input_file>");
            System.exit(1);
        }

    }


}
