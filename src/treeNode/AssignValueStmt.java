package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import ir.MovIr;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(lVal.outputAdaptToHomework()).append("\n");
        builder.append(assignToken.outputAdaptToHomework()).append("\n");
        builder.append(exp.outputAdaptToHomework()).append("\n");
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        dealWithErrorH(symbolTable, errors);
        lVal.createSymbolTable(level, symbolTable, errors);
        exp.createSymbolTable(level, symbolTable, errors);
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        return true;
    }

    public void dealWithErrorH(SymbolTable symbolTable, List<Error> errors) {
        if (lVal.isConstLVal(symbolTable)) {
            errors.add(new Error(lVal.getLineNumber(), "h"));
        }
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
