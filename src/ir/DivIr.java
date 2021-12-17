package ir;

import mips.Div;
import mips.Mflo;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class DivIr extends IntermediateInstruction {
    public DivIr(String oprandIdx1, String oprandIdx2, String resIdx) {
        super(oprandIdx1, oprandIdx2, resIdx);
    }

    public String toString() {
        return "Div " + getLeft() + " " + getRight() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        String op1 = getLeft();
        String op2 = getRight();
        String op3 = getRes();

        SymbolTableItem item1 = getItemFromSymbolTable(op1);
        SymbolTableItem item2 = getItemFromSymbolTable(op2);
        SymbolTableItem item3 = getItemFromSymbolTable(op3);

        SymbolTableType type1 = getOperandSymbolTable(op1);
        SymbolTableType type2 = getOperandSymbolTable(op2);
        SymbolTableType type3 = getOperandSymbolTable(op3);

        String t0 = "$t0", t1 = "$t1", t2 = "$t2";
        mipsCodes.addAll(load(item1, type1, t0));
        mipsCodes.addAll(load(item2, type2, t1));
        mipsCodes.add(new Div(t0, t1));
        mipsCodes.add(new Mflo(t2));
        mipsCodes.addAll(save(item3, type3, t2));
        return mipsCodes;
    }
}
