package main.java.c0.analyser.statement;

import main.java.c0.analyser.expr.Expr;

public class WhileStmt extends Stmt {
    public Expr condition;
    public BlockStmt whileBlock;

    public WhileStmt(Expr condition, BlockStmt whileBlock) {
        this.condition = condition;
        this.whileBlock = whileBlock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("while (");
        sb.append(this.condition);
        sb.append(") ");
        sb.append(this.whileBlock);
        sb.append("\n");
        return sb.toString();
    }
}