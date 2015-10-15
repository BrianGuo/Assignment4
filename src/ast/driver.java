package ast;

import parse.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import parse.*;


public class driver {

	
	public static void main(String[] args){
		try{
			FileReader r = new FileReader("examples/example-rules.txt");
			Program prog = ParserFactory.getParser().parse(r);
			System.out.print(prog.toString());
			prog.mutate();
		}
		catch(FileNotFoundException e){
			System.out.println("file not found");
		}
	}
}