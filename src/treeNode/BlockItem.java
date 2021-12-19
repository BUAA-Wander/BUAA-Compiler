package treeNode;

import ir.IntermediateInstruction;

import java.util.List;

public class BlockItem extends TreeNode {
    private BlockItemType type = null; // DECL, STMT
    private Decl decl = null;
    private Stmt stmt = null;

    public BlockItem(int num, BlockItemType type, Decl decl) {
        super(num);
        this.type = type;
        this.decl = decl;
    }

    public BlockItem(int num, BlockItemType type, Stmt stmt) {
        super(num);
        this.type = type;
        this.stmt = stmt;
    }

    public int getReturnLineNumber() {
        return stmt.getReturnLineNumber();
    }

    public List<IntermediateInstruction> generateIr(int level) {
        if (type == BlockItemType.DECL) {
            return decl.generateIr(level);
        } else {
            return stmt.generateIr(level);
        }
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        if (type == BlockItemType.DECL) {
            return decl.generateIr(level);
        } else {
            return stmt.generateIr(level, label_1, label_2);
        }
    }
}
