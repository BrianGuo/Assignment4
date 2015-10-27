package tests;

import org.junit.Test;
import parse.*;
import ast.*;

import java.io.FileReader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ProgramTest {

    @Test
    public void testMutate() throws Exception {
        Program prog = ParserFactory.getParser().parse(new FileReader("examples/example-rules.txt"));
        System.out.println(prog.size());
        int size = prog.size();
        for(int i = 0; i < 100; i++) {
//            System.out.println(i);
            //Mutation mutation = MutationFactory.getReplace(); //replace is broken because nodeAt and getParent are broken
            Mutation mutation = MutationFactory.getTransform();

            Program mutatedProg = prog.mutate((int) (Math.random() * prog.size()), mutation);
            prog = mutatedProg;
            assertTrue(testIdentity(prog));
        }

        for(int i = 0; i < 1000; i++){
            Program mutatedProg = prog.mutate();

            prog = mutatedProg;
            assertTrue(testIdentity(prog));
        }

    }


    @Test
    public void testPrintParse() throws Exception {
        Program prog = ParserFactory.getParser().parse(new FileReader("examples/example-rules.txt"));
        assertTrue(testIdentity(prog));
    }

    /**
     * Returns true if prog.toString() is the same as parse(prog.toString()).toString()
     */
    public boolean testIdentity(Program prog){
        String pp = prog.toString();
        Program prog2 = ParserFactory.getParser().parse(new StringReader(pp));
        String pp2 = prog.toString();
        return pp.equals(pp2);
    }

}