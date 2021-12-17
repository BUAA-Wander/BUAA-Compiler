package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import ir.JumpIr;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContinueStmt extends Stmt {
    private Continue continueToken;
    private Semicolon semicolon;

    public ContinueStmt(int num, Continue continueToken, Semicolon semicolon) {
        super(num, StmtType.CONTINUE);
        this.continueToken = continueToken;
        this.semicolon = semicolon;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(continueToken.outputAdaptToHomework()).append("\n");
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
            errors.add(new Error(continueToken.getLineNumber(), "m"));
        }
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        instructions.add(new JumpIr(label_1));
        return instructions;
    }
}
