package ir;

import ir.utils.LabelOp;
import ir.utils.Operand;
import mips.Label;
import mips.MipsCode;
import symbol.FunctionLocalSymbolTables;
import symbol.LocalSymbolTable;

import java.util.ArrayList;
import java.util.List;

public class InsertLabelIr extends IntermediateInstruction {
    public InsertLabelIr(Operand label) {
        super(label);
    }

    public String toString() {
        return "InsertLabel " + getRes();
    }

    public List<MipsCode> toMips() {
        Operand op = getRes();
        List<MipsCode> mipsCodes = new ArrayList<>();
        // change symbolTable
        // ensure we are in correct local symbol table
        if (op instanceof LabelOp) {
            String label = ((LabelOp) op).getLabelName();
            if (FunctionLocalSymbolTables.hasLocalSymbolTable(label)) {
                LocalSymbolTable.setSymbolTable(
                        FunctionLocalSymbolTables.getLocalSymbolTableByFunctionName(label));
            }
            mipsCodes.add(new Label(label));
        }
        return mipsCodes;
    }
}
