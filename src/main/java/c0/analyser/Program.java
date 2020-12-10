package main.java.c0.analyser;

import main.java.c0.analyser.function.Function;
import main.java.c0.analyser.statement.DeclStmt;

import java.util.ArrayList;

public class Program {
    public ArrayList<DeclStmt> declStmts;
    public ArrayList<Function> functions;

    public Program(ArrayList<DeclStmt> declStmts, ArrayList<Function> functions) {
        this.declStmts = declStmts;
        this.functions = functions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (DeclStmt declStmt : declStmts) {
            sb.append(declStmt);
            sb.append("\n");
        }
        for (Function function : functions) {
            sb.append(function);
            sb.append("\n");
        }
        return sb.toString();
    }
}
