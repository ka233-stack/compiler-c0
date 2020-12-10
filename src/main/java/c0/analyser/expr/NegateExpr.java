package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.TokenType;

public class NegateExpr extends Expr {
    public Expr expr;

    public NegateExpr(Expr expr) {
        this.expr = expr;
    }

    public IdentType getType() {
        return this.expr.getType();
    }

    @Override
    public void setType(IdentType type) {
        this.expr.setType(type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("- ");
        sb.append(this.expr);
        return sb.toString();
    }
}
