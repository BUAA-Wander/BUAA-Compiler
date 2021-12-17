package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class Stmt extends TreeNode {
    private StmtType type;
    // ASSIGN, EXP, BLOCK, IF, WHILE, BREAK, RETURN, CONTINUE, READ, PRINTF

    public Stmt(int num, StmtType type) {
        super(num);
        this.type = type;
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return true;
    }

    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
        return false;
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {

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
