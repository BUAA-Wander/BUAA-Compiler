package ir;

import mips.Label;
import mips.MipsCode;
import symbol.FunctionLocalSymbolTables;
import symbol.LocalSymbolTable;

import java.util.ArrayList;
import java.util.List;

public class InsertLabelIr extends IntermediateInstruction {
    public InsertLabelIr(String label) {
        super(label);
    }

    public String toString() {
        return "InsertLabel " + getRes();
    }

    public List<MipsCode> toMips() {
        String label = getRes();

        // change symbolTable
        // ensure we are in correct local symbol table
        if (FunctionLocalSymbolTables.hasLocalSymbolTable(label)) {
            LocalSymbolTable.setSymbolTable(
                    FunctionLocalSymbolTables.getLocalSymbolTableByFunctionName(label));
        }

        List<MipsCode> mipsCodes = new ArrayList<>();
        mipsCodes.add(new Label(label));
        return mipsCodes;
    }
}
