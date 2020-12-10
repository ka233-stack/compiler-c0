package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.Token;
import main.java.c0.tokenizer.TokenType;

import java.util.ArrayList;

public class CallExpr extends Expr {

    public ArrayList<Expr> params;
    public Token name;
    public IdentType type;

    public CallExpr(Token name, ArrayList<Expr> params, IdentType type) {
        this.params = params;
        this.name = name;
        this.type = type;
    }

    public IdentType getType() {
        return this.type;
    }

    @Override
    public void setType(IdentType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name.getValueString());
        sb.append("(");
        if (params != null) {
            int size = params.size();
            for (int count = 0; count < size; count++) {
                sb.append(params.get(count));
                if (count != size - 1)
                    sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
