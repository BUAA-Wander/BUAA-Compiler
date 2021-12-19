package treeNode;

import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.utils.Operand;
import symbol.SymbolTable;
import symbol.type.ParamType;

import java.util.List;

public class Exp extends TreeNode {
    private AddExp addExp;

    public Exp(int num, AddExp addExp) {
        super(num);
        this.addExp = addExp;
    }

    public ParamType getParamType(SymbolTable symbolTable) {
        return addExp.getParamType(symbolTable);
    }

    public int getValue(int level) throws ValueTypeException {
        return addExp.getValue(level);
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        return addExp.generateIr(level, instructions, 0);
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions, int used) {
        return addExp.generateIr(level, instructions, used);
    }
}
