package ir;

import mips.MipsCode;
import mips.Seq;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class NotIr extends IntermediateInstruction {
    public NotIr(String oprandIdx1, String resIdx) {
        super(oprandIdx1, resIdx);
    }

    public String toString() {
        return "Not " + getLeft() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        String op1 = getLeft();
        String op3 = getRes();

        SymbolTableItem item1 = getItemFromSymbolTable(op1);
        SymbolTableItem item3 = getItemFromSymbolTable(op3);

        SymbolTableType type1 = getOperandSymbolTable(op1);
        SymbolTableType type3 = getOperandSymbolTable(op3);

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t2 = "$t2", r0 = "$0";
        mipsCodes.addAll(load(item1, type1, t0));
        mipsCodes.add(new Seq(t0, r0, t2));
        mipsCodes.addAll(save(item3, type3, t2));
        return mipsCodes;
    }
}
