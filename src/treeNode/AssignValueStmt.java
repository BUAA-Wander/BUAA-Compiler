package treeNode;

import ir.IntermediateInstruction;
import ir.MovIr;

import java.util.ArrayList;
import java.util.List;

public class AssignValueStmt extends Stmt {
    private LVal lVal;
    private Assign assignToken;
    private Exp exp;
    private Semicolon semicolon;

    public AssignValueStmt(int num, LVal lVal, Assign assignToken, Exp exp, Semicolon se) {
        super(num, StmtType.ASSIGN);
        this.lVal = lVal;
        this.assignToken = assignToken;
        this.exp = exp;
        this.semicolon = se;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String dst = lVal.generateIr(level, instructions);
        String src = exp.generateIr(level, instructions);
        instructions.add(new MovIr(src, dst));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
