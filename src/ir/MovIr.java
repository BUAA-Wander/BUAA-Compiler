package ir;

import ir.utils.Operand;
import mips.Add;
import mips.Addi;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class MovIr extends IntermediateInstruction {
    public MovIr(Operand srcId, Operand dstId) {
        super(srcId, dstId);
    }

    public String toString() {
        return "Mov " + getLeft() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        Operand op1 = getLeft();
        Operand op3 = getRes();

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t2 = "$t2", t0 = "$t0", zero = "0";
        mipsCodes.addAll(op1.loadToReg(t0));
        mipsCodes.add(new Add(t0, zero, t2));
        mipsCodes.addAll(op3.saveValue(t2));
        return mipsCodes;
    }
}
