package ir;

import ir.utils.Operand;
import mips.Add;
import mips.MipsCode;
import mips.Sub;
import mips.Sw;

import java.util.ArrayList;
import java.util.List;

public class StorePointerValueIr extends IntermediateInstruction {
    private boolean isBasePointer;
    private boolean isGlobal;

    public StorePointerValueIr(Operand base, Operand offset, Operand value, boolean isGlobal, boolean isBasePointer) {
        super(base, offset, value);
        this.isGlobal = isGlobal;
        this.isBasePointer = isBasePointer;
    }

    public String toString() {
        return "StorePointerValue " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        Operand base = getLeft();
        Operand offset = getRight();
        Operand value = getRes();

        String t0 = "$t0", t1 = "$t1", t2 = "$t2", t3 = "$t3", gp = "$gp", sp = "$sp";
        mipsCodes.addAll(base.loadToReg(t0));
        mipsCodes.addAll(offset.loadToReg(t1));

        if (!isBasePointer) {
            mipsCodes.add(new Add(t0, t1, t2));
            if (isGlobal) {
                mipsCodes.add(new Add(gp, t2, t2));
            } else {
                mipsCodes.add(new Sub(sp, t2, t2));
            }
        } else {
            // 如果是指针，在考虑偏移的时候就应该考虑这个指针指向的是局部地址还是全局地址
            if (isGlobal) {
                mipsCodes.add(new Add(t0, t1, t2));
            } else {
                mipsCodes.add(new Sub(t0, t1, t2));
            }
        }

        // 取出value
        mipsCodes.addAll(value.loadToReg(t3));
        // 把value放到base + offset地址里面
        mipsCodes.add(new Sw(t2, t3, 0));
        return mipsCodes;
    }
}
