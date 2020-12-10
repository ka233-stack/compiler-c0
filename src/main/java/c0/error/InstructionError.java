package main.java.c0.error;

import main.java.c0.util.Pos;

public class InstructionError extends CompileError {

    ErrorCode code;

    @Override
    public ErrorCode getErr() {
        return code;
    }

    /**
     * @param code ErrorCode
     */
    public InstructionError(ErrorCode code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Instruction Error: " + code;
    }
}
