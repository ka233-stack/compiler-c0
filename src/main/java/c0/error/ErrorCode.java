package main.java.c0.error;

public enum ErrorCode {
    NoError, // 只能在内部使用
    StreamError, //
    EOF, // End of File
    InvalidInput, // 非法输入
    InvalidIdentifier, // 非法标识符
    IntegerOverflow, // int32_t overflow.

    // 字面量
    DoubleOverflow, // double overflow

    NotLiteral, // 非法字面量
    NeedIdentifier, // 缺少标识符
    ConstantNeedValue, // 常量需要初始值
    NoSemicolon, // 缺少分号
    InvalidVariableDeclaration, //
    IncompleteExpression, // 表达式不完整
    NotDeclared, // 未声明
    AssignToConstant, // 赋值常量
    DuplicateDeclaration, // 重复声明
    NotInitialized, // 未初始化
    InvalidAssignment, // 非法赋值
    InvalidPrint, // 非法输出

    DoNotSupportOperations, // 类型不支持运算
    FunctionNotExist, // 函数不存在
    SymbolNotExist, // 符号不存在
    UnsupportedSymbol,
    MainFunctionHaveNoParams, //main函数不能有参数
    MainFunctionOnlyReturnVoid, // main函数只能返回void
    // 调用函数
    NotADeclarationOrFunction, // 不是一个声明或函数
    ExpectedToken, // 缺少token
    CannotBeDefinedAsVoid, // 类型不能定义为 void
    ConstantsCannotBeModified, // 常量不能被修改
    UnsupportedType, // 不支持的类型
    UnsupportedTypeCasting, // 不支持的类型转换
    TooManyOrTooFewArguments, // 参数过多或过少
    TypeMismatch, // 类型不匹配

    ReturnValueTypeMismatched, // 返回值类型不匹配

    // 语法分析
    NeedBinaryOperator, // 缺少二元运算符
    NeedReturnStatement,
    IllegalInstruction,  // 非法指令
    UnsupportedInstructionType //不支持的指令类型

    // o0

}
