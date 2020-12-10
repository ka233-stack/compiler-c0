package main.java.c0.tokenizer;

import main.java.c0.error.ErrorCode;
import main.java.c0.error.TokenizeError;
import main.java.c0.util.Pos;

public class Tokenizer {
    private final StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    public Token nextToken() throws TokenizeError {

        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        // 结尾
        if (it.isEOF())
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());

        char peek = it.peekChar();

        if (Character.isDigit(peek)) {// 数字
            return isUINTOrDOUBLE();
        } else if (Character.isAlphabetic(peek) || peek == '_') {// 标识符、关键字
            return isIdentOrKeyword();
        } else if (peek == '\'') {
            return isCHAR();
        } else if (peek == '\"') {
            return isSTRING();
        } else { // 其他
            return isOperatorOrUnknown();
        }
    }

    private Token isUINTOrDOUBLE() throws TokenizeError {
        char ch;
        boolean isDouble = false;
        StringBuilder sb = new StringBuilder();
        String str;
        Pos startPos = it.currentPos();
        while (Character.isDigit(ch = it.peekChar())) {
            sb.append(it.nextChar());
        }
        if (ch == '.') {
            isDouble = true;
            while (Character.isDigit(ch = it.peekChar()) || ch == '.' || ch == 'e' || ch == 'E' || ch == '+' || ch == '-') {
                sb.append(it.nextChar());
            }
        }
        if (Character.isAlphabetic(ch)) {
            throw new TokenizeError(ErrorCode.InvalidInput, startPos);
        }
        // 解析字符串
        str = sb.toString();
        if (isDouble && str.matches("^\\d[.]\\d+([eE][+-]?\\d+)?$")) { // DOUBLE
            try {
                Double value = Double.parseDouble(str);
                return new Token(TokenType.DOUBLE_LITERAL, value, startPos, startPos);
            } catch (Exception e) {
                throw new TokenizeError(ErrorCode.DoubleOverflow, startPos);
            }
        } else if ((!isDouble) && str.matches("^\\d+$")) {
            try {
                Integer value = Integer.parseInt(str);
                return new Token(TokenType.UINT_LITERAL, value, startPos, startPos);
            } catch (Exception e) {
                throw new TokenizeError(ErrorCode.IntegerOverflow, startPos);
            }
        } else {
            throw new TokenizeError(ErrorCode.InvalidInput, startPos);
        }
    }

    private Token isIdentOrKeyword() {
        StringBuilder sb = new StringBuilder();
        Pos startPos = it.currentPos();
        Pos endPos;
        char peeked;

        sb.append(it.nextChar());

        while (Character.isAlphabetic(peeked = it.peekChar()) || peeked == '_' || Character.isDigit(it.peekChar())) {
            sb.append(it.nextChar());
        }
        endPos = it.currentPos();
        String value = sb.toString();

        // 尝试将存储的字符串解释为关键字
        switch (value) {
            case "fn":
                return new Token(TokenType.FN, value, startPos, endPos);
            case "let":
                return new Token(TokenType.LET, value, startPos, endPos);
            case "const":
                return new Token(TokenType.CONST, value, startPos, endPos);
            case "as":
                return new Token(TokenType.AS, value, startPos, endPos);
            case "while":
                return new Token(TokenType.WHILE, value, startPos, endPos);
            case "if":
                return new Token(TokenType.IF, value, startPos, endPos);
            case "else":
                return new Token(TokenType.ELSE, value, startPos, endPos);
            case "return":
                return new Token(TokenType.RETURN, value, startPos, endPos);
            case "break":
                return new Token(TokenType.BREAK, value, startPos, endPos);
            case "continue":
                return new Token(TokenType.CONTINUE, value, startPos, endPos);
            case "void":
            case "int":
            case "double":
                return new Token(TokenType.TY, value, startPos, endPos);
            default: // IDENT
                return new Token(TokenType.IDENT, value, startPos, endPos);
        }

    }

    private Token isCHAR() throws TokenizeError {
        Pos startPos = it.currentPos();

        // '
        it.nextChar();

        char ch = it.nextChar();

        // 不允许出现单引号'、空白符\r、\n、\t
        if (ch == '\'' || Character.isWhitespace(ch)) {
            throw new TokenizeError(ErrorCode.InvalidInput, startPos);
        } else if (ch == '\\') { // 判断转义
            // '\'
            ch = it.nextChar();
            switch (ch) {
                case '\\':
                case '"':
                case '\'':
                    break;
                case 'n':
                    ch = '\n';
                    break;
                case 't':
                    ch = '\t';
                    break;
                case 'r':
                    ch = '\r';
                    break;
                default:
                    throw new TokenizeError(ErrorCode.InvalidInput, startPos);
            }
        }

        // '
        if (it.nextChar() != '\'')
            throw new TokenizeError(ErrorCode.InvalidInput, startPos);

        return new Token(TokenType.CHAR_LITERAL, ch, startPos, it.currentPos());
    }

    private Token isSTRING() throws TokenizeError {
        StringBuilder sb = new StringBuilder();
        Pos startPos = it.currentPos();
        char peeked;

        // "
        it.nextChar();

        while ((peeked = it.peekChar()) != '\"') {
            // 不允许出现双引号"、空白符\r、\n、\t
            // if (peeked == '\\' || Character.isWhitespace(peeked)){
            if (Character.isWhitespace(peeked)) {
                throw new TokenizeError(ErrorCode.InvalidInput, startPos);
            } else if (peeked == '\\') { // 判断转义
                // '\'
                it.nextChar();
                char nextChar = it.nextChar();
                switch (nextChar) {
                    case '\\':
                    case '"':
                    case '\'':
                        sb.append(nextChar);
                        break;
                    case 'n':
                        sb.append("\n");
                        break;
                    case 't':
                        sb.append("\t");
                        break;
                    case 'r':
                        sb.append("\r");
                        break;
                    default:
                        throw new TokenizeError(ErrorCode.InvalidInput, startPos);
                }
                continue;
            }
            sb.append(it.nextChar());
        }

        // "
        it.nextChar();

        return new

                Token(TokenType.STRING_LITERAL, sb.toString(), startPos, it.

                currentPos());
    }

    private Token isOperatorOrUnknown() throws TokenizeError {
        Pos startPos = it.currentPos();
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.PLUS, "+", startPos, it.currentPos());
            case '-':
                if (it.peekChar() == '>') { // '->'
                    it.nextChar();
                    return new Token(TokenType.ARROW, "->", startPos, it.currentPos());
                } else { // '-'
                    return new Token(TokenType.MINUS, "-", startPos, it.currentPos());
                }
            case '*':
                return new Token(TokenType.MUL, "*", startPos, it.currentPos());
            case '/':
                if (it.peekChar() == '/') { // '//' regex(.*) '\n'
                    do {
                        it.nextChar();
                    } while (it.peekChar() != '\n');
                    // 注释不应当被词法分析输出
                    return nextToken();
                } else // '/'
                    return new Token(TokenType.DIV, "/", startPos, it.currentPos());
            case '=':
                if (it.peekChar() == '=') { // '=='
                    it.nextChar();
                    return new Token(TokenType.EQ, "==", startPos, it.currentPos());
                } else { // '='
                    return new Token(TokenType.ASSIGN, '=', startPos, it.currentPos());
                }
            case '!':
                if (it.peekChar() == '=') { // '!='
                    it.nextChar();
                    return new Token(TokenType.NEQ, "!=", startPos, it.currentPos());
                } else { // error
                    throw new TokenizeError(ErrorCode.InvalidInput, startPos);
                }
            case '<':
                if (it.peekChar() == '=') { // '<='
                    it.nextChar();
                    return new Token(TokenType.LE, "<=", startPos, it.currentPos());
                } else { // '<'
                    return new Token(TokenType.LT, '<', startPos, it.currentPos());
                }
            case '>':
                if (it.peekChar() == '=') { // '>='
                    it.nextChar();
                    return new Token(TokenType.GE, ">=", startPos, it.currentPos());
                } else { // '<'
                    return new Token(TokenType.GT, '>', startPos, it.currentPos());
                }
            case '(':
                return new Token(TokenType.L_PAREN, '(', startPos, it.currentPos());
            case ')':
                return new Token(TokenType.R_PAREN, ')', startPos, it.currentPos());
            case '{':
                return new Token(TokenType.L_BRACE, '{', startPos, it.currentPos());
            case '}':
                return new Token(TokenType.R_BRACE, '}', startPos, it.currentPos());
            case ',':
                return new Token(TokenType.COMMA, ',', startPos, it.currentPos());
            case ':':
                return new Token(TokenType.COLON, ':', startPos, it.currentPos());
            case ';':
                return new Token(TokenType.SEMICOLON, ';', startPos, it.currentPos());
            default:
                throw new TokenizeError(ErrorCode.InvalidInput, startPos);
        }
    }

    /**
     * 跳过之前的所有空白字符
     */
    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
