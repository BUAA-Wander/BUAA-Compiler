package treeNode;

import error.Error;
import exception.ValueTypeException;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.ParamType;

import java.util.List;

public class Exp extends TreeNode {
    private AddExp addExp;

    public Exp(int num, AddExp addExp) {
        super(num);
        this.addExp = addExp;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(addExp.outputAdaptToHomework()).append("\n");
        builder.append("<Exp>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable,
                                  List<Error> errors) {
        addExp.createSymbolTable(level, symbolTable, errors);
    }

    public ParamType getParamType(SymbolTable symbolTable) {
        return addExp.getParamType(symbolTable);
    }

    public int getValue(int level) throws ValueTypeException {
        return addExp.getValue(level);
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        return addExp.generateIr(level, instructions);
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions, int used) {
        return addExp.generateIr(level, instructions, used);
    }
}
