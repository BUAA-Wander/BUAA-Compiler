package treeNode;

import error.Error;
import ir.GetintIr;
import ir.IntermediateInstruction;
import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ReadValueStmt extends Stmt {
    private LVal lVal;
    private Assign assignToken;
    private Getint getintToken;
    private LeftParent leftParent;
    private RightParent rightParent;
    private Semicolon semicolon;

    public ReadValueStmt(int num, LVal lVal, Assign assignToken, Getint getint, LeftParent leftParent,
                         RightParent rightParent, Semicolon semicolon) {
        super(num, StmtType.READ);
        this.lVal = lVal;
        this.assignToken = assignToken;
        this.getintToken = getint;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    public String outputAdaptToHomework() {
        StringBuilder builder = new StringBuilder();
        builder.append(lVal.outputAdaptToHomework()).append("\n");
        builder.append(assignToken.outputAdaptToHomework()).append("\n");
        builder.append(getintToken.outputAdaptToHomework()).append("\n");
        builder.append(leftParent.outputAdaptToHomework()).append("\n");
        builder.append(rightParent.outputAdaptToHomework()).append("\n");
        builder.append(semicolon.outputAdaptToHomework()).append("\n");
        builder.append("<Stmt>");
        return builder.toString();
    }

    public void createSymbolTable(int level, SymbolTable symbolTable
            , List<Error> errors) {
        dealWithErrorH(symbolTable, errors);

        lVal.createSymbolTable(level, symbolTable, errors);
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
        instructions.add(new GetintIr(dst));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
