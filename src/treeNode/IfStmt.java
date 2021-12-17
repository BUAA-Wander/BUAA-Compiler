package treeNode;

import error.Error;
import ir.BranchIfEqIr;
import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.JumpIr;
import ir.LabelGenerator;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(ifToken.outputAdaptToHomework()).append("\n");
        builder.append(leftParent.outputAdaptToHomework()).append("\n");
        builder.append(cond.outputAdaptToHomework()).append("\n");
        builder.append(rightParent.outputAdaptToHomework()).append("\n");
        builder.append(stmt.outputAdaptToHomework()).append("\n");
        if (elseToken != null) {
            builder.append(elseToken.outputAdaptToHomework()).append("\n");
            builder.append(elseStmt.outputAdaptToHomework()).append("\n");
        }
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        cond.createSymbolTable(level, symbolTable, errors);
        stmt.createSymbolTable(level, symbolTable, errors);
        if (hasElseStmt()) {
            elseStmt.createSymbolTable(level, symbolTable, errors);
        }
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return true;
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
        stmt.dealWithErrorM(errors, isLoop);
        if (elseStmt != null) {
            elseStmt.dealWithErrorM(errors, isLoop);
        }
    }

    public List<IntermediateInstruction> generateIr(int level) {
        // if cond stmt else label_2 stmt label_1
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String label_1 = LabelGenerator.nextLabel();
        String label_2 = LabelGenerator.nextLabel();

        String resId = cond.generateIr(level, instructions);
        instructions.add(new BranchIfEqIr(resId, "#0", label_2));

        // TODO create code and insert tag
        if (stmt != null) {
            instructions.addAll(stmt.generateIr(level));
        }
        instructions.add(new JumpIr(label_1));
        instructions.add(new InsertLabelIr(label_2));
        if (elseStmt != null) {
            instructions.addAll(elseStmt.generateIr(level));
        }
        instructions.add(new InsertLabelIr(label_1));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_begin, String label_end) {
        // if cond stmt else label_2 stmt label_1
        List<IntermediateInstruction> instructions = new ArrayList<>();
        String label_1 = LabelGenerator.nextLabel();
        String label_2 = LabelGenerator.nextLabel();

        String resId = cond.generateIr(level, instructions);
        instructions.add(new BranchIfEqIr(resId, "#0", label_2));

        // TODO create code and insert tag
        if (stmt != null) {
            instructions.addAll(stmt.generateIr(level, label_begin, label_end));
        }
        instructions.add(new JumpIr(label_1));
        instructions.add(new InsertLabelIr(label_2));
        if (elseStmt != null) {
            instructions.addAll(elseStmt.generateIr(level, label_begin, label_end));
        }
        instructions.add(new InsertLabelIr(label_1));
        return instructions;
    }
}