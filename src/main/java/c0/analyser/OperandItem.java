package main.java.c0.analyser;

import main.java.c0.util.Pos;

public class OperandItem {
    private IdentType type;
    private Pos pos;

    public OperandItem(IdentType type, Pos pos) {
        this.type = type;
        this.pos = pos;
    }

    public IdentType getType() {
        return type;
    }

    public void setType(IdentType type) {
        this.type = type;
    }

    public Pos getPos() {
        return pos;
    }
}
