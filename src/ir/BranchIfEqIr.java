package ir;

import mips.Beq;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class BranchIfEqIr extends IntermediateInstruction {
    public BranchIfEqIr(String op1, String op2, String res) {
        super(op1, op2, res);
    }

    public String toString() {
        return "BranchIfEq " + getLeft() + " " + getRight() + " " + getRes();
    }

    public List<MipsCode> toMips() {
        String op1 = getLeft();
        String op2 = getRight();
        String label = getRes();

        SymbolTableItem item1 = getItemFromSymbolTable(op1);
        SymbolTableItem item2 = getItemFromSymbolTable(op2);

        SymbolTableType type1 = getOperandSymbolTable(op1);
        SymbolTableType type2 = getOperandSymbolTable(op2);

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", t1 = "$t1";
        mipsCodes.addAll(load(item1, type1, t0));
        mipsCodes.addAll(load(item2, type2, t1));
        mipsCodes.add(new Beq(t0, t1, label));
        return mipsCodes;
    }
}
