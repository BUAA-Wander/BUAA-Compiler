package ir;

import ir.utils.LabelOp;
import ir.utils.Operand;
import mips.J;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class JumpIr extends IntermediateInstruction {
    public JumpIr(Operand label) {
        super(label);
    }

    public String toString() {
        return "Jump " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> instructions = new ArrayList<>();
        Operand op = getRes();
        if (op instanceof LabelOp) {
            instructions.add(new J(((LabelOp) op).getLabelName()));
        }
        return instructions;
    }
}
