package treeNode;

import ir.IntermediateInstruction;

import java.util.List;

public class BlockStmt extends Stmt {
    private Block block;

    public BlockStmt(int num, Block block) {
        super(num, StmtType.BLOCK);
        this.block = block;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        return block.generateIr(level);
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return block.generateIr(level, label_1, label_2);
    }
}