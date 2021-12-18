package ir;

import ir.utils.Operand;
import mips.Add;
import mips.Lw;
import mips.MipsCode;
import mips.Sub;
import java.util.ArrayList;
import java.util.List;

public class LoadArrayValueIr extends IntermediateInstruction {
    private int scope;
    private boolean isBasePointer;
    public LoadArrayValueIr(Operand base, Operand offset, Operand resIdx, int scope, boolean flag) {
        super(base, offset, resIdx);
        this.scope = scope;
        isBasePointer = flag;
    }

    public String toString() {
        return "LoadArrayValue " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        Operand base = getLeft();
        Operand offset = getRight();
        Operand res = getRes();


        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t1 = "$t1", t2 = "$t2", sp = "$sp", gp = "$gp";
        mipsCodes.addAll(base.loadToReg(t0));
        mipsCodes.addAll(offset.loadToReg(t1));
        // base + offset
//        mipsCodes.add(new Add(t0, t1, t2));
        // sp - (base + offset)

        if (!isBasePointer) {
            mipsCodes.add(new Add(t0, t1, t2));
            if (scope == 0) {
                mipsCodes.add(new Add(gp, t2, t2));
            } else {
                mipsCodes.add(new Sub(sp, t2, t2));
            }
        } else {
            if (scope == 0) {
                mipsCodes.add(new Add(t0, t1, t2));
            } else {
                mipsCodes.add(new Sub(t0, t1, t2));
            }
        }
        mipsCodes.add(new Lw(t2, t2, 0));
        mipsCodes.addAll(res.saveValue(t2));
        return mipsCodes;
    }
}
