package ir;

import ir.utils.Operand;
import mips.MipsCode;
import mips.Sw;
import symbol.LocalSymbolTable;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class PassParamIr extends IntermediateInstruction {
    private int offset;
    public PassParamIr(Operand memAddr, int offset) {
        super(memAddr);
        this.offset = offset;
    }

    public String toString() {
        return "PassParam " + getRes();
    }

    public List<MipsCode> toMips() {
        System.out.println("pass param:");
        List<MipsCode> mipsCodes = new ArrayList<>();
        Operand op = getRes();
        String t0 = "$t0";

        mipsCodes.addAll(op.loadToReg(t0));
        // sp - symbolTable.size * 4 is new sp position, offset is offset
        mipsCodes.add(new Sw("$sp", t0,
                offset - LocalSymbolTable.getCurrentLocalSymbolTable().getMemSize()));

        return mipsCodes;
    }
}
