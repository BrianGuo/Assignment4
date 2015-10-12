package parse;

import java.io.Reader;

import ast.*;
import exceptions.SyntaxError;

class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Helper function that checks if the next Token is the given type.  Consumes and advances if so, throws SyntaxError
     * otherwise.  Use when you don't care about the next token, just that it's there.
     * @param t Tokenizer used everywhere
     * @param type Type to compare the token to
     * @throws SyntaxError
     */
    private static void compareAndAdvance(Tokenizer t, TokenType type) throws SyntaxError{
        if(t.peek().getType() == type){
            t.next();
        }
        else{
            throw new SyntaxError();
        }
    }

    /** Parses a program from the stream of tokens provided by the Tokenizer,
     *  consuming tokens representing the program. All following methods with
     *  a name "parseX" have the same spec except that they parse syntactic form
     *  X.
     *  @return the created AST
     *  @throws SyntaxError if there the input tokens have invalid syntax
     */
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        //TODO Parse rules until we reach the end
        ProgramImpl program;
        while(t.hasNext()){
            program.add(parseRule(t));
            t.next(); //do I do this or not
        }

        throw new UnsupportedOperationException();
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Token cond;
        Token command;
        //praseCondition
        compareAndAdvance(t,TokenType.ARR);
        command = parseCommand(t);
        compareAndAdvance(t, TokenType.SEMICOLON);
    }

    public static Command parseCommand(Tokenizer t) throws SyntaxError {
        //parse any pos number of updates, then an update-or-action
    }

    public static UpdateNode parseUpdate(Tokenizer t) throws SyntaxError {
        UpdateNode update;
        compareAndAdvance(t, TokenType.MEM);
        compareAndAdvance(t, TokenType.LBRACKET);
        parseExpression(t); //TODO: add this as a node to update
        compareAndAdvance(t, TokenType.RBRACKET);
        compareAndAdvance(t, TokenType.ASSIGN);
        parseExpression(t); //TODO: add this as a node to update
    }

    public static BinaryCondition parseCondition(Tokenizer t) throws SyntaxError {
        // TODO
        BinaryCondition condition;
        parseConjunction(t); //TODO: add this as left side of the condition
        while(t.peek().getType() == TokenType.OR  || t.peek().getType() == TokenType.AND){
            BinaryCondition condition2;
            t.next();
            parseConjunction(t); //TODO: add this to condition2
            //TODO: add condition2 as right side of condition

        }
        throw new UnsupportedOperationException();
    }

    public static BinaryCondition parseConjunction(Tokenizer t) throws SyntaxError {
        BinaryCondition conj;
        parse
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    // TODO
    // add more as necessary...

    /**
     * Consumes a token of the expected type.
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }
}
