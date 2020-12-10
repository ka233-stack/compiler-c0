package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.Token;
import main.java.c0.tokenizer.TokenType;

public class IdentExpr extends Expr {
    public Token ident;
    public IdentType type;

    public IdentExpr(Token ident, IdentType type) {
        this.ident = ident;
        this.type = type;
    }

    public IdentType getType() {
        return this.type;
    }

    @Override
    public void setType(IdentType type) {
        // TODO 转换规则
        if (this.type == IdentType.INT) {
            if (type == IdentType.DOUBLE) {
                this.type = type;
            }
        }
        this.type = type;
    }

    public Token getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return ident.getValueString();
    }
}
