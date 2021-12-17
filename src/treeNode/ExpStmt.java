package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        if (hasExp()) {
            builder.append(exp.outputAdaptToHomework()).append("\n");
        }
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        if (exp != null) {
            exp.createSymbolTable(level, symbolTable, errors);
        }
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return true;
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