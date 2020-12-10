package main.java.c0.instruction;

import main.java.c0.error.ErrorCode;
import main.java.c0.error.InstructionError;

import java.util.Objects;

public class Instruction {
    private Operation opt;
    public int length; // 参数长度 u32 = 32
    public long param;

    private Instruction() {
    }

    private Instruction(Operation opt) {
        this.opt = opt;
        this.length = 0;
        this.param = 0;
    }

    private Instruction(Operation opt, int length, long param) {
        this.opt = opt;
        this.length = length;
        this.param = param;
    }

    public static Instruction createInstruction(Operation opt) throws InstructionError {
        switch (opt) {
            case NOP:
            case POP:
            case DUP:
            case LOAD_8:
            case LOAD_16:
            case LOAD_32:
            case LOAD_64:
            case STORE_8:
            case STORE_16:
            case STORE_32:
            case STORE_64:
            case ALLOC:
            case FREE:
            case ADD_I:
            case SUB_I:
            case MUL_I:
            case DIV_I:
            case ADD_F:
            case SUB_F:
            case MUL_F:
            case DIV_F:
            case DIV_U:
            case SHL:
            case SHR:
            case AND:
            case OR:
            case XOR:
            case NOT:
            case CMP_I:
            case CMP_U:
            case CMP_F:
            case NEG_I:
            case NEG_F:
            case ITOF:
            case FTOI:
            case SHRL:
            case SET_LT:
            case SET_GT:
            case RET:
            case SCAN_I:
            case SCAN_C:
            case SCAN_F:
            case PRINT_I:
            case PRINT_C:
            case PRINT_F:
            case PRINT_S:
            case PRINTLN:
                return new Instruction(opt);
            default:
                throw new InstructionError(ErrorCode.UnsupportedInstructionType);
        }
    }

    public static Instruction createInstruction(Operation opt, long param) throws InstructionError {
        switch (opt) {
            case POPN:
            case LOCA:
            case ARGA:
            case GLOBA:
            case STACKALLOC:
            case CALL:
            case CALLNAME:
                if (param < 0)
                    throw new InstructionError(ErrorCode.IllegalInstruction);
                return new Instruction(opt, 32, param);
            case BR:
            case BR_FALSE:
            case BR_TURE:
                return new Instruction(opt, 32, param);
            case PUSH:
                if (param < 0)
                    throw new InstructionError(ErrorCode.IllegalInstruction);
                return new Instruction(opt, 64, param);
            default:
                throw new InstructionError(ErrorCode.UnsupportedInstructionType);
        }
    }

    public Operation getOpt() {
        return opt;
    }

    public void setOpt(Operation opt) {
        this.opt = opt;
    }

    public void setParam(long param) {
        this.param = param;
    }

    public String toString() {
        if (this.length < 1)
            return this.opt.toString();
        else
            return this.opt.toString() + "(" + this.param + ")";
    }

    public String generateHexCodeFormat() {
        if (this.length < 1)
            return String.format("%02X", this.opt.getCode());
        else
            return String.format("%02X" + " %0" + this.length / 4 + "X", this.opt.getCode(), this.param);
    }

    public String generateHexCode() {
        if (this.length < 1)
            return String.format("%02X", this.opt.getCode());
        else
            return String.format("%02X" + "%0" + this.length / 4 + "X", this.opt.getCode(), this.param);
    }

    public String generateBinCode() {
        if (this.length < 1) {
            return String.format("%8s", Integer.toBinaryString(this.opt.getCode())).replaceAll(" ", "0");
        } else
            return String.format("%8s%" + this.length + "s", Integer.toBinaryString(this.opt.getCode()),
                    Long.toBinaryString(this.param)).replaceAll(" ", "0");
    }

}