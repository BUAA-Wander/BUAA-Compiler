package treeNode;

import ir.BranchIfEqIr;
import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.JumpIr;
import ir.LabelGenerator;
import ir.utils.LabelOp;
import ir.utils.Operand;
import ir.utils.TmpVariable;
import ir.utils.Variable;

import java.util.ArrayList;
import java.util.List;

public class WhileStmt extends Stmt {
    private While whileToken;
    private LeftParent leftParent;
    private Cond cond;
    private RightParent rightParent;
    private Stmt stmt;

    public WhileStmt(int num, While whileToken, LeftParent leftParent, Cond cond, RightParent rightParent,
                     Stmt stmt) {
        super(num, StmtType.WHILE);
        this.whileToken = whileToken;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.stmt = stmt;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        // while label_1 cond stmt label_2
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String label_1 = LabelGenerator.nextLabel();
        String label_2 = LabelGenerator.nextLabel();

        instructions.add(new InsertLabelIr(new LabelOp(label_1)));
        Operand resId = cond.generateIr(level, instructions);
        instructions.add(new BranchIfEqIr(resId, new TmpVariable("#0", true), new LabelOp(label_2)));

        // TODO create code and insert tag
        if (stmt != null) {
            instructions.addAll(stmt.generateIr(level, label_1, label_2));
        }
        instructions.add(new JumpIr(new LabelOp(label_1)));
        instructions.add(new InsertLabelIr(new LabelOp(label_2)));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
