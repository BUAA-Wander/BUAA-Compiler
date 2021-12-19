package treeNode;

import ir.GetintIr;
import ir.IntermediateInstruction;
import ir.MovIr;
import ir.StorePointerValueIr;
import ir.TmpVarGenerator;
import ir.utils.Immediate;
import ir.utils.Operand;
import ir.utils.TmpVariable;
import symbol.SymbolTable;
import treeNode.util.LValAnalyseMode;

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
        // 要用左值分析
        LValAnalyseMode.setAnalyseMode(true);
        Operand dst = lVal.generateIr(level, instructions);
        LValAnalyseMode.setAnalyseMode(false);
        Operand src = new TmpVariable(level, TmpVarGenerator.nextTmpVar(level), (level == 0));
        instructions.add(new GetintIr(src));
        if (lVal.isPointer(level)) {
            instructions.add(
                    new StorePointerValueIr(
                            dst, new Immediate(0), src, lVal.isPointer(0), true));
        } else {
            instructions.add(new MovIr(src, dst));
        }
        return instructions;
    }

    public List<IntermediateInstruction> generateIr(int level, String label_1, String label_2) {
        return generateIr(level);
    }
}
