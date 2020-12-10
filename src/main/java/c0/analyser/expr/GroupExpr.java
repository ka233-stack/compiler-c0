package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.TokenType;

public class GroupExpr extends Expr {
    public Expr expr;
    public IdentType type;

    public GroupExpr(Expr expr) {
        this.expr = expr;
        this.type = IdentType.VOID;
    }

    public IdentType getType() {
        return this.type;
    }

    @Override
    public void setType(IdentType type) {
        this.setType(type);
    }
}
