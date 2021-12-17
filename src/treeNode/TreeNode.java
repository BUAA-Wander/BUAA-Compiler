package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeNode {
    private int lineNumber;

    public TreeNode(int num) {
        lineNumber = num;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String outputAdaptToHomework() {
        return null;
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {

    }

    public void dealWithErrorB(List<Error> errors) {
        errors.add(new Error(getLineNumber(), "b"));
    }

    public void dealWithErrorC(List<Error> errors) {
        errors.add(new Error(getLineNumber(), "c"));
    }

    public List<IntermediateInstruction> generateIr(int level, SymbolTable symbolTable) {
        return new ArrayList<>();
    }
}
