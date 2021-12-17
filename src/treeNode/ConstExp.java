package treeNode;

import error.Error;
import exception.ValueTypeException;
import ir.IntermediateInstruction;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ConstExp extends TreeNode {
    public AddExp addExp;

    public ConstExp(int num, AddExp addExp) {
        super(num);
        this.addExp = addExp;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(addExp.outputAdaptToHomework()).append("\n");
        builder.append("<ConstExp>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        addExp.createSymbolTable(level, symbolTable, errors);
    }

    public int getValue(int level) throws ValueTypeException {
        return addExp.getValue(level);
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        return addExp.generateIr(level, instructions);
    }
}