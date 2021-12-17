package ir;

import mips.Add;
import mips.MipsCode;
import mips.Sub;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class ToAbsoluteAddrIr extends IntermediateInstruction {
    private boolean isGlobal;
    public ToAbsoluteAddrIr(String addr, boolean isGlobal) {
        super(addr);
        this.isGlobal = isGlobal;
    }

    public String toString() {
        return "ToAbsoluteAddr " + getRes();
    }

    public List<MipsCode> toMips() {
        String op1 = getRes();

        SymbolTableItem item1 = getItemFromSymbolTable(op1);

        SymbolTableType type1 = getOperandSymbolTable(op1);

        List<MipsCode> mipsCodes = new ArrayList<>();
        String t0 = "$t0", sp = "$sp", gp = "$gp";
        mipsCodes.addAll(load(item1, type1, t0));
        if (isGlobal) {
            mipsCodes.add(new Add(t0, gp, t0));
        } else {
            mipsCodes.add(new Sub(sp, t0, t0));
        }
        mipsCodes.addAll(save(item1, type1, t0));
        return mipsCodes;
    }
}
