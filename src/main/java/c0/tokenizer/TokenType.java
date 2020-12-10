package main.java.c0.tokenizer;

public enum TokenType {

    // 标识符
    IDENT,

    // 字面量
    UINT_LITERAL,       // 无符号整型字面值
    DOUBLE_LITERAL,     // 浮点数字面值
    STRING_LITERAL,     // 字符串，'"' (string_regular_char | escape_sequence)* '"'
    CHAR_LITERAL,       // 字符 '\'' (char_regular_char | escape_sequence) '\''

    // 类型
    TY,       // 类型'ty'

    // 关键字
    FN,         // 'fn'
    LET,        // 'let'
    CONST,      // 'const'
    AS,         // 'as'
    WHILE,      // 'while'
    IF,         // 'if'
    ELSE,       // 'else'
    RETURN,     // 'return'
    BREAK,      // 'break'
    CONTINUE,   // 'continue'

    // 符号
    PLUS,       // '+'
    MINUS,      // '-'
    MUL,        // '*'
    DIV,        // '/'

    ASSIGN,     // '='

    EQ,         // '=='
    NEQ,        // '!='
    LT,         // '<'
    GT,         // '>'
    LE,         // '<='
    GE,         // '>='

    L_PAREN,    // '('
    R_PAREN,    // ')'
    L_BRACE,    // '{'
    R_BRACE,    // '}'
    ARROW,      // '->'
    COMMA,      // ','
    COLON,      // ':'
    SEMICOLON,  // ';'

    // 注释
    COMMENT,    // '//' regex(.*) '\n'

    // 其他
    None,       // 空
    SHARP,      // # 仅用于OPG
    EOF;        // 文件尾

    @Override
    public String toString() {
        switch (this) {
            case IDENT:
                return "标识符";
            case UINT_LITERAL:
                return "无符号整数";
            case DOUBLE_LITERAL:
                return "浮点数";
            case STRING_LITERAL:
                return "字符串";
            case CHAR_LITERAL:
                return "字符";
            case TY:
                return "类型";
            case FN:
                return "fn";
            case LET:
                return "let";
            case CONST:
                return "const";
            case AS:
                return "as";
            case WHILE:
                return "while";
            case IF:
                return "if";
            case ELSE:
                return "else";
            case RETURN:
                return "return";
            case BREAK:
                return "break";
            case CONTINUE:
                return "continue";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case ASSIGN:
                return "=";
            case EQ:
                return "==";
            case NEQ:
                return "!=";
            case LT:
                return "<";
            case GT:
                return ">";
            case LE:
                return "<=";
            case GE:
                return ">=";
            case L_PAREN:
                return "(";
            case R_PAREN:
                return ")";
            case L_BRACE:
                return "{";
            case R_BRACE:
                return "}";
            case ARROW:
                return "->";
            case COMMA:
                return ",";
            case COLON:
                return ":";
            case SEMICOLON:
                return ";";
            case COMMENT: // 实际中不应该出现
                return "注释";
            case None:
                return "空";
            case SHARP:
                return "#";
            case EOF:
                return "文件结尾";
            default:
                return "无效Token";
        }
    }
}
