package treeNode;

import ir.IntermediateInstruction;
import ir.JumpIr;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class BreakStmt extends Stmt {
    private Break breakToken;
    private Semicolon semicolon;

    public BreakStmt(int num, Break breakToken, Semicolon semicolon) {
        super(num, StmtType.BREAK);
        this.breakToken = breakToken;
        this.semicolon = semicolon;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        instructions.add(new JumpIr(label_2));
        return instructions;
    }
}
