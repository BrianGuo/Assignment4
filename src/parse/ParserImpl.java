package parse;

import java.io.Reader;
import java.util.Stack;

import ast.*;
import exceptions.SyntaxError;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;

class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) {
        // TODO
        throw new UnsupportedOperationException();
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
        ProgramImpl program = new ProgramImpl();
        while(t.hasNext()){
            //program.
            program.add(parseRule(t));
            //t.next(); //do I do this or not
        }

        throw new UnsupportedOperationException();
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Condition condition;
        Command command;
        condition = parseCondition(t);
        consume(t, TokenType.ARR);
        command = parseCommand(t);
        consume(t, TokenType.SEMICOLON);
        return new Rule(condition, command);
    }

    public static Command parseCommand(Tokenizer t) throws SyntaxError {
        Command command = new Command();
        ActionNode action;
        while (t.peek().getType() == TokenType.MEM) { //this also handles the case of update-or-action being an update
            command.addUpdate(parseUpdate(t));
        }
        if(t.peek().isAction()){ //last one wasn't actually an update
            action = parseAction(t);
            command.addAction(action);
        }
        return command;
    }

    /**
     * Precondition: The next token is actually an action
     */
    public static ActionNode parseAction(Tokenizer t) throws SyntaxError {
        ActionNode actionNode;
        if(t.peek().toString().equals("tag") || t.peek().toString().equals("serve" )){
            Token action = t.next(); //not consume() because we have 2 different types we already checked for...
            consume(t, TokenType.LBRACKET);
            Expr expr = parseExpression(t);
            consume(t, TokenType.RBRACKET);
            return new ActionNode(action, expr);
        }
        else{
            return new ActionNode(t.next());
        }
    }

    public static UpdateNode parseUpdate(Tokenizer t) throws SyntaxError {
        UpdateNode update;
        consume(t, TokenType.MEM);
        consume(t, TokenType.LBRACKET);
        MemoryNode mem = new MemoryNode(parseExpression(t));
        consume(t, TokenType.RBRACKET);
        consume(t, TokenType.ASSIGN);
        Expr e = parseExpression(t);
        update = new UpdateNode(mem, e);
        return update;
    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError {

        //turns out I was describing the shunting yard algorithm
        //push literals onto stack, push conditions onto another stack
        //compare precedence (and vs or): if higher precedence, then pop 2, create tree, push tree

        Stack<Condition> literals = new Stack<>();
        Stack<Token> conditions = new Stack<>();
        literals.add(parseBrace(t));


        while(t.peek().getType() == TokenType.OR  || t.peek().getType() == TokenType.AND){ //if there are more things...
            try {
                Token cur = t.next(); //either OR or AND
                //conditions.add(t.next());
                literals.push(parseBrace(t)); //push the next literal onto the literals stack (or a subtree if it's a brace)
                while(true) {
                    if (compare(cur, conditions.peek())) { //we should reduce now
                        Condition r = literals.pop(); //first one
                        Condition l = literals.pop(); //second one
                        Condition cond = new BinaryCondition(l, conditions.pop(), r); //form the tree...
                        literals.push(cond); //push tree onto literals!
                    } else { //none left
                        conditions.push(cur);
                        break;
                    }
                }
            }

            catch(Exception e){
                throw new SyntaxError(); //Because this means someone screwed up
            }
        }
        //there may still be some left on the stack
        while(conditions.peek() != null){
            Condition r = literals.pop(); //first one
            Condition l = literals.pop(); //second one
            Condition cond = new BinaryCondition(l, conditions.pop(), r);
            literals.push(cond);
        }

        return literals.pop();
    }

    /**
     * Compares precedence of operators
     * Works for both conditions and expressions
     * Return value of True means to pop and reduce, False means to push
     * @param cur
     * @param peek
     * @return true if precedence of cur <= precedence of top of stack (peek), talse if cur < precedence.
     */
    private static boolean compare(Token cur, Token peek) {
        if(peek == null){ //EOF
            return false;
        }
        TokenType tt1 = cur.getType();
        TokenType tt2 = peek.getType();
        if(tt1 == TokenType.AND || cur.isMulOp()){
            return true;
        }
        else if((tt1 == TokenType.OR && tt2 == TokenType.OR) || cur.isAddOp() && peek.isAddOp()){
            return true;
        }
        return false;
    }


    private static Condition parseBrace(Tokenizer t) throws SyntaxError {
        Condition condition;
        if(t.peek().getType() == TokenType.LBRACE){
            consume(t, TokenType.LBRACE);
            condition = parseCondition(t);
            consume(t, TokenType.RBRACE);
        }
        else{
            condition = parseRelation(t);
        }
        return condition;
    }



    public static Relation parseRelation(Tokenizer t) throws SyntaxError {
        Expr l = parseExpression(t);
        if(t.peek().isRelOp()){
            Token rel = t.next();
            Expr r = parseExpression(t);
            return new Relation(l, r, rel);
        }
        else{
            throw new SyntaxError();
        }
    }

    /*
     * Well, I already wrote an implementation of the shunting-yard algorithm, so might as well do the exact same thing!
     * Unfortunately we didn't do the OO design well enough--ideally, Condition and BinaryOp would both implement
     * some binary interface, and I would be able to set the l and r sides and thus reuse the code.  Oh well.
     */
    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        Stack<Expr> literals = new Stack<>();
        Stack<Token> operations = new Stack<>();
        literals.add(parseParen(t));


        while(t.peek().isAddOp() || t.peek().isMulOp()){ //if there are more things...
            try {
                Token cur = t.next(); //either OR or AND

                literals.push(parseParen(t)); //push the next literal onto the literals stack (or a subtree if it's a brace)
                while(true) {
                    if (compare(cur, operations.peek())) { //we should reduce now
                        Expr r = literals.pop(); //first one
                        Expr l = literals.pop(); //second one
                        Expr op = new BinaryOp(l,r,operations.pop()); //form the tree...
                        literals.push(op); //push tree onto literals!
                    } else { //none left
                        operations.push(cur);
                        break;
                    }
                }
            }

            catch(Exception e){
                throw new SyntaxError(); //Because this means someone screwed up
            }
        }
        //there may still be some left on the stack
        while(operations.peek() != null){
            Expr r = literals.pop(); //first one
            Expr l = literals.pop(); //second one
            Expr cond = new BinaryOp(l,r, operations.pop());
            literals.push(cond);
        }

        return literals.pop();
    }

    /**
     * Bad OO design strikes again...different types so I had to hardcode it.  Ugh.
     */
    private static Expr parseParen(Tokenizer t) throws SyntaxError {
        Expr expr;
        if(t.peek().getType() == TokenType.LPAREN){
            consume(t, TokenType.LPAREN);
            expr = parseFactor(t);
            consume(t, TokenType.RPAREN);
        }
        else{
            expr = parseFactor(t);
        }
        return expr;
    }

    //Obsoleted by shunting-yard
    public static Expr parseTerm(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError {
        Token cur = t.next();
        Expr factor;
        if(cur.getType() == TokenType.MEM){
            consume(t, TokenType.MEM);
            consume(t, TokenType.LBRACKET);
            factor = parseExpression(t);
            consume(t, TokenType.RBRACKET);
        }
        else if (cur.getType() == TokenType.MINUS){ //unary negation
            consume(t, TokenType.MINUS);
            factor = new Negation(t.next().toNumToken().getValue());
        }
        else if (cur.isNum()){ //regular number
            factor = new NumberNode(t.next().toNumToken().getValue());
        }
        else{ //sensor
            factor = parseSensor(t);
        }
        return factor;
    }

    public static Expr parseSensor(Tokenizer t) throws SyntaxError {
        Token cur = t.next();
        Expr sensor;
        if(cur.getType() == TokenType.SMELL){
            //er...need sensor nodes.
        }
    }

    // TODO
    // add more as necessary...

    /**
     * Consumes a token of the expected type.
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        if(t.peek().getType() == tt){
            t.next();
        }
        else{
            throw new SyntaxError();
        }
    }
}
