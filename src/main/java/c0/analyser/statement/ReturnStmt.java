package main.java.c0.analyser.statement;

import main.java.c0.analyser.expr.Expr;

public class ReturnStmt extends Stmt {
    public Expr retValue;
    public boolean hasRetValue;

    public ReturnStmt() {
        this.hasRetValue = false;
    }

    public ReturnStmt(Expr retValue) {
        this.hasRetValue = true;
        this.retValue = retValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("return ");
        if (this.hasRetValue) {
            sb.append(this.retValue.toString());
            sb.append(";\n");
        } else
            sb.append("null;\n");
        return sb.toString();
    }
}