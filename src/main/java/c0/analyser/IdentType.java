package main.java.c0.analyser;

import main.java.c0.tokenizer.TokenType;

public enum IdentType {
    INT, // int
    DOUBLE, // double
    STRING, // string
    VOID, // void
    CHAR,// char
    BOOL,
    NONE;


    @Override
    public String toString() {
        switch (this) {
            case INT:
                return "int";
            case CHAR:
                return "char";
            case STRING:
                return "string";
            case DOUBLE:
                return "double";
            case BOOL:
                return "bool";
            case VOID:
                return "void";
            default:
                return "无效类型";
        }
    }

    public static IdentType strToIdentType(String str) {
        switch (str) {
            case "int":
                return IdentType.INT;
            case "double":
                return IdentType.DOUBLE;
            case "void":
                return IdentType.VOID;
            default:
                return IdentType.NONE;
        }
    }
}
