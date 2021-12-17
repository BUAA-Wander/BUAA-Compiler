package ir;

import ir.utils.Operand;
import mips.Add;
import mips.MipsCode;
import mips.Sub;

import java.util.ArrayList;
import java.util.List;

public class OffsetIr extends IntermediateInstruction {
    private boolean isGlobal;
    // 计算绝对地址
    public OffsetIr(Operand op1, Operand op2, Operand res, boolean isGlobal) {
        super(op1, op2, res);
        this.isGlobal = isGlobal;
    }

    public String toString() {
        return "Offset " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        Operand op1 = getLeft();
        Operand op2 = getRight();
        Operand res = getRes();

        String t0 = "$t0", t1 = "$t1", t2 = "$t2", sp = "$sp", gp = "$gp";

        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.addAll(op2.loadToReg(t1));
        mipsCodes.add(new Add(t0, t1, t2));

        if (isGlobal) {
            mipsCodes.add(new Add(t2, gp, t2));
        } else {
            mipsCodes.add(new Sub(sp, t2, t2));
        }

        mipsCodes.addAll(res.saveValue(t2));
        return mipsCodes;
    }
}