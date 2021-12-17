package ir;

import mips.J;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class JumpIr extends IntermediateInstruction {
    public JumpIr(String label) {
        super(label);
    }

    public String toString() {
        return "Jump " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> instructions = new ArrayList<>();
        instructions.add(new J(getRes()));
        return instructions;
    }
}
