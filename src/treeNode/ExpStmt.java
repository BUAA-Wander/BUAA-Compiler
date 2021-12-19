package treeNode;

import ir.IntermediateInstruction;

import java.util.ArrayList;
import java.util.List;

public class ExpStmt extends Stmt {
    private Exp exp = null;
    private Semicolon semicolon;

    public ExpStmt(int num, Exp exp, Semicolon se) {
        super(num, StmtType.EXP);
        this.exp = exp;
        this.semicolon = se;
    }

    public boolean hasExp() {
        return exp != null;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        if (exp != null) {
            exp.generateIr(level, instructions);
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}