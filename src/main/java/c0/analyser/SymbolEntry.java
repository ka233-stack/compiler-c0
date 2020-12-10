package main.java.c0.analyser;

public class SymbolEntry {
    SymbolType symbolType;
    IdentType valueType; // 类型`
    int offset; //

    public SymbolEntry(SymbolType symbolType, IdentType valueType, int offset) {
        this.symbolType = symbolType;
        this.valueType = valueType;
        this.offset = offset;
    }

    public IdentType getValueType() {
        return valueType;
    }

    public void setValueType(IdentType valueType) {
        this.valueType = valueType;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.valueType);
        return sb.toString();
    }
}


