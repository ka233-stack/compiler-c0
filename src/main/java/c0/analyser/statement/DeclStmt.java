package main.java.c0.analyser.statement;

import main.java.c0.analyser.IdentType;
import main.java.c0.analyser.expr.Expr;
import main.java.c0.tokenizer.Token;

public class DeclStmt extends Stmt {
    public boolean isConst;
    public boolean isInitialized;
    public Token ident;
    public IdentType type; // 记录ident类型
    public Expr expr;

    public DeclStmt(boolean isConst, boolean isInitialized, Token ident, IdentType type, Expr expr) {
        this.isConst = isConst;
        this.isInitialized = isInitialized;
        this.ident = ident;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.isConst)
            sb.append("const ");
        else
            sb.append("let ");
        sb.append(this.type);
        sb.append(" ");
        sb.append(this.ident.getValueString());
        if (this.isInitialized) {
            sb.append(" = ");
            sb.append(expr);
        }
        sb.append(";\n");
        return sb.toString();
    }
}
