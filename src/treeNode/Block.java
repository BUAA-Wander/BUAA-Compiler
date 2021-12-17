package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.SymbolTableItem;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(leftBrace.outputAdaptToHomework()).append("\n");
        for (int i = 0; i < blockItems.size(); i++) {
            builder.append(blockItems.get(i).outputAdaptToHomework()).append("\n");
        }
        builder.append(rightBrace.outputAdaptToHomework()).append("\n");
        builder.append("<Block>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        for (int i = 0; i < blockItems.size(); i++) {
            blockItems.get(i).createSymbolTable(level + 1, symbolTable, errors);
        }
        symbolTable.delete(level);
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        boolean flag = true;
        for (int i = 0; i < blockItems.size(); i++) {
            flag &= blockItems.get(i).dealWithErrorF(funcType, errors);
        }
        return flag;
    }

    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
        boolean flag = (funcType == FuncType.VOID);
        for (int i = 0; i < blockItems.size(); i++) {
            flag |= blockItems.get(i).dealWithErrorG(funcType, errors);
        }
        return flag;
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
        for (int i = 0; i < blockItems.size(); i++) {
            blockItems.get(i).dealWithErrorM(errors, isLoop);
        }
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
//        symbolTable.delete(level);
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        for (int i = 0; i < blockItems.size(); i++) {
            instructions.addAll(blockItems.get(i).generateIr(level + 1, label_1, label_2));
        }
//        symbolTable.delete(level);
        return instructions;
    }
}
