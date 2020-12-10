package main.java.c0.analyser.o0;

import main.java.c0.instruction.Instruction;

import static main.java.c0.util.StringUtils.intToBin;
import static main.java.c0.util.StringUtils.intToHex;

public class O0 {
    // 魔数
    public int magic = 0x72303b3e;// u32
    // 版本号，定为 1
    public int version = 0x00000001;// u32
    // 全局变量表
    public Array<GlobalDef> globals;
    /// 函数列表
    public Array<FunctionDef> functions;

    public O0() {
        this.globals = new Array<>();
        this.functions = new Array<>();
        this.globals.add(new GlobalDef((byte) 0, "_start"));
        this.functions.add(new FunctionDef(0, 0, 0, 0));
    }

    public void addGlobal(GlobalDef global) {
        this.globals.add(global);
    }

    public void addFunction(FunctionDef function) {
        this.functions.add(function);
    }

    public void initNewFunc(int funcNo, String funcName) {
        this.functions.add(new FunctionDef(funcNo, 0, 0, 0));
    }

    public int addInstruction(int funcNo, Instruction instruction) {
        return this.functions.getItems().get(funcNo).addInstruction(instruction);
    }

    public Instruction getInstruction(int funcNo, int no) {
        return this.functions.getItems().get(funcNo).getInstruction(no);
    }

    public int getInsNum(int funcNo) {
        return this.functions.getItems().get(funcNo).getInsNum();
    }

    public void addFuncParamNum(int funcNo) {
        this.functions.getItems().get(funcNo).addParamNum();
    }

    public void setFuncRet(int funcNo) {
        this.functions.getItems().get(funcNo).setReturn_slots(1);
    }

    public void addFuncLocNum(int funcNo) {
        this.functions.getItems().get(funcNo).addLocVarNum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        var globalList = this.globals.getItems();
        int globalSize = this.globals.getCount();
        for (int i = 0; i < globalSize; i++) {
            sb.append("static:");
            var value = globalList.get(i).getValue();
            int valueSize = value.getCount();
            var valueList = value.getItems();
            for (int j = 0; j < valueSize; j++) {
                sb.append(" ").append(intToHex(2, valueList.get(j)).toUpperCase());
            }
            sb.append(" ('");
            for (int j = 0; j < valueSize; j++) {
                sb.append((char) (int) valueList.get(j));
            }
            sb.append("')\n");
        }
        var funcList = this.functions.getItems();
        int funcSize = this.functions.getCount();
        for (int i = 0; i < funcSize; i++) {
            var func = funcList.get(i);
            sb.append("fn [").append(func.getName());
            sb.append("] L:").append(func.getLoc_slots());
            sb.append(" P:").append(func.getParam_slots());
            sb.append(" -> ").append(func.getReturn_slots()).append(" {\n");
            var ins = func.getBody();
            int insSize = ins.getCount();
            var insList = ins.getItems();
            for (int j = 0; j < insSize; j++) {
                sb.append("\t").append(String.format("%2s", j));
                sb.append(": ").append(insList.get(j)).append("\n");
            }
            sb.append("}\n");
        }
        return sb.toString();
    }

    public String toBinCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(intToBin(32, this.magic));
        sb.append(intToBin(32, this.version));
        var globalList = this.globals.getItems();
        int globalSize = this.globals.getCount();
        for (int i = 0; i < globalSize; i++) {
            var globalDef = globalList.get(i);
            sb.append(intToBin(32, globalDef.getIs_const()));
            var value = globalList.get(i).getValue();
            int valueSize = value.getCount();
            sb.append(intToBin(32, valueSize));
            var valueList = value.getItems();
            for (int j = 0; j < valueSize; j++) {
                sb.append(intToBin(2, valueList.get(j)));
            }
        }

        int funcSize = this.functions.getCount();
        sb.append(intToBin(32, funcSize));
        var funcList = this.functions.getItems();
        for (int i = 0; i < funcSize; i++) {
            var func = funcList.get(i);
            sb.append(intToBin(32, func.getName()));
            sb.append(intToBin(32, func.getReturn_slots()));
            sb.append(intToBin(32, func.getParam_slots()));
            sb.append(intToBin(32, func.getLoc_slots()));
            var ins = func.getBody();
            int insSize = ins.getCount();
            sb.append(intToBin(32, insSize));
            var insList = ins.getItems();
            for (int j = 0; j < insSize; j++) {
                var instruction = insList.get(j);
                sb.append(instruction.generateBinCode());
            }
        }
        return sb.toString();
    }

    public String toHexCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(intToHex(8, this.magic));
        sb.append(intToHex(8, this.version));
        var globalList = this.globals.getItems();
        int globalSize = this.globals.getCount();
        for (int i = 0; i < globalSize; i++) {
            var globalDef = globalList.get(i);
            sb.append(intToHex(8, globalDef.getIs_const()));
            var value = globalList.get(i).getValue();
            int valueSize = value.getCount();
            sb.append(intToHex(8, valueSize));
            var valueList = value.getItems();
            for (int j = 0; j < valueSize; j++) {
                sb.append(intToHex(2, valueList.get(j)));
            }
        }
        int funcSize = this.functions.getCount();
        sb.append(intToHex(8, funcSize));
        var funcList = this.functions.getItems();
        for (int i = 0; i < funcSize; i++) {
            var func = funcList.get(i);
            sb.append(intToHex(8, func.getName()));
            sb.append(intToHex(8, func.getReturn_slots()));
            sb.append(intToHex(8, func.getParam_slots()));
            sb.append(intToHex(8, func.getLoc_slots()));
            var ins = func.getBody();
            int insSize = ins.getCount();
            sb.append(intToHex(8, insSize));
            var insList = ins.getItems();
            for (int j = 0; j < insSize; j++) {
                var instruction = insList.get(j);
                sb.append(instruction.generateHexCode());
            }
        }
        return sb.toString();
    }

    public String toHexCodeFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(intToHex(8, this.magic));
        sb.append(" // magic\n");
        sb.append(intToHex(8, this.version));
        sb.append(" // version\n\n");
        var globalList = this.globals.getItems();
        int globalSize = this.globals.getCount();
        for (int i = 0; i < globalSize; i++) {
            sb.append("// globals[").append(i).append("]\n");
            var globalDef = globalList.get(i);
            sb.append(intToHex(8, globalDef.getIs_const())).append(" // globals[").append(i).append("].is_const\n");
            var value = globalList.get(i).getValue();
            int valueSize = value.getCount();
            sb.append(intToHex(8, valueSize)).append(" // globals[").append(i).append("].value.count\n");
            var valueList = value.getItems();
            for (int j = 0; j < valueSize; j++) {
                if (j != 0)
                    sb.append(" ");
                sb.append(intToHex(2, valueList.get(j)));
            }
            sb.append(" // globals[").append(i).append("].value.items\n\n");
        }

        int funcSize = this.functions.getCount();
        sb.append(intToHex(8, funcSize)).append(" // functions.count\n\n");
        var funcList = this.functions.getItems();
        for (int i = 0; i < funcSize; i++) {
            sb.append("// functions[").append(i).append("]\n");
            var func = funcList.get(i);
            sb.append(intToHex(8, func.getName())).append(" // functions[").append(i).append("].name\n");
            sb.append(intToHex(8, func.getReturn_slots())).append(" // functions[").append(i).append("].ret_slots\n");
            sb.append(intToHex(8, func.getParam_slots())).append(" // functions[").append(i).append("].param_slots\n");
            sb.append(intToHex(8, func.getLoc_slots())).append(" // functions[").append(i).append("].loc_slots\n");
            var ins = func.getBody();
            int insSize = ins.getCount();
            sb.append(intToHex(8, insSize)).append(" // functions[").append(i).append("].body_count\n");
            var insList = ins.getItems();
            sb.append("\t// functions[").append(i).append("].body.items\n");
            for (int j = 0; j < insSize; j++) {
                var instruction = insList.get(j);
                sb.append("\t").append(instruction.generateHexCode()).append(" // ").append(instruction).append("\n");
            }
            sb.append("\n");
        }
        sb.append("// finish");
        return sb.toString();
    }
}
