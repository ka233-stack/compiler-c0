package main.java.c0.error;

import main.java.c0.util.Pos;

public abstract class CompileError extends Exception {

    private static final long serialVersionUID = 1L;

    public abstract ErrorCode getErr();
}