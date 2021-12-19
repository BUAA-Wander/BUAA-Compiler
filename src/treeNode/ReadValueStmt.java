package treeNode;

import ir.GetintIr;
import ir.IntermediateInstruction;
import ir.utils.Operand;
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

    public List<IntermediateInstruction> generateIr(int level) {
        List<IntermediateInstruction> instructions = new ArrayList<>();
        Operand dst = lVal.generateIr(level, instructions);
        instructions.add(new GetintIr(dst));
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
