package ir;

import ir.utils.Operand;
import mips.MipsCode;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class PassReturnValueIr extends IntermediateInstruction {
    public PassReturnValueIr(Operand memAddr) {
        super(memAddr);
    }

    public String toString() {
        return "PassReturnValue " + getRes();
    }

    public List<MipsCode> toMips() {
        List<MipsCode> mipsCodes = new ArrayList<>();
        Operand memAddr = getRes();
        mipsCodes.addAll(memAddr.loadToReg("$v1"));
        return mipsCodes;
    }
}
