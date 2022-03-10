package ir;

import ir.utils.Operand;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class GetReturnValueIr extends IntermediateInstruction {
    public GetReturnValueIr(Operand dst) {
        super(dst);
    }

    public String toString() {
        return "GetReturnValue " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();

        Operand op = getRes();

        // save return value in $v1!!!!!
        mipsCodes.addAll(op.saveValue("$v1"));
        return mipsCodes;
    }
}
