package main.java.c0.analyser.o0;

import main.java.c0.instruction.Instruction;

public class FunctionDef {
    // 函数名称在全局变量中的位置
    public int name; // u32,
    // 返回值占据的 slot 数
    public int return_slots; // u32,
    // 参数占据的 slot 数
    public int param_slots; // u32,
    // 局部变量占据的 slot 数
    public int loc_slots; // u32,
    // 函数体
    public Array<Instruction> body;

    public FunctionDef(int name, int return_slots, int param_slots, int loc_slots) {
        this.name = name;
        this.return_slots = return_slots;
        this.param_slots = param_slots;
        this.loc_slots = loc_slots;
        this.body = new Array<>();
    }

    public Array<Instruction> getBody() {
        return body;
    }

    public int getName() {
        return name;
    }

    public int getLoc_slots() {
        return loc_slots;
    }

    public int getParam_slots() {
        return param_slots;
    }

    public int getReturn_slots() {
        return return_slots;
    }

    public int addInstruction(Instruction instruction) {
        return this.body.add(instruction);
    }

    public Instruction getInstruction(int no) {
        return this.body.getItems().get(no);
    }

    public int getInsNum() {
        return this.body.getCount();
    }

    public void addLocVarNum() {
        this.loc_slots++;
    }

    public void addParamNum() {
        this.param_slots++;
    }

    public void setReturn_slots(int return_slots) {
        this.return_slots = return_slots;
    }
}
