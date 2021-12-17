package treeNode;

import error.Error;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.PassReturnValueIr;
import ir.ReturnIr;
import symbol.SymbolTable;
import symbol.type.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(returnToken.outputAdaptToHomework()).append("\n");
        if (hasReturnExp()) {
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
        // TODO: is return valid?
    }

    public boolean dealWithErrorF(FuncType funcType, List<Error> errors) {
        if ((funcType == FuncType.VOID) && (exp != null)) {
            return false;
        }
        return true;
    }

    public boolean dealWithErrorG(FuncType funcType, List<Error> errors) {
//        if ((funcType == FuncType.INT) && (exp == null)) {
//            return false;
//        }
//        return true;
        return returnToken != null;
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
