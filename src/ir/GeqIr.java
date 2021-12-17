package ir;

import ir.utils.Operand;
import mips.MipsCode;
import mips.Sge;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class GeqIr extends IntermediateInstruction {
    public GeqIr(Operand oprandIdx1, Operand oprandIdx2, Operand resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "Geq " + getLeft() + " " + getRight() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op2 = getRight();
        Operand op3 = getRes();

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t1 = "$t1", t2 = "$t2";
        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.addAll(op2.loadToReg(t1));
        mipsCodes.add(new Sge(t0, t1, t2));
        mipsCodes.addAll(op3.saveValue(t2));
        return mipsCodes;
    }
}
