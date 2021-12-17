package ir;

import ir.utils.Operand;
import mips.And;
import mips.MipsCode;

import java.util.ArrayList;
import java.util.List;

public class AndIr extends IntermediateInstruction {
    public AndIr(Operand oprandIdx1, Operand oprandIdx2, Operand resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "And " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op2 = getRight();
        Operand op3 = getRes();

        String t0 = "$t0", t1 = "$t1", t2 = "$t2";

        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.addAll(op2.loadToReg(t1));
        // TODO
        mipsCodes.add(new And(t0, t1, t2));
        mipsCodes.addAll(op3.saveValue(t2));

        return mipsCodes;
    }
}
