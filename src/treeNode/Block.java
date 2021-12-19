package treeNode;

import ir.IntermediateInstruction;
import symbol.LocalSymbolTable;

import java.util.ArrayList;
import java.util.List;

public class Block extends TreeNode {
    private LeftBrace leftBrace;
    private List<BlockItem> blockItems;
    private RightBrace rightBrace;

    public Block(int num, LeftBrace leftBrace, List<BlockItem> blockItems, RightBrace rightBrace) {
        super(num);
        this.leftBrace = leftBrace;
        this.blockItems = blockItems;
        this.rightBrace = rightBrace;
    }

    public int getReturnLineNumber() {
        return blockItems.get(blockItems.size() - 1).getReturnLineNumber();
    }

    public int getLastRBraceLineNumber() {
        return rightBrace.getLineNumber();
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < blockItems.size(); i++) {
            instructions.addAll(blockItems.get(i).generateIr(level + 1));
        }
//        LocalSymbolTable.getCurrentLocalSymbolTable().delete(level);
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < blockItems.size(); i++) {
            instructions.addAll(blockItems.get(i).generateIr(level + 1, label_1, label_2));
        }
//        LocalSymbolTable.getCurrentLocalSymbolTable().delete(level);
        return instructions;
    }
}
