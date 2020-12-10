package main.java.c0.tokenizer;


import main.java.c0.util.Pos;

import java.util.Objects;

public class Token {

    private TokenType tokenType;
    private Object value;
    private Pos startPos;
    private Pos endPos;

    public Token(TokenType tokenType, Object value, Pos startPos, Pos endPos) {
        this.tokenType = tokenType;
        this.value = value;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, value, startPos, endPos);
    }

    public String getValueString() {
        if (value instanceof Integer || value instanceof String || value instanceof Character || value instanceof Double) {
            return value.toString();
        }
        throw new Error("No suitable cast for token value.");
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Pos getStartPos() {
        return this.startPos;
    }


    public Pos getEndPos() {
        return this.endPos;
    }

    @Override
    /*
     * "Line: row Column: col Type: tokenType Value: value"
     */
    public String toString() {
        var sb = new StringBuilder();
        if (startPos != null) {
            sb.append("Line: ").append(this.startPos.row).append(' ');
            sb.append("Column: ").append(this.startPos.col).append(' ');
        }
        sb.append("Type: ").append(this.tokenType).append(' ');
        sb.append("Value: ").append(this.value);
        return sb.toString();
    }

    /*
     * "Token(tokenType, value: valueat: startPos(row: row, col: col)
     */
    public String toStringAlt() {
        return new StringBuilder().append("Token(").append(this.tokenType).append(", value: ").append(value)
                .append("at: ").append(this.startPos).toString();
    }
}
