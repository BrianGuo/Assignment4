package parse;

import static parse.TokenCategory.*;
import static parse.TokenType.EOF;
import static parse.TokenType.ERROR;
import static parse.TokenType.NUM;

import java.util.InputMismatchException;

/**
 * A Token represents a legal token (symbol) in the critter language
 */
public class Token {
    private final TokenType type;
    final int lineNo;

    /**
     * Create a token with the specified type.
     * 
     * @param type
     *            The ID of the desired token type
     * @param lineNo
     *            The line number in the input file containing this token.
     */
    public Token(final TokenType type, final int lineNo) {
        this.type = type;
        this.lineNo = lineNo;
    }

    /**
     * @return The type of this token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * @return The line number in the input file of this token.
     */
    int lineNumber() {
        return lineNo;
    }

    /**
     * Determine whether this token is of number type.
     * 
     * @return true if this token is of number type
     */
    boolean isNum() {
        return type == NUM;
    }

    /**
     * @return The number token associated with this token.
     * @throws InputMismatchException
     *             if this token is not of number type
     */
    NumToken toNumToken() {
        if (isNum()) return (NumToken) this;
        throw new InputMismatchException("Token is not a number.");
    }

    /**
     * Determine whether this token is of action type.
     * 
     * @return true if this token is of action type
     */
    public boolean isAction() {
        return type.category == ACTION;
    }

    /**
     * Determine whether this token is of addop type.
     * 
     * @return true if this token is of addop type
     */
    public boolean isAddOp() {
        return type.category == ADDOP;
    }

    /**
     * Determine whether this token is of mulop type.shorthand.
     * 
     * @return true if this token is of mulop type.shorthand
     */
    public boolean isMulOp() {
        return type.category == MULOP;
    }
    
    public boolean isRelOp() {
    	return type.category == TokenCategory.RELOP;
    }

    /**
     * Determine whether this token is of sensor type.shorthand.
     * 
     * @return true if this token is of sensor type.shorthand
     */
    public boolean isSensor() {
        return type.category == SENSOR;
    }

//    /**
//     * Determine whether this token is of relop type.1
//     *
//     * @return true if this token is of relop type.
//     */
//    boolean isRelOp() { return type.category == RELOP; }

    /**
     * Determine whether this token is syntactic sugar for memory locations
     * 
     * @return true if this token is syntactic sugar for memory locations
     */
    public boolean isMemSugar() {
        return type.category == MEMSUGAR;
    }

    @Override
    public String toString() {
        return type.stringRep;
    }

    /**
     * A NumToken is a token containing a number.
     * 
     * @author Chinawat
     */
    static class NumToken extends Token {

        private final int value;

        NumToken(final int value, final int lineNo) {
            super(NUM, lineNo);
            this.value = value;
        }

        int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    static class EOFToken extends Token {

        private final String bufferContents;

        EOFToken(final String bufferContents, final int lineNo) {
            super(EOF, lineNo);
            this.bufferContents = bufferContents;
        }

        String getBufferContents() {
            return bufferContents;
        }

        @Override
        public String toString() {
            return "Buffer contained " + bufferContents + " at EOF";
        }
    }

    static class ErrorToken extends Token {

        private final String value;

        ErrorToken(final String value, final int lineNo) {
            super(ERROR, lineNo);
            this.value = value;
        }

        String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "[error@" + lineNo + "]: " + value;
        }
    }
}
