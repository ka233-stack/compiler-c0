package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.Token;

public class LiteralExpr extends Expr {
    public IdentType type;
    public Token literal;


    public LiteralExpr(IdentType type, Token literal) {
        this.type = type;
        this.literal = literal;

    }

    public IdentType getType() {
        return this.type;
    }

    @Override
    public void setType(IdentType type) {
        this.type = type;
    }

    public Token getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return this.literal.getValueString();
    }
}
