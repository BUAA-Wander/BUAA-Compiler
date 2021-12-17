package treeNode;

import ir.IntermediateInstruction;
import ir.JumpIr;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ContinueStmt extends Stmt {
    private Continue continueToken;
    private Semicolon semicolon;

    public ContinueStmt(int num, Continue continueToken, Semicolon semicolon) {
        super(num, StmtType.CONTINUE);
        this.continueToken = continueToken;
        this.semicolon = semicolon;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        instructions.add(new JumpIr(label_1));
        return instructions;
    }
}
