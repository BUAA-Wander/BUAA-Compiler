package treeNode;

import ir.IntermediateInstruction;

import java.util.ArrayList;
import java.util.List;

public class Stmt extends TreeNode {
    private StmtType type;
    // ASSIGN, EXP, BLOCK, IF, WHILE, BREAK, RETURN, CONTINUE, READ, PRINTF

    public Stmt(int num, StmtType type) {
        super(num);
        this.type = type;
    }

    public int getReturnLineNumber() {
        return -1;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        return new ArrayList<>();
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return new ArrayList<>();
    }
}
