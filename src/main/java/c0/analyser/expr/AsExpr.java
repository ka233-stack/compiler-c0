package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.Token;

public class AsExpr extends Expr {
    public Expr expr;
    public IdentType type;

    public AsExpr(Expr expr, IdentType type) {
        this.expr = expr;
        this.type = type;
    }

    public IdentType getType() {
        return this.type;
    }

    public void setType(IdentType type) {
        this.expr.setType(type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(this.type);
        sb.append(")");
        sb.append(this.expr);
        return sb.toString();
    }
}
