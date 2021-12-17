package ir;

import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class GetReturnValueIr extends IntermediateInstruction {
    public GetReturnValueIr(String dst) {
        super(dst);
    }

    public String toString() {
        return "GetReturnValue " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        String op = getRes();

        SymbolTableItem item = getItemFromSymbolTable(op);
        SymbolTableType type = getOperandSymbolTable(op);

        // save return value in $v1!!!!!
        mipsCodes.addAll(save(item, type, "$v1"));
        return mipsCodes;
    }
}
