package treeNode;

import ir.IntermediateInstruction;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private int lineNumber;

    public TreeNode(int num) {
        lineNumber = num;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public List<IntermediateInstruction> generateIr(int level, SymbolTable symbolTable) {
        return new ArrayList<>();
    }
}
