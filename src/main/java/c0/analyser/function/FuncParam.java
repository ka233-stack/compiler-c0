package main.java.c0.analyser.function;

import main.java.c0.analyser.IdentType;
import main.java.c0.tokenizer.Token;

public class FuncParam {
    public boolean isConst;
    public Token ident;
    public IdentType type;

    public FuncParam(boolean isConst, Token ident, IdentType type) {
        this.isConst = isConst;
        this.ident = ident;
        this.type = type;
    }

    public boolean isConst() {
        return isConst;
    }

    public Token getIdent() {
        return ident;
    }

    public IdentType getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.isConst)
            sb.append("const ");
        sb.append(this.type);
        sb.append(" ");
        sb.append(this.ident.getValueString());
        return sb.toString();
    }
}
