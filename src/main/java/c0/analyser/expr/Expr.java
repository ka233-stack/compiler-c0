package main.java.c0.analyser.expr;

import main.java.c0.analyser.IdentType;

public abstract class Expr {

    public abstract IdentType getType();

    public abstract void setType(IdentType type);
}
