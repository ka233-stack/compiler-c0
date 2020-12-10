# complier-c0



## 一、词法分析

token

```
// # 单词
 
// ## 关键字
FN     -> 'fn'
LET    -> 'let'
CONST  -> 'const'
AS     -> 'as'
WHILE  -> 'while'
IF     -> 'if'
ELSE   -> 'else'
RETURN -> 'return'
BREAK  -> 'break'
CONTINUE -> 'continue'

// ## 字面量
digit -> [0-9]
UINT_LITERAL -> digit+
FLOAT_LITERAL -> digit+ '.' digit+ ([eE] digit+)?

escape_sequence -> '\' [\\"'nrt]
string_regular_char -> [^"\\]
STRING_LITERAL -> '"' (string_regular_char | escape_sequence)* '"'

char_regular_char -> [^'\\]
CHAR_LITERAL -> '\'' (char_regular_char | escape_sequence)* '\''

// ## 标识符
IDENT -> [_a-zA-Z] [_a-zA-Z0-9]*

// ## 符号
PLUS      -> '+'
MINUS     -> '-'
MUL       -> '*'
DIV       -> '/'
ASSIGN    -> '='
EQ        -> '=='
NEQ       -> '!='
LT        -> '<'
GT        -> '>'
LE        -> '<='
GE        -> '>='
L_PAREN   -> '('
R_PAREN   -> ')'
L_BRACE   -> '{'
R_BRACE   -> '}'
ARROW     -> '->'
COMMA     -> ','
COLON     -> ':'
SEMICOLON -> ';'

// ## 注释
COMMENT -> '//' regex(.*) '\n'

// # 表达式
expr -> 
      operator_expr
    | negate_expr
    | assign_expr
    | as_expr
    | call_expr
    | literal_expr
    | ident_expr

binary_operator -> '+' | '-' | '*' | '/' | '==' | '!=' | '<' | '>' | '<=' | '>='
operator_expr -> expr binary_operator expr

negate_expr -> '-' expr

assign_expr -> l_expr '=' expr

as_expr -> expr 'as' ty

call_param_list -> expr (',' expr)*
call_expr -> IDENT '(' call_param_list? ')'

literal_expr -> UINT_LITERAL | FLOAT_LITERAL | STRING_LITERAL | CHAR_LITERAL

ident_expr -> IDENT

// ## 左值表达式
l_expr -> IDENT

// ## 类型
ty -> IDENT

// # 语句
stmt ->
      expr_stmt
    | decl_stmt
    | if_stmt
    | while_stmt
    | break_stmt
    | continue_stmt
    | return_stmt
    | block_stmt
    | empty_stmt

expr_stmt -> expr ';'

let_decl_stmt -> 'let' IDENT ':' ty ('=' expr)? ';'
const_decl_stmt -> 'const' IDENT ':' ty '=' expr ';'
decl_stmt -> let_decl_stmt | const_decl_stmt

if_stmt -> 'if' expr block_stmt ('else' 'if' expr block_stmt)* ('else' block_stmt)?

while_stmt -> 'while' expr block_stmt

break_stmt -> 'break' ';'

continue_stmt -> 'continue' ';'

return_stmt -> 'return' expr? ';'

block_stmt -> '{' stmt* '}'

empty_stmt -> ';'

// # 函数
function_param -> IDENT ':' ty
function_param_list -> function_param (',' function_param)*
function -> 'fn' IDENT '(' function_param_list? ')' '->' ty block_stmt

// # 程序
item -> function | decl_stmt
program -> item*

```





### StringIter

```java
	// 从这里开始其实是一个基于行号的缓冲区的实现
    // 为了简单起见，我们没有单独拿出一个类实现
    // 核心思想和 C 的文件输入输出类似，就是一个 buffer 加一个指针，有三个细节
    // 1.缓冲区包括 \n
    // 2.指针始终指向下一个要读取的 char
    // 3.行号和列号从 0 开始

    // 一次读入全部内容，并且替换所有换行为 \n	    


	// 例
    // | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 偏移
    // |---|---|---|---|---|---|---|---|---|---|
    // | h | a | 1 | 9 | 2 | 6 | 0 | 8 | 1 | \n |（缓冲区第0行）
    // | 7 | 1 | 1 | 4 | 5 | 1 | 4 | （缓冲区第1行）
    // 这里假设指针指向第一行的 \n，那么有
    // currentPos() = (0, 9)
    // nextPos() = (1, 0)
    // previousPos() = (0, 8)
    // nextChar() = '\n' 并且指针移动到 (1, 0)
    // peekChar() = '\n' 并且指针不移动
```



### Questions

- 是否支持代码换行
- 12a如何识别
    - 报错位置startPos？endPos？
- "12121"转int和分开计算那种更快

注释：不返回token（null），因此需要判断token==null

```java
while (true) {
    var token = tokenizer.nextToken();
    // 注释不返回token
    if(token==null)
        continue;
    if (token.getTokenType().equals(TokenType.EOF))
        break;
    tokens.add(token);
    System.out.println(token.toString());
}
```

#### 布尔类型

比较运算符的运行结果是布尔类型。在 c0 中，我们并没有规定布尔类型的实际表示方式。在 navm 虚拟机中，所有非 0 的布尔值都被视为 `true`，而 0 被视为 `false`。

### 测试



|        |  √   |
| :----: | :--: |
| 关键字 |  √   |
| 字面量 |  √   |
| 标识符 |  √   |
| 运算符 |  √   |
|  注释  |  √   |



#### 关键字

问题：ty？

##### 0. void

```

```



##### 0. int

```

```



##### 0. double

```

```



##### 0. char

```
// 正确情况
'\''

// 错误情况
'''
```



##### 0. string

```
// 正确情况
"\""
"\'"
"'"
"\\"
"\n"

// 错误情况
"""
"	"
    
"
"
```



##### 1. `fn`

```
fn
fnf
```



##### 2. `let`

```
let
```



##### 3. `const`

```
const
```



##### 4. `as`

```
as
```



##### 5. `while`

```
while
```



##### 6. `if`

```
if
```



##### 7. `else`

```
else
```



##### 8. `return`

```
return
```



##### 9. `break`

```
break
```



##### 10. `continue`

```
continue
```



#### 字面量

文法

```
digit -> [0-9]
UINT_LITERAL -> digit+

escape_sequence -> '\' [\\"'nrt]
string_regular_char -> [^"\\]
STRING_LITERAL -> '"' (string_regular_char | escape_sequence)* '"'

// 扩展 c0
DOUBLE_LITERAL -> digit+ '.' digit+ ([eE] [+-]? digit+)?

char_regular_char -> [^'\\]
CHAR_LITERAL -> '\'' (char_regular_char | escape_sequence) '\''
```

- 字符串字面量中的字符可以是 ASCII 中除了双引号 `"`、反斜线 `\\`、空白符 `\r` `\n` `\t` 以外的任何字符。转义序列可以是 `\'`、`\"`、`\\`、`\n`、`\t`、`\r`，含义与 C 中的对应序列相同。

基础 c0 有两种字面量，分别是 *无符号整数* 和 *字符串常量*。扩展 c0 增加了 *浮点数常量* 和 *字符常量*。

测试：

##### 1. UINT_LITERAL

```java
0
123
22222222222222222222222222 // IntegerOverflow
```

##### 2. STRING_LITERAL

```

```

##### 3. DOUBLE_LITERAL

```
-1.2e-1
0.0e+0
```

##### 4. CHAR_LITERAL

```
'''
'11'
'	'
'
'
'\'
```



#### 标识符

文法：

```
IDENT -> [_a-zA-Z] [_a-zA-Z0-9]*
```

测试用例

```
_asd
1as
ad1
_
```



#### 运算符

##### 1. `+`

```
+
+1
1+1
sa+b
```

##### 2. `-`

```
-
-1
1-1
sa-b
```

##### 3. `*`

```
*
*1
1*1
sa*b
```

##### 4. `/`

```
/
/1
1/1
sa/b
```

##### 5. `=`

```
=
=1
1=1
sa=b
```

##### 6. `==`

```java
==
=== // == and =
```



##### 7. `!=`

```
!=
!==
```



##### 8. `<`

```
<<
```



##### 9. `>`

```
>
```



##### 10. `<=`

```
<=
=<
```



##### 11. `>=`

```
>=<
```



##### 12. `(`

```
(11)
```



##### 13. `)`

```
(11)
```



##### 14. `{`

```
{as}
```



##### 15. `}`

```
{as}
```



##### 16. `->`

```
-->--
```



##### 17. `,`

```
,
```



##### 18. `:`

```
:
```



##### 19. `;`

```
;
```



#### 注释

```
//asa1121d	\\//
```



## 二、语法分析

### 表达式

```java
expr -> 
      operator_expr // 运算符表达式 
    | negate_expr   // 取反表达式
    | assign_expr   // 赋值表达式
    | as_expr       // 类型转换表达式
    | call_expr     // 函数调用表达式
    | literal_expr  // 字面量表达式
    | ident_expr    // 标识符表达式
    | group_expr    // 括号表达式
```

#### 测试

| 表达式         |      |
| -------------- | ---- |
| 运算符表达式   |      |
| 取反表达式     |      |
| 赋值表达式     |      |
| 类型转换表达式 |      |
| 函数调用表达式 |      |
| 字面量表达式   |      |
| 标识符表达式   |      |
| 括号表达式     |      |

#### Question

字面量表达式是否含有char_literal

#### 运算符表达式

```
binary_operator -> '+' | '-' | '*' | '/' | '==' | '!=' | '<' | '>' | '<=' | '>='
operator_expr -> expr binary_operator expr
```

运算符表达式是中间由一个运算符分隔、两边是子表达式的表达式。r0 一共有 10 种双目运算符。它们分别是：

- 算数运算符 `+` `-` `*` `/`
- 比较运算符 `>` `<` `>=` `<=` `==` `!=`

每个运算符的两侧必须是相同类型的数据。各运算符含义如下：

| 运算符 | 含义                       | 参数类型 | 结果类型   | 结合性 |
| ------ | -------------------------- | -------- | ---------- | ------ |
| `+`    | 将左右两侧相加             | 数值     | 与参数相同 | 左到右 |
| `-`    | 左侧减去右侧               | 数值     | 与参数相同 | 左到右 |
| `*`    | 将左右两侧相乘             | 数值     | 与参数相同 | 左到右 |
| `/`    | 左侧除以右侧               | 数值     | 与参数相同 | 左到右 |
| `>`    | 如果左侧大于右侧则为真     | 数值     | 布尔*      | 左到右 |
| `<`    | 如果左侧小于右侧则为真     | 数值     | 布尔*      | 左到右 |
| `>=`   | 如果左侧大于等于右侧则为真 | 数值     | 布尔*      | 左到右 |
| `<=`   | 如果左侧小于等于右侧则为真 | 数值     | 布尔*      | 左到右 |
| `==`   | 如果左侧等于右侧则为真     | 数值     | 布尔*      | 左到右 |
| `!=`   | 如果左侧不等于右侧则为真   | 数值     | 布尔*      | 左到右 |







#### 取反表达式

```java
negate_expr -> '-' expr
```

取反表达式是在表达式前添加负号组成的表达式。取反表达式的语义是将表达式转换成它的相反数。



#### 赋值表达式

```java
l_expr -> IDENT
assign_expr -> l_expr '=' expr
```

赋值表达式是由 *左值表达式*、*等号 `=`*、*表达式* 组成的表达式。赋值表达式的值类型永远是 `void`（即不能被使用）。

左值表达式是一个局部或全局的变量名。

赋值表达式的语义是将右侧表达式的计算结果赋给左侧表示的值。



#### 类型转换表达式

```
as_expr -> expr 'as' ty
```

类型转换表达式是由 *表达式*、*关键字 `as`*、*类型* 组成的表达式。类型转换表达式的语义是将左侧表达式表示的值转换成右侧类型表示的值。

在 c0 实验中只会涉及到整数 `int` 和浮点数 `double` 之间的互相转换。



#### 函数调用表达式

```java
call_param_list -> expr (',' expr)*
call_expr -> IDENT '(' call_param_list? ')'

// modified
call_param_list -> ε | expr | expr (',' expr)*
call_expr ->IDENT '(' call_param_list ')'
```

##### 标准库

标准库中的函数在调用前不需要声明

由于 c0 语言本身比较简单，为了实现输入输出的功能，我们规定了 8 个不需要声明就可以调用的函数：

```rust
/// 读入一个有符号整数
fn getint() -> int;

/// 读入一个浮点数
fn getdouble() -> double;

/// 读入一个字符
fn getchar() -> int;

/// 输出一个整数
fn putint(int) -> void;

/// 输出一个浮点数
fn putdouble(double) -> void;

/// 输出一个字符
fn putchar(int) -> void;

/// 将编号为这个整数的全局常量看作字符串输出
fn putstr(int) -> void;

/// 输出一个换行
fn putln() -> void;
```

在实现时，这些函数既可以编译成使用虚拟机中的 `callname` 指令调用，也可以编译成相应的虚拟机指令（`scan.i`, `print.i` 等），在虚拟机实现上两者是等价的。



#### 字面量表达式

```
literal_expr -> UINT_LITERAL | DOUBLE_LITERAL | STRING_LITERAL

digit -> [0-9]
UINT_LITERAL -> digit+
DOUBLE_LITERAL -> digit+ '.' digit+ ([eE] digit+)?

escape_sequence -> '\' [\\"'nrt]
string_regular_char -> [^"\\]
STRING_LITERAL -> '"' (string_regular_char | escape_sequence)* '"'

```

字面量表达式可以是一个无符号整数、浮点数或者字符串的字面量。*整数* 和 *浮点数字面量* 的语义就是用对应类型表示的字面量的值（64 位）；*字符串字面量* 只会在 `putstr` 调用中出现，语义是对应的全局常量的编号。



#### 标识符表达式

```
ident_expr -> IDENT
```

标识符表达式是由标识符组成的表达式。其语义是标识符对应的局部或全局变量。标识符表达式的类型与标识符的类型相同。



#### 括号表达式

```
group_expr -> '(' expr ')'
```

括号表达式内部的表达式的值将被优先计算。



### 语句

```java
stmt ->
      expr_stmt    // 表达式语句
    | decl_stmt    // 声明语句
    | if_stmt      // `if` 语句
    | while_stmt   // `while` 语句
    | return_stmt  // `return` 语句
    | block_stmt   // 代码块
    | empty_stmt   // 空语句
```

语句是函数的最小组成部分。

#### 表达式语句

```
expr_stmt -> expr ';'
```

表达式语句由 *表达式* 后接分号组成。表达式如果有值，值将会被丢弃。

#### 声明语句

```java
let_decl_stmt -> 'let' IDENT ':' ty ('=' expr)? ';'
const_decl_stmt -> 'const' IDENT ':' ty '=' expr ';'
decl_stmt -> let_decl_stmt | const_decl_stmt
```

例：

```
let count : int ;
let sum : int = 0 ;

const sum : int = 0 ;
```

声明语句由 `let`（声明变量）或 `const`（声明常量）接 *标识符*、*类型* 和可选的 *初始化表达式* 组成。其中，常量声明语句必须有初始化表达式，而变量声明语句可以没有。

一个声明语句会在当前作用域中创建一个给定类型和标识符的变量或常量。声明语句有以下语义约束：

- 在同一作用域内，一个标识符只能由一个变量或常量使用。
- 变量或常量的类型不能为 `void`。
- 如果存在初始化表达式，其类型应当与变量声明时的类型相同。
- 常量只能被读取，不能被修改。

出现违反约束的声明语句是编译期错误。

> UB: 没有初始化的变量的值未定义。我们不规定对于使用未初始化变量的行为的处理方式，你可以选择忽略、提供默认值或者报错。

> UB: 我们不考虑局部变量和全局函数重名的情况。局部变量和全局变量重名的时候应当覆盖全局变量定义。

以下是一些可以通过编译的变量声明的例子：

```rust
let i: int;
let j: int = 1;
const k: double = 1.20;
```

以下是一些不能通过编译的变量声明的例子：

```rust
// 没有类型
let l = 1;
// 没有初始化
const m: int;
// 类型不匹配
let n: double = 3;
// 常量不能被修改
const p: double = 3.0;
p = 3.1415;
```



#### 控制流语句

基础 C0 中有三种控制流语句，分别是 `if`、`while` 和 `return` 语句。

> 对于 `if` 和 `while` 的条件，如果求值结果是 `int` 类型，则所有非零值均视为 `true`。

#### `if` 语句

```
if_stmt -> 'if' expr block_stmt ('else' (block_stmt | if_stmt))?
//              ^~~~ ^~~~~~~~~~         ^~~~~~~~~~~~~~~~~~~~~~
//              |     if_block           else_block
//              condition
```

`if` 语句代表一组可选执行的语句。

`if` 语句的执行流程是：

- 求`condition`的值
    - 如果值为 `true`，则执行 `if_block`
    - 否则，如果存在 `else_block`，执行 `else_block`
    - 否则，执行下一条语句

请注意，**if 语句的条件表达式可以没有括号**，且 **条件执行的语句都必须是代码块**。

以下是一些合法的 if 语句：

```rust
if x > 0 {
  x = x + 1;
}

if y < 0 {
  z = -1;
} else if y > 0 {
  z = 1;
} else {
  z = 0
}
```

以下是一些不合法的 if 语句：

```rust
// 必须是代码块
if x > 0 
  x = x + 1;
```

#### `while` 语句

```
while_stmt -> 'while' expr block_stmt
//                    ^~~~ ^~~~~~~~~~while_block
//                     condition
```

while 语句代表一组可以重复执行的语句。

while 语句的执行流程是：

- 求值`condition`
    - 如果为`true`
        - 执行 `while_block`
        - 回到开头重新求值
    - 如果为 `false` 则执行之后的代码

#### `return` 语句

```
return_stmt -> 'return' expr? ';'
```

使用 `return` 语句从一个函数中返回。return 语句可以携带一个表达式作为返回值。

return 语句有以下的语义约束：

- 如果函数声明的返回值是 `void`，return 语句不能携带返回值；否则，return 语句必须携带返回值
- 返回值表达式的类型必须与函数声明的返回值类型相同
- 当执行到返回值类型是 `void` 的函数的末尾时，应视作存在一个 return 语句进行返回

> UB: 在基础 C0 中不会出现部分分支没有返回值的情况，所以没有返回语句的分支的返回值是未定义的。在扩展 C0 中你必须检查每个分支都能够正常返回。

#### 代码块

```
block_stmt -> '{' stmt* '}'
```

一个代码块可以包含一条或多条语句。执行代码块的效果是顺序执行这些语句。

在基础 c0 中，一个代码块中的声明语句只能在其他类型的语句之前出现。

在扩展 c0（作用域嵌套）中，一个代码块是其所在作用域的子作用域。在扩展 c0（变量声明增强）中，一个代码块的任何地方均可声明变量。

#### 空语句

```
empty_stmt -> ';'
```

空语句没有任何作用，只是一个分号而已。



### 函数

```java
function_param -> 'const'? IDENT ':' ty
function_param_list -> function_param (',' function_param)*
function -> 'fn' IDENT '(' function_param_list? ')' '->' ty block_stmt
//               ^~~~      ^~~~~~~~~~~~~~~~~~~~          ^~ ^~~~~~~~~~
//               |              |                        |  |
//               function_name  param_list     return_type  function_body
```

c0 中一个函数的定义由 *函数名*、*参数列表*、*返回类型* 和 *函数体* 组成。

函数有以下语义约束：

- 函数的名称 `function_name` 不能重复，也不能和全局变量重复。
- 函数的参数声明 `param_list` 与 含有初始化表达式的变量声明 有相同的语义约束。
- 函数体、函数的参数声明 在同一个作用域（函数作用域）中，是全局作用域的子作用域。

另外再提醒一下，返回值类型 `return_type` 即使为 `void` 也不能省略。

函数体的组成单位是语句，见 [语句页面](#语句)。

### 全局变量

全局变量的声明与局部变量相同，都是使用 [声明语句](#声明语句) 进行声明。全局变量的定义方式和约束与局部变量相同。全局变量所在作用域是全局，因此有可能被函数内定义的局部变量覆盖。



### 程序结构

```
program -> decl_stmt* function*
```

一个 c0 的程序中可以存在多个 *变量声明*，后接多个 *函数声明*。

语义约束：

- 一个合法的 c0 程序必须存在一个名为 `main` 的函数作为程序入口，否则应视为编译错误；
- 一个函数或变量只能在它的定义中及之后的位置被引用，换句话说就是不存在先使用后定义的情况。

> 注：扩展 c0 中允许变量声明和函数声明混搭，但仍要遵循以上规定。





## 符号表和函数表

初始化



```
符号表
################################
_start
		_start 常量，已初始化，函数，int类型，offset
```



```
_start
		let1
			变量，已初始化，非参数，int类型，offset
		foo1
			
foo1
		i
			
main
		x

########################################

_start
		返回值类型void，函数名位置，下一个局部变量偏移，下一个参数偏移（从1开始）
```



## 虚拟机

### 中间代码格式

```
const i : int = 1;
let j : double;
fn foo(i:double,j:int)-> int{
	i;;
	return j+2;
}
fn main()->void{
    let j : int = 2;
    foo(1.0,j);
}
###################################################################################
// 全局变量名、函数名 : 对应ascii码
static: 0 0 0 0 0 0 0 0 (`\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}`)
static: 0 0 0 0 0 0 0 0 (`\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}`)
static: 66 6F 6F (`foo`)
static: 6D 61 69 6E (`main`)
static: 5F 73 74 61 72 74 (`_start`)
// [对应函数名位置] 局部变量数 形参数 是否有返回值
fn [4] 0 0 -> 0 {
    0: GlobA(0)
    1: Push(1)
    2: Store64
    3: StackAlloc(0)
    4: Call(2)
}
fn [2] 0 2 -> 1 {
    0: ArgA(1)
    1: Load64
    2: PopN(1)
    3: ArgA(0)
    4: ArgA(2)
    5: Load64
    6: Push(2)
    7: AddI
    8: Store64
    9: Ret
}

fn [3] 1 0 -> 0 {
    0: LocA(0)
    1: Push(2)
    2: Store64
    3: StackAlloc(1)
    4: Push(4607182418800017408)
    5: LocA(0)
    6: Load64
    7: Call(1)
    8: PopN(1)
    9: Ret
}
```





### 函数形参

```
fn main()->void{}
fn foo(i:int,j:double)->void{
	i;
	j;
}
################################################
static: 6D 61 69 6E (`main`)
static: 66 6F 6F (`foo`)
static: 5F 73 74 61 72 74 (`_start`)
fn [2] 0 0 -> 0 {
    0: StackAlloc(0)
    1: Call(1)
}
fn [0] 0 0 -> 0 {
    0: Ret
}
fn [1] 0 2 -> 0 {
    0: ArgA(0)
    1: Load64
    2: PopN(1)
    3: Ret
}
```





二进制文件

```
// start
// 魔数
72 30 3b 3e // magic  32位
// 版本号
00 00 00 01 // version 32位

// 全局变量表

00 00 00 02 // globals.count 32位 count=2

// globals[0]
00 // globals[0].is_const 8位 is_count = 0
00 00 00 03 // globals[0].value.count
00 01 02 // globals[0].value.items  // 1,2,3

// globals[1]
01 // globals[1].is_const
00 00 00 06 // globals[1].value.count
'_' 's' 't' 'a' 'r' 't' // globals[1].value.items

// 函数列表

00 00 00 01 // functions.count

// functions[0]
00 00 00 01 // functions[0].name       函数名称在全局变量中的位置
00 00 00 00 // functions[0].ret_slots  返回值占据的 slot 数
00 00 00 00 // functions[0].param_slots参数占据的 slot 数
00 00 00 00 // functions[0].loc_slots  局部变量占据的 slot 数
// 函数体
// 函数体的长度
00 00 00 04 // functions[0].body.count 

// functions[0].body.items 
01 00 00 00 00 00 00 00 01 // Push(1) // 01:push , 00 00 00 00 00 00 00 01:param
01 00 00 00 00 00 00 00 02 // Push(2)
20 // AddI
34 // NegI
// finish
```



```
fn main() -> void {
}
################################################
static: 6D 61 69 6E (`main`)

static: 5F 73 74 61 72 74 (`_start`)

fn [1] 0 0 -> 0 {
    0: StackAlloc(0)
    1: Call(1)
}

fn [0] 0 0 -> 0 {
    0: Ret
}
```



```
const i:int = -(1+2);
let j : double= 1.0;
fn main()->void{
const i:int=1;
i;
}

fn foo(s:int)->int {
return i;
}
################################################
static: 0 0 0 0 0 0 0 0 (`\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}`)
static: 0 0 0 0 0 0 0 0 (`\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}\u{0}`)
static: 6D 61 69 6E (`main`)
static: 66 6F 6F (`foo`)
static: 5F 73 74 61 72 74 (`_start`)
// [4]:函数名位置，从0开始
fn [4] 0 0 -> 0 { // _start
    0: GlobA(0)
    1: Push(1)
    2: Push(2)
    3: AddI
    4: NegI
    5: Store64
    6: GlobA(1)
    7: Push(4607182418800017408)
    8: Store64
    9: StackAlloc(0)
   10: Call(1)
}

fn [2] 1 0 -> 0 { // main
    0: LocA(0)
    1: Push(1)
    2: Store64
    3: LocA(0)
    4: Load64
    5: PopN(1)
    6: Ret
}

fn [3] 0 1 -> 1 { // foo
    0: ArgA(0)
    1: GlobA(0)
    2: Load64
    3: Store64
    4: Ret
}
```



```
fn fib(x: int) -> int {
    if x<=1 {
        return 1;
    }
    let result: int = fib(x - 1);
    result = result + fib(x - 2);
    return result;
}

fn main() -> int {
    let i: int = 0;
    let j: int;
    j = getint();
    while i < j {
        putint(i);
        putchar(32);
        putint(fib(i));
        putln();
        i = i + 1;
    }
    return 0;
}


#########################################################

static: 5F 73 74 61 72 74 ('_start')
static: 66 69 62 ('fib')
static: 6D 61 69 6E ('main')
fn [0] L:0 P:0 -> 0 {
}
fn [1] L:1 P:2 -> 1 {
	 0: PUSH(1)
	 1: CMP_I
	 2: SET_GT
	 3: NOT
	 4: PUSH(1)
	 5: RET
	 6: LOCA(0)
	 7: PUSH(1)
	 8: SUB_I
	 9: ARGA(0)
	10: PUSH(2)
	11: SUB_I
	12: ADD_I
	13: STORE_64
	14: RET
}
fn [2] L:2 P:0 -> 0 {
	 0: LOCA(0)
	 1: PUSH(0)
	 2: ARGA(1)
	 3: STORE_64
	 4: CMP_I
	 5: SET_LT
	 6: PUSH(32)
	 7: ARGA(0)
	 8: PUSH(1)
	 9: ADD_I
	10: STORE_64
}

##########################################

_start: {
	_start: void
	main: void
	fib: int
}
main: {
	i: int
	j: int
}
fib: {
	result: int
	x: int
}

```



```
while i<j {
	i = 1;
}
##############################
Br(0)
LocA(0)
Load64
LocA(1)
Load64
CmpI
SetLt
BrFalse(4)
LocA(0)
Push(1)
Store64
Br(-11)
```



```
if i<j {
	i = 1;
} else if i < j + 1 {
	 j = 1;
}
##############################
BrFalse(4)
LocA(0)
Push(1)
Store64
Br(13) // 回填
    LocA(0)
    Load64
    LocA(1)
    Load64
    Push(1)
    AddI
    CmpI
    SetLt
    BrFalse(4)
    LocA(1)
    Push(1)
    Store64
    Br(0)
```





## 总结

先放在这里





# TempCode

analyser（中间代码生成前）





