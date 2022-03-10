package treeNode;

import ir.IntermediateInstruction;
import ir.utils.Operand;

import java.util.List;

public class Cond extends TreeNode {
    private LOrExp lOrExp;

    public Cond(int num, LOrExp lOrExp) {
        super(num);
        this.lOrExp = lOrExp;
    }

    public Operand generateIr(int level, List<IntermediateInstruction> instructions) {
        return lOrExp.generateIr(level, instructions);
    }
}
