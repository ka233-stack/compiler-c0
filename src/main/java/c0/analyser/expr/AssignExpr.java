package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.Token;


public class AssignExpr extends Expr {
    public Token ident;
    public Expr expr;
    public IdentType type;

    public AssignExpr(Token ident, Expr expr) {
        this.ident = ident;
        this.expr = expr;
        this.type = IdentType.VOID;
    }

    public IdentType getType() {
        return this.type;
    }

    @Override
    public void setType(IdentType type) {
        this.expr.setType(type);
    }

    @Override
    public String toString() {
        return this.ident.getValueString() +
                " = " +
                this.expr;
    }
}
