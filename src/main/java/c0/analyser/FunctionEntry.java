package main.java.c0.analyser;

public class FunctionEntry {
    private int funcNo;
    private int nextLocOffset;
    private int nextParamOffset;

    public FunctionEntry(int funcNo) {
        this.funcNo = funcNo;
        this.nextLocOffset = 0;
        this.nextParamOffset = 1;
    }

    public int getFuncNumOffset() {
        return funcNo;
    }


    public int getNextLocOffset() {
        return nextLocOffset++;
    }

    public int getNextParamOffset() {
        return nextParamOffset++;
    }
}
