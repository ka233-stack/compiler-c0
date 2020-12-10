package main.java.c0.analyser;

public enum SymbolType {
    FUNCNAME, // 函数名
    LETNOTINIT, // 未初始化变量
    LETINIT, //  已初始化变量
    CONST, // 常量
    CONSTPARAM, // 常量参数
    LETPARAM; // 变量参数


    @Override
    public String toString() {
        return super.toString();
    }
}
