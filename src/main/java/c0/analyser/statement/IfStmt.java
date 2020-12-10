package main.java.c0.analyser.statement;

import main.java.c0.analyser.expr.Expr;

public class IfStmt extends Stmt {
    public boolean hasElse;
    public Expr condition;
    public BlockStmt ifBlock;
    public Stmt elseBlock;

    public IfStmt(Expr condition, BlockStmt ifBlock) {
        this.hasElse = false;
        this.condition = condition;
        this.ifBlock = ifBlock;
    }

    public IfStmt(Expr condition, BlockStmt ifBlock, Stmt elseBlock) {
        this.hasElse = true;
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("if (");
        sb.append(this.condition);
        sb.append(") ");
        sb.append(this.ifBlock);
        if (this.hasElse) {
            sb.append(this.elseBlock);
        }
        sb.append("\n");
        return sb.toString();
    }
}