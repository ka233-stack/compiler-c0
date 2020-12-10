package main.java.c0.analyser.statement;

import java.util.ArrayList;

public class BlockStmt extends Stmt {
    public ArrayList<Stmt> stmts;

    public BlockStmt(ArrayList<Stmt> stmts) {
        this.stmts = stmts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\n");
        for (Stmt stmt : stmts) {
            sb.append("\t");
            sb.append(stmt);
        }
        sb.append("}");
        return sb.toString();
    }
}