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

        // 根据是否为指针以及是否是全局来选择不同的计算方式
        if (!isBasePointer) {
            mipsCodes.add(new Add(t0, t1, t2));
            if (scope == 0) {
                mipsCodes.add(new Add(gp, t2, t2));
            } else {
                mipsCodes.add(new Sub(sp, t2, t2));
            }
        } else {
            // 如果是指针，在考虑偏移的时候就应该考虑这个指针指向的是局部地址还是全局地址
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
