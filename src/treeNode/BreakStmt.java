package treeNode;

import error.Error;
import ir.BranchIfEqIr;
import ir.InsertLabelIr;
import ir.IntermediateInstruction;
import ir.JumpIr;
import ir.LabelGenerator;
import ir.PassReturnValueIr;
import ir.ReturnIr;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BreakStmt extends Stmt {
    private Break breakToken;
    private Semicolon semicolon;
    // TODO

    public BreakStmt(int num, Break breakToken, Semicolon semicolon) {
        super(num, StmtType.BREAK);
        this.breakToken = breakToken;
        this.semicolon = semicolon;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(breakToken.outputAdaptToHomework()).append("\n");
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        // TODO check while
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return true;
    }

    public void dealWithErrorM(List<Error> errors, boolean isLoop) {
        if (!isLoop) {
            errors.add(new Error(breakToken.getLineNumber(), "m"));
        }
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        instructions.add(new JumpIr(label_2));
        return instructions;
    }
}
