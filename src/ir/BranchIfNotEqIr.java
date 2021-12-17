package ir;

import ir.utils.LabelOp;
import ir.utils.Operand;
import mips.Beq;
import mips.Bne;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class BranchIfNotEqIr extends IntermediateInstruction {
    public BranchIfNotEqIr(Operand op1, Operand op2, Operand res) {
        super(op1, op2, res);
    }

    public String toString() {
        return "BranchIfNotEq " + getLeft() + " " + getRight() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op2 = getRight();
        Operand label = getRes();

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t1 = "$t1";
        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.addAll(op2.loadToReg(t1));
        if (label instanceof LabelOp) {
            mipsCodes.add(new Bne(t0, t1, ((LabelOp) label).getLabelName()));
        }
        return mipsCodes;
    }
}
