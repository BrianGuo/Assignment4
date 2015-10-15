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
        Program prog = ParserFactory.getParser().parse(new FileReader("example-rules.txt"));
        System.out.println(prog);
//        for(int i = 0; i < 10; i++) {
//
//            Mutation duplicate = MutationFactory.getDuplicate();
//            Program mutatedProg = prog.mutate(0, duplicate);
//            System.out.println(mutatedProg);
//            prog = mutatedProg;
//            assertTrue(testIdentity(prog));
//        }
        for(int i = 0; i < 10; i++){
            System.out.println(prog.size());
            System.out.println(prog);
            Program mutatedProg = prog.mutate();

            prog = mutatedProg;
            assertTrue(testIdentity(prog));
        }

    }


    @Test
    public void testPrintParse() throws Exception {
        Program prog = ParserFactory.getParser().parse(new FileReader("example-rules.txt"));
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