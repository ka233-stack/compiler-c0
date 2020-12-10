package main.java.c0.analyser.statement;

import main.java.c0.analyser.expr.Expr;

public class ExprStmt extends Stmt {

    public Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.expr);
        sb.append(";\n");
        return sb.toString();
    }
}
