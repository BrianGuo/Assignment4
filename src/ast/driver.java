package ast;

import parse.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import parse.*;


public class driver {

	
	public static void main(String[] args){
		try{
			FileReader r = new FileReader("examples/example-rules.txt");
			Program prog = ParserFactory.getParser().parse(r);
			System.out.println(prog.toString());
			System.out.println();
			System.out.println();
			Mutation m = MutationFactory.getSwap();
			for(int i = 0; i < prog.size() ; i++ ){
				//System.out.println(prog.nodeAt(i).getClass() + " ");
				//prog.mutate(i,m);
			}
			
			//System.out.println(prog);
		}
		catch(FileNotFoundException e){
			System.out.println("file not found");
		}
	}
}