package treeNode;

import exception.ValueTypeException;
import ir.IntermediateInstruction;
import ir.utils.Operand;

import java.util.List;

public class ConstExp extends TreeNode {
    public AddExp addExp;

    public ConstExp(int num, AddExp addExp) {
        super(num);
        this.addExp = addExp;
    }

    public int getValue(int level) throws ValueTypeException {
        return addExp.getValue(level);
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        return addExp.generateIr(level, instructions, 0);
    }
}