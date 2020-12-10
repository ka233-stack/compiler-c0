package main.java.c0.instruction;

public enum Operation {
    // ------------------------------------------- 指令     指令名        操作数        弹栈              压栈             介绍
    NOP(0x00, 0),           //    0x00    nop          -            -                -               空指令
    PUSH(0x01, 1),          //    0x01    push         num:u64      -                1:num           将num压栈
    POP(0x02, 0),           //    0x02    pop          -            1                                弹栈1个slot
    POPN(0x03, 1),          //    0x03    popn         num:u32      1-num            -               弹栈num个slot
    DUP(0x04, 0),           //    0x04    dup          -            1:num            1:num, 2:num    复制栈顶 slot
    LOCA(0x0a, 1),          //    0x0a    loca         off:u32      -                1:addr          加载off个slot处局部变量的地址
    ARGA(0x0b, 1),          //    0x0b    arga         off:u32      -                1:addr          加载off个slot处参数/返回值的地址
    GLOBA(0x0c, 1),         //    0x0c    globa        n:u32        -                1:addr          加载第n个全局变量/常量的地址
    LOAD_8(0x10, 0),        //    0x10    load.8       -            1:addr           1:val           从addr加载8位value 压栈
    LOAD_16(0x11, 0),       //    0x11    load.16      -            1:addr           1:val           从addr加载16位value 压栈
    LOAD_32(0x12, 0),       //    0x12    load.32      -            1:addr           1:val           从addr加载32位value 压栈
    LOAD_64(0x13, 0),       //    0x13    load.64      -            1:addr           1:val           从addr加载64位value 压栈
    STORE_8(0x14, 0),       //    0x14    store.8      -            1:addr, 2:val    -               把val截断到8位存入addr
    STORE_16(0x15, 0),      //    0x15    store.16     -            1:addr, 2:val    -               把val截断到16位存入addr
    STORE_32(0x16, 0),      //    0x16    store.32     -            1:addr, 2:val    -               把val截断到32位存入addr
    STORE_64(0x17, 0),      //    0x17    store.64     -            1:addr, 2:val    -               把val存入addr
    ALLOC(0x18, 0),         //    0x18    alloc        -            1:size           1:addr          在堆上分配size字节的内存
    FREE(0x19, 0),          //    0x19    free         -            1:addr           -               释放addr指向的内存块
    STACKALLOC(0x1a, 1),    //    0x1a    stackalloc   size:u32     -                -               在当前栈顶分配size个slot，初始化为    0
    ADD_I(0x20, 0),         //    0x20    add.i        -            1:lhs, 2:rhs     1:res           计算 res = lhs + rhs，参数为整数
    SUB_I(0x21, 0),         //    0x21    sub.i        -            1:lhs, 2:rhs     1:res           计算 res = lhs - rhs，参数为整数
    MUL_I(0x22, 0),         //    0x22    mul.i        -            1:lhs, 2:rhs     1:res           计算 res = lhs * rhs，参数为整数
    DIV_I(0x23, 0),         //    0x23    div.i        -            1:lhs, 2:rhs     1:res           计算 res = lhs / rhs，参数为有符号整数
    ADD_F(0x24, 0),         //    0x24    add.f        -            1:lhs, 2:rhs     1:res           计算 res = lhs + rhs，参数为浮点数
    SUB_F(0x25, 0),         //    0x25    sub.f        -            1:lhs, 2:rhs     1:res           计算 res = lhs - rhs，参数为浮点数
    MUL_F(0x26, 0),         //    0x26    mul.f        -            1:lhs, 2:rhs     1:res           计算 res = lhs * rhs，参数为浮点数
    DIV_F(0x27, 0),         //    0x27    div.f        -            1:lhs, 2:rhs     1:res           计算 res = lhs / rhs，参数为浮点数
    DIV_U(0x28, 0),         //    0x28    div.u        -            1:lhs, 2:rhs     1:res           计算 res = lhs / rhs，参数为无符号整数
    SHL(0x29, 0),           //    0x29    shl          -            1:lhs, 2:rhs     1:res           计算 res = lhs << rhs
    SHR(0x2a, 0),           //    0x2a    shr          -            1:lhs, 2:rhs     1:res           计算 res = lhs >> rhs （算术右移）
    AND(0x2b, 0),           //    0x2b    and          -            1:lhs, 2:rhs     1:res           计算 res = lhs & rhs
    OR(0x2c, 0),            //    0x2c    or           -            1:lhs, 2:rhs     1:res           计算 res = lhs | rhs
    XOR(0x2d, 0),           //    0x2d    xor          -            1:lhs, 2:rhs     1:res           计算 res = lhs ^ rhs
    NOT(0x2e, 0),           //    0x2e    not          -            1:lhs            1:res           计算 res = !lh
    CMP_I(0x30, 0),         //    0x30    cmp.i        -            1:lhs, 2:rhs     1:res           比较有符号整数lhs和rhs大小
    CMP_U(0x31, 0),         //    0x31    cmp.u        -            1:lhs, 2:rhs     1:res           比较无符号整数lhs和rhs大小
    CMP_F(0x32, 0),         //    0x32    cmp.f        -            1:lhs, 2:rhs     1:res           比较浮点数lhs和rhs大小
    NEG_I(0x34, 0),         //    0x34    neg.i        -            1:lhs            1:res           对lhs取反
    NEG_F(0x35, 0),         //    0x35    neg.f        -            1:lhs            1:res           对lhs取反
    ITOF(0x36, 0),          //    0x36    itof         -            1:lhs            1:res           把lhs从整数转换成浮点数
    FTOI(0x37, 0),          //    0x37    ftoi         -            1:lhs            1:res           把lhs从浮点数转换成整数
    SHRL(0x38, 0),          //    0x38    shrl         -            1:lhs, 2:rhs     1:res           计算 res = lhs >>> rhs（逻辑右移）
    SET_LT(0x39, 0),        //    0x39    set.lt       -            1:lhs            1:res           如果lhs<0则推入1，否则0
    SET_GT(0x3a, 0),        //    0x3a    set.gt       -            1:lhs            1:res           如果lhs>0则推入1，否则0
    BR(0x41, 1),            //    0x41    br           off:i32                                       无条件跳转偏移off
    BR_FALSE(0x42, 1),      //    0x42    br.false     off:i32      1:test                           如果test是0则跳转偏移off
    BR_TURE(0x43, 1),       //    0x43    br.true      off:i32      1:test                           如果test非0则跳转偏移off
    CALL(0x48, 1),          //    0x48    call         id:u32                        见栈帧介绍        调用编号为id的函数
    RET(0x49, 0),           //    0x49    ret          -            见栈帧介绍                         从当前函数返回
    CALLNAME(0x4a, 1),      //    0x4a    callname     id:u32                        见栈帧介绍        调用名称与编号为id的全局变量内容相同的函数
    SCAN_I(0x50, 0),        //    0x50    scan.i       -            -                1:n             从标准输入读入一个整数n
    SCAN_C(0x51, 0),        //    0x51    scan.c       -            -                1:c             从标准输入读入一个字符c
    SCAN_F(0x52, 0),        //    0x52    scan.f       -            -                1:f             从标准输入读入一个浮点数f
    PRINT_I(0x54, 0),       //    0x54    print.i      -            1:x              -               向标准输出写入一个有符号整数x
    PRINT_C(0x55, 0),       //    0x55    print.c      -            1:c              -               向标准输出写入字符c
    PRINT_F(0x56, 0),       //    0x56    print.f      -            1:f              -               向标准输出写入浮点数f
    PRINT_S(0x57, 0),       //    0x57    print.s      -            1:i              -               向标准输出写入全局变量i代表的字符串
    PRINTLN(0x58, 0),       //    0x58    println      -            -                -               向标准输出写入一个换行
    PANIC(0xfe, 0);         //    0xfe    panic                                                      恐慌（强行退出）

    private int code;
    private int paramsNum;

    private Operation(int code, int paramsNum) {
        this.code = code;
        this.paramsNum = paramsNum;
    }

    public int getCode() {
        return code;
    }

    public int getParamsNum() {
        return paramsNum;
    }
}
