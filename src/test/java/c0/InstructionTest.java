package test.java.c0;

import main.java.c0.error.InstructionError;
import main.java.c0.instruction.Instruction;
import main.java.c0.instruction.Operation;
import org.junit.Test;

import static main.java.c0.instruction.Instruction.createInstruction;

public class InstructionTest {

    @Test
    public void instructionTest() {
        long i = 0;
        try {
            for (Operation operation : Operation.values()) {
                if (operation.getParamsNum() == 0) {
                    if (operation != Operation.PANIC) {
                        System.out.println(i + "、 " + createInstruction(operation).generateBinCode());
                        // System.out.println(createInstrucion(operation).generateHexCode());
                    }
                } else {
                    // System.out.println(createInstrucion(operation, i).generateHexCode());
                    System.out.println(i + "、 " + Instruction.createInstruction(operation, i + 100).generateBinCode());
                }
                i++;
            }
        } catch (InstructionError instructionError) {
            System.out.println("i = " + i);
            instructionError.printStackTrace();
        }
    }

    @Test
    public void uTest() {

    }
}


