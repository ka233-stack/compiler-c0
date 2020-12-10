package main.java.c0.analyser.function;

import main.java.c0.analyser.IdentType;
import main.java.c0.analyser.statement.BlockStmt;

import java.util.ArrayList;

public class Function {
    public String funcName; //ident
    public ArrayList<FuncParam> params;
    public IdentType retType;
    public boolean hasRetStmt;
    public BlockStmt funcBody;

    public Function(String funcName, ArrayList<FuncParam> params, IdentType retType, BlockStmt funcBody) {
        this.funcName = funcName;
        this.params = params;
        this.retType = retType;
        this.funcBody = funcBody;
    }

    public boolean isHasRetStmt() {
        return this.hasRetStmt;
    }

    public void setHasRetStmt(boolean hasRetStmt) {
        this.hasRetStmt = hasRetStmt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.retType);
        sb.append(" ");
        sb.append(this.funcName);
        sb.append(" (");
        if (params != null) {
            int size = params.size();
            for (int count = 0; count < size; count++) {
                sb.append(params.get(count));
                if (count != size - 1)
                    sb.append(", ");
            }
        }
        sb.append(") ");
        sb.append(this.funcBody);
        return sb.toString();
    }
}
