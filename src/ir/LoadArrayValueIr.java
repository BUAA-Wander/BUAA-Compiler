package ir;

import mips.Add;
import mips.Lw;
import mips.MipsCode;
import mips.Sub;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class LoadArrayValueIr extends IntermediateInstruction {
    private int scope; // 0 is global, 1 is local
    public LoadArrayValueIr(String oprandIdx1, String oprandIdx2, String resIdx, int scope) {
        super(oprandIdx1, oprandIdx2, resIdx);
        this.scope = scope;
    }

    public String toString() {
        return "LoadArrayValue " + getLeft() + " " + getRight() + " " + getRes();
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
        String t0 = "$t0", t1 = "$t1", t2 = "$t2", sp = "$sp", gp = "$gp";
        mipsCodes.addAll(load(item1, type1, t0));
        mipsCodes.addAll(load(item2, type2, t1));
        // base + offset
        mipsCodes.add(new Add(t0, t1, t2));
        // sp - (base + offset)
        if (scope == 0) {
            mipsCodes.add(new Add(gp, t2, t2));
        } else {
            mipsCodes.add(new Sub(sp, t2, t2));
        }
        mipsCodes.add(new Lw(t2, t2, 0));
        mipsCodes.addAll(save(item3, type3, t2));
        return mipsCodes;
    }
}
