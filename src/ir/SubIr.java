package ir;

import mips.MipsCode;
import mips.Sub;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class SubIr extends IntermediateInstruction {
    public SubIr(String oprandIdx1, String oprandIdx2, String resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "Sub " + getLeft() + " " + getRight() + " " + getRes();
    }

    @Override
    public List<MipsCode> toMips() {
        String op1 = getLeft();
        String op2 = getRight();
        String op3 = getRes();

        SymbolTableItem item1 = getItemFromSymbolTable(op1);
        SymbolTableItem item2 = getItemFromSymbolTable(op2);
        SymbolTableItem item3 = getItemFromSymbolTable(op3);

        SymbolTableType type1 = getOperandSymbolTable(op1);
        SymbolTableType type2 = getOperandSymbolTable(op2);
        SymbolTableType type3 = getOperandSymbolTable(op3);

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t1 = "$t1", t2 = "$t2";
        mipsCodes.addAll(load(item1, type1, t0));
        mipsCodes.addAll(load(item2, type2, t1));
        mipsCodes.add(new Sub(t0, t1, t2));
        mipsCodes.addAll(save(item3, type3, t2));
        return mipsCodes;
    }
}
