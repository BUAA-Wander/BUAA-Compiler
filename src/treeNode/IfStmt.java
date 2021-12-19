package treeNode;

import ir.BranchIfEqIr;
import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.JumpIr;
import ir.LabelGenerator;
import ir.utils.LabelOp;
import ir.utils.Operand;
import ir.utils.TmpVariable;

import java.util.ArrayList;
import java.util.List;

public class IfStmt extends Stmt {
    private If ifToken;
    private LeftParent leftParent;
    private Cond cond;
    private RightParent rightParent;
    private Stmt stmt;
    private Else elseToken = null;
    private Stmt elseStmt = null;

    public IfStmt(int num, If ifToken, LeftParent leftParent, Cond cond, RightParent rightParent, Stmt stmt) {
        super(num, StmtType.IF);
        this.ifToken = ifToken;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.stmt = stmt;
    }

    public IfStmt(int num, If ifToken, LeftParent leftParent, Cond cond, RightParent rightParent, Stmt stmt,
                  Else elseToken, Stmt elseStmt) {
        this(num, ifToken, leftParent, cond, rightParent, stmt);
        this.elseToken = elseToken;
        this.elseStmt = elseStmt;
    }

    public boolean hasElseStmt() {
        return elseStmt != null;
    }

    public List<IntermediateInstruction> generateIr(int level) {
        // if cond stmt else label_2 stmt label_1
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String label_1 = LabelGenerator.nextLabel();
        String label_2 = LabelGenerator.nextLabel();

        Operand resId = cond.generateIr(level, instructions);
        instructions.add(new BranchIfEqIr(resId, new TmpVariable(level, "#0", true), new LabelOp(label_2)));

        // TODO create code and insert tag
        if (stmt != null) {
            instructions.addAll(stmt.generateIr(level));
        }
        instructions.add(new JumpIr(new LabelOp(label_1)));
        instructions.add(new InsertLabelIr(new LabelOp(label_2)));
        if (elseStmt != null) {
            instructions.addAll(elseStmt.generateIr(level));
        }
        instructions.add(new InsertLabelIr(new LabelOp(label_1)));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_begin, String label_end) {
        // if cond stmt else label_2 stmt label_1
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String label_1 = LabelGenerator.nextLabel();
        String label_2 = LabelGenerator.nextLabel();

        Operand resId = cond.generateIr(level, instructions);
        instructions.add(new BranchIfEqIr(resId, new TmpVariable(level, "#0", true), new LabelOp(label_2)));

        // TODO create code and insert tag
        if (stmt != null) {
            instructions.addAll(stmt.generateIr(level, label_begin, label_end));
        }
        instructions.add(new JumpIr(new LabelOp(label_1)));
        instructions.add(new InsertLabelIr(new LabelOp(label_2)));
        if (elseStmt != null) {
            instructions.addAll(elseStmt.generateIr(level, label_begin, label_end));
        }
        instructions.add(new InsertLabelIr(new LabelOp(label_1)));
        return instructions;
    }
}