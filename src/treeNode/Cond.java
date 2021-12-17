package treeNode;

import ir.IntermediateInstruction;

import java.util.List;

public class Cond extends TreeNode {
    private LOrExp lOrExp;

    public Cond(int num, LOrExp lOrExp) {
        super(num);
        this.lOrExp = lOrExp;
    }

    public String generateIr(int level, List<IntermediateInstruction> instructions) {
        return lOrExp.generateIr(level, instructions);
    }
}
