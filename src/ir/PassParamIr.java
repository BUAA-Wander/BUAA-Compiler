package ir;

import mips.MipsCode;
import mips.Sw;
import symbol.LocalSymbolTable;
import symbol.SymbolTableItem;
import symbol.SymbolTableType;

import java.util.ArrayList;
import java.util.List;

public class PassParamIr extends IntermediateInstruction {
    private int offset;
    public PassParamIr(String memAddr, int offset) {
        super(memAddr);
        this.offset = offset;
    }

    public String toString() {
        return "PassParam " + getRes();
    }

    public List<MipsCode> toMips() {
        System.out.println("pass param:");
        List<MipsCode> mipsCodes = new ArrayList<>();
        String op = getRes();
        String t0 = "$t0";

        SymbolTableItem item = getItemFromSymbolTable(op);
        SymbolTableType type = getOperandSymbolTable(op);

        mipsCodes.addAll(load(item, type, t0));
        // sp - symbolTable.size * 4 is new sp position, offset is offset
        mipsCodes.add(new Sw("$sp", t0,
                offset - 4 * LocalSymbolTable.getCurrentLocalSymbolTable().getSize()));

        return mipsCodes;
    }
}
