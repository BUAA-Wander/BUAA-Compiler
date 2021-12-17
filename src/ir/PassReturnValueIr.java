package ir;

import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class PassReturnValueIr extends IntermediateInstruction {
    public PassReturnValueIr(String memAddr) {
        super(memAddr);
    }

    public String toString() {
        return "PassReturnValue " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        String memAddr = getRes();
        SymbolTableItem item = getItemFromSymbolTable(memAddr);
        SymbolTableType type = getOperandSymbolTable(memAddr);
        mipsCodes.addAll(load(item, type, "$v1"));
        return mipsCodes;
    }
}
