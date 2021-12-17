package treeNode;

import ir.IntermediateInstruction;
import ir.PassReturnValueIr;
import ir.ReturnIr;
import java.util.ArrayList;
import java.util.List;

public class ReturnStmt extends Stmt {
    private Return returnToken = null;
    private Exp exp = null;
    private Semicolon semicolon = null;

    public ReturnStmt(int num, Return returnToken, Exp exp, Semicolon semicolon) {
        super(num, StmtType.RETURN);
        this.returnToken = returnToken;
        this.exp = exp;
        this.semicolon = semicolon;
    }

    public boolean hasReturnExp() {
        return exp != null;
    }

    public int getReturnLineNumber() {
        return returnToken.getLineNumber();
    }

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();

        if (exp != null) {
            String resId = exp.generateIr(level, instructions);
            instructions.add(new PassReturnValueIr(resId));
        }
        instructions.add(new ReturnIr());

        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
