package treeNode;

import ir.BranchIfEqIr;
import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.JumpIr;
import ir.LabelGenerator;

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

        instructions.add(new InsertLabelIr(label_1));
        String resId = cond.generateIr(level, instructions);
        instructions.add(new BranchIfEqIr(resId, "#0", label_2));

        // TODO create code and insert tag
        if (stmt != null) {
            instructions.addAll(stmt.generateIr(level, label_1, label_2));
        }
        instructions.add(new JumpIr(label_1));
        instructions.add(new InsertLabelIr(label_2));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
