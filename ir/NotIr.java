package ir;

import ir.utils.Operand;
import mips.MipsCode;
import mips.Seq;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class NotIr extends IntermediateInstruction {
    public NotIr(Operand oprandIdx1, Operand resIdx) {
        super(oprandIdx1, resIdx);
    }

    public String toString() {
        return "Not " + getLeft() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op3 = getRes();

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t2 = "$t2", r0 = "$0";
        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.add(new Seq(t0, r0, t2));
        mipsCodes.addAll(op3.saveValue(t2));
        return mipsCodes;
    }
}
