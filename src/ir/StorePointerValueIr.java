package ir;

import ir.utils.Operand;
import mips.Add;
import mips.MipsCode;
import mips.Sw;

import java.util.ArrayList;
import java.util.List;

public class StorePointerValueIr extends IntermediateInstruction {
    public StorePointerValueIr(Operand base, Operand offset, Operand value) {
        super(base, offset, value);
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

        String t0 = "$t0", t1 = "$t1", t2 = "$t2", t3 = "$t3";
        mipsCodes.addAll(base.loadToReg(t0));
        mipsCodes.addAll(offset.loadToReg(t1));
        // base + offset
        mipsCodes.add(new Add(t0, t1, t2));
        // 取出value
        mipsCodes.addAll(value.loadToReg(t3));
        // 把value放到base + offset地址里面
        mipsCodes.add(new Sw(t2, t3, 0));
        return mipsCodes;
    }
}
