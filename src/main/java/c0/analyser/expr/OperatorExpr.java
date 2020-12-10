package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.error.AnalyzeError;
import main.java.c0.error.ErrorCode;
import main.java.c0.tokenizer.Token;
import main.java.c0.tokenizer.TokenType;

public class OperatorExpr extends Expr {
    public Expr expr_l;
    public Token op;
    public Expr expr_r;
    public IdentType type;

    public OperatorExpr(Expr expr_l, Expr expr_r, Token op) throws AnalyzeError {
        if (!expr_l.getType().equals(expr_r.getType()))
            throw new AnalyzeError(ErrorCode.TypeMismatch, op.getEndPos());
        this.expr_l = expr_l;
        this.expr_r = expr_r;
        this.op = op;
        this.type = expr_l.getType();
    }

    public IdentType getType() {
        return this.type;
    }

    @Override
    public void setType(IdentType type) {
        this.expr_l.setType(type);
        this.expr_r.setType(type);
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.expr_l);
        sb.append(" ");
        sb.append(this.op.getValueString());
        sb.append(" ");
        sb.append(this.expr_r);
        return sb.toString();
    }
}
