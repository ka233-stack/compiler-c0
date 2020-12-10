package main.java.c0.analyser;

import main.java.c0.analyser.o0.GlobalDef;
import main.java.c0.analyser.o0.O0;
import main.java.c0.error.*;
import main.java.c0.instruction.Instruction;
import main.java.c0.instruction.Operation;
import main.java.c0.tokenizer.Token;
import main.java.c0.tokenizer.TokenType;
import main.java.c0.tokenizer.Tokenizer;
import main.java.c0.util.Pos;

import java.util.*;

import static main.java.c0.analyser.IdentType.strToIdentType;
import static main.java.c0.instruction.Instruction.createInstruction;

public final class Analyser {

    String startFunc = "_start";

    Tokenizer tokenizer;

    private int nextGlobalOffset = 0;

    // 指令列表
    public O0 binCodeFile;

    /**
     * 符号表
     * <p>
     * 全局变量： <__global: <变量名: 条目>> 存放全局变量 函数中变量： <函数名: <变量名: 条目>> 存放参数和局部变量
     */

    private Map<String, Map<String, SymbolEntry>> symbolTable;

    /**
     * 函数表
     * <p>
     * <函数名:< 返回值类型, 函数名offset,局部变量offset,参数offset>
     */
    private Map<String, FunctionEntry> funcTable;

    Token peekedToken = null;

    HashMap<TokenType, Integer> precedenceMap = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put(TokenType.MUL, 20);
            put(TokenType.DIV, 20);
            put(TokenType.PLUS, 10);
            put(TokenType.MINUS, 10);
            put(TokenType.EQ, 2);
            put(TokenType.NEQ, 2);
            put(TokenType.LT, 2);
            put(TokenType.GT, 2);
            put(TokenType.LE, 2);
            put(TokenType.GE, 2);
            put(TokenType.SHARP, 0);
        }
    };

    // -----------------------------------------------------------------------------------------------------

    public Analyser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        initTables();
        this.binCodeFile = new O0();
    }

    private void initTables() {
        this.funcTable = new HashMap<>(); // 创建函数表
        this.funcTable.put(startFunc, new FunctionEntry(0)); // 函数表添加startFunc
        this.symbolTable = new HashMap<>(); // 创建符号表
        this.symbolTable.put(startFunc, new HashMap<>()); // 符号表添加<startFunc : <>>
        // 符号表添加<startFunc: <startFunc :>>
        int symbolNo = this.funcTable.get(startFunc).getNextLocOffset();
        SymbolEntry entry = new SymbolEntry(SymbolType.FUNCNAME, IdentType.VOID, symbolNo);
        this.symbolTable.get(startFunc).put(startFunc, entry);
    }

    // -----------------------------------------------------------------------------------------------------

    private int getNextGlobalOffset() {
        return this.nextGlobalOffset++;
    }

    // -----------------------------------------------------------------------------------------------------

    private Token peek() throws TokenizeError {
        if (peekedToken == null)
            peekedToken = tokenizer.nextToken();
        return peekedToken;
    }

    private Token next() throws TokenizeError {
        if (peekedToken != null) {
            var token = peekedToken;
            peekedToken = null;
            return token;
        }
        return tokenizer.nextToken();
    }

    private boolean check(TokenType tt) throws TokenizeError {
        var token = peek();
        return token.getTokenType() == tt;
    }

    private Token nextIf(TokenType tt) throws TokenizeError {
        var token = peek();
        if (token.getTokenType() == tt)
            return next();
        return null;
    }

    private Token expect(TokenType tt) throws CompileError {
        var token = peek();
        if (token.getTokenType() == tt)
            return next();
        throw new ExpectedTokenError(tt, token);
    }

    // 符号表
    // -----------------------------------------------------------------------------------------------------

    private SymbolEntry getSymbolEntry(String funcName, String symbolName) throws AnalyzeError {
        var symbolMap = this.symbolTable.get(funcName);
        if (symbolMap == null)  // 函数不存在
            return null;
        if (!symbolMap.containsKey(symbolName))
            return null;
        return symbolMap.get(symbolName);

    }

    private FunctionEntry getFuncEntry(String funcName, Pos curPos) throws AnalyzeError {
        var symbolEntry = this.symbolTable.get(startFunc).get(funcName);
        if (symbolEntry == null) // 函数不存在
            throw new AnalyzeError(ErrorCode.FunctionNotExist, curPos);
        return this.funcTable.get(funcName);
    }

    private int addFunc(String funcName, IdentType retType, Pos curPos) throws AnalyzeError {
        // funcName不能重复，也不能和全局变量重复（从全局变量中找
        // 添加到函数表
        int funcNo = addGlobalSymbol(funcName, SymbolType.FUNCNAME, retType, curPos);
        this.symbolTable.put(funcName, new HashMap<>());
        // TODO 最后输出_start时，参数个数、局部变量个数、返回值全为0
        // 添加到符号表
        FunctionEntry functionEntry = new FunctionEntry(funcNo);
        this.funcTable.put(funcName, functionEntry);
        return funcNo;
    }

    private int getFuncNo(String funcName) throws AnalyzeError {
        return getSymbolOffset(startFunc, funcName);
    }

    private int addGlobalSymbol(String symbolName, SymbolType symbolType, IdentType valueType, Pos curPos)
            throws AnalyzeError {
        var symbolMap = this.symbolTable.get(startFunc); // 获取
        if (symbolMap.containsKey(symbolName) && symbolMap.get(symbolName).getSymbolType() == symbolType) // 符号存在
            throw new AnalyzeError(ErrorCode.DuplicateDeclaration, curPos); // 报错重复声明
        if (symbolType == SymbolType.CONSTPARAM || symbolType == SymbolType.LETPARAM)// startFunc无参数
            throw new AnalyzeError(ErrorCode.UnsupportedSymbol, curPos);
        int offset = this.funcTable.get(startFunc).getNextLocOffset(); // 函数表startFunc局部变量偏移+1
        symbolMap.put(symbolName, new SymbolEntry(symbolType, valueType, offset)); // 符号表中添加global
        switch (symbolType) {
            case CONST:
                if (valueType == IdentType.STRING)
                    this.binCodeFile.addGlobal(new GlobalDef((byte) 1, symbolName));
                else
                    this.binCodeFile.addGlobal(new GlobalDef((byte) 1)); // o0结构体中添加global
                break;
            case LETINIT:
            case LETNOTINIT:
                this.binCodeFile.addGlobal(new GlobalDef((byte) 0)); // o0结构体中添加global
                break;
            case FUNCNAME:
                this.binCodeFile.addGlobal(new GlobalDef((byte) 0, symbolName)); // o0结构体中添加global
                break;
        }
        return offset;
    }

    private int addLocalSymbol(String funcName, int funcNo, String symbolName, SymbolType symbolType, IdentType valueType,
                               Pos curPos) throws AnalyzeError {
        if (symbolType == SymbolType.FUNCNAME) // 局部变量不存函数名
            throw new AnalyzeError(ErrorCode.UnsupportedSymbol, curPos);
        var symbolMap = this.symbolTable.get(funcName);
        if (symbolMap == null) // 若函数不存在，报错
            throw new AnalyzeError(ErrorCode.FunctionNotExist, curPos);
        if (symbolMap.containsKey(symbolName))// 符号存在，则报错重复声明
            throw new AnalyzeError(ErrorCode.DuplicateDeclaration, curPos);
        // 符号表中funcName添加局部变量symbolName,函数表funcName局部变量偏移+1
        int offset;
        if (symbolType == SymbolType.CONSTPARAM || symbolType == SymbolType.LETPARAM) {
            offset = getFuncEntry(funcName, curPos).getNextParamOffset(); // 函数表中paramOffset+1
            this.binCodeFile.addFuncParamNum(funcNo); // o0结构体中函数param_slots++
        } else {
            offset = getFuncEntry(funcName, curPos).getNextLocOffset();// 函数表中locOffset+1
            this.binCodeFile.addFuncLocNum(funcNo); // o0结构体中函数loc_slots++
        }
        symbolMap.put(symbolName, new SymbolEntry(symbolType, valueType, offset));
        return offset;
    }

    // private void addParam(String funcName,String symbolName,IdentType symbolType)

    private void setSymbolSymbolType(String funcName, String symbolName, SymbolType type)
            throws AnalyzeError {
        getSymbolEntry(funcName, symbolName).setSymbolType(type);
    }

    private SymbolType getSymbolSymbolType(String funcName, String symbolName) throws AnalyzeError {
        return getSymbolEntry(funcName, symbolName).getSymbolType();
    }

    private void setSymbolValueType(String funcName, String symbolName, IdentType type, Pos pos) throws AnalyzeError {
        var entry = getSymbolEntry(funcName, symbolName);
        if (entry == null)
            throw new AnalyzeError(ErrorCode.NotDeclared, pos);
        entry.setValueType(type);
    }

    private ArrayList<IdentType> getParamsSymbolType(String funcName) {
        ArrayList<IdentType> paramSymbolTypes = new ArrayList<>();
        var map = this.symbolTable.get(funcName);
        for (Map.Entry<String, SymbolEntry> entry : map.entrySet()) {
            SymbolEntry symbolEntry = entry.getValue();
            SymbolType symbolType = symbolEntry.getSymbolType();
            IdentType valueType = symbolEntry.getValueType();
            if (symbolType == SymbolType.CONSTPARAM || symbolType == SymbolType.LETPARAM) {
                paramSymbolTypes.add(valueType);
            }
        }
        return paramSymbolTypes;
    }

    private IdentType getSymbolValueType(String funcName, String symbolName) throws AnalyzeError {
        var entry = getSymbolEntry(funcName, symbolName);
        if (entry == null)
            return null;
        return entry.getValueType();
    }

    private IdentType getRetType(String funcName) throws AnalyzeError {
        return getSymbolValueType(startFunc, funcName);
    }

    private void setRetType(String funcName, IdentType type, Pos pos) throws AnalyzeError {
        setSymbolValueType(startFunc, funcName, type, pos);
    }

    private int getSymbolOffset(String funcName, String symbolName) throws AnalyzeError {
        var entry = getSymbolEntry(funcName, symbolName);
        if (entry == null)
            return -1;
        return entry.getOffset();
    }

    private boolean isConstSymbol(String funcName, String symbolName) throws AnalyzeError {
        return getSymbolSymbolType(funcName, symbolName) == SymbolType.CONST;
    }

    // for debugging
    public String getSymbolTable() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Map<String, SymbolEntry>> tableEntry : symbolTable.entrySet()) {
            sb.append(tableEntry.getKey());
            var temp = tableEntry.getValue();
            if (temp.size() == 0)
                sb.append(": {}\n");
            else {
                sb.append(": {\n");
                for (Map.Entry<String, SymbolEntry> tempEntry : temp.entrySet()) {
                    sb.append("\t");
                    sb.append(tempEntry.getKey());
                    sb.append(": ");
                    sb.append(tempEntry.getValue());
                    sb.append("\n");
                }
                sb.append("}\n");
            }
        }
        return sb.toString();
    }

    private void initializeSymbol(String funcName, String symbolName, Pos curPos) throws AnalyzeError {
        SymbolType symbolType = getSymbolSymbolType(funcName, symbolName);
        if (symbolType != SymbolType.LETNOTINIT)
            throw new AnalyzeError(ErrorCode.UnsupportedSymbol, curPos);
        setSymbolSymbolType(funcName, symbolName, SymbolType.LETINIT);
    }

    private boolean isPrecedent(TokenType type1, TokenType type2) {
        int token1Precedence = precedenceMap.get(type1);
        int token2Precedence = precedenceMap.get(type2);
        return token1Precedence > token2Precedence;
    }

    private boolean isOp(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
            case MUL:
            case DIV:
            case EQ:
            case NEQ:
            case LT:
            case GT:
            case LE:
            case GE:
            case AS:
                return true;
            default:
                return false;
        }
    }

    // 主程序
    // -----------------------------------------------------------------------------------------------------

    public O0 analyse() throws CompileError {
        analyseProgram();
        return this.binCodeFile;
    }

    private void analyseProgram() throws CompileError { // ( decl_stmt | function ) *
        while (!check(TokenType.EOF)) {
            boolean temp = check(TokenType.CONST);
            if (check(TokenType.LET)) {
                analyseLetDeclStmt(startFunc, 0);
            } else if (check(TokenType.CONST)) {
                analyseConstDeclStmt(startFunc, 0);
            } else if (check(TokenType.FN)) {
                analyseFunc();
            } else
                throw new AnalyzeError(ErrorCode.NotADeclarationOrFunction, peek().getStartPos());
        }
    }

    // expr
    // -----------------------------------------------------------------------------------------------------

    private OperandItem analysePrimaryExpr(String funcName, int funcNo) throws CompileError {
        /* primary_expr
         * literal_expr  ->  UINT_LITERAL | DOUBLE_LITERAL | STRING_LITERAL | CHAR_LITERAL
         * call_expr     ->  IDENT '(' call_param_list? ')'
         * assign_expr   ->  IDENT '=' expr
         * ident_expr    ->  IDENT
         * negate_expr   ->  '-' expr
         * group_expr    ->  '(' expr ')'
         */
        Instruction instruction;
        if (check(TokenType.UINT_LITERAL)) { // UINT_LITERAL
            Token token = next();
            instruction = createInstruction(Operation.PUSH, (int) token.getValue());
            this.binCodeFile.addInstruction(funcNo, instruction);
            return new OperandItem(IdentType.INT, token.getStartPos());
        } else if (check(TokenType.CHAR_LITERAL)) { // CHAR_LITERAL
            Token token = next();
            instruction = createInstruction(Operation.PUSH, (char) token.getValue());
            this.binCodeFile.addInstruction(funcNo, instruction);
            return new OperandItem(IdentType.CHAR, token.getStartPos());
        } else if (check(TokenType.DOUBLE_LITERAL)) { // DOUBLE_LITERAL
            Token token = next();
            long num = Double.doubleToRawLongBits((double) token.getValue());
            instruction = createInstruction(Operation.PUSH, num);
            this.binCodeFile.addInstruction(funcNo, instruction);
            return new OperandItem(IdentType.DOUBLE, token.getStartPos());
        } else if (check(TokenType.STRING_LITERAL)) { // STRING_LITERAL
            Token token = next();
            Pos pos = token.getStartPos();
            int num = addGlobalSymbol(token.getValueString(), SymbolType.CONST, IdentType.STRING, pos);
            instruction = createInstruction(Operation.PUSH, num);
            this.binCodeFile.addInstruction(funcNo, instruction);
            return new OperandItem(IdentType.STRING, pos);
        } else if (check(TokenType.MINUS)) { // negate_expr -> '-' expr
            return analyseNegateExpr(funcName, funcNo);
        } else if (nextIf(TokenType.L_PAREN) != null) { // group_expr -> '(' expr ')'
            OperandItem operandItem = analyseExprOPG(funcName, funcNo);
            expect(TokenType.R_PAREN);
            return operandItem;
        } else if (check(TokenType.IDENT)) { // assign_expr | call_expr | ident_expr
            Token ident = next();
            String symbolName = ident.getValueString();
            Pos pos = ident.getStartPos();
            IdentType valueType;
            SymbolType symbolType;
            int num;
            boolean isGlobal = false;
            if (check(TokenType.L_PAREN)) { // call_expr -> IDENT '(' call_param_list? ')'
                return analyseCallExpr(funcName, funcNo, ident);
            } else if (check(TokenType.ASSIGN)) { // assign_expr -> IDENT '=' expr
                SymbolEntry entry = getSymbolEntry(funcName, symbolName);// 调用局部变量、调用参数
                if (entry == null) { // 不存在
                    entry = getSymbolEntry(startFunc, symbolName); // 调用全局变量
                    isGlobal = true;
                    if (entry == null)
                        throw new AnalyzeError(ErrorCode.NotDeclared, pos);
                }
                valueType = entry.getValueType();
                symbolType = entry.getSymbolType();
                num = entry.getOffset();
                if (symbolType == SymbolType.CONST) {
                    throw new AnalyzeError(ErrorCode.ConstantsCannotBeModified, pos);
                } else if (isGlobal) { // 全局变量、全局常量
                    instruction = createInstruction(Operation.GLOBA, num);
                    this.binCodeFile.addInstruction(funcNo, instruction);
                } else if (symbolType == SymbolType.LETPARAM || symbolType == SymbolType.CONSTPARAM) { // 参数
                    instruction = createInstruction(Operation.ARGA, num);
                    this.binCodeFile.addInstruction(funcNo, instruction);
                } else if (symbolType != SymbolType.FUNCNAME) { // 局部变量、局部常量
                    instruction = createInstruction(Operation.LOCA, num);
                    this.binCodeFile.addInstruction(funcNo, instruction);
                }
                next(); //'='
                entry.setSymbolType(SymbolType.LETINIT);
                OperandItem expr = analyseExprOPG(funcName, funcNo);
                if (expr.getType() != valueType)
                    throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                instruction = createInstruction(Operation.STORE_64);
                this.binCodeFile.addInstruction(funcNo, instruction);
                return new OperandItem(IdentType.VOID, pos);
            } else { // ident_expr -> IDENT
                SymbolEntry entry = getSymbolEntry(funcName, symbolName);// 调用局部变量、调用参数
                if (entry == null) { // 不存在
                    entry = getSymbolEntry(startFunc, symbolName); // 调用全局变量
                    isGlobal = true;
                    if (entry == null)
                        throw new AnalyzeError(ErrorCode.NotDeclared, pos);
                }
                valueType = entry.getValueType();
                symbolType = entry.getSymbolType();
                num = entry.getOffset();
                if (isGlobal) { // 全局变量、全局常量
                    this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.GLOBA, num));
                } else if (symbolType == SymbolType.LETPARAM || symbolType == SymbolType.CONSTPARAM) { // 参数
                    this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.ARGA, num));
                } else if (symbolType != SymbolType.FUNCNAME) { // 局部变量、局部常量
                    this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.LOCA, num));
                }
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.LOAD_64));
                return new OperandItem(valueType, pos);
            }
        } else {
            throw new ExpectedTokenError(Arrays.asList(TokenType.UINT_LITERAL, TokenType.DOUBLE_LITERAL, TokenType.STRING_LITERAL, TokenType.CHAR_LITERAL, TokenType.IDENT, TokenType.MINUS), peek());
        }
    }

    private OperandItem analyseExprOPG(String funcName, int funcNo) throws CompileError {
        Instruction instruction;
        Stack<TokenType> operator = new Stack<>();
        Stack<OperandItem> operand = new Stack<>();
        operator.push(TokenType.SHARP);
        operand.push(analysePrimaryExpr(funcName, funcNo));
        /*
         * operator_expr ->  expr binary_operator expr
         * as_expr -> expr 'as' ty
         */
        while (!operator.isEmpty()) {
            TokenType topOperator = operator.peek(); //
            TokenType nextOperator = peek().getTokenType(); //

            if (!isOp(topOperator) && !isOp(nextOperator)) { // 若两个符号都不是运算符
                break;
            } else if (!isOp(topOperator) || isOp(nextOperator) && isPrecedent(nextOperator, topOperator)) { // 移进
                operator.push(nextOperator); // 运算符入栈
                next();
                if (nextOperator == TokenType.AS) { // as_expr -> expr 'as' ty
                    IdentType type = strToIdentType(expect(TokenType.TY).getValueString());
                    operand.push(new OperandItem(type, null));
                } else {
                    operand.push(analysePrimaryExpr(funcName, funcNo));
                }
            } else if (!isOp(nextOperator) || isPrecedent(topOperator, nextOperator)) { // 归约
                if (operand.size() > 1) {
                    OperandItem operand_r = operand.pop();
                    OperandItem operand_l = operand.peek();
                    IdentType type_l = operand_l.getType();
                    IdentType type_r = operand_r.getType();
                    TokenType op = operator.pop();
                    Pos pos = operand_l.getPos();
                    if (op == TokenType.AS) { // as_expr -> expr 'as' ty
                        if (type_l == IdentType.VOID || type_r == IdentType.VOID)
                            throw new AnalyzeError(ErrorCode.UnsupportedTypeCasting, pos);
                        if (type_l == IdentType.INT && type_r == IdentType.DOUBLE) {
                            operand_l.setType(type_r);
                            instruction = createInstruction(Operation.ITOF);
                            this.binCodeFile.addInstruction(funcNo, instruction);
                        } else if (type_l == IdentType.DOUBLE && type_r == IdentType.INT) {
                            operand_l.setType(type_r);
                            instruction = createInstruction(Operation.FTOI);
                            this.binCodeFile.addInstruction(funcNo, instruction);
                        }
                    } else {
                        if (type_l != type_r)
                            throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                        switch (op) {
                            case MUL:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.MUL_I);
                                } else {
                                    instruction = createInstruction(Operation.MUL_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                break;
                            case DIV:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.DIV_I);
                                } else {
                                    instruction = createInstruction(Operation.DIV_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                break;
                            case PLUS:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.ADD_I);
                                } else {
                                    instruction = createInstruction(Operation.ADD_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                break;
                            case MINUS:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.SUB_I);
                                } else {
                                    instruction = createInstruction(Operation.SUB_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                break;
                            case EQ:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.CMP_I);
                                } else {
                                    instruction = createInstruction(Operation.CMP_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.NOT));
                                operand_l.setType(IdentType.BOOL);
                                break;
                            case NEQ:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.CMP_I);
                                } else {
                                    instruction = createInstruction(Operation.CMP_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                operand_l.setType(IdentType.BOOL);
                                break;
                            case LT:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.CMP_I);
                                } else {
                                    instruction = createInstruction(Operation.CMP_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SET_LT));
                                operand_l.setType(IdentType.BOOL);
                                break;
                            case GT:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.CMP_I);
                                } else {
                                    instruction = createInstruction(Operation.CMP_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SET_GT));
                                operand_l.setType(IdentType.BOOL);
                                break;
                            case LE:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.CMP_I);
                                } else {
                                    instruction = createInstruction(Operation.CMP_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SET_GT));
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.NOT));
                                operand_l.setType(IdentType.BOOL);
                                break;
                            case GE:
                                if (type_l == IdentType.INT) {
                                    instruction = createInstruction(Operation.CMP_I);
                                } else {
                                    instruction = createInstruction(Operation.CMP_F);
                                }
                                this.binCodeFile.addInstruction(funcNo, instruction);
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SET_LT));
                                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.NOT));
                                operand_l.setType(IdentType.BOOL);
                                break;
                        }
                    }
                }
            }
        }
        return operand.pop();
    }

    private OperandItem analyseCallExpr(String funcName, int funcNo, Token ident) throws CompileError {
        // call_expr -> IDENT '(' call_param_list? ')'
        String callName = ident.getValueString();
        Pos pos = expect(TokenType.L_PAREN).getEndPos();
        IdentType paramType, retType;
        switch (callName) {
            case "getint":
                expect(TokenType.R_PAREN);
                retType = IdentType.INT;
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SCAN_I));
                return new OperandItem(retType, pos);
            case "getchar":
                expect(TokenType.R_PAREN);
                retType = IdentType.INT;
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SCAN_C));
                return new OperandItem(retType, pos);
            case "getdouble":
                expect(TokenType.R_PAREN);
                retType = IdentType.DOUBLE;
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.SCAN_F));
                return new OperandItem(retType, pos);
            case "putln":
                expect(TokenType.R_PAREN);
                retType = IdentType.VOID;
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.PRINTLN));
                return new OperandItem(retType, pos);
            case "putint":
                retType = IdentType.VOID;
                paramType = analyseExprOPG(funcName, funcNo).getType();
                if (paramType != IdentType.INT)
                    throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                expect(TokenType.R_PAREN);
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.PRINT_I));
                return new OperandItem(retType, pos);
            case "putchar":
                retType = IdentType.VOID;
                paramType = analyseExprOPG(funcName, funcNo).getType();
                if (paramType != IdentType.INT && paramType != IdentType.CHAR)
                    throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                expect(TokenType.R_PAREN);
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.PRINT_C));
                return new OperandItem(retType, pos);
            case "putstr":
                retType = IdentType.VOID;
                paramType = analyseExprOPG(funcName, funcNo).getType();
                if (paramType != IdentType.INT && paramType != IdentType.STRING)
                    throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                expect(TokenType.R_PAREN);
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.PRINT_S));
                return new OperandItem(retType, pos);
            case "putdouble":
                retType = IdentType.VOID;
                paramType = analyseExprOPG(funcName, funcNo).getType();
                if (paramType != IdentType.DOUBLE)
                    throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                expect(TokenType.R_PAREN);
                // TODO
                return new OperandItem(retType, pos);
            default: {
                retType = getRetType(callName);
                // call_param_list -> expr (',' expr)*
                // ε | expr (',' expr)*
                // 每个参数的类型匹配
                ArrayList<IdentType> paramsSymbolTypes = getParamsSymbolType(callName);
                int count = 0;
                while (!check(TokenType.R_PAREN)) {
                    paramType = analyseExprOPG(funcName, funcNo).getType();
                    if (paramType != paramsSymbolTypes.get(count))
                        throw new AnalyzeError(ErrorCode.TypeMismatch, pos);
                    count++;
                    if (nextIf(TokenType.COMMA) == null)
                        break;
                }
                if (count != paramsSymbolTypes.size())
                    throw new AnalyzeError(ErrorCode.TooManyOrTooFewArguments, pos);
                expect(TokenType.R_PAREN);
                // TODO
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.CALLNAME, funcNo));
                return new OperandItem(retType, pos);
            }
        }
    }

    private OperandItem analyseNegateExpr(String funcName, int funcNo) throws CompileError { // negate_expr -> '-' expr
        Pos pos = expect(TokenType.MINUS).getStartPos(); // '-'
        IdentType type = analyseExprOPG(funcName, funcNo).getType(); // expr
        Instruction instruction;
        if (type == IdentType.INT) {
            instruction = createInstruction(Operation.NEG_I);
        } else if (type == IdentType.DOUBLE) {
            instruction = createInstruction(Operation.NEG_F);
        } else {
            throw new AnalyzeError(ErrorCode.UnsupportedType, pos);
        }
        this.binCodeFile.addInstruction(funcNo, instruction);
        return new OperandItem(type, pos);
    }

    private IdentType analyseGroupExpr(String funcName, int funcNo) throws CompileError { // group_expr -> '(' expr ')'
        expect(TokenType.L_PAREN);// '('
        IdentType type = analyseExprOPG(funcName, funcNo).getType(); // expr
        expect(TokenType.R_PAREN); // ')'
        return type;
    }

    // statements
    // -----------------------------------------------------------------------------------------------------

    private void analyseStmt(String funcName, int funcNo) throws CompileError {
        if (check(TokenType.LET)) {// let_decl_stmt
            analyseLetDeclStmt(funcName, funcNo);
        } else if (check(TokenType.CONST)) { // const_decl_stmt
            analyseConstDeclStmt(funcName, funcNo);
        } else if (check(TokenType.IF)) {
            // if_stmt
            analyseIfStmt(funcName, funcNo);
        } else if (check(TokenType.WHILE)) {// while_stmt
            analyseWhileStmt(funcName, funcNo);
        } else if (check(TokenType.RETURN)) { // return_stmt
            analyseReturnStmt(funcName, funcNo);
        } else if (check(TokenType.L_BRACE)) { // block_stmt
            // block_stmt
            analyseBlockStmt(funcName, funcNo);
        } else if (check(TokenType.SEMICOLON)) { // empty_stmt
            next();// ';'
        } else { // expr_stmt -> expr ';'
            analyseExprOPG(funcName, funcNo); // expr
            // ';'
            expect(TokenType.SEMICOLON);
        }
    }

    private void analyseLetDeclStmt(String funcName, int funcNo) throws CompileError {
        Token ident;
        Pos curPos;
        String symbolName;
        IdentType type;
        Instruction instruction;
        boolean isGlobal = funcName.equals(startFunc);
        int offset;
        // 'let' IDENT ':' ty ('=' expr)? ';'
        expect(TokenType.LET);
        ident = expect(TokenType.IDENT);
        symbolName = ident.getValueString();
        curPos = ident.getStartPos();
        expect(TokenType.COLON);
        type = strToIdentType(expect(TokenType.TY).getValueString());
        // 类型不能为void
        if (type == IdentType.VOID)
            throw new AnalyzeError(ErrorCode.CannotBeDefinedAsVoid, ident.getEndPos());
        if (nextIf(TokenType.ASSIGN) != null) { // 初始化, '=' expr
            if (isGlobal) {
                offset = addGlobalSymbol(symbolName, SymbolType.LETINIT, type, curPos); // 添加到符号表
                instruction = createInstruction(Operation.GLOBA, offset);
            } else {
                offset = addLocalSymbol(funcName, funcNo, symbolName, SymbolType.LETINIT, type, curPos); // 添加到符号表
                instruction = createInstruction(Operation.LOCA, offset);
            }
            this.binCodeFile.addInstruction(funcNo, instruction); // 添加指令
            IdentType valueType = analyseExprOPG(funcName, funcNo).getType();
            // 类型一致
            if (valueType != type)
                throw new AnalyzeError(ErrorCode.TypeMismatch, curPos);
            this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.STORE_64));
        } else { // 未初始化
            if (isGlobal) {
                addGlobalSymbol(symbolName, SymbolType.LETNOTINIT, type, curPos);
            } else {
                addLocalSymbol(funcName, funcNo, symbolName, SymbolType.LETNOTINIT, type, curPos);
            }
        }
        expect(TokenType.SEMICOLON);
    }

    private void analyseConstDeclStmt(String funcName, int funcNo) throws CompileError {
        Token ident;
        Pos curPos;
        IdentType type;
        String symbolName;
        Instruction instruction;
        boolean isGlobal = funcName.equals(startFunc);
        int offset;
        // 'const' IDENT ':' ty '=' expr ';'
        expect(TokenType.CONST);
        ident = expect(TokenType.IDENT);
        symbolName = ident.getValueString();
        curPos = ident.getStartPos();
        expect(TokenType.COLON);
        type = strToIdentType(expect(TokenType.TY).getValueString());
        // 类型不能为void
        if (type == IdentType.VOID)
            throw new AnalyzeError(ErrorCode.CannotBeDefinedAsVoid, ident.getEndPos());
        expect(TokenType.ASSIGN);
        if (isGlobal) {
            offset = addGlobalSymbol(symbolName, SymbolType.CONST, type, curPos); // 添加到符号表
            instruction = createInstruction(Operation.GLOBA, offset);
        } else {
            offset = addLocalSymbol(funcName, funcNo, symbolName, SymbolType.CONST, type, curPos); // 添加到符号表
            instruction = createInstruction(Operation.LOCA, offset);
        }
        this.binCodeFile.addInstruction(funcNo, instruction);
        IdentType valueType = analyseExprOPG(funcName, funcNo).getType();
        if (type != valueType)
            throw new AnalyzeError(ErrorCode.TypeMismatch, curPos);
        this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.STORE_64));
        expect(TokenType.SEMICOLON);
    }

    private void analyseIfStmt(String funcName, int funcNo) throws CompileError {
        // TODO  check return
        // 'if' expr block_stmt ('else' (block_stmt | if_stmt))?
        expect(TokenType.IF);
        OperandItem operandItem = analyseExprOPG(funcName, funcNo);
        IdentType type = operandItem.getType();
        Pos pos = operandItem.getPos();
        if (type == IdentType.VOID)
            throw new AnalyzeError(ErrorCode.UnsupportedType, pos);
        int br_false = this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.BR_FALSE, 0));
        analyseBlockStmt(funcName, funcNo);
        int br_exit = this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.BR, 0));
        this.binCodeFile.getInstruction(funcNo, br_false).setParam(br_exit - br_false);
        // ('else' (block_stmt | if_stmt))?
        if (nextIf(TokenType.ELSE) != null) {
            if (check(TokenType.L_BRACE)) { // block_stmt
                analyseBlockStmt(funcName, funcNo);
                int offset = this.binCodeFile.getInsNum(funcNo) - br_exit - 1;
                this.binCodeFile.getInstruction(funcNo, br_exit).setParam(offset);
                System.out.println(offset);
            } else if (check(TokenType.IF)) { // if_stmt
                analyseIfStmt(funcName, funcNo);
                int offset = this.binCodeFile.getInsNum(funcNo) - br_exit - 1;
                System.out.println(br_exit);
                System.out.println(offset);
                this.binCodeFile.getInstruction(funcNo, br_exit).setParam(offset);
            }
        }
    }

    private void analyseWhileStmt(String funcName, int funcNo) throws CompileError {
        // 'while' expr block_stmt
        expect(TokenType.WHILE);
        int startOffset = this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.BR, 0));
        analyseExprOPG(funcName, funcNo); // expr
        int br_false = this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.BR_FALSE, 0));
        analyseBlockStmt(funcName, funcNo); // block_stmt
        int curOffset = this.binCodeFile.getInsNum(funcNo);
        int br_loop = this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.BR, startOffset - curOffset));
        this.binCodeFile.getInstruction(funcNo, br_false).setParam(br_loop - br_false);
    }

    private void analyseReturnStmt(String funcName, int funcNo) throws CompileError {
        Pos curPos = expect(TokenType.RETURN).getStartPos();
        IdentType funcRetType = getRetType(funcName); // 从符号表中获取函数返回值类型
        // 'return' expr? ';'
        if (nextIf(TokenType.SEMICOLON) == null) {
            if (funcRetType == IdentType.VOID) // 如果函数声明的返回值是 void,return 语句不能携带返回值
                throw new AnalyzeError(ErrorCode.ReturnValueTypeMismatched, peek().getStartPos());
            this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.ARGA, 0));
            IdentType valueType = analyseExprOPG(funcName, funcNo).getType();
            expect(TokenType.SEMICOLON);
            if (valueType != funcRetType)
                throw new AnalyzeError(ErrorCode.ReturnValueTypeMismatched, peek().getStartPos());
            this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.STORE_64));
        }
        this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.RET));
    }

    private void analyseBlockStmt(String funcName, int funcNo) throws CompileError { // block_stmt -> '{' stmt* '}'
        boolean hasRet = false;
        expect(TokenType.L_BRACE);
        while (nextIf(TokenType.R_BRACE) == null) {
            analyseStmt(funcName, funcNo);
        }
    }

    // function
    // -----------------------------------------------------------------------------------------------------

    private void analyseFunc() throws CompileError {
        Token token;
        String funcName;
        IdentType retType;
        boolean isMain;
        Pos curPos;
        int funcNo;
        // 'fn' IDENT '(' function_param_list? ')' '->' ty block_stmt
        expect(TokenType.FN);
        token = expect(TokenType.IDENT);
        curPos = token.getStartPos();
        funcName = token.getValueString();
        isMain = funcName.equals("main");
        funcNo = addFunc(funcName, IdentType.VOID, curPos); // 添加函数到符号表和函数表
        if (funcNo == -1)
            throw new AnalyzeError(ErrorCode.NotDeclared, curPos);
        this.binCodeFile.initNewFunc(funcNo, funcName);
        if (isMain) {
            this.binCodeFile.addInstruction(0, createInstruction(Operation.STACKALLOC, 0));
            this.binCodeFile.addInstruction(0, createInstruction(Operation.CALLNAME, funcNo));
        }
        expect(TokenType.L_PAREN);
        if (nextIf(TokenType.R_PAREN) == null) { // function_param_list?
            if (isMain)
                throw new AnalyzeError(ErrorCode.MainFunctionHaveNoParams, curPos);
            analyseFuncParamList(funcName, funcNo);
            expect(TokenType.R_PAREN);
        }
        expect(TokenType.ARROW);
        retType = strToIdentType(expect(TokenType.TY).getValueString());
        if (retType != IdentType.VOID) {
            // if (isMain)
            //    throw new AnalyzeError(ErrorCode.MainFunctionOnlyReturnVoid, curPos);
            this.binCodeFile.setFuncRet(funcNo);
        }
        setRetType(funcName, retType, curPos); // 设置函数返回值类型
        analyseBlockStmt(funcName, funcNo);

        Instruction instruction = this.binCodeFile.getInstruction(funcNo, this.binCodeFile.getInsNum(funcNo) - 1);
        if (instruction.getOpt() != Operation.RET) {
            if (retType != IdentType.VOID)
                throw new AnalyzeError(ErrorCode.NeedReturnStatement, peek().getStartPos());
            else
                this.binCodeFile.addInstruction(funcNo, createInstruction(Operation.RET));
        }
    }

    private void analyseFuncParamList(String funcName, int funcNo) throws CompileError {
        SymbolType symbolType = SymbolType.LETPARAM;
        String paramName;
        Token ident;
        IdentType valueType;
        // function_param_list -> function_param (',' function_param)*
        do {
            // function_param -> 'const'? IDENT ':' ty
            if (nextIf(TokenType.CONST) != null)
                symbolType = SymbolType.CONSTPARAM;
            ident = expect(TokenType.IDENT);
            paramName = ident.getValueString();
            Pos curPos = ident.getStartPos();
            expect(TokenType.COLON);
            valueType = strToIdentType(expect(TokenType.TY).getValueString());
            addLocalSymbol(funcName, funcNo, paramName, symbolType, valueType, curPos);
            this.binCodeFile.addFuncParamNum(funcNo);
        } while (nextIf(TokenType.COMMA) != null);
    }
}